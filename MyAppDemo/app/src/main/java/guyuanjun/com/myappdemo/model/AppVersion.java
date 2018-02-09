package guyuanjun.com.myappdemo.model;

/**
 * Created by HP on 2017-3-19.
 */

public class AppVersion {

    /**
     * 版本号
     */
    private String mVersionName;

    /**
     * 版本码
     */
    private int mVersionCode;

    /**
     * 图片路径
     */
    private String mDownloadUrl;

    /**
     * 时间戳
     */
    private long mUpdateTime;

    public AppVersion(){}

    public String getVersionName() {
        return mVersionName;
    }

    public void setVersionName(String versionName) {
        mVersionName = versionName;
    }

    public int getVersionCode() {
        return mVersionCode;
    }

    public void setVersionCode(int version_code) {
        mVersionCode = version_code;
    }

    public String getDownloadUrl() {
        return mDownloadUrl;
    }

    public void setDownloadUrl(String download_url) {
        mDownloadUrl = download_url;
    }

    public long getUpdateTime() {
        return mUpdateTime;
    }

    public void setUpdateTime(long update_time) {
        mUpdateTime = update_time;
    }

    @Override
    public String toString() {
        return "[version_name=" + mVersionName + ", download_url=" + mDownloadUrl + ", update_time=" + mUpdateTime + "]";
    }
}
