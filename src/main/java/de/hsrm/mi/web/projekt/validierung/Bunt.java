package de.hsrm.mi.web.projekt.validierung;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=BuntValidator.class)
@Pattern.List({@Pattern(regexp = "^$|^#(?:[A-Fa-f\\d]{3}){1,2}$"), })
public @interface Bunt {
    String message() default "Diese Farbe enthält zwei gleiche R/G/B-Anteile und ist daher nicht bunt genug";
    
    Class<? extends Payload>[] payload() default { };

    Class<?>[] groups() default { };
}
