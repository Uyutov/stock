package org.orderservice.service;

import org.orderservice.dto.order.OrderCreationDTO;
import org.orderservice.dto.order.OrderResponseDTO;
import org.orderservice.dto.product.ProductResponseDTO;
import org.orderservice.dto.product.ProductTransactionDTO;
import org.orderservice.entity.Order;
import org.orderservice.entity.OrderProduct;
import org.orderservice.entity.User;
import org.orderservice.mapper.OrderMapper;
import org.orderservice.mapper.ProductMapper;
import org.orderservice.repository.OrderRepository;
import org.orderservice.repository.UserRepository;
import org.orderservice.service.interfaces.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductMapper productMapper;
    private final OrderMapper orderMapper;
    private final String PACKAGING = "PACKAGING";
    private final String DELIVERING = "DELIVERING";
    private final String AWAITING = "AWAITING";
    private final String RECEIVED = "RECEIVED";

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, ProductMapper productMapper, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productMapper = productMapper;
        this.orderMapper = orderMapper;
    }

    @Override
    public ResponseEntity<OrderResponseDTO> getOrder(Long id) {
        Optional<Order> orderEntry = orderRepository.findById(id);

        if (orderEntry.isPresent()) {
            Order order = orderEntry.get();
            OrderResponseDTO orderResponse = getResponseFromOrder(order);
            return ResponseEntity.of(Optional.of(orderResponse));
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Page<OrderResponseDTO>> getOrdersPage(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);
        if(orders.getTotalElements() != 0)
        {
            Page<OrderResponseDTO> orderResponsePage = orders.map(order -> getResponseFromOrder(order));
            return ResponseEntity.ok(orderResponsePage);
        }else
        {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<OrderResponseDTO> createOrder(OrderCreationDTO dto, Jwt jwt) {
        Optional<User> userEntry = userRepository.findUserByUsername(jwt.getClaimAsString("username"));
        if(userEntry.isPresent())
        {
            //TODO Check if there enough products in warehouse
            Order newOrder = Order.builder()
                    .user(userEntry.get())
                    .state(PACKAGING)
                    .deliveryAddress(dto.deliveryAddress())
                    .orderedProducts(getOrderedProductsFromDTO(dto.products()))
                    .build();
            Order order = orderRepository.save(newOrder);
            OrderResponseDTO response = getResponseFromOrder(order);

            //TODO Crete controller for orders and add link to newly created order
            //ResponseEntity.created(response, linkTo(
            //        methodOn(OrderController.class).getOrderById(order.getId())
            //).withSelfRel());

            return ResponseEntity.status(201).body(response);
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<Object> orderPackaged(Long id) {
        return changeOrderStatus(id, DELIVERING);
    }

    @Override
    public ResponseEntity<Object> orderDelivered(Long id) {
        return changeOrderStatus(id, AWAITING);
    }

    @Override
    public ResponseEntity<Object> orderReceived(Long id) {
        return changeOrderStatus(id, RECEIVED);
    }
    private ResponseEntity<Object> changeOrderStatus(Long id, String status)
    {
        Optional<Order> orderEntry = orderRepository.findById(id);
        if(orderEntry.isPresent())
        {
            Order packagedOrder = orderEntry.get();
            packagedOrder.setState(DELIVERING);
            orderRepository.save(packagedOrder);

            return ResponseEntity.status(204).build();
            //TODO after creation of controllers add link to updated order
            //return ResponseEntity.status(204)
            //        .location(linkTo(
            //                        methodOn(OrderController.class).getOrderById(packagedOrder.getId())
            //                ).withSelfRel());
        }else {
            return ResponseEntity.notFound().build();
        }
    }
    private OrderResponseDTO getResponseFromOrder(Order order)
    {
        List<ProductResponseDTO> orderedProducts = productMapper.getResponseFromOrderedProducts(order.getOrderedProducts());
        return orderMapper.getResponseDtoFromOrderAndProductsList(order, orderedProducts);
    }
    private List<OrderProduct> getOrderedProductsFromDTO(List<ProductTransactionDTO> dto)
    {
        return null;
    }
}
