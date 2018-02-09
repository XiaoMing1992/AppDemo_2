package guyuanjun.com.myappdemo.db;

import android.content.Context;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import guyuanjun.com.myappdemo.bean.AdminInfo;
import guyuanjun.com.myappdemo.greendao.AdminInfoDao;

/**
 * Created by Administrator on 2017/8/30 0030.
 */

public class AdminInfoDaoOpe {

    private static volatile AdminInfoDaoOpe instance = null;

    private AdminInfoDaoOpe(){

    }

    public static AdminInfoDaoOpe getInstance(){
        if (instance == null){
            synchronized (AdminInfoDaoOpe.class){
                instance = new AdminInfoDaoOpe();
            }
        }
        return instance;
    }

    /**
     * 插入单条数据
     * @param context
     * @param adminInfo
     */
    public void insertData(Context context, AdminInfo adminInfo){
        DbManager.getInstance(context).getDaoSession(context).getAdminInfoDao().insert(adminInfo);
    }

    /**
     * 批量插入数据
     * @param context
     * @param adminInfoList
     */
    public void insertData(Context context, List<AdminInfo> adminInfoList){
        if (adminInfoList == null || adminInfoList.size()<=0)
            return;
        DbManager.getInstance(context).getDaoSession(context).getAdminInfoDao().insertInTx(adminInfoList);
    }

    /**
     * 添加数据至数据库，如果存在，将原来的数据覆盖
     * 内部代码判断了如果存在就update(entity);不存在就insert(entity)；
     *
     * @param context
     * @param adminInfo
     */
    public void save(Context context, AdminInfo adminInfo){
        DbManager.getInstance(context).getDaoSession(context).getAdminInfoDao().save(adminInfo);
    }

    /**
     * 删除某条记录
     * @param context
     * @param adminInfo
     */
    public void deleteData(Context context, AdminInfo adminInfo){
        DbManager.getInstance(context).getDaoSession(context).getAdminInfoDao().delete(adminInfo);
    }

    /**
     * 根据id来删除数据
     * @param context
     * @param id
     */
    public void deleteDataByKey(Context context, long id){
        DbManager.getInstance(context).getDaoSession(context).getAdminInfoDao().deleteByKey(id);
    }

    /**
     * 删除所有数据
     * @param context
     */
    public void deleteAll(Context context){
        DbManager.getInstance(context).getDaoSession(context).getAdminInfoDao().deleteAll();
    }

    /**
     * 更新某条记录
     * @param context
     * @param adminInfo
     */
    public void updateData(Context context, AdminInfo adminInfo){
        DbManager.getInstance(context).getDaoSession(context).getAdminInfoDao().update(adminInfo);
    }

    /**
     * 查询所有记录
     * @param context
     * @return
     */
    public List<AdminInfo> queryAll(Context context){
        QueryBuilder<AdminInfo> builder = DbManager.getInstance(context).getDaoSession(context).getAdminInfoDao().queryBuilder();
        return builder.list();
    }

    /**
     * 根据id来查询记录
     * @param context
     * @param id
     * @return
     */
    public List<AdminInfo> query(Context context, long id){
        QueryBuilder<AdminInfo> builder = DbManager.getInstance(context).getDaoSession(context).getAdminInfoDao().queryBuilder();
        /**
         * 返回当前id的数据集合,当然where(这里面可以有多组，做为条件);
         * 这里build.list()；与where(StudentDao.Properties.Id.eq(id)).list()结果是一样的；
         * 在QueryBuilder类中list()方法return build().list();
         *
         */
        // Query<UserInfo> build = builder.where(UserInfoDao.Properties.Id.eq(id)).build();
        // List<UserInfo> list = build.list();
        return builder.where(AdminInfoDao.Properties.Id.eq(id)).list();
    }

    /**
     * 根据用户名和密码来查询
     * @param context
     * @param adminName
     * @param password
     * @return
     */
    public List<AdminInfo> query(Context context, String adminName, String password){
        QueryBuilder<AdminInfo> builder = DbManager.getInstance(context).getDaoSession(context).getAdminInfoDao().queryBuilder();
        return builder.where(AdminInfoDao.Properties.Adminname.eq(adminName), AdminInfoDao.Properties.Password.eq(password)).list();
    }

    /**
     * 根据用户名来查询
     * @param context
     * @param adminName
     * @return
     */
    public List<AdminInfo> queryByAdminname(Context context, String adminName){
        QueryBuilder<AdminInfo> builder = DbManager.getInstance(context).getDaoSession(context).getAdminInfoDao().queryBuilder();
        return builder.where(AdminInfoDao.Properties.Adminname.eq(adminName)).list();
    }
}
