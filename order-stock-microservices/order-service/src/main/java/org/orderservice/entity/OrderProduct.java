package org.orderservice.entity;

import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.orderservice.entity.composite_key.OrderProductKey;

@Entity
@Data
@Table(name = "order_product", schema = "order_stock")
public class OrderProduct {

    @EmbeddedId
    private OrderProductKey id;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @Min(value = 0, message = "Amount should be positive")
    private Integer amount;

}
