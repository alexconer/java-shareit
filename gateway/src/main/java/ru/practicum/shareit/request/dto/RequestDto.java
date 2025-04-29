package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RequestDto {
    private Long id;
    @NotEmpty(message = "Описание не может быть пустым")
    private String description;
    private LocalDateTime created;
}
