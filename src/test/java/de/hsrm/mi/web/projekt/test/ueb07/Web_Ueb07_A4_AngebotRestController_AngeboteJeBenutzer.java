package de.hsrm.mi.web.projekt.test.ueb07;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.MockMvc;

import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerprofilRepository;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class Web_Ueb07_A4_AngebotRestController_AngeboteJeBenutzer {
    @Autowired
    private MockMvc mockmvc;

    @Autowired
    Ueb07_init ueb07_init;

    @Test
    public void vorabcheck() {
        assertThat(BenutzerprofilRepository.class).isInterface();
    }

    @BeforeEach
    @Transactional
    public void cleanDB()  {
        ueb07_init.cleanBenutzerAngebotDB();
    }


    @Test
    @DisplayName("GebotRestController - GET /api/gebot")
    @Transactional
    public void get_api_gebot() throws Exception {
        var benutzerlst = ueb07_init.initDB();

        var benutzer0 = benutzerlst.get(0);
        var benutzerangebote = benutzer0.getAngebote();

        mockmvc.perform(
            get("/api/benutzer/"+benutzer0.getId()+"/angebot")
            .contentType("application/json")
        ).andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$", hasSize((int)benutzerangebote.size())))

        .andExpect(jsonPath("$[0].beschreibung", is(benutzerangebote.get(0).getBeschreibung())))
        .andExpect(jsonPath("$[1].beschreibung", is(benutzerangebote.get(1).getBeschreibung())))
        .andExpect(jsonPath("$[2].beschreibung", is(benutzerangebote.get(2).getBeschreibung())))

        .andExpect(jsonPath("$[0].anbieterid", is(benutzerangebote.get(0).getAnbieter().getId()), Long.class))
        .andExpect(jsonPath("$[1].anbieterid", is(benutzerangebote.get(1).getAnbieter().getId()), Long.class))
        .andExpect(jsonPath("$[2].anbieterid", is(benutzerangebote.get(2).getAnbieter().getId()), Long.class))

        .andExpect(jsonPath("$[0].anbietername", is(benutzerangebote.get(0).getAnbieter().getName())))
        .andExpect(jsonPath("$[1].anbietername", is(benutzerangebote.get(1).getAnbieter().getName())))
        .andExpect(jsonPath("$[2].anbietername", is(benutzerangebote.get(2).getAnbieter().getName())))

        .andExpect(jsonPath("$[0].mindestpreis", is(benutzerangebote.get(0).getMindestpreis()), Long.class))
        .andExpect(jsonPath("$[1].mindestpreis", is(benutzerangebote.get(1).getMindestpreis()), Long.class))
        .andExpect(jsonPath("$[2].mindestpreis", is(benutzerangebote.get(2).getMindestpreis()), Long.class))

        .andExpect(jsonPath("$[0].gebote", is(0L), Long.class))
        .andExpect(jsonPath("$[1].gebote", is(0L), Long.class))
        .andExpect(jsonPath("$[2].gebote", is(0L), Long.class))

        .andExpect(jsonPath("$[0].abholort", is(benutzerangebote.get(0).getAbholort())))
        .andExpect(jsonPath("$[1].abholort", is(benutzerangebote.get(1).getAbholort())))
        .andExpect(jsonPath("$[2].abholort", is(benutzerangebote.get(2).getAbholort())))
        ;
    }


    @Test
    @DisplayName("AngebotRestController - GET /api/benutzer/<zweitbenutzerid>/angebot")
    @Transactional
    public void get_api_angebot1() throws Exception {
        var benutzerlst = ueb07_init.initDB();

        var benutzer1 = benutzerlst.get(1);
        var benutzerangebote = benutzer1.getAngebote();

        mockmvc.perform(
            get("/api/benutzer/"+benutzer1.getId()+"/angebot")
            .contentType("application/json")
        ).andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$", hasSize((int)benutzerangebote.size())))

        .andExpect(jsonPath("$[0].beschreibung", is(benutzerangebote.get(0).getBeschreibung())))
        .andExpect(jsonPath("$[1].beschreibung", is(benutzerangebote.get(1).getBeschreibung())))

        .andExpect(jsonPath("$[0].anbieterid", is(benutzerangebote.get(0).getAnbieter().getId()), Long.class))
        .andExpect(jsonPath("$[1].anbieterid", is(benutzerangebote.get(1).getAnbieter().getId()), Long.class))

        .andExpect(jsonPath("$[0].anbietername", is(benutzerangebote.get(0).getAnbieter().getName())))
        .andExpect(jsonPath("$[1].anbietername", is(benutzerangebote.get(1).getAnbieter().getName())))

        .andExpect(jsonPath("$[0].mindestpreis", is(benutzerangebote.get(0).getMindestpreis()), Long.class))
        .andExpect(jsonPath("$[1].mindestpreis", is(benutzerangebote.get(1).getMindestpreis()), Long.class))

        .andExpect(jsonPath("$[0].gebote", is(0L), Long.class))
        .andExpect(jsonPath("$[1].gebote", is(0L), Long.class))

        .andExpect(jsonPath("$[0].abholort", is(benutzerangebote.get(0).getAbholort())))
        .andExpect(jsonPath("$[1].abholort", is(benutzerangebote.get(1).getAbholort())))
        ;
    }


    @Test
    @DisplayName("AngebotRestController - GET /api/benutzer/<drittbenutzerid>/angebot")
    @Transactional
    public void get_api_angebot2() throws Exception {
        var benutzerlst = ueb07_init.initDB();

        var benutzer2 = benutzerlst.get(2);
        var benutzerangebote = benutzer2.getAngebote();

        mockmvc.perform(
            get("/api/benutzer/"+benutzer2.getId()+"/angebot")
            .contentType("application/json")
        ).andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$", hasSize((int)benutzerangebote.size())))

        .andExpect(jsonPath("$[0].beschreibung", is(benutzerangebote.get(0).getBeschreibung())))

        .andExpect(jsonPath("$[0].anbieterid", is(benutzerangebote.get(0).getAnbieter().getId()), Long.class))

        .andExpect(jsonPath("$[0].anbietername", is(benutzerangebote.get(0).getAnbieter().getName())))

        .andExpect(jsonPath("$[0].mindestpreis", is(benutzerangebote.get(0).getMindestpreis()), Long.class))

        .andExpect(jsonPath("$[0].gebote", is(0L), Long.class))

        .andExpect(jsonPath("$[0].abholort", is(benutzerangebote.get(0).getAbholort())))
        ;
    }


    @Test
    @DisplayName("AngebotRestController - GET /api/benutzer/<viertbenutzerid>/angebot")
    @Transactional
    public void get_api_angebot3() throws Exception {
        var benutzerlst = ueb07_init.initDB();

        var benutzer3 = benutzerlst.get(3);
        var benutzerangebote = benutzer3.getAngebote();

        mockmvc.perform(
            get("/api/benutzer/"+benutzer3.getId()+"/angebot")
            .contentType("application/json")
        ).andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$", hasSize((int)benutzerangebote.size())))

        .andExpect(jsonPath("$[0].beschreibung", is(benutzerangebote.get(0).getBeschreibung())))

        .andExpect(jsonPath("$[0].anbieterid", is(benutzerangebote.get(0).getAnbieter().getId()), Long.class))

        .andExpect(jsonPath("$[0].anbietername", is(benutzerangebote.get(0).getAnbieter().getName())))

        .andExpect(jsonPath("$[0].mindestpreis", is(benutzerangebote.get(0).getMindestpreis()), Long.class))

        .andExpect(jsonPath("$[0].gebote", is(0L), Long.class))

        .andExpect(jsonPath("$[0].abholort", is(benutzerangebote.get(0).getAbholort())))
        ;
    }

}