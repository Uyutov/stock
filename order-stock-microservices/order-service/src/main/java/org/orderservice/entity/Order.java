package org.orderservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.orderservice.entity.enums.OrderState;

import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders", schema = "order_stock")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_id_seq")
    @SequenceGenerator(name = "orders_id_seq", schema = "order_stock", sequenceName = "orders_id_seq", allocationSize = 5, initialValue = 1)
    private Long id;

    @NotNull(message = "Please provide order state")
    @Enumerated(EnumType.STRING)
    private OrderState state;
    @NotBlank(message = "Please provide delivery address for order")
    private String deliveryAddress;

    @NotNull(message = "User must be specified")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotEmpty(message = "Order should contain at least one product")
    @OneToMany(mappedBy = "order")
    List<OrderProduct> products;
}
