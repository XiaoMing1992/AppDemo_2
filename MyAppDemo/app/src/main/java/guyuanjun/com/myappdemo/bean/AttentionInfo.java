package guyuanjun.com.myappdemo.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import java.util.Date;

/**
 * Created by HP on 2017-5-9.
 */

@Entity(generateConstructors = false)
public class AttentionInfo {

    @Id(autoincrement = true)
    private Long id;

    private Long user_id;

    private Long item_id;
    private Date attention_time;
    private boolean has_attentioned = false;

    public AttentionInfo() {

    }

    public AttentionInfo(long user_id, long item_id) {
        this.user_id = user_id;
        this.item_id = item_id;
    }

    public AttentionInfo(long user_id, long item_id, Date attention_time) {
        this.user_id = user_id;
        this.item_id = item_id;
        this.attention_time = attention_time;
    }

    public AttentionInfo(long user_id, long item_id, Date attention_time, boolean has_attentioned) {
        this.user_id = user_id;
        this.item_id = item_id;
        this.attention_time = attention_time;
        this.has_attentioned = has_attentioned;
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

    public Date getAttention_time() {
        return this.attention_time;
    }

    public void setAttention_time(Date attention_time) {
        this.attention_time = attention_time;
    }

    public boolean getHas_attentioned() {
        return this.has_attentioned;
    }

    public void setHas_attentioned(boolean has_attentioned) {
        this.has_attentioned = has_attentioned;
    }
}
