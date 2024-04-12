package com.example.lamcagym.Controller;

import com.example.lamcagym.BookingRequest;
import com.example.lamcagym.Entity.Booking;
import com.example.lamcagym.Entity.Session;
import com.example.lamcagym.Entity.User;
import com.example.lamcagym.Service.BookingService;
import com.example.lamcagym.Service.SessionService;
import com.example.lamcagym.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;
    @Autowired
    UserService userService;
    @Autowired
    SessionService sessionService;

    // Hämta alla bokningar
    @GetMapping("/")
    public ResponseEntity<?> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    // Skapa en ny bokning
    @PostMapping("/book")
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest bookingRequest) {
        return bookingService.createBooking(bookingRequest);
    }


    // Hämta en specifik bokning med ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable Integer id) {
        Booking booking = bookingService.getBookingById(id);
        if(booking != null) {
            return ResponseEntity.ok(booking);
        }
        return ResponseEntity.notFound().build();
    }

    // Uppdatera en befintlig bokning
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable Integer id, @RequestBody Booking booking) {
        boolean isUpdated = bookingService.updateBooking(id, booking);
        if(isUpdated) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Ta bort en bokning
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable Integer id) {
        boolean isDeleted = bookingService.deleteBooking(id);
        if(isDeleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}