package kr.ac.kaist.nmsl.scan;

import android.content.Intent;

/**
 * Created by wns349 on 2015-09-30.
 */
public class ServiceBean {
    private String serviceName;
    private boolean isRunning;

    private Intent serviceIntent;

    public ServiceBean(String serviceName, Intent serviceIntent, boolean isRunning){
        this.serviceName = serviceName;
        this.serviceIntent = serviceIntent;
        this.isRunning = isRunning;
    }

    public void setRunning(boolean isRunning){
        this.isRunning = isRunning;
    }

    public Intent getServiceIntent(){
        return this.serviceIntent;
    }

    public String getServiceName(){
        return this.serviceName;
    }

    public boolean isRunning(){
        return this.isRunning;
    }
}
