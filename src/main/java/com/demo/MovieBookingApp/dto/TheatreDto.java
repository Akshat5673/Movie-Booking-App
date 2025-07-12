package com.demo.MovieBookingApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TheatreDto {
    private String theatreName;
    private String theatreLocation;
    private Integer capacity;
    private String screenType;
}
