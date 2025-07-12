package com.demo.MovieBookingApp.repository;

import com.demo.MovieBookingApp.enums.MovieGenre;
import com.demo.MovieBookingApp.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie,Long> {
    List<Movie> findByGenre(MovieGenre genre);
    List<Movie> findByLanguage(String language);
    List<Movie> findByMovieNameContainingIgnoreCase(String title);
    boolean existsByMovieName(String movieName);
    List<Movie> findByMovieNameIn(List<String> names);
}
