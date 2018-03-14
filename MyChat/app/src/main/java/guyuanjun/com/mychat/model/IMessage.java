package guyuanjun.com.mychat.model;

/**
 * Created by HP on 2018-3-14.
 */

public interface IMessage {
    String getContent();
    String getTime();
    void setContent(String content);
    void setTime(String time);
}
