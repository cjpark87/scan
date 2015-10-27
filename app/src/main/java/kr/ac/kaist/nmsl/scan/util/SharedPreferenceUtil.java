package kr.ac.kaist.nmsl.scan.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by wns349 on 2015-10-27.
 */
public class SharedPreferenceUtil {
    private static final String SHARED_PREFERENCE_NAME="kr.ac.kaist.nmsl.scan";
    private static final String KEY_MY_UUID = "MyUUID";
    private static final String KEY_WHITE_LISTED_APPS = "WhiteListedApps";

    public static String getMyUUID(Context context){
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String uuid = prefs.getString(KEY_MY_UUID, null);

        return uuid;
    }

    public static void saveMyUUID(Context context, String uuid){
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_MY_UUID, uuid);
        editor.commit();
    }

    public static Set<String> getWhiteListedPackages(Context context){
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        Set<String> packages = prefs.getStringSet(KEY_WHITE_LISTED_APPS, new HashSet<String>());

        return packages;
    }

    public static void saveWhiteListedPackages(Context context, Set<String> whiteListedPackages){
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet(KEY_WHITE_LISTED_APPS, whiteListedPackages);
        editor.commit();
    }
}
