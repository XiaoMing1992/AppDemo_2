package guyuanjun.com.myappdemo.db;

import android.content.Context;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import guyuanjun.com.myappdemo.bean.AttentionInfo;
import guyuanjun.com.myappdemo.bean.CollectInfo;
import guyuanjun.com.myappdemo.greendao.AttentionInfoDao;
import guyuanjun.com.myappdemo.greendao.CollectInfoDao;

/**
 * Created by HP on 2017-5-9.
 */

public class AttentionInfoDaoOpe {

    private static volatile AttentionInfoDaoOpe instance = null;

    private AttentionInfoDaoOpe(){

    }

    public static AttentionInfoDaoOpe getInstance(){
        if (instance == null){
            synchronized (AttentionInfoDaoOpe.class){
                instance = new AttentionInfoDaoOpe();
            }
        }
        return instance;
    }

    /**
     * 插入单条数据
     * @param context
     * @param attentionInfo
     * @return 200表示成功，505表示失败
     */
    public int insertData(Context context, AttentionInfo attentionInfo){
        try {
            DbManager.getInstance(context).getDaoSession(context).getAttentionInfoDao().insert(attentionInfo);
            return 200;
        } catch (Exception e) {
            e.printStackTrace();
            return 505;
        }
    }

    /**
     * 批量插入数据
     * @param context
     * @param attentionInfoList
     * @return 200表示成功，404表示客户端传入的参数有问题，505表示失败
     */
    public int insertData(Context context, List<AttentionInfo> attentionInfoList){
        if (attentionInfoList == null || attentionInfoList.size()<=0)
            return 404;
        try {
            DbManager.getInstance(context).getDaoSession(context).getAttentionInfoDao().insertInTx(attentionInfoList);
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
     * @param attentionInfo
     * @return 200表示成功，505表示失败
     */
    public int save(Context context, AttentionInfo attentionInfo){
        try {
            DbManager.getInstance(context).getDaoSession(context).getAttentionInfoDao().save(attentionInfo);
            return 200;
        } catch (Exception e) {
            e.printStackTrace();
            return 505;
        }
    }

    /**
     * 删除某条记录
     * @param context
     * @param attentionInfo
     * @return 200表示成功，505表示失败
     */
    public int deleteData(Context context, AttentionInfo attentionInfo){
        try {
            DbManager.getInstance(context).getDaoSession(context).getAttentionInfoDao().delete(attentionInfo);
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
            DbManager.getInstance(context).getDaoSession(context).getAttentionInfoDao().deleteByKey(id);
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
            DbManager.getInstance(context).getDaoSession(context).getAttentionInfoDao().deleteAll();
            return 200;
        } catch (Exception e) {
            e.printStackTrace();
            return 505;
        }
    }

    /**
     * 更新某条记录
     * @param context
     * @param attentionInfo
     * @return 200表示成功，505表示失败
     */
    public int updateData(Context context, AttentionInfo attentionInfo){
        try {
            DbManager.getInstance(context).getDaoSession(context).getAttentionInfoDao().update(attentionInfo);
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
    public List<AttentionInfo> queryAll(Context context){
        try {
            QueryBuilder<AttentionInfo> builder = DbManager.getInstance(context).getDaoSession(context).getAttentionInfoDao().queryBuilder();
            return builder.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据user_id来查询记录
     * @param context
     * @param user_id
     * @return 失败返回null
     */
    public List<AttentionInfo> query(Context context, long user_id){
        try {
            QueryBuilder<AttentionInfo> builder = DbManager.getInstance(context).getDaoSession(context).getAttentionInfoDao().queryBuilder();
            /**
             * 返回当前id的数据集合,当然where(这里面可以有多组，做为条件);
             * 这里build.list()；与where(AttentionInfoDao.Properties.User_id.eq(user_id)).list()结果是一样的；
             * 在QueryBuilder类中list()方法return build().list();
             *
             */
            // Query<CollectInfo> build = builder.where(CollectInfoDao.Properties.User_id.eq(user_id)).build();
            // List<CollectInfo> list = build.list();
            return builder.where(AttentionInfoDao.Properties.User_id.eq(user_id)).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据用户的id和关注的微头条的id来查询
     * @param context
     * @param user_id
     * @param item_id
     * @return 失败返回null
     */
    public List<AttentionInfo> query(Context context, long user_id, long item_id){
        try {
            QueryBuilder<AttentionInfo> builder = DbManager.getInstance(context).getDaoSession(context).getAttentionInfoDao().queryBuilder();
            return builder.where(AttentionInfoDao.Properties.User_id.eq(user_id), AttentionInfoDao.Properties.Item_id.eq(item_id)).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

