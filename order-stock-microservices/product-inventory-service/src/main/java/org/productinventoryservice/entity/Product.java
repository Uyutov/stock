package org.productinventoryservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank(message = "Please provide product name")
    private String name;

    @NotNull(message = "Price could not be null")
    @Min(value = 1, message = "Price should be positive")
    private Integer price;

    @OneToMany(mappedBy = "product")
    private List<WarehouseProduct> productAmount;
}
