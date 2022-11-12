package me.taling.live.global.valid;

import org.apache.commons.lang3.StringUtils;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Pattern;

public class BlankOrConstraintValidator implements ConstraintValidator<BlankOrPattern, String> {

    private BlankOrPattern annotation;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(s)) {
            return true;
        }

        Pattern patternValidator = annotation.value();
        if (patternValidator != null) {
            Constraint[] enumConstants = (Constraint[]) patternValidator.annotationType().getEnumConstants();
            for (Constraint constraint : enumConstants) {
                constraint.validatedBy();
            }
        }

        return true;
    }

    @Override
    public void initialize(BlankOrPattern annotation) {
        this.annotation = annotation;
    }
}
