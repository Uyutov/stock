package org.orderservice.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orderservice.dto.order.OrderResponseDTO;
import org.orderservice.dto.product.ProductResponseDTO;
import org.orderservice.entity.Order;
import org.orderservice.entity.OrderProduct;
import org.orderservice.entity.Product;
import org.orderservice.entity.User;
import org.orderservice.entity.composite_key.OrderProductKey;
import org.orderservice.entity.enums.OrderState;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class OrderMapperTest {

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private OrderMapper orderMapper;

    private User user;
    private Order order;
    private OrderProduct orderedApples;
    private OrderProduct orderedBottles;
    private ProductResponseDTO appleDTO;
    private ProductResponseDTO bottleDTO;
    private OrderResponseDTO orderDTO;


    @BeforeEach
    public void setUp() {
        user = User.builder()
                .id("1")
                .email("Vladimir@gmail.com")
                .username("Vladimir")
                .build();

        Product apple = Product.builder()
                .id(1L)
                .name("apple")
                .price(3)
                .build();

        Product bottle = Product.builder()
                .id(2L)
                .name("bottle")
                .price(5)
                .build();

        order = Order.builder()
                .id(1L)
                .state(OrderState.PACKAGING)
                .deliveryAddress("Golubeva")
                .user(user)
                .build();

        OrderProductKey appleProductKey = new OrderProductKey(order.getId(), apple.getId());
        OrderProductKey bottleProductKey = new OrderProductKey(order.getId(), bottle.getId());

        orderedApples = new OrderProduct(appleProductKey, 5, order, apple);
        orderedBottles = new OrderProduct(bottleProductKey, 10, order, bottle);

        order.setProducts(List.of(orderedApples, orderedBottles));

        appleDTO = ProductResponseDTO.builder()
                .id(1L)
                .name("apple")
                .amount(5)
                .price(3)
                .build();

        bottleDTO = ProductResponseDTO.builder()
                .id(2L)
                .name("bottle")
                .amount(10)
                .price(5)
                .build();

        orderDTO = OrderResponseDTO.builder()
                .id(1L)
                .state(order.getState().toString())
                .products(List.of(appleDTO, bottleDTO))
                .deliveryAddress(order.getDeliveryAddress())
                .build();
    }

    @Test
    void getResponseDtoFromOrder() {
        Mockito.when(productMapper.getProductResponseFromOrderProduct(orderedApples)).thenReturn(appleDTO);
        Mockito.when(productMapper.getProductResponseFromOrderProduct(orderedBottles)).thenReturn(bottleDTO);

        OrderResponseDTO response = orderMapper.getResponseDtoFromOrder(order);

        assertThat(response).isEqualTo(orderDTO);
    }
}