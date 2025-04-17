package ru.practicum.shareit.booking.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateEndValidator.class)
public @interface ValidDateEnd {
    String message() default "Дата окончания должна быть позже даты начала";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
