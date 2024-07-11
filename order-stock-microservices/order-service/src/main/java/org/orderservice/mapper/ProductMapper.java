package org.orderservice.mapper;

import org.orderservice.dto.product.ProductResponseDTO;
import org.orderservice.entity.OrderProduct;
import org.orderservice.entity.Product;

import java.util.List;

public class ProductMapper {
    public List<ProductResponseDTO> getResponseFromOrderedProducts(List<OrderProduct> orderedProducts) {
        if (orderedProducts == null || orderedProducts.isEmpty()) {
            throw new NullPointerException("List of ordered products cannot be empty");
        }

        List<ProductResponseDTO> products = orderedProducts.stream().map(orderedProduct -> {
            Product product = orderedProduct.getProduct();

            return ProductResponseDTO.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .amount(orderedProduct.getAmount())
                    .build();
        }).toList();

        return products;
    }
}
