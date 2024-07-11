package org.orderservice.service.interfaces;

import org.orderservice.dto.order.OrderCreationDTO;
import org.orderservice.dto.order.OrderResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;

public interface OrderService {
    public ResponseEntity<OrderResponseDTO> getOrder(Long id);
    public ResponseEntity<Page<OrderResponseDTO>> getOrdersPage(Pageable pageable);
    public ResponseEntity<OrderResponseDTO> createOrder(OrderCreationDTO dto, Jwt jwt);
    public ResponseEntity<Object> orderPackaged(Long id);
    public ResponseEntity<Object> orderDelivered(Long id);
    public ResponseEntity<Object> orderReceived(Long id);
}
