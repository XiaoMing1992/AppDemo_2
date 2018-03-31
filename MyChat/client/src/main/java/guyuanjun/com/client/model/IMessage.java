package guyuanjun.com.client.model;

/**
 * Created by HP on 2018-3-14.
 */

public interface IMessage {
    String getFrom();
    String getTime();
    void setFrom(String from);
    void setTime(String time);
    void setTo(String to);
    String getTo();
    String getMsg();
    void setMsg(String msg);
    void setId(long id);
    long getId();
    void setInfoId(long infoId);
    long getInfoId();
    void setType(int type);
    int getType();
}
