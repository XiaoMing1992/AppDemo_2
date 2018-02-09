package guyuanjun.com.myappdemo.db;

import android.content.Context;

import org.greenrobot.greendao.query.QueryBuilder;
import java.util.List;
import guyuanjun.com.myappdemo.bean.LookupInfo;
import guyuanjun.com.myappdemo.greendao.LookupInfoDao;

/**
 * Created by HP on 2017-5-14.
 */

public class LookupInfoDaoOpe {
    private static volatile LookupInfoDaoOpe instance = null;

    private LookupInfoDaoOpe() {

    }

    public static LookupInfoDaoOpe getInstance() {
        if (instance == null) {
            synchronized (LookupInfoDaoOpe.class) {
                instance = new LookupInfoDaoOpe();
            }
        }
        return instance;
    }

    /**
     * 插入单条数据
     * @param context
     * @param lookupInfo
     * @return 200表示成功，505表示失败
     */
    public int insertData(Context context, LookupInfo lookupInfo){
        try {
            DbManager.getInstance(context).getDaoSession(context).getLookupInfoDao().insert(lookupInfo);
            return 200;
        } catch (Exception e) {
            e.printStackTrace();
            return 505;
        }
    }

    /**
     * 批量插入数据
     * @param context
     * @param lookupInfoList
     * @return 200表示成功，404表示客户端传入的参数有问题，505表示失败
     */
    public int insertData(Context context, List<LookupInfo> lookupInfoList){
        if (lookupInfoList == null || lookupInfoList.size()<=0)
            return 404;
        try {
            DbManager.getInstance(context).getDaoSession(context).getLookupInfoDao().insertInTx(lookupInfoList);
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
     * @param lookupInfo
     * @return 200表示成功，505表示失败
     */
    public int save(Context context, LookupInfo lookupInfo){
        try {
            DbManager.getInstance(context).getDaoSession(context).getLookupInfoDao().save(lookupInfo);
            return 200;
        } catch (Exception e) {
            e.printStackTrace();
            return 505;
        }
    }

    /**
     * 删除某条记录
     * @param context
     * @param lookupInfo
     * @return 200表示成功，505表示失败
     */
    public int deleteData(Context context, LookupInfo lookupInfo){
        try {
            DbManager.getInstance(context).getDaoSession(context).getLookupInfoDao().delete(lookupInfo);
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
            DbManager.getInstance(context).getDaoSession(context).getLookupInfoDao().deleteByKey(id);
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
            DbManager.getInstance(context).getDaoSession(context).getLookupInfoDao().deleteAll();
            return 200;
        } catch (Exception e) {
            e.printStackTrace();
            return 505;
        }
    }

    /**
     * 更新某条记录
     * @param context
     * @param lookupInfo
     * @return 200表示成功，505表示失败
     */
    public int updateData(Context context, LookupInfo lookupInfo){
        try {
            DbManager.getInstance(context).getDaoSession(context).getLookupInfoDao().update(lookupInfo);
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
    public List<LookupInfo> queryAll(Context context){
        try {
            QueryBuilder<LookupInfo> builder = DbManager.getInstance(context).getDaoSession(context).getLookupInfoDao().queryBuilder();
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
    public List<LookupInfo> query(Context context, long id){
        try {
            QueryBuilder<LookupInfo> builder = DbManager.getInstance(context).getDaoSession(context).getLookupInfoDao().queryBuilder();
            /**
             * 返回当前id的数据集合,当然where(这里面可以有多组，做为条件);
             * 这里build.list()；与where(AttentionInfoDao.Properties.User_id.eq(user_id)).list()结果是一样的；
             * 在QueryBuilder类中list()方法return build().list();
             *
             */
            // Query<CollectInfo> build = builder.where(CollectInfoDao.Properties.User_id.eq(user_id)).build();
            // List<CollectInfo> list = build.list();
            return builder.where(LookupInfoDao.Properties.Id.eq(id)).list();
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
    public List<LookupInfo> query(Context context, long id, long item_id){
        try {
            QueryBuilder<LookupInfo> builder = DbManager.getInstance(context).getDaoSession(context).getLookupInfoDao().queryBuilder();
            return builder.where(LookupInfoDao.Properties.Id.eq(id), LookupInfoDao.Properties.Item_id.eq(item_id)).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
