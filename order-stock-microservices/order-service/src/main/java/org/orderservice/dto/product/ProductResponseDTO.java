package org.orderservice.dto.product;

import lombok.Builder;

@Builder
public record ProductResponseDTO(Long id, String name, Integer price, Integer amount) {
}
