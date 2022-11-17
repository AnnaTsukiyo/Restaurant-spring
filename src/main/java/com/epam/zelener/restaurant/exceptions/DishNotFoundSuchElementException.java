package com.epam.zelener.restaurant.exceptions;

import java.util.NoSuchElementException;

public class DishNotFoundSuchElementException extends NoSuchElementException {
    public DishNotFoundSuchElementException() {
        super();
    }

    public DishNotFoundSuchElementException(String s) {
        super(s);
    }
}

