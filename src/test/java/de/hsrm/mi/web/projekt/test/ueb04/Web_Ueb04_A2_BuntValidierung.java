package de.hsrm.mi.web.projekt.test.ueb04;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import de.hsrm.mi.web.projekt.benutzerprofil.BenutzerProfil;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class Web_Ueb04_A2_BuntValidierung {

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

    @Test
    @DisplayName("@Bunt: Leerstring wird akzeptiert")
    public void validateFarbeLeerstringOk() {
        Set<ConstraintViolation<BenutzerProfil>> violations = validator.validateValue(
                BenutzerProfil.class, "lieblingsfarbe", "");
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("@Bunt: Farbe 'blau' ist kein RGB-Wert")
    public void validateFarbeKeinRGBFalsch() {
        Set<ConstraintViolation<BenutzerProfil>> violations = validator.validateValue(
                BenutzerProfil.class, "lieblingsfarbe", "blau");
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("@Bunt: Farbe muss mit # beginnen")
    public void validateFarbeKeinHashFalsch() {
        Set<ConstraintViolation<BenutzerProfil>> violations1 = validator.validateValue(
                BenutzerProfil.class, "lieblingsfarbe", "123456");
        assertFalse(violations1.isEmpty());

        Set<ConstraintViolation<BenutzerProfil>> violations2 = validator.validateValue(
                BenutzerProfil.class, "lieblingsfarbe", "1234567");
        assertFalse(violations2.isEmpty());

        Set<ConstraintViolation<BenutzerProfil>> violations3 = validator.validateValue(
                BenutzerProfil.class, "lieblingsfarbe", "123");
        assertFalse(violations3.isEmpty());

        Set<ConstraintViolation<BenutzerProfil>> violations4 = validator.validateValue(
                BenutzerProfil.class, "lieblingsfarbe", "63245");
        assertFalse(violations4.isEmpty());
    }

    @Test
    @DisplayName("@Bunt: Farbe falsche Länge")
    public void validateFarbeLaengeFalsch() {
        Set<ConstraintViolation<BenutzerProfil>> violations2 = validator.validateValue(
                BenutzerProfil.class, "lieblingsfarbe", "#ab");
        assertFalse(violations2.isEmpty());
        Set<ConstraintViolation<BenutzerProfil>> violations4 = validator.validateValue(
                BenutzerProfil.class, "lieblingsfarbe", "#1234");
        assertFalse(violations4.isEmpty());
        Set<ConstraintViolation<BenutzerProfil>> violations7 = validator.validateValue(
                BenutzerProfil.class, "lieblingsfarbe", "#1234567");
        assertFalse(violations7.isEmpty());
    }

    @Test
    @DisplayName("@Bunt: Farbe #rgb ok")
    public void validateFarbeRGBok() {
        Set<ConstraintViolation<BenutzerProfil>> violations = validator.validateValue(
                BenutzerProfil.class, "lieblingsfarbe", "#Ab8");
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("@Bunt: Farbe #rrggbb ok")
    public void validateFarbeRRGGBBok() {
        Set<ConstraintViolation<BenutzerProfil>> violations = validator.validateValue(
                BenutzerProfil.class, "lieblingsfarbe", "#abCDef");
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("@Bunt: Farbe #rgb mit falscher Ziffer")
    public void validateFarbeRGBzifferFalsch() {
        Set<ConstraintViolation<BenutzerProfil>> violations = validator.validateValue(
                BenutzerProfil.class, "lieblingsfarbe", "#Ag8");
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("@Bunt: Farbe #rrggbb mit falscher Ziffer")
    public void validateFarbeRRGGBBzifferFalsch() {
        Set<ConstraintViolation<BenutzerProfil>> violations = validator.validateValue(
                BenutzerProfil.class, "lieblingsfarbe", "#as7249");
        assertFalse(violations.isEmpty());
    }

}
