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
class Web_Ueb04_A2_BuntValidierung_WebMvc {
	private final String TESTNAME = "Joendhard Biffel";
	private final String TESTORT = "In der Ecke 17, 99441 Vollradisroda";
	private final String TESTDATUM = "2002-07-17";
	private final String TESTEMAIL = "joendhard@biffel.biz";
	private final String TESTFARBE_OK = "#4b8DeF";

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
	@DisplayName("POST /benutzerprofil/bearbeiten mit Validierung: Lieblingsfarbe ok")
	void benutzerprofil_bearbeiten_post_validate_lieblingsfarbe_ok() throws Exception {
		mockmvc.perform(
				post("/benutzerprofil/bearbeiten")
						.param("name", TESTNAME)
						.param("geburtsdatum", TESTDATUM)
						.param("adresse", TESTORT)
						.param("email", TESTEMAIL)
						.param("lieblingsfarbe", TESTFARBE_OK)
						.param("interessen", "hupfen, schlumpfen, schauen")
						.contentType(MediaType.MULTIPART_FORM_DATA))
				.andExpect(status().is3xxRedirection())
				.andExpect(header().string("Location", "/benutzerprofil"));
	}

	@Test
	@DisplayName("POST /benutzerprofil/bearbeiten mit Validierung: Lieblingsfarbe leer ok")
	void benutzerprofil_bearbeiten_post_validate_lieblingsfarbe_leer_ok() throws Exception {
		mockmvc.perform(
				post("/benutzerprofil/bearbeiten")
						.param("name", TESTNAME)
						.param("geburtsdatum", TESTDATUM)
						.param("adresse", TESTORT)
						.param("email", TESTEMAIL)
						.param("lieblingsfarbe", "")
						.param("interessen", "hupfen, schlumpfen, schauen")
						.contentType(MediaType.MULTIPART_FORM_DATA))
				.andExpect(status().is3xxRedirection())
				.andExpect(header().string("Location", "/benutzerprofil"));
	}

	@Test
	@DisplayName("POST /benutzerprofil/bearbeiten mit Validierung: Lieblingsfarbe falsch, bleibt auf Bearbeitungsseite")
	void benutzerprofil_bearbeiten_post_validate_lieblingsfarbe_falsch() throws Exception {
		mockmvc.perform(
				post("/benutzerprofil/bearbeiten")
						.param("name", TESTNAME)
						.param("geburtsdatum", TESTDATUM)
						.param("adresse", TESTORT)
						.param("email", TESTEMAIL)
						.param("lieblingsfarbe", "#axy")
						.param("interessen", "hupfen, schlumpfen, schauen")
						.contentType(MediaType.MULTIPART_FORM_DATA))
				.andExpect(status().isOk())
				.andExpect(view().name("benutzerprofil/profileditor"))
				.andExpect(model().attributeHasFieldErrors("profil", "lieblingsfarbe"))
				.andExpect(content().string(containsString(TESTNAME)))
				.andExpect(content().string(containsString(TESTORT)))
				.andExpect(content().string(containsString(TESTEMAIL)));
	}
}
