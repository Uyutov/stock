package org.productinventoryservice.entity;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class WarehouseTest {
    static Validator validator;

    private final String ADDRESS = "Dzerginskogo";
    private final String NAME = "Apple";

    @BeforeAll
    public static void setUpClass() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void successfulCreation() {
        var warehouse = new Warehouse(-1L, ADDRESS, NAME);
        var violations = validator.validate(warehouse);

        assertThat(violations.size()).isEqualTo(0);
    }

    @Test
    public void addressIsNull() {
        var warehouse = new Warehouse(-1L, null, NAME);
        var violations = validator.validate(warehouse);

        String violationMessage = violations.stream().map(violation -> violation.getMessage()).toList().get(0);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violationMessage).isEqualTo("Please provide warehouse address");
    }

    @Test
    public void addressIsEmpty() {
        var warehouse = new Warehouse(-1L, "", NAME);
        var violations = validator.validate(warehouse);

        String violationMessage = violations.stream().map(violation -> violation.getMessage()).toList().get(0);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violationMessage).isEqualTo("Please provide warehouse address");
    }

    @Test
    public void addressConsistsOfSpaces() {
        var warehouse = new Warehouse(-1L, "      ", NAME);
        var violations = validator.validate(warehouse);

        String violationMessage = violations.stream().map(violation -> violation.getMessage()).toList().get(0);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violationMessage).isEqualTo("Please provide warehouse address");
    }

    @Test
    public void nameIsNull() {
        var warehouse = new Warehouse(-1L, ADDRESS, null);
        var violations = validator.validate(warehouse);

        String violationMessage = violations.stream().map(violation -> violation.getMessage()).toList().get(0);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violationMessage).isEqualTo("Please provide warehouse name");
    }

    @Test
    public void nameIsEmpty() {
        var warehouse = new Warehouse(-1L, ADDRESS, "");
        var violations = validator.validate(warehouse);

        String violationMessage = violations.stream().map(violation -> violation.getMessage()).toList().get(0);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violationMessage).isEqualTo("Please provide warehouse name");
    }

    @Test
    public void nameConsistsOfSpaces() {
        var warehouse = new Warehouse(-1L, ADDRESS, "         ");
        var violations = validator.validate(warehouse);

        String violationMessage = violations.stream().map(violation -> violation.getMessage()).toList().get(0);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violationMessage).isEqualTo("Please provide warehouse name");
    }

    @Test
    public void wrongOrderCreation() {
        var warehouse = new Warehouse(-1L, "         ", null);
        var violations = validator.validate(warehouse);

        List<String> violationMessages = violations.stream().map(violation -> violation.getMessage()).toList();

        assertThat(violations.size()).isEqualTo(2);
        assertThat(violationMessages).contains(
                "Please provide warehouse address",
                "Please provide warehouse name");
    }
}