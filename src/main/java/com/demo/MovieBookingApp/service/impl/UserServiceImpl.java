package com.demo.MovieBookingApp.service.impl;

import com.demo.MovieBookingApp.adapter.GenericEntityDtoAdapter;
import com.demo.MovieBookingApp.dto.CreateUserDto;
import com.demo.MovieBookingApp.dto.UserDto;
import com.demo.MovieBookingApp.exception.AlreadyExistsException;
import com.demo.MovieBookingApp.exception.ResourceNotFoundException;
import com.demo.MovieBookingApp.model.User;
import com.demo.MovieBookingApp.repository.UserRepository;
import com.demo.MovieBookingApp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;

    @Override
    public UserDto createUser(CreateUserDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new AlreadyExistsException("User", "email", dto.getEmail());
        }
        User user = GenericEntityDtoAdapter.toEntityObject(dto, User.class);
        User saved = userRepository.save(user);
        return GenericEntityDtoAdapter.toDtoObject(saved, UserDto.class);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return GenericEntityDtoAdapter.toDtoList(userRepository.findAll(), UserDto.class);
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id.toString()));
        return GenericEntityDtoAdapter.toDtoObject(user, UserDto.class);
    }

    @Override
    public UserDto updateUser(Long id, CreateUserDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id.toString()));

        user.setUserName(dto.getUserName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        User updated = userRepository.save(user);
        return GenericEntityDtoAdapter.toDtoObject(updated, UserDto.class);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id.toString()));
        userRepository.delete(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return GenericEntityDtoAdapter.toDtoObject(user, UserDto.class);
    }

    @Override
    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return GenericEntityDtoAdapter.toDtoObject(user, UserDto.class);
    }
}
