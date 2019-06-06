package wat.projectsi.client;

import android.os.Build;

public class ConnectingURL {
    //private static final String URL = "http://localhost:8080";
    private static final String AVD_URL = "http://10.0.2.2:8080";       //Android Studio
    private static final String GEN_URL = "http://10.0.3.2:8080";       //Genymotion
    private static final String VBX_URL = "http://192.168.56.1:8080";   //VirtualBox Studio
    private static final String NOX_URL= "http://172.17.100.2:8080";    //NOX
    private static final String DOC_URL = "http://10.0.0.42:8080";      //Docker    //TODO: check if is

    public static final String URL_Signup = getURL() +"/signup";
    public static final String URL_Signin = getURL() +"/signin";
    public static final String URL_Posts = getURL() +"/posts";
    public static final String URL_Notifications_Acquaintance = getURL() +"/notifications/acquaintances";
    public static final String URL_Notifications_Messages = getURL() +"/notifications/messages";
    public static final String URL_Notifications_Post = getURL() +"/notifications/posts";
    public static final String URL_Acquaintances = getURL() +"/acquaintances";
    public static final String URL_Users = getURL()+"/users";
    public static final String URL_Users_ByName = getURL()+"/users/byname";
    public static final String URL_Users_Current = getURL()+"/users/current";
    public static final String URL_Users_Profile = getURL()+"/users/profile";
    public static final String URL_Violations = getURL()+"/violations";
    public static final String URL_ViolationsComments = getURL()+"/violations/comments";
    public static final String URL_ViolationsPosts = getURL()+"/violations/posts";
    public static final String URL_Comments=getURL()+ "/comments";

    public static String getURL()
    {
        if(isGenymotion())
            return GEN_URL;
        else if(isVirtualBox())
            return VBX_URL;
        else if(isNox())
            return NOX_URL;
        else if(isOtherEmulator())
            return AVD_URL;
        else return DOC_URL;
        //TODO: return on real devices
    }

    private static boolean isGenymotion()
    {
        return  Build.MANUFACTURER.contains("Genymotion");
    }

    private static boolean isVirtualBox() { return Build.DEVICE.contains("vbox"); }

    private static boolean isNox() { return Build.DEVICE.contains("greatlteks"); }

    private static boolean isOtherEmulator()
    {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }
}
