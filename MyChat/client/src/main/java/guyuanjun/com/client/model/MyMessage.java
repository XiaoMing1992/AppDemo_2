package guyuanjun.com.client.model;

/**
 * Created by HP on 2018-3-14.
 */

public class MyMessage implements IMessage{
    private String mFrom;
    private String mTimeStr;
    private String mTo;
    private String mMsg;
    private long mId;
    private long mInfoId;
    private int mType;

    @Override
    public String getFrom() {
        return this.mFrom;
    }

    @Override
    public String getTime() {
        return this.mTimeStr;
    }

    @Override
    public void setFrom(String from) {
        this.mFrom = from;
    }

    @Override
    public void setTime(String time) {
        this.mTimeStr = time;
    }

    @Override
    public void setTo(String to) {
        this.mTo = to;
    }

    @Override
    public String getTo() {
        return this.mTo;
    }

    @Override
    public String getMsg() {
        return this.mMsg;
    }

    @Override
    public void setMsg(String msg) {
        this.mMsg = msg;
    }

    @Override
    public void setId(long id) {
        this.mId = id;
    }

    @Override
    public long getId() {
        return this.mId;
    }

    @Override
    public void setInfoId(long infoId) {
        this.mInfoId = infoId;
    }

    @Override
    public long getInfoId() {
        return this.mInfoId;
    }

    @Override
    public void setType(int type) {
        this.mType = type;
    }

    @Override
    public int getType() {
        return mType;
    }
}
