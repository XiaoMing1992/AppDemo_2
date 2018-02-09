package guyuanjun.com.myappdemo.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by HP on 2017-4-18.
 */
@Entity(generateConstructors = false)
public class NewsItemInfo implements Serializable{
    private final static long serialVersionUID = 536871008;

    /**
     * id
     */
    @Id(autoincrement = true)
    private Long id;

    private Long user_id;

    /**
     * 标题
     */
    private String title;

    /**
     * 来源
     */
    private String where;

    /**
     * 时间
     */
    private Date time;

    /**
     * 图片
     */
    private String img_path;

    /**
     * 图片列表
     */
    private String img_path_list;

    /**
     * 文字内容
     */
    private String content;

    /**
     * 内容类型
     */
    private int flag_type;

    /**
     * 赞的个数
     */
    private Integer praise_num;

    public NewsItemInfo(Long user_id, String title,String where, Date time, int flag_type){
        this.title = title;
        this.where = where;
        this.user_id = user_id;
        this.time = time;
        this.img_path = null;
        this.img_path_list = null;
        this.content = null;
        this.flag_type = flag_type;
        this.praise_num = 0;
    }

    public NewsItemInfo() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWhere() {
        return this.where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getImg_path() {
        return this.img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getUser_id() {
        return this.user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Date getTime() {
        return this.time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getFlag_type() {
        return this.flag_type;
    }

    public void setFlag_type(int flag_type) {
        this.flag_type = flag_type;
    }

    public Integer getPraise_num() {
        return this.praise_num;
    }

    public void setPraise_num(Integer praise_num) {
        this.praise_num = praise_num;
    }

    public String getImg_path_list() {
        return this.img_path_list;
    }

    public void setImg_path_list(String img_path_list) {
        this.img_path_list = img_path_list;
    }

}
