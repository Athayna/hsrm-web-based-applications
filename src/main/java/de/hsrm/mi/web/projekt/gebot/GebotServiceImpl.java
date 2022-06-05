package de.hsrm.mi.web.projekt.gebot;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.hsrm.mi.web.projekt.angebot.Angebot;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerProfil;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerprofilServiceImpl;

@Service
public class GebotServiceImpl implements GebotService {
    @Autowired
    GebotRepository gebRep;
    @Autowired
    BenutzerprofilServiceImpl bService;

    @Override
    public List<Gebot> findeAlleGebote() {
        return gebRep.findAll();
    }

    @Override
    public List<Gebot> findeAlleGeboteFuerAngebot(long angebotid) {
        return bService.findeAngebotMitId(angebotid).get().getGebote();
    }

    @Override
    public Gebot bieteFuerAngebot(long benutzerprofilid, long angebotid, long betrag) {
        Angebot angebot = bService.findeAngebotMitId(angebotid).get();
        BenutzerProfil benutzer = bService.holeBenutzerProfilMitId(benutzerprofilid).get();
        Gebot gebot;
        try {
            gebot = gebRep.findByAngebotIdAndBieterId(angebotid, benutzerprofilid).get();
        } catch (NoSuchElementException e) {
            gebot = new Gebot();
            gebot.setAngebot(angebot);
            gebot.setGebieter(benutzer);
            angebot.getGebote().add(gebot);
            benutzer.getGebote().add(gebot);
        }
        gebot.setBetrag(betrag);
        gebot.setGebotzeitpunkt(LocalDateTime.now());
        gebRep.save(gebot);
        return gebot;
    }

    @Override
    public void loescheGebot(long gebotid) {
        Gebot geb = gebRep.getById(gebotid);
        BenutzerProfil bp = geb.getGebieter();
        Angebot an = geb.getAngebot();
        bp.getGebote().remove(geb);
        an.getGebote().remove(geb);
        gebRep.deleteById(gebotid);
        bService.speichereBenutzerProfil(bp);
    }
}
