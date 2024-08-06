package org.productinventoryservice.dto.product;

public record AddProductTransactionDTO(Long productId, Long warehouseId, Integer amount) {
}
