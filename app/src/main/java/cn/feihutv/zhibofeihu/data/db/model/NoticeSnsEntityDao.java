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
 * DAO for table "NOTICE_SNS_ENTITY".
*/
public class NoticeSnsEntityDao extends AbstractDao<NoticeSnsEntity, Long> {

    public static final String TABLENAME = "NOTICE_SNS_ENTITY";

    /**
     * Properties of entity NoticeSnsEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Uid = new Property(1, String.class, "uid", false, "UID");
        public final static Property Time = new Property(2, long.class, "time", false, "TIME");
        public final static Property Content = new Property(3, String.class, "content", false, "CONTENT");
        public final static Property HeadUrl = new Property(4, String.class, "headUrl", false, "HEAD_URL");
        public final static Property Level = new Property(5, String.class, "level", false, "LEVEL");
        public final static Property NickName = new Property(6, String.class, "nickName", false, "NICK_NAME");
        public final static Property UserId = new Property(7, String.class, "userId", false, "USER_ID");
        public final static Property FeedId = new Property(8, String.class, "FeedId", false, "FEED_ID");
        public final static Property ImgUrl = new Property(9, String.class, "imgUrl", false, "IMG_URL");
        public final static Property ReplyContent = new Property(10, String.class, "replyContent", false, "REPLY_CONTENT");
        public final static Property FeedMsgType = new Property(11, int.class, "feedMsgType", false, "FEED_MSG_TYPE");
        public final static Property FeedType = new Property(12, int.class, "FeedType", false, "FEED_TYPE");
        public final static Property FeedContent = new Property(13, String.class, "FeedContent", false, "FEED_CONTENT");
    }


    public NoticeSnsEntityDao(DaoConfig config) {
        super(config);
    }
    
    public NoticeSnsEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"NOTICE_SNS_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"UID\" TEXT," + // 1: uid
                "\"TIME\" INTEGER NOT NULL ," + // 2: time
                "\"CONTENT\" TEXT," + // 3: content
                "\"HEAD_URL\" TEXT," + // 4: headUrl
                "\"LEVEL\" TEXT," + // 5: level
                "\"NICK_NAME\" TEXT," + // 6: nickName
                "\"USER_ID\" TEXT," + // 7: userId
                "\"FEED_ID\" TEXT," + // 8: FeedId
                "\"IMG_URL\" TEXT," + // 9: imgUrl
                "\"REPLY_CONTENT\" TEXT," + // 10: replyContent
                "\"FEED_MSG_TYPE\" INTEGER NOT NULL ," + // 11: feedMsgType
                "\"FEED_TYPE\" INTEGER NOT NULL ," + // 12: FeedType
                "\"FEED_CONTENT\" TEXT);"); // 13: FeedContent
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"NOTICE_SNS_ENTITY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, NoticeSnsEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String uid = entity.getUid();
        if (uid != null) {
            stmt.bindString(2, uid);
        }
        stmt.bindLong(3, entity.getTime());
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(4, content);
        }
 
        String headUrl = entity.getHeadUrl();
        if (headUrl != null) {
            stmt.bindString(5, headUrl);
        }
 
        String level = entity.getLevel();
        if (level != null) {
            stmt.bindString(6, level);
        }
 
        String nickName = entity.getNickName();
        if (nickName != null) {
            stmt.bindString(7, nickName);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(8, userId);
        }
 
        String FeedId = entity.getFeedId();
        if (FeedId != null) {
            stmt.bindString(9, FeedId);
        }
 
        String imgUrl = entity.getImgUrl();
        if (imgUrl != null) {
            stmt.bindString(10, imgUrl);
        }
 
        String replyContent = entity.getReplyContent();
        if (replyContent != null) {
            stmt.bindString(11, replyContent);
        }
        stmt.bindLong(12, entity.getFeedMsgType());
        stmt.bindLong(13, entity.getFeedType());
 
        String FeedContent = entity.getFeedContent();
        if (FeedContent != null) {
            stmt.bindString(14, FeedContent);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, NoticeSnsEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String uid = entity.getUid();
        if (uid != null) {
            stmt.bindString(2, uid);
        }
        stmt.bindLong(3, entity.getTime());
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(4, content);
        }
 
        String headUrl = entity.getHeadUrl();
        if (headUrl != null) {
            stmt.bindString(5, headUrl);
        }
 
        String level = entity.getLevel();
        if (level != null) {
            stmt.bindString(6, level);
        }
 
        String nickName = entity.getNickName();
        if (nickName != null) {
            stmt.bindString(7, nickName);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(8, userId);
        }
 
        String FeedId = entity.getFeedId();
        if (FeedId != null) {
            stmt.bindString(9, FeedId);
        }
 
        String imgUrl = entity.getImgUrl();
        if (imgUrl != null) {
            stmt.bindString(10, imgUrl);
        }
 
        String replyContent = entity.getReplyContent();
        if (replyContent != null) {
            stmt.bindString(11, replyContent);
        }
        stmt.bindLong(12, entity.getFeedMsgType());
        stmt.bindLong(13, entity.getFeedType());
 
        String FeedContent = entity.getFeedContent();
        if (FeedContent != null) {
            stmt.bindString(14, FeedContent);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public NoticeSnsEntity readEntity(Cursor cursor, int offset) {
        NoticeSnsEntity entity = new NoticeSnsEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // uid
            cursor.getLong(offset + 2), // time
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // content
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // headUrl
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // level
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // nickName
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // userId
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // FeedId
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // imgUrl
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // replyContent
            cursor.getInt(offset + 11), // feedMsgType
            cursor.getInt(offset + 12), // FeedType
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13) // FeedContent
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, NoticeSnsEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUid(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setTime(cursor.getLong(offset + 2));
        entity.setContent(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setHeadUrl(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setLevel(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setNickName(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setUserId(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setFeedId(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setImgUrl(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setReplyContent(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setFeedMsgType(cursor.getInt(offset + 11));
        entity.setFeedType(cursor.getInt(offset + 12));
        entity.setFeedContent(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(NoticeSnsEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(NoticeSnsEntity entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(NoticeSnsEntity entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
