package com.demo.MovieBookingApp.service.impl;

import com.demo.MovieBookingApp.adapter.GenericEntityDtoAdapter;
import com.demo.MovieBookingApp.dto.BulkUploadTheatresResponseDto;
import com.demo.MovieBookingApp.dto.TheatreDto;
import com.demo.MovieBookingApp.exception.AlreadyExistsException;
import com.demo.MovieBookingApp.exception.BadCredentialsException;
import com.demo.MovieBookingApp.exception.ResourceNotFoundException;
import com.demo.MovieBookingApp.model.Theatre;
import com.demo.MovieBookingApp.repository.TheatreRepository;
import com.demo.MovieBookingApp.service.TheatreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class TheatreServiceImpl implements TheatreService {

    @Autowired
    private TheatreRepository theatreRepository;

    // Allowed screen types and capacity limit
    private static final Set<String> VALID_SCREEN_TYPES = Set.of("2D", "3D", "IMAX", "4DX", "PLATINUM");
    private static final int MAX_CAPACITY = 500;

    @Override
    public List<TheatreDto> getAllTheatres() {
        return GenericEntityDtoAdapter.toDtoList(theatreRepository.findAll(), TheatreDto.class);
    }

    @Override
    public List<TheatreDto> getTheatresByScreenType(String screenType) {
        validateScreenType(screenType);
        List<Theatre> theatres = theatreRepository.findByScreenType(screenType);
        return GenericEntityDtoAdapter.toDtoList(theatres, TheatreDto.class);
    }

    @Override
    public TheatreDto getTheatreById(Long id) {
        Theatre theatre = theatreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre", "id", String.valueOf(id)));
        return GenericEntityDtoAdapter.toDtoObject(theatre, TheatreDto.class);
    }

    @Override
    public TheatreDto createTheatre(TheatreDto theatreDto) {
        if (theatreRepository.existsByTheatreNameAndTheatreLocation(theatreDto.getTheatreName(),
                theatreDto.getTheatreLocation())) {
            throw new AlreadyExistsException("Theatre", "name+location",
                    theatreDto.getTheatreName() + "," + theatreDto.getTheatreLocation());
        }
        Theatre theatre = GenericEntityDtoAdapter.toEntityObject(theatreDto, Theatre.class);
        Theatre saved = theatreRepository.save(theatre);
        return GenericEntityDtoAdapter.toDtoObject(saved, TheatreDto.class);
    }

    @Override
    public TheatreDto updateTheatre(Long id, TheatreDto dto) {
        validateTheatreDto(dto);
        Theatre existing = theatreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre", "id", id.toString()));
        boolean conflict = theatreRepository.existsByTheatreNameAndTheatreLocation(dto.getTheatreName(), dto.getTheatreLocation())
                && (!existing.getTheatreName().equals(dto.getTheatreName())
                || !existing.getTheatreLocation().equals(dto.getTheatreLocation()));
        if (conflict) {
            throw new AlreadyExistsException("Theatre", "name+location",
                    dto.getTheatreName() + "," + dto.getTheatreLocation());
        }
        existing.setTheatreName(dto.getTheatreName());
        existing.setTheatreLocation(dto.getTheatreLocation());
        existing.setCapacity(dto.getCapacity());
        existing.setScreenType(dto.getScreenType());
        Theatre updated = theatreRepository.save(existing);
        return GenericEntityDtoAdapter.toDtoObject(updated, TheatreDto.class);
    }

    @Override
    public BulkUploadTheatresResponseDto bulkUploadTheatres(List<TheatreDto> theatres) {
        List<TheatreDto> successful = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        Set<String> payloadKeys = new HashSet<>();

        if (theatres == null || theatres.isEmpty()) {
            errors.add("Input theatre list is empty.");
            return new BulkUploadTheatresResponseDto(successful, errors);
        }

        for (int i = 0; i < theatres.size(); i++) {
            TheatreDto dto = theatres.get(i);
            String key = dto.getTheatreName().toLowerCase().trim() + "," + dto.getTheatreLocation().toLowerCase().trim();
            try {
                if (!payloadKeys.add(key)) {
                    throw new BadCredentialsException("Duplicate theatre in request at row " + (i + 1));
                }
                validateTheatreDto(dto);
                if (theatreRepository.existsByTheatreNameAndTheatreLocation(dto.getTheatreName(), dto.getTheatreLocation())) {
                    throw new AlreadyExistsException("Theatre", "name+location", key);
                }
                Theatre t = GenericEntityDtoAdapter.toEntityObject(dto, Theatre.class);
                Theatre saved = theatreRepository.save(t);
                successful.add(GenericEntityDtoAdapter.toDtoObject(saved, TheatreDto.class));
            } catch (Exception e) {
                errors.add("Row " + (i + 1) + ": " + e.getMessage());
            }
        }

        return new BulkUploadTheatresResponseDto(successful, errors);
    }

    @Override
    public void deleteTheatre(Long id) {
        Theatre existing = theatreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre", "id", String.valueOf(id)));
        theatreRepository.delete(existing);
    }

    private void validateTheatreDto(TheatreDto dto) {
        if (dto.getCapacity() == null || dto.getCapacity() < 1 || dto.getCapacity() > MAX_CAPACITY) {
            throw new BadCredentialsException("Capacity must be between 1 and " + MAX_CAPACITY + ", got "
                    + dto.getCapacity());
        }
        validateScreenType(dto.getScreenType());
    }

    private void validateScreenType(String screenType) {
        if (screenType == null || !VALID_SCREEN_TYPES.contains(screenType)) {
            throw new BadCredentialsException(
                    "Invalid screen type: '" + screenType + "'. Allowed types: " + VALID_SCREEN_TYPES
            );
        }
    }

}
