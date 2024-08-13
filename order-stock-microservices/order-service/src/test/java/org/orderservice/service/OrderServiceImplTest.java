package org.orderservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orderservice.dto.order.OrderCreationDTO;
import org.orderservice.dto.order.OrderRequestDTO;
import org.orderservice.dto.order.OrderResponseDTO;
import org.orderservice.dto.order.OrderStatusChangeDTO;
import org.orderservice.dto.product.ProductResponseDTO;
import org.orderservice.dto.product.ProductTransactionDTO;
import org.orderservice.dto.user.UserDTO;
import org.orderservice.entity.Order;
import org.orderservice.entity.OrderProduct;
import org.orderservice.entity.Product;
import org.orderservice.entity.User;
import org.orderservice.entity.composite_key.OrderProductKey;
import org.orderservice.entity.enums.OrderState;
import org.orderservice.mapper.OrderMapper;
import org.orderservice.mapper.ProductMapper;
import org.orderservice.mapper.UserMapper;
import org.orderservice.repository.OrderProductRepository;
import org.orderservice.repository.OrderRepository;
import org.orderservice.repository.ProductRepository;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderProductRepository orderProductRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserDetailsService userService;

    @Mock
    private OrderMapper orderMapper;
    @Mock
    private UserMapper userMapper;

    @Mock
    private RabbitTemplate rabbitTemplate;
    @Mock DirectExchange exchange;

    @InjectMocks
    private OrderServiceImpl orderService;

    private User user;
    private Order order;
    private Product apple;
    private Product bottle;
    private List<OrderProduct> orderedProducts;

    private UserDTO userDTO;
    private OrderResponseDTO orderResponse;
    private ProductResponseDTO appleProductResponse;
    private ProductResponseDTO bottleProductResponse;

    @Value("${rabbitmq.exchange}")
    private String exchangeName;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .id("1")
                .email("Vladimir@gmail.com")
                .username("Vladimir")
                .build();

        order = Order.builder()
                .id(1L)
                .state(OrderState.PACKAGING)
                .deliveryAddress("Golubeva")
                .user(user)
                .build();

        apple = Product.builder()
                .id(1L)
                .name("Apple")
                .price(10)
                .build();

        bottle = Product.builder()
                .id(2L)
                .name("Bottle")
                .price(20)
                .build();

        OrderProductKey appleOrderProductKey = new OrderProductKey(order.getId(), apple.getId());
        OrderProductKey bottleOrderProductKey = new OrderProductKey(order.getId(), bottle.getId());

        orderedProducts = List.of(
                new OrderProduct(appleOrderProductKey, 5, order, apple),
                new OrderProduct(bottleOrderProductKey, 10, order, bottle)
        );

        order.setProducts(orderedProducts);

        userDTO = UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();

        appleProductResponse = ProductResponseDTO.builder()
                .id(apple.getId())
                .name(apple.getName())
                .price(apple.getPrice())
                .amount(orderedProducts.get(0).getAmount())
                .build();

        bottleProductResponse = ProductResponseDTO.builder()
                .id(bottle.getId())
                .name(bottle.getName())
                .price(bottle.getPrice())
                .amount(orderedProducts.get(1).getAmount())
                .build();

        orderResponse = OrderResponseDTO.builder()
                .id(order.getId())
                .state(order.getState().toString())
                .deliveryAddress(order.getDeliveryAddress())
                .products(List.of(
                        appleProductResponse,
                        bottleProductResponse
                ))
                .build();

        exchange = new DirectExchange(exchangeName);
    }

    @Test
    void getOrder() {
        Long id = 1L;

        Mockito.when(orderRepository.findById(id)).thenReturn(Optional.of(order));
        Mockito.when(orderMapper.getResponseDtoFromOrder(order)).thenReturn(orderResponse);

        OrderResponseDTO response = orderService.getOrder(id);

        assertThat(response).isEqualTo(orderResponse);
    }

    @Test
    void getOrdersPage() {
        Pageable pageable = PageRequest.of(0, 1);
        Page<Order> ordersPage = new PageImpl<>(List.of(order), pageable, 1);
        Page<OrderResponseDTO> orderResponseDTOPage = new PageImpl<>(List.of(orderResponse), pageable, 1);

        Mockito.when(orderRepository.findAll(pageable)).thenReturn(ordersPage);
        Mockito.when(orderMapper.getResponseDtoFromOrder(order)).thenReturn(orderResponse);

        Page<OrderResponseDTO> response = orderService.getOrdersPage(pageable);

        assertThat(response).isEqualTo(orderResponseDTOPage);
        assertThat(response.toList()).isEqualTo(List.of(orderResponse));

        OrderResponseDTO actualOrderFromPage = response.toList().get(0);
        assertThat(actualOrderFromPage).isEqualTo(orderResponse);
        assertThat(actualOrderFromPage.products()).contains(appleProductResponse);
        assertThat(actualOrderFromPage.products()).contains(bottleProductResponse);
    }

    @Test
    void createOrder() {
        /*Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("username", "Vladimir").build();*/
        String username = "Vladimir";

        ProductTransactionDTO requestedApple = new ProductTransactionDTO(1L, 5);
        ProductTransactionDTO requestedBottle = new ProductTransactionDTO(2L, 10);

        OrderCreationDTO orderCreationDTO = new OrderCreationDTO(
                "Golubeva",
                List.of(requestedApple, requestedBottle)
        );

        Order newOrder = Order.builder()
                .user(user)
                .deliveryAddress(orderCreationDTO.deliveryAddress())
                .state(OrderState.PACKAGING)
                .build();

        OrderProduct orderedApple = new OrderProduct(
                new OrderProductKey(order.getId(), apple.getId()),
                requestedApple.amount(),
                order,
                apple
        );
        OrderProduct orderedBottle = new OrderProduct(
                new OrderProductKey(order.getId(), bottle.getId()),
                requestedBottle.amount(),
                order,
                bottle
        );
        List<OrderProduct> collectedProductsInOrder = List.of(orderedApple, orderedBottle);


        Mockito.when(userService.loadUserByUsername(username)).thenReturn(userDTO);
        Mockito.when(userMapper.getUserFromDTO(userDTO)).thenReturn(user);

        Mockito.when(orderRepository.save(newOrder)).thenReturn(order);

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(apple));
        Mockito.when(productRepository.findById(2L)).thenReturn(Optional.of(bottle));

        Mockito.when(orderProductRepository.saveAll(collectedProductsInOrder)).thenReturn(orderedProducts);

        Mockito.when(orderMapper.getResponseDtoFromOrder(order)).thenReturn(orderResponse);

        Mockito.when(rabbitTemplate.convertSendAndReceive(exchange.getName(), orderCreationDTO.products())).thenReturn(true);

        OrderResponseDTO response = orderService.createOrder(orderCreationDTO, username);

        assertThat(response).isEqualTo(orderResponse);
        assertThat(response.products()).contains(appleProductResponse);
        assertThat(response.products()).contains(bottleProductResponse);
    }

    @Test
    void changeOrderStatus() {
        Long id = 1L;
        OrderState state = OrderState.DELIVERING;

        Order orderWithNewState = Order.builder()
                .id(order.getId())
                .deliveryAddress(order.getDeliveryAddress())
                .products(order.getProducts())
                .user(order.getUser())
                .state(OrderState.DELIVERING)
                .build();

        OrderResponseDTO orderResponseWithNewState = OrderResponseDTO.builder()
                .id(orderResponse.id())
                .state(orderWithNewState.getState().toString())
                .deliveryAddress(orderResponse.deliveryAddress())
                .products(orderResponse.products())
                .build();

        Mockito.when(orderRepository.findById(id)).thenReturn(Optional.of(order));
        Mockito.when(orderRepository.save(orderWithNewState)).thenReturn(orderWithNewState);
        Mockito.when(orderMapper.getResponseDtoFromOrder(orderWithNewState)).thenReturn(orderResponseWithNewState);

        OrderResponseDTO response = orderService.changeOrderStatus(id, state);

        assertThat(response).isEqualTo(orderResponseWithNewState);
        assertThat(response.state()).isEqualTo(OrderState.DELIVERING.toString());
    }
}