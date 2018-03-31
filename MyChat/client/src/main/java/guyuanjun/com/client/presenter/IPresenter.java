package guyuanjun.com.client.presenter;

import guyuanjun.com.client.adapter.MyAdapter;

/**
 * Created by HP on 2018-3-14.
 */

public interface IPresenter {
    void clear();
    void sengMsg(final String to_id, final String msg);
    MyAdapter getMyAdapter();
    void getIp();
    void getServerMsg();
    String getFromIp();
    void getLocalMsgs();
}
