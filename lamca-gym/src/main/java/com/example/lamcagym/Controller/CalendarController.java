package com.example.lamcagym.Controller;

import com.example.lamcagym.Entity.Calendar;
import com.example.lamcagym.Service.CalendarService;
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

// Definierar en REST-kontroller med en bas-URL för alla request-mappningar till "/calendars"
@RestController
@RequestMapping("/calendars")
public class CalendarController {

    // Skapar en logger för att logga information och fel.
    private static final Logger logger = LoggerFactory.getLogger(CalendarController.class);

    // Använder Spring's dependency injection för att infoga en instans av CalendarService
    @Autowired
    private CalendarService calendarService;

    // Hanterar GET-förfrågningar för att hämta alla kalenderposter
    @GetMapping("/all")
    public ResponseEntity<List<Calendar>> getAllCalendars() {
        logger.info("Fetching all calendar entries.");
        return new ResponseEntity<>(calendarService.getAll(), HttpStatus.OK);
    }

    // Hanterar POST-förfrågningar för att skapa en ny kalenderpost, validerar inkommande data
    @PostMapping("/")
    public ResponseEntity<?> createCalendar(@Valid @RequestBody Calendar calendar, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Om valideringsfel uppstår, logga och returnera dessa som ett felmeddelande
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            logger.warn("Validation errors occurred while creating a new calendar entry: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }
        logger.info("Attempting to create a new calendar entry.");
        boolean success = calendarService.createCalendar(calendar);
        if (success) {
            logger.info("Calendar entry successfully created.");
            return ResponseEntity.status(HttpStatus.CREATED).body(calendar);
        } else {
            logger.error("Failed to create a new calendar entry.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create a new calendar entry.");
        }
    }

    // Hanterar GET-förfrågningar för att hämta en specifik kalenderpost via ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getCalendarById(@PathVariable("id") int id) {
        logger.info("Requested calendar entry by ID: {}", id);
        Calendar calendar = calendarService.getCalendar(id);
        if (calendar != null) {
            logger.info("Calendar entry with ID: {} found.", id);
            return ResponseEntity.ok(calendar);
        } else {
            logger.warn("Calendar entry with ID: {} not found.", id);
            return ResponseEntity.notFound().build();
        }
    }

    // Hanterar DELETE-förfrågningar för att radera en kalenderpost via ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCalendar(@PathVariable("id") int id) {
        logger.info("Attempting to delete calendar entry with ID: {}", id);
        boolean success = calendarService.deleteCalendar(id);
        if (success) {
            logger.info("Calendar entry with ID: {} successfully deleted.", id);
            return ResponseEntity.ok().body("Calendar entry successfully deleted.");
        } else {
            logger.warn("Calendar entry with ID: {} not found.", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Calendar entry not found.");
        }
    }

    // Hanterar PUT-förfrågningar för att uppdatera en befintlig kalenderpost med ny data
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCalendar(@PathVariable("id") int id, @Valid @RequestBody Calendar calendarDetails, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Om valideringsfel uppstår, logga och returnera dessa som ett felmeddelande
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            logger.warn("Validation errors occurred while updating calendar entry with ID {}: {}", id, errors);
            return ResponseEntity.badRequest().body(errors);
        }
        logger.info("Attempting to update calendar entry with ID: {}", id);
        boolean success = calendarService.updateCalendar(id, calendarDetails);
        if (success) {
            logger.info("Calendar entry with ID: {} successfully updated.", id);
            return ResponseEntity.ok().body(calendarDetails);
        } else {
            logger.error("Failed to update calendar entry with ID: {}.", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to update calendar entry.");
        }
    }
}
