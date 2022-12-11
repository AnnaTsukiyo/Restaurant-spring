package com.epam.zelener.restaurant.validation;

import com.epam.zelener.restaurant.dtos.FullUserDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ConfirmedPasswordValidator implements ConstraintValidator<WrongConfirmedPassword, FullUserDto> {
    @Override
    public void initialize(WrongConfirmedPassword confirmedPassword) {
    }

    @Override
    public boolean isValid(FullUserDto user, ConstraintValidatorContext cxt) {
        return user.getPassword().equals(user.getConfirmedPassword());
    }
}
