package com.demo.MovieBookingApp.service.impl;

import com.demo.MovieBookingApp.adapter.GenericEntityDtoAdapter;
import com.demo.MovieBookingApp.dto.ShowDto;
import com.demo.MovieBookingApp.exception.ResourceNotFoundException;
import com.demo.MovieBookingApp.model.Booking;
import com.demo.MovieBookingApp.model.Movie;
import com.demo.MovieBookingApp.model.Show;
import com.demo.MovieBookingApp.model.Theatre;
import com.demo.MovieBookingApp.repository.MovieRepository;
import com.demo.MovieBookingApp.repository.ShowRepository;
import com.demo.MovieBookingApp.repository.TheatreRepository;
import com.demo.MovieBookingApp.service.ShowService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ShowServiceImpl implements ShowService {

    @Autowired
    private ShowRepository showRepository;
    @Autowired
    private final MovieRepository movieRepository;
    @Autowired
    private final TheatreRepository theatreRepository;


    @Override
    public List<ShowDto> getAllShows() {
        List<Show> shows = showRepository.findAll();
        return GenericEntityDtoAdapter.toDtoList(shows, ShowDto.class);
    }

    @Override
    public ShowDto getShowById(Long id) {
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show", "id", String.valueOf(id)));
        return GenericEntityDtoAdapter.toDtoObject(show, ShowDto.class);
    }

    @Override
    public List<ShowDto> getAllShowsByMovie(Long movieId) {
        movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", String.valueOf(movieId)));
        List<Show> shows = showRepository.findByMovieId(movieId);
        if(!shows.isEmpty())
            return GenericEntityDtoAdapter.toDtoList(shows, ShowDto.class);
        else
            throw new RuntimeException("No shows available for this movie!");
    }

    @Override
    public List<ShowDto> getAllShowsByTheatre(Long theatreId) {
        theatreRepository.findById(theatreId)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre", "id", String.valueOf(theatreId)));
        List<Show> shows = showRepository.findByTheatreId(theatreId);
        if (!shows.isEmpty())
            return GenericEntityDtoAdapter.toDtoList(shows, ShowDto.class);
        else
            throw new RuntimeException("No shows available in this theatre!");
    }

    @Override
    public List<ShowDto> getShowsByMovieInTheatre(Long movieId, Long theatreId) {
        movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", String.valueOf(movieId)));
        theatreRepository.findById(theatreId)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre", "id", String.valueOf(theatreId)));
        List<Show> shows = showRepository.findByMovieIdAndTheatreId(movieId, theatreId);
        if (!shows.isEmpty())
            return GenericEntityDtoAdapter.toDtoList(shows, ShowDto.class);
        else
            throw new RuntimeException("No shows available in this theatre for this movie!");
    }

    @Override
    public ShowDto createShow(ShowDto showDto) {
        Movie movie = movieRepository.findById(showDto.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", String.valueOf(showDto.getMovieId())));
        Theatre theatre = theatreRepository.findById(showDto.getTheatreId())
                .orElseThrow(() -> new ResourceNotFoundException("Theatre", "id", String.valueOf(showDto.getTheatreId())));
        Show show = GenericEntityDtoAdapter.toEntityObject(showDto, Show.class);
        show.setMovie(movie);
        show.setTheatre(theatre);
        Show saved = showRepository.save(show);
        return GenericEntityDtoAdapter.toDtoObject(saved, ShowDto.class);
    }

    @Override
    public ShowDto updateShow(Long id, ShowDto showDto) {
        Show existing = showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show", "id", String.valueOf(id)));
        Movie movie = movieRepository.findById(showDto.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", String.valueOf(showDto.getMovieId())));
        Theatre theatre = theatreRepository.findById(showDto.getTheatreId())
                .orElseThrow(() -> new ResourceNotFoundException("Theatre", "id", String.valueOf(showDto.getTheatreId())));
        existing.setShowTime(showDto.getShowTime());
        existing.setPrice(showDto.getPrice());
        existing.setMovie(movie);
        existing.setTheatre(theatre);
        Show updated = showRepository.save(existing);
        return GenericEntityDtoAdapter.toDtoObject(updated, ShowDto.class);
    }

    @Override
    public void deleteShow(Long id) {
        Show existing = showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show", "id", String.valueOf(id)));
        List<Booking> bookings = showRepository.findById(id).orElseThrow().getBookings();  // .get()
        if(bookings.isEmpty())
            showRepository.delete(existing);
        else
            throw new RuntimeException("Cannot delete show with active bookings!");
    }



}
