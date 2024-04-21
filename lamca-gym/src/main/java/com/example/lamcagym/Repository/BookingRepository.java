package com.example.lamcagym.Repository;

import com.example.lamcagym.Entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    // Funktion för att hitta bokningar baserat på sessionens id och användarens id
    List<Booking> findBySession_SessionIdAndUser_UserId(Integer sessionId, Integer userId);

    // Funktion för att räkna antalet bokningar för en viss session baserat på sessionens id
    int countBySession_SessionId(Integer sessionId);
}