package org.orderservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_entity", schema = "public")
public class User {
    @Id
    private String id;

    @NotBlank(message = "Username should be specified")
    @Column(name = "username")
    private String username;

    @Email(message = "Email is incorrect")
    private String email;
}
