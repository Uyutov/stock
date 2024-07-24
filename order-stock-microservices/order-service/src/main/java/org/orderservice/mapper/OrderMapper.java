package org.orderservice.mapper;

import org.orderservice.dto.order.OrderResponseDTO;
import org.orderservice.dto.product.ProductResponseDTO;
import org.orderservice.entity.Order;

import java.util.List;

public class OrderMapper {
    public OrderResponseDTO getResponseDtoFromOrder(Order order) {
        return OrderResponseDTO.builder()
                .id(order.getId())
                .state(order.getState().toString())
                .build();
    }
}
