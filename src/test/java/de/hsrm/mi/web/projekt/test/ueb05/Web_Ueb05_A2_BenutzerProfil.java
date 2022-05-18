package de.hsrm.mi.web.projekt.test.ueb05;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.Entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerProfil;


@Testable
public class Web_Ueb05_A2_BenutzerProfil {
    @Test
    @DisplayName("BenutzerProfil-Entity instanziierbar und dahanne")
    public void benutzerprofil_entitaet_ok() {
        BenutzerProfil profil = new BenutzerProfil();
        assertThat(BenutzerProfil.class).hasAnnotation(Entity.class);
    }
}