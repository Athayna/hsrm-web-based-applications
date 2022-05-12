package de.hsrm.mi.web.projekt.test.ueb03;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerProfil;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerprofilController;


@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testable
class Web_Ueb03_A5_BenutzerProfilBearbeiten {
    private final String TESTNAME = "Joghurta";
    private final String TESTADRESSE = "Waldstrasse 17, 99441 Vollradisroda";
    private final String TESTISODATUM = "2017-07-17";
	private final LocalDate TESTDATUM = LocalDate.of(2017, 7, 17);
    private final String TESTEMAIL = "joghi@mi.hs-rm.de";
    private final String TESTLIEBLINGSFARBE = "#111222";
    private final String TESTINTERESSEN = "weit hoppen, fern sehen  ,  Topflappen haekeln";
    
    BenutzerProfil benutzerprofil = null;

    @BeforeEach
    public void benutzerprofil_init() {
        benutzerprofil = new BenutzerProfil();
        benutzerprofil.setName(TESTNAME);
        benutzerprofil.setAdresse(TESTADRESSE);
        benutzerprofil.setGeburtsdatum(TESTDATUM);
        benutzerprofil.setEmail(TESTEMAIL);
        benutzerprofil.setLieblingsfarbe(TESTLIEBLINGSFARBE);
        benutzerprofil.setInteressen(TESTINTERESSEN);
    }

	@Autowired
	private MockMvc mockmvc;
	
	@Autowired
	private BenutzerprofilController benutzerprofilController;

	@Test
	void vorabcheck() {
		assertThat(benutzerprofilController).isNotNull();
		assertThat(mockmvc).isNotNull();
	}

	@Test
	@DisplayName("GET /benutzerprofil/bearbeiten liefert HTML-Seite")
	void benutzerprofil_neu_get() throws Exception {
		mockmvc.perform(
			get("/benutzerprofil/bearbeiten")
			.contentType("text/html")
		).andExpect(status().isOk());

	}
	
	@Test
	@DisplayName("POST /benutzerprofil/bearbeiten mit Formulardaten füllt BenutzerProfil-Attribute und redirectet zu /benutzerprofil")
	void benutzerprofil_neu_post() throws Exception {
		// Sessionattribut vorbereiten
		Map<String, Object> sessiondata = new HashMap<>();
		sessiondata.put("profil", new BenutzerProfil());

		// Eintrag per HTTP POST anlegen
		MvcResult result = mockmvc.perform(
			post("/benutzerprofil/bearbeiten")
			.param("name", TESTNAME)
			.param("adresse", TESTADRESSE)
			.param("geburtsdatum", TESTISODATUM)
			.param("email", TESTEMAIL)
			.param("lieblingsfarbe", TESTLIEBLINGSFARBE)
			.param("interessen", TESTINTERESSEN)
			.contentType(MediaType.MULTIPART_FORM_DATA)
			.sessionAttrs(sessiondata)
		).andExpect(status().is3xxRedirection())
		.andExpect(request().sessionAttribute("profil", isA(BenutzerProfil.class)))
		.andExpect(header().string("Location", "/benutzerprofil"))
		.andReturn();

		var obj = result.getRequest().getSession().getAttribute("profil");
		assertThat(obj).isNotNull();

		// Gucken, ob alles übernommen wurde
		BenutzerProfil profil = (BenutzerProfil)obj;
		assertThat(profil.getName()).isEqualTo(TESTNAME);
		assertThat(profil.getAdresse()).isEqualTo(TESTADRESSE);
		assertThat(profil.getGeburtsdatum()).isEqualTo(TESTISODATUM);
		assertThat(profil.getEmail()).isEqualTo(TESTEMAIL);
		assertThat(profil.getLieblingsfarbe()).isEqualTo(TESTLIEBLINGSFARBE);
		assertThat(profil.getInteressen()).isEqualTo(TESTINTERESSEN);
	}
}
