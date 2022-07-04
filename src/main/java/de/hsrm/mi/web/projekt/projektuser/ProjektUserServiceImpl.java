package de.hsrm.mi.web.projekt.projektuser;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerProfil;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerprofilService;

@Service
public class ProjektUserServiceImpl implements ProjektUserService {
    @Autowired
    private ProjektUserRepository userRep;
    @Autowired
    private PasswordEncoder pwenc;
    @Autowired
    private BenutzerprofilService bService;

    @Override @Transactional
    public ProjektUser neuenBenutzerAnlegen(String username, String klartextpasswort, String rolle) throws ProjektUserServiceException {
        if (rolle == null || rolle == "")
            rolle = "USER";
        if (userRep.findById(username).isPresent())
            throw new ProjektUserServiceException("Benutzer existiert bereits");
        ProjektUser user = new ProjektUser();
        BenutzerProfil profil = new BenutzerProfil();
        user.setUsername(username);
        user.setPassword(pwenc.encode(klartextpasswort));
        user.setRole(rolle);
        profil.setName(username);
        user = userRep.save(user);
        profil = bService.speichereBenutzerProfil(profil);
        user.setBenutzerProfil(profil);
        profil.setProjektUser(user);
        return user;
    }

    @Override @Transactional
    public ProjektUser findeBenutzer(String username) throws ProjektUserServiceException {
        return userRep.findById(username).orElseThrow(() -> new ProjektUserServiceException("Benutzer nicht gefunden"));
    }
}
