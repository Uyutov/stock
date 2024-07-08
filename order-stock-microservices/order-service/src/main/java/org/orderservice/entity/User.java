package org.orderservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "user", schema = "public")
public class User {
    @Id
    private String id;

    @NotBlank(message = "Username should be specified")
    private String username;
    @Email(message = "Email is incorrect")
    private String email;
}
