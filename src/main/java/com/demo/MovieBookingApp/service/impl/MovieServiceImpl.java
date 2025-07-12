package com.demo.MovieBookingApp.service.impl;

import com.demo.MovieBookingApp.adapter.GenericEntityDtoAdapter;
import com.demo.MovieBookingApp.dto.BulkUploadMoviesResponseDto;
import com.demo.MovieBookingApp.dto.MovieDto;
import com.demo.MovieBookingApp.enums.MovieGenre;
import com.demo.MovieBookingApp.exception.AlreadyExistsException;
import com.demo.MovieBookingApp.exception.BadCredentialsException;
import com.demo.MovieBookingApp.exception.ResourceNotFoundException;
import com.demo.MovieBookingApp.model.Movie;
import com.demo.MovieBookingApp.repository.MovieRepository;
import com.demo.MovieBookingApp.service.MovieService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;

    @Override
    public List<MovieDto> getAllMovies() {
        log.info("Fetching all movies from repository");
        List<Movie> movies = movieRepository.findAll();
        return GenericEntityDtoAdapter.toDtoList(movies, MovieDto.class);
    }

    @Override
    public List<MovieDto> getMoviesByGenre(String genreStr) {
        log.info("Fetching movies by genre: {}", genreStr);
        MovieGenre genre;
        try {
            genre = MovieGenre.valueOf(genreStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("Invalid genre provided: {}", genreStr);
            throw new BadCredentialsException("Invalid genre: " + genreStr);
        }
        List<Movie> movies = movieRepository.findByGenre(genre);
        return GenericEntityDtoAdapter.toDtoList(movies, MovieDto.class);
    }

    @Override
    public List<MovieDto> getMoviesByLanguage(String language) {
        log.info("Fetching movies by language: {}", language);
        List<Movie> movies = movieRepository.findByLanguage(language);
        return GenericEntityDtoAdapter.toDtoList(movies, MovieDto.class);
    }

    @Override
    public List<MovieDto> getMovieByTitle(String title) {
        log.info("Fetching movies by title: {}", title);
        List<Movie> movies = movieRepository.findByMovieNameContainingIgnoreCase(title);
        if (movies.isEmpty()) {
            log.error("No movies found with title: {}", title);
            throw new ResourceNotFoundException("Movie", "title", title);
        }
        return GenericEntityDtoAdapter.toDtoList(movies, MovieDto.class);
    }

    @Override
    public MovieDto createMovie(MovieDto movieDto) {
        log.info("Creating movie: {}", movieDto);
        if (movieRepository.existsByMovieName(movieDto.getMovieName())) {
            log.error("Movie already exists: {}", movieDto.getMovieName());
            throw new AlreadyExistsException("Movie", "name", movieDto.getMovieName());
        }
        Movie movie = GenericEntityDtoAdapter.toEntityObject(movieDto, Movie.class);
        Movie saved = movieRepository.save(movie);
        return GenericEntityDtoAdapter.toDtoObject(saved, MovieDto.class);
    }

    @Override
    public MovieDto updateMovie(Long id, MovieDto movieDto) {
        log.info("Updating movie with ID: {}", id);
        Movie existing = movieRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Movie not found with ID: {}", id);
                    return new ResourceNotFoundException("Movie", "id", String.valueOf(id));
                });
        existing.setMovieName(movieDto.getMovieName());
        existing.setDescription(movieDto.getDescription());
        existing.setGenre(movieDto.getGenre());
        existing.setDuration(movieDto.getDuration());
        existing.setReleaseDate(movieDto.getReleaseDate());
        existing.setLanguage(movieDto.getLanguage());
        Movie updated = movieRepository.save(existing);
        return GenericEntityDtoAdapter.toDtoObject(updated, MovieDto.class);
    }

    @Override
    public void deleteMovie(Long id) {
        log.info("Deleting movie with ID: {}", id);
        Movie existing = movieRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Movie not found for deletion with ID: {}", id);
                    return new ResourceNotFoundException("Movie", "id", String.valueOf(id));
                });
        movieRepository.delete(existing);
    }

    @Override
    public BulkUploadMoviesResponseDto bulkUploadMovies(List<MovieDto> movies) {
        log.info("Bulk upload of movies, count: {}", movies.size());
        if (movies.isEmpty()) {
            throw new BadCredentialsException("Movie list cannot be empty");
        }
        List<String> names = movies.stream()
                .map(MovieDto::getMovieName)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        Set<String> nameSet = new HashSet<>();
        List<String> errors = new ArrayList<>();
        for (String name : names) {
            if (!nameSet.add(name)) {
                errors.add("Duplicate in payload: " + name);
            }
        }
        List<Movie> existing = movieRepository.findByMovieNameIn(names);
        for (Movie m : existing) {
            errors.add("Already exists: " + m.getMovieName());
        }
        List<MovieDto> toCreate = movies.stream()
                .filter(m -> m.getMovieName() != null)
                .filter(m -> !errors.contains("Duplicate in payload: " + m.getMovieName()))
                .filter(m -> existing.stream().noneMatch(e -> e.getMovieName().equals(m.getMovieName())))
                .toList();
        List<Movie> entities = toCreate.stream()
                .map(dto -> GenericEntityDtoAdapter.toEntityObject(dto, Movie.class))
                .collect(Collectors.toList());
        List<Movie> saved = movieRepository.saveAll(entities);
        List<MovieDto> createdDtos = GenericEntityDtoAdapter.toDtoList(saved, MovieDto.class);
        return new BulkUploadMoviesResponseDto(createdDtos, errors);
    }
}