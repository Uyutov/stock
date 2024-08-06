package org.orderservice.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orderservice.dto.product.ProductResponseDTO;
import org.orderservice.entity.Order;
import org.orderservice.entity.OrderProduct;
import org.orderservice.entity.Product;
import org.orderservice.entity.User;
import org.orderservice.entity.composite_key.OrderProductKey;
import org.orderservice.entity.enums.OrderState;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ProductMapperTest {
    @InjectMocks
    ProductMapper productMapper;

    private OrderProduct orderedProduct;
    private ProductResponseDTO productResponseDTO;
    @BeforeEach
    public void setUp(){
        User user = User.builder()
                .id("1")
                .email("Vladimir@gmail.com")
                .username("Vladimir")
                .build();

        Product product = Product.builder()
                .id(1L)
                .name("apple")
                .price(3)
                .build();

        Order order = Order.builder()
                .id(1L)
                .state(OrderState.PACKAGING)
                .user(user)
                .deliveryAddress("Golubeva")
                .build();

        OrderProductKey orderProductKey = new OrderProductKey(order.getId(), product.getId());

        orderedProduct = new OrderProduct(orderProductKey, 5, order, product);

        productResponseDTO = ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .amount(orderedProduct.getAmount())
                .build();
    }
    @Test
    void getProductResponseFromOrderProduct() {
        ProductResponseDTO response = productMapper.getProductResponseFromOrderProduct(orderedProduct);

        assertThat(response).isEqualTo(productResponseDTO);
    }
}