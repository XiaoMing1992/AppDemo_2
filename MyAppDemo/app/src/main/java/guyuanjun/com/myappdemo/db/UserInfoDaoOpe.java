package guyuanjun.com.myappdemo.db;

import android.content.Context;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import guyuanjun.com.myappdemo.bean.UserInfo;
import guyuanjun.com.myappdemo.greendao.UserInfoDao;

/**
 * Created by HP on 2017-4-12.
 */

public class UserInfoDaoOpe {

    private static volatile UserInfoDaoOpe instance = null;

    private UserInfoDaoOpe(){

    }

    public static UserInfoDaoOpe getInstance(){
        if (instance == null){
            synchronized (UserInfoDaoOpe.class){
                instance = new UserInfoDaoOpe();
            }
        }
        return instance;
    }

    /**
     * 插入单条数据
     * @param context
     * @param userInfo
     */
    public void insertData(Context context, UserInfo userInfo){
        DbManager.getInstance(context).getDaoSession(context).getUserInfoDao().insert(userInfo);
    }

    /**
     * 批量插入数据
     * @param context
     * @param userInfoList
     */
    public void insertData(Context context, List<UserInfo> userInfoList){
        if (userInfoList == null || userInfoList.size()<=0)
            return;
        DbManager.getInstance(context).getDaoSession(context).getUserInfoDao().insertInTx(userInfoList);
    }

    /**
     * 添加数据至数据库，如果存在，将原来的数据覆盖
     * 内部代码判断了如果存在就update(entity);不存在就insert(entity)；
     *
     * @param context
     * @param userInfo
     */
    public void save(Context context, UserInfo userInfo){
        DbManager.getInstance(context).getDaoSession(context).getUserInfoDao().save(userInfo);
    }

    /**
     * 删除某条记录
     * @param context
     * @param userInfo
     */
    public void deleteData(Context context, UserInfo userInfo){
        DbManager.getInstance(context).getDaoSession(context).getUserInfoDao().delete(userInfo);
    }

    /**
     * 根据id来删除数据
     * @param context
     * @param id
     */
    public void deleteDataByKey(Context context, long id){
        DbManager.getInstance(context).getDaoSession(context).getUserInfoDao().deleteByKey(id);
    }

    /**
     * 删除所有数据
     * @param context
     */
    public void deleteAll(Context context){
        DbManager.getInstance(context).getDaoSession(context).getUserInfoDao().deleteAll();
    }

    /**
     * 更新某条记录
     * @param context
     * @param userInfo
     */
    public void updateData(Context context, UserInfo userInfo){
        DbManager.getInstance(context).getDaoSession(context).getUserInfoDao().update(userInfo);
    }

    /**
     * 查询所有记录
     * @param context
     * @return
     */
    public List<UserInfo> queryAll(Context context){
        QueryBuilder<UserInfo> builder = DbManager.getInstance(context).getDaoSession(context).getUserInfoDao().queryBuilder();
        return builder.list();
    }

    /**
     * 根据id来查询记录
     * @param context
     * @param id
     * @return
     */
    public List<UserInfo> query(Context context, long id){
        QueryBuilder<UserInfo> builder = DbManager.getInstance(context).getDaoSession(context).getUserInfoDao().queryBuilder();
        /**
         * 返回当前id的数据集合,当然where(这里面可以有多组，做为条件);
         * 这里build.list()；与where(StudentDao.Properties.Id.eq(id)).list()结果是一样的；
         * 在QueryBuilder类中list()方法return build().list();
         *
         */
        // Query<UserInfo> build = builder.where(UserInfoDao.Properties.Id.eq(id)).build();
        // List<UserInfo> list = build.list();
        return builder.where(UserInfoDao.Properties.Id.eq(id)).list();
    }

    /**
     * 根据用户名和密码来查询
     * @param context
     * @param userName
     * @param password
     * @return
     */
    public List<UserInfo> query(Context context, String userName, String password){
        QueryBuilder<UserInfo> builder = DbManager.getInstance(context).getDaoSession(context).getUserInfoDao().queryBuilder();
        return builder.where(UserInfoDao.Properties.Username.eq(userName), UserInfoDao.Properties.Password.eq(password)).list();
    }

    /**
     * 根据用户名来查询
     * @param context
     * @param userName
     * @return
     */
    public List<UserInfo> queryByUsername(Context context, String userName){
        QueryBuilder<UserInfo> builder = DbManager.getInstance(context).getDaoSession(context).getUserInfoDao().queryBuilder();
        return builder.where(UserInfoDao.Properties.Username.eq(userName)).list();
    }

    /**
     * 根据手机号来查询
     * @param context
     * @param phone
     * @return
     */
    public List<UserInfo> query(Context context, String phone){
        QueryBuilder<UserInfo> builder = DbManager.getInstance(context).getDaoSession(context).getUserInfoDao().queryBuilder();
        return builder.where(UserInfoDao.Properties.Phone.eq(phone)).list();
    }
}
