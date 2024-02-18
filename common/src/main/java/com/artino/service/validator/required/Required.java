package com.artino.service.validator.required;


import com.artino.service.validator.phone.PhoneValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER })
@Constraint(validatedBy = { RequiredValidator.class })
public @interface Required {
    String message() default "请输入值";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
