package de.hsrm.mi.web.projekt.gebot;

import java.util.List;

public interface GebotService {
    List<Gebot> findeAlleGebote();
    List<Gebot> findeAlleGeboteFuerAngebot(long angebotid);
    Gebot bieteFuerAngebot(long benutzerprofilid, long angebotid, long betrag);
    void loescheGebot(long gebotid);
}
