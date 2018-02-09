package guyuanjun.com.myappdemo.db;

import android.content.Context;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import guyuanjun.com.myappdemo.bean.FanInfo;
import guyuanjun.com.myappdemo.bean.MobileInfo;
import guyuanjun.com.myappdemo.greendao.FanInfoDao;
import guyuanjun.com.myappdemo.greendao.MobileInfoDao;

/**
 * Created by HP on 2017-5-3.
 */

public class MobileInfoDaoOpe {

    private static volatile MobileInfoDaoOpe instance = null;

    private MobileInfoDaoOpe(){

    }

    public static MobileInfoDaoOpe getInstance(){
        if (instance == null){
            synchronized (MobileInfoDaoOpe.class){
                instance = new MobileInfoDaoOpe();
            }
        }
        return instance;
    }

    /**
     * 插入单条数据
     * @param context
     * @param mobileInfo
     */
    public void insertData(Context context, MobileInfo mobileInfo){
        DbManager.getInstance(context).getDaoSession(context).getMobileInfoDao().insert(mobileInfo);
    }

    /**
     * 批量插入数据
     * @param context
     * @param mobileInfoList
     */
    public void insertData(Context context, List<MobileInfo> mobileInfoList){
        if (mobileInfoList == null || mobileInfoList.size()<=0)
            return;
        DbManager.getInstance(context).getDaoSession(context).getMobileInfoDao().insertInTx(mobileInfoList);
    }

    /**
     * 添加数据至数据库，如果存在，将原来的数据覆盖
     * 内部代码判断了如果存在就update(entity);不存在就insert(entity)；
     *
     * @param context
     * @param mobileInfo
     */
    public void save(Context context, MobileInfo mobileInfo){
        DbManager.getInstance(context).getDaoSession(context).getMobileInfoDao().save(mobileInfo);
    }

    /**
     * 删除某条记录
     * @param context
     * @param mobileInfo
     */
    public void deleteData(Context context, MobileInfo mobileInfo){
        DbManager.getInstance(context).getDaoSession(context).getMobileInfoDao().delete(mobileInfo);
    }

    /**
     * 根据id来删除数据
     * @param context
     * @param id
     */
    public void deleteDataByKey(Context context, long id){
        DbManager.getInstance(context).getDaoSession(context).getMobileInfoDao().deleteByKey(id);
        //DbManager.getInstance(context).getDaoSession(context).getCollectInfoDao().
    }

    /**
     * 删除所有数据
     * @param context
     */
    public void deleteAll(Context context){
        DbManager.getInstance(context).getDaoSession(context).getMobileInfoDao().deleteAll();
    }

    /**
     * 更新某条记录
     * @param context
     * @param mobileInfo
     */
    public void updateData(Context context, MobileInfo mobileInfo){
        DbManager.getInstance(context).getDaoSession(context).getMobileInfoDao().update(mobileInfo);
    }

    /**
     * 查询所有记录
     * @param context
     * @return
     */
    public List<MobileInfo> queryAll(Context context){
        QueryBuilder<MobileInfo> builder = DbManager.getInstance(context).getDaoSession(context).getMobileInfoDao().queryBuilder();
        return builder.list();
    }

    /**
     * 根据用户的手机号码来查询记录
     * @param context
     * @param phone
     * @return
     */
    public List<MobileInfo> query(Context context, String phone){
        QueryBuilder<MobileInfo> builder = DbManager.getInstance(context).getDaoSession(context).getMobileInfoDao().queryBuilder();
        // Query<MobileInfo> build = builder.where(MobileInfoDao.Properties.Phone.eq(phone)).build();
        // List<MobileInfo> list = build.list();
        return builder.where(MobileInfoDao.Properties.Phone.eq(phone)).list();
    }

    /**
     * 根据用户的手机号码和订单的id来查询
     * @param context
     * @param phone
     * @param order_id
     * @return
     */
    public List<MobileInfo> query(Context context, String phone, String order_id){
        QueryBuilder<MobileInfo> builder = DbManager.getInstance(context).getDaoSession(context).getMobileInfoDao().queryBuilder();
        return builder.where(MobileInfoDao.Properties.Phone.eq(phone), MobileInfoDao.Properties.Order_id.eq(order_id)).list();
    }
}
