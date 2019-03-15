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
 * DAO for table "SYS_ITEM_BEAN".
*/
public class SysItemBeanDao extends AbstractDao<SysItemBean, String> {

    public static final String TABLENAME = "SYS_ITEM_BEAN";

    /**
     * Properties of entity SysItemBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property ItemId = new Property(0, String.class, "itemId", true, "ITEM_ID");
        public final static Property ItemName = new Property(1, String.class, "itemName", false, "ITEM_NAME");
    }


    public SysItemBeanDao(DaoConfig config) {
        super(config);
    }
    
    public SysItemBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SYS_ITEM_BEAN\" (" + //
                "\"ITEM_ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: itemId
                "\"ITEM_NAME\" TEXT);"); // 1: itemName
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SYS_ITEM_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, SysItemBean entity) {
        stmt.clearBindings();
 
        String itemId = entity.getItemId();
        if (itemId != null) {
            stmt.bindString(1, itemId);
        }
 
        String itemName = entity.getItemName();
        if (itemName != null) {
            stmt.bindString(2, itemName);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, SysItemBean entity) {
        stmt.clearBindings();
 
        String itemId = entity.getItemId();
        if (itemId != null) {
            stmt.bindString(1, itemId);
        }
 
        String itemName = entity.getItemName();
        if (itemName != null) {
            stmt.bindString(2, itemName);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public SysItemBean readEntity(Cursor cursor, int offset) {
        SysItemBean entity = new SysItemBean( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // itemId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1) // itemName
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, SysItemBean entity, int offset) {
        entity.setItemId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setItemName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
     }
    
    @Override
    protected final String updateKeyAfterInsert(SysItemBean entity, long rowId) {
        return entity.getItemId();
    }
    
    @Override
    public String getKey(SysItemBean entity) {
        if(entity != null) {
            return entity.getItemId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(SysItemBean entity) {
        return entity.getItemId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
