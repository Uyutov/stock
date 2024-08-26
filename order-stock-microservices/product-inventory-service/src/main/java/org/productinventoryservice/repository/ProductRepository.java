package org.productinventoryservice.repository;


import org.productinventoryservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "select order_stock.product.* from order_stock.product " +
            "left join order_stock.warehouse on order_stock.warehouse.id = ?2 " +
            "where order_stock.product.name = ?1",
    nativeQuery = true)
    public Optional<Product> findIfProductAlreadyExists (String name, Long warehouseId);
}
