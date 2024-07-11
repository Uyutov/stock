package org.orderservice.entity;

import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
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

    @ManyToOne
    @MapsId("orderId")
    @NotNull(message = "Order must be specified")
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @MapsId("productId")
    @NotNull(message = "Product must be specified")
    @JoinColumn(name = "product_id")
    private Product product;

    @NotNull(message = "Amount could not be null")
    @Min(value = 1, message = "Amount should be positive")
    private Integer amount;

}
