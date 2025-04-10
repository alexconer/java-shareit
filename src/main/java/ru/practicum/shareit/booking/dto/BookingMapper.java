package ru.practicum.shareit.booking.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.Booking;

@UtilityClass
public class BookingMapper {
    public Booking toBookingModel(BookingRequestDto dto) {
        Booking booking = new Booking();
        booking.setItem(dto.getItemId());
        booking.setStart(dto.getStart());
        booking.setEnd(dto.getEnd());
        return booking;
    }

    public BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .bookerId(booking.getBooker())
                .itemId(booking.getItem())
                .build();
    }
}
