package org.orderservice.dao;

import lombok.RequiredArgsConstructor;
import org.orderservice.entity.Order;
import org.orderservice.entity.enums.FilterOptions;
import org.orderservice.entity.enums.OrderState;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderDao {
    public Specification<Order> getOrdersPage(FilterOptions filterOption, String filterValue) {
        switch (filterOption) {
            case LOWER_THEN_PRICE:
                return (order, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThan(order.get("totalPrice"), Integer.parseInt(filterValue));
            case HIGHER_THEN_PRICE:
                return (order, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThan(order.get("totalPrice"), Integer.parseInt(filterValue));
            case BY_STATE:
                return (order, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(order.get("state"), OrderState.valueOf(filterValue));
            case BY_ADDRESS:
                return (order, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(order.get("deliveryAddress"), filterValue);
        }
        return (order, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(order.get("state"), OrderState.PACKAGING);
    }
}
