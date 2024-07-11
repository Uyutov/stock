package org.productinventoryservice.entity;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.productinventoryservice.entity.composite_key.WarehouseProductKey;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class WarehouseProductTest {

    static Validator validator;

    private final Warehouse WAREHOUSE = new Warehouse(-1L, "Dzerginskogo", "OZON");
    private final Product PRODUCT = new Product(-1L, "Apple", 10, null);
    private final WarehouseProductKey KEY = new WarehouseProductKey(WAREHOUSE.getId(), PRODUCT.getId());
    private final Integer AMOUNT = 100;

    @BeforeAll
    public static void setUpClass() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void successfulCreation() {
        var warehouseProduct = new WarehouseProduct(KEY, WAREHOUSE, PRODUCT, AMOUNT);
        var violations = validator.validate(warehouseProduct);

        assertThat(violations.size()).isEqualTo(0);
    }

    @Test
    public void warehouseProductKeyIsNull() {
        var warehouseProduct = new WarehouseProduct(null, WAREHOUSE, PRODUCT, AMOUNT);
        var violations = validator.validate(warehouseProduct);

        String violationMessage = violations.stream().map(violation -> violation.getMessage()).toList().get(0);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violationMessage).isEqualTo("WarehouseProduct embedded id must be specified");
    }

    @Test
    public void warehouseIsNull() {
        var warehouseProduct = new WarehouseProduct(KEY, null, PRODUCT, AMOUNT);
        var violations = validator.validate(warehouseProduct);

        String violationMessage = violations.stream().map(violation -> violation.getMessage()).toList().get(0);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violationMessage).isEqualTo("Warehouse must be specified");
    }

    @Test
    public void productIsNull() {
        var warehouseProduct = new WarehouseProduct(KEY, WAREHOUSE, null, AMOUNT);
        var violations = validator.validate(warehouseProduct);

        String violationMessage = violations.stream().map(violation -> violation.getMessage()).toList().get(0);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violationMessage).isEqualTo("Product must be specified");
    }

    @Test
    public void amountIsNull() {
        var warehouseProduct = new WarehouseProduct(KEY, WAREHOUSE, PRODUCT, null);
        var violations = validator.validate(warehouseProduct);

        String violationMessage = violations.stream().map(violation -> violation.getMessage()).toList().get(0);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violationMessage).isEqualTo("Amount cannot be null");
    }

    @Test
    public void amountLessThenZero() {
        var warehouseProduct = new WarehouseProduct(KEY, WAREHOUSE, PRODUCT, -10);
        var violations = validator.validate(warehouseProduct);

        String violationMessage = violations.stream().map(violation -> violation.getMessage()).toList().get(0);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violationMessage).isEqualTo("Amount should be positive");
    }

    @Test
    public void wrongCreation() {
        var warehouseProduct = new WarehouseProduct(null, null, null, -100);
        var violations = validator.validate(warehouseProduct);

        List<String> violationMessages = violations.stream().map(violation -> violation.getMessage()).toList();

        assertThat(violations.size()).isEqualTo(4);
        assertThat(violationMessages).contains(
                "WarehouseProduct embedded id must be specified",
                "Warehouse must be specified",
                "Product must be specified",
                "Amount should be positive"
        );
    }
}