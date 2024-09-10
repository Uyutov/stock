package org.productinventoryservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "warehouse", schema = "order_stock")
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "warehouse_id_seq")
    @SequenceGenerator(name = "warehouse_id_seq", schema = "order_stock", sequenceName = "warehouse_id_seq", allocationSize = 5, initialValue = 1)
    private Long id;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String name;
}
