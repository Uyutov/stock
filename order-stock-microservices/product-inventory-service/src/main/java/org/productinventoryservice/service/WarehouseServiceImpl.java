package org.productinventoryservice.service;

import jakarta.persistence.EntityNotFoundException;
import org.productinventoryservice.dto.warehouse.NewWarehouseDTO;
import org.productinventoryservice.dto.warehouse.WarehouseDTO;
import org.productinventoryservice.entity.Warehouse;
import org.productinventoryservice.exception.WarehouseException;
import org.productinventoryservice.mapper.WarehouseMapper;
import org.productinventoryservice.repository.WarehouseRepository;
import org.productinventoryservice.service.interfaces.WarehouseService;

import java.util.Optional;

public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;

    public WarehouseServiceImpl(WarehouseRepository warehouseRepository,
                                WarehouseMapper warehouseMapper) {
        this.warehouseRepository = warehouseRepository;
        this.warehouseMapper = warehouseMapper;
    }

    @Override
    public Warehouse getWarehouseById(Long id) {
        return warehouseRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Warehouse with productId " + id + " not found");
        });
    }

    @Override
    public WarehouseDTO createWarehouse(NewWarehouseDTO dto) {
        Optional<Warehouse> existingWarehouse = warehouseRepository.findByNameAndAddress(dto.name(), dto.address());
        if (existingWarehouse.isPresent()) {
            throw new WarehouseException("Cannot create duplicate warehouse with name " + dto.name() + " and address " + dto.address());
        }
        Warehouse newWarehouse = warehouseRepository.save(warehouseMapper.getWarehouseFromCreationDTO(dto));

        return warehouseMapper.getDTOFromWarehouse(newWarehouse);
    }

    @Override
    public WarehouseDTO updateWarehouse(WarehouseDTO dto) {
        Warehouse warehouse = warehouseRepository.findById(dto.id())
                .orElseThrow(() -> new EntityNotFoundException("Cannot update unexisting warehouse with requested id " + dto.id()));

        warehouse.setAddress(dto.address());
        warehouse.setName(dto.name());

        Warehouse updatedWarehouse = warehouseRepository.save(warehouse);

        return warehouseMapper.getDTOFromWarehouse(updatedWarehouse);
    }
}
