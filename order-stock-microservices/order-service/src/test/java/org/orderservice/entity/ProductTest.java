package org.orderservice.entity;

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

    @BeforeAll
    public static void setUpClass() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void successfulCreation() {
        var product = new Product(-1L, NAME, PRICE);
        var violations = validator.validate(product);

        assertThat(violations.size()).isEqualTo(0);
    }

    @Test
    public void emptyProductName() {
        var product = new Product(-1L, "", PRICE);
        var violations = validator.validate(product);

        String violationMessage = violations.stream().map(violation -> violation.getMessage()).toList().get(0);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violationMessage).isEqualTo("Please provide product name");
    }

    @Test
    public void nullProductName() {
        var product = new Product(-1L, null, PRICE);
        var violations = validator.validate(product);

        String violationMessage = violations.stream().map(violation -> violation.getMessage()).toList().get(0);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violationMessage).isEqualTo("Please provide product name");
    }

    @Test
    public void productNameConsistingOfSpaces() {
        var product = new Product(-1L, "         ", PRICE);
        var violations = validator.validate(product);

        String violationMessage = violations.stream().map(violation -> violation.getMessage()).toList().get(0);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violationMessage).isEqualTo("Please provide product name");
    }

    @Test
    public void productPriceEqualsZero() {
        var product = new Product(-1L, NAME, 0);
        var violations = validator.validate(product);

        String violationMessage = violations.stream().map(violation -> violation.getMessage()).toList().get(0);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violationMessage).isEqualTo("Price should be positive");
    }

    @Test
    public void productPriceIsNull() {
        var product = new Product(-1L, NAME, null);
        var violations = validator.validate(product);

        String violationMessage = violations.stream().map(violation -> violation.getMessage()).toList().get(0);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violationMessage).isEqualTo("Price could not be null");
    }

    @Test
    public void productPriceIsNegative() {
        var product = new Product(-1L, NAME, -10);
        var violations = validator.validate(product);

        String violationMessage = violations.stream().map(violation -> violation.getMessage()).toList().get(0);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violationMessage).isEqualTo("Price should be positive");
    }

    @Test
    public void wrongCreationOfProduct() {
        var product = new Product(-1L, null, -10);
        var violations = validator.validate(product);

        List<String> violationMessages = violations.stream().map(violation -> violation.getMessage()).toList();

        assertThat(violations.size()).isEqualTo(2);
        assertThat(violationMessages).contains(
                "Please provide product name",
                "Price should be positive"
                );
    }
}