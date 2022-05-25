package de.hsrm.mi.web.projekt.test.ueb06;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import de.hsrm.mi.web.projekt.angebot.Angebot;
import de.hsrm.mi.web.projekt.angebot.AngebotRepository;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerProfil;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerprofilRepository;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerprofilService;


@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@Testable
public class Web_Ueb06_A4_BenutzerProfilAngebot {
    public static final String ABHOLORT = "Neuer Weg, Wassertrüdingen";
    public static final String BESCHREIBUNG = "gelbe Tupelwolle";
    public static final long MINDESTPREIS = 8492;
    public static LocalDateTime ABLAUFZEITPUNKT = LocalDateTime.of(2032,7,15, 23,59,59);


    @Autowired
    private BenutzerprofilRepository benutzerprofilRepository;

    @Autowired
    private AngebotRepository angebotRepository;


    @Autowired
    private BenutzerprofilService benutzerprofilService;


    @Test
    public void vorabcheck() {
        assertThat(BenutzerprofilRepository.class).isInterface();
        assertThat(benutzerprofilRepository).isNotNull();
        assertThat(BenutzerprofilService.class).isInterface();
        assertThat(benutzerprofilService).isNotNull();
        assertThat(AngebotRepository.class).isInterface();
        assertThat(angebotRepository).isNotNull();
    }


    private BenutzerProfil makeBenutzerProfil(String name, int i, String adresse) {
        BenutzerProfil benutzer = new BenutzerProfil();
        benutzer.setName(name);
        benutzer.setAdresse(adresse);
        benutzer.setEmail(name+"@"+name.toLowerCase()+"-online.de");
        benutzer.setGeburtsdatum(LocalDate.of(2000+i, 1+i%12, 1+(5*i)%28));
        benutzer.setLieblingsfarbe(String.format("#%02x%02x%02x", i, i+5, i+10));
        benutzer.setInteressen(i+" Runden reiten, schwimmen, "+name+" ruehmen");
        return benutzer;
    }

    @BeforeEach
    @Transactional
    public void cleanDB() {
        // Repository leeren
        benutzerprofilRepository.deleteAll();
        assertThat(benutzerprofilRepository.count()).isEqualTo(0);

        angebotRepository.deleteAll();
        assertThat(angebotRepository.count()).isEqualTo(0);
    }


    @Test
    @Transactional
    @DisplayName("BenutzerprofilService: fuegeAngebotHinzu(), komme von BenutzerProfil zu Angebot")
    public void benutzerprofilservice_fuegeAngebotHinzu_benutzerprofil_zu_angebot()  {
        final BenutzerProfil marizzibel = makeBenutzerProfil("Marizzibel", 2, "Schloss Biebrich, Wiesbaden");

        // BenutzerProfil in Repository speichern
        BenutzerProfil bp2 = benutzerprofilService.speichereBenutzerProfil(marizzibel);
        long gespeichertId = bp2.getId();

        // Repository müsste nun einen Eintrag enthalten (war vorher leer)
        assertThat(benutzerprofilRepository.count()).isEqualTo(1);

        // Angebot hinzufügen
        Angebot a = new Angebot();
        a.setBeschreibung(BESCHREIBUNG);
        a.setMindestpreis(MINDESTPREIS);
        a.setAbholort(ABHOLORT);
        a.setAblaufzeitpunkt(ABLAUFZEITPUNKT);

        benutzerprofilService.fuegeAngebotHinzu(gespeichertId, a);

        // Benutzerprofil wieder aus DB holen - hängt erwartetes Angebot dran?
        BenutzerProfil testbp = benutzerprofilRepository.findById(gespeichertId).orElseThrow();
        assertThat(testbp.getAngebote().size()).isEqualTo(1);

        Angebot testang = testbp.getAngebote().get(0);
        assertThat(testang.getBeschreibung()).isEqualTo(BESCHREIBUNG);
        assertThat(testang.getMindestpreis()).isEqualTo(MINDESTPREIS);

        // Geo-Koordinaten sollten beim Speichern gesetzt worden sein
        assertThat(testang.getLat()).isBetween(49.04, 49.05);
        assertThat(testang.getLon()).isBetween(10.59, 10.60);
    }


