package de.hsrm.mi.web.projekt.test.ueb07;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.MockMvc;

import de.hsrm.mi.web.projekt.gebot.GebotRepository;
import de.hsrm.mi.web.projekt.gebot.GebotService;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class Web_Ueb07_A2_GebotRestController {

    @Autowired
    private GebotRepository gebotRepository;

    @Autowired
    private GebotService gebotService;

    @Autowired
    private MockMvc mockmvc;

    @Autowired
    Ueb07_init ueb07_init;

    @Test
    public void vorabcheck() {
        assertThat(gebotRepository).isNotNull();
        assertThat(gebotService).isNotNull();
    }

    @BeforeEach
    @Transactional
    public void cleanDB()  {
        ueb07_init.cleanBenutzerAngebotDB();
        gebotRepository.deleteAll();
    }

    @Test
    @DisplayName("GebotRestController - GET /api/gebot, alle Gebote")
    @Transactional
    public void get_api_angebot1() throws Exception {
        var benutzerlst = ueb07_init.initDB();

        var bieter1 = benutzerlst.get(1);
        var bieter2 = benutzerlst.get(2);
        var bieter3 = benutzerlst.get(3);

        var angebot = benutzerlst.get(0).getAngebote().get(1);

        var g1 = gebotService.bieteFuerAngebot(bieter1.getId(), angebot.getId(), 10);
        var g2 = gebotService.bieteFuerAngebot(bieter2.getId(), angebot.getId(), 20);
        var g3 = gebotService.bieteFuerAngebot(bieter3.getId(), angebot.getId(), 30);


        mockmvc.perform(
            get("/api/gebot")
            .contentType("application/json")
        ).andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$", hasSize(3)))
        .andExpect(jsonPath("$[0].gebotid", 
            anyOf(is(g1.getId()), is(g2.getId()), is(g3.getId())), Long.class))
        .andExpect(jsonPath("$[0].gebieterid", 
            anyOf(is(g1.getGebieter().getId()), is(g2.getGebieter().getId()), is(g3.getGebieter().getId())), Long.class))
        .andExpect(jsonPath("$[0].angebotid", 
            anyOf(is(g1.getAngebot().getId()), is(g2.getAngebot().getId()), is(g3.getAngebot().getId())), Long.class))
        .andExpect(jsonPath("$[0].betrag", 
            anyOf(is(g1.getBetrag()), is(g2.getBetrag()), is(g3.getBetrag())), Long.class))
        ;
    }


    @Test
    @DisplayName("GebotRestController - DELETE /api/gebot/{id}, einzelne Gebote löschen")
    @Transactional
    public void get_api_angebot_einzeln() throws Exception {
        var benutzerlst = ueb07_init.initDB();

        var bieter1 = benutzerlst.get(1);
        var bieter2 = benutzerlst.get(2);
        var bieter3 = benutzerlst.get(3);

        var angebot = benutzerlst.get(0).getAngebote().get(1);

        assertThat(gebotRepository.count()).isZero();

        var g1 = gebotService.bieteFuerAngebot(bieter1.getId(), angebot.getId(), 10);
        var g2 = gebotService.bieteFuerAngebot(bieter2.getId(), angebot.getId(), 20);
        var g3 = gebotService.bieteFuerAngebot(bieter3.getId(), angebot.getId(), 30);

        assertThat(gebotRepository.count()).isEqualTo(3);


        for (var g: List.of(g2,g1,g3)) {
            assertThat(gebotRepository.existsById(g.getId())).isTrue();
            mockmvc.perform(
                delete("/api/gebot/"+g.getId())
                .contentType("application/json")
            ).andExpect(status().is2xxSuccessful())
            ;
            assertThat(gebotRepository.existsById(g.getId())).isFalse();
        }

        assertThat(gebotRepository.count()).isZero();
    }

}