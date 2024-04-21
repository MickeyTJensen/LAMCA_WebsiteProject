package com.example.lamcagym.Repository;

import com.example.lamcagym.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repository-gränssnitt för att hantera dataåtkomst för entiteten User
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // Metod för att kontrollera om en användare med angiven e-postadress redan finns i databasen
    boolean existsByEmail(String email);

    // Metod för att hitta en användare baserat på dess e-postadress
    User findByEmail(String email);
}