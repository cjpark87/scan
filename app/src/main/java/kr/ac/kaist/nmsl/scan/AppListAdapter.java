package kr.ac.kaist.nmsl.scan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wns349 on 2015-10-27.
 */
public class AppListAdapter extends BaseAdapter {
    private final List<AppBean> apps;

    private final Context context;
    public AppListAdapter(Context context){
        this(context, new ArrayList<AppBean>());
    }

    public AppListAdapter(Context context, List<AppBean> apps){
        this.context = context;
        this.apps = apps;
    }

    @Override
    public int getCount() {
        return apps.size();
    }

    @Override
    public Object getItem(int position) {
        return apps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.app_list, parent, false);

        CheckBox chkAppName = (CheckBox) rowView.findViewById(R.id.chk_app);
        TextView txtPackageName = (TextView) rowView.findViewById(R.id.txt_app_package);

        final AppBean appBean = this.apps.get(position);

        chkAppName.setChecked(appBean.isWhiteListed());
        chkAppName.setText(appBean.getAppName());
        txtPackageName.setText(appBean.getPackageName());

        chkAppName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                appBean.setWhiteListed(isChecked);
            }
        });

        return rowView;
    }
}
