package guyuanjun.com.myappdemo.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by HP on 2017-4-17.
 */

@Entity(generateConstructors = false)
public class CommentInfo {

    @Id(autoincrement = true)
    private Long id;  //此id是每条评论的id

    private Long user_id;
    private Long item_id; //此id是在NewsAdapter那里的某个item的id
    private String comment;
    private Date comment_time;
    private Integer praise_num; //赞的个数
    private Boolean has_praise; //user_id是否已经赞

    public CommentInfo() {

    }

    public CommentInfo(long user_id, long item_id) {
        this.user_id = user_id;
        this.item_id = item_id;
    }

    public CommentInfo(long user_id, long item_id, String comment, Date comment_time) {
        this.user_id = user_id;
        this.item_id = item_id;
        this.comment = comment;
        this.comment_time = comment_time;
        this.praise_num = 0; //初始化赞的个数为0
        this.has_praise = false; //初始化没有赞
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return this.user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getItem_id() {
        return this.item_id;
    }

    public void setItem_id(Long item_id) {
        this.item_id = item_id;
    }

    public Date getComment_time() {
        return this.comment_time;
    }

    public void setComment_time(Date comment_time) {
        this.comment_time = comment_time;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getPraise_num() {
        return this.praise_num;
    }

    public void setPraise_num(Integer praise_num) {
        this.praise_num = praise_num;
    }

    public Boolean getHas_praise() {
        return this.has_praise;
    }

    public void setHas_praise(Boolean has_praise) {
        this.has_praise = has_praise;
    }
    
}