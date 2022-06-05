package de.hsrm.mi.web.projekt.api.gebot;

import java.time.LocalDateTime;

import de.hsrm.mi.web.projekt.gebot.Gebot;

public record GetGebotResponseDTO(Long gebotid,
        Long gebieterid, String gebietername,
        Long angebotid, String angebotbeschreibung,
        Long betrag,
        LocalDateTime gebotzeitpunkt) {
    public static GetGebotResponseDTO from(Gebot g) {
        return new GetGebotResponseDTO(
                g.getId(),
                g.getGebieter().getId(), g.getGebieter().getName(),
                g.getAngebot().getId(), g.getAngebot().getBeschreibung(),
                g.getBetrag(),
                g.getGebotzeitpunkt());
    }
}
