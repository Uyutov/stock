package org.productinventoryservice.controller;

import org.productinventoryservice.dto.warehouse.NewWarehouseDTO;
import org.productinventoryservice.dto.warehouse.WarehouseDTO;
import org.productinventoryservice.dto.warehouse.WarehouseRequestDTO;
import org.productinventoryservice.service.interfaces.WarehouseService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/warehouse")
public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping("/id")
    public ResponseEntity<WarehouseDTO> getWarehouseById(WarehouseRequestDTO dto)
    {
        return ResponseEntity.ok(warehouseService.getWarehouseDTOById(dto));
    }

    @PostMapping("/create-warehouse")
    public ResponseEntity<WarehouseDTO> createWarehouse(NewWarehouseDTO dto)
    {
        return new ResponseEntity<>(warehouseService.createWarehouse(dto), HttpStatusCode.valueOf(201));
    }

    @PatchMapping("/update-warehouse")
    public ResponseEntity<WarehouseDTO> updateWarehouse(WarehouseDTO dto)
    {
        return new ResponseEntity(warehouseService.updateWarehouse(dto), HttpStatusCode.valueOf(200));
    }

}
