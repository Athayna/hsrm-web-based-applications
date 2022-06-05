package de.hsrm.mi.web.projekt.gebot;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import de.hsrm.mi.web.projekt.angebot.Angebot;
import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerProfil;

@Entity
public class Gebot {
    @Id @GeneratedValue
    private long id;
    @Version
    private long version;
    @ManyToOne
    private BenutzerProfil gebieter;
    @ManyToOne
    private Angebot angebot;
    private long betrag = 0;
    private LocalDateTime gebotzeitpunkt = LocalDateTime.now();

    public Angebot getAngebot() {
        return angebot;
    }
    public BenutzerProfil getGebieter() {
        return gebieter;
    }
    public long getBetrag() {
        return betrag;
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getGebotzeitpunkt() {
        return gebotzeitpunkt;
    }

    public void setGebieter(BenutzerProfil gebieter) {
        this.gebieter = gebieter;
    }

    public void setAngebot(Angebot angebot) {
        this.angebot = angebot;
    }

    public void setBetrag(long betrag) {
        this.betrag = betrag;
    }

    public void setGebotzeitpunkt(LocalDateTime gebotzeitpunkt) {
        this.gebotzeitpunkt = gebotzeitpunkt;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Gebot other = (Gebot) obj;
        if (this.id == other.id)
            return true;
        return false;
    }
}
