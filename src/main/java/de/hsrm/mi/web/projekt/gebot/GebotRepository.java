package de.hsrm.mi.web.projekt.gebot;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GebotRepository extends JpaRepository<Gebot, Long> {
    @Query("SELECT g FROM Gebot g WHERE g.angebot.id=?1 AND g.gebieter.id=?2")
    Optional<Gebot> findByAngebotIdAndBieterId(long angebotId, long BieterId);
}
