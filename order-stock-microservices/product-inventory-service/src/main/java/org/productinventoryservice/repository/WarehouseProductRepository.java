package org.productinventoryservice.repository;

import org.productinventoryservice.entity.WarehouseProduct;
import org.productinventoryservice.entity.composite_key.WarehouseProductKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseProductRepository extends JpaRepository<WarehouseProduct, WarehouseProductKey> {
}
