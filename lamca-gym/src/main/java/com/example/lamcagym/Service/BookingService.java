package com.example.lamcagym.Service;

import com.example.lamcagym.BookingRequest;
import com.example.lamcagym.Entity.Booking;
import com.example.lamcagym.Entity.Session;
import com.example.lamcagym.Entity.User;
import com.example.lamcagym.Repository.BookingRepository;
import com.example.lamcagym.Repository.SessionRepository;
import com.example.lamcagym.Repository.UserRepository;
import com.example.lamcagym.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public ResponseEntity<?> createBooking(BookingRequest bookingRequest) {
        User user = userRepository.findById(bookingRequest.getUserId()).orElse(null);
        Session session = sessionRepository.findById(bookingRequest.getSessionId()).orElse(null);

        if (user == null || session == null) {
            return ResponseEntity.badRequest().body(new ResponseObject("error", "Användare eller session finns inte.", null));
        }

        Booking booking = new Booking(user.getUserId(), session.getSessionId(), new Date());
        Booking savedBooking = bookingRepository.save(booking);

        return ResponseEntity.ok(new ResponseObject("success", "Session bokad.", savedBooking));
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