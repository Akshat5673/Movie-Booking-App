package com.demo.MovieBookingApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkUploadMoviesResponseDto {
    private List<MovieDto> successful;
    private List<String> errors;
}
