package org.orderservice.mapper;

import org.orderservice.dto.product.ProductResponseDTO;
import org.orderservice.entity.OrderProduct;
import org.orderservice.entity.Product;

public class ProductMapper {
    public ProductResponseDTO getProductResponseFromOrderProduct(OrderProduct orderProduct)
    {
        Product product = orderProduct.getProduct();
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .amount(orderProduct.getAmount())
                .build();
    }
}
