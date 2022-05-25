package de.hsrm.mi.web.projekt.test.ueb06;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.transaction.Transactional;

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

import de.hsrm.mi.web.projekt.angebot.AngebotRepository;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerProfil;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerprofilController;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerprofilRepository;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerprofilService;


@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testable
class Web_Ueb06_A5_AngebotEingeben {
    private final String TESTBESCHREIBUNG = "Das ist eine wohlleuke Sache";
    private final long TESTPREIS = 1234;
	private final LocalDateTime TESTABGEBOTSENDE = LocalDateTime.of(2030,7,17, 10,15,0);
    private final String TESTABHOLORT = "Poststr 1, Taucha";
    
    BenutzerProfil benutzerprofil = null;

    private BenutzerProfil makeBenutzerProfil(String name, int i, String adresse) {
        BenutzerProfil benutzer = new BenutzerProfil();
        benutzer.setName(name);
        benutzer.setAdresse(adresse);
        benutzer.setEmail(name+"@"+name.toLowerCase()+"-online.de");
        benutzer.setGeburtsdatum(LocalDate.of(2000+i, 1+i%12, 1+(5*i)%28));
        benutzer.setLieblingsfarbe(String.format("#%02x%02x%02x", i, i+5, i+10));
        benutzer.setInteressen(i+" Runden reiten, schwimmen, "+name+" ruehmen");
        return benutzer;
    }

	@Autowired
	private MockMvc mockmvc;
	
	@Autowired
	private BenutzerprofilController benutzerprofilController;

	@Autowired
	private BenutzerprofilService benutzerprofilService;

	@Autowired
	private BenutzerprofilRepository benutzerprofilRepository;

	@Autowired
	private AngebotRepository angebotRepository;

	@BeforeEach
	@Transactional
	public void cleandb() {
		benutzerprofilRepository.deleteAll();
		angebotRepository.deleteAll();
	}


	@Test
	void vorabcheck() {
		assertThat(benutzerprofilController).isNotNull();
		assertThat(benutzerprofilService).isNotNull();
		assertThat(mockmvc).isNotNull();
	}

	@Test
	@DisplayName("GET /benutzerprofil/angebot liefert HTML-Seite")
	void benutzerprofil_angebot_get() throws Exception {
		mockmvc.perform(
			get("/benutzerprofil/angebot")
			.contentType("text/html")
		).andExpect(status().isOk());

	}
	
	@Test
	@DisplayName("POST /benutzerprofil/angebot mit Formulardaten füllt Angebot und redirectet zu /benutzerprofil")
	@Transactional
	void benutzerprofil_angebot_post() throws Exception {

		var benutzerprofil = makeBenutzerProfil("Willibrord", 5, "Am Hepper, Husten");
		var savedBenutzerProfil = benutzerprofilService.speichereBenutzerProfil(benutzerprofil);
		var savedBenutzerProfilId = savedBenutzerProfil.getId();

		// Eintrag per HTTP POST anlegen
		mockmvc.perform(
			post("/benutzerprofil/angebot")
			.sessionAttr("profil", savedBenutzerProfil)
			.param("beschreibung", TESTBESCHREIBUNG)
			.param("mindestpreis", TESTPREIS+"")
			.param("abholort", TESTABHOLORT)
			.param("ablaufzeitpunkt", TESTABGEBOTSENDE.toString())
			.contentType(MediaType.MULTIPART_FORM_DATA)
		).andExpect(status().is3xxRedirection())
		.andExpect(header().string("Location", "/benutzerprofil"))
		;

		var dbprofil = benutzerprofilRepository.findById(savedBenutzerProfilId).orElseThrow();
		assertThat(dbprofil.getAngebote()).isNotNull();
		assertThat(dbprofil.getAngebote()).size().isOne();

		var angebot1 = dbprofil.getAngebote().get(0);
		assertThat(angebot1.getBeschreibung()).isEqualTo(TESTBESCHREIBUNG);
		assertThat(angebot1.getMindestpreis()).isEqualTo(TESTPREIS);
		assertThat(angebot1.getAbholort()).isEqualTo(TESTABHOLORT);
		assertThat(angebot1.getAblaufzeitpunkt()).isEqualTo(TESTABGEBOTSENDE);

		assertThat(angebot1.getAnbieter().getId()).isEqualTo(savedBenutzerProfilId);
	}
}
