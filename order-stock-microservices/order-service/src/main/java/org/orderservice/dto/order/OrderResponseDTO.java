package org.orderservice.dto.order;

import lombok.Builder;
import org.orderservice.dto.product.ProductResponseDTO;

import java.util.List;

@Builder
public record OrderResponseDTO(Long id, String state, List<ProductResponseDTO> products, String deliveryAddress) {
}
