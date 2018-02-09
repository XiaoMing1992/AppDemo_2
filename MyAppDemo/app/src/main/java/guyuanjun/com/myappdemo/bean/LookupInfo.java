package guyuanjun.com.myappdemo.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.util.Date;

/**
 * Created by HP on 2017-5-14.
 */

@Entity(generateConstructors = false)
public class LookupInfo {
    /**
     * id
     */
    @Id(autoincrement = true)
    private Long id;

    private Long item_id;

    /**
     * 时间
     */
    private Date time;

    /**
     * 内容类型
     */
    private int flag_type;

    public LookupInfo(Long item_id, Date time, int flag_type){
        this.item_id = item_id;
        this.time = time;
        this.flag_type = flag_type;
    }

    public LookupInfo() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItem_id() {
        return this.item_id;
    }

    public void setItem_id(Long item_id) {
        this.item_id = item_id;
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
}
