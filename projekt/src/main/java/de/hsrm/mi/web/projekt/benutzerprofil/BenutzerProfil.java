package de.hsrm.mi.web.projekt.benutzerprofil;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class BenutzerProfil {
    private String name = "";
    private LocalDate geburtsdatum = LocalDate.of(1,1,1);
    private String adresse = "";
    private String email = "";
    private String lieblingsfarbe = "";
    private String interessen = "";

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
        List<String> interessen = Arrays.asList(this.interessen.split(", "));
        return interessen;
    }

    @Override
    public String toString() {
        return "BenutzerProfil [adresse=" + adresse + ", email=" + email + ", geburtsdatum=" + geburtsdatum
                + ", interessen=" + interessen + ", lieblingsfarbe=" + lieblingsfarbe + ", name=" + name + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((adresse == null) ? 0 : adresse.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((geburtsdatum == null) ? 0 : geburtsdatum.hashCode());
        result = prime * result + ((interessen == null) ? 0 : interessen.hashCode());
        result = prime * result + ((lieblingsfarbe == null) ? 0 : lieblingsfarbe.hashCode());
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
        if (adresse == null) {
            if (other.adresse != null)
                return false;
        } else if (!adresse.equals(other.adresse))
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        if (geburtsdatum == null) {
            if (other.geburtsdatum != null)
                return false;
        } else if (!geburtsdatum.equals(other.geburtsdatum))
            return false;
        if (interessen == null) {
            if (other.interessen != null)
                return false;
        } else if (!interessen.equals(other.interessen))
            return false;
        if (lieblingsfarbe == null) {
            if (other.lieblingsfarbe != null)
                return false;
        } else if (!lieblingsfarbe.equals(other.lieblingsfarbe))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
}
