package com.example.lamcagym;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Component
public class GenerateSessionsRunner implements CommandLineRunner {

    private final DataSource dataSource;

    public GenerateSessionsRunner(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    @Override
    public void run(String... args) throws Exception {
        if (!sessionsExist()) {
            List<SessionEvent> events = Arrays.asList(
                    new SessionEvent("Yoga", LocalTime.of(7, 0), 90, 20, "Arta"),
                    new SessionEvent("Yoga", LocalTime.of(8, 30), 90, 20, "Arta"),
                    new SessionEvent("Yoga", LocalTime.of(10, 0), 90, 20, "Arta"),
                    new SessionEvent("Gruppträning", LocalTime.of(11, 30), 90, 20, "Lars"),
                    new SessionEvent("Gruppträning", LocalTime.of(13, 0), 90, 20, "Lars"),
                    new SessionEvent("Gruppträning", LocalTime.of(14, 30), 90, 20, "Mickey"),
                    new SessionEvent("Tabata", LocalTime.of(16, 0), 90, 20, "Anders"),
                    new SessionEvent("Tabata", LocalTime.of(17, 30), 90, 20, "Anders"),
                    new SessionEvent("Spinning", LocalTime.of(19, 0), 90, 20, "Chung"),
                    new SessionEvent("Spinning", LocalTime.of(20, 30), 90, 20, "Chung")
                    // Lägg till fler events här...
            );

            LocalDate startDate = LocalDate.now();
            LocalDate endDate = startDate.plusMonths(6);

            try (Connection conn = dataSource.getConnection()) {
                for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                    for (SessionEvent event : events) {
                        insertEvent(conn, event, date);
                    }
                }
            }

        } else {
            System.out.println("Sessioner finns redan för den kommande 6 månaderna.");
        }
    }
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
            if (resultSet.next() && resultSet.getInt(1) == 0) {
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setString(1, event.getType());
                    insertStmt.setString(2, dateTime);
                    insertStmt.setInt(3, event.getDuration());
                    insertStmt.setInt(4, event.getCapacity());
                    insertStmt.setString(5, event.getInstructor());

                    insertStmt.executeUpdate();
                }
            }
        }
    }

    static class SessionEvent {
        private String type;
        private LocalTime startTime;
        private int duration, capacity;
        private String instructor;

        public SessionEvent(String type, LocalTime startTime, int duration, int capacity, String instructor) {
            this.type = type;
            this.startTime = startTime;
            this.duration = duration;
            this.capacity = capacity;
            this.instructor = instructor;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public LocalTime getStartTime() {
            return startTime;
        }

        public void setStartTime(LocalTime startTime) {
            this.startTime = startTime;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getCapacity() {
            return capacity;
        }

        public void setCapacity(int capacity) {
            this.capacity = capacity;
        }

        public String getInstructor() {
            return instructor;
        }

        public void setInstructor(String instructor) {
            this.instructor = instructor;
        }


    }
}