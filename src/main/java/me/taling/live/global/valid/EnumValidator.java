package me.taling.live.global.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<Enum, String> {

    private Enum annotation;

    @Override
    public void initialize(Enum constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Object[] enumConstants = annotation.enumClass().getEnumConstants();
        if (enumConstants != null) {
            for (Object enumConstant : enumConstants) {
                if (value.equals(enumConstant.toString())
                        || (this.annotation.ignoreCase() && value.equalsIgnoreCase(enumConstant.toString()))) {
                    return true;
                }
            }
        }
        return false;
    }
}
