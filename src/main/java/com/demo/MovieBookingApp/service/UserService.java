package com.demo.MovieBookingApp.service;

import com.demo.MovieBookingApp.dto.CreateUserDto;
import com.demo.MovieBookingApp.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(CreateUserDto createUserDto);
    List<UserDto> getAllUsers();
    UserDto getUserById(Long id);
    UserDto updateUser(Long id, CreateUserDto updateUserDto);
    void deleteUser(Long id);
    UserDto getUserByEmail(String email);
    UserDto getUserByUsername(String username);
}
