package com.example.lamcagym.Service;

import com.example.lamcagym.Entity.Session;
import com.example.lamcagym.Repository.SessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SessionService {

    // Skapa en logger för att logga information, varningar och fel för SessionService.
    private static final Logger logger = LoggerFactory.getLogger(SessionService.class);

    // Autowired används för att automatiskt tråda sessionRepository beroende av SessionRepository.
    @Autowired
    private SessionRepository sessionRepository;

    // Metod för att hämta alla sessioner från databasen.
    public List<Session> getAll() {
        return sessionRepository.findAll();
    }

    // Metod för att skapa en ny session.
    public Session createSession(Session session) {
        try {
            // Spara den nya sessionen i databasen.
            Session savedSession = sessionRepository.save(session);
            // Logga att sessionen har skapats med dess ID.
            logger.info("Session created with ID: {}", savedSession.getSessionId());
            return savedSession;
        } catch (Exception e) {
            // Om det uppstår ett fel, logga felmeddelandet.
            logger.error("Failed to create session: {}", e.getMessage());
            return null;
        }
    }

    // Metod för att hämta en session med ett specifikt ID.
    public Session getSession(int sessionId) {
        // Försök att hämta sessionen från databasen med det angivna ID:t.
        Optional<Session> session = sessionRepository.findById(sessionId);
        // Om sessionen finns, returnera den.
        if (session.isPresent()) {
            return session.get();
        } else {
            // Annars, logga att ingen session hittades med det angivna ID:t.
            logger.warn("No session found with ID: {}", sessionId);
            return null;
        }
    }

    // Metod för att ta bort en session med ett specifikt ID.
    public boolean deleteSession(int sessionId) {
        // Kontrollera om sessionen med det angivna ID:t finns.
        if (sessionRepository.existsById(sessionId)) {
            try {
                // Om sessionen finns, ta bort den från databasen.
                sessionRepository.deleteById(sessionId);
                // Logga att sessionen har tagits bort med dess ID.
                logger.info("Session deleted with ID: {}", sessionId);
                return true;
            } catch (Exception e) {
                // Om det uppstår ett fel, logga felmeddelandet.
                logger.error("Failed to delete session with ID: {}: {}", sessionId, e.getMessage());
                return false;
            }
        } else {
            // Om ingen session hittades med det angivna ID:t, logga detta.
            logger.warn("No session found with ID: {}, cannot delete", sessionId);
            return false;
        }
    }

    // Metod för att uppdatera en befintlig session.
    public Session updateSession(int sessionId, Session sessionDetails) {
        // Kontrollera om sessionen med det angivna ID:t finns.
        if (sessionRepository.existsById(sessionId)) {
            try {
                // Uppdatera sessionen i databasen och bevara ID:t.
                sessionDetails.setSessionId(sessionId);
                Session updatedSession = sessionRepository.save(sessionDetails);
                // Logga att sessionen har uppdaterats med dess ID.
                logger.info("Session updated with ID: {}", sessionId);
                return updatedSession;
            } catch (Exception e) {
                // Om det uppstår ett fel, logga felmeddelandet.
                logger.error("Failed to update session with ID: {}: {}", sessionId, e.getMessage());
                return null;
            }
        } else {
            // Om ingen session hittades med det angivna ID:t, logga detta.
            logger.warn("No session found with ID: {}, cannot update", sessionId);
            return null;
        }
    }
}
