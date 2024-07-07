package org.orderservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "product", schema = "order_stock")
public class Product {
    @Id
    private String id;

    private String name;
    private Integer price;

    @OneToMany(mappedBy = "product")
    private List<WarehouseProduct> productAmount;
}
