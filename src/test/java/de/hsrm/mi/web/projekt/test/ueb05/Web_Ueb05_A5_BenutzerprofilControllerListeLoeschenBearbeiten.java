package de.hsrm.mi.web.projekt.test.ueb05;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerProfil;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerprofilRepository;


@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testable
public class Web_Ueb05_A5_BenutzerprofilControllerListeLoeschenBearbeiten {
    Logger logger = LoggerFactory.getLogger(Web_Ueb05_A5_BenutzerprofilControllerListeLoeschenBearbeiten.class);
    
    public final static String OP_LOESCHEN = "loeschen";
    public final static String OP_BEARBEITEN = "bearbeiten";

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
        benutzer.setAdresse("Strasse des "+name+", "+name+"stadt");
        benutzer.setEmail(name+"@"+name.toLowerCase()+"-online.de");
        benutzer.setGeburtsdatum(LocalDate.of(1990+i, 1+i%12, 1+(6*i)%28));
        benutzer.setLieblingsfarbe(String.format("#%02x%02x%02x", i, i+5, i+10));
        benutzer.setInteressen(i+" Runden reiten, jockeln, "+name+" ruehmen");
        return benutzer;
    }

    private List<BenutzerProfil> initDB() throws IOException {
        benutzerprofilRepository.deleteAll();

        // BenutzerProfil(-Entity) in DB bereitstellen
        List<BenutzerProfil> profilList = new ArrayList<>();
        int i=1;
        for (var name : List.of("Hoeboet", "Erdmute", "Frits", "Frans", "Notulen")) {
            i+=1;
            BenutzerProfil bp = makeBenutzer(name, i);
            // BenutzerProfil(-Entity) in DB speichern
            BenutzerProfil entity = benutzerprofilRepository.save(bp);
            profilList.add(entity);
        }
        return profilList;
    }

    @Test
    @Transactional
	@DisplayName("BenutzerprofilController: GET /benutzerprofil/liste?op=loeschen&id=... loescht DB-User")
	void benutzerprofilcontroller_get_db_loesche() throws Exception {
        var benliste = initDB();
        var count = benliste.size();

        for (var b: benliste) {
            logger.info("benutzerprofil_db_loesche löscht {}", b);
            mockmvc.perform(
                get("/benutzerprofil/liste")
                .queryParam("op", OP_LOESCHEN)
                .queryParam("id", String.valueOf(b.getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/benutzerprofil/liste"));
                ;
            count--;
            assertThat(benutzerprofilRepository.count()).isEqualTo(count);
            assertThat(benutzerprofilRepository.findById(b.getId())).isEmpty();
        }
	}


    @Test
    @Transactional
	@DisplayName("BenutzerprofilController: GET /benutzerprofil/liste?op=bearbeiten&id=... lädt DB-User in Bearbeiten-Seite")
	void benutzerprofilcontroller_get_db_bearbeite() throws Exception {
        var benliste = initDB();
        var count = benliste.size();
        var chosenOne = benliste.get(count/2);

        MvcResult result = mockmvc.perform(
            get("/benutzerprofil/liste")
            .queryParam("op", OP_BEARBEITEN)
            .queryParam("id", String.valueOf(chosenOne.getId())))
            .andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", "/benutzerprofil/bearbeiten"))
            .andExpect(request().sessionAttribute("profil", isA(BenutzerProfil.class)))
            .andReturn()
            ;

            // Model-/Session-Attribut "profil" auslesen
            var obj = result.getRequest().getSession().getAttribute("profil");
            assertThat(obj).isNotNull();
    
            // Gucken, ob alles in's 'profil' übernommen wurde
            BenutzerProfil profil = (BenutzerProfil)obj;
            assertThat(profil.getName()).isEqualTo(chosenOne.getName());
            assertThat(profil.getAdresse()).isEqualTo(chosenOne.getAdresse());
            assertThat(profil.getGeburtsdatum()).isEqualTo(chosenOne.getGeburtsdatum());
            assertThat(profil.getEmail()).isEqualTo(chosenOne.getEmail());
            assertThat(profil.getLieblingsfarbe()).isEqualTo(chosenOne.getLieblingsfarbe());
            assertThat(profil.getInteressen()).isEqualTo(chosenOne.getInteressen());
	}


}