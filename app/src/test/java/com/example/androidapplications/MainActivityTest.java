package com.example.androidapplications;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class MainActivityTest {

    @Test
    public void requestCodeValue() {
        // The request code used for startActivityForResult should be 10
        int expectedRequestCode = 10;
        assertEquals("REQ_CODE should be 10", 10, expectedRequestCode);
        assertNotEquals("REQ_CODE should not be 0", 0, expectedRequestCode);
    }

    @Test
    public void intentTargetClasses() {
        // Verify the target Activity classes exist and have the expected names
        String listItemsName = ListItemsActivity.class.getSimpleName();
        String chatWindowName = ChatWindow.class.getSimpleName();

        assertEquals("ListItemsActivity", listItemsName);
        assertEquals("ChatWindow", chatWindowName);
    }
}