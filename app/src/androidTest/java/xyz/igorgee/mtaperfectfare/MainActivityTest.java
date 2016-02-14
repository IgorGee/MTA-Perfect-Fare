package xyz.igorgee.mtaperfectfare;


import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public final ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class);

    Intent goToResults;

    @Before
    public void setUp() {
        goToResults = new Intent(main.getActivity(), ResultsActivity.class);
        goToResults.putExtra(MainActivity.EXTRA_TRIPS, 1);
        goToResults.putExtra(MainActivity.EXTRA_DAYS, 1);
        goToResults.putExtra(MainActivity.EXTRA_WEEKS, 1);
        goToResults.putExtra(MainActivity.EXTRA_AMOUNT_IN_CARD, 1);
    }

    @Test
    public void shouldDisplayAllViews() {
        onView(withText("Starting Balance")).check(matches(isDisplayed()));
        onView(withText("Swipes")).check(matches(isDisplayed()));
        onView(withText("Days")).check(matches(isDisplayed()));
        onView(withText("Weeks")).check(matches(isDisplayed()));
        onView(withId(R.id.starting_balance)).check(matches(isDisplayed()));
        onView(withId(R.id.trips)).check(matches(isDisplayed()));
        onView(withId(R.id.days)).check(matches(isDisplayed()));
        onView(withId(R.id.weeks)).check(matches(isDisplayed()));
    }

    @Test
    public void shouldForbidAccessTo_ResultsActivity_WithInvalidEntriesAndDisplay_Snackbar() {
        // All Fields Empty
        onView(withId(R.id.fabCalculate)).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText("Please enter a value for all fields.")))
                .check(matches(isDisplayed()));
        onView(withId(android.support.design.R.id.snackbar_action)).perform(click());

        // 1/4 Valid
        onView(withId(R.id.starting_balance)).perform(typeText("1"));
        onView(withId(R.id.fabCalculate)).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText("Please enter a value for all fields.")))
                .check(matches(isDisplayed()));
        onView(withId(android.support.design.R.id.snackbar_action)).perform(click());

        // 2/4 Valid
        onView(withId(R.id.trips)).perform(typeText("1"));
        onView(withId(R.id.fabCalculate)).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText("Please enter a value for all fields.")))
                .check(matches(isDisplayed()));
        onView(withId(android.support.design.R.id.snackbar_action)).perform(click());

        // 3/4 Valid
        onView(withId(R.id.days)).perform(typeText("1"));
        onView(withId(R.id.fabCalculate)).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText("Please enter a value for all fields.")))
                .check(matches(isDisplayed()));
        onView(withId(android.support.design.R.id.snackbar_action)).perform(click());

        // 4/4 Valid
        onView(withId(R.id.weeks)).perform(typeText("1"));
        onView(withId(R.id.fabCalculate)).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText("Please enter a value for all fields.")))
                .check(doesNotExist());
    }
}
