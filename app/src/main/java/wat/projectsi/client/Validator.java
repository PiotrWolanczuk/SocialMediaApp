package wat.projectsi.client;

import java.util.regex.Pattern;

public class Validator {
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern VALID_NAME_REGEX =
            Pattern.compile("[A-Z]+", Pattern.CASE_INSENSITIVE);

    public static boolean isEmailValid(String emailStr) {
        return VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr).matches();
    }

    public static boolean isPasswordValid(String passStr) {
        return passStr.length() > 3;
    }

    public static boolean isLoginValid(String loginStr) {
        return loginStr.length() > 3;
    }

    public static boolean isNameValid(String nameStr) {
        return VALID_NAME_REGEX.matcher(nameStr).matches();
    }
    public static boolean isPostContentValid(String str)
    {
        return str.length()<=10000;
    }
}
