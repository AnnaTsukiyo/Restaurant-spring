package com.epam.zelener.restaurant.validation;

import com.epam.zelener.restaurant.services.DishService;

import javax.annotation.Resource;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.NoSuchElementException;

public class UniqueTitleValidator implements ConstraintValidator<TitleAlreadyExists, String> {

    @Resource
    private DishService dishService;

    @Override
    public void initialize(TitleAlreadyExists title) {
    }

    @Override
    public boolean isValid(String title, ConstraintValidatorContext context) {
        try {
            dishService.getDishByTitle(title);
            return false;
        } catch (NullPointerException | NoSuchElementException ex) {
            return true;
        }
    }
}
