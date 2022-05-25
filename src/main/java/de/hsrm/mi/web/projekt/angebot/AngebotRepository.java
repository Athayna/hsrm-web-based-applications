package de.hsrm.mi.web.projekt.angebot;

import org.springframework.data.jpa.repository.JpaRepository;

import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerProfil;

public interface AngebotRepository extends JpaRepository<BenutzerProfil, Long> {
    
}
