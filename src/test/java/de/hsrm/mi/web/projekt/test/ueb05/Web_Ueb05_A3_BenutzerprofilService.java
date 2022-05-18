package de.hsrm.mi.web.projekt.test.ueb05;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
public class Web_Ueb05_A3_BenutzerprofilService {
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

    private List<BenutzerProfil> initDB()  {
        benutzerprofilRepository.deleteAll();

        // DB-Einträge bereitstellen
        List<BenutzerProfil> profilList = new ArrayList<>();
        int i=1;
        for (var name : List.of("Joghurta", "Joendhard", "Friedfert", "Marizzibel", "Trubert")) {
            i+=1;
            BenutzerProfil bp = makeBenutzerProfil(name, i);
            // in DB speichern
            BenutzerProfil entity = benutzerprofilRepository.save(bp);
            profilList.add(entity);
        }
        return profilList;
    }


    @Test
    @Transactional
    @DisplayName("BenutzerprofilService: speichereBenutzerProfil()")
    public void test_speichereBenutzerProfil()  {
        final BenutzerProfil glogo = makeBenutzerProfil("Glogomir", 17);

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
    }


    @Test
    @DisplayName("BenutzerprofilService: holeBenutzerProfilMitId()")
    @Transactional
    public void test_holeBenutzerProfilMitId() throws Exception {
        List<BenutzerProfil> profilliste = initDB();

        for (var profil: profilliste) {
            // alle eingespeicherten BenutzerProfile müssen per Service auffindbar sein
            var gesuchteId = profil.getId();
            BenutzerProfil ausdb = benutzerprofilService.holeBenutzerProfilMitId(gesuchteId).orElseThrow();

            assertThat(ausdb.getName()).isEqualTo(profil.getName());
            assertThat(ausdb.getGeburtsdatum()).isEqualTo(profil.getGeburtsdatum());
            assertThat(ausdb.getAdresse()).isEqualTo(profil.getAdresse());
            assertThat(ausdb.getEmail()).isEqualTo(profil.getEmail());
            assertThat(ausdb.getLieblingsfarbe()).isEqualTo(profil.getLieblingsfarbe());
            assertThat(ausdb.getInteressen()).isEqualTo(profil.getInteressen());
            }
        assertThat(benutzerprofilRepository.count()).isEqualTo(profilliste.size());
    }


    @Test
    @DisplayName("BenutzerprofilService: alleBenutzerProfile() sollte alle Benutzerprofile nach Namen sortiert liefern")
    @Transactional
    public void test_alleBenutzerProfile() throws Exception {
        List<BenutzerProfil> profilListe = initDB();

        // Liste nach Namen sortieren
        profilListe.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));

        // gucken, ob Service die BenutzerProfile in gleicher Reihenfolge liefert
        Iterator<BenutzerProfil> iter = profilListe.iterator();
        for (var ausdb: benutzerprofilService.alleBenutzerProfile()) {
            BenutzerProfil ausliste = iter.next();

            assertThat(ausdb.getName()).isEqualTo(ausliste.getName());
            assertThat(ausdb.getGeburtsdatum()).isEqualTo(ausliste.getGeburtsdatum());
            assertThat(ausdb.getAdresse()).isEqualTo(ausliste.getAdresse());
            assertThat(ausdb.getEmail()).isEqualTo(ausliste.getEmail());
            assertThat(ausdb.getLieblingsfarbe()).isEqualTo(ausliste.getLieblingsfarbe());
            assertThat(ausdb.getInteressen()).isEqualTo(ausliste.getInteressen());
        }

    }


    @Test
    @DisplayName("BenutzerprofilService: loescheBenutzerProfilMitId")
    @Transactional
    public void test_loescheBenutzerProfilMitId() throws Exception {
        List<BenutzerProfil> profilListe = initDB();

        // BenutzerProfile nach IDs nacheinander aus DB löschen
        for (var profil: profilListe) {
            benutzerprofilService.loescheBenutzerProfilMitId(profil.getId());
            // BenutzerProfil mit gelöschter id darf nicht mehr im Repository sein
            assertThat(benutzerprofilRepository.findById(profil.getId()).isPresent()).isFalse();
        }
        // Zum Schluss darf kein BenutzerProfil(-Entity) mehr übrig sein
        assertThat(benutzerprofilRepository.count()).isZero();
    }

}