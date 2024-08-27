package org.orderservice.service.interfaces;

import org.orderservice.dto.order.OrderCreationDTO;
import org.orderservice.dto.order.OrderResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    public OrderResponseDTO getOrder(Long id);
    public Page<OrderResponseDTO> getOrdersPage(Pageable pageable);
    public OrderResponseDTO createOrder(OrderCreationDTO dto);
    public OrderResponseDTO changeOrderStatus(Long id);
}
