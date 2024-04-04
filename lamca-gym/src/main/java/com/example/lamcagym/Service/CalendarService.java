package com.example.lamcagym.Service;

import com.example.lamcagym.Entity.Calendar;
import com.example.lamcagym.Repository.CalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CalendarService {

    @Autowired
    private CalendarRepository calendarRepository;

    public ArrayList<Calendar> getAll() {
        return (ArrayList<Calendar>) calendarRepository.findAll();
    }

    public boolean createCalendar(Calendar calendar) {
        try {
            calendarRepository.save(calendar);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Calendar getCalendar(int calendarId) {
        Optional<Calendar> optionalCalendar = calendarRepository.findById(calendarId);
        return optionalCalendar.orElse(null);
    }

    public boolean deleteCalendar(int calendarId) {
        if (calendarRepository.existsById(calendarId)) {
            calendarRepository.deleteById(calendarId);
            return true;
        }
        return false;
    }

    public boolean updateCalendar(Calendar calendar) {
        if (calendarRepository.existsById(calendar.getCalendarId())) {
            calendarRepository.save(calendar);
            return true;
        }
        return false;
    }
}