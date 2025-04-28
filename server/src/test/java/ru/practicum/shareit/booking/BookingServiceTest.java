package ru.practicum.shareit.booking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class BookingServiceTest {

    private final BookingService bookingService;
    private final ItemService itemService;
    private final UserService userService;
    private final EntityManager em;

    @Test
    void addBookingTest() {

        UserDto newUserDto = getUserDto("user1", "user1@mail.ru");
        newUserDto = userService.addUser(newUserDto);

        ItemDto newItemDto = getItemDto("item1", "description1", true);
        newItemDto = itemService.addItem(newUserDto.getId(), newItemDto);

        UserDto newUserDto2 = getUserDto("user2", "user2@mail.ru");
        newUserDto2 = userService.addUser(newUserDto2);

        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(2);

        BookingRequestDto newBookingDto = getBookingDto(newItemDto.getId(), start, end);
        BookingDto savedBookingDto = bookingService.createBooking(newUserDto2.getId(), newBookingDto);

        TypedQuery<Booking> query = em.createQuery("SELECT b FROM Booking b WHERE b.id = :id", Booking.class);
        query.setParameter("id", savedBookingDto.getId());
        Booking booking = query.getSingleResult();

        assertThat(booking.getId()).isNotNull();
        assertThat(booking.getStart()).isEqualTo(start);
        assertThat(booking.getEnd()).isEqualTo(end);
        assertThat(booking.getItem().getId()).isEqualTo(newItemDto.getId());
        assertThat(booking.getBooker().getId()).isEqualTo(newUserDto2.getId());
    }

    @Test
    void getBookingByIdTest() {
        UserDto newUserDto = getUserDto("user1", "user1@mail.ru");
        newUserDto = userService.addUser(newUserDto);

        ItemDto newItemDto = getItemDto("item1", "description1", true);
        newItemDto = itemService.addItem(newUserDto.getId(), newItemDto);

        UserDto newUserDto2 = getUserDto("user2", "user2@mail.ru");
        newUserDto2 = userService.addUser(newUserDto2);

        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(2);

        BookingRequestDto newBookingDto = getBookingDto(newItemDto.getId(), start, end);
        BookingDto savedBookingDto = bookingService.createBooking(newUserDto2.getId(), newBookingDto);

        BookingDto bookingDto = bookingService.getBookingById(savedBookingDto.getId(), newUserDto.getId());

        assertThat(bookingDto.getId()).isNotNull();
        assertThat(bookingDto.getStart()).isEqualTo(start);
        assertThat(bookingDto.getEnd()).isEqualTo(end);
        assertThat(bookingDto.getItem().getId()).isEqualTo(newItemDto.getId());
        assertThat(bookingDto.getBooker().getId()).isEqualTo(newUserDto2.getId());
        assertThat(bookingDto.getStatus()).isEqualTo(BookingStatus.WAITING);
    }

    private UserDto getUserDto(String name, String email) {
        return UserDto.builder()
                .name(name)
                .email(email)
                .build();
    }

    private ItemDto getItemDto(String name, String description, Boolean available) {
        return ItemDto.builder()
                .name(name)
                .description(description)
                .available(available)
                .build();
    }

    private BookingRequestDto getBookingDto(long itemId, LocalDateTime start, LocalDateTime end) {
        return BookingRequestDto.builder()
                .itemId(itemId)
                .start(start)
                .end(end)
                .build();
    }
}
