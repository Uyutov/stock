package org.productinventoryservice.dto.product;

import lombok.Builder;
import org.productinventoryservice.dto.warehouse.WarehouseDTO;

@Builder
public record NewProductDTO(String name, Integer price, Integer amount, WarehouseDTO warehouse) {
}
