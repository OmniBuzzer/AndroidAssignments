package com.example.androidapplications;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;

public class ChatWindowTest {

    @Test
    public void msgListEmpty() {
        ArrayList<String> messages = new ArrayList<>();
        assertEquals("New message list should be size 0", 0, messages.size());
        assertTrue("New message list should be empty", messages.isEmpty());
    }

    @Test
    public void msgListMultipleMessagesOrder() {

        ArrayList<String> messages = new ArrayList<>();
        messages.add("One1");
        messages.add("Two2");
        messages.add("Three3");

        assertEquals(3, messages.size());
        assertEquals("One1", messages.get(0));
        assertEquals("Two2", messages.get(1));
        assertEquals("Three3", messages.get(2));
    }

    @Test
    public void msgListSize() {
        ArrayList<String> messages = new ArrayList<>();
        assertEquals(0, messages.size());

        messages.add("test");
        messages.add("test2");
        assertEquals("should list size", 2, messages.size());
    }

}