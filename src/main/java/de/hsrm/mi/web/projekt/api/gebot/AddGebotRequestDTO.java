package de.hsrm.mi.web.projekt.api.gebot;

public record AddGebotRequestDTO(Long benutzerprofilid,
        Long angebotid,
        Long betrag) {}