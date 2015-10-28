package kr.ac.kaist.nmsl.scan;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import kr.ac.kaist.nmsl.scan.gps.GPSService;
import kr.ac.kaist.nmsl.scan.mic.MicrophoneService;
import kr.ac.kaist.nmsl.scan.notification.NotificationListener;
import kr.ac.kaist.nmsl.scan.sensor.AccelerometerService;
import kr.ac.kaist.nmsl.scan.sensor.GravityService;
import kr.ac.kaist.nmsl.scan.sensor.GyroscopeService;
import kr.ac.kaist.nmsl.scan.sensor.HumidityService;
import kr.ac.kaist.nmsl.scan.sensor.LightService;
import kr.ac.kaist.nmsl.scan.sensor.MagneticFieldService;
import kr.ac.kaist.nmsl.scan.sensor.OrientationService;
import kr.ac.kaist.nmsl.scan.sensor.PressureService;
import kr.ac.kaist.nmsl.scan.sensor.ProximityService;
import kr.ac.kaist.nmsl.scan.sensor.RotationVectorService;
import kr.ac.kaist.nmsl.scan.sensor.SignificantMotionService;
import kr.ac.kaist.nmsl.scan.sensor.StepDectionService;
import kr.ac.kaist.nmsl.scan.sensor.TemperatureService;
import kr.ac.kaist.nmsl.scan.util.FileUtil;
import kr.ac.kaist.nmsl.scan.util.ServiceUtil;
import kr.ac.kaist.nmsl.scan.util.SharedPreferenceUtil;
import kr.ac.kaist.nmsl.scan.util.UUIDGenerator;

public class MainActivity extends Activity {
    private final List<ServiceBean> services = new ArrayList<ServiceBean>();
    private ServiceListAdapter listAdapter;

    private Object lock = new Object();
    private boolean isTurnedOn = false;

    private static final int ACTIVITY_RESULT_NOTIFICATION_LISTENER_SETTINGS = 142;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize SCAN folder
        File scanDir = new File(Environment.getExternalStoragePublicDirectory(Constants.TAG).getAbsolutePath());
        if (!scanDir.exists() || !scanDir.isDirectory()) {
            scanDir.mkdirs();
        }

        File uuidDir = new File(scanDir.getAbsolutePath() + "/" + UUIDGenerator.getUUID(this));
        if (!uuidDir.exists() || !uuidDir.isDirectory()) {
            uuidDir.mkdirs();
        }

