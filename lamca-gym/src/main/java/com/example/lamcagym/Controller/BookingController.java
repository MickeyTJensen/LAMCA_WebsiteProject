package com.example.lamcagym.Controller;
import com.example.lamcagym.BookingRequest;
import com.example.lamcagym.Entity.Booking;
import com.example.lamcagym.Service.BookingService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
// Definierar en REST-kontroller med en bas-URL för alla request-mappningar till "/bookings"
@RestController
@RequestMapping("/bookings")
public class BookingController {

    // Skapar en logger för att logga information och fel.
    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    // Använder Spring's dependency injection för att infoga en instans av BookingService
    @Autowired
    private BookingService bookingService;

    // Hanterar GET-förfrågningar för att hämta alla bokningar
    @GetMapping("/")
    public ResponseEntity<List<Booking>> getAllBookings() {
        logger.info("Fetching all bookings");
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    // Hanterar POST-förfrågningar för att skapa en ny bokning
    @PostMapping("/book")
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest bookingRequest) {
        logger.info("Försöker skapa en ny bokning");
        ResponseEntity<?> response = bookingService.createBooking(bookingRequest); // Skapar en bokning genom att anropa bookingService.createBooking med den inkommande förfrågan
        if (response.getStatusCode().is2xxSuccessful()) { // Kontrollerar om bokningen lyckades genom att kolla om statuskoden är en 2xx-kod (lyckad)
            logger.info("Bokning skapad framgångsrikt");
        } else { // Om bokningen misslyckades
            logger.error("Misslyckades med att skapa bokning");
        }
        return response; // Returnerar svar från bokningstjänsten, antingen med bokningsdata eller felmeddelande, till klienten
    }

    // Hanterar GET-förfrågningar för att hämta en specifik bokning via ID
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Integer id) {
        logger.info("Fetching booking with ID: {}", id);
        Booking booking = bookingService.getBookingById(id);
        if (booking != null) {
            logger.info("Booking with ID: {} found", id);
            return ResponseEntity.ok(booking);
        } else {
            logger.warn("Booking with ID: {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    // Hanterar PUT-förfrågningar för att uppdatera en befintlig bokning med ny data
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable Integer id, @Valid @RequestBody Booking bookingDetails, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Om valideringsfel uppstår, logga och returnera dessa som ett felmeddelande
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            logger.warn("Validation errors occurred while updating booking with ID {}: {}", id, errors);
            return ResponseEntity.badRequest().body(errors);
        }
        logger.info("Attempting to update booking with ID: {}", id);
        boolean success = bookingService.updateBooking(id, bookingDetails);
        if (success) {
            logger.info("Booking with ID: {} updated successfully", id);
            return ResponseEntity.ok().build();
        } else {
            logger.error("Failed to update booking with ID: {}.", id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update booking.");
        }
    }

    // Hanterar DELETE-förfrågningar för att avboka en bokning baserat på session-ID och användar-ID
    @DeleteMapping("/book/{sessionId}/{userId}")
    public ResponseEntity<?> cancelBooking(@PathVariable Integer sessionId, @PathVariable Integer userId) {
        // Loggar ett försök att avboka en bokning
        logger.info("Attempting to cancel booking for session ID: {}, user ID: {}", sessionId, userId);

        // Anropar service-metoden för att avboka och lagrar resultatet
        boolean isCancelled = bookingService.cancelBooking(sessionId, userId);

        // Kontrollerar om avbokningen lyckades
        if(isCancelled) {
            // Loggar en lyckad avbokning
            logger.info("Booking successfully cancelled for session ID: {}, user ID: {}", sessionId, userId);
            return ResponseEntity.ok().build(); // Returnerar status OK om avbokningen är lyckad
        } else {
            // Loggar ett felmeddelande om att avbokningen inte kunde hittas
            logger.warn("Failed to cancel booking: No booking found for session ID: {}, user ID: {}", sessionId, userId);
            return ResponseEntity.notFound().build(); // Returnerar status Not Found om bokningen inte finns
        }
    }
}
