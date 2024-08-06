package org.productinventoryservice.dto.product;

import org.productinventoryservice.dto.warehouse.WarehouseDTO;

public record NewProductDTO(String name, Integer price, Integer amount, WarehouseDTO warehouse) {
}
