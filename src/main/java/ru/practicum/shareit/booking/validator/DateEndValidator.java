package ru.practicum.shareit.booking.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

public class DateEndValidator implements ConstraintValidator<ValidDateEnd, BookingRequestDto> {
    @Override
    public boolean isValid(BookingRequestDto dto, ConstraintValidatorContext constraintValidatorContext) {
        return dto.getStart().isBefore(dto.getEnd());
    }
}
