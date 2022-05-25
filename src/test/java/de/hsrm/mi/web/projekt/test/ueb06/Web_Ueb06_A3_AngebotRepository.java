package de.hsrm.mi.web.projekt.test.ueb06;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import de.hsrm.mi.web.projekt.angebot.Angebot;
import de.hsrm.mi.web.projekt.angebot.AngebotRepository;



@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@Testable
public class Web_Ueb06_A3_AngebotRepository {
    private final String BESCHREIBUNG = "Joghurta Biffel";
	private final long MINDESTPREIS = 1234;
    private final LocalDateTime ABLAUFZEITPUNKT = LocalDateTime.of(2032,7,17, 17,30,00);
	private final String ABHOLORT = "Bergweg 1, Enzklösterle";


    @Autowired
    private AngebotRepository angebotRepository;


    @Test
    public void vorabcheck() {
        assertThat(AngebotRepository.class).isInterface();
        assertThat(angebotRepository).isNotNull();
    }

    @Test
    @Transactional
    @DisplayName("Angebot-Entity im Repository persistieren")
    public void angebot_persist() throws IOException {
        final Angebot unmanaged = new Angebot();
        unmanaged.setBeschreibung(BESCHREIBUNG);
        unmanaged.setMindestpreis(MINDESTPREIS);
        unmanaged.setAblaufzeitpunkt(ABLAUFZEITPUNKT);
        unmanaged.setAbholort(ABHOLORT);

        // Repository leeren
        angebotRepository.deleteAll();
        assertThat(angebotRepository.count()).isEqualTo(0);

        // Angebot in Repository speichern
        final Angebot managed = angebotRepository.save(unmanaged);
        long gespeichertId = managed.getId();

        // Repository müsste nun einen Eintrag enthalten (war vorher leer)
        assertThat(angebotRepository.count()).isEqualTo(1);

        // und wieder frisch aus DB auslesen
        Angebot neugelesen = angebotRepository.findById(gespeichertId).orElseThrow();
        assertThat(neugelesen.getBeschreibung()).isEqualTo(BESCHREIBUNG);
        assertThat(neugelesen.getMindestpreis()).isEqualTo(MINDESTPREIS);
        assertThat(neugelesen.getAblaufzeitpunkt()).isEqualTo(ABLAUFZEITPUNKT);
        assertThat(neugelesen.getAbholort()).isEqualTo(ABHOLORT);
    }

}