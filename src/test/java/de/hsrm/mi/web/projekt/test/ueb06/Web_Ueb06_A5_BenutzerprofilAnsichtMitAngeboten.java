package de.hsrm.mi.web.projekt.test.ueb06;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.MockMvc;

import de.hsrm.mi.web.projekt.angebot.Angebot;
import de.hsrm.mi.web.projekt.angebot.AngebotRepository;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerProfil;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerprofilRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testable
public class Web_Ueb06_A5_BenutzerprofilAnsichtMitAngeboten {
    @Autowired
    private BenutzerprofilRepository benutzerprofilRepository;

    @Autowired
    private AngebotRepository angebotRepository;


    @Autowired
    private MockMvc mockmvc;

    @Test
    public void vorabcheck() {
        assertThat(BenutzerprofilRepository.class).isInterface();
        assertThat(benutzerprofilRepository).isNotNull();
    }

    private BenutzerProfil makeBenutzer(String name, int i) {
        BenutzerProfil benutzer = new BenutzerProfil();
        benutzer.setName(name);
        benutzer.setAdresse("Strasse des " + name + ", " + name + "stadt");
        benutzer.setEmail(name + "@" + name.toLowerCase() + "-online.de");
        benutzer.setGeburtsdatum(LocalDate.of(2000 + i, 1 + i % 12, 1 + (5 * i) % 28));
        benutzer.setLieblingsfarbe(String.format("#%02x%02x%02x", i, i + 5, i + 10));
        benutzer.setInteressen(i + " Runden reiten, schwimmen, " + name + " ruehmen");
        return benutzer;
    }

    private List<BenutzerProfil> initDB() throws IOException {
        benutzerprofilRepository.deleteAll();

        // BenutzerProfile (Entities) in DB bereitstellen
        List<BenutzerProfil> profilList = new ArrayList<>();
        int i = 1;
        for (var name : List.of("Eustachius", "Glogomir", "Marizzibel", "Wuselbert")) {
            i += 1;
            BenutzerProfil bp = makeBenutzer(name, i);
            for (var beschr: List.of("Mandli", "Schockchi", "Nussi", "Gutzeli")) {
                Angebot a = new Angebot();
                a.setAnbieter(bp);
                a.setAbholort("Bei "+name);
                a.setBeschreibung(beschr+" von "+name);
                a.setMindestpreis(beschr.hashCode() % 1000);
                bp.getAngebote().add(a);
            }
            // BenutzerProfile (Entities) in DB speichern
            BenutzerProfil entity = benutzerprofilRepository.save(bp);
            profilList.add(entity);
        }
        return profilList;
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
    @DisplayName("BenutzerprofilController: GET /benutzerprofil zeigt (auch) Angebote")
    void get_benutzerprofil_liste_angebote() throws Exception {
        var benutzerliste = initDB();
        var profil = benutzerliste.get(0);

        mockmvc.perform(
                get("/benutzerprofil").sessionAttr("profil", profil))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(profil.getName())))
                .andExpect(content().string(containsString(profil.getAdresse())))
                .andExpect(content().string(containsString(profil.getEmail())))
                .andExpect(content().string(containsString(profil.getAngebote().get(0).getBeschreibung())))
                .andExpect(content().string(containsString(profil.getAngebote().get(1).getBeschreibung())))
                .andExpect(content().string(containsString(profil.getAngebote().get(2).getBeschreibung())))
                .andExpect(content().string(containsString(profil.getAngebote().get(0).getMindestpreis()+"")))
                .andExpect(content().string(containsString(profil.getAngebote().get(1).getMindestpreis()+"")))
                .andExpect(content().string(containsString(profil.getAngebote().get(2).getMindestpreis()+"")))
                ;

    }
}