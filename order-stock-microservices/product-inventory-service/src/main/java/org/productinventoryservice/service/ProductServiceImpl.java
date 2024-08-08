package org.productinventoryservice.service;


import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.productinventoryservice.dto.product.*;
import org.productinventoryservice.entity.Product;
import org.productinventoryservice.entity.Warehouse;
import org.productinventoryservice.entity.WarehouseProduct;
import org.productinventoryservice.entity.composite_key.WarehouseProductKey;
import org.productinventoryservice.exception.RepeatedProductCreationException;
import org.productinventoryservice.mapper.ProductMapper;
import org.productinventoryservice.repository.ProductRepository;
import org.productinventoryservice.repository.WarehouseProductRepository;
import org.productinventoryservice.service.interfaces.ProductService;
import org.productinventoryservice.service.interfaces.WarehouseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private final WarehouseService warehouseService;

    private final ProductRepository productRepository;
    private final WarehouseProductRepository warehouseProductRepository;

    private final ProductMapper productMapper;

    private final String PRODUCT_ALREADY_EXIST_EXC = "Product already exists in this warehouse";
    private final String PRODUCT_NOT_FOUND_EXC = "Could not find product with id ";
    private final String PRODUCT_NOT_FOUND_IN_WAREHOUSE = "Could not found product with id %d in warehouse with id %d";
    public ProductServiceImpl(WarehouseService warehouseService,
                              ProductRepository productRepository,
                              WarehouseProductRepository warehouseProductRepository,
                              ProductMapper productMapper) {
        this.warehouseService = warehouseService;
        this.productRepository = productRepository;
        this.warehouseProductRepository = warehouseProductRepository;
        this.productMapper = productMapper;
    }

    @Override
    public ProductDTO createProduct(NewProductDTO dto) {
        Warehouse warehouse = warehouseService.getWarehouseById(dto.warehouse().id());

        Optional<Product> existingProduct = productRepository.findIfProductAlreadyExists(dto.name(), warehouse.getId());

        if (existingProduct.isPresent()) {
            throw new RepeatedProductCreationException(PRODUCT_ALREADY_EXIST_EXC);
        }

        Product newProduct = Product.builder()
                .name(dto.name())
                .price(dto.price())
                .build();

        newProduct = productRepository.save(newProduct);

        WarehouseProductKey warehouseProductKey = new WarehouseProductKey(warehouse.getId(), newProduct.getId());

        WarehouseProduct warehouseProduct = WarehouseProduct.builder()
                .id(warehouseProductKey)
                .product(newProduct)
                .warehouse(warehouse)
                .amount(dto.amount())
                .build();

        warehouseProductRepository.save(warehouseProduct);

        return productMapper.getDTOFromProduct(newProduct);
    }

    @Override
    public ProductDTO addAmountToProduct(AddProductTransactionDTO dto) {
        Product product = productRepository.findById(dto.productId()).orElseThrow(() -> {
            return new EntityNotFoundException(PRODUCT_NOT_FOUND_EXC + dto.productId());
        });

        WarehouseProductKey warehouseProductKey = new WarehouseProductKey(dto.warehouseId(), dto.productId());

        WarehouseProduct productAmount = warehouseProductRepository.findById(warehouseProductKey).orElseThrow(() -> {
            return new EntityNotFoundException(String.format(PRODUCT_NOT_FOUND_IN_WAREHOUSE, dto.productId(),dto.warehouseId())
            );
        });

        productAmount.setAmount(productAmount.getAmount() + dto.amount());
        return productMapper.getDTOFromProduct(product);
    }

    @Override
    @Transactional
    public Boolean checkForProductAvailabilityAndSubtractThem(List<ProductSubtractionTransactionDTO> requestedProducts) {
        for (var request : requestedProducts) {
            Optional<Product> product = productRepository.findById(request.productId());
            Integer requestedProductAmount = request.amount();

            if (product.isEmpty()) { return false; }

            Integer stockAmount = product.get().getProductAmount().stream()
                    .mapToInt(productInWarehouse -> productInWarehouse.getAmount()).sum();

            if (stockAmount < requestedProductAmount) { return false; }

            for (var productInWarehouse : product.get().getProductAmount()) {
                if (requestedProductAmount > productInWarehouse.getAmount()) {
                    requestedProductAmount -= productInWarehouse.getAmount();
                    productInWarehouse.setAmount(0);
                } else {
                    productInWarehouse.setAmount(productInWarehouse.getAmount() - requestedProductAmount);
                    break;
                }
            }
        }
        return true;
    }

    @Override
    public ProductDTO updateProduct(ProductDTO dto) {
        Product product = productRepository.findById(dto.id())
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND_EXC + dto.id()));

        product.setName(dto.name());
        product.setPrice(dto.price());

        Product newProduct = productRepository.save(product);
        return productMapper.getDTOFromProduct(newProduct);
    }

    @Override
    public void deleteProduct(DeleteProductDTO dto) {
        productRepository.deleteById(dto.id());
    }
}
