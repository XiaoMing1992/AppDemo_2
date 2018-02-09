package guyuanjun.com.myappdemo.db;

import android.content.Context;
import org.greenrobot.greendao.query.QueryBuilder;
import java.util.List;
import guyuanjun.com.myappdemo.bean.NewsItemInfo;
import guyuanjun.com.myappdemo.greendao.NewsItemInfoDao;

/**
 * Created by HP on 2017-4-18.
 */

public class NewsItemInfoDaoOpe {
    private static volatile NewsItemInfoDaoOpe instance = null;

    private NewsItemInfoDaoOpe(){

    }

    public static NewsItemInfoDaoOpe getInstance(){
        if (instance == null){
            synchronized (NewsItemInfoDaoOpe.class){
                instance = new NewsItemInfoDaoOpe();
            }
        }
        return instance;
    }

    /**
     * 插入单条数据
     * @param context
     * @param newsItemInfo
     */
    public void insertData(Context context, NewsItemInfo newsItemInfo){
        DbManager.getInstance(context).getDaoSession(context).getNewsItemInfoDao().insert(newsItemInfo);
    }

    /**
     * 批量插入数据
     * @param context
     * @param newsItemInfoList
     */
    public void insertData(Context context, List<NewsItemInfo> newsItemInfoList){
        if (newsItemInfoList == null || newsItemInfoList.size()<=0)
            return;
        DbManager.getInstance(context).getDaoSession(context).getNewsItemInfoDao().insertInTx(newsItemInfoList);
    }

    /**
     * 添加数据至数据库，如果存在，将原来的数据覆盖
     * 内部代码判断了如果存在就update(entity);不存在就insert(entity)；
     *
     * @param context
     * @param newsItemInfo
     */
    public void save(Context context, NewsItemInfo newsItemInfo){
        DbManager.getInstance(context).getDaoSession(context).getNewsItemInfoDao().save(newsItemInfo);
    }

    /**
     * 删除某条记录
     * @param context
     * @param newsItemInfo
     */
    public void deleteData(Context context, NewsItemInfo newsItemInfo){
        DbManager.getInstance(context).getDaoSession(context).getNewsItemInfoDao().delete(newsItemInfo);
    }

    /**
     * 根据id来删除数据
     * @param context
     * @param id
     */
    public void deleteDataByKey(Context context, long id){
        DbManager.getInstance(context).getDaoSession(context).getNewsItemInfoDao().deleteByKey(id);
    }

    /**
     * 删除所有数据
     * @param context
     */
    public void deleteAll(Context context){
        DbManager.getInstance(context).getDaoSession(context).getNewsItemInfoDao().deleteAll();
    }

    /**
     * 更新某条记录
     * @param context
     * @param newsItemInfo
     */
    public void updateData(Context context, NewsItemInfo newsItemInfo){
        DbManager.getInstance(context).getDaoSession(context).getNewsItemInfoDao().update(newsItemInfo);
    }

    /**
     * 查询所有记录
     * @param context
     * @return
     */
    public List<NewsItemInfo> queryAll(Context context){
        QueryBuilder<NewsItemInfo> builder = DbManager.getInstance(context).getDaoSession(context).getNewsItemInfoDao().queryBuilder();
        return builder.list();
    }

    /**
     * 根据user_id来查询记录
     * @param context
     * @param user_id
     * @return
     */
    public List<NewsItemInfo> query(Context context, long user_id){
        QueryBuilder<NewsItemInfo> builder = DbManager.getInstance(context).getDaoSession(context).getNewsItemInfoDao().queryBuilder();
        /**
         * 返回当前id的数据集合,当然where(这里面可以有多组，做为条件);
         * 这里build.list()；与where(NewsItemInfoDao.Properties.User_id.eq(user_id)).list()结果是一样的；
         * 在QueryBuilder类中list()方法return build().list();
         *
         */
        // Query<CommentInfo> build = builder.where(NewsItemInfoDao.Properties.User_id.eq(user_id)).build();
        // List<NewsItemInfo> list = build.list();
        return builder.where(NewsItemInfoDao.Properties.User_id.eq(user_id)).list();
    }

    /**
     * 根据用户的id和信息的id来查询
     * @param context
     * @param user_id
     * @param item_id
     * @return
     */
    public List<NewsItemInfo> query(Context context, long user_id, long item_id){
        QueryBuilder<NewsItemInfo> builder = DbManager.getInstance(context).getDaoSession(context).getNewsItemInfoDao().queryBuilder();
        return builder.where(NewsItemInfoDao.Properties.User_id.eq(user_id), NewsItemInfoDao.Properties.Id.eq(item_id)).list();
    }

    /**
     * 根据信息的id来查询
     * @param context
     * @param item_id
     * @return
     */
    public List<NewsItemInfo> queryByItemId(Context context, long item_id){
        QueryBuilder<NewsItemInfo> builder = DbManager.getInstance(context).getDaoSession(context).getNewsItemInfoDao().queryBuilder();
        return builder.where(NewsItemInfoDao.Properties.Id.eq(item_id)).list();
    }
}

