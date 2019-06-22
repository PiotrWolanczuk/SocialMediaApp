package wat.projectsi.client;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Map;
import java.util.Set;

public class SharedOurPreferences implements android.content.SharedPreferences {
    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
        if(key.equals(Misc.preferenceUserChangeStr))
            removeDefaults(key, context);
    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    public static void removeDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.commit();
    }

    public static void clear(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(Misc.preferenceRoleStr);
        editor.remove(Misc.preferenceTokenStr);;
        editor.commit();
    }

    @Override
    public Map<String, ?> getAll() {
        return null;
    }


    @Override
    public String getString(String key,String defValue) {
        return null;
    }


    @Override
    public Set<String> getStringSet(String key, Set<String> defValues) {
        return null;
    }

    @Override
    public int getInt(String key, int defValue) {
        return 0;
    }

    @Override
    public long getLong(String key, long defValue) {
        return 0;
    }

    @Override
    public float getFloat(String key, float defValue) {
        return 0;
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        return false;
    }

    @Override
    public boolean contains(String key) {
        return false;
    }

    @Override
    public Editor edit() {
        return null;
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {

    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {

    }
}
