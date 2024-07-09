package org.orderservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
@Table(name = "product", schema = "order_stock")
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private String id;

    @NotBlank(message = "Please provide warehouse address")
    private String address;

    @NotBlank(message = "Please provide warehouse name")
    private String name;
}
