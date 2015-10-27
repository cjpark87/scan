package kr.ac.kaist.nmsl.scan.util;

import android.content.Context;
import android.util.Log;

import java.util.UUID;

import kr.ac.kaist.nmsl.scan.Constants;

/**
 * Created by wns349 on 2015-09-30.
 */
public class UUIDGenerator {


    public static String getUUID(Context context){
       String uuid = SharedPreferenceUtil.getMyUUID(context);

        if(uuid == null){
            uuid = UUID.randomUUID().toString();
            Log.d(Constants.TAG, "Generated new UUID: " + uuid);
            SharedPreferenceUtil.saveMyUUID(context, uuid);
        }

        return uuid;
    }
}
