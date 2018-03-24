package guyuanjun.com.mychat.model;

/**
 * Created by HP on 2018-3-14.
 */

public class MyMessage implements IMessage{
    private String mContent;
    private String mTimeStr;
    private String mTo;
    private String mMsg;

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

    @Override
    public void setTo(String to) {
        mTo = to;
    }

    @Override
    public String getTo() {
        return mTo;
    }

    @Override
    public String getMsg() {
        return mMsg;
    }

    @Override
    public void setMsg(String msg) {
        mMsg = msg;
    }
}
