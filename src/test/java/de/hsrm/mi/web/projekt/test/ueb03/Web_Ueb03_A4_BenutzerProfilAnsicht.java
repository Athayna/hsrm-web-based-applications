package de.hsrm.mi.web.projekt.test.ueb03;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.MockMvc;

import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerProfil;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerprofilController;

// https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/test/web/servlet/request/MockHttpServletRequestBuilder.html

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testable
class Web_Ueb03_A4_BenutzerProfilAnsicht {
    private final String TESTNAME = "Friedfert";
    private final String TESTADRESSE = "Waldstrasse 17, 99441 Vollradisroda";
	private final LocalDate TESTDATUM = LocalDate.of(2017, 1, 17);
    private final String TESTEMAIL = "friedfert@mi.hs-rm.de";
    private final String TESTLIEBLINGSFARBE = "#171717";
    private final String TESTINTERESSEN = "sehr weit hupfen, ausserordentlich fern sehen  ,  Topflappen erzeugen";
    private final List<String> TESTINTERESSEN_L = List.of("sehr weit hupfen", "ausserordentlich fern sehen", "Topflappen erzeugen");
    
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
	@DisplayName("GET /benutzerprofil vorhanden und liefert HTML")
	void benutzerprofil_get() throws Exception {
		mockmvc.perform(
			get("/benutzerprofil")
			.contentType("text/html")
		).andExpect(status().isOk());
	}

	@Test
	@DisplayName("GET /benutzerprofil legt 'profil' Sessionattribut mit BenutzerProfil an")
	void benutzerprofil_get_sessionattr() throws Exception {
		mockmvc.perform(
			get("/benutzerprofil")
			.contentType("text/html")
		).andExpect(status().isOk())
		.andExpect(request().sessionAttribute("profil", isA(BenutzerProfil.class)));
	}

	@Test
	@DisplayName("GET /benutzerprofil mit belegtem Session-Attribut 'profil' dessen Inhalte auch an")
	void benutzerprofil_get_sessionattr_belegt() throws Exception {
		// Sessionattribut vorbereiten
		Map<String, Object> sessiondata = new HashMap<>();
		sessiondata.put("profil", benutzerprofil);

		// und gucken, ob Daten in der /benutzerprofil-Ausgabe auftauchen
		mockmvc.perform(
			get("/benutzerprofil")
			.sessionAttrs(sessiondata)
		)
		.andExpect(content().string(containsString(TESTNAME)))
		.andExpect(content().string(containsString(TESTADRESSE)))
		.andExpect(content().string(containsString(TESTEMAIL)))
		.andExpect(content().string(containsString(TESTADRESSE)))
		.andExpect(content().string(containsString(TESTINTERESSEN_L.get(0))))
		.andExpect(content().string(containsString(TESTINTERESSEN_L.get(1))))
		.andExpect(content().string(containsString(TESTINTERESSEN_L.get(2))));
	}
}
