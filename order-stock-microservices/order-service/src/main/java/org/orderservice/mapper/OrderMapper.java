package org.orderservice.mapper;

import org.orderservice.dto.order.OrderResponseDTO;
import org.orderservice.dto.product.ProductResponseDTO;
import org.orderservice.entity.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {
    private final ProductMapper productMapper;

    public OrderMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public OrderResponseDTO getResponseDtoFromOrder(Order order) {
        List<ProductResponseDTO> orderedProducts = order.getProducts().stream()
                .map(orderedProduct -> {
                    return productMapper.getProductResponseFromOrderProduct(orderedProduct);
                }).toList();

        return OrderResponseDTO.builder()
                .id(order.getId())
                .state(order.getState().toString())
                .deliveryAddress(order.getDeliveryAddress())
                .totalPrice(order.getTotalPrice())
                .products(orderedProducts)
                .build();
    }
}
