package org.productinventoryservice.entity.composite_key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class WarehouseProductKey implements Serializable {
    @Column(name = "warehouse_id")
    private Long warehouseId;
    @Column(name = "product_id")
    private Long productId;
}
