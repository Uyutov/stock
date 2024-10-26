package org.productinventoryservice.mapper;

import org.productinventoryservice.dto.product.ProductDTO;
import org.productinventoryservice.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDTO getDTOFromProduct(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }
}
