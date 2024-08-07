package org.productinventoryservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.productinventoryservice.dto.warehouse.NewWarehouseDTO;
import org.productinventoryservice.dto.warehouse.WarehouseDTO;
import org.productinventoryservice.entity.Warehouse;
import org.productinventoryservice.mapper.WarehouseMapper;
import org.productinventoryservice.repository.WarehouseRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class WarehouseServiceImplTest {

    @Mock
    private WarehouseRepository warehouseRepository;
    @Mock
    private WarehouseMapper warehouseMapper;

    @InjectMocks
    private WarehouseServiceImpl warehouseService;

    private Warehouse warehouse;

    @BeforeEach
    public void setUp(){
        warehouse = Warehouse.builder()
                .id(1L)
                .name("Ozon")
                .address("Golubeva")
                .build();
    }

    @Test
    void getWarehouseById() {
        Long warehouseID = 1L;

        Mockito.when(warehouseRepository.findById(warehouseID)).thenReturn(Optional.of(warehouse));

        Warehouse response = warehouseService.getWarehouseById(warehouseID);

        assertThat(response).isEqualTo(warehouse);
    }

    @Test
    void createWarehouse() {
        NewWarehouseDTO newWarehouseDTO = NewWarehouseDTO.builder()
                .address("Golubeva")
                .name("Ozon")
                .build();

        WarehouseDTO warehouseDTO = WarehouseDTO.builder()
                .id(1L)
                .address("Golubeva")
                .name("Ozon")
                .build();

        Mockito.when(warehouseRepository.findByAddress(newWarehouseDTO.address())).thenReturn(Optional.empty());
        Mockito.when(warehouseMapper.getWarehouseFromCreationDTO(any(NewWarehouseDTO.class))).thenReturn(warehouse);
        Mockito.when(warehouseRepository.save(any(Warehouse.class))).thenReturn(warehouse);
        Mockito.when(warehouseMapper.getDTOFromWarehouse(warehouse)).thenReturn(warehouseDTO);

        WarehouseDTO response = warehouseService.createWarehouse(newWarehouseDTO);

        assertThat(response).isEqualTo(warehouseDTO);
    }

    @Test
    void updateWarehouse() {
        WarehouseDTO updateForWarehouse = WarehouseDTO.builder()
                .id(1L)
                .address("Pravdi")
                .name("Wildberries")
                .build();

        Warehouse updatedWarehouse = Warehouse.builder()
                .id(1L)
                .address("Pravdi")
                .name("Wildberries")
                .build();

        Mockito.when(warehouseRepository.findById(updateForWarehouse.id())).thenReturn(Optional.of(warehouse));
        Mockito.when(warehouseRepository.save(warehouse)).thenReturn(updatedWarehouse);
        Mockito.when(warehouseMapper.getDTOFromWarehouse(updatedWarehouse)).thenReturn(updateForWarehouse);

        WarehouseDTO response = warehouseService.updateWarehouse(updateForWarehouse);

        assertThat(response).isEqualTo(updateForWarehouse);
    }
}