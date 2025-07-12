package com.demo.MovieBookingApp.model;

import com.demo.MovieBookingApp.enums.MovieGenre;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Movie Name cannot be blank!")
    @Size(max = 150 ,message = "Movie name cannot be more than 150 words!")
    private String movieName;
    @NotBlank
    @Size(max = 1000)
    private String description;
    @NotNull
    @Enumerated(EnumType.STRING)
    private MovieGenre genre;
    @NotNull
    @Positive
    private Integer duration;
    @NotNull
    private LocalDate releaseDate;
    @NotBlank
    @Size(max = 40)
    private String language;
    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true )
    private List<Show> shows = new ArrayList<>();
}
