package org.orderservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "product", schema = "order_stock")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private String id;

    @NotBlank(message = "Please provide product name")
    private String name;

    @Min(value = 0, message = "Price should be positive")
    private Integer price;

    @OneToMany(mappedBy = "product")
    private List<WarehouseProduct> productAmount;
}
