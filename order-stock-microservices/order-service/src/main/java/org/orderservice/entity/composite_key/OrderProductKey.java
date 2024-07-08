package org.orderservice.entity.composite_key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class OrderProductKey implements Serializable {
    @Column(name = "order_id")
    private long orderId;
    @Column(name = "product_id")
    private long productId;
}
