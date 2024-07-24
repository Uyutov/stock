package org.orderservice.repository;

import org.orderservice.entity.OrderProduct;
import org.orderservice.entity.composite_key.OrderProductKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, OrderProductKey> {
}
