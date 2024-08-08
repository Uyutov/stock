package org.productinventoryservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.productinventoryservice.dto.product.*;
import org.productinventoryservice.dto.warehouse.WarehouseDTO;
import org.productinventoryservice.entity.Product;
import org.productinventoryservice.entity.Warehouse;
import org.productinventoryservice.entity.WarehouseProduct;
import org.productinventoryservice.entity.composite_key.WarehouseProductKey;
import org.productinventoryservice.mapper.ProductMapper;
import org.productinventoryservice.repository.ProductRepository;
import org.productinventoryservice.repository.WarehouseProductRepository;
import org.productinventoryservice.service.interfaces.WarehouseService;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private WarehouseService warehouseService;

    @Mock
    private ProductRepository productRepository;
    @Mock
    private WarehouseProductRepository warehouseProductRepository;

    @Mock
    ProductMapper productMapper;

    private Warehouse ozonWarehouse;
    private Warehouse wildberriesWarehouse;
    private Product apple;
    private Product bottle;
    private WarehouseProduct appleInOzonWarehouse;
    private WarehouseProduct appleInWildberriesWarehouse;
    private WarehouseProduct bottleInWarehouse;

    @BeforeEach
    public void setUp() {
        ozonWarehouse = Warehouse.builder()
                .id(1L)
                .name("OZON")
                .address("Golubeva 1")
                .build();

        wildberriesWarehouse = Warehouse.builder()
                .id(2L)
                .name("Wildberries")
                .address("Golubeva 2")
                .build();

        apple = Product.builder()
                .id(1L)
                .name("Apple")
                .price(5)
                .build();

        bottle = Product.builder()
                .id(2L)
                .name("Bottle")
                .price(10)
                .build();

        WarehouseProductKey appleOzonWarehouseKey = new WarehouseProductKey(ozonWarehouse.getId(), apple.getId());
        WarehouseProductKey appleWildberriesWarehouseKey = new WarehouseProductKey(wildberriesWarehouse.getId(), apple.getId());
        WarehouseProductKey bottleWarehouseKey = new WarehouseProductKey(ozonWarehouse.getId(), bottle.getId());

        appleInOzonWarehouse = WarehouseProduct.builder()
                .id(appleOzonWarehouseKey)
                .product(apple)
                .warehouse(ozonWarehouse)
                .amount(10)
                .build();

        appleInWildberriesWarehouse = WarehouseProduct.builder()
                .id(appleWildberriesWarehouseKey)
                .product(apple)
                .warehouse(wildberriesWarehouse)
                .amount(15)
                .build();

        apple.setProductAmount(List.of(appleInOzonWarehouse, appleInWildberriesWarehouse));

        bottleInWarehouse = WarehouseProduct.builder()
                .id(bottleWarehouseKey)
                .product(bottle)
                .warehouse(ozonWarehouse)
                .amount(15)
                .build();

        bottle.setProductAmount(List.of(bottleInWarehouse));
    }

    @Test
    void getProductById() {
        ProductRequestDTO requestDTO = new ProductRequestDTO(1L);

        ProductDTO dto = ProductDTO.builder()
                .id(1L)
                .name("Apple")
                .price(5)
                .build();

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(apple));
        Mockito.when(productMapper.getDTOFromProduct(apple)).thenReturn(dto);

        ProductDTO response = productService.getProductById(requestDTO);

        assertThat(response).isEqualTo(dto);
    }

    @Test
    void getProductPageById() {
        Pageable pageable = PageRequest.of(0,2);

        ProductDTO appleDTO = ProductDTO.builder()
                .id(1L)
                .name("Apple")
                .price(5)
                .build();

        ProductDTO bottleDTO = ProductDTO.builder()
                .id(2L)
                .name("Bottle")
                .price(10)
                .build();

        Page<Product> productPage = new PageImpl<>(List.of(apple, bottle));

        Mockito.when(productRepository.findAll(pageable)).thenReturn(productPage);
        Mockito.when(productMapper.getDTOFromProduct(apple)).thenReturn(appleDTO);
        Mockito.when(productMapper.getDTOFromProduct(bottle)).thenReturn(bottleDTO);

        Page<ProductDTO> response = productService.getProductsPage(pageable);

        assertThat(response).contains(appleDTO);
        assertThat(response).contains(bottleDTO);
    }

    @Test
    void createProduct() {
        WarehouseDTO warehouseDTO = WarehouseDTO.builder()
                .id(ozonWarehouse.getId())
                .name(ozonWarehouse.getName())
                .address(ozonWarehouse.getAddress())
                .build();

        NewProductDTO dto = NewProductDTO.builder()
                .name("Phone")
                .amount(8)
                .price(100)
                .warehouse(warehouseDTO)
                .build();

        Product newProduct = Product.builder()
                .id(3L)
                .name("Phone")
                .price(100)
                .build();

        WarehouseProductKey phoneOzonWarehouseKey = new WarehouseProductKey(warehouseDTO.id(), newProduct.getId());
        WarehouseProduct phoneInWarehouse = WarehouseProduct.builder()
                .id(phoneOzonWarehouseKey)
                .warehouse(ozonWarehouse)
                .amount(8)
                .product(newProduct)
                .build();

        newProduct.setProductAmount(List.of(phoneInWarehouse));

        ProductDTO createdProductDTO = ProductDTO.builder()
                .id(3L)
                .name("Phone")
                .price(100)
                .build();

        Mockito.when(warehouseService.getWarehouseById(warehouseDTO.id())).thenReturn(ozonWarehouse);
        Mockito.when(productRepository.findIfProductAlreadyExists(dto.name(), ozonWarehouse.getId())).thenReturn(Optional.empty());
        Mockito.when(productRepository.save(any(Product.class))).thenReturn(newProduct);
        Mockito.when(productMapper.getDTOFromProduct(newProduct)).thenReturn(createdProductDTO);

        ProductDTO response = productService.createProduct(dto);

        assertThat(response).isEqualTo(createdProductDTO);
    }

    @Test
    void addAmountToProduct() {
        AddProductTransactionDTO addingDTO = new AddProductTransactionDTO(1L, 1L, 5);

        ProductDTO productWithIncrementedAmount = ProductDTO.builder()
                .id(apple.getId())
                .name(apple.getName())
                .price(apple.getPrice())
                .build();

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(apple));
        Mockito.when(warehouseProductRepository.findById(appleInOzonWarehouse.getId())).thenReturn(Optional.of(appleInOzonWarehouse));
        Mockito.when(productMapper.getDTOFromProduct(any(Product.class))).thenReturn(productWithIncrementedAmount);

        ProductDTO response = productService.addAmountToProduct(addingDTO);

        assertThat(response).isEqualTo(productWithIncrementedAmount);
    }

    @Test
    void checkForSuccessfulProductAvailabilityAndSubtractThem() {
        ProductSubtractionTransactionDTO appleSubtraction = new ProductSubtractionTransactionDTO
                (1L, 15);
        ProductSubtractionTransactionDTO bottleSubtraction = new ProductSubtractionTransactionDTO
                (2L, 5);

        var requestedProducts = List.of(appleSubtraction, bottleSubtraction);

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(apple));
        Mockito.when(productRepository.findById(2L)).thenReturn(Optional.of(bottle));

        Boolean response = productService.checkForProductAvailabilityAndSubtractThem(requestedProducts);

        assertThat(response).isTrue();
    }

    @Test
    void checkForFailingProductAvailabilityAndSubtractThem() {
        ProductSubtractionTransactionDTO appleSubtraction = new ProductSubtractionTransactionDTO
                (1L, 15);
        ProductSubtractionTransactionDTO bottleSubtraction = new ProductSubtractionTransactionDTO
                (2L, 20); // More than stored in warehouse

        var requestedProducts = List.of(appleSubtraction, bottleSubtraction);

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(apple));
        Mockito.when(productRepository.findById(2L)).thenReturn(Optional.of(bottle));

        Boolean response = productService.checkForProductAvailabilityAndSubtractThem(requestedProducts);

        assertThat(response).isFalse();
    }

    @Test
    void updateProduct() {
        ProductDTO updatedProductDTO = ProductDTO.builder()
                .id(1L)
                .name("Red Apple")
                .price(40)
                .build();

        Product savedProduct = Product.builder()
                .id(1L)
                .name("Red Apple")
                .price(40)
                .build();

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(apple));
        Mockito.when(productRepository.save(apple)).thenReturn(savedProduct);
        Mockito.when(productMapper.getDTOFromProduct(savedProduct)).thenReturn(updatedProductDTO);

        ProductDTO response = productService.updateProduct(updatedProductDTO);

        assertThat(response).isEqualTo(updatedProductDTO);
    }
}