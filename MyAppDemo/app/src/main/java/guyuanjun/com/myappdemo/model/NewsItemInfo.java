package guyuanjun.com.myappdemo.model;

import android.graphics.Bitmap;

/**
 * Created by HP on 2017-3-16.
 */

public class NewsItemInfo {
    /**
     * id
     */
    private long mId;

    /**
     * 标题
     */
    private String mTitle;

    /**
     * 来源
     */
    private String mWhere;

    /**
     * 评论
     */
    private String mComment;

    /**
     * 时间
     */
    private String mTime;

    /**
     * 图片
     */
    private Bitmap mImg = null;

    /**
     * 文字内容
     */
    private String mContent =null;

    public NewsItemInfo(String title,String where, String comment, String time){
        this.mTitle = title;
        this.mWhere = where;
        this.mComment = comment;
        this.mTime = time;
    }

    public void setId(long id){
        this.mId = id;
    }

    public long getId(){
        return this.mId;
    }
    public String getTitle() {
        return this.mTitle;
    }

    public String getWhere() {
        return this.mWhere;
    }

    public String getComment() {
        return this.mComment;
    }

    public String getTime() {
        return this.mTime;
    }

    public Bitmap getImg() {
        return this.mImg;
    }

    public String getContent() {
        return this.mContent;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setComment(String comment) {
        this.mComment = comment;
    }

    public void setImg(Bitmap img) {
        this.mImg = img;
    }

    public void setWhere(String where) {
        this.mWhere = where;
    }

    public void setTime(String time) {
        this.mTime = time;
    }

    public void setContent(String content) {
        this.mContent = content;
    }
}
