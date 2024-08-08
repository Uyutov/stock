package org.orderservice.controller;

import org.orderservice.dto.order.OrderCreationDTO;
import org.orderservice.dto.order.OrderRequestDTO;
import org.orderservice.dto.order.OrderResponseDTO;
import org.orderservice.dto.order.OrderStatusChangeDTO;
import org.orderservice.service.interfaces.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/id")
    public ResponseEntity<OrderResponseDTO> getOrderById(OrderRequestDTO dto) {
        return ResponseEntity.ok(orderService.getOrder(dto));
    }

    @GetMapping("/page")
    public ResponseEntity<Page<OrderResponseDTO>> getOrdersPage(Pageable pageable) {
        return ResponseEntity.ok(orderService.getOrdersPage(pageable));
    }

    @PostMapping("/create-order")
    public ResponseEntity<Object> createOrder(OrderCreationDTO dto, @AuthenticationPrincipal Jwt jwt)
    {
        return new ResponseEntity<>(orderService.createOrder(dto, jwt), HttpStatusCode.valueOf(201));
    }

    @PutMapping("/order-packaged")
    public ResponseEntity<OrderResponseDTO> packageOrder(OrderStatusChangeDTO dto){
        return ResponseEntity.ok(orderService.changeOrderStatus(dto));
    };

    @PutMapping("/order-delivered")
    public ResponseEntity<OrderResponseDTO> deliveredOrder(OrderStatusChangeDTO dto){
        return ResponseEntity.ok(orderService.changeOrderStatus(dto));
    };

    @PutMapping("/order-received")
    public ResponseEntity<OrderResponseDTO> orderReceived(OrderStatusChangeDTO dto){
        return ResponseEntity.ok(orderService.changeOrderStatus(dto));
    };
}
