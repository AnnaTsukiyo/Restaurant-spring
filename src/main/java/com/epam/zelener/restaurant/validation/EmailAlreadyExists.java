package com.epam.zelener.restaurant.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueEmailValidator.class)
@Target( {ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailAlreadyExists {
    String message() default "{User with such email already exists}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

