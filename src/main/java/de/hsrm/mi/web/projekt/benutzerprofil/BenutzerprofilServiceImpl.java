package de.hsrm.mi.web.projekt.benutzerprofil;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import de.hsrm.mi.web.projekt.angebot.Angebot;
import de.hsrm.mi.web.projekt.angebot.AngebotRepository;
import de.hsrm.mi.web.projekt.geo.GeoServiceImpl;

@Service
public class BenutzerprofilServiceImpl implements BenutzerprofilService{
    @Autowired
    BenutzerprofilRepository benRep;
    @Autowired
    GeoServiceImpl gServiceImpl;
    @Autowired
    AngebotRepository anRep;

    @Override @Transactional
    public BenutzerProfil speichereBenutzerProfil(BenutzerProfil bp) {
        if (gServiceImpl.findeAdressInfo(bp.getAdresse()).isEmpty()) {
            bp.setLon(0);
            bp.setLat(0);
        } else {
            bp.setLat(gServiceImpl.findeAdressInfo(bp.getAdresse()).get(0).lat());
            bp.setLon(gServiceImpl.findeAdressInfo(bp.getAdresse()).get(0).lon());
        }
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

    @Override @Transactional
    public void fuegeAngebotHinzu(long id, Angebot angebot) {
        if (angebot.getAbholort() == null || gServiceImpl.findeAdressInfo(angebot.getAbholort()).isEmpty()) {
            angebot.setLon(0);
            angebot.setLat(0);
        } else {
            angebot.setLon(gServiceImpl.findeAdressInfo(angebot.getAbholort()).get(0).lon());
            angebot.setLat(gServiceImpl.findeAdressInfo(angebot.getAbholort()).get(0).lat());
        }
        BenutzerProfil bp = holeBenutzerProfilMitId(id).get();
        if(!bp.getAngebote().contains(angebot)) {
            bp.getAngebote().add(angebot);
            angebot.setAnbieter(bp);
        }
        benRep.save(bp);
    }

    @Override @Transactional
    public void loescheAngebot(long id) {
        Angebot an = anRep.getById(id);
        BenutzerProfil bp = an.getAnbieter();
        bp.getAngebote().remove(an);
        anRep.deleteById(id);
        benRep.save(bp);
    }

    @Override @Transactional
    public List<Angebot> alleAngebote() {
        return anRep.findAll();
    }

    @Override @Transactional
    public Optional<Angebot> findeAngebotMitId(long angebotid) {
        return anRep.findById(angebotid);
    }
}
