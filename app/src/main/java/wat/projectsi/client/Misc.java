package wat.projectsi.client;

import android.content.Context;

import java.util.HashMap;

public class Misc {
    public static final long refreshTime = 1000 ;

    public static HashMap<String, String> getSecureHeaders(Context context){
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization","Bearer "+ SharedOurPreferences.getDefaults("token",context));

        return headers;
    }

}
