package de.hsrm.mi.web.projekt.benutzerprofil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Version;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import de.hsrm.mi.web.projekt.angebot.Angebot;
import de.hsrm.mi.web.projekt.gebot.Gebot;
import de.hsrm.mi.web.projekt.projektuser.ProjektUser;
import de.hsrm.mi.web.projekt.validierung.Bunt;

@Entity
public class BenutzerProfil {
    @Id @GeneratedValue
    private long id;
    @Version
    private long version;
    @Size(min=3, max=60) @NotNull
    private String name = "";
    @PastOrPresent @NotNull @DateTimeFormat(iso = ISO.DATE)
    private LocalDate geburtsdatum = LocalDate.of(1,1,1);
    @NotNull
    private String adresse = "";
    @Email
    private String email = null;
    @NotNull @Bunt(message="{bunt.errormessage}")
    private String lieblingsfarbe = "";
    @NotNull
    private String interessen = "";
    private double lat, lon;
    @OneToMany(mappedBy = "anbieter", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Angebot> angebot = new ArrayList<Angebot>();
    @OneToMany(mappedBy = "gebieter", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Gebot> gebote = new ArrayList<Gebot>();
    @OneToOne(mappedBy = "benutzerProfil", cascade = CascadeType.REMOVE)
    private ProjektUser projektUser;

    //Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getGeburtsdatum() {
        return geburtsdatum;
    }

    public void setGeburtsdatum(LocalDate geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLieblingsfarbe() {
        return lieblingsfarbe;
    }

    public void setLieblingsfarbe(String lieblingsfarbe) {
        this.lieblingsfarbe = lieblingsfarbe;
    }

    public String getInteressen() {
        return interessen;
    }

    public void setInteressen(String interessen) {
        this.interessen = interessen;
    }

    public List<String> getInteressenListe() {
        if (interessen.isEmpty())
            return Collections.emptyList();
        return Arrays.asList(interessen.split("\\s*,\\s*"));
    }

    public long getId() {
        return id;
    }

    public long getVersion() {
        return version;
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

    public List<Angebot> getAngebote() {
        return angebot;
    }
    public List<Gebot> getGebote() {
        return gebote;
    }

    public void setProjektUser(ProjektUser projektUser) {
        this.projektUser = projektUser;
    }

    public ProjektUser getProjektUser() {
        return projektUser;
    }

    @Override
    public String toString() {
        return "BenutzerProfil [adresse=" + adresse + ", email=" + email + ", geburtsdatum=" + geburtsdatum + ", id="
                + id + ", interessen=" + interessen + ", lat=" + lat + ", lieblingsfarbe=" + lieblingsfarbe + ", lon="
                + lon + ", name=" + name + ", version=" + version + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((geburtsdatum == null) ? 0 : geburtsdatum.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BenutzerProfil other = (BenutzerProfil) obj;
        if (geburtsdatum == null) {
            if (other.geburtsdatum != null)
                return false;
        } else if (!geburtsdatum.equals(other.geburtsdatum))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
}
