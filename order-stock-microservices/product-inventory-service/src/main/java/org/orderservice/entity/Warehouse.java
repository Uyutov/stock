package org.orderservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
@Table(name = "warehouse", schema = "order_stock")
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "warehouse_id_seq")
    private String id;

    @NotBlank(message = "Please provide warehouse address")
    private String address;

    @NotBlank(message = "Please provide warehouse name")
    private String name;
}
