package de.hsrm.mi.web.projekt.benutzerprofil;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class BenutzerprofilServiceImpl implements BenutzerprofilService{
    @Autowired
    BenutzerprofilRepository benRep;

    @Override @Transactional
    public BenutzerProfil speichereBenutzerProfil(BenutzerProfil bp) {
        return benRep.save(bp);
    }

    @Override
    public Optional<BenutzerProfil> holeBenutzerProfilMitId(Long id) {
        if (benRep.findById(id) != null)
            return benRep.findById(id);
        return Optional.empty();
    }

    @Override
    public List<BenutzerProfil> alleBenutzerProfile() {
        return benRep.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Override
    public void loescheBenutzerProfilMitId(Long loesch) {
        benRep.deleteById(loesch);
    }
}
