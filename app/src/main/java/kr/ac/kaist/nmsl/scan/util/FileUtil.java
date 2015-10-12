package kr.ac.kaist.nmsl.scan.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
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
    public static final String FILE_UTIL_FILE_DATE_FORMAT = "yyyyMMdd";
    public static final String FILE_UTIL_FILE_DATETIME_FORMAT = "yyyyMMdd_HHmmss";
    public static final String FILE_UTIL_DATA_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private static final String FILE_UTIL_FILE_FORMAT = "%s_%s.%s";
    private static final String FILE_SUFFIX_JSON = "json";
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

    public static void refreshDataFile (Context context, File file) {
        File uuidDir = new File(Environment.getExternalStoragePublicDirectory(Constants.TAG).getAbsolutePath() + "/" + UUIDGenerator.getUUID(context));

        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"+file.getAbsolutePath())));
    }

    public static String getDataFilename (Context context, Date date, String sensorType, String suffix) {
        File uuidDir = new File(Environment.getExternalStoragePublicDirectory(Constants.TAG).getAbsolutePath() + "/" + UUIDGenerator.getUUID(context));

        SimpleDateFormat fileDateFormat = new SimpleDateFormat(FILE_UTIL_FILE_DATETIME_FORMAT);
        String dataFilename = String.format(FILE_UTIL_FILE_FORMAT, sensorType, fileDateFormat.format(date), suffix);

        return uuidDir.getAbsolutePath()+"/"+dataFilename;
    }

    public static void writeData (Context context, Date date, String sensorType, JSONObject value) {
        File uuidDir = new File(Environment.getExternalStoragePublicDirectory(Constants.TAG).getAbsolutePath() + "/" + UUIDGenerator.getUUID(context));

        SimpleDateFormat fileDateFormat = new SimpleDateFormat(FILE_UTIL_FILE_DATE_FORMAT);
        String dataFilename = String.format(FILE_UTIL_FILE_FORMAT, sensorType, fileDateFormat.format(date),FILE_SUFFIX_JSON);

        try {
            SimpleDateFormat dataDateFormat = new SimpleDateFormat(FILE_UTIL_DATA_DATE_FORMAT);

            File dataFile = new File(uuidDir.getAbsolutePath() + "/" + dataFilename);
            FileOutputStream outputStream = new FileOutputStream(dataFile, true);

            JSONObject dataJSON = new JSONObject();
            dataJSON.put(DATA_KEYS.DATE.toString(), dataDateFormat.format(date));
            dataJSON.put(DATA_KEYS.UUID.toString(), UUIDGenerator.getUUID(context));
            dataJSON.put(DATA_KEYS.TYPE.toString(), sensorType);
            dataJSON.put(DATA_KEYS.VALUE.toString(), value);

            outputStream.write((dataJSON.toString() + FILE_UTIL_DATA_SEPARATOR).getBytes());

            outputStream.close();

            refreshDataFile(context, dataFile);

            Log.d(Constants.DEBUG_TAG, dataJSON.toString());
        } catch (Exception e) {
            Log.e(Constants.DEBUG_TAG, e.getMessage());
        }
    }
}
