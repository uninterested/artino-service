package com.artino.service.validator.phone;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER })
@Constraint(validatedBy = { PhoneValidator.class })
public @interface Phone {
    String message() default "手机号不合法";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}