package de.hsrm.mi.web.projekt.test.ueb04;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Locale;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.MockMvc;

import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerprofilController;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testable
class Web_Ueb04_A3_I18n_und_L10n {
	@Autowired
	private MockMvc mockmvc;

	@Autowired
	private BenutzerprofilController benutzerprofilController;

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
	void vorabcheck() {
		assertThat(benutzerprofilController).isNotNull();
		assertThat(mockmvc).isNotNull();
	}

	@Test
	@DisplayName("GET /benutzerprofil mit Locale.GERMAN")
	void sichtung_liste_deutsch_locale_gesetzt() throws Exception {
		mockmvc.perform(
				get("/benutzerprofil")
						.locale(Locale.GERMAN) // entspricht: Browser-Voreinstellung setzt
												// "Accept-Language"-Requestheader
		).andExpect(status().isOk())
				.andExpect(content().string(containsStringIgnoringCase(NAME_DE)))
				.andExpect(content().string(containsStringIgnoringCase(GEBURTSDATUM_DE)))
				.andExpect(content().string(containsStringIgnoringCase(ADRESSE_DE)))
				.andExpect(content().string(containsStringIgnoringCase(EMAIL_DE)))
				.andExpect(content().string(containsStringIgnoringCase(LIEBLINGSFARBE_DE)));
	}

	@Test
	@DisplayName("GET /benutzerprofil mit Locale.ENGLISH")
	void sichtung_liste_englisch_locale_gesetzt() throws Exception {
		mockmvc.perform(
				get("/benutzerprofil")
						.locale(Locale.ENGLISH) // entspricht: Browser-Voreinstellung setzt
												// "Accept-Language"-Requestheader
		).andExpect(status().isOk())
				.andExpect(content().string(containsStringIgnoringCase(NAME_EN)))
				.andExpect(content().string(containsStringIgnoringCase(GEBURTSDATUM_EN)))
				.andExpect(content().string(containsStringIgnoringCase(ADRESSE_EN)))
				.andExpect(content().string(containsStringIgnoringCase(EMAIL_EN)))
				.andExpect(content().string(containsStringIgnoringCase(LIEBLINGSFARBE_EN)));
	}
}
