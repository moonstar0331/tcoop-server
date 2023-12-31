package com.genai.tcoop.common.annotation;

import com.genai.tcoop.common.annotation.validator.LongitudeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = LongitudeValidator.class)
public @interface IsLongitude {

    String message() default "Not valid longitude, must be between -180 and 180";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}