    @Test
    @Transactional
    @DisplayName("BenutzerprofilService: fuegeAngebotHinzu(), komme von Angebot an BenutzerProfil")
    public void benutzerprofilservice_fuegeAngebotHinzu_angebot_zu_benutzerprofil()  {
        final String TESTNAME = "GlogomirDeluxe";
        final BenutzerProfil glogo = makeBenutzerProfil(TESTNAME, 2, "Jagdschloss Platte, Wiesbaden");

        // BenutzerProfil in Repository speichern
        BenutzerProfil bp2 = benutzerprofilService.speichereBenutzerProfil(glogo);
        long gespeichertId = bp2.getId();

        // Repository müsste nun einen Eintrag enthalten (war vorher leer)
        assertThat(benutzerprofilRepository.count()).isEqualTo(1);

        // Angebot hinzufügen
        Angebot a = new Angebot();
        a.setBeschreibung(BESCHREIBUNG);
        a.setMindestpreis(MINDESTPREIS);
        a.setAbholort(ABHOLORT);
        a.setAblaufzeitpunkt(ABLAUFZEITPUNKT);

        benutzerprofilService.fuegeAngebotHinzu(gespeichertId, a);

        // erstes Angebot holen - es kann/darf nur genau eines geben
        Angebot angebot = angebotRepository.findAll().get(0);
        assertThat(angebot.getAbholort()).isEqualTo(ABHOLORT);
        assertThat(angebot.getBeschreibung()).isEqualTo(BESCHREIBUNG);

        // vom Angebot muss man zum Anbieter kommen
        assertThat(angebot.getAnbieter().getId()).isEqualTo(gespeichertId);
        assertThat(angebot.getAnbieter().getName()).isEqualTo(TESTNAME);
    }


    @Test
    @Transactional
    @DisplayName("BenutzerprofilService: loescheAngebot()")
    public void benutzerprofilservice_loescheangebot()  {
        final int ANZAHL = 5;
        final String TESTNAME = "Nussbaer";
        final BenutzerProfil glogo = makeBenutzerProfil(TESTNAME, 2, "Horton Plaza, San Diego, USA");

        // BenutzerProfil in Repository speichern
        BenutzerProfil bp = benutzerprofilService.speichereBenutzerProfil(glogo);
        long gespeichertId = bp.getId();

        // Repository müsste nun einen Eintrag enthalten (war vorher leer)
        assertThat(benutzerprofilRepository.count()).isEqualTo(1);

        // Angebote hinzufügen
        for (int i=0; i < ANZAHL; i++) {
            Angebot a = new Angebot();
            a.setBeschreibung(BESCHREIBUNG+i);
            a.setMindestpreis(MINDESTPREIS+i);
            a.setAbholort(ABHOLORT);
            a.setAblaufzeitpunkt(ABLAUFZEITPUNKT);
    
            benutzerprofilService.fuegeAngebotHinzu(gespeichertId, a);
        }

        assertThat(angebotRepository.count()).isEqualTo(ANZAHL);

        // Angebote löschen
        var angebot_ids = angebotRepository.findAll().stream().map(e -> e.getId()).collect(Collectors.toSet());
        int n = ANZAHL;
        for (var id: angebot_ids) {
            assertThat(angebotRepository.findById(id)).isPresent();
            benutzerprofilService.loescheAngebot(id);
            assertThat(angebotRepository.findById(id)).isEmpty();
            n--;
            assertThat(angebotRepository.count()).isEqualTo(n);
            assertThat(bp.getAngebote().size()).isEqualTo(n);
        }
        // kein Angebot übrig in DB
        assertThat(angebotRepository.count()).isZero();
    }

}