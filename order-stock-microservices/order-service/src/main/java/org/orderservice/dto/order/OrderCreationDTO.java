package org.orderservice.dto.order;

import org.orderservice.dto.product.ProductTransactionDTO;

import java.util.List;

public record OrderCreationDTO(String deliveryAddress, List<ProductTransactionDTO> products) {
}
