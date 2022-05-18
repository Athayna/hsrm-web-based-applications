package de.hsrm.mi.web.projekt.test.ueb05;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.time.LocalDate;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerProfil;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerprofilRepository;


/* HINWEIS:
 * In VSCode Settings (File > Preferences > Settings oder [strg][,])
 * Boot-Java > Live-Information > Automatic Connection: AUS schalten
 * (in Settings einfach nach "JMX" suchen)
 * sonst unregelm. Laufzeitfehler bei @SpringBootTest-Klassen
 * ("JMX InstanceAlreadyExistsException" und Folgefehler bzgl.
 * ApplicationContext-Instanziierung) im VSCode Testrunner
 * (während "gradle test" durchläuft)
 */


@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@Testable
public class Web_Ueb05_A2_BenutzerprofilRepository {
    private final String TESTNAME = "Joghurta Biffel";
	private final String TESTORT = "In der Ecke 17, 99441 Vollradisroda";
    private final LocalDate TESTDATUML = LocalDate.of(2021,7,17);
	private final String TESTEMAIL = "joghurta.biffel@vollradisroda.be";
	private final String TESTFARBE = "#4b8DeF";
    private final String TESTINTERESSEN = "weidomieren, folloppen, wuhnen";


    @Autowired
    private BenutzerprofilRepository benutzerProfilRepository;


    @Test
    public void vorabcheck() {
        assertThat(BenutzerprofilRepository.class).isInterface();
        assertThat(benutzerProfilRepository).isNotNull();
    }

    @Test
    @Transactional
    @DisplayName("BenutzerProfil-Entity im Repository persistieren")
    public void benutzerprofil_persist() throws IOException {
        final BenutzerProfil unmanaged = new BenutzerProfil();
        // Id wird bei save() von JPA vergeben
        unmanaged.setName(TESTNAME);
        unmanaged.setGeburtsdatum(TESTDATUML);
        unmanaged.setAdresse(TESTORT);
        unmanaged.setEmail(TESTEMAIL);
        unmanaged.setInteressen(TESTINTERESSEN);
        unmanaged.setLieblingsfarbe(TESTFARBE);

        // Repository leeren
        benutzerProfilRepository.deleteAll();
        assertThat(benutzerProfilRepository.count()).isEqualTo(0);

        // BenutzerProfil in Repository speichern
        final BenutzerProfil managed = benutzerProfilRepository.save(unmanaged);
        long gespeichertId = managed.getId();

        // Repository müsste nun einen Eintrag enthalten (war vorher leer)
        assertThat(benutzerProfilRepository.count()).isEqualTo(1);

        // und wieder frisch aus DB auslesen
        BenutzerProfil neugelesen = benutzerProfilRepository.findById(gespeichertId).orElseThrow();
        assertThat(neugelesen.getName()).isEqualTo(TESTNAME);
        assertThat(neugelesen.getGeburtsdatum()).isEqualTo(TESTDATUML);
        assertThat(neugelesen.getAdresse()).isEqualTo(TESTORT);
        assertThat(neugelesen.getEmail()).isEqualTo(TESTEMAIL);
        assertThat(neugelesen.getLieblingsfarbe()).isEqualTo(TESTFARBE);
        assertThat(neugelesen.getInteressen()).isEqualTo(TESTINTERESSEN);
    }

}