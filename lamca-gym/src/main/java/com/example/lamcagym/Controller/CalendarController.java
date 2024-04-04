package com.example.lamcagym.Controller;

import com.example.lamcagym.Entity.Calendar;
import com.example.lamcagym.Service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/calendars")
public class CalendarController {

    @Autowired
    private CalendarService calendarService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllCalendars() {
        return new ResponseEntity<>(calendarService.getAll(), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<?> createCalendar(@RequestBody Calendar calendar) {
        return calendarService.createCalendar(calendar) ?
                new ResponseEntity<>(calendar, HttpStatus.CREATED) :
                new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCalendarById(@PathVariable("id") int id) {
        Calendar calendar = calendarService.getCalendar(id);
        return calendar != null ?
                new ResponseEntity<>(calendar, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCalendar(@PathVariable("id") int id) {
        return calendarService.deleteCalendar(id) ?
                new ResponseEntity<>(HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/")
    public ResponseEntity<?> updateCalendar(@RequestBody Calendar calendar) {
        return calendarService.updateCalendar(calendar) ?
                new ResponseEntity<>(calendar, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}