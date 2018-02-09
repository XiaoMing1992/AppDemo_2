package guyuanjun.com.myappdemo.db;

import android.content.Context;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import guyuanjun.com.myappdemo.bean.AnswearInfo;
import guyuanjun.com.myappdemo.greendao.AnswearInfoDao;
import guyuanjun.com.myappdemo.utils.ErrorCode;

/**
 * Created by HP on 2017-6-20.
 */

public class AnswearInfoDaoOpe {
    private static volatile AnswearInfoDaoOpe instance = null;

    private AnswearInfoDaoOpe() {

    }

    public static AnswearInfoDaoOpe getInstance() {
        if (instance == null) {
            synchronized (AnswearInfoDaoOpe.class) {
                instance = new AnswearInfoDaoOpe();
            }
        }
        return instance;
    }

    /**
     * 插入单条数据
     * @param context
     * @param answearInfo
     * @return 200表示成功，505表示失败
     */
    public int insertData(Context context, AnswearInfo answearInfo){
        try {
            DbManager.getInstance(context).getDaoSession(context).getAnswearInfoDao().insert(answearInfo);
            return ErrorCode.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ErrorCode.SERVER_ERROR;
        }
    }

    /**
     * 批量插入数据
     * @param context
     * @param answearInfoList
     * @return 200表示成功，404表示客户端传入的参数有问题，505表示失败
     */
    public int insertData(Context context, List<AnswearInfo> answearInfoList){
        if (answearInfoList == null || answearInfoList.size()<=0)
            return ErrorCode.CLIENT_ERROR;
        try {
            DbManager.getInstance(context).getDaoSession(context).getAnswearInfoDao().insertInTx(answearInfoList);
            return ErrorCode.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ErrorCode.SERVER_ERROR;
        }
    }

    /**
     * 添加数据至数据库，如果存在，将原来的数据覆盖
     * 内部代码判断了如果存在就update(entity);不存在就insert(entity)；
     *
     * @param context
     * @param answearInfo
     * @return 200表示成功，505表示失败
     */
    public int save(Context context, AnswearInfo answearInfo){
        try {
            DbManager.getInstance(context).getDaoSession(context).getAnswearInfoDao().save(answearInfo);
            return ErrorCode.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ErrorCode.SERVER_ERROR;
        }
    }

    /**
     * 删除某条记录
     * @param context
     * @param answearInfo
     * @return 200表示成功，505表示失败
     */
    public int deleteData(Context context, AnswearInfo answearInfo){
        try {
            DbManager.getInstance(context).getDaoSession(context).getAnswearInfoDao().delete(answearInfo);
            return ErrorCode.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ErrorCode.SERVER_ERROR;
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
            DbManager.getInstance(context).getDaoSession(context).getAnswearInfoDao().deleteByKey(id);
            return ErrorCode.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ErrorCode.SERVER_ERROR;
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
            DbManager.getInstance(context).getDaoSession(context).getAnswearInfoDao().deleteAll();
            return ErrorCode.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ErrorCode.SERVER_ERROR;
        }
    }

    /**
     * 更新某条记录
     * @param context
     * @param answearInfo
     * @return 200表示成功，505表示失败
     */
    public int updateData(Context context, AnswearInfo answearInfo){
        try {
            DbManager.getInstance(context).getDaoSession(context).getAnswearInfoDao().update(answearInfo);
            return ErrorCode.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ErrorCode.SERVER_ERROR;
        }
    }

    /**
     * 查询所有记录
     * @param context
     * @return 失败返回null
     */
    public List<AnswearInfo> queryAll(Context context){
        try {
            QueryBuilder<AnswearInfo> builder = DbManager.getInstance(context).getDaoSession(context).getAnswearInfoDao().queryBuilder();
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
    public List<AnswearInfo> query(Context context, long id){
        try {
            QueryBuilder<AnswearInfo> builder = DbManager.getInstance(context).getDaoSession(context).getAnswearInfoDao().queryBuilder();
            /**
             * 返回当前id的数据集合,当然where(这里面可以有多组，做为条件);
             * 这里build.list()；与where(AttentionInfoDao.Properties.User_id.eq(user_id)).list()结果是一样的；
             * 在QueryBuilder类中list()方法return build().list();
             *
             */
            // Query<CollectInfo> build = builder.where(CollectInfoDao.Properties.User_id.eq(user_id)).build();
            // List<CollectInfo> list = build.list();
            return builder.where(AnswearInfoDao.Properties.Id.eq(id)).list();
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
    public List<AnswearInfo> query(Context context, long id, long item_id){
        try {
            QueryBuilder<AnswearInfo> builder = DbManager.getInstance(context).getDaoSession(context).getAnswearInfoDao().queryBuilder();
            return builder.where(AnswearInfoDao.Properties.Id.eq(id), AnswearInfoDao.Properties.Item_id.eq(item_id)).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据信息的id来查询
     * @param context
     * @param item_id
     * @return 失败返回null
     */
    public List<AnswearInfo> queryByItemId(Context context, long item_id){
        try {
            QueryBuilder<AnswearInfo> builder = DbManager.getInstance(context).getDaoSession(context).getAnswearInfoDao().queryBuilder();
            return builder.where(AnswearInfoDao.Properties.Item_id.eq(item_id)).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
