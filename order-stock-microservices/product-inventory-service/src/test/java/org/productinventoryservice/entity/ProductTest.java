package org.productinventoryservice.entity;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {

    static Validator validator;

    private final String NAME = "Apple";
    private final Integer PRICE = 10;
    private final List<WarehouseProduct> WAREHOUSES = List.of(new WarehouseProduct());

    @BeforeAll
    public static void setUpClass() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void successfulCreation() {
        var product = new Product(-1L, NAME, PRICE, WAREHOUSES);
        var violations = validator.validate(product);

        assertThat(violations.size()).isEqualTo(0);
    }

    @Test
    public void productNameIsNull() {
        var product = new Product(-1L, null, PRICE, WAREHOUSES);
        var violations = validator.validate(product);

        String violationMessage = violations.stream().map(violation -> violation.getMessage()).toList().get(0);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violationMessage).isEqualTo("Please provide product name");
    }

    @Test
    public void productNameIsEmpty() {
        var product = new Product(-1L, "", PRICE, WAREHOUSES);
        var violations = validator.validate(product);

        String violationMessage = violations.stream().map(violation -> violation.getMessage()).toList().get(0);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violationMessage).isEqualTo("Please provide product name");
    }

    @Test
    public void productNameConsistsOfSpaces() {
        var product = new Product(-1L, "         ", PRICE, WAREHOUSES);
        var violations = validator.validate(product);

        String violationMessage = violations.stream().map(violation -> violation.getMessage()).toList().get(0);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violationMessage).isEqualTo("Please provide product name");
    }

    @Test
    public void productPriceIsNull() {
        var product = new Product(-1L, NAME, null, WAREHOUSES);
        var violations = validator.validate(product);

        String violationMessage = violations.stream().map(violation -> violation.getMessage()).toList().get(0);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violationMessage).isEqualTo("Price could not be null");
    }

    @Test
    public void productPriceEqualsZero() {
        var product = new Product(-1L, NAME, 0, WAREHOUSES);
        var violations = validator.validate(product);

        String violationMessage = violations.stream().map(violation -> violation.getMessage()).toList().get(0);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violationMessage).isEqualTo("Price should be positive");
    }

    @Test
    public void productPriceIsNegative() {
        var product = new Product(-1L, NAME, -10, WAREHOUSES);
        var violations = validator.validate(product);

        String violationMessage = violations.stream().map(violation -> violation.getMessage()).toList().get(0);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violationMessage).isEqualTo("Price should be positive");
    }

    @Test
    public void warehouseIsNull() {
        var product = new Product(-1L, NAME, PRICE, null);
        var violations = validator.validate(product);

        String violationMessage = violations.stream().map(violation -> violation.getMessage()).toList().get(0);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violationMessage).isEqualTo("Warehouse should be specified");
    }

    @Test
    public void wrongCreation() {
        var product = new Product(-1L, "       ", -10, null);
        var violations = validator.validate(product);

        List<String> violationMessages = violations.stream().map(violation -> violation.getMessage()).toList();

        assertThat(violations.size()).isEqualTo(3);
        assertThat(violationMessages).contains(
                "Please provide product name",
                "Price should be positive",
                "Warehouse should be specified"
        );
    }
}