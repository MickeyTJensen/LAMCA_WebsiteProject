package com.example.lamcagym.Service;

import com.example.lamcagym.BookingRequest;
import com.example.lamcagym.Entity.Booking;
import com.example.lamcagym.Entity.Session;
import com.example.lamcagym.Entity.User;
import com.example.lamcagym.Repository.BookingRepository;
import com.example.lamcagym.Repository.SessionRepository;
import com.example.lamcagym.Repository.UserRepository;
import com.example.lamcagym.ResponseObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class BookingService {

    // Skapa en logger för att logga information, varningar och fel för BookingService.
    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    // Autowired används för att automatiskt tråda bookingRepository beroende av BookingRepository.
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    // Metod för att hämta alla bokningar från databasen.
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // Metod för att skapa en ny bokning.
    public ResponseEntity<?> createBooking(BookingRequest bookingRequest) {
        User user = userRepository.findById(bookingRequest.getUserId()).orElse(null);
        Session session = sessionRepository.findById(bookingRequest.getSessionId()).orElse(null);

        if (user == null || session == null) {
            return ResponseEntity.badRequest().body(new ResponseObject("error", "User or session doesn't exist.", null));
        }

        Booking booking = new Booking(user, session, new Date()); // Använd det hämtade Session-objektet
        Booking savedBooking = bookingRepository.save(booking);

        return ResponseEntity.ok(new ResponseObject("success", "Your session is booked.", savedBooking));
    }

    // Metod för att hämta en bokning med ett specifikt ID.
    public Booking getBookingById(Integer id) {
        // Försök att hämta bokningen från databasen med det angivna ID:t.
        return bookingRepository.findById(id).orElse(null);
    }

    // Metod för att uppdatera en befintlig bokning.
    public boolean updateBooking(Integer id, Booking booking) {
        // Kontrollera om bokningen med det angivna ID:t finns.
        if (bookingRepository.existsById(id)) {
            try {
                // Uppdatera bokningen i databasen och bevara ID:t.
                booking.setBookingId(id);
                bookingRepository.save(booking);
                // Logga att bokningen har uppdaterats med dess ID.
                logger.info("Booking updated with ID: {}", id);
                return true;
            } catch (Exception e) {
                // Om det uppstår ett fel, logga felmeddelandet.
                logger.error("Failed to update booking with ID {}: {}", id, e.getMessage());
                return false;
            }
        }
        // Om ingen bokning hittades med det angivna ID:t, logga detta.
        logger.warn("No booking found with ID: {}, cannot update", id);
        return false;
    }

    // Metod för att ta bort en bokning med ett specifikt ID.
    public boolean deleteBooking(Integer id) {
        // Kontrollera om bokningen med det angivna ID:t finns.
        if (bookingRepository.existsById(id)) {
            try {
                // Om bokningen finns, ta bort den från databasen.
                bookingRepository.deleteById(id);
                // Logga att bokningen har tagits bort med dess ID.
                logger.info("Booking deleted with ID: {}", id);
                return true;
            } catch (Exception e) {
                // Om det uppstår ett fel, logga felmeddelandet.
                logger.error("Failed to delete booking with ID {}: {}", id, e.getMessage());
                return false;
            }
        }
        // Om ingen bokning hittades med det angivna ID:t, logga detta.
        logger.warn("No booking found with ID: {}, cannot delete", id);
        return false;
    }

    public int countBookingsBySession(Integer sessionId) {
        return bookingRepository.countBySession_SessionId(sessionId);
    }
    public boolean cancelBooking(Integer sessionId, Integer userId) {
        // Hämta alla bokningar baserat på sessions-ID och användar-ID
        List<Booking> bookings = bookingRepository.findBySession_SessionIdAndUser_UserId(sessionId, userId);

        if (!bookings.isEmpty()) {
            // Iterera över varje bokning och ta bort den
            for (Booking booking : bookings) {
                bookingRepository.delete(booking);
            }
            return true; // Returnera true om avbokningen lyckades
        } else {
            // Om ingen bokning hittades, returnera false för att indikera att avbokningen misslyckades
            return false;
        }
    }
}