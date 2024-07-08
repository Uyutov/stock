package org.orderservice.entity;

import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.orderservice.entity.composite_key.WarehouseProductKey;

@Entity
@Data
@Table(name = "warehouse_product", schema = "order_stock")
public class WarehouseProduct {
    @EmbeddedId
    private WarehouseProductKey id;

    @ManyToOne
    @MapsId("warehouseId")
    @JoinColumn(name = "warehouse_id")
    private Warehouse order;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @Min(0)
    private Integer amount;

}
