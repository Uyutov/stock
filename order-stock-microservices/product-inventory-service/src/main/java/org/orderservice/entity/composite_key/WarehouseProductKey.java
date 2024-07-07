package org.orderservice.entity.composite_key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class WarehouseProductKey implements Serializable {
    @Column(name = "warehouse_id")
    private long warehouseId;
    @Column(name = "product_id")
    private long productId;
}
