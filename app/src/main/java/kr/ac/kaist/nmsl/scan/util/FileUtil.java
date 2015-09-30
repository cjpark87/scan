package kr.ac.kaist.nmsl.scan.util;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import kr.ac.kaist.nmsl.scan.Constants;

/**
 * Created by cjpark on 2015-09-30.
 */
public class FileUtil {
    private static final String FILE_UTIL_FILE_DATE_FORMAT = "yyyyMMdd";
    private static final String FILE_UTIL_FILE_FORMAT = "%s_%s.json";
    private static final String FILE_UTIL_DATA_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private static final String FILE_UTIL_DATA_SEPARATOR = ",\n";
    private enum DATA_KEYS {
        DATE("date"), TYPE("type"), VALUE("value"), UUID("uuid");

        private String name;
        DATA_KEYS(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    public static void writeData (Context context, Date date, String sensorType, JSONObject value) {
        File uuidDir = new File(context.getFilesDir() + "/" + UUIDGenerator.getUUID(context));
        if (!uuidDir.exists() || !uuidDir.isDirectory())
            uuidDir.mkdirs();

        SimpleDateFormat fileDateFormat = new SimpleDateFormat(FILE_UTIL_FILE_DATE_FORMAT);
        String dataFilename = String.format(FILE_UTIL_FILE_FORMAT, sensorType, fileDateFormat.format(date));

        try {
            SimpleDateFormat dataDateFormat = new SimpleDateFormat(FILE_UTIL_DATA_DATE_FORMAT);

            FileOutputStream outputStream = new FileOutputStream(uuidDir.getAbsolutePath()+"/"+dataFilename, true);

            JSONObject dataJSON = new JSONObject();
            dataJSON.put(DATA_KEYS.DATE.toString(), dataDateFormat.format(date));
            dataJSON.put(DATA_KEYS.UUID.toString(), UUIDGenerator.getUUID(context));
            dataJSON.put(DATA_KEYS.TYPE.toString(), sensorType);
            dataJSON.put(DATA_KEYS.VALUE.toString(), value);

            outputStream.write((dataJSON.toString() + FILE_UTIL_DATA_SEPARATOR).getBytes());

            outputStream.close();
        } catch (Exception e) {
            Log.e(Constants.DEBUG_TAG, e.getMessage());
        }
    }
}
