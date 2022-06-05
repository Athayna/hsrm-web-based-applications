package de.hsrm.mi.web.projekt.test.ueb07;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.hsrm.mi.web.projekt.angebot.Angebot;
import de.hsrm.mi.web.projekt.angebot.AngebotRepository;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerProfil;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerprofilRepository;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerprofilService;

@Component
public class Ueb07_init {
    @Autowired
    BenutzerprofilService benutzerprofilService;
    @Autowired
    BenutzerprofilRepository benutzerprofilRepository;
    @Autowired
    AngebotRepository angebotRepository;


    public BenutzerProfil makeBenutzerProfil(String name, int i, String adresse) {
        BenutzerProfil benutzer = new BenutzerProfil();
        benutzer.setName(name);
        benutzer.setAdresse(adresse);
        benutzer.setEmail(name+"@"+name.toLowerCase()+"-epost.de");
        benutzer.setGeburtsdatum(LocalDate.of(2000+i, 1+i%12, 1+(5*i)%28));
        benutzer.setLieblingsfarbe(String.format("#%02x%02x%02x", i, i+5, i+10));
        benutzer.setInteressen(i+" Runden hopsen, klimmen, "+name+" loben");
        return benutzer;
    }
    
    @Transactional
    public void cleanBenutzerAngebotDB() {
        // Benutzerprofile wegwerfen
        benutzerprofilRepository.deleteAll();
        // sollte wegen cascading del nichts zu tun haben...
        angebotRepository.deleteAll();
    }

    private record AngebotRec(String beschreibung, String abholort, long minpreis) {}
    private record BenutzerRec(String name, String adresse, AngebotRec[] angebote) {}

    @Transactional
    public List<BenutzerProfil> initDB() throws IOException {
        cleanBenutzerAngebotDB();

        // BenutzerProfile (Entities) in DB bereitstellen
        List<BenutzerProfil> profilList = new ArrayList<>();
        int i = 1;
        for (BenutzerRec br : List.of(
          new BenutzerRec("Jöndhard", "Unter den Eichen 5, 65195 Wiesbaden",
            new AngebotRec[]{ 
                new AngebotRec("Universalratgeber", "Obere Straße 24, Wankheim", 175),
                new AngebotRec("Medizinischer Spezialratgeber", "Thunesweg 54, Lintorf", 1),
                new AngebotRec("Elektrischer Ratgeber", "Kullenberg 10, Meerbusch", 5)
            }),

            new BenutzerRec("Eustachius", "In den Blamüsen 1, Düsseldorf",
              new AngebotRec[]{ 
                new AngebotRec("Biene Maja DVD", "Spielplatz am Honigbaum, Eppstein", 20),
                new AngebotRec("Leuchtbommelmuetze", "Auf der Platte, Sprollenhaus", 20)}),

            new BenutzerRec("Glogomir", "Waldhörnlestraße 13, Tübingen",
              new AngebotRec[]{ new AngebotRec("Ballnetz", "Sägmühleweg 6, Gompelscheuer", 80)}),

            new BenutzerRec("Joghurta", "Bergweg 1, Enzklösterle",
              new AngebotRec[]{ new AngebotRec("Fahrtrichtungsänderungsanzeiger", "Wohlboldstraße 3, Tübingen", 175)})
            )) {
            i += 1;
            BenutzerProfil bp = makeBenutzerProfil(br.name(), i, br.adresse());
            for (var ang: br.angebote()) {
                Angebot a = new Angebot();
                a.setAnbieter(bp);
                a.setAbholort(ang.abholort());
                a.setBeschreibung(ang.beschreibung());
                a.setMindestpreis(ang.minpreis());
                a.setAblaufzeitpunkt(LocalDateTime.now().plusYears(1));
                bp.getAngebote().add(a);
            }
            // BenutzerProfile (Entities) in DB speichern
            BenutzerProfil entity = benutzerprofilRepository.save(bp);
            profilList.add(entity);
        }
        return profilList;
    }
}
