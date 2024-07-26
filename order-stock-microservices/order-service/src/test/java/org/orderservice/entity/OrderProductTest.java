package org.orderservice.entity;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.orderservice.entity.composite_key.OrderProductKey;
import org.orderservice.entity.enums.OrderState;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderProductTest {

    static Validator validator;

    private final Integer AMOUNT = 5;
    private final Order ORDER = new Order(-1L, OrderState.PACKAGING, "Golubeva", new User(), null);
    private final Product PRODUCT = new Product(-1L, "Apple", 10);
    private final OrderProductKey KEY = new OrderProductKey(ORDER.getId(), PRODUCT.getId());
    @BeforeAll
    public static void setUpClass() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void successfulOrderProductConnectionCreation() {
        var orderProduct = new OrderProduct(KEY, AMOUNT, ORDER, PRODUCT);
        var violations = validator.validate(orderProduct);

        assertThat(violations.size()).isEqualTo(0);
    }

    @Test
    public void nullIdInOrderProduct() {
        var orderProduct = new OrderProduct(null, AMOUNT, ORDER, PRODUCT);
        var violations = validator.validate(orderProduct);

        String violationMessage = violations.stream().map(violation -> violation.getMessage()).toList().get(0);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violationMessage).isEqualTo("OrderProduct embedded id must be specified");
    }

    @Test
    public void nullOrderInOrderProduct() {
        var orderProduct = new OrderProduct(KEY, AMOUNT, null, PRODUCT);
        var violations = validator.validate(orderProduct);

        String violationMessage = violations.stream().map(violation -> violation.getMessage()).toList().get(0);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violationMessage).isEqualTo("Order must be specified");
    }

    @Test
    public void nullProductInOrderProduct() {
        var orderProduct = new OrderProduct(KEY, AMOUNT, ORDER, null);
        var violations = validator.validate(orderProduct);

        String violationMessage = violations.stream().map(violation -> violation.getMessage()).toList().get(0);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violationMessage).isEqualTo("Product must be specified");
    }

    @Test
    public void nullAmountInOrderProduct() {
        var orderProduct = new OrderProduct(KEY, null, ORDER, PRODUCT);
        var violations = validator.validate(orderProduct);

        String violationMessage = violations.stream().map(violation -> violation.getMessage()).toList().get(0);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violationMessage).isEqualTo("Amount could not be null");
    }

    @Test
    public void amountEqualsZeroInOrderProduct() {
        var orderProduct = new OrderProduct(KEY, 0, ORDER, PRODUCT);
        var violations = validator.validate(orderProduct);

        String violationMessage = violations.stream().map(violation -> violation.getMessage()).toList().get(0);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violationMessage).isEqualTo("Amount should be positive");
    }

    @Test
    public void negativeAmountInOrderProduct() {
        var orderProduct = new OrderProduct(KEY, -10, ORDER, PRODUCT);
        var violations = validator.validate(orderProduct);

        String violationMessage = violations.stream().map(violation -> violation.getMessage()).toList().get(0);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violationMessage).isEqualTo("Amount should be positive");
    }

    @Test
    public void wrongCreationOfOrderProduct() {
        var orderProduct = new OrderProduct(null, 0, null, null);
        var violations = validator.validate(orderProduct);

        List<String> violationMessages = violations.stream().map(violation -> violation.getMessage()).toList();

        assertThat(violations.size()).isEqualTo(4);
        assertThat(violationMessages).contains(
                "OrderProduct embedded id must be specified",
                "Order must be specified",
                "Product must be specified",
                "Amount should be positive");
    }
}