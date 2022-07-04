package de.hsrm.mi.web.projekt.angebot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerProfil;
import de.hsrm.mi.web.projekt.gebot.Gebot;

@Entity
public class Angebot {
    @Id @GeneratedValue
    private long id;
    @Version
    private long version;
    private String beschreibung = "";
    private long mindestpreis = 0;
    @FutureOrPresent @NotNull @DateTimeFormat(iso=ISO.DATE_TIME)
    private LocalDateTime ablaufzeitpunkt = LocalDateTime.now();
    private String abholort = "";
    private double lat, lon;
    @ManyToOne
    private BenutzerProfil anbieter;
    @OneToMany(mappedBy = "gebieter", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Gebot> gebote = new ArrayList<Gebot>();

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public long getMindestpreis() {
        return mindestpreis;
    }

    public void setMindestpreis(long mindestpreis) {
        this.mindestpreis = mindestpreis;
    }

    public LocalDateTime getAblaufzeitpunkt() {
        return ablaufzeitpunkt;
    }

    public void setAblaufzeitpunkt(LocalDateTime ablaufzeitpunkt) {
        this.ablaufzeitpunkt = ablaufzeitpunkt;
    }

    public String getAbholort() {
        return abholort;
    }

    public void setAbholort(String abholort) {
        this.abholort = abholort;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public long getId() {
        return id;
    }

    public long getVersion() {
        return version;
    }

    public void setAnbieter(BenutzerProfil anbieter) {
        this.anbieter = anbieter;
    }

    public BenutzerProfil getAnbieter() {
        return anbieter;
    }

    public List<Gebot> getGebote() {
        return gebote;
    }

    public long getTopgebot() {
        if (gebote.isEmpty())
            return 0;
        long top = gebote.get(0).getId();
        long topBetrag = gebote.get(0).getBetrag();
        for (Gebot geb : gebote) {
            if (geb.getBetrag() > topBetrag) {
                top = geb.getId();
                topBetrag = geb.getBetrag();
            }
        }
        return top;
    }
}
