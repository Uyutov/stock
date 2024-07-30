package org.productinventoryservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue productAvailabilityQueue()
    {
        return new Queue("product.availability.request");
    }

    @Bean
    public DirectExchange exchange(){
        return new DirectExchange("product.availability");
    }

    @Bean
    public Binding binding(DirectExchange exchange, Queue queue)
    {
        return BindingBuilder.bind(queue).to(exchange).with("product");
    }
}
