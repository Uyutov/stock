package org.orderservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "orders", schema = "order_stock")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_id_seq")
    @SequenceGenerator(name = "orders_id_seq", sequenceName = "orders_id_seq", allocationSize = 5, initialValue = 1)
    private Long id;

    @NotBlank(message = "Please provide order state")
    private String state;
    @NotBlank(message = "Please provide delivery address for order")
    private String deliveryAddress;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order")
    List<OrderProduct> productAmount;
}
