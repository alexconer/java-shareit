package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Slf4j
public class BookingController {
    private static final String USER_HEADER_NAME = "X-Sharer-User-Id";

    private final BookingService bookingService;

    @GetMapping
    public Collection<BookingDto> getAllBooking(@RequestHeader(USER_HEADER_NAME) Long userId, @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("Получен запрос на получение всех бронирований пользователя с id: {}", userId);
        return bookingService.getAllBooking(userId, state);
    }

    @GetMapping("/{id}")
    public BookingDto getBookingById(@RequestHeader(USER_HEADER_NAME) Long userId, @PathVariable Long id) {
        log.info("Получен запрос на получение бронирования с id: {}", id);
        return bookingService.getBookingById(userId, id);
    }

    @GetMapping("/owner")
    public Collection<BookingDto> getAllBookingByOwner(@RequestHeader(USER_HEADER_NAME) Long userId, @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("Получен запрос на получение всех бронирований пользователя с id: {}", userId);
        return bookingService.getAllBookingByOwner(userId, state);
    }

    @PostMapping
    public BookingDto createBooking(@RequestHeader(USER_HEADER_NAME) Long userId, @RequestBody BookingRequestDto bookingRequestDto) {
        log.info("Получен запрос на создание бронирования: {}", bookingRequestDto);
        return bookingService.createBooking(userId, bookingRequestDto);
    }

    @PatchMapping("/{id}")
    public BookingDto approveBooking(@RequestHeader(USER_HEADER_NAME) Long userId, @PathVariable Long id, @RequestParam Boolean approved) {
        log.info("Получен запрос на подтверждение бронирования с id: {}", id);
        return bookingService.approveBooking(userId, id, approved);
    }

}
