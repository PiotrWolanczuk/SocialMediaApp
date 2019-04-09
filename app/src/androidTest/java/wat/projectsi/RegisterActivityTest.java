package wat.projectsi;

import android.content.Intent;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import wat.projectsi.client.activity.RegisterActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class RegisterActivityTest {

    @Rule
    public ActivityTestRule rule = new ActivityTestRule(RegisterActivity.class, true, false);

//    @Test
//    public void clickRegisterButton_Good()
//    {
//        rule.launchActivity(new Intent());
//
//        onView(withId(R.id.name)).perform(typeText("Anna"));
//        onView(withId(R.id.surname)).perform(typeText("Annowska"));
//        onView(withId(R.id.login)).perform(typeText("Anna12"));
//        onView(withId(R.id.email)).perform(typeText("anna@annowska.com"));
//        onView(withId(R.id.password)).perform(scrollTo()).perform(clearText())
//              .perform(typeText("password"));
//        onView(withId(R.id.re_password)).perform(scrollTo()).perform(scrollTo()).perform(clearText())
//              .perform(typeText("password"));
//        onView(withId(R.id.acceptTerms)).perform(scrollTo()).perform(click());
//
//        onView(withId(R.id.registerButton)).perform(scrollTo()).perform(click());
//
////        Assert.assertTrue();
//
//
//    }

    @Test
    public void clickRegisterButton_BadName()
    {
        rule.launchActivity(new Intent());

        onView(withId(R.id.name)).perform(typeText("Anna12"));
        onView(withId(R.id.surname)).perform(typeText("Annowska"));
        onView(withId(R.id.login)).perform(typeText("Anna12"));
        onView(withId(R.id.email)).perform(typeText("anna@annowska.com"));
        onView(withId(R.id.password)).perform(scrollTo()).perform(clearText())
                .perform(typeText("password"));
        onView(withId(R.id.re_password)).perform(scrollTo()).perform(clearText())
                .perform(typeText("password"));
        onView(withId(R.id.acceptTerms)).perform(scrollTo()).perform(click());

        onView(withId(R.id.registerButton)).perform(scrollTo()).perform(click());

        onView(withId(R.id.name)).check(matches(hasErrorText( rule.getActivity().getString (R.string.error_invalid_characters))));

        rule.finishActivity();

    }

    @Test
    public void clickRegisterButton_BadSurname()
    {
        rule.launchActivity(new Intent());

        onView(withId(R.id.name)).perform(typeText("Anna"));
        onView(withId(R.id.surname)).perform(typeText("Annowska43"));
        onView(withId(R.id.login)).perform(typeText("Anna12"));
        onView(withId(R.id.email)).perform(typeText("anna@annowska.com"));
        onView(withId(R.id.password)).perform(scrollTo()).perform(clearText())
                .perform(typeText("password"));
        onView(withId(R.id.re_password)).perform(scrollTo()).perform(clearText())
                .perform(typeText("password"));
        onView(withId(R.id.acceptTerms)).perform(scrollTo()).perform(click());

        onView(withId(R.id.registerButton)).perform(scrollTo()).perform(click());

        onView(withId(R.id.surname)).check(matches(hasErrorText( rule.getActivity().getString (R.string.error_invalid_characters))));

        rule.finishActivity();

    }

    @Test
    public void clickRegisterButton_BadLogin()
    {
        rule.launchActivity(new Intent());

        onView(withId(R.id.name)).perform(typeText("Anna"));
        onView(withId(R.id.surname)).perform(typeText("Annowska"));
        onView(withId(R.id.login)).perform(typeText("An"));
        onView(withId(R.id.email)).perform(typeText("anna@"));
        onView(withId(R.id.password)).perform(scrollTo()).perform(clearText())
                .perform(typeText("password"));
        onView(withId(R.id.re_password)).perform(scrollTo()).perform(clearText())
                .perform(typeText("password"));
        onView(withId(R.id.acceptTerms)).perform(scrollTo()).perform(click());

        onView(withId(R.id.registerButton)).perform(scrollTo()).perform(click());

        onView(withId(R.id.login)).check(matches(hasErrorText( rule.getActivity().getString (R.string.error_invalid_login))));

        rule.finishActivity();

    }

    @Test
    public void clickRegisterButton_BadPassword()
    {
        rule.launchActivity(new Intent());

        onView(withId(R.id.name)).perform(typeText("Anna"));
        onView(withId(R.id.surname)).perform(typeText("Annowska"));
        onView(withId(R.id.login)).perform(typeText("Anna12"));
        onView(withId(R.id.email)).perform(typeText("anna@annowska.com"));
        onView(withId(R.id.password)).perform(scrollTo()).perform(clearText())
                .perform(typeText("p"));
        onView(withId(R.id.re_password)).perform(scrollTo()).perform(clearText())
                .perform(typeText("p"));
        onView(withId(R.id.acceptTerms)).perform(scrollTo()).perform(click());

        onView(withId(R.id.registerButton)).perform(scrollTo()).perform(click());

        onView(withId(R.id.password)).check(matches(hasErrorText( rule.getActivity().getString (R.string.error_invalid_password))));

        rule.finishActivity();

    }

    @Test
    public void clickRegisterButton_BadRePassword()
    {
        rule.launchActivity(new Intent());

        onView(withId(R.id.name)).perform(typeText("Anna"));
        onView(withId(R.id.surname)).perform(typeText("Annowska"));
        onView(withId(R.id.login)).perform(typeText("Anna12"));
        onView(withId(R.id.email)).perform(typeText("anna@annowska.com"));
        onView(withId(R.id.password)).perform(scrollTo()).perform(clearText())
                .perform(typeText("password"));
        onView(withId(R.id.re_password)).perform(scrollTo()).perform(clearText())
                .perform(typeText("passwo"));
        onView(withId(R.id.acceptTerms)).perform(scrollTo()).perform(click());

        onView(withId(R.id.registerButton)).perform(scrollTo()).perform(click());

        onView(withId(R.id.re_password)).check(matches(hasErrorText( rule.getActivity().getString (R.string.error_repassword_password_not_same))));

        rule.finishActivity();

    }

    @Test
    public void clickRegisterButton_BadEmail()
    {
        rule.launchActivity(new Intent());

        onView(withId(R.id.name)).perform(typeText("Anna"));
        onView(withId(R.id.surname)).perform(typeText("Annowska"));
        onView(withId(R.id.login)).perform(typeText("Anna12"));
        onView(withId(R.id.email)).perform(typeText("anna@"));
        onView(withId(R.id.password)).perform(scrollTo()).perform(clearText())
                .perform(typeText("password"));
        onView(withId(R.id.re_password)).perform(scrollTo()).perform(clearText())
                .perform(typeText("password"));
        onView(withId(R.id.acceptTerms)).perform(scrollTo()).perform(click());

        onView(withId(R.id.registerButton)).perform(scrollTo()).perform(click());

        onView(withId(R.id.email)).check(matches(hasErrorText( rule.getActivity().getString (R.string.error_invalid_email))));

        rule.finishActivity();

    }

    @Test
    public void clickRegisterButton_NotCheckedTerms()
    {
        rule.launchActivity(new Intent());

        onView(withId(R.id.name)).perform(typeText("Anna"));
        onView(withId(R.id.surname)).perform(typeText("Annowska"));
        onView(withId(R.id.login)).perform(typeText("Anna12"));
        onView(withId(R.id.email)).perform(typeText("anna@annowska.com"));
        onView(withId(R.id.password)).perform(scrollTo()).perform(clearText())
                .perform(typeText("password"));
        onView(withId(R.id.re_password)).perform(scrollTo()).perform(clearText())
                .perform(typeText("password"));

        onView(withId(R.id.registerButton)).perform(scrollTo()).perform(click());

        onView(withId(R.id.acceptTerms)).check(matches(isNotChecked()));
//        onView(withId(R.id.acceptTerms)).check(matches(hasErrorText( rule.getActivity().getString (R.string.error_not_accepted_terms))));

        rule.finishActivity();

    }

    @Test
    public void clickRegisterButton_EmptyEmail()
    {
        rule.launchActivity(new Intent());

        onView(withId(R.id.name)).perform(typeText("Anna"));
        onView(withId(R.id.surname)).perform(typeText("Annowska"));
        onView(withId(R.id.login)).perform(typeText("Anna12"));
        onView(withId(R.id.email)).perform(typeText(""));
        onView(withId(R.id.password)).perform(scrollTo()).perform(clearText())
                .perform(typeText("password"));
        onView(withId(R.id.re_password)).perform(scrollTo()).perform(clearText())
                .perform(typeText("password"));
        onView(withId(R.id.acceptTerms)).perform(scrollTo()).perform(click());

        onView(withId(R.id.registerButton)).perform(scrollTo()).perform(click());

        onView(withId(R.id.email)).check(matches(hasErrorText( rule.getActivity().getString (R.string.error_field_required))));

        rule.finishActivity();

    }

    @Test
    public void clickRegisterButton_EmptyPassword()
    {
        rule.launchActivity(new Intent());

        onView(withId(R.id.name)).perform(typeText("Anna"));
        onView(withId(R.id.surname)).perform(typeText("Annowska"));
        onView(withId(R.id.login)).perform(typeText("Anna12"));
        onView(withId(R.id.email)).perform(typeText("anna@annowska.com"));
        onView(withId(R.id.password)).perform(scrollTo()).perform(clearText())
                .perform(typeText(""));
        onView(withId(R.id.re_password)).perform(scrollTo()).perform(clearText())
                .perform(typeText(""));
        onView(withId(R.id.acceptTerms)).perform(scrollTo()).perform(click());

        onView(withId(R.id.registerButton)).perform(scrollTo()).perform(click());

        onView(withId(R.id.password)).check(matches(hasErrorText( rule.getActivity().getString (R.string.error_field_required))));

        rule.finishActivity();
    }

    @Test
    public void clickRegisterButton_EmptyLogin()
    {
        rule.launchActivity(new Intent());

        onView(withId(R.id.name)).perform(typeText("Anna"));
        onView(withId(R.id.surname)).perform(typeText("Annowska"));
        onView(withId(R.id.login)).perform(typeText(""));
        onView(withId(R.id.email)).perform(typeText("anna@annowska.com"));
        onView(withId(R.id.password)).perform(scrollTo()).perform(clearText())
                .perform(typeText("password"));
        onView(withId(R.id.re_password)).perform(scrollTo()).perform(clearText())
                .perform(typeText("password"));
        onView(withId(R.id.acceptTerms)).perform(scrollTo()).perform(click());

        onView(withId(R.id.registerButton)).perform(scrollTo()).perform(click());

        onView(withId(R.id.login)).check(matches(hasErrorText( rule.getActivity().getString (R.string.error_field_required))));

        rule.finishActivity();
    }

}
