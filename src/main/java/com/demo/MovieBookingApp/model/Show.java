package com.demo.MovieBookingApp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Show {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Showtime cannot be null!")
    private LocalDateTime showTime;
    @NotNull(message = "Price cannot be null!")
    @Positive(message = "Price cannot be negative!")
    private Double price;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "movie_id",nullable = false)
    private Movie movie;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "theatre_id",nullable = false)
    private Theatre theatre;
    @OneToMany(mappedBy = "show", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Booking> bookings;
}
