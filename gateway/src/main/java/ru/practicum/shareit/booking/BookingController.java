package ru.practicum.shareit.booking;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

	private static final String USER_HEADER_NAME = "X-Sharer-User-Id";

	private final BookingClient bookingClient;

	@GetMapping
	public ResponseEntity<Object> getAllBooking(@RequestHeader(USER_HEADER_NAME) Long userId, @RequestParam(defaultValue = "all") String state) {
		BookingState bookingState = BookingState.from(state)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
		log.info("Получен запрос на получение всех бронирований пользователя с id={}, userId={}", state, userId);
		return bookingClient.getAllBooking(userId, bookingState);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> getBookingById(@RequestHeader(USER_HEADER_NAME) Long userId, @PathVariable Long id) {
		log.info("Получен запрос на получение бронирования с id={}, userId={}", id, userId);
		return bookingClient.getBookingById(userId, id);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getAllBookingByOwner(@RequestHeader(USER_HEADER_NAME) Long userId, @RequestParam(defaultValue = "ALL") String state) {
		BookingState bookingState = BookingState.from(state)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
		log.info("Получен запрос на получение всех бронирований пользователя с id: {}", userId);
		return bookingClient.getAllBookingByOwner(userId, bookingState);
	}

	@PostMapping
	public ResponseEntity<Object> createBooking(@RequestHeader(USER_HEADER_NAME) Long userId, @RequestBody @Valid BookingRequestDto bookingRequestDto) {
		log.info("Получен запрос на создание бронирования: {}", bookingRequestDto);
		return bookingClient.createBooking(userId, bookingRequestDto);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Object> approveBooking(@RequestHeader(USER_HEADER_NAME) Long userId, @PathVariable Long id, @RequestParam Boolean approved) {
		log.info("Получен запрос на подтверждение бронирования с id: {}", id);
		return bookingClient.approveBooking(userId, id, approved);
	}


}
