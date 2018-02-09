package guyuanjun.com.myappdemo.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/26 0026.
 */

public class AdverBean implements Serializable{
    /**
     * 广告的id
     */
    private long id;

    /**
     * 图片的路径
     */
    private String img_path;

    /**
     * item的id
     */
    private long item_id;

    public long getId() {
        return id;
    }

    public long getItem_id() {
        return item_id;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setItem_id(long item_id) {
        this.item_id = item_id;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }
}
