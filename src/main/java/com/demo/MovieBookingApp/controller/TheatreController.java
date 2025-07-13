package com.demo.MovieBookingApp.controller;

import com.demo.MovieBookingApp.dto.BulkUploadTheatresResponseDto;
import com.demo.MovieBookingApp.dto.TheatreDto;
import com.demo.MovieBookingApp.payload.ApiResponse;
import com.demo.MovieBookingApp.service.TheatreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/theatre")
@RequiredArgsConstructor
public class TheatreController {

    @Autowired
    private final TheatreService theatreService;

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
    public ResponseEntity<TheatreDto> create(@RequestBody @Valid TheatreDto dto) {
        TheatreDto created = theatreService.createTheatre(dto);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TheatreDto> updateTheatre(
            @PathVariable Long id,
            @RequestBody @Valid TheatreDto dto) {
        return ResponseEntity.ok(theatreService.updateTheatre(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteTheatre(@PathVariable Long id) {
        theatreService.deleteTheatre(id);
        return ResponseEntity.ok(new ApiResponse("Theatre deleted successfully", true));
    }

    @PostMapping("/bulk")
    public ResponseEntity<BulkUploadTheatresResponseDto> bulk(@RequestBody @Valid List<TheatreDto> dtos) {
        BulkUploadTheatresResponseDto resp = theatreService.bulkUploadTheatres(dtos);
        return new ResponseEntity<>(resp, HttpStatus.MULTI_STATUS);
    }
}
