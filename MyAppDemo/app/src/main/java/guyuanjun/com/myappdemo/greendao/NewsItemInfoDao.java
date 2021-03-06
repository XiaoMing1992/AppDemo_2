package guyuanjun.com.myappdemo.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import guyuanjun.com.myappdemo.bean.NewsItemInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "NEWS_ITEM_INFO".
*/
public class NewsItemInfoDao extends AbstractDao<NewsItemInfo, Long> {

    public static final String TABLENAME = "NEWS_ITEM_INFO";

    /**
     * Properties of entity NewsItemInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property User_id = new Property(1, Long.class, "user_id", false, "USER_ID");
        public final static Property Title = new Property(2, String.class, "title", false, "TITLE");
        public final static Property Where = new Property(3, String.class, "where", false, "WHERE");
        public final static Property Time = new Property(4, java.util.Date.class, "time", false, "TIME");
        public final static Property Img_path = new Property(5, String.class, "img_path", false, "IMG_PATH");
        public final static Property Img_path_list = new Property(6, String.class, "img_path_list", false, "IMG_PATH_LIST");
        public final static Property Content = new Property(7, String.class, "content", false, "CONTENT");
        public final static Property Flag_type = new Property(8, int.class, "flag_type", false, "FLAG_TYPE");
        public final static Property Praise_num = new Property(9, Integer.class, "praise_num", false, "PRAISE_NUM");
    }


    public NewsItemInfoDao(DaoConfig config) {
        super(config);
    }
    
    public NewsItemInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"NEWS_ITEM_INFO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"USER_ID\" INTEGER," + // 1: user_id
                "\"TITLE\" TEXT," + // 2: title
                "\"WHERE\" TEXT," + // 3: where
                "\"TIME\" INTEGER," + // 4: time
                "\"IMG_PATH\" TEXT," + // 5: img_path
                "\"IMG_PATH_LIST\" TEXT," + // 6: img_path_list
                "\"CONTENT\" TEXT," + // 7: content
                "\"FLAG_TYPE\" INTEGER NOT NULL ," + // 8: flag_type
                "\"PRAISE_NUM\" INTEGER);"); // 9: praise_num
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"NEWS_ITEM_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, NewsItemInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long user_id = entity.getUser_id();
        if (user_id != null) {
            stmt.bindLong(2, user_id);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(3, title);
        }
 
        String where = entity.getWhere();
        if (where != null) {
            stmt.bindString(4, where);
        }
 
        java.util.Date time = entity.getTime();
        if (time != null) {
            stmt.bindLong(5, time.getTime());
        }
 
        String img_path = entity.getImg_path();
        if (img_path != null) {
            stmt.bindString(6, img_path);
        }
 
        String img_path_list = entity.getImg_path_list();
        if (img_path_list != null) {
            stmt.bindString(7, img_path_list);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(8, content);
        }
        stmt.bindLong(9, entity.getFlag_type());
 
        Integer praise_num = entity.getPraise_num();
        if (praise_num != null) {
            stmt.bindLong(10, praise_num);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, NewsItemInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long user_id = entity.getUser_id();
        if (user_id != null) {
            stmt.bindLong(2, user_id);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(3, title);
        }
 
        String where = entity.getWhere();
        if (where != null) {
            stmt.bindString(4, where);
        }
 
        java.util.Date time = entity.getTime();
        if (time != null) {
            stmt.bindLong(5, time.getTime());
        }
 
        String img_path = entity.getImg_path();
        if (img_path != null) {
            stmt.bindString(6, img_path);
        }
 
        String img_path_list = entity.getImg_path_list();
        if (img_path_list != null) {
            stmt.bindString(7, img_path_list);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(8, content);
        }
        stmt.bindLong(9, entity.getFlag_type());
 
        Integer praise_num = entity.getPraise_num();
        if (praise_num != null) {
            stmt.bindLong(10, praise_num);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public NewsItemInfo readEntity(Cursor cursor, int offset) {
        NewsItemInfo entity = new NewsItemInfo();
        readEntity(cursor, entity, offset);
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, NewsItemInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUser_id(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setTitle(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setWhere(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setTime(cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)));
        entity.setImg_path(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setImg_path_list(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setContent(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setFlag_type(cursor.getInt(offset + 8));
        entity.setPraise_num(cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(NewsItemInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(NewsItemInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(NewsItemInfo entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
