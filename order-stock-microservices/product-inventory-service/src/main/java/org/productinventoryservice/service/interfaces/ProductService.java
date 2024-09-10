package org.productinventoryservice.service.interfaces;

import org.productinventoryservice.dto.product.AddProductTransactionDTO;
import org.productinventoryservice.dto.product.DeleteProductDTO;
import org.productinventoryservice.dto.product.NewProductDTO;
import org.productinventoryservice.dto.product.ProductDTO;
import org.productinventoryservice.dto.product.ProductSubtractionTransactionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    public ProductDTO getProductById(Long id);
    public Page<ProductDTO> getProductsPage(Pageable pageable);
    public ProductDTO createProduct(NewProductDTO dto);
    public ProductDTO addAmountToProduct(AddProductTransactionDTO dto);
    public Boolean checkForProductAvailabilityAndSubtractThem(List<ProductSubtractionTransactionDTO> dto);
    public ProductDTO updateProduct(ProductDTO dto);
    public void deleteProduct(DeleteProductDTO dto);
}
