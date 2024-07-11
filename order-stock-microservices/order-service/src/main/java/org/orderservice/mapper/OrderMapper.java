package org.orderservice.mapper;

import org.orderservice.dto.order.OrderCreationDTO;
import org.orderservice.dto.order.OrderResponseDTO;
import org.orderservice.dto.product.ProductResponseDTO;
import org.orderservice.entity.Order;
import org.orderservice.entity.User;

import java.util.List;

public class OrderMapper {
    public OrderResponseDTO getResponseDtoFromOrderAndProductsList(Order order, List<ProductResponseDTO> orderedProducts) {
        if (order == null) {
            throw new NullPointerException("Order must be present to map it");
        }

        if (orderedProducts == null || orderedProducts.isEmpty()) {
            throw new NullPointerException("Order cannot be mapped without ordered products");
        }

        return OrderResponseDTO.builder()
                .Id(order.getId())
                .state(order.getState())
                .products(orderedProducts).build();
    }
}
