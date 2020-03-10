package ch.epfl.qedit;

import android.content.Intent;
import android.os.Bundle;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.qedit.model.User;
import ch.epfl.qedit.view.LoginActivity;
import ch.epfl.qedit.view.ViewRoleActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4ClassRunner.class)
public class ViewRoleActivityTest {
    @Rule
    public final IntentsTestRule<ViewRoleActivity> testRule = new IntentsTestRule<>(ViewRoleActivity.class, false, false);

    public void launchActivity(User user) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(LoginActivity.USER, user);
        intent.putExtras(bundle);
        testRule.launchActivity(intent);
    }

    public void finishActivity() {
        testRule.finishActivity();
    }

    public void testUserIsDisplayedCorrectly(User user, String greeting, String role) {
        launchActivity(user);
        onView(withId(R.id.greeting)).check(matches(withText(greeting)));
        onView(withId(R.id.role)).check(matches(withText(role)));
        finishActivity();
    }

    @Test
    public void testParticipantIsDisplayedCorrectly() {
        testUserIsDisplayedCorrectly(new User("Bill", "Gates", User.Role.Participant), "Bienvenue Bill Gates !", "Vous êtes un participant.");
    }

    @Test
    public void testEditorIsDisplayedCorrectly() {
        testUserIsDisplayedCorrectly(new User("John", "Cena", User.Role.Editor), "Bienvenue John Cena !", "Vous êtes un éditeur.");
    }

    @Test
    public void testAdministratorIsDisplayedCorrectly() {
        testUserIsDisplayedCorrectly(new User("The", "Rock", User.Role.Administrator), "Bienvenue The Rock !", "Vous êtes un administrateur.");
    }
}
