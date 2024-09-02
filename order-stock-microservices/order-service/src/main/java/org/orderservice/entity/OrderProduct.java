package org.orderservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.orderservice.entity.composite_key.OrderProductKey;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_product", schema = "order_stock")
public class OrderProduct {
    @EmbeddedId
    @NotNull(message = "OrderProduct embedded id must be specified")
    private OrderProductKey id;

    @Column(nullable = false)
    @Min(value = 1, message = "Amount should be positive")
    private Integer amount;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
