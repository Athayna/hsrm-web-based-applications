package de.hsrm.mi.web.projekt.test.ueb04;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerProfil;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testable
public class Web_Ueb04_A1_BenutzerProfilValidierung {

    private static ValidatorFactory validatorFactory;

    private static Validator validator;

    @BeforeAll
    public static void setupValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void closeValidatorFactory() {
        validatorFactory.close();
    }

    /*
     * Name darf nicht Null sein und muss mindestens 3 Zeichen lang sein
     */

    @Test
    @DisplayName("BenutzerProfil-Validierung: Name nicht leer")
    public void validateNameNichtLeer() {
        Set<ConstraintViolation<BenutzerProfil>> violations = validator.validateValue(
                BenutzerProfil.class, "name", "");
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("BenutzerProfil-Validierung: Name nicht null")
    public void validateNameNichtNull() {
        Set<ConstraintViolation<BenutzerProfil>> violations = validator.validateValue(
                BenutzerProfil.class, "name", null);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("BenutzerProfil-Validierung: Name ok")
    public void validateNameOk() {
        Set<ConstraintViolation<BenutzerProfil>> violations = validator.validateValue(
                BenutzerProfil.class, "name", "Jux");
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("BenutzerProfil-Validierung: Name zu kurz ist falsch")
    public void validateNameZuKurz() {
        Set<ConstraintViolation<BenutzerProfil>> violations = validator.validateValue(
                BenutzerProfil.class, "name", "Gr");
        assertFalse(violations.isEmpty());
    }

    /*
     * Geburtsdatum
     */

    @Test
    @DisplayName("BenutzerProfil-Validierung: Geburtsdatum darf nicht Null sein")
    public void validateGeburtsdatumAbgelaufen() {
        Set<ConstraintViolation<BenutzerProfil>> violations = validator.validateValue(
                BenutzerProfil.class, "geburtsdatum", null);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("BenutzerProfil-Validierung: Geburtsdatum 'heute' ok")
    public void validateGeburtsdatumHeuteOk() {
        Set<ConstraintViolation<BenutzerProfil>> violations = validator.validateValue(
                BenutzerProfil.class, "geburtsdatum", LocalDate.now());
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("BenutzerProfil-Validierung: Geburtsdatum in Vergangenheit ok")
    public void validateGeburtsdatumVergangenheitOk() {
        Set<ConstraintViolation<BenutzerProfil>> violations = validator.validateValue(
                BenutzerProfil.class, "geburtsdatum", LocalDate.now().minusDays(17));
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("BenutzerProfil-Validierung: Geburtsdatum in Zukunft falsch")
    public void validateGeburtsdatumZukunftFalsch() {
        Set<ConstraintViolation<BenutzerProfil>> violations = validator.validateValue(
                BenutzerProfil.class, "geburtsdatum", LocalDate.now().plusDays(17));
        assertFalse(violations.isEmpty());
    }

    /*
     * Adresse
     */

    @Test
    @DisplayName("BenutzerProfil-Validierung: Adresse nicht null")
    public void validateAdresseNichtNull() {
        Set<ConstraintViolation<BenutzerProfil>> violations = validator.validateValue(
                BenutzerProfil.class, "adresse", null);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("BenutzerProfil-Validierung: Adresse ok")
    public void validateAdresseOk() {
        Set<ConstraintViolation<BenutzerProfil>> violations = validator.validateValue(
                BenutzerProfil.class, "adresse", "Unter den Eichen 7, 65195 Wiesbaden");
        assertTrue(violations.isEmpty());
    }

    /*
     * EMail
     */

    @Test
    @DisplayName("BenutzerProfil-Validierung: EMail kann Null sein")
    public void validateEmailNull() {
        Set<ConstraintViolation<BenutzerProfil>> violations = validator.validateValue(
                BenutzerProfil.class, "email", null);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("BenutzerProfil-Validierung: EMail ok 1")
    public void validateEmailOk1() {
        Set<ConstraintViolation<BenutzerProfil>> violations = validator.validateValue(
                BenutzerProfil.class, "email", "joendhard.biffel@vollradisroda.nl");
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("BenutzerProfil-Validierung: EMail ok 2")
    public void validateEmailOk2() {
        Set<ConstraintViolation<BenutzerProfil>> violations = validator.validateValue(
                BenutzerProfil.class, "email", "friedfert@localhost");
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("BenutzerProfil-Validierung: EMail falsch 1")
    public void validateEmailFalsch1() {
        Set<ConstraintViolation<BenutzerProfil>> violations = validator.validateValue(
                BenutzerProfil.class, "email", "joghurta.biffel in vollradisroda.nl");
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("BenutzerProfil-Validierung: EMail falsch 2")
    public void validateEmailFalsch2() {
        Set<ConstraintViolation<BenutzerProfil>> violations = validator.validateValue(
                BenutzerProfil.class, "email", "joghurta");
        assertFalse(violations.isEmpty());
    }

    /*
     * Lieblingsfarbe darf nicht Null und nicht laenger als 7 Zeichen sein
     */

    @Test
    @DisplayName("BenutzerProfil-Validierung: Farbe null ist nicht ok")
    public void validateLieblingsfarbeNichtNull() {
        Set<ConstraintViolation<BenutzerProfil>> violations = validator.validateValue(
                BenutzerProfil.class, "lieblingsfarbe", null);
        System.out.println(violations);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("BenutzerProfil-Validierung: Lieblingsfarbe kann Leerstring sein")
    public void validateLieblingsfarbeNichtLeer() {
        Set<ConstraintViolation<BenutzerProfil>> violations = validator.validateValue(
                BenutzerProfil.class, "lieblingsfarbe", "");
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("BenutzerProfil-Validierung: Lieblingsfarbe #RRGGBB ok")
    public void validateLieblingsfarbeRRGGBBistOk() {
        Set<ConstraintViolation<BenutzerProfil>> violations = validator.validateValue(
                BenutzerProfil.class, "lieblingsfarbe", "#abCDef");
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("BenutzerProfil-Validierung: Lieblingsfarbe #RGB ok")
    public void validateLieblingsfarbeRGBistOk() {
        Set<ConstraintViolation<BenutzerProfil>> violations = validator.validateValue(
                BenutzerProfil.class, "lieblingsfarbe", "#123");
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("BenutzerProfil-Validierung: Lieblingsfarbe zu lang")
    public void validateLieblingsfarbeZuLang() {
        Set<ConstraintViolation<BenutzerProfil>> violations = validator.validateValue(
                BenutzerProfil.class, "lieblingsfarbe", "#4242429");
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("BenutzerProfil-Validierung: Lieblingsfarbe zu kurz")
    public void validateLieblingsfarbeZuKurz() {
        Set<ConstraintViolation<BenutzerProfil>> violations = validator.validateValue(
                BenutzerProfil.class, "lieblingsfarbe", "#12");
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("BenutzerProfil-Validierung: Lieblingsfarbe enthält merkwürdige Zeichen")
    public void validateLieblingsfarbeMitSeltsamenZeichen() {
        Set<ConstraintViolation<BenutzerProfil>> violations = validator.validateValue(
                BenutzerProfil.class, "lieblingsfarbe", "#bcdx12");
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("BenutzerProfil-Validierung: Lieblingsfarbe beginnt nicht mit '#'")
    public void validateLieblingsfarbeOhneHashAmAnfang() {
        Set<ConstraintViolation<BenutzerProfil>> violations = validator.validateValue(
                BenutzerProfil.class, "lieblingsfarbe", "123456");
        assertFalse(violations.isEmpty());
    }

}
