package org.orderservice.service;

import jakarta.persistence.EntityNotFoundException;
import org.orderservice.dto.order.OrderCreationDTO;
import org.orderservice.dto.order.OrderRequestDTO;
import org.orderservice.dto.order.OrderResponseDTO;
import org.orderservice.dto.order.OrderStatusChangeDTO;
import org.orderservice.dto.user.UserDTO;
import org.orderservice.entity.Order;
import org.orderservice.entity.OrderProduct;
import org.orderservice.entity.Product;
import org.orderservice.entity.User;
import org.orderservice.entity.composite_key.OrderProductKey;
import org.orderservice.entity.enums.OrderState;
import org.orderservice.exception.NotEnoughProductException;
import org.orderservice.exception.WrongOrderStateException;
import org.orderservice.mapper.OrderMapper;
import org.orderservice.mapper.UserMapper;
import org.orderservice.repository.OrderProductRepository;
import org.orderservice.repository.OrderRepository;
import org.orderservice.repository.ProductRepository;
import org.orderservice.service.interfaces.OrderService;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final UserDetailsService userService;

    private final OrderMapper orderMapper;
    private final UserMapper userMapper;

    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange exchange;

    private final OrderState PACKAGING = OrderState.PACKAGING;

    private final String ORDER_NOT_FOUND_EXC = "Order with id %d not found";
    private final String NOT_ENOUGH_PRODUCT_EXC = "There is not enough of products to create order";
    private final String WRONG_ORDER_STATUS = "Wrong order status: %s";
    public OrderServiceImpl(OrderRepository orderRepository,
                            ProductRepository productRepository,
                            OrderProductRepository orderProductRepository,
                            UserDetailsService userService,
                            OrderMapper orderMapper,
                            UserMapper userMapper,
                            RabbitTemplate rabbitTemplate,
                            DirectExchange exchange) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderProductRepository = orderProductRepository;
        this.userService = userService;
        this.orderMapper = orderMapper;
        this.userMapper = userMapper;
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
    }

    @Override
    public OrderResponseDTO getOrder(OrderRequestDTO dto) {
        Order order = orderRepository.findById(dto.id()).orElseThrow(() -> {
            return new EntityNotFoundException(String.format(ORDER_NOT_FOUND_EXC, dto.id()));
        });

        OrderResponseDTO orderResponse = orderMapper.getResponseDtoFromOrder(order);
        return orderResponse;
    }

    @Override
    public Page<OrderResponseDTO> getOrdersPage(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);
        if (orders.getTotalElements() != 0) {
            Page<OrderResponseDTO> orderResponsePage = orders.map(order -> orderMapper.getResponseDtoFromOrder(order));
            return orderResponsePage;
        }
        return Page.empty();
    }

    @Override
    public OrderResponseDTO createOrder(OrderCreationDTO dto, Jwt jwt) {
        UserDTO userDTO = (UserDTO) userService.loadUserByUsername(jwt.getClaim("username"));

        User user = userMapper.getUserFromDTO(userDTO);

        Boolean canCreateOrder = (Boolean) rabbitTemplate.convertSendAndReceive(exchange.getName(), dto.products());

        if(canCreateOrder) {
            Order newOrder = Order.builder()
                    .user(user)
                    .state(PACKAGING)
                    .deliveryAddress(dto.deliveryAddress())
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
        }else {
            throw new NotEnoughProductException(NOT_ENOUGH_PRODUCT_EXC);
        }
    }

    @Override
    public OrderResponseDTO changeOrderStatus(OrderStatusChangeDTO dto) {
        Order order = orderRepository.findById(dto.id()).orElseThrow(() -> {
                    return new EntityNotFoundException(String.format(ORDER_NOT_FOUND_EXC, dto.id()));
                }
        );

        try {
            OrderState state = OrderState.valueOf(dto.status());

            order.setState(state);
            OrderResponseDTO response = orderMapper.getResponseDtoFromOrder(orderRepository.save(order));

            return response;
        }catch (IllegalArgumentException exception)
        {
            throw new WrongOrderStateException(String.format(WRONG_ORDER_STATUS, dto.status()));
        }
    }
}
