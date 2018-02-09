package guyuanjun.com.myappdemo.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/8/29 0029.
 */

@Entity(generateConstructors = false)
public class AdminInfo {

    @Id(autoincrement = true)
    private Long id;

    private String adminname;

    private String nickname;

    private String password;

    private String email;

    private Integer adminlevel;

    public AdminInfo() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdminname() {
        return this.adminname;
    }

    public void setAdminname(String adminname) {
        this.adminname = adminname;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAdminlevel() {
        return this.adminlevel;
    }

    public void setAdminlevel(Integer adminlevel) {
        this.adminlevel = adminlevel;
    }
    
}
