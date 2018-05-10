package com.ragazm.bakingapp.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Andris on 010 10.05.18.
 *
 * https://github.com/googlesamples/android-testing/tree/master/ui/espresso/IdlingResourceSample/app/src
 */



    @RunWith(AndroidJUnit4.class)
public class RecyclerViewClickTest {

        @Rule
        public ActivityTestRule<MainActivity> activityTestRule =
                new ActivityTestRule<>(MainActivity.class);


    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = activityTestRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);
    }

        @Test
        public void clickOnRecyclerViewItem(){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            onView(withId(R.id.recipe_list_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));

            onView(withId(R.id.ingredients_container)).check(matches(isDisplayed()));

            onView(withId(R.id.recipe_list))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(4, click()));


        }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }




    }

