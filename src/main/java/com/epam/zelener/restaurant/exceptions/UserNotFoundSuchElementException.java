package com.epam.zelener.restaurant.exceptions;

import java.util.NoSuchElementException;

public class UserNotFoundSuchElementException extends NoSuchElementException {
    public UserNotFoundSuchElementException() {
        super();
    }

    public UserNotFoundSuchElementException(String s) {
        super(s);
    }
}
