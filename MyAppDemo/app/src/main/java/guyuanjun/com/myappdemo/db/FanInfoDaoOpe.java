package guyuanjun.com.myappdemo.db;

import android.content.Context;
import org.greenrobot.greendao.query.QueryBuilder;
import java.util.List;

import guyuanjun.com.myappdemo.bean.FanInfo;
import guyuanjun.com.myappdemo.greendao.FanInfoDao;

/**
 * Created by HP on 2017-4-30.
 */

public class FanInfoDaoOpe {

    private static volatile FanInfoDaoOpe instance = null;

    private FanInfoDaoOpe(){

    }

    public static FanInfoDaoOpe getInstance(){
        if (instance == null){
            synchronized (FanInfoDaoOpe.class){
                instance = new FanInfoDaoOpe();
            }
        }
        return instance;
    }

    /**
     * 插入单条数据
     * @param context
     * @param fanInfo
     */
    public void insertData(Context context, FanInfo fanInfo){
        DbManager.getInstance(context).getDaoSession(context).getFanInfoDao().insert(fanInfo);
    }

    /**
     * 批量插入数据
     * @param context
     * @param fanInfoList
     */
    public void insertData(Context context, List<FanInfo> fanInfoList){
        if (fanInfoList == null || fanInfoList.size()<=0)
            return;
        DbManager.getInstance(context).getDaoSession(context).getFanInfoDao().insertInTx(fanInfoList);
    }

    /**
     * 添加数据至数据库，如果存在，将原来的数据覆盖
     * 内部代码判断了如果存在就update(entity);不存在就insert(entity)；
     *
     * @param context
     * @param fanInfo
     */
    public void save(Context context, FanInfo fanInfo){
        DbManager.getInstance(context).getDaoSession(context).getFanInfoDao().save(fanInfo);
    }

    /**
     * 删除某条记录
     * @param context
     * @param fanInfo
     */
    public void deleteData(Context context, FanInfo fanInfo){
        DbManager.getInstance(context).getDaoSession(context).getFanInfoDao().delete(fanInfo);
    }

    /**
     * 根据id来删除数据
     * @param context
     * @param id
     */
    public void deleteDataByKey(Context context, long id){
        DbManager.getInstance(context).getDaoSession(context).getFanInfoDao().deleteByKey(id);
        //DbManager.getInstance(context).getDaoSession(context).getCollectInfoDao().
    }

    /**
     * 删除所有数据
     * @param context
     */
    public void deleteAll(Context context){
        DbManager.getInstance(context).getDaoSession(context).getFanInfoDao().deleteAll();
    }

    /**
     * 更新某条记录
     * @param context
     * @param fanInfo
     */
    public void updateData(Context context, FanInfo fanInfo){
        DbManager.getInstance(context).getDaoSession(context).getFanInfoDao().update(fanInfo);
    }

    /**
     * 查询所有记录
     * @param context
     * @return
     */
    public List<FanInfo> queryAll(Context context){
        QueryBuilder<FanInfo> builder = DbManager.getInstance(context).getDaoSession(context).getFanInfoDao().queryBuilder();
        return builder.list();
    }

    /**
     * 根据用户的id来查询记录
     * @param context
     * @param host_id
     * @return 失败返回null
     */
    public List<FanInfo> query(Context context, long host_id){
        try {
            QueryBuilder<FanInfo> builder = DbManager.getInstance(context).getDaoSession(context).getFanInfoDao().queryBuilder();
            // Query<CollectInfo> build = builder.where(CollectInfoDao.Properties.User_id.eq(user_id)).build();
            // List<CollectInfo> list = build.list();
            return builder.where(FanInfoDao.Properties.Host_id.eq(host_id)).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据用户的id和粉丝的id来查询
     * @param context
     * @param host_id
     * @param fan_id
     * @return
     */
    public List<FanInfo> query(Context context, long host_id, long fan_id){
        QueryBuilder<FanInfo> builder = DbManager.getInstance(context).getDaoSession(context).getFanInfoDao().queryBuilder();
        return builder.where(FanInfoDao.Properties.Host_id.eq(host_id), FanInfoDao.Properties.Fan_id.eq(fan_id)).list();
    }
}
