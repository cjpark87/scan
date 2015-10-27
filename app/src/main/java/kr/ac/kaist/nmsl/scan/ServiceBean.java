package kr.ac.kaist.nmsl.scan;

/**
 * Created by wns349 on 2015-09-30.
 */
public class ServiceBean {
    private String serviceName;

    private Class<?> serviceClass;

    public ServiceBean(String serviceName, Class<?> serviceClass){
        this.serviceName = serviceName;
        this.serviceClass = serviceClass;
    }

    public Class<?> getServiceClass() { return this.serviceClass; }

    public String getServiceName(){
        return this.serviceName;
    }

}
