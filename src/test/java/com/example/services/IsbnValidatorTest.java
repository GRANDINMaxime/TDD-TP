package com.example.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class IsbnValidatorTest {
    @Test
    public void checkValid10CharsISBNCode() {
        ISBNValidator validator = new ISBNValidator();
        boolean result = validator.validateISBN("2210765528");
        assertTrue(result, "first assertion");
        result = validator.validateISBN("2226392122");
        assertTrue(result, "second assertion");
    }

    @Test
    public void checkInvalid10CharsISBNCode() {
        ISBNValidator validator = new ISBNValidator();
        boolean result = validator.validateISBN("2210765525");
        assertFalse(result);
    }

    @Test
    public void invalidLengthShouldThrowsException() {
        ISBNValidator validator = new ISBNValidator();
        assertThrows(NumberFormatException.class, () -> validator.validateISBN("123456789"));
        assertThrows(NumberFormatException.class, () -> validator.validateISBN("12345678912"));
    }

    @Test
    public void nonNumericISBNThrowsException() {
        ISBNValidator validator = new ISBNValidator();
        assertThrows(NumberFormatException.class, () -> validator.validateISBN("helloworld"));
    }

    @Test
    public void checkISBNEndingWithAnXIsValid() {
        ISBNValidator validator = new ISBNValidator();
        boolean result = validator.validateISBN("140274577X");
        assertTrue(result);
    }

    @Test
    public void checkValid13CharsISBNCode() {
        ISBNValidator validator = new ISBNValidator();
        boolean result = validator.validateISBN("9781402745775");
        assertTrue(result);
    }
}
