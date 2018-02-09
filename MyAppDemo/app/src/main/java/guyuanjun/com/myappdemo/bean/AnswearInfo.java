package guyuanjun.com.myappdemo.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by HP on 2017-6-20.
 */
@Entity(generateConstructors = false)
public class AnswearInfo {

    @Id(autoincrement = true)
    private Long id;
    private Long host_id;
    private Long answear_id;
    private Long item_id;
    private String host_nickname;
    private String answear_nickname;
    private String answear_content;

    public AnswearInfo() {
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

    public Long getAnswear_id() {
        return this.answear_id;
    }

    public void setAnswear_id(Long answear_id) {
        this.answear_id = answear_id;
    }

    public String getHost_nickname() {
        return this.host_nickname;
    }

    public void setHost_nickname(String host_nickname) {
        this.host_nickname = host_nickname;
    }

    public String getAnswear_nickname() {
        return this.answear_nickname;
    }

    public void setAnswear_nickname(String answear_nickname) {
        this.answear_nickname = answear_nickname;
    }

    public String getAnswear_content() {
        return this.answear_content;
    }

    public void setAnswear_content(String answear_content) {
        this.answear_content = answear_content;
    }

    public Long getItem_id() {
        return this.item_id;
    }

    public void setItem_id(Long item_id) {
        this.item_id = item_id;
    }
}
