package com.example.lamcagym.Service;


import com.example.lamcagym.Entity.Session;
import com.example.lamcagym.Repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

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
}