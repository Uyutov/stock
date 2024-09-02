package org.productinventoryservice.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.stream.Stream;

@Configuration
public class SecurityConfig {

    @Value("${cors.headers}")
    private String[] headers;
    @Value("${cors.origins}")
    private String[] origins;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.oauth2ResourceServer(oauth2 -> {
            oauth2.jwt(Customizer.withDefaults());
        });

        http.csrf(csrf -> csrf.disable());

        return http
                .authorizeHttpRequests(request -> {
                    request.requestMatchers("/product/{id}").hasRole("USER")
                            .requestMatchers("/product/product-page").hasRole("USER")
                            .requestMatchers("/product/**").hasRole("STOREKEEPER")
                            .requestMatchers("/warehouse/**").hasRole("STOREKEEPER")
                            .anyRequest().denyAll();
                })
                .build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        var converter = new JwtAuthenticationConverter();
        var jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

        converter.setPrincipalClaimName("preferred_username");

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            var authorities = jwtGrantedAuthoritiesConverter.convert(jwt);
            var roles = (List<String>) jwt.getClaimAsMap("realm_access").get("roles");

            return Stream.concat(authorities.stream(),
                    roles.stream()
                            .filter(role -> role.startsWith("ROLE_"))
                            .map(SimpleGrantedAuthority::new)
                            .map(GrantedAuthority.class::cast)
            ).toList();
        });

        return converter;
    }

    @Bean
    public WebMvcConfigurer corsConfig() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/product/**").allowedOrigins(origins).allowedHeaders(headers);
                registry.addMapping("/warehouse/**").allowedOrigins(origins).allowedHeaders(headers);
            }
        };
    }
}
