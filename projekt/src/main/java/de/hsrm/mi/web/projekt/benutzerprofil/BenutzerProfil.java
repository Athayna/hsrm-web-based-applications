package de.hsrm.mi.web.projekt.benutzerprofil;

import java.time.LocalDate;

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

    @Override
    public String toString() {
        return "BenutzerProfil [adresse=" + adresse + ", email=" + email + ", geburtsdatum=" + geburtsdatum
                + ", interessen=" + interessen + ", lieblingsfarbe=" + lieblingsfarbe + ", name=" + name + "]";
    }  
}
