package wat.projectsi.client;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.util.HashMap;

import wat.projectsi.R;


public class Misc {
    public static final long refreshTime = 10000;
    public static final int MY_SOCKET_TIMEOUT_MS=(int)(refreshTime/2L);

    public static final String manStr = "man";
    public static final String womanStr = "woman";
    public static final String roleAdminStr = "ROLE_ADMIN";
    public static final String roleUserStr = "ROLE_USER";
    public static final String preferenceTokenStr = "token";
    public static final String preferenceRoleStr = "role";
    public static final String preferenceLanguageStr = "language";
    public static final String preferenceUserChangeStr = "userChanged";

    public static final String[] suportedLocaleCodes={"pl", "en"};

    public static final Bitmap defaultAvatar;
    static{
        Bitmap bitmap=null;
        try {
           bitmap = BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.default_avatar);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            defaultAvatar=bitmap;
        }
    }

    public static HashMap<String, String> getSecureHeaders(Context context){
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization","Bearer "+ SharedOurPreferences.getDefaults("token",context));

        return headers;
    }

    private static Application getApplication() throws Exception {
        return (Application) Class.forName("android.app.ActivityThread")
                .getMethod("currentApplication").invoke(null, (Object[]) null);
    }


}
