package de.hsrm.mi.web.projekt.api.benutzer;

import java.time.LocalDateTime;

import de.hsrm.mi.web.projekt.angebot.Angebot;

public record GetAngebotResponseDTO(
        long angebotid,
        String beschreibung,
        long anbieterid,
        String anbietername,
        long mindestpreis,
        LocalDateTime ablaufzeitpunkt,
        String abholort, double lat, double lon,
        long topgebot, long gebote
        ) {
    public static GetAngebotResponseDTO from(Angebot a) {
        return new GetAngebotResponseDTO(
                a.getId(),
                a.getBeschreibung(),
                a.getAnbieter().getId(),
                a.getAnbieter().getName(),
                a.getMindestpreis(),
                a.getAblaufzeitpunkt(), 
                a.getAbholort(), a.getLat(), a.getLon(),
                a.getTopgebot(), a.getGebote().size());
    }
}