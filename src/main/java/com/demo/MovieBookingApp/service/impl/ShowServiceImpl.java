package com.demo.MovieBookingApp.service.impl;

import com.demo.MovieBookingApp.adapter.GenericEntityDtoAdapter;
import com.demo.MovieBookingApp.dto.BulkUploadShowsResponseDto;
import com.demo.MovieBookingApp.dto.ShowDto;
import com.demo.MovieBookingApp.exception.AlreadyExistsException;
import com.demo.MovieBookingApp.exception.BadCredentialsException;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    // buffer time in minutes between shows in same theatre
    @Value("${theatre.show.buffer-minutes}")
    private long bufferMinutes;


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
    public ShowDto createShow(ShowDto dto) {
        if (showRepository.existsByMovieIdAndTheatreIdAndShowTime(
                dto.getMovieId(), dto.getTheatreId(), dto.getShowTime())) {
            throw new AlreadyExistsException(
                    "Show", "movieId+theatreId+showTime",
                    dto.getMovieId() + "_" + dto.getTheatreId() + "_" + dto.getShowTime()
            );
        }
        validateShowOverlap(dto, null);

        Movie movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", dto.getMovieId().toString()));
        Theatre theatre = theatreRepository.findById(dto.getTheatreId())
                .orElseThrow(() -> new ResourceNotFoundException("Theatre", "id", dto.getTheatreId().toString()));

        Show show = new Show();
        show.setShowTime(dto.getShowTime());
        show.setPrice(dto.getPrice());
        show.setMovie(movie);
        show.setTheatre(theatre);
        Show saved = showRepository.save(show);
        return GenericEntityDtoAdapter.toDtoObject(saved, ShowDto.class);
    }

    @Override
    public ShowDto updateShow(Long id, ShowDto dto) {
        Show existing = showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show", "id", String.valueOf(id)));

        // check duplicate except itself
        if (showRepository.existsByMovieIdAndTheatreIdAndShowTime(
                dto.getMovieId(), dto.getTheatreId(), dto.getShowTime())
                && !(existing.getMovie().getId().equals(dto.getMovieId())
                && existing.getTheatre().getId().equals(dto.getTheatreId())
                && existing.getShowTime().equals(dto.getShowTime()))) {
            throw new AlreadyExistsException(
                    "Show", "movieId+theatreId+showTime",
                    dto.getMovieId() + "_" + dto.getTheatreId() + "_" + dto.getShowTime()
            );
        }
        validateShowOverlap(dto, existing);

        Movie movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", dto.getMovieId().toString()));
        Theatre theatre = theatreRepository.findById(dto.getTheatreId())
                .orElseThrow(() -> new ResourceNotFoundException("Theatre", "id", dto.getTheatreId().toString()));

        existing.setShowTime(dto.getShowTime());
        existing.setPrice(dto.getPrice());
        existing.setMovie(movie);
        existing.setTheatre(theatre);
        Show updated = showRepository.save(existing);
        return GenericEntityDtoAdapter.toDtoObject(updated, ShowDto.class);
    }

    @Override
    public BulkUploadShowsResponseDto bulkUploadShows(List<ShowDto> dtos) {
        List<ShowDto> successful = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        Set<String> payloadKeys = new HashSet<>();

        if (dtos == null || dtos.isEmpty()) {
            errors.add("Input show list is empty.");
            return new BulkUploadShowsResponseDto(successful, errors);
        }

        for (int i = 0; i < dtos.size(); i++) {
            ShowDto dto = dtos.get(i);
            String key = dto.getMovieId() + "_" + dto.getTheatreId() + "_" + dto.getShowTime();
            try {
                if (!payloadKeys.add(key)) {
                    throw new BadCredentialsException("Duplicate show in request at row " + (i + 1));
                }
                if (showRepository.existsByMovieIdAndTheatreIdAndShowTime(
                        dto.getMovieId(), dto.getTheatreId(), dto.getShowTime())) {
                    throw new AlreadyExistsException("Show", "movieId+theatreId+showTime", key);
                }
                validateShowOverlap(dto, null);

                Movie movie = movieRepository.findById(dto.getMovieId())
                        .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", dto.getMovieId().toString()));
                Theatre theatre = theatreRepository.findById(dto.getTheatreId())
                        .orElseThrow(() -> new ResourceNotFoundException("Theatre", "id", dto.getTheatreId().toString()));

                Show show = new Show();
                show.setShowTime(dto.getShowTime());
                show.setPrice(dto.getPrice());
                show.setMovie(movie);
                show.setTheatre(theatre);

                Show saved = showRepository.save(show);
                successful.add(GenericEntityDtoAdapter.toDtoObject(saved, ShowDto.class));
            } catch (Exception e) {
                errors.add("Row " + (i + 1) + ": " + e.getMessage());
            }
        }

        return new BulkUploadShowsResponseDto(successful, errors);
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

    /**
     * Validate that the new or updated show does not overlap existing shows in same theatre.
     * @param dto incoming data
     * @param updating existing show if in update, so skip itself
     */
    private void validateShowOverlap(ShowDto dto, Show updating) {
        if (dto.getMovieId() == null || dto.getTheatreId() == null || dto.getShowTime() == null) {
            throw new BadCredentialsException("Movie ID, Theatre ID and Show Time must not be null.");
        }
        LocalDateTime newStart = dto.getShowTime();
        Movie movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", dto.getMovieId().toString()));
        LocalDateTime newEnd = newStart.plus(Duration.ofMinutes(movie.getDuration()));
        Duration buffer = Duration.ofMinutes(bufferMinutes);

        List<Show> existingShows = showRepository.findByTheatreId(dto.getTheatreId());
        for (Show s : existingShows) {
            if (updating != null && s.getId().equals(updating.getId())) continue;
            LocalDateTime sStart = s.getShowTime();
            LocalDateTime sEnd = sStart.plus(Duration.ofMinutes(s.getMovie().getDuration()));
            // apply buffer to end of existing show
            LocalDateTime sEndWithBuffer = sEnd.plus(buffer);
            // apply buffer to end of new show
            LocalDateTime newEndWithBuffer = newEnd.plus(buffer);

            boolean overlap = newStart.isBefore(sEndWithBuffer) && sStart.isBefore(newEndWithBuffer);
            if (overlap) {
                throw new BadCredentialsException(
                        String.format("Show time %s - %s overlaps existing show at %s", newStart, newEnd, sStart)
                );
            }
        }
    }

}
