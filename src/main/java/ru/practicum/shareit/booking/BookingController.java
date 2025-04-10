package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Slf4j
public class BookingController {
    private static final String USER_HEADER_NAME = "X-Sharer-User-Id";

    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(@RequestHeader(USER_HEADER_NAME) Long userId, @RequestBody @Valid BookingRequestDto bookingRequestDto) {
        log.info("Получен запрос на создание бронирования: {}", bookingRequestDto);
        return bookingService.createBooking(userId, bookingRequestDto);
    }
}
