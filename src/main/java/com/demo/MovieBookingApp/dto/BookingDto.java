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
public class BookingDto {
    private Integer seats;
//    private LocalDateTime bookingTime;
//    private BookingStatus status;
    private List<String> seatNumbers;
    private Long userId;
    private Long showId;
}
