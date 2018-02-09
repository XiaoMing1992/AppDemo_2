package guyuanjun.com.myappdemo.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by HP on 2017-4-14.
 */

@Entity(generateConstructors = false)
public class CollectInfo {

    @Id(autoincrement = true)
    private Long id;
    
    private Long user_id;

    private Long item_id;
    private Date collect_time;
    private boolean has_collected = false;
    
    public CollectInfo() {

    }

    public CollectInfo(long user_id, long item_id){
        this.user_id = user_id;
        this.item_id = item_id;
    }

    public CollectInfo(long user_id, long item_id, Date collect_time){
        this.user_id = user_id;
        this.item_id = item_id;
        this.collect_time = collect_time;
    }

    public CollectInfo(long user_id, long item_id, Date collect_time, boolean has_collected){
        this.user_id = user_id;
        this.item_id = item_id;
        this.collect_time = collect_time;
        this.has_collected = has_collected;
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

    public Date getCollect_time() {
        return this.collect_time;
    }

    public void setCollect_time(Date collect_time) {
        this.collect_time = collect_time;
    }

    public boolean getHas_collected() {
        return this.has_collected;
    }

    public void setHas_collected(boolean has_collected) {
        this.has_collected = has_collected;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}

