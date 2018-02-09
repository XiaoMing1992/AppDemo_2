package guyuanjun.com.myappdemo.db;

import android.content.Context;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import guyuanjun.com.myappdemo.bean.CollectInfo;
import guyuanjun.com.myappdemo.bean.UserInfo;
import guyuanjun.com.myappdemo.greendao.CollectInfoDao;
import guyuanjun.com.myappdemo.greendao.UserInfoDao;

/**
 * Created by HP on 2017-4-14.
 */

public class CollectInfoDaoOpe {

    private static volatile CollectInfoDaoOpe instance = null;

    private CollectInfoDaoOpe(){

    }

    public static CollectInfoDaoOpe getInstance(){
        if (instance == null){
            synchronized (CollectInfoDaoOpe.class){
                instance = new CollectInfoDaoOpe();
            }
        }
        return instance;
    }

    /**
     * 插入单条数据
     * @param context
     * @param collectInfo
     */
    public void insertData(Context context, CollectInfo collectInfo){
        DbManager.getInstance(context).getDaoSession(context).getCollectInfoDao().insert(collectInfo);
    }

    /**
     * 批量插入数据
     * @param context
     * @param collectInfoList
     */
    public void insertData(Context context, List<CollectInfo> collectInfoList){
        if (collectInfoList == null || collectInfoList.size()<=0)
            return;
        DbManager.getInstance(context).getDaoSession(context).getCollectInfoDao().insertInTx(collectInfoList);
    }

    /**
     * 添加数据至数据库，如果存在，将原来的数据覆盖
     * 内部代码判断了如果存在就update(entity);不存在就insert(entity)；
     *
     * @param context
     * @param collectInfo
     */
    public void save(Context context, CollectInfo collectInfo){
        DbManager.getInstance(context).getDaoSession(context).getCollectInfoDao().save(collectInfo);
    }

    /**
     * 删除某条记录
     * @param context
     * @param collectInfo
     */
    public void deleteData(Context context, CollectInfo collectInfo){
        DbManager.getInstance(context).getDaoSession(context).getCollectInfoDao().delete(collectInfo);
    }

    /**
     * 根据id来删除数据
     * @param context
     * @param id
     */
    public void deleteDataByKey(Context context, long id){
        DbManager.getInstance(context).getDaoSession(context).getCollectInfoDao().deleteByKey(id);
        //DbManager.getInstance(context).getDaoSession(context).getCollectInfoDao().
    }

    /**
     * 删除所有数据
     * @param context
     */
    public void deleteAll(Context context){
        DbManager.getInstance(context).getDaoSession(context).getCollectInfoDao().deleteAll();
    }

    /**
     * 更新某条记录
     * @param context
     * @param collectInfo
     */
    public void updateData(Context context, CollectInfo collectInfo){
        DbManager.getInstance(context).getDaoSession(context).getCollectInfoDao().update(collectInfo);
    }

    /**
     * 查询所有记录
     * @param context
     * @return
     */
    public List<CollectInfo> queryAll(Context context){
        QueryBuilder<CollectInfo> builder = DbManager.getInstance(context).getDaoSession(context).getCollectInfoDao().queryBuilder();
        return builder.list();
    }

    /**
     * 根据user_id来查询记录
     * @param context
     * @param user_id
     * @return
     */
    public List<CollectInfo> query(Context context, long user_id){
        QueryBuilder<CollectInfo> builder = DbManager.getInstance(context).getDaoSession(context).getCollectInfoDao().queryBuilder();
        /**
         * 返回当前id的数据集合,当然where(这里面可以有多组，做为条件);
         * 这里build.list()；与where(CollectInfoDao.Properties.User_id.eq(user_id)).list()结果是一样的；
         * 在QueryBuilder类中list()方法return build().list();
         *
         */
        // Query<CollectInfo> build = builder.where(CollectInfoDao.Properties.User_id.eq(user_id)).build();
        // List<CollectInfo> list = build.list();
        return builder.where(CollectInfoDao.Properties.User_id.eq(user_id)).list();
    }

    /**
     * 根据用户的id和收藏的信息的id来查询
     * @param context
     * @param user_id
     * @param item_id
     * @return
     */
    public List<CollectInfo> query(Context context, long user_id, long item_id){
        QueryBuilder<CollectInfo> builder = DbManager.getInstance(context).getDaoSession(context).getCollectInfoDao().queryBuilder();
        return builder.where(CollectInfoDao.Properties.User_id.eq(user_id), CollectInfoDao.Properties.Item_id.eq(item_id)).list();
    }
}
