package guyuanjun.com.myappdemo.db;

/**
 * Created by HP on 2017-4-11.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import guyuanjun.com.myappdemo.greendao.DaoMaster;
import guyuanjun.com.myappdemo.greendao.DaoSession;

public class DbManager {

    // 是否加密
    public static final boolean ENCRYPTED = true;

    private static final String DB_NAME = "test1.db";
    private static volatile DbManager mDbManager;
    private final DaoMaster.DevOpenHelper mDevOpenHelper;
    //private static DaoMaster mDaoMaster;
    private  final DaoSession mDaoSession;

    private Context mContext;

    private DbManager(Context context) {
        this.mContext = context;
        // 初始化数据库信息
        mDevOpenHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
        SQLiteDatabase writableDatabase = mDevOpenHelper.getWritableDatabase();
        DaoMaster mDaoMaster = new DaoMaster(writableDatabase);
        mDaoSession = mDaoMaster.newSession();
        //getDaoMaster(context);
        //getDaoSession(context);
    }

    public static DbManager getInstance(Context context) {
        if (null == mDbManager) {
            synchronized (DbManager.class) {
                if (null == mDbManager) {
                    mDbManager = new DbManager(context);
                }
            }
        }
        return mDbManager;
    }

    /**
     * 获取可读数据库
     *
     * @param context
     * @return
     */
/*    public static SQLiteDatabase getReadableDatabase(Context context) {
        if (null == mDevOpenHelper) {
            getInstance(context);
            //mDevOpenHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
        }
        SQLiteDatabase db = mDevOpenHelper.getWritableDatabase();
        return db;
    }*/

    /**
     * 获取可写数据库
     *
     * @param context
     * @return
     */
/*    public static SQLiteDatabase getWritableDatabase(Context context) {
        if (null == mDevOpenHelper) {
            getInstance(context);
            //mDevOpenHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
        }
        //mDevOpenHelper = new DaoMaster.DevOpenHelper(context, DB_NAME);
        SQLiteDatabase db = mDevOpenHelper.getWritableDatabase();
        return db;
    }*/

    /**
     * 获取DaoMaster
     *
     * @param context
     * @return
     */
/*     public static DaoMaster getDaoMaster(Context context) {
       if (null == mDaoMaster) {
            synchronized (DbManager.class) {
                if (null == mDaoMaster) {
                    //mDaoMaster = new DaoMaster(getWritableDatabase(context));
                    //mDaoMaster = new DaoMaster(mDevOpenHelper.getWritableDb());
                }
            }
        }
        return mDaoMaster;
    }*/

    /**
     * 获取DaoSession
     *
     * @param context
     * @return
     */
    public DaoSession getDaoSession(Context context) {
/*        if (null == mDaoSession) {
            synchronized (DbManager.class) {
                mDaoSession = getDaoMaster(context).newSession();
            }
        }*/
        return mDaoSession;
    }
}

