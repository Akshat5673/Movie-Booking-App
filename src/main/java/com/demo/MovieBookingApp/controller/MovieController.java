package com.demo.MovieBookingApp.controller;

import com.demo.MovieBookingApp.dto.BulkUploadMoviesResponseDto;
import com.demo.MovieBookingApp.dto.MovieDto;
import com.demo.MovieBookingApp.payload.ApiResponse;
import com.demo.MovieBookingApp.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping
    public ResponseEntity<List<MovieDto>> getAllMovies() {
        log.info("GET /api/movies called");
        List<MovieDto> allMovies = movieService.getAllMovies();
        return ResponseEntity.ok(allMovies);
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<MovieDto>> getByGenre(@PathVariable String genre) {
        log.info("GET /api/movies/genre/{} called", genre);
        List<MovieDto> movies = movieService.getMoviesByGenre(genre);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/language/{lang}")
    public ResponseEntity<List<MovieDto>> getByLanguage(@PathVariable("lang") String lang) {
        log.info("GET /api/movies/language/{} called", lang);
        List<MovieDto> movies = movieService.getMoviesByLanguage(lang);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<List<MovieDto>> getByTitle(@PathVariable String title) {
        log.info("GET /api/movies/title/{} called", title);
        List<MovieDto> movies = movieService.getMovieByTitle(title);
        return ResponseEntity.ok(movies);
    }

    @PostMapping
    public ResponseEntity<MovieDto> createMovie(@RequestBody MovieDto dto) {
        log.info("POST /api/movies called with body: {}", dto);
        return new ResponseEntity<>(movieService.createMovie(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieDto> updateMovie(
            @PathVariable Long id,
            @RequestBody MovieDto dto) {
        log.info("PUT /api/movies/{} called with body: {}", id, dto);
        return ResponseEntity.ok(movieService.updateMovie(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteMovie(@PathVariable Long id) {
        log.info("DELETE /api/movies/{} called", id);
        movieService.deleteMovie(id);
        return ResponseEntity.ok(new ApiResponse("Movie deleted successfully", true));
    }

    @PostMapping("/bulk-upload-movies")
    public ResponseEntity<BulkUploadMoviesResponseDto> bulkUpload(@RequestBody List<MovieDto> movieDtoList) {
        log.info("POST /api/movies/bulk called, count: {}", movieDtoList.size());
        BulkUploadMoviesResponseDto result = movieService.bulkUploadMovies(movieDtoList);
        return new ResponseEntity<>(result,HttpStatus.CREATED);
    }

}
