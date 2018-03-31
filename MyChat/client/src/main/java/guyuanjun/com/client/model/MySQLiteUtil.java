package guyuanjun.com.client.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 2018-3-29.
 */

public class MySQLiteUtil {

    public static synchronized long insert(Context context, final MyMessage msg) {
        if (msg == null) return -1;
        SQLiteDatabase db = MySQLiteOpenHelper.getInstance(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_INFO.INFO_ID, msg.getInfoId());
        values.put(DB_INFO.FROM_ID, msg.getFrom());
        values.put(DB_INFO.TO_ID, msg.getTo());
        values.put(DB_INFO.MSG, msg.getMsg());
        values.put(DB_INFO.TIME, msg.getTime());
        values.put(DB_INFO.TYPE, msg.getType());
        long id = db.insert(DB_INFO.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public static synchronized void delete(Context context, final MyMessage msg) {
        if (msg == null) return;
        SQLiteDatabase db = MySQLiteOpenHelper.getInstance(context).getWritableDatabase();
        String where = DB_INFO.ID + "=?";
        String[] params = new String[]{String.valueOf(msg.getId())};
        db.delete(DB_INFO.TABLE_NAME, where, params);
        db.close();
    }

    public static synchronized void delete(Context context, final long id) {
        if (id <= 0) return;
        SQLiteDatabase db = MySQLiteOpenHelper.getInstance(context).getWritableDatabase();
        String where = DB_INFO.ID + "=?";
        String[] params = new String[]{String.valueOf(id)};
        db.delete(DB_INFO.TABLE_NAME, where, params);
        db.close();
    }

    public static List<MyMessage> query(Context context, final String from_id) {
        if (from_id == null) return null;
        SQLiteDatabase db = MySQLiteOpenHelper.getInstance(context).getReadableDatabase();

        String where = DB_INFO.FROM_ID + "=?";
        String[] params = new String[]{from_id};
        String[] columns = new String[]{DB_INFO.ID, DB_INFO.INFO_ID, DB_INFO.TO_ID, DB_INFO.MSG, DB_INFO.TIME, DB_INFO.TYPE};

        List<MyMessage> infos = new ArrayList<>();
        Cursor cursor = db.query(DB_INFO.TABLE_NAME, columns, where, params, null, null, null);
        cursor.moveToFirst();
        while (cursor.moveToNext()){
            MyMessage info = new MyMessage();
            info.setId(cursor.getLong(cursor.getColumnIndex(DB_INFO.ID)));
            info.setInfoId(cursor.getInt(cursor.getColumnIndex(DB_INFO.INFO_ID)));
            info.setFrom(from_id);
            info.setTo(cursor.getString(cursor.getColumnIndex(DB_INFO.TO_ID)));
            info.setMsg(cursor.getString(cursor.getColumnIndex(DB_INFO.MSG)));
            info.setTime(cursor.getString(cursor.getColumnIndex(DB_INFO.TIME)));
            info.setType(cursor.getInt(cursor.getColumnIndex(DB_INFO.TYPE)));
            infos.add(info);
        }
        cursor.close();
        db.close();
        return infos;
    }

    public static List<MyMessage> query(Context context) {
        //if (from_id == null) return null;
        SQLiteDatabase db = MySQLiteOpenHelper.getInstance(context).getReadableDatabase();

        //String where = DB_INFO.FROM_ID + "=?";
        //String[] params = new String[]{from_id};
        String[] columns = new String[]{DB_INFO.ID, DB_INFO.INFO_ID, DB_INFO.FROM_ID, DB_INFO.TO_ID, DB_INFO.MSG, DB_INFO.TIME, DB_INFO.TYPE};

        List<MyMessage> infos = new ArrayList<>();
        Cursor cursor = db.query(DB_INFO.TABLE_NAME, columns, null, null, null, null, null);
        cursor.moveToFirst();
        while (cursor.moveToNext()){
            MyMessage info = new MyMessage();
            info.setId(cursor.getLong(cursor.getColumnIndex(DB_INFO.ID)));
            info.setInfoId(cursor.getInt(cursor.getColumnIndex(DB_INFO.INFO_ID)));
            info.setFrom(cursor.getString(cursor.getColumnIndex(DB_INFO.FROM_ID)));
            info.setTo(cursor.getString(cursor.getColumnIndex(DB_INFO.TO_ID)));
            info.setMsg(cursor.getString(cursor.getColumnIndex(DB_INFO.MSG)));
            info.setTime(cursor.getString(cursor.getColumnIndex(DB_INFO.TIME)));
            info.setType(cursor.getInt(cursor.getColumnIndex(DB_INFO.TYPE)));
            infos.add(info);
        }
        cursor.close();
        db.close();
        return infos;
    }

    public static synchronized void update(Context context, final MyMessage msg) {
        if (msg == null) return;
        SQLiteDatabase db = MySQLiteOpenHelper.getInstance(context).getWritableDatabase();
        String where = DB_INFO.ID + "=?";
        String[] params = new String[]{String.valueOf(msg.getId())};

        ContentValues values = new ContentValues();
        values.put(DB_INFO.INFO_ID, msg.getInfoId());
        values.put(DB_INFO.FROM_ID, msg.getFrom());
        values.put(DB_INFO.TO_ID, msg.getTo());
        values.put(DB_INFO.MSG, msg.getMsg());
        values.put(DB_INFO.TIME, msg.getTime());
        values.put(DB_INFO.TYPE, msg.getType());
        db.update(DB_INFO.TABLE_NAME, values, where, params);
        db.close();
    }
}
