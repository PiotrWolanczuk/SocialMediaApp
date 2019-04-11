package wat.projectsi;

import org.junit.Test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import wat.projectsi.client.Validator;


@RunWith(JUnit4.class)
public class ValidatorTest {
    @Test
    public void emailValidatorBad() {
        Assert.assertFalse(Validator.isEmailValid("aaa"));
    }

    @Test
    public void emailValidatorGood() {
        Assert.assertTrue(Validator.isEmailValid("aaa@qqq.co"));
    }

    @Test
    public void nameValidatorBad() { Assert.assertFalse(Validator.isNameValid("12abc")); }

    @Test
    public void nameValidatorGood() {
        Assert.assertTrue(Validator.isNameValid("Karol"));
    }

    @Test
    public void passwordValidatorBad() { Assert.assertFalse(Validator.isPasswordValid("12"));  }

    @Test
    public void passwordValidatorGood() {
        Assert.assertTrue(Validator.isPasswordValid("12345"));
    }

    @Test
    public void loginValidatorBad() {
        Assert.assertFalse(Validator.isLoginValid("a"));
    }
    @Test
    public void loginValidatorGood() {
        Assert.assertTrue(Validator.isLoginValid("Karol"));
    }

}
