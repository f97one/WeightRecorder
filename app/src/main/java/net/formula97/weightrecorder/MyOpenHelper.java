package net.formula97.weightrecorder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by f97one on 14/10/31.
 */
public class MyOpenHelper extends OrmLiteSqliteOpenHelper {

    private static MyOpenHelper mHelper;

    public MyOpenHelper(Context context) {
        super(context, AppConst.DATABASE_FILE, null, AppConst.CURRENT_DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

        try {
            TableUtils.createTable(connectionSource, WeightHistory.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    public static MyOpenHelper openDatabase(Context context) {
        if (mHelper == null) {
            mHelper = new MyOpenHelper(context);
        }

        return mHelper;
    }
}
