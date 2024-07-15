package org.orderservice.service;


import org.productinventoryservice.dto.product.DeleteProductDTO;
import org.productinventoryservice.dto.product.NewProductDTO;
import org.productinventoryservice.dto.product.ProductTransactionDTO;
import org.productinventoryservice.repository.ProductRepository;
import org.productinventoryservice.service.interfaces.ProductService;

public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void createProduct(NewProductDTO dto) {

    }

    @Override
    public void addProduct(ProductTransactionDTO dto) {

    }

    @Override
    public void takeProduct(ProductTransactionDTO dto) {

    }

    @Override
    public void updateProduct(NewProductDTO dto) {

    }

    @Override
    public void deleteProduct(DeleteProductDTO dto) {

    }
}
