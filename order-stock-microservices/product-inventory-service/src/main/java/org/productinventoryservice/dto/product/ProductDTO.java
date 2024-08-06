package org.productinventoryservice.dto.product;

import lombok.Builder;

@Builder
public record ProductDTO(Long id, String name, Integer price) {
}
