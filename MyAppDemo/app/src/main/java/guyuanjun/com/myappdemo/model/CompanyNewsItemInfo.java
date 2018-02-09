package guyuanjun.com.myappdemo.model;

/**
 * Created by HP on 2017-3-19.
 */

public class CompanyNewsItemInfo {

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

    public CompanyNewsItemInfo(String title,String where, String comment, String time){
        this.mTitle = title;
        this.mWhere = where;
        this.mComment = comment;
        this.mTime = time;
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

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setComment(String comment) {
        this.mComment = comment;
    }

    public void setWhere(String where) {
        this.mWhere = where;
    }

    public void setTime(String time) {
        this.mTime = time;
    }
}
