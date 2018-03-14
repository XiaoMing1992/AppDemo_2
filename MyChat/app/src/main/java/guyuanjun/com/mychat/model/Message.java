package guyuanjun.com.mychat.model;

/**
 * Created by HP on 2018-3-14.
 */

public class Message implements IMessage{
    private String mContent;
    private String mTimeStr;
    
    @Override
    public String getContent() {
        return mContent;
    }

    @Override
    public String getTime() {
        return mTimeStr;
    }

    @Override
    public void setContent(String content) {
        this.mContent = content;
    }

    @Override
    public void setTime(String time) {
        this.mTimeStr = time;
    }
}
