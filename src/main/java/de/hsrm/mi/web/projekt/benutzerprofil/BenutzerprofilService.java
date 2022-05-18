package de.hsrm.mi.web.projekt.benutzerprofil;

import java.util.List;
import java.util.Optional;

public interface BenutzerprofilService {
    public BenutzerProfil speichereBenutzerProfil(BenutzerProfil bp);
    public Optional<BenutzerProfil> holeBenutzerProfilMitId(Long id);
    public List<BenutzerProfil> alleBenutzerProfile();
    public void loescheBenutzerProfilMitId(Long loesch);
}
