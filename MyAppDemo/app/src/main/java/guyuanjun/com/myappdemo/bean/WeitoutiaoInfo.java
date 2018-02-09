package guyuanjun.com.myappdemo.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.util.Date;

/**
 * Created by HP on 2017-4-18.
 */
@Entity(generateConstructors = false)
public class WeitoutiaoInfo {

    /**
     * id
     */
    @Id(autoincrement = true)
    private Long id;

    private Long user_id;

    /**
     *
     */
    private String username;

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
     * 阅读数量
     */
    private Integer read_num;

    /**
     * 赞的个数
     */
    private Integer praise_num;

    /**
     * 评论的数量
     */
    private Integer comment_num;

    /**
     * 是否已经赞
     */
    private Boolean has_praised = false;

    public WeitoutiaoInfo(Long user_id, String username, Date time, int flag_type){
        this.username = username;
        this.user_id = user_id;
        this.time = time;
        this.img_path = null;
        this.content = null;
        this.flag_type = flag_type;
        this.read_num = 0;
        this.praise_num = 0;
        this.comment_num = 0;
    }

    public WeitoutiaoInfo() {
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

    public String getWhere() {
        return this.where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public Date getTime() {
        return this.time;
    }

    public void setTime(Date time) {
        this.time = time;
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

    public Integer getRead_num() {
        return this.read_num;
    }

    public void setRead_num(Integer read_num) {
        this.read_num = read_num;
    }

    public Integer getComment_num() {
        return this.comment_num;
    }

    public void setComment_num(Integer comment_num) {
        this.comment_num = comment_num;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getHas_praised() {
        return this.has_praised;
    }

    public void setHas_praised(Boolean has_praised) {
        this.has_praised = has_praised;
    }

    public String getImg_path_list() {
        return this.img_path_list;
    }

    public void setImg_path_list(String img_path_list) {
        this.img_path_list = img_path_list;
    }

}
