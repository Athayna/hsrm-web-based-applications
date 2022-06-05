package de.hsrm.mi.web.projekt.api.gebot;

import org.springframework.web.bind.annotation.RestController;

import de.hsrm.mi.web.projekt.gebot.Gebot;
import de.hsrm.mi.web.projekt.gebot.GebotRepository;
import de.hsrm.mi.web.projekt.gebot.GebotServiceImpl;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
public class GebotRestController {
    @Autowired
    GebotServiceImpl gService;
    @Autowired
    GebotRepository gebRep;

    @GetMapping(value = "/gebot", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GetGebotResponseDTO> getGebot() {
        List<GetGebotResponseDTO> gebList = new ArrayList<GetGebotResponseDTO>();
        for (Gebot g : gService.findeAlleGebote()) {
            gebList.add(GetGebotResponseDTO.from(g));
        }
        return gebList;
    }

    @PostMapping(value = "/gebot")
    public long postGebot(@RequestBody JSONObject json) {
        AddGebotRequestDTO gebot = new AddGebotRequestDTO((long)json.get("gebieterid"), (long)json.get("angebotid"), (long)json.get("betrag"));
        return gService.bieteFuerAngebot(gebot.benutzerprofilid(), gebot.angebotid(), gebot.betrag()).getId();
    }

    @DeleteMapping("/gebot/{id}")
    public void deleteGebot(@PathVariable Long id) {
        gService.loescheGebot(id);
    }
}
