package de.hsrm.mi.web.projekt.test.ueb06;

import static org.assertj.core.api.Assertions.assertThat;

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
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerprofilService;


@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@Testable
public class Web_Ueb06_A2_BenutzerProfilStandortdaten {
    @Autowired
    private BenutzerprofilRepository benutzerprofilRepository;

    @Autowired
    private BenutzerprofilService benutzerprofilService;


    @Test
    public void vorabcheck() {
        assertThat(BenutzerprofilRepository.class).isInterface();
        assertThat(benutzerprofilRepository).isNotNull();
        assertThat(BenutzerprofilService.class).isInterface();
        assertThat(benutzerprofilService).isNotNull();
    }

    private BenutzerProfil makeBenutzerProfil(String name, int i) {
        BenutzerProfil benutzer = new BenutzerProfil();
        benutzer.setName(name);
        benutzer.setAdresse("Strasse des "+name+", "+name+"stadt");
        benutzer.setEmail(name+"@"+name.toLowerCase()+"-online.de");
        benutzer.setGeburtsdatum(LocalDate.of(2000+i, 1+i%12, 1+(5*i)%28));
        benutzer.setLieblingsfarbe(String.format("#%02x%02x%02x", i, i+5, i+10));
        benutzer.setInteressen(i+" Runden reiten, schwimmen, "+name+" ruehmen");
        return benutzer;
    }


    @Test
    @Transactional
    @DisplayName("BenutzerprofilService: speichereBenutzerProfil() setzt Geo-Koordinaten und toString() gibt sie aus")
    public void test_speichereBenutzerProfil()  {
        final BenutzerProfil glogo = makeBenutzerProfil("Glogomir", 17);
        glogo.setAdresse("Waldhörnlestraße, Tübingen");

        // Repository leeren
        benutzerprofilRepository.deleteAll();
        assertThat(benutzerprofilRepository.count()).isEqualTo(0);

        // BenutzerProfil in Repository speichern
        BenutzerProfil bp2 = benutzerprofilService.speichereBenutzerProfil(glogo);
        long gespeichertId = bp2.getId();

        // Repository müsste nun einen Eintrag enthalten (war vorher leer)
        assertThat(benutzerprofilRepository.count()).isEqualTo(1);

        // und wieder frisch aus DB auslesen
        BenutzerProfil neugelesen = benutzerprofilRepository.getById(gespeichertId);
        assertThat(neugelesen.getName()).isEqualTo(glogo.getName());
        assertThat(neugelesen.getGeburtsdatum()).isEqualTo(glogo.getGeburtsdatum());
        assertThat(neugelesen.getAdresse()).isEqualTo(glogo.getAdresse());
        assertThat(neugelesen.getEmail()).isEqualTo(glogo.getEmail());
        assertThat(neugelesen.getLieblingsfarbe()).isEqualTo(glogo.getLieblingsfarbe());
        assertThat(neugelesen.getInteressen()).isEqualTo(glogo.getInteressen());

        assertThat(neugelesen.getLat()).isBetween(48.49, 48.5);
        assertThat(neugelesen.getLon()).isBetween(9.05, 9.069);

    }


}