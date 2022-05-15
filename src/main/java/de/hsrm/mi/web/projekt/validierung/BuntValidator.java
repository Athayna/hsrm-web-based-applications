package de.hsrm.mi.web.projekt.validierung;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BuntValidator implements ConstraintValidator<Bunt, String> {
    protected String color;

    @Override
    public boolean isValid(String obj, ConstraintValidatorContext ctx) {
        if (obj == null)
            return true;
        if (obj.length() == 4) {
            if (obj.charAt(1) == obj.charAt(2) || obj.charAt(1) == obj.charAt(3) || obj.charAt(2) == obj.charAt(3))
                return false;
        }
        else if(obj.length() == 7) {
            if (obj.substring(1, 3).equals(obj.substring(3, 5)) || obj.substring(1, 3).equals(obj.substring(5))  || obj.substring(3, 5).equals(obj.substring(5)))
                return false;
        }
        return true;
    }
}
