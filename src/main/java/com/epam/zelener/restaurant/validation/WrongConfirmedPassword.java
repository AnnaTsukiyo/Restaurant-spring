package com.epam.zelener.restaurant.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ConfirmedPasswordValidator.class)
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface WrongConfirmedPassword {
    String message() default "Wrong confirmed password entered";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
