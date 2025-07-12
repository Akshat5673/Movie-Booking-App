package com.demo.MovieBookingApp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Theatre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(max = 120)
    @Column(name = "theatre_name", nullable = false)
    private String theatreName;

    @NotBlank
    @Size(max = 120)
    @Column(name = "theatre_location", nullable = false)
    private String theatreLocation;

    @NotNull
    @Positive
    private Integer capacity;

    @NotBlank
    @Size(max = 50)
    private String screenType;
    @OneToMany(mappedBy = "theatre", fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true )
    private List<Show> shows = new ArrayList<>();

}
