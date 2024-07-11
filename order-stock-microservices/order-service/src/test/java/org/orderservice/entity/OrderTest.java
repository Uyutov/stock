package org.orderservice.entity;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

    static Validator validator;

    private final String STATE = "ASSEMBLING";
    private final String ADDRESS = "Golubeva";
    private final User USER = new User();
    private final List<OrderProduct> ORDER_PRODUCT = List.of(new OrderProduct());

    @BeforeAll
    public static void setUpClass() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void successfulCreation() {
        var order = new Order(-1L, STATE, ADDRESS, USER, ORDER_PRODUCT);
        var violations = validator.validate(order);

        assertThat(violations.size()).isEqualTo(0);
    }

    @Test
    public void emptyState() {
        var order = new Order(-1L, "", ADDRESS, USER, ORDER_PRODUCT);
        var violations = validator.validate(order);

        String violationMessage = violations.stream().map(violation -> violation.getMessage()).toList().get(0);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violationMessage).isEqualTo("Please provide order state");
    }

    @Test
    public void nullState() {
        var order = new Order(-1L, null, ADDRESS, USER, ORDER_PRODUCT);
        var violations = validator.validate(order);

        String violationMessage = violations.stream().map(violation -> violation.getMessage()).toList().get(0);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violationMessage).isEqualTo("Please provide order state");
    }

    @Test
    public void stateWithSpaces() {
        var order = new Order(-1L, "          ", ADDRESS, USER, ORDER_PRODUCT);
        var violations = validator.validate(order);

        String violationMessage = violations.stream().map(violation -> violation.getMessage()).toList().get(0);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violationMessage).isEqualTo("Please provide order state");
    }

    @Test
    public void emptyAddress() {
        var order = new Order(-1L, STATE, "", USER, ORDER_PRODUCT);
        var violations = validator.validate(order);

        String violationMessage = violations.stream().map(violation -> violation.getMessage()).toList().get(0);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violationMessage).isEqualTo("Please provide delivery address for order");
    }

    @Test
    public void nullAddress() {
        var order = new Order(-1L, STATE, null, USER, ORDER_PRODUCT);
        var violations = validator.validate(order);

        String violationMessage = violations.stream().map(violation -> violation.getMessage()).toList().get(0);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violationMessage).isEqualTo("Please provide delivery address for order");
    }

    @Test
    public void stateWithAddress() {
        var order = new Order(-1L, STATE, "     ", USER, ORDER_PRODUCT);
        var violations = validator.validate(order);

        String violationMessage = violations.stream().map(violation -> violation.getMessage()).toList().get(0);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violationMessage).isEqualTo("Please provide delivery address for order");
    }

    @Test
    public void nullUser() {
        var order = new Order(-1L, STATE, ADDRESS, null, ORDER_PRODUCT);
        var violations = validator.validate(order);

        String violationMessage = violations.stream().map(violation -> violation.getMessage()).toList().get(0);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violationMessage).isEqualTo("User must be specified");
    }

    @Test
    public void nullProductList() {
        var order = new Order(-1L, STATE, ADDRESS, USER, null);
        var violations = validator.validate(order);

        String violationMessage = violations.stream().map(violation -> violation.getMessage()).toList().get(0);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violationMessage).isEqualTo("Order should contain at least one product");
    }

    @Test
    public void emptyProductList() {
        var order = new Order(-1L, STATE, ADDRESS, USER, List.of());
        var violations = validator.validate(order);

        String violationMessage = violations.stream().map(violation -> violation.getMessage()).toList().get(0);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violationMessage).isEqualTo("Order should contain at least one product");
    }

    @Test
    public void wrongOrderCreation() {
        var order = new Order(-1L, "", null, null, List.of());
        var violations = validator.validate(order);

        List<String> violationMessages = violations.stream().map(violation -> violation.getMessage()).toList();

        assertThat(violations.size()).isEqualTo(4);
        assertThat(violationMessages).contains(
                "Please provide order state",
                "Please provide delivery address for order",
                "User must be specified",
                "Order should contain at least one product");
    }
}