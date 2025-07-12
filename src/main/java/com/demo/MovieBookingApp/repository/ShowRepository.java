package com.demo.MovieBookingApp.repository;

import com.demo.MovieBookingApp.model.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show,Long> {
    List<Show> findByMovieId(Long movieId);
    List<Show> findByTheatreId(Long theatreId);
    List<Show> findByMovieIdAndTheatreId(Long movieId, Long theatreId);
}
