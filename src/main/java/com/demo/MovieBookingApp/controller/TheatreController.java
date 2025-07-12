package com.demo.MovieBookingApp.controller;

import com.demo.MovieBookingApp.dto.TheatreDto;
import com.demo.MovieBookingApp.payload.ApiResponse;
import com.demo.MovieBookingApp.service.TheatreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/theatre")
public class TheatreController {

    @Autowired
    private TheatreService theatreService;

    @GetMapping
    public ResponseEntity<List<TheatreDto>> getAllTheatres() {
        return ResponseEntity.ok(theatreService.getAllTheatres());
    }

    @GetMapping("/screenType/{type}")
    public ResponseEntity<List<TheatreDto>> getByScreenType(@PathVariable("type") String type) {
        return ResponseEntity.ok(theatreService.getTheatresByScreenType(type));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TheatreDto> getTheatreById(@PathVariable Long id) {
        return ResponseEntity.ok(theatreService.getTheatreById(id));
    }

    @PostMapping
    public ResponseEntity<TheatreDto> createTheatre(@RequestBody TheatreDto dto) {
        TheatreDto created = theatreService.createTheatre(dto);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TheatreDto> updateTheatre(
            @PathVariable Long id,
            @RequestBody TheatreDto dto) {
        return ResponseEntity.ok(theatreService.updateTheatre(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteTheatre(@PathVariable Long id) {
        theatreService.deleteTheatre(id);
        return ResponseEntity.ok(new ApiResponse("Theatre deleted successfully", true));
    }
}
