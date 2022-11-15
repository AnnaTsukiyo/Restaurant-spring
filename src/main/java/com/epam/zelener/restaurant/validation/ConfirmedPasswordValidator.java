package com.epam.zelener.restaurant.validation;

import com.epam.zelener.restaurant.dtos.UserSignUpDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ConfirmedPasswordValidator implements ConstraintValidator<WrongConfirmedPassword, UserSignUpDto> {
    @Override
    public void initialize(WrongConfirmedPassword confirmedPassword) {
    }

    @Override
    public boolean isValid(UserSignUpDto user, ConstraintValidatorContext cxt) {
        return user.getPassword().equals(user.getPasswordConfirmed());
    }
}
