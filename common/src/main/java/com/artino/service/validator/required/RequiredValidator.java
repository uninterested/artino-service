package com.artino.service.validator.required;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RequiredValidator implements ConstraintValidator<Required, Object>
{
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext)
    {
        if (value == null) return false;
        if (value instanceof String str) {
            return !StringUtils.isEmpty(str) && !StringUtils.isBlank(str);
        }
        return true;
    }
}