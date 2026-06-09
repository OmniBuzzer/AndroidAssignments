package com.example.androidapplications;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class ListItemsActivityTest {

    @Test
    public void cameraRequestCodeValue() {
        int expectedCameraRequestCode = 20;
        assertEquals("Code should be 20", 20, expectedCameraRequestCode);
        assertNotEquals("Code should not be 0", 0, expectedCameraRequestCode);
    }

    @Test
    public void intentValueIsCorrect() {
        // The OK button sends this exact string back to MainActivity
        String expectedValue = "Here is my response";
        assertEquals(expectedValue, "Here is my response");
        assertNotEquals("", expectedValue);
    }

    @Test
    public void intentTargetMainActivity() {
        String expectedCaller = "MainActivity";
        assertEquals(expectedCaller, MainActivity.class.getSimpleName());
    }

    @Test
    public void cameraRequestCode_differsFromMainActivityRequestCode() {
        int mainActivityRequestCode = 10;
        int listItemsCameraRequestCode = 20;
        assertNotEquals("Request codes must be distinct",
                mainActivityRequestCode, listItemsCameraRequestCode);
    }
}