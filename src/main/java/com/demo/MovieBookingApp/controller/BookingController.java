package com.demo.MovieBookingApp.controller;

import com.demo.MovieBookingApp.dto.BookingDto;
import com.demo.MovieBookingApp.dto.BookingResponseDto;
import com.demo.MovieBookingApp.enums.BookingStatus;
import com.demo.MovieBookingApp.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDto> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping("/show/{showId}")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByShow(@PathVariable Long showId) {
        return ResponseEntity.ok(bookingService.getBookingsByShow(showId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(bookingService.getBookingsByUser(userId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByStatus(@PathVariable BookingStatus status) {
        return ResponseEntity.ok(bookingService.getBookingsByStatus(status));
    }

    @PostMapping("/create-booking")
    public ResponseEntity<BookingResponseDto> createBooking( @Valid @RequestBody BookingDto bookingDto){
        BookingResponseDto newBooking = bookingService.createBooking(bookingDto);
        return ResponseEntity.status(201).body(newBooking);
    }

    @PutMapping("/confirm/{id}")
    public ResponseEntity<BookingResponseDto> confirmPayment(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.confirmBooking(id));
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<BookingResponseDto> cancelPayment(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }

}
