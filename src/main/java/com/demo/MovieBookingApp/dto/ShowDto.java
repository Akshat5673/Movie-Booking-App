package com.demo.MovieBookingApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowDto {
    private LocalDateTime showTime;
    private Double price;
    private Long movieId;
    private Long theatreId;
}
