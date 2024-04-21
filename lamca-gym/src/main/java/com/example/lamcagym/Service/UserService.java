package com.example.lamcagym.Service;

import com.example.lamcagym.Repository.UserRepository;
import com.example.lamcagym.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService {
    // Skapa en logger för att logga information, varningar och fel för UserService.
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    // Autowired används för att automatiskt tråda userRepository beroende av UserRepository.
    @Autowired
    private UserRepository userRepository;

    // Metod för att skapa en ny användare.
    public boolean createUser(User user) {
        try {
            // Spara den nya användaren i databasen.
            User newUser = userRepository.save(user);
            // Logga att användaren har skapats med sitt ID.
            logger.info("User created with ID: {}", newUser.getUserId());
            return true;
        } catch (Exception e) {
            // Om det uppstår ett fel, logga felmeddelandet.
            logger.error("Failed to create user: {}", e.getMessage());
            return false;
        }
    }

    // Metod för att hämta en användare med ett specifikt ID.
    public User getUser(int id) {
        // Försök att hämta användaren från databasen med det angivna ID:t.
        Optional<User> user = userRepository.findById(id);
        // Om användaren finns, returnera den.
        if (user.isPresent()) {
            return user.get();
        } else {
            // Annars, logga att ingen användare hittades med det angivna ID:t.
            logger.warn("No user found with ID: {}", id);
            return null;
        }
    }

    // Metod för att hämta en användare med en specifik e-postadress.
    public User getUserByEmail(String email) {
        // Försök att hämta användaren från databasen med den angivna e-postadressen.
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));
        // Om användaren finns, returnera den.
        if (user.isPresent()) {
            return user.get();
        } else {
            // Annars, logga att ingen användare hittades med den angivna e-postadressen.
            logger.warn("No user found with email: {}", email);
            return null;
        }
    }

    // Metod för att ta bort en användare med ett specifikt ID.
    public boolean deleteUser(int id) {
        // Kontrollera om användaren med det angivna ID:t finns.
        if (userRepository.existsById(id)) {
            // Om användaren finns, ta bort den från databasen.
            userRepository.deleteById(id);
            // Logga att användaren har tagits bort med sitt ID.
            logger.info("User deleted with ID: {}", id);
            return true;
        } else {
            // Om ingen användare hittades med det angivna ID:t, logga detta.
            logger.warn("No user found with ID: {}, cannot delete", id);
            return false;
        }
    }

    // Metod för att uppdatera en befintlig användare.
    public boolean updateUser(User user) {
        // Kontrollera om användaren med det angivna ID:t finns.
        if (userRepository.existsById(user.getUserId())) {
            try {
                // Uppdatera användaren i databasen.
                userRepository.save(user);
                // Logga att användaren har uppdaterats med sitt ID.
                logger.info("User updated with ID: {}", user.getUserId());
                return true;
            } catch (Exception e) {
                // Om det uppstår ett fel, logga felmeddelandet.
                logger.error("Failed to update user with ID: {}: {}", user.getUserId(), e.getMessage());
                return false;
            }
        } else {
            // Om ingen användare hittades med det angivna ID:t, logga detta.
            logger.warn("No user found with ID: {}, cannot update", user.getUserId());
            return false;
        }
    }

    // Metod för att hämta alla användare från databasen.
    public ArrayList<User> getAll() {
        return (ArrayList<User>) userRepository.findAll();
    }
}
