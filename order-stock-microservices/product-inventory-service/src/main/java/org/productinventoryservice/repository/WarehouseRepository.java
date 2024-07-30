package org.productinventoryservice.repository;

import org.productinventoryservice.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    Optional<Warehouse> findByNameAndAddress(String name, String address);
}
