package org.productinventoryservice.component;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProductAvailabilityReceiver {

    @RabbitListener
    public void checkProductAvailability() {
        //TODO implement logic of receiving, converting to dto and checking for availability
    }

}
