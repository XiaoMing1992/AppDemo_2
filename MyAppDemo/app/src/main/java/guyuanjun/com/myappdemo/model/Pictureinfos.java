package guyuanjun.com.myappdemo.model;

/**
 * Created by HP on 2017-2-14.
 */

public class Pictureinfos {

    /**
     * 图片顺序
     */
    private int mOrder;

    /**
     * 图片路径
     */
    private String mImage_url;

    /**
     * 时间戳
     */
    private long mUpdate_time;

    public Pictureinfos() {
        mOrder = 0;
        mImage_url = null;
        mUpdate_time = 0;
    }

    public int getOrder() {
        return mOrder;
    }

    public void setOrder(int order) {
        mOrder = order;
    }

    public String getImageUrl() {
        return mImage_url;
    }

    public void setImageUrl(String image_url) {
        mImage_url = image_url;
    }

    public long getUpdateTime() {
        return mUpdate_time;
    }

    public void setUpdateTime(long update_time) {
        mUpdate_time = update_time;
    }

    @Override
    public String toString() {
        return "[order=" + mOrder + ", image_url=" + mImage_url + ", update_time=" + mUpdate_time + "]";
    }
}
