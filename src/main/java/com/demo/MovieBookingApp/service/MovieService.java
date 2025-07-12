package com.demo.MovieBookingApp.service;

import com.demo.MovieBookingApp.dto.BulkUploadMoviesResponseDto;
import com.demo.MovieBookingApp.dto.MovieDto;

import java.util.List;

public interface MovieService {
    List<MovieDto> getAllMovies();
    List<MovieDto> getMoviesByGenre(String genre);
    List<MovieDto> getMoviesByLanguage(String language);
    List<MovieDto> getMovieByTitle(String title);
    MovieDto createMovie(MovieDto movieDto);
    MovieDto updateMovie(Long id, MovieDto movieDto);
    void deleteMovie(Long id);
    BulkUploadMoviesResponseDto bulkUploadMovies(List<MovieDto> movies);
}
