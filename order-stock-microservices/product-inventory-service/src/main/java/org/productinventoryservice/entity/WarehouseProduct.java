package org.productinventoryservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.productinventoryservice.entity.composite_key.WarehouseProductKey;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "warehouse_product", schema = "order_stock")
public class WarehouseProduct {
    @EmbeddedId
    @NotNull(message = "WarehouseProduct embedded id must be specified")
    private WarehouseProductKey id;

    @ManyToOne
    @MapsId("warehouseId")
    @JoinColumn(name = "warehouse_id")
    @NotNull(message = "Warehouse must be specified")
    private Warehouse warehouse;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    @NotNull(message = "Product must be specified")
    private Product product;

    @NotNull(message = "Amount cannot be null")
    @Min(value = 0, message = "Amount should be positive")
    private Integer amount;

}
