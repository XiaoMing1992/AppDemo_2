package guyuanjun.com.client.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by HP on 2018-3-29.
 */

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    private static volatile MySQLiteOpenHelper instance = null;
    private final static int VERSION = 1;
    private final static String DB_NAME = "clientchat.db";
    private final String CREATE_TABLE = "create table if not exists" + " " + DB_INFO.TABLE_NAME + "("
            + DB_INFO.ID + " " + "integer not null primary key" + ","
            + DB_INFO.INFO_ID + " " + "integer not null" + ","
            + DB_INFO.FROM_ID + " " + "varchar(200)" + ","
            + DB_INFO.TO_ID + " " + "varchar(200)" + ","
            + DB_INFO.MSG + " " + "text" + ","
            + DB_INFO.TIME + " " + "varchar(200)"+ ","
            + DB_INFO.TYPE + " " + "int" + ");";

    /**
     * @param context 上下文对象
     * @param name    数据库名称
     * @param factory factory
     * @param version 当前数据库的版本，值必须是整数并且是递增的状态
     */
    private MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    private MySQLiteOpenHelper(Context context, String name, int version) {
        this(context, name, null, version);
    }

    private MySQLiteOpenHelper(Context context, String name) {
        this(context, name, VERSION);
    }

    public static MySQLiteOpenHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (MySQLiteOpenHelper.class) {
                if (instance == null) {
                    instance = new MySQLiteOpenHelper(context, DB_NAME, VERSION);
                }
            }
        }
        return instance;
    }

    //当数据库创建的时候被调用
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        System.out.println("创建数据库和表");
        sqLiteDatabase.execSQL(CREATE_TABLE);
        //数据库实际上是没有被创建或者打开的，直到getWritableDatabase()
        // 或者 getReadableDatabase() 方法中的一个被调用时才会进行创建或者打开
    }

    //数据库升级时调用
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int ollVersion, int newVersion) {
        System.out.println("更新数据库版本为:"+newVersion);
    }
}
