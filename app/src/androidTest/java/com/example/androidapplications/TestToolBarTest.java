package com.example.androidapplications;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class TestToolBarTest {

    @Test
    public void threeVisibleIcons() {
        ActivityScenario.launch(TestToolBar.class);

        onView(withId(R.id.action_one)).check(matches(isDisplayed()));
        onView(withId(R.id.action_two)).check(matches(isDisplayed()));
        onView(withId(R.id.action_three)).check(matches(isDisplayed()));
    }

    @Test
    public void secondIconBackButton() {
        ActivityScenario.launch(TestToolBar.class);

        onView(withId(R.id.action_two)).perform(click());

        onView(withText(R.string.dialog_back_title_page))
                .check(matches(isDisplayed()));
    }

    @Test
    public void lastIconNewMessage() {
        ActivityScenario.launch(TestToolBar.class);

        onView(withId(R.id.action_three)).perform(click());

        onView(withId(R.id.dialog_edit_message))
                .perform(replaceText("testing hello 123"), closeSoftKeyboard());

        onView(withText(R.string.ok)).perform(click());

        onView(withId(R.id.action_one)).perform(click());

        onView(withText("testing hello 123"))
                .check(matches(isDisplayed()));
    }

}