package org.orderservice.mapper;

import org.orderservice.dto.order.OrderResponseDTO;
import org.orderservice.dto.product.ProductResponseDTO;
import org.orderservice.entity.Order;
import org.orderservice.exception.OrderMappingException;

import java.util.List;

public class OrderMapper {
    private final ProductMapper productMapper;

    public OrderMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public OrderResponseDTO getResponseDtoFromOrder(Order order) {
        List<ProductResponseDTO> orderedProducts = order.getOrderedProducts()
                .stream().map(
                        product -> productMapper.getResponseFromOrderedProducts(product)
                ).toList();

        return OrderResponseDTO.builder()
                .id(order.getId())
                .state(order.getState().toString())
                .products(orderedProducts).build();
    }
}
