package kr.ac.kaist.nmsl.scan.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.UUID;

import kr.ac.kaist.nmsl.scan.Constants;

/**
 * Created by wns349 on 2015-09-30.
 */
public class UUIDGenerator {
    private static final String SHARED_PREFERENCE_NAME="kr.ac.kaist.nmsl.scan";
    private static final String KEY_MY_UUID = "MyUUID";

    public static String getUUID(Context context){
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String uuid = prefs.getString(KEY_MY_UUID, null);

        if(uuid == null){
            uuid = UUID.randomUUID().toString();
            Log.d(Constants.TAG, "Generated new UUID: " + uuid);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(KEY_MY_UUID, uuid);
            editor.commit();
        }

        return uuid;
    }
}
