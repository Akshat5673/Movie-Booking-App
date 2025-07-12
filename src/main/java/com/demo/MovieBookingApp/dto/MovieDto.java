package com.demo.MovieBookingApp.dto;

import com.demo.MovieBookingApp.enums.MovieGenre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {
    private String movieName;
    private String description;
    private MovieGenre genre;
    private Integer duration;
    private LocalDate releaseDate;
    private String language;
}
