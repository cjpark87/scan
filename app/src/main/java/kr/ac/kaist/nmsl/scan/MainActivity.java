package kr.ac.kaist.nmsl.scan;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.ac.kaist.nmsl.scan.mic.MicrophoneService;
import kr.ac.kaist.nmsl.scan.sensor.AccelerometerService;
import kr.ac.kaist.nmsl.scan.gps.GPSService;
import kr.ac.kaist.nmsl.scan.sensor.GravityService;
import kr.ac.kaist.nmsl.scan.sensor.GyroscopeService;
import kr.ac.kaist.nmsl.scan.sensor.LightService;
import kr.ac.kaist.nmsl.scan.sensor.MagneticFieldService;
import kr.ac.kaist.nmsl.scan.sensor.OrientationService;
import kr.ac.kaist.nmsl.scan.sensor.PressureService;
import kr.ac.kaist.nmsl.scan.sensor.ProximityService;
import kr.ac.kaist.nmsl.scan.sensor.HumidityService;
import kr.ac.kaist.nmsl.scan.sensor.RotationVectorService;
import kr.ac.kaist.nmsl.scan.sensor.SignificantMotionService;
import kr.ac.kaist.nmsl.scan.sensor.StepDectionService;
import kr.ac.kaist.nmsl.scan.sensor.TemperatureService;
import kr.ac.kaist.nmsl.scan.util.UUIDGenerator;

public class MainActivity extends Activity {

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

        List<ServiceBean> services = new ArrayList<ServiceBean>();

        // Add services here
        services.add(new ServiceBean(GPSService.class.getSimpleName(), new Intent(this, GPSService.class), false));
        services.add(new ServiceBean(AccelerometerService.class.getSimpleName(), new Intent(this, AccelerometerService.class), false));
        services.add(new ServiceBean(GravityService.class.getSimpleName(), new Intent(this, GravityService.class), false));
        services.add(new ServiceBean(GyroscopeService.class.getSimpleName(), new Intent(this, GyroscopeService.class), false));
        services.add(new ServiceBean(RotationVectorService.class.getSimpleName(), new Intent(this, RotationVectorService.class), false));
        services.add(new ServiceBean(SignificantMotionService.class.getSimpleName(), new Intent(this, SignificantMotionService.class), false));
        services.add(new ServiceBean(StepDectionService.class.getSimpleName(), new Intent(this, StepDectionService.class), false));
        services.add(new ServiceBean(MagneticFieldService.class.getSimpleName(), new Intent(this, MagneticFieldService.class), false));
        services.add(new ServiceBean(OrientationService.class.getSimpleName(), new Intent(this, OrientationService.class), false));
        services.add(new ServiceBean(ProximityService.class.getSimpleName(), new Intent(this, ProximityService.class), false));
        services.add(new ServiceBean(LightService.class.getSimpleName(), new Intent(this, LightService.class), false));
        services.add(new ServiceBean(PressureService.class.getSimpleName(), new Intent(this, PressureService.class), false));
        services.add(new ServiceBean(TemperatureService.class.getSimpleName(), new Intent(this, TemperatureService.class), false));
        services.add(new ServiceBean(HumidityService.class.getSimpleName(), new Intent(this, HumidityService.class), false));
        services.add(new ServiceBean(MicrophoneService.class.getSimpleName(), new Intent(this, MicrophoneService.class), false));

        ListView listServices = (ListView) findViewById(R.id.list_services);
        ServiceListAdapter listAdapter = new ServiceListAdapter(this, services);
        listServices.setAdapter(listAdapter);

        listAdapter.notifyDataSetChanged();

        // Initialize annotate button
        initializeAnnotateButton();

        // Initialize UUID
        initializeUUID();
    }

    private void initializeUUID(){
        TextView txtUUID = (TextView)findViewById(R.id.txt_uuid);
        txtUUID.setText(UUIDGenerator.getUUID(this));
    }

    private void initializeAnnotateButton(){
        final Context context = this;
        Button btnAnnotate = (Button) findViewById(R.id.btn_annotate);
        btnAnnotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialogAnnotate = new Dialog(context);
                dialogAnnotate.setContentView(R.layout.dialog_annotate);
                dialogAnnotate.setTitle(R.string.annotate);

                final EditText edtAnnotate = (EditText) dialogAnnotate.findViewById(R.id.edt_annotate);

                // Set date
                final EditText edtDate = (EditText) dialogAnnotate.findViewById(R.id.edt_date);
                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                edtDate.setText(currentDateTimeString);

                // cancel button
                Button btnCancel = (Button) dialogAnnotate.findViewById(R.id.btn_cancel);
                btnCancel.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        dialogAnnotate.dismiss();
                    }
                });

                // save button
                Button btnSave = (Button) dialogAnnotate.findViewById(R.id.btn_save);
                btnSave.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        String message = edtAnnotate.getText().toString().trim();
                        String date = edtDate.getText().toString().trim();
                        Log.d(Constants.TAG, "Annotation: " + message + " (" + date + ")");

                        dialogAnnotate.dismiss();
                    }
                });

                dialogAnnotate.show();
            }
        });
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
}
