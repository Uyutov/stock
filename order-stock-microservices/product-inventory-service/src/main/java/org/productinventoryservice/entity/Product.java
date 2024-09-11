package org.productinventoryservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product", schema = "order_stock")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_seq")
    @SequenceGenerator(name = "product_id_seq", schema = "order_stock", sequenceName = "product_id_seq", allocationSize = 5, initialValue = 1)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Min(value = 1, message = "Price should be positive")
    private Integer price;

    @OneToMany(mappedBy = "product")
    private List<WarehouseProduct> productAmount;
}
