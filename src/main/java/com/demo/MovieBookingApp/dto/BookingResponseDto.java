package com.demo.MovieBookingApp.dto;

import com.demo.MovieBookingApp.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDto {
    private Long bookingId;
    private Integer numberOfSeats;
    private LocalDateTime bookingTime;
    private Double price;
    private BookingStatus status;
    private String movieName;
    private LocalDateTime showTime;
    private String theatreName;
    private String screenType;
    private String userName;
    private List<String> seatNumbers;
}
