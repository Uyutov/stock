package org.orderservice.service.interfaces;

import org.orderservice.dto.order.OrderCreationDTO;
import org.orderservice.dto.order.OrderRequestDTO;
import org.orderservice.dto.order.OrderResponseDTO;
import org.orderservice.dto.order.OrderStatusChangeDTO;
import org.orderservice.entity.enums.OrderState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;

public interface OrderService {
    public OrderResponseDTO getOrder(Long id);
    public Page<OrderResponseDTO> getOrdersPage(Pageable pageable);
    public OrderResponseDTO createOrder(OrderCreationDTO dto);
    public OrderResponseDTO changeOrderStatus(Long id, OrderState state);
}
