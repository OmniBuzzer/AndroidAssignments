package com.example.androidapplications;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LoginActivityTest {

    @Test
    public void hasMainActivity() {
        String expectedTarget = "MainActivity";
        assertEquals(expectedTarget, MainActivity.class.getSimpleName());
    }

    @Test
    public void validEmail() {
        assertTrue(isValidEmailFormat("user@email.com"));
        assertTrue(isValidEmailFormat("test_one@company.com"));
    }

    @Test
    public void invalidEmail() {
        assertFalse(isValidEmailFormat("fjgtjtr54645@"));
        assertFalse(isValidEmailFormat("@random.com"));
    }

    @Test
    public void emptyInput() {
        assertFalse(isValidEmailFormat(""));
    }

    private boolean isValidEmailFormat(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
}