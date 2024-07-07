package org.orderservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "product", schema = "order_stock")
public class Warehouse {
    @Id
    private String id;

    private String address;
    private String name;
}
