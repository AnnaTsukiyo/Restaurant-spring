package com.epam.zelener.restaurant.validation;

import com.epam.zelener.restaurant.services.UserService;

import javax.annotation.Resource;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.NoSuchElementException;

public class UniqueEmailValidator implements ConstraintValidator<EmailAlreadyExists, String> {

    @Resource
    private UserService userService;

    @Override
    public void initialize(EmailAlreadyExists emailAlreadyExists) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        try {
            userService.getUserByEmail(email);
            return false;
        } catch (NullPointerException | NoSuchElementException ex) {
            return true;
        }
    }
}
