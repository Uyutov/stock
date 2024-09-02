package org.productinventoryservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.productinventoryservice.entity.composite_key.WarehouseProductKey;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "warehouse_product", schema = "order_stock")
public class WarehouseProduct {
    @EmbeddedId
    @NotNull(message = "WarehouseProduct embedded productId must be specified")
    private WarehouseProductKey id;

    @ManyToOne
    @MapsId("warehouseId")
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    @Min(value = 0, message = "Amount should be positive")
    private Integer amount;

}
