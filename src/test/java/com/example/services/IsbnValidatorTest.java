package com.example.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class IsbnValidatorTest {
    @Test
    public void checkValid10CharsISBNCode() {
        ISBNValidatorService validator = new ISBNValidatorService();
        boolean result = validator.validateISBN("2210765528");
        assertTrue(result, "first assertion");
        result = validator.validateISBN("2226392122");
        assertTrue(result, "second assertion");
    }

    @Test
    public void checkInvalid10CharsISBNCode() {
        ISBNValidatorService validator = new ISBNValidatorService();
        boolean result = validator.validateISBN("2210765525");
        assertFalse(result);
    }

    @Test
    public void invalidLengthShouldThrowsException() {
        ISBNValidatorService validator = new ISBNValidatorService();
        assertThrows(NumberFormatException.class, () -> validator.validateISBN("123456789"));
        assertThrows(NumberFormatException.class, () -> validator.validateISBN("12345678912"));
    }

    @Test
    public void nonNumericISBNThrowsException() {
        ISBNValidatorService validator = new ISBNValidatorService();
        assertThrows(NumberFormatException.class, () -> validator.validateISBN("helloworld"));
    }

    @Test
    public void checkISBNEndingWithAnXIsValid() {
        ISBNValidatorService validator = new ISBNValidatorService();
        boolean result = validator.validateISBN("140274577X");
        assertTrue(result);
    }

    @Test
    public void checkValid13CharsISBNCode() {
        ISBNValidatorService validator = new ISBNValidatorService();
        boolean result = validator.validateISBN("9781402745775");
        assertTrue(result);
    }
}
