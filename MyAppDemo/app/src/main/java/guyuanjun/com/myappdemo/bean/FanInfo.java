package guyuanjun.com.myappdemo.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by HP on 2017-4-12.
 */

@Entity(generateConstructors = false)
public class FanInfo {

    @Id(autoincrement = true)
    private Long id;
    private Long host_id;
    private Long fan_id;
    private String nickname;
    private Date createTime;

    public FanInfo() {

    }

    public FanInfo(long host_id, long fan_id) {
        this.host_id = host_id;
        this.fan_id = fan_id;
    }

    public FanInfo(long host_id, long fan_id, String nickname, Date createTime) {
        this.host_id = host_id;
        this.fan_id = fan_id;
        this.nickname = nickname;
        this.createTime = createTime;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHost_id() {
        return this.host_id;
    }

    public void setHost_id(Long host_id) {
        this.host_id = host_id;
    }

    public Long getFan_id() {
        return this.fan_id;
    }

    public void setFan_id(Long fan_id) {
        this.fan_id = fan_id;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
}


