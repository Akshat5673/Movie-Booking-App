package com.demo.MovieBookingApp.repository;

import com.demo.MovieBookingApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUserName(String name);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
