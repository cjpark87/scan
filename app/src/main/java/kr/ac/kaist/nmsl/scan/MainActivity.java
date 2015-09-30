package kr.ac.kaist.nmsl.scan;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.ac.kaist.nmsl.scan.gps.GPSService;
import kr.ac.kaist.nmsl.scan.util.UUIDGenerator;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<ServiceBean> services = new ArrayList<ServiceBean>();

        // Add services here
        services.add(new ServiceBean(GPSService.class.getSimpleName(), new Intent(this, GPSService.class), false));


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
