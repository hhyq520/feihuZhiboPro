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
 * DAO for table "MvDownLog".
*/
public class MvDownLogDao extends AbstractDao<MvDownLog, String> {

    public static final String TABLENAME = "MvDownLog";

    /**
     * Properties of entity MvDownLog.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "id", true, "id");
        public final static Property UserId = new Property(1, String.class, "userId", false, "userId");
        public final static Property MvId = new Property(2, String.class, "mvId", false, "mvId");
        public final static Property MvUrl = new Property(3, String.class, "mvUrl", false, "mvUrl");
        public final static Property State = new Property(4, int.class, "state", false, "state");
        public final static Property MvDownUrlValidityTime = new Property(5, Long.class, "mvDownUrlValidityTime", false, "mvDownUrlValidityTime");
        public final static Property SavePath = new Property(6, String.class, "savePath", false, "savePath");
        public final static Property Icon = new Property(7, String.class, "icon", false, "icon");
        public final static Property Title = new Property(8, String.class, "title", false, "title");
        public final static Property ZbName = new Property(9, String.class, "zbName", false, "zbName");
        public final static Property ZbIcon = new Property(10, String.class, "zbIcon", false, "zbIcon");
        public final static Property DownTime = new Property(11, Long.class, "downTime", false, "downTime");
        public final static Property SeeTime = new Property(12, Long.class, "seeTime", false, "seeTime");
        public final static Property SeeState = new Property(13, int.class, "seeState", false, "seeState");
    }


    public MvDownLogDao(DaoConfig config) {
        super(config);
    }
    
    public MvDownLogDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MvDownLog\" (" + //
                "\"id\" TEXT PRIMARY KEY NOT NULL ," + // 0: id
                "\"userId\" TEXT," + // 1: userId
                "\"mvId\" TEXT," + // 2: mvId
                "\"mvUrl\" TEXT," + // 3: mvUrl
                "\"state\" INTEGER NOT NULL ," + // 4: state
                "\"mvDownUrlValidityTime\" INTEGER," + // 5: mvDownUrlValidityTime
                "\"savePath\" TEXT," + // 6: savePath
                "\"icon\" TEXT," + // 7: icon
                "\"title\" TEXT," + // 8: title
                "\"zbName\" TEXT," + // 9: zbName
                "\"zbIcon\" TEXT," + // 10: zbIcon
                "\"downTime\" INTEGER," + // 11: downTime
                "\"seeTime\" INTEGER," + // 12: seeTime
                "\"seeState\" INTEGER NOT NULL );"); // 13: seeState
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MvDownLog\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, MvDownLog entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(2, userId);
        }
 
        String mvId = entity.getMvId();
        if (mvId != null) {
            stmt.bindString(3, mvId);
        }
 
        String mvUrl = entity.getMvUrl();
        if (mvUrl != null) {
            stmt.bindString(4, mvUrl);
        }
        stmt.bindLong(5, entity.getState());
 
        Long mvDownUrlValidityTime = entity.getMvDownUrlValidityTime();
        if (mvDownUrlValidityTime != null) {
            stmt.bindLong(6, mvDownUrlValidityTime);
        }
 
        String savePath = entity.getSavePath();
        if (savePath != null) {
            stmt.bindString(7, savePath);
        }
 
        String icon = entity.getIcon();
        if (icon != null) {
            stmt.bindString(8, icon);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(9, title);
        }
 
        String zbName = entity.getZbName();
        if (zbName != null) {
            stmt.bindString(10, zbName);
        }
 
        String zbIcon = entity.getZbIcon();
        if (zbIcon != null) {
            stmt.bindString(11, zbIcon);
        }
 
        Long downTime = entity.getDownTime();
        if (downTime != null) {
            stmt.bindLong(12, downTime);
        }
 
        Long seeTime = entity.getSeeTime();
        if (seeTime != null) {
            stmt.bindLong(13, seeTime);
        }
        stmt.bindLong(14, entity.getSeeState());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, MvDownLog entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(2, userId);
        }
 
        String mvId = entity.getMvId();
        if (mvId != null) {
            stmt.bindString(3, mvId);
        }
 
        String mvUrl = entity.getMvUrl();
        if (mvUrl != null) {
            stmt.bindString(4, mvUrl);
        }
        stmt.bindLong(5, entity.getState());
 
        Long mvDownUrlValidityTime = entity.getMvDownUrlValidityTime();
        if (mvDownUrlValidityTime != null) {
            stmt.bindLong(6, mvDownUrlValidityTime);
        }
 
        String savePath = entity.getSavePath();
        if (savePath != null) {
            stmt.bindString(7, savePath);
        }
 
        String icon = entity.getIcon();
        if (icon != null) {
            stmt.bindString(8, icon);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(9, title);
        }
 
        String zbName = entity.getZbName();
        if (zbName != null) {
            stmt.bindString(10, zbName);
        }
 
        String zbIcon = entity.getZbIcon();
        if (zbIcon != null) {
            stmt.bindString(11, zbIcon);
        }
 
        Long downTime = entity.getDownTime();
        if (downTime != null) {
            stmt.bindLong(12, downTime);
        }
 
        Long seeTime = entity.getSeeTime();
        if (seeTime != null) {
            stmt.bindLong(13, seeTime);
        }
        stmt.bindLong(14, entity.getSeeState());
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public MvDownLog readEntity(Cursor cursor, int offset) {
        MvDownLog entity = new MvDownLog( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // userId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // mvId
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // mvUrl
            cursor.getInt(offset + 4), // state
            cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5), // mvDownUrlValidityTime
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // savePath
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // icon
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // title
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // zbName
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // zbIcon
            cursor.isNull(offset + 11) ? null : cursor.getLong(offset + 11), // downTime
            cursor.isNull(offset + 12) ? null : cursor.getLong(offset + 12), // seeTime
            cursor.getInt(offset + 13) // seeState
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, MvDownLog entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setUserId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setMvId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setMvUrl(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setState(cursor.getInt(offset + 4));
        entity.setMvDownUrlValidityTime(cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5));
        entity.setSavePath(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setIcon(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setTitle(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setZbName(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setZbIcon(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setDownTime(cursor.isNull(offset + 11) ? null : cursor.getLong(offset + 11));
        entity.setSeeTime(cursor.isNull(offset + 12) ? null : cursor.getLong(offset + 12));
        entity.setSeeState(cursor.getInt(offset + 13));
     }
    
    @Override
    protected final String updateKeyAfterInsert(MvDownLog entity, long rowId) {
        return entity.getId();
    }
    
    @Override
    public String getKey(MvDownLog entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(MvDownLog entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
