package com.demo.MovieBookingApp.repository;

import com.demo.MovieBookingApp.model.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TheatreRepository extends JpaRepository<Theatre,Long> {
    List<Theatre> findByScreenType(String screenType);
}
