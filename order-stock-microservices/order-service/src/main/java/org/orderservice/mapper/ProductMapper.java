package org.orderservice.mapper;

import org.orderservice.dto.product.ProductResponseDTO;
import org.orderservice.dto.product.ProductTransactionDTO;
import org.orderservice.entity.OrderProduct;
import org.orderservice.entity.Product;
import org.orderservice.exception.ProductMapperException;

import java.util.List;

public class ProductMapper {
    public ProductResponseDTO getResponseFromOrderedProducts(OrderProduct orderProduct) {
        if (orderProduct == null) {
            throw new ProductMapperException("List of ordered products cannot be empty");
        }

        Product product = orderProduct.getProduct();

        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .amount(orderProduct.getAmount())
                .build();
    }
}
