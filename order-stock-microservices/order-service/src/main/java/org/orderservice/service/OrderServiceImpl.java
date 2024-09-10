package org.orderservice.service;

import jakarta.persistence.EntityNotFoundException;
import org.orderservice.dao.OrderDao;
import org.orderservice.dto.order.OrderCreationDTO;
import org.orderservice.dto.order.OrderResponseDTO;
import org.orderservice.dto.user.UserDTO;
import org.orderservice.entity.Order;
import org.orderservice.entity.OrderProduct;
import org.orderservice.entity.Product;
import org.orderservice.entity.User;
import org.orderservice.entity.composite_key.OrderProductKey;
import org.orderservice.entity.enums.FilterOptions;
import org.orderservice.entity.enums.OrderState;
import org.orderservice.exception.NotEnoughProductException;
import org.orderservice.exception.WrongOrderStateException;
import org.orderservice.mapper.OrderMapper;
import org.orderservice.mapper.RoleToOrderStateMapper;
import org.orderservice.mapper.UserMapper;
import org.orderservice.repository.OrderProductRepository;
import org.orderservice.repository.OrderRepository;
import org.orderservice.repository.ProductRepository;
import org.orderservice.service.interfaces.OrderService;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderDao orderDao;
    private final UserDetailsService userService;

    private final OrderMapper orderMapper;
    private final UserMapper userMapper;
    private final RoleToOrderStateMapper roleToOrderStateMapper;

    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange exchange;

    private final OrderState PACKAGING = OrderState.PACKAGING;

    private final String ORDER_NOT_FOUND_EXC = "Order with id %d not found";
    private final String NOT_ENOUGH_PRODUCT_EXC = "There is not enough of products to create order";
    private final String WRONG_ORDER_STATUS = "Wrong order status: %s";

    public OrderServiceImpl(OrderRepository orderRepository,
                            ProductRepository productRepository,
                            OrderProductRepository orderProductRepository,
                            OrderDao orderDao,
                            UserDetailsService userService,
                            OrderMapper orderMapper,
                            UserMapper userMapper,
                            RoleToOrderStateMapper roleToOrderStateMapper,
                            RabbitTemplate rabbitTemplate,
                            DirectExchange exchange) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderProductRepository = orderProductRepository;
        this.orderDao = orderDao;
        this.userService = userService;
        this.orderMapper = orderMapper;
        this.userMapper = userMapper;
        this.roleToOrderStateMapper = roleToOrderStateMapper;
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
    }

    @Override
    public OrderResponseDTO getOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException(String.format(ORDER_NOT_FOUND_EXC, id));
        });

        OrderResponseDTO orderResponse = orderMapper.getResponseDtoFromOrder(order);
        return orderResponse;
    }

    @Override
    public Page<OrderResponseDTO> getOrdersPage(Pageable pageable, FilterOptions filterOption, String filterValue) {
        Page<Order> orders = orderRepository.findAll(pageable);
        if (orders.getTotalElements() != 0) {
            if(filterOption == null)
            {
                return orders.map(order -> orderMapper.getResponseDtoFromOrder(order));
            }
            return orderRepository.findAll(orderDao.getOrdersPage(filterOption, filterValue), pageable)
                    .map(order -> orderMapper.getResponseDtoFromOrder(order));
        }
        return Page.empty();
    }

    @Override
    public OrderResponseDTO createOrder(OrderCreationDTO dto) {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = jwt.getClaimAsString("preferred_username");
        UserDTO userDTO = (UserDTO) userService.loadUserByUsername(username);

        User user = userMapper.getUserFromDTO(userDTO);

        Boolean canCreateOrder = (Boolean) rabbitTemplate.convertSendAndReceive(exchange.getName(), "check", dto.products());

        if (canCreateOrder) {
            Integer totalPrice = dto.products().stream().mapToInt(
                            product -> product.amount() * productRepository.findById(product.id()).get().getPrice())
                    .sum();

            Order newOrder = Order.builder()
                    .user(user)
                    .state(PACKAGING)
                    .deliveryAddress(dto.deliveryAddress())
                    .totalPrice(totalPrice)
                    .build();

            Order order = orderRepository.save(newOrder);

            List<OrderProduct> orderProducts = new ArrayList<>();

            for (var productDTO : dto.products()) {
                Product product = productRepository.findById(productDTO.id()).orElseThrow(() ->
                        new EntityNotFoundException(String.format(ORDER_NOT_FOUND_EXC, productDTO.id()))
                );
                orderProducts.add(
                        new OrderProduct(
                                new OrderProductKey(order.getId(), product.getId()),
                                productDTO.amount(),
                                order,
                                product)
                );
            }

            order.setProducts(orderProductRepository.saveAll(orderProducts));

            OrderResponseDTO response = orderMapper.getResponseDtoFromOrder(order);

            return response;
        } else {
            throw new NotEnoughProductException(NOT_ENOUGH_PRODUCT_EXC);
        }
    }

    @Override
    public OrderResponseDTO changeOrderStatus(Long id) {
        var authorities = (List<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        OrderState state = roleToOrderStateMapper.getOrderSateByRole(authorities);

        Order order = orderRepository.findById(id).orElseThrow(() -> {
                    return new EntityNotFoundException(String.format(ORDER_NOT_FOUND_EXC, id));
                }
        );

        try {
            order.setState(state);
            OrderResponseDTO response = orderMapper.getResponseDtoFromOrder(orderRepository.save(order));

            return response;
        } catch (IllegalArgumentException exception) {
            throw new WrongOrderStateException(String.format(WRONG_ORDER_STATUS, state));
        }
    }
}
