package org.productinventoryservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.productinventoryservice.entity.composite_key.WarehouseProductKey;

@Entity
@Data
@Table(name = "warehouse_product", schema = "order_stock")
public class WarehouseProduct {
    @EmbeddedId
    private WarehouseProductKey id;

    @ManyToOne
    @MapsId("warehouseId")
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @Min(value = 0, message = "Amount should be positive")
    private Integer amount;

}
