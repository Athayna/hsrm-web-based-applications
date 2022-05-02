package de.hsrm.mi.web.projekt.test.ueb03;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerProfil;

@Testable
public class Web_Ueb03_A2_BenutzerProfilInteressenListe {
    private final String TESTNAME = "Jöndhard";
    private final String TESTORT = "Waldstraße 17, 99441 Vollradisroda";
    private final LocalDate TESTDATUM = LocalDate.now();
    private final String TESTEMAIL = "joendhard@mi.hs-rm.de";
    private final String TESTLIEBLINGSFARBE = "#171717";
    private final String TESTINTERESSEN_OHNE_SPACES = "hüpfen,gucken,Briefmarken sammeln";
    private final List<String> TESTINTERESSEN1_OHNE_SPACES_L = List.of("hüpfen", "gucken", "Briefmarken sammeln");
    private final String TESTINTERESSEN_MIT_SPACES = "weit hüpfen, fern sehen  ,  Topflappen häkeln";
    private final List<String> TESTINTERESSEN2_MIT_SPACES_L = List.of("weit hüpfen", "fern sehen", "Topflappen häkeln");
    
    BenutzerProfil benutzerprofil = null;

    @BeforeEach
    public void benutzerprofil_init() {
        benutzerprofil = new BenutzerProfil();
        benutzerprofil.setName(TESTNAME);
        benutzerprofil.setAdresse(TESTORT);
        benutzerprofil.setGeburtsdatum(TESTDATUM);
        benutzerprofil.setEmail(TESTEMAIL);
        benutzerprofil.setLieblingsfarbe(TESTLIEBLINGSFARBE);
        benutzerprofil.setInteressen(TESTINTERESSEN_OHNE_SPACES);
    }

    @Test
    @DisplayName("BenutzerProfil: getInteressenListe() liefert bei leerem interessen-String leere Liste")
    public void benutzerprofil_keineinteressen_getinteressenliste() {
        benutzerprofil.setInteressen("");
        assertThat(benutzerprofil.getInteressenListe().size()).isEqualTo(0);
    }


    @Test
    @DisplayName("BenutzerProfil: getInteressenListe() liefert bei nicht-leerem interessen-String Liste der Interessen")
    public void benutzerprofil_getinteressenliste() {
        assertThat(benutzerprofil.getInteressenListe()).containsExactlyElementsOf(TESTINTERESSEN1_OHNE_SPACES_L);
    }

    @Test
    @DisplayName("BenutzerProfil: getInteressenListe() entfernt Leerzeichen rechts/links von Einzelinteressen")
    public void benutzerprofil_getinteressenliste_trimmed() {
        benutzerprofil.setInteressen(TESTINTERESSEN_MIT_SPACES);
        assertThat(benutzerprofil.getInteressenListe()).containsExactlyElementsOf(TESTINTERESSEN2_MIT_SPACES_L);
    }

}