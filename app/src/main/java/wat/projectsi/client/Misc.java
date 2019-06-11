package wat.projectsi.client;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import wat.projectsi.R;


public class Misc {
    public static final long refreshTime = 10000 ;
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
