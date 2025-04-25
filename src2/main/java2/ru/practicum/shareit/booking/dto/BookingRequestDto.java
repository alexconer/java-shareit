package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.validator.ValidDateEnd;

import java.time.LocalDateTime;

@Data
@Builder
@ValidDateEnd
public class BookingRequestDto {
    @NotNull(message = "Не указан идентификатор вещи")
    private Long itemId;
    @NotNull(message = "Не указана дата начала бронирования")
    @Future(message = "Дата начала бронирования не может быть в прошлом")
    private LocalDateTime start;
    @NotNull(message = "Не указана дата окончания бронирования")
    @Future(message = "Дата окончания бронирования не может быть в прошлом")
    private LocalDateTime end;
}
