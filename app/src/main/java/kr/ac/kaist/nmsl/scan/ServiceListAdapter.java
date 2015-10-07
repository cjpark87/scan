package kr.ac.kaist.nmsl.scan;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kr.ac.kaist.nmsl.scan.util.ServiceUtil;

/**
 * Created by wns349 on 2015-09-30.
 */
public class ServiceListAdapter extends BaseAdapter{
    private final List<ServiceBean> services;

    private final Context context;
    public ServiceListAdapter(Context context){
        this(context, new ArrayList<ServiceBean>());
    }

    public ServiceListAdapter(Context context, List<ServiceBean> services){
        this.context = context;
        this.services = services;
    }

    @Override
    public int getCount() {
        return services.size();
    }

    @Override
    public Object getItem(int position) {
        return services.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.service_list, parent, false);

        TextView txtServiceName = (TextView) rowView.findViewById(R.id.txt_service_name);
        Switch switchServiceStatus = (Switch) rowView.findViewById(R.id.switch_service);

        final ServiceBean serviceBean = this.services.get(position);

        txtServiceName.setText(serviceBean.getServiceName());
        switchServiceStatus.setChecked(ServiceUtil.isServiceRunning(context, serviceBean.getServiceClass()));
        switchServiceStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Start service
                    Log.d(Constants.TAG, "Starting service: " + serviceBean.getServiceName());
                    context.startService(new Intent(context, serviceBean.getServiceClass()));
                } else {
                    // Stop service
                    Log.d(Constants.TAG, "Stopping service: " + serviceBean.getServiceName());
                    context.stopService(new Intent(context, serviceBean.getServiceClass()));
                }
            }
        });

        return rowView;
    }

}
