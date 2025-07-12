package com.demo.MovieBookingApp.controller;

import com.demo.MovieBookingApp.dto.ShowDto;
import com.demo.MovieBookingApp.payload.ApiResponse;
import com.demo.MovieBookingApp.service.ShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shows")
@RequiredArgsConstructor
public class ShowController {

    @Autowired
    private ShowService showService;


    @GetMapping
    public ResponseEntity<List<ShowDto>> getAllShows() {
        return ResponseEntity.ok(showService.getAllShows());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowDto> getShowById(@PathVariable Long id) {
        return ResponseEntity.ok(showService.getShowById(id));
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<ShowDto>> getByMovie(@PathVariable Long movieId) {
        return ResponseEntity.ok(showService.getAllShowsByMovie(movieId));
    }

    @GetMapping("/theatre/{theatreId}")
    public ResponseEntity<List<ShowDto>> getByTheatre(@PathVariable Long theatreId) {
        return ResponseEntity.ok(showService.getAllShowsByTheatre(theatreId));
    }

    @GetMapping("/movie/{movieId}/theatre/{theatreId}")
    public ResponseEntity<List<ShowDto>> getByMovieAndTheatre(
            @PathVariable Long movieId,
            @PathVariable Long theatreId) {
        return ResponseEntity.ok(
                showService.getShowsByMovieInTheatre(movieId, theatreId)
        );
    }

    @PostMapping
    public ResponseEntity<ShowDto> createShow(@RequestBody ShowDto dto) {
        ShowDto created = showService.createShow(dto);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShowDto> updateShow(@PathVariable Long id, @RequestBody ShowDto dto) {
        return ResponseEntity.ok(showService.updateShow(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteShow(@PathVariable Long id) {
        showService.deleteShow(id);
        return ResponseEntity.ok(new ApiResponse("Show deleted successfully", true));
    }

}
