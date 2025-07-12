package com.demo.MovieBookingApp.service;

import com.demo.MovieBookingApp.dto.BookingDto;
import com.demo.MovieBookingApp.dto.BookingResponseDto;
import com.demo.MovieBookingApp.enums.BookingStatus;

import java.util.List;

public interface BookingService {
    BookingResponseDto createBooking(BookingDto bookingDto);
    BookingResponseDto confirmBooking(Long bookingId);
    BookingResponseDto cancelBooking(Long bookingId);
    List<BookingResponseDto> getBookingsByUser(Long userId);
    List<BookingResponseDto> getBookingsByShow(Long showId);
    List<BookingResponseDto> getBookingsByStatus(BookingStatus status);
    BookingResponseDto getBookingById(Long bookingId);
}
