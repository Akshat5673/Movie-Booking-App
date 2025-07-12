package com.demo.MovieBookingApp.service;

import com.demo.MovieBookingApp.dto.BulkUploadShowsResponseDto;
import com.demo.MovieBookingApp.dto.ShowDto;

import java.util.List;

public interface ShowService {
    List<ShowDto> getAllShows();
    ShowDto getShowById(Long id);
    List<ShowDto> getAllShowsByMovie(Long movieId);
    List<ShowDto> getAllShowsByTheatre(Long theatreId);
    List<ShowDto> getShowsByMovieInTheatre(Long movieId, Long theatreId);
    ShowDto createShow(ShowDto showDto);
    ShowDto updateShow(Long id, ShowDto showDto);

    BulkUploadShowsResponseDto bulkUploadShows(List<ShowDto> shows);

    void deleteShow(Long id);
}
