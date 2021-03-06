package guyuanjun.com.myappdemo.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import guyuanjun.com.myappdemo.bean.UserInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USER_INFO".
*/
public class UserInfoDao extends AbstractDao<UserInfo, Long> {

    public static final String TABLENAME = "USER_INFO";

    /**
     * Properties of entity UserInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Username = new Property(1, String.class, "username", false, "USERNAME");
        public final static Property Phone = new Property(2, String.class, "phone", false, "PHONE");
        public final static Property Nickname = new Property(3, String.class, "nickname", false, "NICKNAME");
        public final static Property Password = new Property(4, String.class, "password", false, "PASSWORD");
        public final static Property Email = new Property(5, String.class, "email", false, "EMAIL");
        public final static Property Head_path = new Property(6, String.class, "head_path", false, "HEAD_PATH");
        public final static Property Avator = new Property(7, String.class, "avator", false, "AVATOR");
        public final static Property CreateTime = new Property(8, String.class, "createTime", false, "CREATE_TIME");
        public final static Property Salt = new Property(9, String.class, "salt", false, "SALT");
        public final static Property Age = new Property(10, Integer.class, "age", false, "AGE");
        public final static Property Sex = new Property(11, String.class, "sex", false, "SEX");
        public final static Property City = new Property(12, String.class, "city", false, "CITY");
        public final static Property Score = new Property(13, Integer.class, "score", false, "SCORE");
        public final static Property InvitationCode = new Property(14, String.class, "invitationCode", false, "INVITATION_CODE");
    }


    public UserInfoDao(DaoConfig config) {
        super(config);
    }
    
    public UserInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER_INFO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"USERNAME\" TEXT," + // 1: username
                "\"PHONE\" TEXT," + // 2: phone
                "\"NICKNAME\" TEXT," + // 3: nickname
                "\"PASSWORD\" TEXT," + // 4: password
                "\"EMAIL\" TEXT," + // 5: email
                "\"HEAD_PATH\" TEXT," + // 6: head_path
                "\"AVATOR\" TEXT," + // 7: avator
                "\"CREATE_TIME\" TEXT," + // 8: createTime
                "\"SALT\" TEXT," + // 9: salt
                "\"AGE\" INTEGER," + // 10: age
                "\"SEX\" TEXT," + // 11: sex
                "\"CITY\" TEXT," + // 12: city
                "\"SCORE\" INTEGER," + // 13: score
                "\"INVITATION_CODE\" TEXT);"); // 14: invitationCode
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USER_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, UserInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String username = entity.getUsername();
        if (username != null) {
            stmt.bindString(2, username);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(3, phone);
        }
 
        String nickname = entity.getNickname();
        if (nickname != null) {
            stmt.bindString(4, nickname);
        }
 
        String password = entity.getPassword();
        if (password != null) {
            stmt.bindString(5, password);
        }
 
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(6, email);
        }
 
        String head_path = entity.getHead_path();
        if (head_path != null) {
            stmt.bindString(7, head_path);
        }
 
        String avator = entity.getAvator();
        if (avator != null) {
            stmt.bindString(8, avator);
        }
 
        String createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindString(9, createTime);
        }
 
        String salt = entity.getSalt();
        if (salt != null) {
            stmt.bindString(10, salt);
        }
 
        Integer age = entity.getAge();
        if (age != null) {
            stmt.bindLong(11, age);
        }
 
        String sex = entity.getSex();
        if (sex != null) {
            stmt.bindString(12, sex);
        }
 
        String city = entity.getCity();
        if (city != null) {
            stmt.bindString(13, city);
        }
 
        Integer score = entity.getScore();
        if (score != null) {
            stmt.bindLong(14, score);
        }
 
        String invitationCode = entity.getInvitationCode();
        if (invitationCode != null) {
            stmt.bindString(15, invitationCode);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, UserInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String username = entity.getUsername();
        if (username != null) {
            stmt.bindString(2, username);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(3, phone);
        }
 
        String nickname = entity.getNickname();
        if (nickname != null) {
            stmt.bindString(4, nickname);
        }
 
        String password = entity.getPassword();
        if (password != null) {
            stmt.bindString(5, password);
        }
 
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(6, email);
        }
 
        String head_path = entity.getHead_path();
        if (head_path != null) {
            stmt.bindString(7, head_path);
        }
 
        String avator = entity.getAvator();
        if (avator != null) {
            stmt.bindString(8, avator);
        }
 
        String createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindString(9, createTime);
        }
 
        String salt = entity.getSalt();
        if (salt != null) {
            stmt.bindString(10, salt);
        }
 
        Integer age = entity.getAge();
        if (age != null) {
            stmt.bindLong(11, age);
        }
 
        String sex = entity.getSex();
        if (sex != null) {
            stmt.bindString(12, sex);
        }
 
        String city = entity.getCity();
        if (city != null) {
            stmt.bindString(13, city);
        }
 
        Integer score = entity.getScore();
        if (score != null) {
            stmt.bindLong(14, score);
        }
 
        String invitationCode = entity.getInvitationCode();
        if (invitationCode != null) {
            stmt.bindString(15, invitationCode);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public UserInfo readEntity(Cursor cursor, int offset) {
        UserInfo entity = new UserInfo();
        readEntity(cursor, entity, offset);
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, UserInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUsername(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPhone(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setNickname(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setPassword(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setEmail(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setHead_path(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setAvator(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setCreateTime(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setSalt(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setAge(cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10));
        entity.setSex(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setCity(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setScore(cursor.isNull(offset + 13) ? null : cursor.getInt(offset + 13));
        entity.setInvitationCode(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(UserInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(UserInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(UserInfo entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
