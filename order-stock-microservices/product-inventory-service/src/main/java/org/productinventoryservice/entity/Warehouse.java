package org.productinventoryservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
@Table(name = "warehouse", schema = "order_stock")
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "warehouse_id_seq")
    @SequenceGenerator(name = "warehouse_id_seq", sequenceName = "warehouse_id_seq", allocationSize = 5, initialValue = 1)
    private Long id;

    @NotBlank(message = "Please provide warehouse address")
    private String address;

    @NotBlank(message = "Please provide warehouse name")
    private String name;
}
