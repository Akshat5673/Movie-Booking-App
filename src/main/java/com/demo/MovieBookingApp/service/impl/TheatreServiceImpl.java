package com.demo.MovieBookingApp.service.impl;

import com.demo.MovieBookingApp.adapter.GenericEntityDtoAdapter;
import com.demo.MovieBookingApp.dto.TheatreDto;
import com.demo.MovieBookingApp.exception.ResourceNotFoundException;
import com.demo.MovieBookingApp.model.Theatre;
import com.demo.MovieBookingApp.repository.TheatreRepository;
import com.demo.MovieBookingApp.service.TheatreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TheatreServiceImpl implements TheatreService {

    @Autowired
    private TheatreRepository theatreRepository;


    @Override
    public List<TheatreDto> getAllTheatres() {
        return GenericEntityDtoAdapter.toDtoList(theatreRepository.findAll(), TheatreDto.class);
    }

    @Override
    public List<TheatreDto> getTheatresByScreenType(String screenType) {
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
        Theatre theatre = GenericEntityDtoAdapter.toEntityObject(theatreDto, Theatre.class);
        Theatre saved = theatreRepository.save(theatre);
        return GenericEntityDtoAdapter.toDtoObject(saved, TheatreDto.class);
    }

    @Override
    public TheatreDto updateTheatre(Long id, TheatreDto theatreDto) {
        Theatre existing = theatreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre", "id", String.valueOf(id)));
        existing.setTheatreName(theatreDto.getTheatreName());
        existing.setTheatreLocation(theatreDto.getTheatreLocation());
        existing.setCapacity(theatreDto.getCapacity());
        existing.setScreenType(theatreDto.getScreenType());
        Theatre updated = theatreRepository.save(existing);
        return GenericEntityDtoAdapter.toDtoObject(updated, TheatreDto.class);
    }

    @Override
    public void deleteTheatre(Long id) {
        Theatre existing = theatreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre", "id", String.valueOf(id)));
        theatreRepository.delete(existing);
    }

}
