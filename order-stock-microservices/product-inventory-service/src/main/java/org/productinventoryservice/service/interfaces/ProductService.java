package org.productinventoryservice.service.interfaces;

import org.productinventoryservice.dto.product.*;

import java.util.List;

public interface ProductService {
    public ProductDTO createProduct(NewProductDTO dto);
    public ProductDTO addProduct(AddProductTransactionDTO dto);
    public Boolean checkForProductAvailabilityAndSubtractThem(List<ProductSubtractionTransactionDTO> dto);
    public ProductDTO updateProduct(ProductDTO dto);
    public void deleteProduct(DeleteProductDTO dto);
}
