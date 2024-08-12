package org.productinventoryservice.service.interfaces;

import org.productinventoryservice.dto.warehouse.NewWarehouseDTO;
import org.productinventoryservice.dto.warehouse.WarehouseDTO;
import org.productinventoryservice.dto.warehouse.WarehouseRequestDTO;
import org.productinventoryservice.entity.Warehouse;

public interface WarehouseService {
    public Warehouse getWarehouseById(Long id);
    public WarehouseDTO getWarehouseDTOById(Long id);
    public WarehouseDTO createWarehouse(NewWarehouseDTO dto);
    public WarehouseDTO updateWarehouse(WarehouseDTO dto);
    public void deleteWarehouse(Long id);
}
