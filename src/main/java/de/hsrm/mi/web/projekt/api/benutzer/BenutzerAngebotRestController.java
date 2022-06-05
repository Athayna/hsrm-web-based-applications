package de.hsrm.mi.web.projekt.api.benutzer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.hsrm.mi.web.projekt.angebot.Angebot;
import de.hsrm.mi.web.projekt.api.gebot.GetGebotResponseDTO;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerprofilServiceImpl;
import de.hsrm.mi.web.projekt.gebot.Gebot;


@RestController
@RequestMapping("/api")
public class BenutzerAngebotRestController {
    @Autowired
    BenutzerprofilServiceImpl bService;

    @GetMapping(value = "/angebot", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GetAngebotResponseDTO> getAngebote() {
        List<GetAngebotResponseDTO> anList = new ArrayList<GetAngebotResponseDTO>();
        for (Angebot a : bService.alleAngebote()) {
            anList.add(GetAngebotResponseDTO.from(a));
        }
        return anList;
    }

    @GetMapping(value = "/angebot/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GetAngebotResponseDTO getAngebot(@PathVariable("id") long id) {
        return GetAngebotResponseDTO.from(bService.findeAngebotMitId(id).get());
    }

    @GetMapping(value="/angebot/{id}/gebot")
    public List<GetGebotResponseDTO> getGebote(@PathVariable("id") long id) {
        List<GetGebotResponseDTO> gebList = new ArrayList<GetGebotResponseDTO>();
        for (Gebot g : bService.findeAngebotMitId(id).get().getGebote()) {
            gebList.add(GetGebotResponseDTO.from(g));
        }
        return gebList;
    }

    @GetMapping(value = "/benutzer/{id}/angebot")
    public List<GetAngebotResponseDTO> getBenutzerAngebote(@PathVariable("id") long id) {
        List<GetAngebotResponseDTO> anList = new ArrayList<GetAngebotResponseDTO>();
        for (Angebot a : bService.holeBenutzerProfilMitId(id).get().getAngebote()) {
            anList.add(GetAngebotResponseDTO.from(a));
        }
        return anList;
    }
    
}
