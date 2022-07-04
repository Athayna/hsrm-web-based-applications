package de.hsrm.mi.web.projekt.projektuser;

import org.springframework.stereotype.Service;

@Service
public interface ProjektUserService {
    ProjektUser neuenBenutzerAnlegen(String username, String klartextpasswort, String rolle);
    ProjektUser findeBenutzer(String username);
}