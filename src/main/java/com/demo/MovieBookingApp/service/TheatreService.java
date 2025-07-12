package com.demo.MovieBookingApp.service;

import com.demo.MovieBookingApp.dto.BulkUploadTheatresResponseDto;
import com.demo.MovieBookingApp.dto.TheatreDto;

import java.util.List;

public interface TheatreService {
    List<TheatreDto> getAllTheatres();
    List<TheatreDto> getTheatresByScreenType(String screenType);
    TheatreDto getTheatreById(Long id);
    TheatreDto createTheatre(TheatreDto theatreDto);
    TheatreDto updateTheatre(Long id, TheatreDto theatreDto);
    void deleteTheatre(Long id);
    BulkUploadTheatresResponseDto bulkUploadTheatres(List<TheatreDto> theatres);
}
