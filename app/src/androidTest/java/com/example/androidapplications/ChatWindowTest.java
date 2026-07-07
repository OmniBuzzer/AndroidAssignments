package com.example.androidapplications;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ChatWindowTest {

    @Test
    public void clicking_addsToList() {
        try (ActivityScenario<ChatWindow> scenario =
                     ActivityScenario.launch(ChatWindow.class)) {

            scenario.moveToState(Lifecycle.State.RESUMED);

            onView(withId(R.id.send_text))
                    .perform(typeText("trying to test this"), closeSoftKeyboard());

            onView(withId(R.id.send_button)).perform(click());

            onView(withText("trying to test this")).check(matches(isDisplayed()));
        }
    }
}