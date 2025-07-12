package com.demo.MovieBookingApp.adapter;

import com.demo.MovieBookingApp.dto.BookingResponseDto;
import com.demo.MovieBookingApp.model.Booking;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public final class BookingMapper {
    public static BookingResponseDto toResponseDto(Booking booking) {
        return new BookingResponseDto(
                booking.getId(),
                booking.getNumberOfSeats(),
                booking.getBookingTime(),
                booking.getPrice(),
                booking.getStatus(),
                booking.getShow().getMovie().getMovieName(),
                booking.getShow().getShowTime(),
                booking.getShow().getTheatre().getTheatreName(),
                booking.getShow().getTheatre().getScreenType(),
                booking.getUser().getUserName(),
                booking.getSeatNumbers()
        );
    }

    public static List<BookingResponseDto> toResponseDtoList(List<Booking> bookings) {
        return bookings.stream().map(BookingMapper::toResponseDto).toList();
    }
}
