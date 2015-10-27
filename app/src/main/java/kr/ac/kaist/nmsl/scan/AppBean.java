package kr.ac.kaist.nmsl.scan;

/**
 * Created by wns349 on 2015-10-27.
 */
public class AppBean {

    private boolean isWhiteListed;
    private String appName;
    private String packageName;

    public AppBean(String appName, String packageName, boolean isWhiteListed) {
        this.appName = appName;
        this.packageName = packageName;
        this.isWhiteListed = isWhiteListed;
    }

    public void setWhiteListed(boolean isWhiteListed) {
        this.isWhiteListed = isWhiteListed;
    }

    public boolean isWhiteListed() {
        return this.isWhiteListed;
    }

    public String getAppName() {
        return this.appName;
    }

    public String getPackageName() {
        return this.packageName;
    }
}
