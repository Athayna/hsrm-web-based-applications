package de.hsrm.mi.web.projekt.test.ueb04;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerprofilController;

// https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/test/web/servlet/request/MockHttpServletRequestBuilder.html

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testable
class Web_Ueb04_A1_WebMvcValidierung {
	private final String TESTNAME = "Joghurta Biffel";
	private final String TESTORT = "In der Ecke 17, 99441 Vollradisroda";
	private final String TESTDATUM = "2021-07-17";
	private final String TESTEMAIL = "joghurta.biffel@vollradisroda.be";
	private final String TESTFARBE = "#4b8DeF";

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
	@DisplayName("POST /benutzerprofil/bearbeiten mit Validierung: Eintrag ok")
	void benutzerprofil_bearbeiten_post_validate_eintrag_ok() throws Exception {
		mockmvc.perform(
				post("/benutzerprofil/bearbeiten")
						.param("name", TESTNAME)
						.param("geburtsdatum", TESTDATUM)
						.param("adresse", TESTORT)
						.param("email", TESTEMAIL)
						.param("lieblingsfarbe", TESTFARBE)
						.param("interessen", "hupfen, schlumpfen, schauen")
						.contentType(MediaType.MULTIPART_FORM_DATA))
				.andExpect(status().is3xxRedirection())
				.andExpect(header().string("Location", "/benutzerprofil"));
	}

	@Test
	@DisplayName("POST /benutzerprofil/bearbeiten mit Validierung: Name kurz ok")
	void benutzerprofil_bearbeiten_post_validate_name_kurz_ok() throws Exception {
		mockmvc.perform(
				post("/benutzerprofil/bearbeiten")
						.param("name", "Bla")
						.param("geburtsdatum", TESTDATUM)
						.param("adresse", TESTORT)
						.param("email", TESTEMAIL)
						.param("lieblingsfarbe", TESTFARBE)
						.param("interessen", "hupfen, schlumpfen, schauen")
						.contentType(MediaType.MULTIPART_FORM_DATA))
				.andExpect(status().is3xxRedirection())
				.andExpect(header().string("Location", "/benutzerprofil"));
	}

	@Test
	@DisplayName("POST /benutzerprofil/bearbeiten mit Validierung: Name zu kurz")
	void benutzerprofil_bearbeiten_post_validate_nake_kurz() throws Exception {
		mockmvc.perform(
				post("/benutzerprofil/bearbeiten")
						.param("name", "Yo")
						.param("geburtsdatum", TESTDATUM)
						.param("adresse", TESTORT)
						.param("email", TESTEMAIL)
						.param("lieblingsfarbe", TESTFARBE)
						.param("interessen", "hupfen, schlumpfen, schauen")
						.contentType(MediaType.MULTIPART_FORM_DATA))
				.andExpect(status().is2xxSuccessful())
				.andExpect(view().name("benutzerprofil/profileditor"))
				.andExpect(model().attributeHasFieldErrors("profil", "name"))
				.andExpect(content().string(containsString(TESTDATUM)))
				.andExpect(content().string(containsString(TESTORT)))
				.andExpect(content().string(containsString(TESTEMAIL)));

	}

	@Test
	@DisplayName("POST /benutzerprofil/bearbeiten mit Validierung: Datum in Zukunft, bleibt auf Bearbeitungsseite")
	void benutzerprofil_bearbeiten_post_validate_geburtsdatum_zukunft() throws Exception {
		final String TESTDATUM_ZUKUNFT = "2030-05-01";
		mockmvc.perform(
				post("/benutzerprofil/bearbeiten")
						.param("name", TESTNAME)
						.param("geburtsdatum", TESTDATUM_ZUKUNFT)
						.param("adresse", TESTORT)
						.param("email", TESTEMAIL)
						.param("lieblingsfarbe", TESTFARBE)
						.param("interessen", "hupfen, schlumpfen, schauen")
						.contentType(MediaType.MULTIPART_FORM_DATA))
				.andExpect(status().isOk())
				.andExpect(view().name("benutzerprofil/profileditor"))
				.andExpect(model().attributeHasFieldErrors("profil", "geburtsdatum"))
				.andExpect(content().string(containsString(TESTNAME)))
				.andExpect(content().string(containsString(TESTORT)))
				.andExpect(content().string(containsString(TESTEMAIL)));
	}

	@Test
	@DisplayName("POST /benutzerprofil/bearbeiten mit Validierung: Adresse null falsch")
	void benutzerprofil_bearbeiten_post_validate_adresse_null() throws Exception {
		final String FALSCHORT = null;
		mockmvc.perform(
				post("/benutzerprofil/bearbeiten")
						.param("name", TESTNAME)
						.param("geburtsdatum", TESTDATUM)
						.param("adresse", FALSCHORT)
						.param("email", TESTEMAIL)
						.param("lieblingsfarbe", TESTFARBE)
						.param("interessen", "hupfen, schlumpfen, schauen")
						.contentType(MediaType.MULTIPART_FORM_DATA))
				.andExpect(status().isOk())
				.andExpect(view().name("benutzerprofil/profileditor"))
				.andExpect(model().attributeHasFieldErrors("profil", "adresse"))
				.andExpect(content().string(containsString(TESTNAME)))
				.andExpect(content().string(containsString(TESTDATUM)))
				.andExpect(content().string(containsString(TESTEMAIL)));
	}
}
