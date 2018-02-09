package guyuanjun.com.myappdemo.event;

import guyuanjun.com.myappdemo.bean.AdverBean;
import guyuanjun.com.myappdemo.bean.IBase;
import guyuanjun.com.myappdemo.bean.RetBean;

/**
 * Created by Administrator on 2017/11/26 0026.
 */

public class AdverEvent implements IBase<AdverBean> {
    private AdverBean mData;
    private RetBean mRet;

    @Override
    public void setData(AdverBean data) {
        this.mData = data;
    }

    @Override
    public AdverBean getData() {
        return this.mData;
    }

    @Override
    public void setRetBean(RetBean bean) {
        this.mRet = bean;
    }

    @Override
    public RetBean getRetBean() {
        return this.mRet;
    }
}
