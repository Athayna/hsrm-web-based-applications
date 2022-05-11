package de.hsrm.mi.web.projekt.test.ueb04;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Locale;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.MockMvc;

import de.hsrm.mi.web.projekt.benutzerprofil.*;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class Web_Ueb04_A4_I18n_und_L10n_Sprach_Umschalter {
	@Autowired
	private MockMvc mockmvc;
	
	@Autowired
	private BenutzerprofilController benutzerprofilController;

	@Test
	void vorabcheck() {
		assertThat(benutzerprofilController).isNotNull();
		assertThat(mockmvc).isNotNull();
	}

	static final String NAME_DE = "Name";
	static final String GEBURTSDATUM_DE = "Geburtsdatum";
	static final String ADRESSE_DE = "Anschrift";
	static final String EMAIL_DE = "Digitale Postanschrift";
	static final String LIEBLINGSFARBE_DE = "Lieblingsfarbe";
	static final String BEARBEITEN_DE = "bearbeiten";
	static final String SPEICHERN_DE = "abspeichern";
	static final String ABBRECHEN_DE = "abbrechen";

	static final String NAME_EN = "Name";
	static final String GEBURTSDATUM_EN = "Date of birth";
	static final String ADRESSE_EN = "Street address";
	static final String EMAIL_EN = "EMail address";
	static final String LIEBLINGSFARBE_EN = "Favourite colour";
	static final String BEARBEITEN_EN = "edit";
	static final String SPEICHERN_EN = "save";
	static final String ABBRECHEN_EN = "cancel";


	@Test
	@DisplayName("GET /benutzerprofil?sprache=de Sprache setzen per Query-Parameter")
	void benutzerprofil_deutsch_query_param_de() throws Exception {
		mockmvc.perform(
			get("/benutzerprofil")
			.param("sprache","de")  // setze Sprache per Query-Parameter ...?sprache=de
		).andExpect(status().isOk())
		.andExpect(content().string(containsStringIgnoringCase(NAME_DE)))
		.andExpect(content().string(containsStringIgnoringCase(GEBURTSDATUM_DE)))
		.andExpect(content().string(containsStringIgnoringCase(ADRESSE_DE)))
		.andExpect(content().string(containsStringIgnoringCase(EMAIL_DE)))
		.andExpect(content().string(containsStringIgnoringCase(LIEBLINGSFARBE_DE)))
		.andExpect(content().string(containsStringIgnoringCase(BEARBEITEN_DE)))
		;
	}


	@Test
	@DisplayName("GET /benutzerprofil?sprache=en Sprache setzen per Query-Parameter")
	void benutzerprofil_englisch() throws Exception {
		mockmvc.perform(
			get("/benutzerprofil")
			.param("sprache","en")  // setze Sprache in per Query-Parameter ...?sprache=en
		).andExpect(status().isOk())
		.andExpect(content().string(containsStringIgnoringCase(NAME_EN)))
		.andExpect(content().string(containsStringIgnoringCase(GEBURTSDATUM_EN)))
		.andExpect(content().string(containsStringIgnoringCase(ADRESSE_EN)))
		.andExpect(content().string(containsStringIgnoringCase(EMAIL_EN)))
		.andExpect(content().string(containsStringIgnoringCase(LIEBLINGSFARBE_EN)))
		.andExpect(content().string(containsStringIgnoringCase(BEARBEITEN_EN)))
		;
	}


	@Test
	@DisplayName("GET /benutzerprofil?sprache=en hat Vorrang vor Accept-Language Browsereinstellung")
	void benutzerprofil_browser_deutsch_queryparam_englisch() throws Exception {
		mockmvc.perform(
			get("/benutzerprofil")
			.locale(Locale.GERMAN)  // simuliert Accept-Header-Setzung aus Browsereinstellung
			.param("sprache","en")  // setze abweichende Sprache in per Query-Parameter ...?sprache=en
		).andExpect(status().isOk())
		.andExpect(content().string(containsStringIgnoringCase(NAME_EN)))
		.andExpect(content().string(containsStringIgnoringCase(GEBURTSDATUM_EN)))
		.andExpect(content().string(containsStringIgnoringCase(ADRESSE_EN)))
		.andExpect(content().string(containsStringIgnoringCase(EMAIL_EN)))
		.andExpect(content().string(containsStringIgnoringCase(LIEBLINGSFARBE_EN)))
		.andExpect(content().string(containsStringIgnoringCase(BEARBEITEN_EN)))
		;
	}

	@Test
	@DisplayName("GET /benutzerprofil?sprache=de hat Vorrang vor Accept-Language Browsereinstellung")
	void benutzerprofil_browser_englisch_queryparam_deutsch() throws Exception {
		mockmvc.perform(
			get("/benutzerprofil")
			.locale(Locale.ENGLISH)  // simuliert Accept-Header-Setzung aus Browsereinstellung
			.param("sprache","de")   // setze abweichende Sprache in per Query-Parameter ...?sprache=en
		).andExpect(status().isOk())
		.andExpect(content().string(containsStringIgnoringCase(NAME_DE)))
		.andExpect(content().string(containsStringIgnoringCase(GEBURTSDATUM_DE)))
		.andExpect(content().string(containsStringIgnoringCase(ADRESSE_DE)))
		.andExpect(content().string(containsStringIgnoringCase(EMAIL_DE)))
		.andExpect(content().string(containsStringIgnoringCase(LIEBLINGSFARBE_DE)))
		.andExpect(content().string(containsStringIgnoringCase(BEARBEITEN_DE)))
		;
	}


	@Test
	@DisplayName("GET /benutzerprofil?sprache=en Sprache setzen mit Query-Parameter, dann Seite wechseln")
	void benutzerprofil_englisch_dann_bearbeiten() throws Exception {
		mockmvc.perform(
			get("/benutzerprofil")
			.locale(Locale.GERMAN)  // simuliert Accept-Header-Setzung aus Browsereinstellung			
			.param("sprache","en")  // setze Sprache in per Query-Parameter ...?sprache=de
		).andExpect(status().isOk())
		.andExpect(content().string(containsStringIgnoringCase(NAME_EN)))
		.andExpect(content().string(containsStringIgnoringCase(GEBURTSDATUM_EN)))
		.andExpect(content().string(containsStringIgnoringCase(ADRESSE_EN)))
		.andExpect(content().string(containsStringIgnoringCase(LIEBLINGSFARBE_EN)))
		.andExpect(content().string(containsStringIgnoringCase(BEARBEITEN_EN)))
		;
		/*
		 * Wechsel von Übersichtslisten- zu Neuerfassungs-Seite; Spracheinstellung von
		 * Übersichtslisten-Seite sollte erhalten bleiben, da (von Spring) in Session mitgeführt
		 */
		mockmvc.perform(
			get("/benutzerprofil/bearbeiten")
		).andExpect(status().isOk())
		.andExpect(content().string(containsStringIgnoringCase(NAME_EN)))
		.andExpect(content().string(containsStringIgnoringCase(GEBURTSDATUM_EN)))
		.andExpect(content().string(containsStringIgnoringCase(ADRESSE_EN)))
		.andExpect(content().string(containsStringIgnoringCase(LIEBLINGSFARBE_EN)))
		.andExpect(content().string(containsStringIgnoringCase(SPEICHERN_EN)))
		.andExpect(content().string(containsStringIgnoringCase(ABBRECHEN_EN)))
		;
	}

}
