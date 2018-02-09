package guyuanjun.com.myappdemo.db;

import android.content.Context;
import org.greenrobot.greendao.query.QueryBuilder;
import java.util.List;
import guyuanjun.com.myappdemo.bean.CommentInfo;
import guyuanjun.com.myappdemo.greendao.CommentInfoDao;

/**
 * Created by HP on 2017-4-17.
 */

public class CommentInfoDaoOpe {
    private static volatile CommentInfoDaoOpe instance = null;

    private CommentInfoDaoOpe(){

    }

    public static CommentInfoDaoOpe getInstance(){
        if (instance == null){
            synchronized (CommentInfoDaoOpe.class){
                instance = new CommentInfoDaoOpe();
            }
        }
        return instance;
    }

    /**
     * 插入单条数据
     * @param context
     * @param commentInfo
     */
    public void insertData(Context context, CommentInfo commentInfo){
        DbManager.getInstance(context).getDaoSession(context).getCommentInfoDao().insert(commentInfo);
    }

    /**
     * 批量插入数据
     * @param context
     * @param commentInfoList
     */
    public void insertData(Context context, List<CommentInfo> commentInfoList){
        if (commentInfoList == null || commentInfoList.size()<=0)
            return;
        DbManager.getInstance(context).getDaoSession(context).getCommentInfoDao().insertInTx(commentInfoList);
    }

    /**
     * 添加数据至数据库，如果存在，将原来的数据覆盖
     * 内部代码判断了如果存在就update(entity);不存在就insert(entity)；
     *
     * @param context
     * @param commentInfo
     */
    public void save(Context context, CommentInfo commentInfo){
        DbManager.getInstance(context).getDaoSession(context).getCommentInfoDao().save(commentInfo);
    }

    /**
     * 删除某条记录
     * @param context
     * @param commentInfo
     */
    public void deleteData(Context context, CommentInfo commentInfo){
        DbManager.getInstance(context).getDaoSession(context).getCommentInfoDao().delete(commentInfo);
    }

    /**
     * 根据id来删除数据
     * @param context
     * @param id
     */
    public void deleteDataByKey(Context context, long id){
        DbManager.getInstance(context).getDaoSession(context).getCommentInfoDao().deleteByKey(id);
    }

    /**
     * 删除所有数据
     * @param context
     */
    public void deleteAll(Context context){
        DbManager.getInstance(context).getDaoSession(context).getCommentInfoDao().deleteAll();
    }

    /**
     * 更新某条记录
     * @param context
     * @param commentInfo
     */
    public void updateData(Context context, CommentInfo commentInfo){
        DbManager.getInstance(context).getDaoSession(context).getCommentInfoDao().update(commentInfo);
    }

    /**
     * 查询所有记录
     * @param context
     * @return
     */
    public List<CommentInfo> queryAll(Context context){
        QueryBuilder<CommentInfo> builder = DbManager.getInstance(context).getDaoSession(context).getCommentInfoDao().queryBuilder();
        return builder.list();
    }

    /**
     * 根据user_id来查询记录
     * @param context
     * @param user_id
     * @return
     */
    public List<CommentInfo> query(Context context, long user_id){
        QueryBuilder<CommentInfo> builder = DbManager.getInstance(context).getDaoSession(context).getCommentInfoDao().queryBuilder();
        /**
         * 返回当前id的数据集合,当然where(这里面可以有多组，做为条件);
         * 这里build.list()；与where(CommentInfoDao.Properties.User_id.eq(user_id)).list()结果是一样的；
         * 在QueryBuilder类中list()方法return build().list();
         *
         */
        // Query<CommentInfo> build = builder.where(CommentInfoDao.Properties.User_id.eq(user_id)).build();
        // List<CommentInfo> list = build.list();
        return builder.where(CommentInfoDao.Properties.User_id.eq(user_id)).list();
    }

    /**
     * 根据用户的id和收藏的信息的id来查询
     * @param context
     * @param user_id
     * @param item_id
     * @return
     */
    public List<CommentInfo> query(Context context, long user_id, long item_id){
        QueryBuilder<CommentInfo> builder = DbManager.getInstance(context).getDaoSession(context).getCommentInfoDao().queryBuilder();
        return builder.where(CommentInfoDao.Properties.User_id.eq(user_id), CommentInfoDao.Properties.Item_id.eq(item_id)).list();
    }

    /**
     * 根据用户的id和收藏的信息的id来查询
     * @param context
     * @param item_id
     * @return
     */
    public List<CommentInfo> queryByItemId(Context context, long item_id){
        QueryBuilder<CommentInfo> builder = DbManager.getInstance(context).getDaoSession(context).getCommentInfoDao().queryBuilder();
        return builder.where(CommentInfoDao.Properties.Item_id.eq(item_id)).list();
    }
}
