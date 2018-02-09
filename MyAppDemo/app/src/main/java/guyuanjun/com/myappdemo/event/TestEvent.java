package guyuanjun.com.myappdemo.event;

import guyuanjun.com.myappdemo.bean.IBase;
import guyuanjun.com.myappdemo.bean.RetBean;
import guyuanjun.com.myappdemo.bean.Student;

/**
 * Created by HP on 2017-6-15.
 */

public class TestEvent implements IBase<Student> {
    private Student mData;
    private RetBean mBean;

    @Override
    public Student getData() {
        return mData;
    }

    @Override
    public void setData(Student data) {
        mData = data;
    }

    @Override
    public void setRetBean(RetBean bean) {
        mBean = bean;
    }

    @Override
    public RetBean getRetBean() {
        return mBean;
    }
}
