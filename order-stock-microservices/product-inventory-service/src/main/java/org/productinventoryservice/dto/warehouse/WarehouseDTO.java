package org.productinventoryservice.dto.warehouse;

import lombok.Builder;

@Builder
public record WarehouseDTO(Long id, String address, String name) {
}
