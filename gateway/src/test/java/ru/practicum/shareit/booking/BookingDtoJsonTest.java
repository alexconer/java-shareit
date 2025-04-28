package ru.practicum.shareit.booking;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingDtoJsonTest {

    @Autowired
    private JacksonTester<BookingRequestDto> json;

    private Validator validator;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testBookingDtoJson() throws Exception {

        LocalDateTime start = LocalDateTime.now().plusMinutes(1);
        LocalDateTime end = LocalDateTime.now().plusMinutes(2);

        BookingRequestDto bookingDto = BookingRequestDto.builder()
                .start(start)
                .end(end)
                .itemId(1L)
                .build();

        assertThat(json.write(bookingDto)).hasJsonPath("$.itemId");
        assertThat(json.write(bookingDto)).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(json.write(bookingDto)).hasJsonPath("$.start");
        assertThat(json.write(bookingDto)).extractingJsonPathStringValue("$.start").isEqualTo(start.format(formatter));
        assertThat(json.write(bookingDto)).hasJsonPath("$.end");
        assertThat(json.write(bookingDto)).extractingJsonPathStringValue("$.end").isEqualTo(end.format(formatter));
    }

    @Test
    void testJsonToBookingDto() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusMinutes(1);
        LocalDateTime end = LocalDateTime.now().plusMinutes(2);

        BookingRequestDto bookingDto = BookingRequestDto.builder()
                .start(start)
                .end(end)
                .itemId(1L)
                .build();

        String jsonTest = """
                {"itemId":1,"start":"%s","end":"%s"}
            """.formatted(start.format(formatter), end.format(formatter));

        BookingRequestDto parsedBookingDto = json.parseObject(jsonTest);

        assertThat(parsedBookingDto.getItemId()).isEqualTo(bookingDto.getItemId());
        assertThat(parsedBookingDto.getStart()).isEqualTo(bookingDto.getStart().format(formatter));
        assertThat(parsedBookingDto.getEnd()).isEqualTo(bookingDto.getEnd().format(formatter));
    }

    @Test
    void testInvalidBookingDtoJson() throws Exception {
        LocalDateTime start = LocalDateTime.now().minusMinutes(1);
        LocalDateTime end = LocalDateTime.now().minusMinutes(2);

        BookingRequestDto bookingDto = BookingRequestDto.builder()
                .start(start)
                .end(end)
                .build();

        Set<ConstraintViolation<BookingRequestDto>> violations = validator.validate(bookingDto);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(violation -> violation.getPropertyPath().toString().equals("itemId"));

        bookingDto.setItemId(1L);
        violations = validator.validate(bookingDto);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(violation -> violation.getPropertyPath().toString().equals("start"));

        start = LocalDateTime.now().plusMinutes(1);
        bookingDto.setStart(start);
        violations = validator.validate(bookingDto);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(violation -> violation.getPropertyPath().toString().equals("end"));

        end = LocalDateTime.now().plusMinutes(2);
        bookingDto.setEnd(end);
        violations = validator.validate(bookingDto);
        assertThat(violations).isEmpty();
    }
}
