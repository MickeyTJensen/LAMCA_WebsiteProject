package com.example.lamcagym.Controller;

import com.example.lamcagym.Entity.Booking;
import com.example.lamcagym.Entity.Session;
import com.example.lamcagym.Service.BookingService;
import com.example.lamcagym.Service.SessionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

// Annotering som definierar klassen som en REST-kontroller med en specifik bas-URL för alla dess hanterade anrop.
@RestController
@RequestMapping("/sessions")
public class SessionController {
    @Autowired
    BookingService bookingService;

    // Logger för att logga information, varningar och fel.
    private static final Logger logger = LoggerFactory.getLogger(SessionController.class);
    private final SessionService sessionService;

    // Konstruktor som injicerar en instans av SessionService.
    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/all")
    public List<Session> getAllSessions() {
        logger.info("Fetching all sessions from the database.");
        List<Session> sessions = sessionService.getAll();
        // Hämta och sätt antalet bokningar för varje session
        for (Session session : sessions) {
            int bookedCount = bookingService.countBookingsBySession(session.getSessionId());
            session.setBooked(bookedCount);
        }
        return sessions;
    }

    // Hanterar GET-förfrågningar till "/sessions/". Returnerar en session med specifikt ID eller en 404 om sessionen inte finns.
    @GetMapping("/")
    public ResponseEntity<Session> getSessionById(@RequestParam int id) {
        logger.info("Requested session by ID: {}", id);
        Session session = sessionService.getSession(id);
        if (session != null) {
            logger.info("Session with ID: {} found.", id);
            return ResponseEntity.status(HttpStatus.OK).body(session);
        } else {
            logger.warn("Session with ID: {} not found.", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Hanterar POST-förfrågningar till "/sessions/". Skapar en ny session om inga valideringsfel uppstår.
    @PostMapping("/")
    public ResponseEntity<?> createSession(@Valid @RequestBody Session newSession) {
        try {
            logger.info("Creating session of type '{}' scheduled at {}", newSession.getSessionType(), newSession.getTime());
            Session createdSession = sessionService.createSession(newSession);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSession);
        } catch (Exception e) {
            logger.error("Unexpected error occurred while creating a new session.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while creating the session.");
        }
    }

    // Hanterar DELETE-förfrågningar till "/sessions/". Tar bort en session med specifikt ID om sessionen finns.
    @DeleteMapping("/")
    public ResponseEntity<?> deleteSession(@RequestParam int id) {
        logger.info("Attempting to delete session with ID: {}", id);
        boolean success = sessionService.deleteSession(id);
        if (success) {
            logger.info("Session with ID: {} successfully deleted.", id);
            return ResponseEntity.ok().body("Session with ID: " + id + " successfully deleted.");
        } else {
            logger.warn("Deletion failed. Session with ID: {} not found.", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Session with ID: " + id + " not found.");
        }
    }

    // Hanterar PUT-förfrågningar till "/sessions/{id}". Uppdaterar en befintlig session med nya detaljer om inga valideringsfel uppstår.
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSession(
            @PathVariable int id,
            @Valid @RequestBody Session sessionDetails) {
        try {
            logger.info("Attempting to update session with ID: {}", id);
            Session updatedSession = sessionService.updateSession(id, sessionDetails);
            if (updatedSession != null) {
                return ResponseEntity.ok(updatedSession);
            } else {
                logger.warn("Could not update session with ID: {}.", id);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not update session with ID: " + id);
            }
        } catch (Exception e) {
            logger.error("Unexpected error occurred while updating session with ID: {}.", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the session.");
        }
    }
}