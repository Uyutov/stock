package org.productinventoryservice.mapper;

import org.productinventoryservice.dto.warehouse.NewWarehouseDTO;
import org.productinventoryservice.dto.warehouse.WarehouseDTO;
import org.productinventoryservice.entity.Warehouse;
import org.springframework.stereotype.Component;

@Component
public class WarehouseMapper {

    public Warehouse getWarehouseFromCreationDTO(NewWarehouseDTO dto) {
        return Warehouse.builder()
                .address(dto.address())
                .name(dto.name())
                .build();
    }

    public WarehouseDTO getDTOFromWarehouse(Warehouse warehouse) {
        return WarehouseDTO.builder()
                .id(warehouse.getId())
                .name(warehouse.getName())
                .address(warehouse.getAddress())
                .build();
    }
}
