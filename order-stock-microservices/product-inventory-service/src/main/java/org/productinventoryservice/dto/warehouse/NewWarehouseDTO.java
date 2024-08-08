package org.productinventoryservice.dto.warehouse;

import lombok.Builder;

@Builder
public record NewWarehouseDTO(String address, String name) {
}
