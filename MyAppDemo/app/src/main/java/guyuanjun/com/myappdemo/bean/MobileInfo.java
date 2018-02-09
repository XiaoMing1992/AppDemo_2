package guyuanjun.com.myappdemo.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by HP on 2017-4-17.
 */

@Entity(generateConstructors = false)
public class MobileInfo {

    @Id(autoincrement = true)
    private Long id;
    private Integer state;
    private String order_id;
    private String phone;
    private Date chongzhi_time;
    private String sporder_id; //聚合订单号
    private String uordercash; //订单扣除金额

    public MobileInfo() {

    }

    public MobileInfo(Date chongzhi_time) {
        this.chongzhi_time = chongzhi_time;
    }

    public MobileInfo(String phone, String order_id) {
        this.phone = phone;
        this.order_id = order_id;
    }

    public MobileInfo(String phone, String order_id, Date chongzhi_time) {
        this.order_id = order_id;
        this.phone = phone;
        this.chongzhi_time = chongzhi_time;
        this.state = 0; //
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getState() {
        return this.state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getOrder_id() {
        return this.order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getChongzhi_time() {
        return this.chongzhi_time;
    }

    public void setChongzhi_time(Date chongzhi_time) {
        this.chongzhi_time = chongzhi_time;
    }

    public String getSporder_id() {
        return this.sporder_id;
    }

    public void setSporder_id(String sporder_id) {
        this.sporder_id = sporder_id;
    }

    public String getUordercash() {
        return this.uordercash;
    }

    public void setUordercash(String uordercash) {
        this.uordercash = uordercash;
    }
}
