package com.example.lamcagym.Service;

import com.example.lamcagym.Entity.Booking;
import com.example.lamcagym.Repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public Booking getBookingById(Integer id) {
        return bookingRepository.findById(id).orElse(null);
    }

    public boolean updateBooking(Integer id, Booking booking) {
        if(bookingRepository.existsById(id)) {
            booking.setBookingId(id);
            bookingRepository.save(booking);
            return true;
        }
        return false;
    }

    public boolean deleteBooking(Integer id) {
        if(bookingRepository.existsById(id)) {
            bookingRepository.deleteById(id);
            return true;
        }
        return false;
    }
}