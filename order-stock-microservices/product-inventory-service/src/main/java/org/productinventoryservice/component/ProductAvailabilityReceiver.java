package org.productinventoryservice.component;


import org.productinventoryservice.dto.product.ProductSubtractionTransactionDTO;
import org.productinventoryservice.service.interfaces.ProductService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductAvailabilityReceiver {

    private final ProductService productService;

    public ProductAvailabilityReceiver(ProductService productService) {
        this.productService = productService;
    }

    @RabbitListener(queues = "check")
    public boolean checkProductAvailability(List<ProductSubtractionTransactionDTO> products) {
        return productService.checkForProductAvailabilityAndSubtractThem(products);
    }

}
