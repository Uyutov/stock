package org.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("order", route -> route.path("/api/v1/order/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://ORDER-SERVICE"))
                .route("users", route -> route.path("/api/v1/users/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://USER-SERVICE"))
                .route("product", route -> route.path("/api/v1/product/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://PRODUCT-WAREHOUSE-SERVICE"))
                .route("warehouse", route -> route.path("/api/v1/warehouse/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://PRODUCT-WAREHOUSE-SERVICE"))
                .build();
    }

}
