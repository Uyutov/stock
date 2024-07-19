package org.orderservice.service;

import jakarta.persistence.EntityNotFoundException;
import org.orderservice.dto.order.OrderCreationDTO;
import org.orderservice.dto.order.OrderResponseDTO;
import org.orderservice.dto.product.ProductTransactionDTO;
import org.orderservice.dto.user.UserDTO;
import org.orderservice.entity.Order;
import org.orderservice.entity.OrderProduct;
import org.orderservice.entity.Product;
import org.orderservice.entity.User;
import org.orderservice.entity.enums.OrderState;
import org.orderservice.exception.OrderMappingException;
import org.orderservice.exception.UserMappingException;
import org.orderservice.mapper.OrderMapper;
import org.orderservice.mapper.UserMapper;
import org.orderservice.repository.OrderRepository;
import org.orderservice.repository.ProductRepository;
import org.orderservice.service.interfaces.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserDetailServiceImpl userService;
    private final OrderMapper orderMapper;
    private final UserMapper userMapper;

    public OrderServiceImpl(OrderRepository orderRepository,
                            ProductRepository productRepository,
                            UserDetailServiceImpl userService,
                            OrderMapper orderMapper,
                            UserMapper userMapper) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userService = userService;
        this.orderMapper = orderMapper;
        this.userMapper = userMapper;
    }

    @Override
    public OrderResponseDTO getOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Order with id " + id + " not found");
        });

        OrderResponseDTO orderResponse = validateBeforeMappingFromOrderToDTO(order);
        return orderResponse;
    }

    @Override
    public Page<OrderResponseDTO> getOrdersPage(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);
        if (orders.getTotalElements() != 0) {
            Page<OrderResponseDTO> orderResponsePage = orders.map(order -> validateBeforeMappingFromOrderToDTO(order));
            return orderResponsePage;
        }
        return Page.empty();
    }

    @Override
    public OrderResponseDTO createOrder(OrderCreationDTO dto, Jwt jwt) {
        UserDTO userDTO = (UserDTO) userService.loadUserByUsername(jwt.getClaim("username"));

        User user = validateBeforeMappingFromDTOToUser(userDTO);

        Order newOrder = Order.builder()
                .user(user)
                .state(OrderState.PACKAGING)
                .deliveryAddress(dto.deliveryAddress())
                .orderedProducts(getOrderedProductsFromDTO(dto.products()))
                .build();

        Order order = orderRepository.save(newOrder);
        OrderResponseDTO response = validateBeforeMappingFromOrderToDTO(order);

        return response;
    }

    @Override
    public OrderResponseDTO changeOrderStatus(Long id, OrderState state) {
        Order order = orderRepository.findById(id).orElseThrow(() -> {
                    return new EntityNotFoundException("Order with id " + id + " not found");
                }
        );

        order.setState(state);
        OrderResponseDTO response = validateBeforeMappingFromOrderToDTO(orderRepository.save(order));

        return response;
    }

    private List<OrderProduct> getOrderedProductsFromDTO(List<ProductTransactionDTO> productsDTOs) {
        List<OrderProduct> orderProducts = new ArrayList<>();

        for (var dto : productsDTOs) {
            Product product = productRepository.findById(dto.id()).orElseThrow(() ->
                    new EntityNotFoundException("Order with id " + dto.id() + " not found")
            );
            orderProducts.add(new OrderProduct(product, dto.amount()));
        }

        return orderProducts;
    }

    private OrderResponseDTO validateBeforeMappingFromOrderToDTO(Order order) {
        if (order == null) {
            throw new OrderMappingException("Order must be present to map it");
        }

        if (order.getOrderedProducts() == null || order.getOrderedProducts().isEmpty()) {
            throw new OrderMappingException("Order cannot be mapped without ordered products");
        }
        return  orderMapper.getResponseDtoFromOrder(order);
    }

    private User validateBeforeMappingFromDTOToUser(UserDTO dto) {
        if (dto == null)
        {
            throw new UserMappingException("User dto must be present to map it");
        }

        return userMapper.getUserFromDTO(dto);
    }

}
