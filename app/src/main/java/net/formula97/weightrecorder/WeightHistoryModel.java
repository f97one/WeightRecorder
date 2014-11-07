package net.formula97.weightrecorder;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.StatementBuilder;
import com.j256.ormlite.support.CompiledStatement;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.support.DatabaseResults;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by f97one on 14/10/31.
 */
public class WeightHistoryModel {

    private Context mContext;
    private MyOpenHelper helper;
    private Dao<WeightHistory, Integer> mDao;

    public WeightHistoryModel(Context context) {
        this.mContext = context;
        this.helper = MyOpenHelper.openDatabase(context);
        try {
            mDao = this.helper.getDao(WeightHistory.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int saveHist(final WeightHistory savedEntity) {
        int ret = 0;

        try {
            ret = TransactionManager.callInTransaction(mDao.getConnectionSource(), new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {

                    Dao.CreateOrUpdateStatus status = mDao.createOrUpdate(savedEntity);
                    return status.getNumLinesChanged();
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public WeightHistory readLatestHistory() {
        List<WeightHistory> histories = getHistories(false);

        return histories == null ? null : histories.get(0);
    }

    public List<WeightHistory> getHistories(boolean isAscending) {
        // ORDER BY generatedAt DESC だけQueryBuilderに渡す
        QueryBuilder<WeightHistory, Integer> qb = mDao.queryBuilder().orderBy("generatedAt", isAscending);

        List<WeightHistory> histories = null;
        try {
            histories = qb.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return histories == null ? null : histories;
    }

    public boolean erase(final WeightHistory historyEntity) {
        boolean ret = false;

        try {
            ret = TransactionManager.callInTransaction(mDao.getConnectionSource(), new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {

                    int affected = mDao.delete(historyEntity);

                    return affected > 0 ? true : false;
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ret;
    }
}
