package cn.feihutv.zhibofeihu.data.db;


import android.content.Context;

import org.greenrobot.greendao.database.Database;

import javax.inject.Inject;
import javax.inject.Singleton;

import cn.feihutv.zhibofeihu.data.db.model.DaoMaster;
import cn.feihutv.zhibofeihu.di.ApplicationContext;
import cn.feihutv.zhibofeihu.di.DatabaseInfo;
import cn.feihutv.zhibofeihu.utils.AppLogger;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/09/25
 *     desc   : 数据库升级操作类
 *     version: 1.0
 * </pre>
 */

@Singleton
public class DbOpenHelper extends DaoMaster.OpenHelper {

    @Inject
    public DbOpenHelper(@ApplicationContext Context context, @DatabaseInfo String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        AppLogger.d( "DB_OLD_VERSION : " + oldVersion + ", DB_NEW_VERSION : " + newVersion);



        switch (oldVersion) {
            case 1:
            case 2:
                //db.execSQL("ALTER TABLE " + UserDao.TABLENAME + " ADD COLUMN "
                // + UserDao.Properties.Name.columnName + " TEXT DEFAULT 'DEFAULT_VAL'");
        }
    }
}
