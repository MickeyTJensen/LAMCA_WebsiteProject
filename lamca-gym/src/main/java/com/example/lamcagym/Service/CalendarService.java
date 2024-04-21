package com.example.lamcagym.Service;

import com.example.lamcagym.Entity.Calendar;
import com.example.lamcagym.Repository.CalendarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CalendarService {

    // Skapa en logger för att logga information, varningar och fel för CalendarService.
    private static final Logger logger = LoggerFactory.getLogger(CalendarService.class);

    // Autowired används för att automatiskt tråda calendarRepository beroende av CalendarRepository.
    @Autowired
    private CalendarRepository calendarRepository;

    // Metod för att hämta alla kalendrar från databasen.
    public ArrayList<Calendar> getAll() {
        return (ArrayList<Calendar>) calendarRepository.findAll();
    }

    // Metod för att skapa en ny kalender.
    public boolean createCalendar(Calendar calendar) {
        try {
            // Spara den nya kalendern i databasen.
            calendarRepository.save(calendar);
            // Logga att kalendern har skapats med dess ID.
            logger.info("Calendar created with ID: {}", calendar.getCalendarId());
            return true;
        } catch (Exception e) {
            // Om det uppstår ett fel, logga felmeddelandet.
            logger.error("Failed to create calendar: {}", e.getMessage());
            return false;
        }
    }

    // Metod för att hämta en kalender med ett specifikt ID.
    public Calendar getCalendar(int calendarId) {
        // Försök att hämta kalendern från databasen med det angivna ID:t.
        Optional<Calendar> optionalCalendar = calendarRepository.findById(calendarId);
        // Om kalendern finns, returnera den.
        if (optionalCalendar.isPresent()) {
            return optionalCalendar.get();
        } else {
            // Annars, logga att ingen kalender hittades med det angivna ID:t.
            logger.warn("No calendar found with ID: {}", calendarId);
            return null;
        }
    }

    // Metod för att ta bort en kalender med ett specifikt ID.
    public boolean deleteCalendar(int calendarId) {
        // Kontrollera om kalendern med det angivna ID:t finns.
        if (calendarRepository.existsById(calendarId)) {
            try {
                // Om kalendern finns, ta bort den från databasen.
                calendarRepository.deleteById(calendarId);
                // Logga att kalendern har tagits bort med dess ID.
                logger.info("Calendar deleted with ID: {}", calendarId);
                return true;
            } catch (Exception e) {
                // Om det uppstår ett fel, logga felmeddelandet.
                logger.error("Failed to delete calendar with ID: {}: {}", calendarId, e.getMessage());
                return false;
            }
        } else {
            // Om ingen kalender hittades med det angivna ID:t, logga detta.
            logger.warn("No calendar found with ID: {}, cannot delete", calendarId);
            return false;
        }
    }

    // Metod för att uppdatera en befintlig kalender.
    public boolean updateCalendar(int id, Calendar calendarDetails) {
        // Kontrollera om kalendern med det angivna ID:t finns.
        Optional<Calendar> existingCalendar = calendarRepository.findById(id);
        if (existingCalendar.isPresent()) {
            try {
                // Uppdatera kalendern i databasen och bevara ID:t.
                Calendar updatedCalendar = existingCalendar.get();
                // Kopiera alla fält från calendarDetails till updatedCalendar här, till exempel:
                updatedCalendar.setBookingTime(calendarDetails.getBookingTime());
                // För ManyToOne-fälten måste du se till att du faktiskt kopierar över referensen till de befintliga entiteterna.
                updatedCalendar.setUser(calendarDetails.getUser());
                updatedCalendar.setSession(calendarDetails.getSession());

                calendarRepository.save(updatedCalendar);
                // Logga att kalendern har uppdaterats med dess ID.
                logger.info("Calendar updated with ID: {}", id);
                return true;
            } catch (Exception e) {
                // Om det uppstår ett fel, logga felmeddelandet.
                logger.error("Failed to update calendar with ID: {}: {}", id, e.getMessage());
                return false;
            }
        } else {
            // Om ingen kalender hittades med det angivna ID:t, logga detta.
            logger.warn("No calendar found with ID: {}, cannot update", id);
            return false;
        }
    }
}
