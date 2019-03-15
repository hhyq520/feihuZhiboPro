package cn.feihutv.zhibofeihu.data.db.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SYS_FONT_COLOR_BEAN".
*/
public class SysFontColorBeanDao extends AbstractDao<SysFontColorBean, String> {

    public static final String TABLENAME = "SYS_FONT_COLOR_BEAN";

    /**
     * Properties of entity SysFontColorBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Key = new Property(0, String.class, "key", true, "KEY");
        public final static Property KeyName = new Property(1, String.class, "keyName", false, "KEY_NAME");
        public final static Property Phone = new Property(2, String.class, "phone", false, "PHONE");
        public final static Property Pc = new Property(3, String.class, "pc", false, "PC");
    }


    public SysFontColorBeanDao(DaoConfig config) {
        super(config);
    }
    
    public SysFontColorBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SYS_FONT_COLOR_BEAN\" (" + //
                "\"KEY\" TEXT PRIMARY KEY NOT NULL ," + // 0: key
                "\"KEY_NAME\" TEXT," + // 1: keyName
                "\"PHONE\" TEXT," + // 2: phone
                "\"PC\" TEXT);"); // 3: pc
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SYS_FONT_COLOR_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, SysFontColorBean entity) {
        stmt.clearBindings();
 
        String key = entity.getKey();
        if (key != null) {
            stmt.bindString(1, key);
        }
 
        String keyName = entity.getKeyName();
        if (keyName != null) {
            stmt.bindString(2, keyName);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(3, phone);
        }
 
        String pc = entity.getPc();
        if (pc != null) {
            stmt.bindString(4, pc);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, SysFontColorBean entity) {
        stmt.clearBindings();
 
        String key = entity.getKey();
        if (key != null) {
            stmt.bindString(1, key);
        }
 
        String keyName = entity.getKeyName();
        if (keyName != null) {
            stmt.bindString(2, keyName);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(3, phone);
        }
 
        String pc = entity.getPc();
        if (pc != null) {
            stmt.bindString(4, pc);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public SysFontColorBean readEntity(Cursor cursor, int offset) {
        SysFontColorBean entity = new SysFontColorBean( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // key
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // keyName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // phone
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // pc
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, SysFontColorBean entity, int offset) {
        entity.setKey(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setKeyName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPhone(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setPc(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    @Override
    protected final String updateKeyAfterInsert(SysFontColorBean entity, long rowId) {
        return entity.getKey();
    }
    
    @Override
    public String getKey(SysFontColorBean entity) {
        if(entity != null) {
            return entity.getKey();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(SysFontColorBean entity) {
        return entity.getKey() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}