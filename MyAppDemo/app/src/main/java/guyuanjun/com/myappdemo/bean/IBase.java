package guyuanjun.com.myappdemo.bean;

/**
 * Created by HP on 2017-6-15.
 */

public interface IBase<T> {
    void setData(T data);
    T getData();
    void setRetBean(RetBean bean);
    RetBean getRetBean();
}
