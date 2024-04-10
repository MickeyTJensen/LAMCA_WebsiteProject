package com.example.lamcagym.Service;


import com.example.lamcagym.Entity.Session;
import com.example.lamcagym.Entity.SessionDTO;
import com.example.lamcagym.Repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    public ArrayList<Session> getAll() {
        return (ArrayList<Session>) sessionRepository.findAll();
    }

    public boolean createSession(Session session) {
        // Lägg till logik för att spara sessionen i databasen
        try {
            sessionRepository.save(session);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Session getSession(int sessionId) {
        Optional<Session> optionalSession = sessionRepository.findById(sessionId);
        return optionalSession.orElse(null);
    }

    public boolean deleteSession(int sessionId) {
        if (sessionRepository.existsById(sessionId)) {
            sessionRepository.deleteById(sessionId);
            return true;
        }
        return false;
    }

    public boolean updateSession(Session session) {
        if (sessionRepository.existsById(session.getSessionId())) {
            sessionRepository.save(session);
            return true;
        }
        return false;
    }

    public ArrayList<Session> getAllSessions() {
        return (ArrayList<Session>) sessionRepository.findAll();
    }

    // Inom SessionService eller SessionController
    public List<SessionDTO> getAllSessionDTOs() {
        List<Session> sessions = sessionRepository.findAll();
        return sessions.stream().map(session -> {
            SessionDTO dto = new SessionDTO();
            dto.setId(session.getSessionId().toString());
            dto.setTitle(session.getSessionType());
            dto.setStart(session.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            dto.setDuration(session.getDuration()); // Anta att du har en getter för duration
            dto.calculateEnd(); // Beräknar `end` baserat på `start` och `duration`
            return dto;
        }).collect(Collectors.toList());
    }
}