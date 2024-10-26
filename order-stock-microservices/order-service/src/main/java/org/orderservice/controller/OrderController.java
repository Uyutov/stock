package org.orderservice.controller;

import org.orderservice.dto.order.OrderCreationDTO;
import org.orderservice.dto.order.OrderResponseDTO;
import org.orderservice.service.interfaces.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(orderService.getOrder(id));
    }

    @GetMapping("/page")
    public ResponseEntity<Page<OrderResponseDTO>> getOrdersPage(Pageable pageable) {
        return ResponseEntity.ok(orderService.getOrdersPage(pageable));
    }

    @PostMapping("/create-order")
    public ResponseEntity<Object> createOrder(@RequestBody OrderCreationDTO dto)
    {
        return new ResponseEntity<>(orderService.createOrder(dto), HttpStatusCode.valueOf(201));
    }

    @PutMapping("/{id}/change-state")
    public ResponseEntity<OrderResponseDTO> packageOrder(@PathVariable("id") Long id){
        return ResponseEntity.ok(orderService.changeOrderStatus(id));
    };
}
