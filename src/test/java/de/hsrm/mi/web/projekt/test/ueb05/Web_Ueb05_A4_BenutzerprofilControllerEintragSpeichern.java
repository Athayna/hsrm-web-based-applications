package de.hsrm.mi.web.projekt.test.ueb05;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerProfil;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerprofilRepository;


@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testable
public class Web_Ueb05_A4_BenutzerprofilControllerEintragSpeichern {
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
        benutzer.setAdresse("Platz des "+name+", "+name+"burg");
        benutzer.setEmail(name+"@"+name.toLowerCase()+"-offline.de");
        benutzer.setGeburtsdatum(LocalDate.of(1990+i, 1+i%12, 1+(3*i)%28));
        benutzer.setLieblingsfarbe(String.format("#%02x%02x%02x", i, i+5, i+10));
        benutzer.setInteressen(i+"x Runden rudern, hopsen, "+name+" ruehmen");
        return benutzer;
    }


    @Test
	@DisplayName("BenutzerprofilController: POST /benutzerprofil/bearbeiten legt DB-User an")
	void benutzerprofil_post_legt_profil_in_db_an() throws Exception {
        var ben = makeBenutzer("Jockel", 3);
		mockmvc.perform(
				post("/benutzerprofil/bearbeiten")
						.param("name", ben.getName())
						.param("geburtsdatum", ben.getGeburtsdatum().toString())
						.param("adresse", ben.getAdresse())
						.param("email", ben.getEmail())
						.param("lieblingsfarbe", ben.getLieblingsfarbe())
						.param("interessen", ben.getInteressen())
						.contentType(MediaType.MULTIPART_FORM_DATA))
				.andExpect(status().is3xxRedirection())
				.andExpect(header().string("Location", "/benutzerprofil"));

        // DB vorher leer, ID sollte daher 1 sein
        assertThat(benutzerprofilRepository.count()).isEqualTo(1L);

        BenutzerProfil neugelesen = benutzerprofilRepository.findAll().get(0);
        
        assertThat(neugelesen.getName()).isEqualTo(ben.getName());
        assertThat(neugelesen.getGeburtsdatum()).isEqualTo(ben.getGeburtsdatum());
        assertThat(neugelesen.getAdresse()).isEqualTo(ben.getAdresse());
        assertThat(neugelesen.getEmail()).isEqualTo(ben.getEmail());
        assertThat(neugelesen.getLieblingsfarbe()).isEqualTo(ben.getLieblingsfarbe());
        assertThat(neugelesen.getInteressen()).isEqualTo(ben.getInteressen());
	}

}