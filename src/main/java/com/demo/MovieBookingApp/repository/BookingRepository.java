package com.demo.MovieBookingApp.repository;

import com.demo.MovieBookingApp.enums.BookingStatus;
import com.demo.MovieBookingApp.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {
    List<Booking> findAllByStatus(BookingStatus status);
    List<Booking> findAllByUserId(Long userId);
    List<Booking> findAllByShowId(Long showId);
}
