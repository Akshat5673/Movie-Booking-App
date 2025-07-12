package com.demo.MovieBookingApp.model;

import com.demo.MovieBookingApp.enums.BookingStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Positive
    private Integer numberOfSeats;
    @NotNull
    private LocalDateTime bookingTime;
    @NotNull
    @Positive
    private Double price;
    @NotNull
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "booking_seat_numbers", joinColumns = @JoinColumn(name = "booking_id"))  // creates a separate table with the entity id(here booking id) and the field as column
    @Column(name = "seat_number", nullable = false)
    @Size(min = 1)
    private List<@NotBlank String> seatNumbers;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "show_id",nullable = false)
    private Show show;
}
