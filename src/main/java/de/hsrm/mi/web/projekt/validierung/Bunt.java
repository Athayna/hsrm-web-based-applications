package de.hsrm.mi.web.projekt.validierung;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.constraints.Pattern;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=BuntValidator.class)
@Pattern(regexp = "#\w{(3|6)}")
public @interface Bunt {
    String message() default "Der Wert ist nicht bunt";
    
}
