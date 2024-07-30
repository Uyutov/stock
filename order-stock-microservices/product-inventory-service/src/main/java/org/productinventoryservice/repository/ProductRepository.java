package org.productinventoryservice.repository;


import org.productinventoryservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "select product.* from product " +
            "left join warehouse on warehouse.id = ?2 " +
            "where product.name = ?1")
    public Optional<Product> findIfProductAlreadyExists (String name, Long warehouseId);
}
