package guyuanjun.com.myappdemo.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import guyuanjun.com.myappdemo.bean.MobileInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MOBILE_INFO".
*/
public class MobileInfoDao extends AbstractDao<MobileInfo, Long> {

    public static final String TABLENAME = "MOBILE_INFO";

    /**
     * Properties of entity MobileInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property State = new Property(1, Integer.class, "state", false, "STATE");
        public final static Property Order_id = new Property(2, String.class, "order_id", false, "ORDER_ID");
        public final static Property Phone = new Property(3, String.class, "phone", false, "PHONE");
        public final static Property Chongzhi_time = new Property(4, java.util.Date.class, "chongzhi_time", false, "CHONGZHI_TIME");
        public final static Property Sporder_id = new Property(5, String.class, "sporder_id", false, "SPORDER_ID");
        public final static Property Uordercash = new Property(6, String.class, "uordercash", false, "UORDERCASH");
    }


    public MobileInfoDao(DaoConfig config) {
        super(config);
    }
    
    public MobileInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MOBILE_INFO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"STATE\" INTEGER," + // 1: state
                "\"ORDER_ID\" TEXT," + // 2: order_id
                "\"PHONE\" TEXT," + // 3: phone
                "\"CHONGZHI_TIME\" INTEGER," + // 4: chongzhi_time
                "\"SPORDER_ID\" TEXT," + // 5: sporder_id
                "\"UORDERCASH\" TEXT);"); // 6: uordercash
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MOBILE_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, MobileInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer state = entity.getState();
        if (state != null) {
            stmt.bindLong(2, state);
        }
 
        String order_id = entity.getOrder_id();
        if (order_id != null) {
            stmt.bindString(3, order_id);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(4, phone);
        }
 
        java.util.Date chongzhi_time = entity.getChongzhi_time();
        if (chongzhi_time != null) {
            stmt.bindLong(5, chongzhi_time.getTime());
        }
 
        String sporder_id = entity.getSporder_id();
        if (sporder_id != null) {
            stmt.bindString(6, sporder_id);
        }
 
        String uordercash = entity.getUordercash();
        if (uordercash != null) {
            stmt.bindString(7, uordercash);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, MobileInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer state = entity.getState();
        if (state != null) {
            stmt.bindLong(2, state);
        }
 
        String order_id = entity.getOrder_id();
        if (order_id != null) {
            stmt.bindString(3, order_id);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(4, phone);
        }
 
        java.util.Date chongzhi_time = entity.getChongzhi_time();
        if (chongzhi_time != null) {
            stmt.bindLong(5, chongzhi_time.getTime());
        }
 
        String sporder_id = entity.getSporder_id();
        if (sporder_id != null) {
            stmt.bindString(6, sporder_id);
        }
 
        String uordercash = entity.getUordercash();
        if (uordercash != null) {
            stmt.bindString(7, uordercash);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public MobileInfo readEntity(Cursor cursor, int offset) {
        MobileInfo entity = new MobileInfo();
        readEntity(cursor, entity, offset);
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, MobileInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setState(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setOrder_id(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setPhone(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setChongzhi_time(cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)));
        entity.setSporder_id(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setUordercash(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(MobileInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(MobileInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(MobileInfo entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
