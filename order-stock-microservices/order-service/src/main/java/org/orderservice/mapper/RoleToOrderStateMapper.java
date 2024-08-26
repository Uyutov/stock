package org.orderservice.mapper;

import org.orderservice.entity.enums.OrderState;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class RoleToOrderStateMapper {

    public OrderState getOrderSateByRole(List<SimpleGrantedAuthority> authorities)
    {
        String role = authorities.stream()
                .filter(authority -> authority.getAuthority().startsWith("ROLE_"))
                .map(authority -> authority.getAuthority())
                .toList().get(0);
        switch (role){
            case ("ROLE_PACKER"):
                return OrderState.DELIVERING;
            case ("ROLE_DELIVERY"):
                return OrderState.AWAITING;
            case ("ROLE_PICKUP_MANAGER"):
                return OrderState.RECEIVED;
            default:
                return OrderState.PACKAGING;
        }
    }
}