        // Add services here
        services.add(new ServiceBean(GPSService.class.getSimpleName(), GPSService.class));
        services.add(new ServiceBean(AccelerometerService.class.getSimpleName(), AccelerometerService.class));
        services.add(new ServiceBean(GravityService.class.getSimpleName(), GravityService.class));
        services.add(new ServiceBean(GyroscopeService.class.getSimpleName(), GyroscopeService.class));
        services.add(new ServiceBean(RotationVectorService.class.getSimpleName(), RotationVectorService.class));
        services.add(new ServiceBean(SignificantMotionService.class.getSimpleName(), SignificantMotionService.class));
        services.add(new ServiceBean(StepDectionService.class.getSimpleName(), StepDectionService.class));
        services.add(new ServiceBean(MagneticFieldService.class.getSimpleName(), MagneticFieldService.class));
        services.add(new ServiceBean(OrientationService.class.getSimpleName(), OrientationService.class));
        services.add(new ServiceBean(ProximityService.class.getSimpleName(), ProximityService.class));
        services.add(new ServiceBean(LightService.class.getSimpleName(), LightService.class));
        services.add(new ServiceBean(PressureService.class.getSimpleName(), PressureService.class));
        services.add(new ServiceBean(TemperatureService.class.getSimpleName(), TemperatureService.class));
        services.add(new ServiceBean(HumidityService.class.getSimpleName(), HumidityService.class));
        services.add(new ServiceBean(MicrophoneService.class.getSimpleName(), MicrophoneService.class));

        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));

        ListView listServices = (ListView) findViewById(R.id.list_services);
        listAdapter = new ServiceListAdapter(this, services);
        listServices.setAdapter(listAdapter);

        listAdapter.notifyDataSetChanged();

        // Initialize annotate button
        initializeAnnotateButton();

        // Initialize switch
        initializeSwitchAll();

        // Initialize UUID
        initializeUUID();

        // Initialize Notification Listener ToggleButton
        initializeNotificationListenerButton();

        // Initialize apps button
        initializeAppsButton();

        // Initialize fake button
        initializeFakePushButton();
    }

    private void initializeFakePushButton() {
        Button btnFake = (Button) findViewById(R.id.btn_fake_push);
        final Context context = this;
        btnFake.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        NotificationCompat.Builder notiBuilder =
                                new NotificationCompat.Builder(context)
                                        .setSmallIcon(android.R.drawable.ic_dialog_alert)
                                        .setContentTitle("Push Test" + System.currentTimeMillis())
                                        .setContentText(System.currentTimeMillis() + "")
                                        .setOngoing(false)
                                        .setLights(Color.RED, 3000, 3000)
                                        .setVibrate(new long[]{1000, 1000, 1000});

                        // Gets an instance of the NotificationManager service
                        NotificationManager notificationManager =
                                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                        Random rnd = (new Random());
                        // Builds the notification and issues it.
                        notificationManager.notify(rnd.nextInt(10), notiBuilder.build());
                    }
                }, 3000);

            }
        });

    }

    private void initializeAppsButton() {
        Button btnApps = (Button) findViewById(R.id.btn_apps);
        final Context context = this;
        btnApps.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
                final List<AppBean> apps = new ArrayList<AppBean>(packs.size());
                for (int i = 0; i < packs.size(); i++) {
                    PackageInfo p = packs.get(i);
                    String appName = p.applicationInfo.loadLabel(getPackageManager()).toString();
                    apps.add(new AppBean(appName, p.packageName, isWhiteListed(p.packageName)));
                }

                final Dialog dialogApp = new Dialog(context);
                dialogApp.setContentView(R.layout.dialog_apps);

                // Listview
                Collections.sort(apps, new Comparator<AppBean>() {
                    @Override
                    public int compare(AppBean lhs, AppBean rhs) {
                        return lhs.getAppName().compareTo(rhs.getAppName());
                    }
                });
                final AppListAdapter adapter = new AppListAdapter(context, apps);
                ListView listApps = (ListView) dialogApp.findViewById(R.id.list_apps);
                listApps.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                // Buttons
                final Button btnSave = (Button) dialogApp.findViewById(R.id.btn_apps_save);
                btnSave.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Save to shared preference
                        Set<String> packages = new HashSet<String>();
                        for (AppBean appBean : apps) {
                            if (appBean.isWhiteListed()) {
                                packages.add(appBean.getPackageName());
                            }
                        }

                        SharedPreferenceUtil.saveWhiteListedPackages(context, packages);

                        dialogApp.dismiss();
                    }
                });

                final Button btnCancel = (Button) dialogApp.findViewById(R.id.btn_apps_cancel);
                btnCancel.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogApp.dismiss();
                    }
                });

                final Button btnCheckAll = (Button) dialogApp.findViewById(R.id.btn_apps_check_all);
                btnCheckAll.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (AppBean appBean : apps) {
                            appBean.setWhiteListed(true);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

                final Button btnUncheckAll = (Button) dialogApp.findViewById(R.id.btn_apps_uncheck_all);
                btnUncheckAll.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (AppBean appBean : apps) {
                            appBean.setWhiteListed(false);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

                dialogApp.show();
            }
        });
    }

    private boolean isWhiteListed(String packageName) {
        Set<String> whiteListedPackages = SharedPreferenceUtil.getWhiteListedPackages(this);
        return whiteListedPackages.contains(packageName);
    }

    private void initializeNotificationListenerButton() {
        final ToggleButton btnNotificationListener = (ToggleButton) findViewById(R.id.btn_notification_listener_service);
        updateNotificationListenerButton();

        btnNotificationListener.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingIntent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                startActivityForResult(settingIntent, ACTIVITY_RESULT_NOTIFICATION_LISTENER_SETTINGS);
            }
        });
    }

    private void updateNotificationListenerButton() {
        final ToggleButton btnNotificationListener = (ToggleButton) findViewById(R.id.btn_notification_listener_service);
        ComponentName cn = new ComponentName(this, NotificationListener.class);
        String flat = Settings.Secure.getString(this.getContentResolver(), "enabled_notification_listeners");
        final boolean enabled = flat != null && flat.contains(cn.flattenToString());

        btnNotificationListener.setChecked(enabled);
    }

    private boolean isAllServiceRunning() {
        for (ServiceBean serviceBean : this.services) {
            if (!ServiceUtil.isServiceRunning(this, serviceBean.getServiceClass())) {
                return false;
            }
        }
        return true;
    }

    private void initializeUUID() {
        TextView txtUUID = (TextView) findViewById(R.id.txt_uuid);
        txtUUID.setText(UUIDGenerator.getUUID(this));
    }

    private void initializeSwitchAll() {
        final Context context = this;
        Switch switchAll = (Switch) findViewById(R.id.switch_all_service);
        switchAll.setChecked(isAllServiceRunning());

        switchAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Start all services
                    for (ServiceBean serviceBean : services) {
                        Log.d(Constants.TAG, "Starting service: " + serviceBean.getServiceName());
                        context.startService(new Intent(context, serviceBean.getServiceClass()));
                    }
                } else {
                    // Stop all services
                    for (ServiceBean serviceBean : services) {
                        Log.d(Constants.TAG, "Stopping service: " + serviceBean.getServiceName());
                        context.stopService(new Intent(context, serviceBean.getServiceClass()));
                    }
                }

                listAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initializeAnnotateButton() {
        final Context context = this;
        Button btnAnnotate = (Button) findViewById(R.id.btn_annotate);
        btnAnnotate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialogAnnotate = new Dialog(context);
                dialogAnnotate.setContentView(R.layout.dialog_annotate);
                dialogAnnotate.setTitle(R.string.annotate);

                final EditText edtAnnotate = (EditText) dialogAnnotate.findViewById(R.id.edt_annotate);

                // Set date
                final EditText edtDate = (EditText) dialogAnnotate.findViewById(R.id.edt_date);
                final SimpleDateFormat sdf = new SimpleDateFormat(FileUtil.FILE_UTIL_DATA_DATE_FORMAT);
                String currentDateTimeString = sdf.format(new Date());
                edtDate.setText(currentDateTimeString);

                // cancel button
                Button btnCancel = (Button) dialogAnnotate.findViewById(R.id.btn_cancel);
                btnCancel.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogAnnotate.dismiss();
                    }
                });

                // save button
                Button btnSave = (Button) dialogAnnotate.findViewById(R.id.btn_save);
                btnSave.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Date date = null;
                        String message = edtAnnotate.getText().toString().trim();
                        try {
                            date = sdf.parse(edtDate.getText().toString().trim());
                        } catch (ParseException e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                            return;
                        }
                        Log.d(Constants.TAG, "Annotation: " + message + " (" + date + ")");

                        JSONObject msg = new JSONObject();
                        try {
                            msg.put("activity", message);
                        } catch (JSONException e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                            return;
                        }

                        FileUtil.writeData(context, date, "ACTIVITY_LOG", msg);

                        Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
                        dialogAnnotate.dismiss();
                    }
                });

                dialogAnnotate.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case ACTIVITY_RESULT_NOTIFICATION_LISTENER_SETTINGS:
                updateNotificationListenerButton();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void turnOnAllSensorService() {
        synchronized (lock) {
            if (!isTurnedOn) {
                for (ServiceBean serviceBean : services) {
                    Log.d(Constants.TAG, "Starting service: " + serviceBean.getServiceName());
                    startService(new Intent(this, serviceBean.getServiceClass()));
                }
                isTurnedOn = true;
            }
        }
    }

    public void turnOffAllSensorService() {
        synchronized (lock) {
            if (isTurnedOn) {
                for (ServiceBean serviceBean : services) {
                    Log.d(Constants.TAG, "Stopping service: " + serviceBean.getServiceName());
                    stopService(new Intent(this, serviceBean.getServiceClass()));
                }
                isTurnedOn = false;
            }
        }
    }

    private BroadcastReceiver onNotice = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String pack = intent.getStringExtra("package");
            Log.i(Constants.DEBUG_TAG, "[CJS] " + pack);

            final Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    turnOffAllSensorService();
                    timer.cancel();
                }
            };

            JSONObject value = new JSONObject();
            try {
                value.put("package", pack);
            } catch (JSONException e) {
                Log.e(Constants.DEBUG_TAG, e.getMessage());
            }

            FileUtil.writeData(getApplicationContext(), new Date(), "PUSH", value);
            turnOnAllSensorService();
            timer.schedule(timerTask, 10 * 1000);
        }
    };
}