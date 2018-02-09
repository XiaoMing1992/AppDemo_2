package guyuanjun.com.myappdemo.vedio_module.contract;

import guyuanjun.com.myappdemo.vedio_module.base.BasePresenter;
import guyuanjun.com.myappdemo.vedio_module.base.IBaseFragment;
import guyuanjun.com.myappdemo.vedio_module.bean.NeteastVideoSummary;

/**
 * Created by mvp on 2016/9/12.
 */

public interface VideoContract {

    public interface IVideoPresenter extends BasePresenter {
        /**
         * 获取最新的日报数据
         *
         * @return
         */
        void getVideoData(String id, int startPage);
    }

    public interface IVideoFragment extends IBaseFragment {

        void updateVideoData(NeteastVideoSummary videoData);
    }
}
