package wat.projectsi;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    @Rule
    public IntentsTestRule<LoginActivity> mLoginIntentsTestRule =
            new IntentsTestRule<>(LoginActivity.class);


    @Test
    public void clickLoginButton_openMainActivity() throws IOException {
        onView(withId(R.id.email)).perform(ViewActions.clearText())
                .perform(ViewActions.typeText("example@gmail.com"));
        onView(withId(R.id.password)).perform(ViewActions.clearText())
                .perform(ViewActions.typeText("123456"));
        onView(withId(R.id.email_sign_in_button)).perform(click());

        intended(toPackage("wat.projectsi"));
        intended(hasComponent("wat.projectsi.MainActivity"));
    }

    @Test
    public void clickLoginButton_withoutEmail(){
        onView(withId(R.id.email)).perform(ViewActions.clearText())
                .perform(ViewActions.typeText(""));
        onView(withId(R.id.password)).perform(ViewActions.clearText())
                .perform(ViewActions.typeText("123456"));
        onView(withId(R.id.email_sign_in_button)).perform(click());

        onView(withId(R.id.email)).check(matches(hasErrorText("This field is required")));
    }

    @Test
    public void clickLoginButton_withoutPassword(){
        onView(withId(R.id.email)).perform(ViewActions.clearText())
                .perform(ViewActions.typeText("example@gmail.com"));
        onView(withId(R.id.password)).perform(ViewActions.clearText())
                .perform(ViewActions.typeText(""));
        onView(withId(R.id.email_sign_in_button)).perform(click());

        onView(withId(R.id.password)).check(matches(hasErrorText("This field is required")));
    }
}
