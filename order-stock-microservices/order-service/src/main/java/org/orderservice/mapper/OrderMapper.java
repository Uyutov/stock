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
        if (order == null) {
            throw new OrderMappingException("Order must be present to map it");
        }

        List<ProductResponseDTO> orderedProducts = order.getOrderedProducts()
                .stream().map(
                        product -> productMapper.getResponseFromOrderedProducts(product)
                ).toList();

        if (orderedProducts == null || orderedProducts.isEmpty()) {
            throw new OrderMappingException("Order cannot be mapped without ordered products");
        }

        return OrderResponseDTO.builder()
                .id(order.getId())
                .state(order.getState().toString())
                .products(orderedProducts).build();
    }
}
