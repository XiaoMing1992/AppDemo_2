package guyuanjun.com.myappdemo.db;

import android.content.Context;
import org.greenrobot.greendao.query.QueryBuilder;
import java.util.List;
import guyuanjun.com.myappdemo.bean.WeitoutiaoInfo;
import guyuanjun.com.myappdemo.greendao.WeitoutiaoInfoDao;

/**
 * Created by HP on 2017-5-5.
 */

public class WeitoutiaoInfoDaoOpe {
    private static volatile WeitoutiaoInfoDaoOpe instance = null;

    private WeitoutiaoInfoDaoOpe(){

    }

    public static WeitoutiaoInfoDaoOpe getInstance(){
        if (instance == null){
            synchronized (WeitoutiaoInfoDaoOpe.class){
                instance = new WeitoutiaoInfoDaoOpe();
            }
        }
        return instance;
    }

    /**
     * 插入单条数据
     * @param context
     * @param weitoutiaoInfo
     */
    public int insertData(Context context, WeitoutiaoInfo weitoutiaoInfo){
        try {
            DbManager.getInstance(context).getDaoSession(context).getWeitoutiaoInfoDao().insert(weitoutiaoInfo);
            return 200;
        } catch (Exception e) {
            e.printStackTrace();
            return 505;
        }
    }

    /**
     * 批量插入数据
     * @param context
     * @param weitoutiaoInfoList
     */
    public void insertData(Context context, List<WeitoutiaoInfo> weitoutiaoInfoList){
        if (weitoutiaoInfoList == null || weitoutiaoInfoList.size()<=0)
            return;
        DbManager.getInstance(context).getDaoSession(context).getWeitoutiaoInfoDao().insertInTx(weitoutiaoInfoList);
    }

    /**
     * 添加数据至数据库，如果存在，将原来的数据覆盖
     * 内部代码判断了如果存在就update(entity);不存在就insert(entity)；
     *
     * @param context
     * @param weitoutiaoInfo
     * @return 200表示成功，505表示失败
     */
    public int save(Context context, WeitoutiaoInfo weitoutiaoInfo){
        try {
            DbManager.getInstance(context).getDaoSession(context).getWeitoutiaoInfoDao().save(weitoutiaoInfo);
            return 200;
        } catch (Exception e) {
            e.printStackTrace();
            return 505;
        }
    }

    /**
     * 删除某条记录
     * @param context
     * @param weitoutiaoInfo
     */
    public void deleteData(Context context, WeitoutiaoInfo weitoutiaoInfo){
        DbManager.getInstance(context).getDaoSession(context).getWeitoutiaoInfoDao().delete(weitoutiaoInfo);
    }

    /**
     * 根据id来删除数据
     * @param context
     * @param id
     */
    public void deleteDataByKey(Context context, long id){
        DbManager.getInstance(context).getDaoSession(context).getWeitoutiaoInfoDao().deleteByKey(id);
    }

    /**
     * 删除所有数据
     * @param context
     */
    public void deleteAll(Context context){
        DbManager.getInstance(context).getDaoSession(context).getWeitoutiaoInfoDao().deleteAll();
    }

    /**
     * 更新某条记录
     * @param context
     * @param weitoutiaoInfo
     */
    public void updateData(Context context, WeitoutiaoInfo weitoutiaoInfo){
        DbManager.getInstance(context).getDaoSession(context).getWeitoutiaoInfoDao().update(weitoutiaoInfo);
    }

    /**
     * 查询所有记录
     * @param context
     * @return
     */
    public List<WeitoutiaoInfo> queryAll(Context context){
        QueryBuilder<WeitoutiaoInfo> builder = DbManager.getInstance(context).getDaoSession(context).getWeitoutiaoInfoDao().queryBuilder();
        return builder.list();
    }

    /**
     * 根据user_id来查询记录
     * @param context
     * @param user_id
     * @return
     */
    public List<WeitoutiaoInfo> query(Context context, long user_id){
        QueryBuilder<WeitoutiaoInfo> builder = DbManager.getInstance(context).getDaoSession(context).getWeitoutiaoInfoDao().queryBuilder();
        /**
         * 返回当前id的数据集合,当然where(这里面可以有多组，做为条件);
         * 这里build.list()；与where(NewsItemInfoDao.Properties.User_id.eq(user_id)).list()结果是一样的；
         * 在QueryBuilder类中list()方法return build().list();
         *
         */
        // Query<CommentInfo> build = builder.where(NewsItemInfoDao.Properties.User_id.eq(user_id)).build();
        // List<NewsItemInfo> list = build.list();
        return builder.where(WeitoutiaoInfoDao.Properties.User_id.eq(user_id)).list();
    }

    /**
     * 根据用户的id和信息的id来查询
     * @param context
     * @param user_id
     * @param item_id
     * @return 失败返回null
     */
    public List<WeitoutiaoInfo> query(Context context, long user_id, long item_id){
        try {
            QueryBuilder<WeitoutiaoInfo> builder = DbManager.getInstance(context).getDaoSession(context).getWeitoutiaoInfoDao().queryBuilder();
            return builder.where(WeitoutiaoInfoDao.Properties.User_id.eq(user_id), WeitoutiaoInfoDao.Properties.Id.eq(item_id)).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据信息的id来查询
     * @param context
     * @param item_id
     * @return
     */
    public List<WeitoutiaoInfo> queryByItemId(Context context, long item_id){
        try {
            QueryBuilder<WeitoutiaoInfo> builder = DbManager.getInstance(context).getDaoSession(context).getWeitoutiaoInfoDao().queryBuilder();
            return builder.where(WeitoutiaoInfoDao.Properties.Id.eq(item_id)).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}


