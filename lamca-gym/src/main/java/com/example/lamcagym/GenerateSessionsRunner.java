package com.example.lamcagym;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

// Komponent för att generera sessionsdata vid start av applikationen
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateSessionsRunner implements CommandLineRunner {

    @Autowired
    private DataSource dataSource; // Datakälla för att ansluta till databasen

    // Lista med händelser/sessioner
    private List<SessionEvent> events = Arrays.asList(
            new SessionEvent("Yoga", LocalTime.of(7, 0), 90, 20, "Arta"),
            new SessionEvent("Yoga", LocalTime.of(8, 30), 90, 20, "Arta"),
            new SessionEvent("Yoga", LocalTime.of(10, 0), 90, 20, "Arta"),
            new SessionEvent("Grouptraining", LocalTime.of(11, 30), 90, 20, "Lars"),
            new SessionEvent("Grouptraining", LocalTime.of(13, 0), 90, 20, "Lars"),
            new SessionEvent("Grouptraining", LocalTime.of(14, 30), 90, 20, "Mickey"),
            new SessionEvent("Tabata", LocalTime.of(16, 0), 90, 20, "Anders"),
            new SessionEvent("Tabata", LocalTime.of(17, 30), 90, 20, "Anders"),
            new SessionEvent("Spinning", LocalTime.of(19, 0), 90, 20, "Chung"),
            new SessionEvent("Spinning", LocalTime.of(20, 30), 90, 20, "Chung")
    );

    // Metod som körs vid start av applikationen
    @Override
    public void run(String... args) throws Exception {
        if (!sessionsExist()) { // Kontrollera om sessioner redan finns för de kommande 6 månaderna
            LocalDate startDate = LocalDate.now();
            LocalDate endDate = startDate.plusMonths(6);

            try (Connection conn = dataSource.getConnection()) { // Anslut till databasen
                for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) { // Iterera över dagar
                    for (SessionEvent event : events) { // Iterera över sessioner
                        insertEvent(conn, event, date); // Lägg till session i databasen
                    }
                }
            }

        } else {
            System.out.println("Sessions already exist for the next 6 months."); // Meddelande om att sessioner redan finns
        }
    }

    // Metod för att kontrollera om sessioner redan finns för de kommande 6 månaderna
    private boolean sessionsExist() throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM Sessions WHERE date(time) BETWEEN ? AND ?";
        LocalDate sixMonthsFromNow = LocalDate.now().plusMonths(6);
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(checkSql)) {
            stmt.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            stmt.setDate(2, java.sql.Date.valueOf(sixMonthsFromNow));
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        }
        return false;
    }

    // Metod för att lägga till en session i databasen
    private void insertEvent(Connection conn, SessionEvent event, LocalDate date) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM Sessions WHERE session_type = ? AND DATE(time) = ? AND TIME(time) = ?";
        String insertSql = "INSERT INTO Sessions (session_type, time, duration, capacity, instructor) VALUES (?, ?, ?, ?, ?)";

        LocalTime startTime = event.getStartTime();
        String dateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(date.atTime(startTime));

        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, event.getType());
            checkStmt.setString(2, date.toString());
            checkStmt.setString(3, startTime.toString());

            ResultSet resultSet = checkStmt.executeQuery();
            if (resultSet.next() && resultSet.getInt(1) == 0) { // Kontrollera om sessionen redan finns
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setString(1, event.getType());
                    insertStmt.setString(2, dateTime);
                    insertStmt.setInt(3, event.getDuration());
                    insertStmt.setInt(4, event.getCapacity());
                    insertStmt.setString(5, event.getInstructor());

                    insertStmt.executeUpdate(); // Utför insättningen i databasen
                }
            }
        }
    }

    // Dataklass för en session/händelse
    @Data
    @AllArgsConstructor
    static class SessionEvent {
        private String type; // Typ av session
        private LocalTime startTime; // Starttid för sessionen
        private int duration, capacity; // Varaktighet och kapacitet för sessionen
        private String instructor; // Instruktör för sessionen
    }
}