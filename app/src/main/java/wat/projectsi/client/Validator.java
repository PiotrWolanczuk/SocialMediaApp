package wat.projectsi.client;

import java.text.DateFormat;
import java.util.regex.Pattern;

public class Validator {
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern VALID_NAME_REGEX =
            Pattern.compile("[A-Z]+", Pattern.CASE_INSENSITIVE);

    public static final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
    public static final long maxDate = System.currentTimeMillis();
    public static final long minDate = System.currentTimeMillis() - 1000L * 360L * 24L * 365L * 3600L;

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
}
