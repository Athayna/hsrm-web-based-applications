package de.hsrm.mi.web.projekt.test.ueb07;

import static org.assertj.core.api.Assertions.assertThat;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import de.hsrm.mi.web.projekt.angebot.AngebotRepository;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerprofilRepository;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerprofilService;
import de.hsrm.mi.web.projekt.gebot.Gebot;
import de.hsrm.mi.web.projekt.gebot.GebotRepository;
import de.hsrm.mi.web.projekt.gebot.GebotService;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class Web_Ueb07_A1_GebotService {

    @Autowired
    private BenutzerprofilRepository benutzerprofilRepository;
    @Autowired
    private GebotRepository gebotRepository;
    @Autowired
    private GebotService gebotService;
    @Autowired
    private AngebotRepository angebotRepository;

    @Autowired
    private BenutzerprofilService benutzerprofilService;

    @Autowired
    Ueb07_init ueb07_init;

    @Test
    public void vorabcheck() {
        assertThat(BenutzerprofilRepository.class).isInterface();
        assertThat(benutzerprofilRepository).isNotNull();
        assertThat(benutzerprofilService).isNotNull();
        assertThat(gebotRepository).isNotNull();
    }

    @BeforeEach
    @Transactional
    public void cleanDB()  {
        ueb07_init.cleanBenutzerAngebotDB();
        gebotRepository.deleteAll();
    }


    @Test
    @DisplayName("GebotService - bieteFuerAngebot() - Angebot anlegen")
    @Transactional
    public void gebotservice_angebot_anlegen() throws Exception {
        final long BETRAG = 987L;
        var benutzerlst = ueb07_init.initDB();
        var bieter = benutzerlst.get(1);
        var anbieter = benutzerlst.get(0);
        var angebot = anbieter.getAngebote().get(1);

        Gebot g = gebotService.bieteFuerAngebot(bieter.getId(), angebot.getId(), BETRAG);
        
        assertThat(g.getAngebot().getId()).isEqualTo(angebot.getId());
        assertThat(g.getBetrag()).isEqualTo(BETRAG);
        assertThat(g.getGebieter().getId()).isEqualTo(bieter.getId());

        // gucken, ob's in der DB angekommen ist
        var gid = g.getId();
        var gg = gebotRepository.findById(gid).orElseThrow();
        assertThat(gg.getAngebot().getId()).isEqualTo(angebot.getId());
        assertThat(gg.getBetrag()).isEqualTo(BETRAG);
        assertThat(gg.getGebieter().getId()).isEqualTo(bieter.getId());

        var b = benutzerprofilRepository.findById(bieter.getId()).orElseThrow();
        var a = angebotRepository.findById(angebot.getId()).orElseThrow();
        assertThat(b.getGebote()).contains(g);
        assertThat(a.getGebote()).contains(g);
    }

    @Test
    @DisplayName("GebotService - zweimal bieteFuerAngebot() - Angebotsänderung")
    @Transactional
    public void gebotservice_angebot_ueberbieten() throws Exception {
        final long BETRAG_1 = 987L;
        final long BETRAG_2 = 1987L;
        var benutzerlst = ueb07_init.initDB();
        var anbieter = benutzerlst.get(0);
        var angebot = anbieter.getAngebote().get(1);
        var bieter = benutzerlst.get(1);

        Gebot g = gebotService.bieteFuerAngebot(bieter.getId(), angebot.getId(), BETRAG_1);
        assertThat(gebotRepository.count()).isEqualTo(1);

        // gucken, ob's in der DB angekommen ist
        var gid = g.getId();
        var gg1 = gebotRepository.findById(gid).orElseThrow();
        assertThat(gg1.getAngebot().getId()).isEqualTo(angebot.getId());
        assertThat(gg1.getBetrag()).isEqualTo(BETRAG_1);
        assertThat(gg1.getGebieter().getId()).isEqualByComparingTo(bieter.getId());

        // Angebot erhöhen überbietet
        Gebot g2 = gebotService.bieteFuerAngebot(bieter.getId(), angebot.getId(), BETRAG_2);
        assertThat(gebotRepository.count()).isEqualTo(1);
        assertThat(g.getId()).isEqualTo(g2.getId());

        // gucken, ob Aenderung in der DB angekommen ist
        var gg2 = gebotRepository.findById(gid).orElseThrow();
        assertThat(gg2.getAngebot().getId()).isEqualTo(angebot.getId());
        assertThat(gg2.getBetrag()).isEqualTo(BETRAG_2);
        assertThat(gg2.getGebieter().getId()).isEqualByComparingTo(bieter.getId());
    }    

    @Test
    @DisplayName("GebotService - zwei Bieter mit bieteFuerAngebot()")
    @Transactional
    public void gebotservice_angebot_zwei_bieter() throws Exception {
        final long BETRAG_1 = 123;
        final long BETRAG_2 = 456L;
        var benutzerlst = ueb07_init.initDB();
        var anbieter = benutzerlst.get(0);
        var angebot = anbieter.getAngebote().get(1);
        var bieter1 = benutzerlst.get(1);
        var bieter2 = benutzerlst.get(2);

        Gebot g = gebotService.bieteFuerAngebot(bieter1.getId(), angebot.getId(), BETRAG_1);
        assertThat(gebotRepository.count()).isEqualTo(1);

        // gucken, ob's in der DB angekommen ist
        var gid = g.getId();
        var gg1 = gebotRepository.findById(gid).orElseThrow();
        assertThat(gg1.getAngebot().getId()).isEqualTo(angebot.getId());
        assertThat(gg1.getBetrag()).isEqualTo(BETRAG_1);
        assertThat(gg1.getGebieter().getId()).isEqualByComparingTo(bieter1.getId());

        // zweiter Bieter erhöhen überbietet
        Gebot g2 = gebotService.bieteFuerAngebot(bieter2.getId(), angebot.getId(), BETRAG_2);
        assertThat(gebotRepository.count()).isEqualTo(2);

        // gucken, ob Aenderung in der DB angekommen ist
        var gg2 = gebotRepository.findById(g2.getId()).orElseThrow();
        assertThat(gg2.getAngebot().getId()).isEqualTo(angebot.getId());
        assertThat(gg2.getBetrag()).isEqualTo(BETRAG_2);
        assertThat(gg2.getGebieter().getId()).isEqualByComparingTo(bieter2.getId());
    }        

    @Test
    @DisplayName("GebotService - findeAlleGebote()")
    @Transactional
    public void gebotservice_alle_gebote() throws Exception {
        final long BETRAG_1 = 123;
        final long BETRAG_2 = 456L;
        var benutzerlst = ueb07_init.initDB();
        var anbieter = benutzerlst.get(0);
        var angebot = anbieter.getAngebote().get(1);
        var bieter1 = benutzerlst.get(1);
        var bieter2 = benutzerlst.get(2);

        Gebot g = gebotService.bieteFuerAngebot(bieter1.getId(), angebot.getId(), BETRAG_1);
        assertThat(gebotRepository.count()).isEqualTo(1);

        // zweiter Bieter erhöhen überbietet
        Gebot g2 = gebotService.bieteFuerAngebot(bieter2.getId(), angebot.getId(), BETRAG_2);
        assertThat(gebotRepository.count()).isEqualTo(2);

        var gebote = gebotService.findeAlleGebote();
        assertThat(gebote.size()).isEqualTo(2);

        assertThat(gebote).contains(g);
        assertThat(gebote).contains(g2);
    }        


    @Test
    @DisplayName("GebotService - findeAlleGeboteFuerAngebot()")
    @Transactional
    public void gebotservice_alle_gebote_fuer_angebot() throws Exception {
        final long BETRAG_1 = 123;
        final long BETRAG_2 = 456L;
        final long BETRAG_3 = 789L;
        var benutzerlst = ueb07_init.initDB();
        var anbieter = benutzerlst.get(0);
        var angebot1 = anbieter.getAngebote().get(1);
        var angebot2 = anbieter.getAngebote().get(2);
        var bieter1 = benutzerlst.get(1);
        var bieter2 = benutzerlst.get(2);
        var bieter3 = benutzerlst.get(3);

        // erster Bieter fuer Angebot 1
        Gebot g = gebotService.bieteFuerAngebot(bieter1.getId(), angebot1.getId(), BETRAG_1);
        assertThat(gebotRepository.count()).isEqualTo(1);

        // zweiter Bieter für Angebot 1
        Gebot g2 = gebotService.bieteFuerAngebot(bieter2.getId(), angebot1.getId(), BETRAG_2);
        assertThat(gebotRepository.count()).isEqualTo(2);

        // dritter Bieter bietet für anderes Angebot
        Gebot g3 = gebotService.bieteFuerAngebot(bieter3.getId(), angebot2.getId(), BETRAG_3);
        assertThat(gebotRepository.count()).isEqualTo(3);

        // findeAlleGeboteFuerAngebot(angebot1)
        var gebote1 = gebotService.findeAlleGeboteFuerAngebot(angebot1.getId());
        assertThat(gebote1.size()).isEqualTo(2);
        assertThat(gebote1).contains(g);
        assertThat(gebote1).contains(g2);

        // findeAlleGeboteFuerAngebot(angebot2)
        var gebote2 = gebotService.findeAlleGeboteFuerAngebot(angebot2.getId());
        assertThat(gebote2.size()).isEqualTo(1);
        assertThat(gebote2).contains(g3);
    }        

    @Test
    @DisplayName("GebotService - lösche Gebot")
    @Transactional
    public void gebotservice_loesche_gebot() throws Exception {
        final long BETRAG_1 = 123;
        final long BETRAG_2 = 456L;
        var benutzerlst = ueb07_init.initDB();
        var anbieter = benutzerlst.get(0);
        var angebot = anbieter.getAngebote().get(1);
        var bieter1 = benutzerlst.get(1);
        var bieter2 = benutzerlst.get(2);

        Gebot g = gebotService.bieteFuerAngebot(bieter1.getId(), angebot.getId(), BETRAG_1);
        assertThat(gebotRepository.count()).isEqualTo(1);

        // gucken, ob's in der DB angekommen ist
        var gid = g.getId();
        var gg1 = gebotRepository.findById(gid).orElseThrow();
        assertThat(gg1.getAngebot().getId()).isEqualTo(angebot.getId());
        assertThat(gg1.getBetrag()).isEqualTo(BETRAG_1);
        assertThat(gg1.getGebieter().getId()).isEqualByComparingTo(bieter1.getId());

        // zweiter Bieter erhöhen überbietet
        Gebot g2 = gebotService.bieteFuerAngebot(bieter2.getId(), angebot.getId(), BETRAG_2);
        assertThat(gebotRepository.count()).isEqualTo(2);

        // gucken, ob Aenderung in der DB angekommen ist
        var gg2 = gebotRepository.findById(g2.getId()).orElseThrow();
        assertThat(gg2.getAngebot().getId()).isEqualTo(angebot.getId());
        assertThat(gg2.getBetrag()).isEqualTo(BETRAG_2);
        assertThat(gg2.getGebieter().getId()).isEqualByComparingTo(bieter2.getId());

        // sukzessives Löschen der beiden Gebote
        assertThat(gebotRepository.findById(g2.getId())).isPresent();
        gebotService.loescheGebot(g2.getId());
        assertThat(gebotRepository.findById(g2.getId())).isEmpty();

        assertThat(gebotRepository.findById(gid)).isPresent();
        gebotService.loescheGebot(g.getId());
        assertThat(gebotRepository.findById(gid)).isEmpty();

    }        
}


/*
    List<Gebot> findeAlleGebote();
    List<Gebot> findeAlleGeboteFuerAngebot(long angebotid);
*/