package guyuanjun.com.client.presenter;

import guyuanjun.com.client.adapter.MyAdapter;

/**
 * Created by HP on 2018-3-14.
 */

public interface IPresenter {
    void clear();
    void sengMsg(final String msg);
    MyAdapter getMyAdapter();
}
