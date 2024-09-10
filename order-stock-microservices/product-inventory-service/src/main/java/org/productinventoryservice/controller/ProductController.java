package org.productinventoryservice.controller;

import org.productinventoryservice.dto.product.AddProductTransactionDTO;
import org.productinventoryservice.dto.product.DeleteProductDTO;
import org.productinventoryservice.dto.product.NewProductDTO;
import org.productinventoryservice.dto.product.ProductDTO;
import org.productinventoryservice.service.interfaces.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/product-page")
    public ResponseEntity<Page<ProductDTO>> getProductsPage(Pageable pageable) {
        return ResponseEntity.ok(productService.getProductsPage(pageable));
    }

    @PostMapping("/create")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody NewProductDTO dto) {
        return new ResponseEntity<>(productService.createProduct(dto), HttpStatusCode.valueOf(201));
    }

    @PatchMapping("/add-to-product")
    public ResponseEntity<ProductDTO> addProductAmount(@RequestBody AddProductTransactionDTO dto) {
        return new ResponseEntity<>(productService.addAmountToProduct(dto), HttpStatusCode.valueOf(200));
    }

    @PatchMapping("/update-product")
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO dto) {
        return new ResponseEntity<>(productService.updateProduct(dto), HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/delete-product")
    public ResponseEntity<Object> deleteProduct(@RequestBody DeleteProductDTO dto) {
        productService.deleteProduct(dto);
        return ResponseEntity.status(200).build();
    }
}
