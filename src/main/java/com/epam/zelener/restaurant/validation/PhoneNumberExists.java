package com.epam.zelener.restaurant.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniquePhoneValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumberExists {
    String message() default "{User with such phone number already exists}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
