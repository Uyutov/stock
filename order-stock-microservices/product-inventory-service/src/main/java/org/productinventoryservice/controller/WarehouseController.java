package org.productinventoryservice.controller;

import org.productinventoryservice.dto.warehouse.NewWarehouseDTO;
import org.productinventoryservice.dto.warehouse.WarehouseDTO;
import org.productinventoryservice.service.interfaces.WarehouseService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/warehouse")
public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseDTO> getWarehouseById(@PathVariable("id") Long id)
    {
        return ResponseEntity.ok(warehouseService.getWarehouseDTOById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<WarehouseDTO> createWarehouse(@RequestBody NewWarehouseDTO dto)
    {
        return new ResponseEntity<>(warehouseService.createWarehouse(dto), HttpStatusCode.valueOf(201));
    }

    @PatchMapping("/update")
    public ResponseEntity<WarehouseDTO> updateWarehouse(@RequestBody WarehouseDTO dto)
    {
        return new ResponseEntity(warehouseService.updateWarehouse(dto), HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Object> deleteWarehouse(@PathVariable("id") Long id)
    {
        warehouseService.deleteWarehouse(id);
        return ResponseEntity.ok().build();
    }
}
