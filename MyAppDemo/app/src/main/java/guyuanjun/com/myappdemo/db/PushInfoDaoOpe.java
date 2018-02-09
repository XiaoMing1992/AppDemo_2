package guyuanjun.com.myappdemo.db;

import android.content.Context;
import org.greenrobot.greendao.query.QueryBuilder;
import java.util.List;
import guyuanjun.com.myappdemo.bean.PushInfo;
import guyuanjun.com.myappdemo.greendao.PushInfoDao;

/**
 * Created by HP on 2017-5-14.
 */

public class PushInfoDaoOpe {
    private static volatile PushInfoDaoOpe instance = null;

    private PushInfoDaoOpe() {

    }

    public static PushInfoDaoOpe getInstance() {
        if (instance == null) {
            synchronized (PushInfoDaoOpe.class) {
                instance = new PushInfoDaoOpe();
            }
        }
        return instance;
    }

    /**
     * 插入单条数据
     * @param context
     * @param pushInfo
     * @return 200表示成功，505表示失败
     */
    public int insertData(Context context, PushInfo pushInfo){
        try {
            DbManager.getInstance(context).getDaoSession(context).getPushInfoDao().insert(pushInfo);
            return 200;
        } catch (Exception e) {
            e.printStackTrace();
            return 505;
        }
    }

    /**
     * 批量插入数据
     * @param context
     * @param pushInfoList
     * @return 200表示成功，404表示客户端传入的参数有问题，505表示失败
     */
    public int insertData(Context context, List<PushInfo> pushInfoList){
        if (pushInfoList == null || pushInfoList.size()<=0)
            return 404;
        try {
            DbManager.getInstance(context).getDaoSession(context).getPushInfoDao().insertInTx(pushInfoList);
            return 200;
        } catch (Exception e) {
            e.printStackTrace();
            return 505;
        }
    }

    /**
     * 添加数据至数据库，如果存在，将原来的数据覆盖
     * 内部代码判断了如果存在就update(entity);不存在就insert(entity)；
     *
     * @param context
     * @param pushInfo
     * @return 200表示成功，505表示失败
     */
    public int save(Context context, PushInfo pushInfo){
        try {
            DbManager.getInstance(context).getDaoSession(context).getPushInfoDao().save(pushInfo);
            return 200;
        } catch (Exception e) {
            e.printStackTrace();
            return 505;
        }
    }

    /**
     * 删除某条记录
     * @param context
     * @param pushInfo
     * @return 200表示成功，505表示失败
     */
    public int deleteData(Context context, PushInfo pushInfo){
        try {
            DbManager.getInstance(context).getDaoSession(context).getPushInfoDao().delete(pushInfo);
            return 200;
        } catch (Exception e) {
            e.printStackTrace();
            return 505;
        }
    }

    /**
     * 根据id来删除数据
     * @param context
     * @param id
     * @return 200表示成功，505表示失败
     */
    public int deleteDataByKey(Context context, long id){
        try {
            DbManager.getInstance(context).getDaoSession(context).getPushInfoDao().deleteByKey(id);
            return 200;
        } catch (Exception e) {
            e.printStackTrace();
            return 505;
        }
        //DbManager.getInstance(context).getDaoSession(context).getCollectInfoDao().
    }

    /**
     * 删除所有数据
     * @param context
     * @return 200表示成功，505表示失败
     */
    public int deleteAll(Context context){
        try {
            DbManager.getInstance(context).getDaoSession(context).getPushInfoDao().deleteAll();
            return 200;
        } catch (Exception e) {
            e.printStackTrace();
            return 505;
        }
    }

    /**
     * 更新某条记录
     * @param context
     * @param pushInfo
     * @return 200表示成功，505表示失败
     */
    public int updateData(Context context, PushInfo pushInfo){
        try {
            DbManager.getInstance(context).getDaoSession(context).getPushInfoDao().update(pushInfo);
            return 200;
        } catch (Exception e) {
            e.printStackTrace();
            return 505;
        }
    }

    /**
     * 查询所有记录
     * @param context
     * @return 失败返回null
     */
    public List<PushInfo> queryAll(Context context){
        try {
            QueryBuilder<PushInfo> builder = DbManager.getInstance(context).getDaoSession(context).getPushInfoDao().queryBuilder();
            return builder.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据id来查询记录
     * @param context
     * @param id
     * @return 失败返回null
     */
    public List<PushInfo> query(Context context, long id){
        try {
            QueryBuilder<PushInfo> builder = DbManager.getInstance(context).getDaoSession(context).getPushInfoDao().queryBuilder();
            /**
             * 返回当前id的数据集合,当然where(这里面可以有多组，做为条件);
             * 这里build.list()；与where(AttentionInfoDao.Properties.User_id.eq(user_id)).list()结果是一样的；
             * 在QueryBuilder类中list()方法return build().list();
             *
             */
            // Query<CollectInfo> build = builder.where(CollectInfoDao.Properties.User_id.eq(user_id)).build();
            // List<CollectInfo> list = build.list();
            return builder.where(PushInfoDao.Properties.Id.eq(id)).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据id和信息的id来查询
     * @param context
     * @param id
     * @param item_id
     * @return 失败返回null
     */
    public List<PushInfo> query(Context context, long id, long item_id){
        try {
            QueryBuilder<PushInfo> builder = DbManager.getInstance(context).getDaoSession(context).getPushInfoDao().queryBuilder();
            return builder.where(PushInfoDao.Properties.Id.eq(id), PushInfoDao.Properties.Item_id.eq(item_id)).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
