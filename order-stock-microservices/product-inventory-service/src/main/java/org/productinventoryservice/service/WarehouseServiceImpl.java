package org.productinventoryservice.service;

import jakarta.persistence.EntityNotFoundException;
import org.productinventoryservice.dto.warehouse.NewWarehouseDTO;
import org.productinventoryservice.dto.warehouse.WarehouseDTO;
import org.productinventoryservice.entity.Warehouse;
import org.productinventoryservice.exception.RepeatedWarehouseCreationException;
import org.productinventoryservice.mapper.WarehouseMapper;
import org.productinventoryservice.repository.WarehouseRepository;
import org.productinventoryservice.service.interfaces.WarehouseService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;

    private final String UPDATING_UNEXISTING_WAREHOUSE_EXC = "Cannot update unexisting warehouse with requested id ";
    private final String CREATION_OF_DUPLICATE_WAREHOUSE_EXC = "Cannot create duplicate warehouse with address ";
    private final String WAREHOUSE_NOT_FOUND_EXC = "Could not find warehouse with id ";
    public WarehouseServiceImpl(WarehouseRepository warehouseRepository,
                                WarehouseMapper warehouseMapper) {
        this.warehouseRepository = warehouseRepository;
        this.warehouseMapper = warehouseMapper;
    }

    @Override
    public Warehouse getWarehouseById(Long id) {
        return warehouseRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException(WAREHOUSE_NOT_FOUND_EXC + id);
        });
    }

    @Override
    public WarehouseDTO createWarehouse(NewWarehouseDTO dto) {
        Optional<Warehouse> existingWarehouse = warehouseRepository.findByAddress(dto.address());
        if (existingWarehouse.isPresent()) {
            throw new RepeatedWarehouseCreationException(CREATION_OF_DUPLICATE_WAREHOUSE_EXC + dto.address());
        }
        Warehouse newWarehouse = warehouseRepository.save(warehouseMapper.getWarehouseFromCreationDTO(dto));

        return warehouseMapper.getDTOFromWarehouse(newWarehouse);
    }

    @Override
    public WarehouseDTO updateWarehouse(WarehouseDTO dto) {
        Warehouse warehouse = warehouseRepository.findById(dto.id())
                .orElseThrow(() -> new EntityNotFoundException(UPDATING_UNEXISTING_WAREHOUSE_EXC + dto.id()));

        warehouse.setAddress(dto.address());
        warehouse.setName(dto.name());

        Warehouse updatedWarehouse = warehouseRepository.save(warehouse);

        return warehouseMapper.getDTOFromWarehouse(updatedWarehouse);
    }
}
