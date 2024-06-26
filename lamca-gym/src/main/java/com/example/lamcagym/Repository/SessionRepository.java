package com.example.lamcagym.Repository;

import com.example.lamcagym.Entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repository-gränssnitt för att hantera dataåtkomst för entiteten Session
@Repository
public interface SessionRepository extends JpaRepository<Session, Integer> {

}