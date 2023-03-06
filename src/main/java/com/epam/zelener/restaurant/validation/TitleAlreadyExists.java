package com.epam.zelener.restaurant.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueTitleValidator.class)
@Target( {ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface TitleAlreadyExists {
    String message() default "{Dish with such title already exists}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
