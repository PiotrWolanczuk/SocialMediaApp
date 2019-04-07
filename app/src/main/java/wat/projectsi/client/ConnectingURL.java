package wat.projectsi.client;

public class ConnectingURL {
    //private static final String URL = "http://localhost:8080";
    private static final String AVD_URL = "http://10.0.2.2:8080";   //Android Studio
    public static final String URL_Signup = getURL() +"/api/auth/signup";
    public static final String URL_Signin = getURL() +"/api/auth/signin";

    public static String getURL()
    {
        return AVD_URL;
    }
}
