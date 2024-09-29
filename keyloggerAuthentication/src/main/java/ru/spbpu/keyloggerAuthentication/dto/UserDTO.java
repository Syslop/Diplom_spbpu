package ru.spbpu.keyloggerAuthentication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private UUID id;

    private String login;

    private String password;
}
