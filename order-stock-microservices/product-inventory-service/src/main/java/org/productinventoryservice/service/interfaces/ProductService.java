package org.productinventoryservice.service.interfaces;

import org.productinventoryservice.dto.product.DeleteProductDTO;
import org.productinventoryservice.dto.product.NewProductDTO;
import org.productinventoryservice.dto.product.ProductTransactionDTO;

public interface ProductService {
    public void createProduct(NewProductDTO dto);
    public void addProduct(ProductTransactionDTO dto);
    public void takeProduct(ProductTransactionDTO dto);
    public void updateProduct(NewProductDTO dto);
    public void deleteProduct(DeleteProductDTO dto);
}
