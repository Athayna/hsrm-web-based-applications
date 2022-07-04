package de.hsrm.mi.web.projekt.projektuser;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerProfil;

@Entity
public class ProjektUser {
    @Id @NotBlank @Size(min = 3)
    private String username;
    @NotBlank @Size(min = 3)
    private String password;
    private String role = "";
    @OneToOne
    private BenutzerProfil benutzerProfil;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setBenutzerProfil(BenutzerProfil benutzerProfil) {
        this.benutzerProfil = benutzerProfil;
    }

    public BenutzerProfil getBenutzerProfil() {
        return benutzerProfil;
    }
}
