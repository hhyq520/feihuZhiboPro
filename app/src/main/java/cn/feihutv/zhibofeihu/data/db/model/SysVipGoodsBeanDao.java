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
 * DAO for table "SYS_VIP_GOODS_BEAN".
*/
public class SysVipGoodsBeanDao extends AbstractDao<SysVipGoodsBean, String> {

    public static final String TABLENAME = "SYS_VIP_GOODS_BEAN";

    /**
     * Properties of entity SysVipGoodsBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property GoodsId = new Property(0, String.class, "goodsId", true, "GOODS_ID");
        public final static Property GoodsName = new Property(1, String.class, "goodsName", false, "GOODS_NAME");
        public final static Property Duration = new Property(2, String.class, "duration", false, "DURATION");
        public final static Property Czz = new Property(3, String.class, "czz", false, "CZZ");
        public final static Property Xiaolaba = new Property(4, String.class, "xiaolaba", false, "XIAOLABA");
        public final static Property Hb = new Property(5, long.class, "hb", false, "HB");
        public final static Property Recommend = new Property(6, int.class, "recommend", false, "RECOMMEND");
        public final static Property Discount = new Property(7, int.class, "discount", false, "DISCOUNT");
        public final static Property VipMountDays = new Property(8, String.class, "vipMountDays", false, "VIP_MOUNT_DAYS");
        public final static Property SortOrder = new Property(9, int.class, "sortOrder", false, "SORT_ORDER");
        public final static Property IsSelect = new Property(10, int.class, "isSelect", false, "IS_SELECT");
    }


    public SysVipGoodsBeanDao(DaoConfig config) {
        super(config);
    }
    
    public SysVipGoodsBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SYS_VIP_GOODS_BEAN\" (" + //
                "\"GOODS_ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: goodsId
                "\"GOODS_NAME\" TEXT," + // 1: goodsName
                "\"DURATION\" TEXT," + // 2: duration
                "\"CZZ\" TEXT," + // 3: czz
                "\"XIAOLABA\" TEXT," + // 4: xiaolaba
                "\"HB\" INTEGER NOT NULL ," + // 5: hb
                "\"RECOMMEND\" INTEGER NOT NULL ," + // 6: recommend
                "\"DISCOUNT\" INTEGER NOT NULL ," + // 7: discount
                "\"VIP_MOUNT_DAYS\" TEXT," + // 8: vipMountDays
                "\"SORT_ORDER\" INTEGER NOT NULL ," + // 9: sortOrder
                "\"IS_SELECT\" INTEGER NOT NULL );"); // 10: isSelect
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SYS_VIP_GOODS_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, SysVipGoodsBean entity) {
        stmt.clearBindings();
 
        String goodsId = entity.getGoodsId();
        if (goodsId != null) {
            stmt.bindString(1, goodsId);
        }
 
        String goodsName = entity.getGoodsName();
        if (goodsName != null) {
            stmt.bindString(2, goodsName);
        }
 
        String duration = entity.getDuration();
        if (duration != null) {
            stmt.bindString(3, duration);
        }
 
        String czz = entity.getCzz();
        if (czz != null) {
            stmt.bindString(4, czz);
        }
 
        String xiaolaba = entity.getXiaolaba();
        if (xiaolaba != null) {
            stmt.bindString(5, xiaolaba);
        }
        stmt.bindLong(6, entity.getHb());
        stmt.bindLong(7, entity.getRecommend());
        stmt.bindLong(8, entity.getDiscount());
 
        String vipMountDays = entity.getVipMountDays();
        if (vipMountDays != null) {
            stmt.bindString(9, vipMountDays);
        }
        stmt.bindLong(10, entity.getSortOrder());
        stmt.bindLong(11, entity.getIsSelect());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, SysVipGoodsBean entity) {
        stmt.clearBindings();
 
        String goodsId = entity.getGoodsId();
        if (goodsId != null) {
            stmt.bindString(1, goodsId);
        }
 
        String goodsName = entity.getGoodsName();
        if (goodsName != null) {
            stmt.bindString(2, goodsName);
        }
 
        String duration = entity.getDuration();
        if (duration != null) {
            stmt.bindString(3, duration);
        }
 
        String czz = entity.getCzz();
        if (czz != null) {
            stmt.bindString(4, czz);
        }
 
        String xiaolaba = entity.getXiaolaba();
        if (xiaolaba != null) {
            stmt.bindString(5, xiaolaba);
        }
        stmt.bindLong(6, entity.getHb());
        stmt.bindLong(7, entity.getRecommend());
        stmt.bindLong(8, entity.getDiscount());
 
        String vipMountDays = entity.getVipMountDays();
        if (vipMountDays != null) {
            stmt.bindString(9, vipMountDays);
        }
        stmt.bindLong(10, entity.getSortOrder());
        stmt.bindLong(11, entity.getIsSelect());
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public SysVipGoodsBean readEntity(Cursor cursor, int offset) {
        SysVipGoodsBean entity = new SysVipGoodsBean( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // goodsId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // goodsName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // duration
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // czz
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // xiaolaba
            cursor.getLong(offset + 5), // hb
            cursor.getInt(offset + 6), // recommend
            cursor.getInt(offset + 7), // discount
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // vipMountDays
            cursor.getInt(offset + 9), // sortOrder
            cursor.getInt(offset + 10) // isSelect
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, SysVipGoodsBean entity, int offset) {
        entity.setGoodsId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setGoodsName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setDuration(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setCzz(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setXiaolaba(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setHb(cursor.getLong(offset + 5));
        entity.setRecommend(cursor.getInt(offset + 6));
        entity.setDiscount(cursor.getInt(offset + 7));
        entity.setVipMountDays(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setSortOrder(cursor.getInt(offset + 9));
        entity.setIsSelect(cursor.getInt(offset + 10));
     }
    
    @Override
    protected final String updateKeyAfterInsert(SysVipGoodsBean entity, long rowId) {
        return entity.getGoodsId();
    }
    
    @Override
    public String getKey(SysVipGoodsBean entity) {
        if(entity != null) {
            return entity.getGoodsId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(SysVipGoodsBean entity) {
        return entity.getGoodsId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}