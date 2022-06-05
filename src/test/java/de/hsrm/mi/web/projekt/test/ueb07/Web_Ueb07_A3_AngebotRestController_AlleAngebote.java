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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import de.hsrm.mi.web.projekt.angebot.AngebotRepository;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class Web_Ueb07_A3_AngebotRestController_AlleAngebote {
    @Autowired
    private AngebotRepository angebotRepository;

    @Autowired
    private MockMvc mockmvc;

    @Autowired
    Ueb07_init ueb07_init;

    @Test
    public void vorabcheck() {
        assertThat(angebotRepository).isNotNull();
    }

    @BeforeEach
    @Transactional
    public void cleanDB()  {
        ueb07_init.cleanBenutzerAngebotDB();
    }


    @Test
    @DisplayName("AngebotRestController - GET /api/angebot, alle Angebote")
    @Transactional
    public void get_api_angebot1() throws Exception {
        ueb07_init.initDB();
        var anzahlangebote = angebotRepository.count();

        mockmvc.perform(
            get("/api/angebot")
            .contentType("application/json")
        ).andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$", hasSize((int)anzahlangebote)))
        ;
    }

    @Test
    @DisplayName("AngebotRestController - GET /api/angebot/<id>")
    @Transactional
    public void get_api_angebot_id1() throws Exception {
        ueb07_init.initDB();
        var angebotlst = angebotRepository.findAll();

        for (var a: angebotlst) {
            mockmvc.perform(
                get("/api/angebot/"+a.getId())
                .contentType("application/json")
            ).andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.angebotid", is(a.getId()), Long.class))
            .andExpect(jsonPath("$.beschreibung", is(a.getBeschreibung())))
            .andExpect(jsonPath("$.anbieterid", is(a.getAnbieter().getId()), Long.class))
            .andExpect(jsonPath("$.anbietername", is(a.getAnbieter().getName())))
            .andExpect(jsonPath("$.mindestpreis", is(a.getMindestpreis()), Long.class))
            .andExpect(jsonPath("$.abholort", is(a.getAbholort())))
            .andExpect(jsonPath("$.gebote", is((long)a.getGebote().size()), Long.class))
            ;
        }
    }
}