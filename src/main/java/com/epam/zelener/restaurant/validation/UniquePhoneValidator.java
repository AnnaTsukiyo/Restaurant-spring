package com.epam.zelener.restaurant.validation;

import com.epam.zelener.restaurant.services.UserService;

import javax.annotation.Resource;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.NoSuchElementException;

public class UniquePhoneValidator implements ConstraintValidator<PhoneNumberExists, String> {

    @Resource
    private UserService userService;

    @Override
    public void initialize(PhoneNumberExists phone) {
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        try{
            userService.getUserByPhoneNumber(phoneNumber);
        return false;
        } catch (NullPointerException | NoSuchElementException ex) {
            return true;
        }
    }
}
