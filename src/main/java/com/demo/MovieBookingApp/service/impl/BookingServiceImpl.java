package com.demo.MovieBookingApp.service.impl;

import com.demo.MovieBookingApp.adapter.BookingMapper;
import com.demo.MovieBookingApp.dto.BookingDto;
import com.demo.MovieBookingApp.dto.BookingResponseDto;
import com.demo.MovieBookingApp.enums.BookingStatus;
import com.demo.MovieBookingApp.exception.ResourceNotFoundException;
import com.demo.MovieBookingApp.model.Booking;
import com.demo.MovieBookingApp.model.Show;
import com.demo.MovieBookingApp.model.User;
import com.demo.MovieBookingApp.repository.BookingRepository;
import com.demo.MovieBookingApp.repository.ShowRepository;
import com.demo.MovieBookingApp.repository.UserRepository;
import com.demo.MovieBookingApp.service.BookingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ShowRepository showRepository;
    private final UserRepository userRepository;


    @Override
    public BookingResponseDto createBooking(BookingDto bookingDto) {
        Show show = showRepository.findById(bookingDto.getShowId()).orElseThrow(()->
                new ResourceNotFoundException("Show","id",bookingDto.getShowId().toString()));
        if(!isSeatAvailable(show,bookingDto.getSeats())){
            throw new RuntimeException("Not enough seats!");
        }
        if (bookingDto.getSeatNumbers().size()!= bookingDto.getSeats()){
            throw new RuntimeException("Number of seats and size of seat numbers should be equal!");
        }
        validateDuplicateSeats(show,bookingDto.getSeatNumbers());
        User user = userRepository.findById(bookingDto.getUserId()).orElseThrow(()->
                new ResourceNotFoundException("User","id",bookingDto.getUserId().toString()));
        Booking newBooking = new Booking();
        newBooking.setUser(user);
        newBooking.setShow(show);
        newBooking.setNumberOfSeats(bookingDto.getSeats());
        newBooking.setBookingTime(LocalDateTime.now());
        newBooking.setPrice(calculatePrice(show.getPrice(),bookingDto.getSeats()));
        newBooking.setSeatNumbers(bookingDto.getSeatNumbers());
        newBooking.setStatus(BookingStatus.PENDING);

        bookingRepository.save(newBooking);

        return BookingMapper.toResponseDto(newBooking);
    }

    private Double calculatePrice(Double showPrice, Integer seats) {
        return (showPrice*seats);
    }

    private void validateDuplicateSeats(Show show, List<String> seatNumbers) {
        Set<String> occupiedSeats =  show.getBookings().stream().filter(booking->
                booking.getStatus()!= BookingStatus.CANCELLED).flatMap(booking -> booking.getSeatNumbers()
                .stream()).collect(Collectors.toSet());
        List<String> duplicateSeats = seatNumbers.stream().filter(occupiedSeats::contains).toList();
        if (!duplicateSeats.isEmpty()){
            throw new RuntimeException("Requested seats not available!");
        }
    }

    private boolean isSeatAvailable(Show show, Integer seats) {
        int bookedSeats = show.getBookings().stream().filter(booking->
                booking.getStatus()!= BookingStatus.CANCELLED).mapToInt(Booking::getNumberOfSeats).sum();
        int availableSeats = show.getTheatre().getCapacity() - bookedSeats;
        return availableSeats >= seats;
    }

    @Override
    public BookingResponseDto confirmBooking( Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", String.valueOf(bookingId)));

        if (booking.getStatus()!= BookingStatus.PENDING){
            throw new RuntimeException("Booking is not in Pending!");
        }
        //Payment Process
        booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);
        return BookingMapper.toResponseDto(booking);
    }

    @Override
    public BookingResponseDto cancelBooking( Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", String.valueOf(bookingId)));
        validateCancellation(booking);
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
        return BookingMapper.toResponseDto(booking);
    }

    private void validateCancellation(Booking booking) {
        LocalDateTime showTime = booking.getShow().getShowTime();
        LocalDateTime deadline = showTime.minusHours(2);
        if (LocalDateTime.now().isAfter(deadline)){
            throw new RuntimeException("Cannot cancel after deadline!");
        }
        if (booking.getStatus() == BookingStatus.CANCELLED){
            throw new RuntimeException(" Already Cancelled!");
        }
    }

    @Override
    public List<BookingResponseDto> getBookingsByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId.toString());
        }

        List<Booking> bookings = bookingRepository.findAllByUserId(userId);
        return BookingMapper.toResponseDtoList(bookings);
    }


    @Override
    public List<BookingResponseDto> getBookingsByShow(Long showId) {
        if(!showRepository.existsById(showId)){
            throw new ResourceNotFoundException("Show","id",showId.toString());
        }

        List<Booking> bookings = bookingRepository.findAllByShowId(showId);
        return BookingMapper.toResponseDtoList(bookings);
    }

    @Override
    public List<BookingResponseDto> getBookingsByStatus(BookingStatus status) {
        List<Booking> bookings = bookingRepository.findAllByStatus(status);
        return BookingMapper.toResponseDtoList(bookings);
    }


    @Override
    public BookingResponseDto getBookingById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(()-> new ResourceNotFoundException("Booking", "id", bookingId.toString()));
        return BookingMapper.toResponseDto(booking);
    }
    
}
