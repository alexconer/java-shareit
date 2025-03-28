package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private Long id;
    @NotEmpty(message = "Имя пользователя не может быть пустым")
    private String name;
    @NotEmpty(message = "Email пользователя не может быть пустым")
    @Email(message = "Некорректный email")
    private String email;
}
