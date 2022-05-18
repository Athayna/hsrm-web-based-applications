package de.hsrm.mi.web.projekt.test.ueb05;

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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.MockMvc;

import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerProfil;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerprofilRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testable
public class Web_Ueb05_A5_BenutzerprofilControllerListe {
    @Autowired
    private BenutzerprofilRepository benutzerprofilRepository;

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
            // BenutzerProfile (Entities) in DB speichern
            BenutzerProfil entity = benutzerprofilRepository.save(bp);
            profilList.add(entity);
        }
        return profilList;
    }

    @Test
    @Transactional
    @DisplayName("BenutzerprofilController: GET /benutzerprofil/liste zeigt BenutzerProfile aus DB")
    void get_benutzerprofil_liste() throws Exception {
        benutzerprofilRepository.deleteAll();

        var benutzerliste = initDB();

        mockmvc.perform(
                get("/benutzerprofil/liste"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(benutzerliste.get(0).getName())))
                .andExpect(content().string(containsString(benutzerliste.get(1).getName())))
                .andExpect(content().string(containsString(benutzerliste.get(2).getName())))
                .andExpect(content().string(containsString(benutzerliste.get(3).getName())))
                .andExpect(content().string(containsString(benutzerliste.get(0).getEmail())))
                .andExpect(content().string(containsString(benutzerliste.get(1).getEmail())))
                .andExpect(content().string(containsString(benutzerliste.get(2).getEmail())))
                .andExpect(content().string(containsString(benutzerliste.get(3).getEmail())))
                ;

    }
}