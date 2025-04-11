package ru.practicum.shareit.booking;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dal.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public Collection<BookingDto> getAllBooking(Long userId, BookingState state) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AccessDeniedException("Пользователь не найден"));

        Collection<Booking> bookings = switch (state) {
            case ALL -> bookingRepository.findAllByBooker(user);
            case CURRENT -> bookingRepository.findAllCurrentByBooker(user, LocalDateTime.now());
            case PAST -> bookingRepository.findAllPastByBooker(user, LocalDateTime.now());
            case FUTURE -> bookingRepository.findAllFutureByBooker(user, LocalDateTime.now());
            case WAITING -> bookingRepository.findAllByBookerAndStatus(user, BookingStatus.WAITING);
            case REJECTED -> bookingRepository.findAllByBookerAndStatus(user, BookingStatus.REJECTED);
        };

        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }

    public Collection<BookingDto> getAllBookingByOwner(Long userId, BookingState state) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AccessDeniedException("Пользователь не найден"));

        Collection<Booking> bookings = switch (state) {
            case ALL -> bookingRepository.findAllByItemOwner(user);
            case CURRENT -> bookingRepository.findAllCurrentByItemOwner(user, LocalDateTime.now());
            case PAST -> bookingRepository.findAllPastByItemOwner(user, LocalDateTime.now());
            case FUTURE -> bookingRepository.findAllFutureByItemOwner(user, LocalDateTime.now());
            case WAITING -> bookingRepository.findAllByItemOwnerAndStatus(user, BookingStatus.WAITING);
            case REJECTED -> bookingRepository.findAllByItemOwnerAndStatus(user, BookingStatus.REJECTED);
        };
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }

    public BookingDto getBookingById(Long userId, Long bookingId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AccessDeniedException("Пользователь не найден"));
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Бронирование не найдено"));
        if (!booking.getBooker().getId().equals(user.getId()) && !booking.getItem().getOwner().equals(user.getId())) {
            throw new NotFoundException("Бронирование не найдено");
        }
        return BookingMapper.toBookingDto(booking);
    }

    @Transactional
    public BookingDto createBooking(Long userId, BookingRequestDto bookingRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AccessDeniedException("Пользователь не найден"));
        Item item = itemRepository.findById(bookingRequestDto.getItemId()).orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        if (!item.getAvailable()) {
            throw new ValidationException("Вещь недоступна для бронирования");
        }
        if (item.getOwner().equals(user.getId())) {
            throw new NotFoundException("Владелец вещи не может бронировать свою вещь");
        }

        Booking booking = BookingMapper.toBookingModel(bookingRequestDto);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Transactional
    public BookingDto approveBooking(Long userId, Long bookingId, Boolean approved) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AccessDeniedException("Пользователь не найден"));

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Бронирование не найдено"));
        if (!booking.getItem().getOwner().equals(userId)) {
            throw new NotFoundException("Пользователь не является владельцем вещи");
        }
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }
}
