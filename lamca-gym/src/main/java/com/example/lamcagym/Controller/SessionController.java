package com.example.lamcagym.Controller;

import com.example.lamcagym.Entity.Session;
import com.example.lamcagym.Service.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    private static final Logger logger = LoggerFactory.getLogger(SessionController.class);
    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/all")
    public ArrayList<Session> getAllSessions() {
        logger.info("Fetching all sessions from the database.");
        return sessionService.getAll();
    }

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

    @PostMapping("/")
    public ResponseEntity<?> createSession(
            @RequestParam String sessionType,
            @RequestParam String time,
            @RequestParam Integer duration,
            @RequestParam Integer capacity,
            @RequestParam String instructor) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date sessionTime;
        try {
            sessionTime = dateFormat.parse(time);
            logger.info("Creating session of type '{}' scheduled at {}", sessionType, sessionTime);
            Session newSession = new Session(sessionType, sessionTime, duration, capacity, instructor);
            boolean success = sessionService.createSession(newSession);
            if (success) {
                logger.info("Session successfully created with ID: {}", newSession.getSessionId());
                return ResponseEntity.status(HttpStatus.CREATED).body(newSession);
            }
        } catch (java.text.ParseException e) {
            logger.error("Error parsing the date '{}' for a new session.", time, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error parsing the date.");
        } catch (Exception e) {
            logger.error("Unexpected error occurred while creating a new session.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while creating the session.");
        }
        logger.warn("Failed to create a new session due to business logic.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create the session due to business logic constraints.");
    }

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

    @PutMapping("/")
    public ResponseEntity<?> updateSession(
            @RequestParam int id,
            @RequestParam String sessionType,
            @RequestParam String time,
            @RequestParam Integer duration,
            @RequestParam Integer capacity,
            @RequestParam String instructor) {

        logger.info("Attempting to update session with ID: {}", id);
        Session sessionToUpdate = sessionService.getSession(id);
        if (sessionToUpdate != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date sessionTime = dateFormat.parse(time);
                sessionToUpdate.setSessionType(sessionType);
                sessionToUpdate.setTime(sessionTime);
                sessionToUpdate.setDuration(duration);
                sessionToUpdate.setCapacity(capacity);
                sessionToUpdate.setInstructor(instructor);
                boolean success = sessionService.updateSession(sessionToUpdate);
                if (success) {
                    logger.info("Session with ID: {} successfully updated.", id);
                    return ResponseEntity.ok().body("Session with ID: " + id + " successfully updated.");
                } else {
                    logger.warn("Update failed. Could not update session with ID: {}.", id);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not update session with ID: " + id);
                }
            } catch (java.text.ParseException e) {
                logger.error("Error parsing the date '{}' for session ID: {}.", time, id, e);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error parsing the date.");
            } catch (Exception e) {
                logger.error("Unexpected error occurred while updating session with ID: {}.", id, e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the session.");
            }
        } else {
            logger.warn("Update failed. Session with ID: {} not found for update.", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Session with ID: " + id + " not found for update.");
        }
    }
}