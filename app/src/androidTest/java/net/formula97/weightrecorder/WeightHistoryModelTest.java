package net.formula97.weightrecorder;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.util.Calendar;
import java.util.List;


public class WeightHistoryModelTest extends AndroidTestCase {

    private static final String TEST_PREFIX = "test_";
    private RenamingDelegatingContext mContext;
    private WeightHistoryModel model;

    public void setUp() throws Exception {
        super.setUp();
        mContext = new RenamingDelegatingContext(getContext(), TEST_PREFIX);
        MyOpenHelper helper = new MyOpenHelper(mContext);
        Dao<WeightHistory, Integer> dao = helper.getDao(WeightHistory.class);

        model = new WeightHistoryModel(mContext);

        // テストデータ投入
        Calendar cal = Calendar.getInstance();
        // 現在データ（1分前とする）
        cal.add(Calendar.MINUTE, -1);
        long now = cal.getTimeInMillis();
        // 過去データ（１年前）
        cal.add(Calendar.YEAR, -1);
        long past1 = cal.getTimeInMillis();
        // 過去データ（２年前）
        cal.add(Calendar.YEAR, -1);
        long past2 = cal.getTimeInMillis();

        WeightHistory hist1 = new WeightHistory();

        hist1.setWeight(75.0);
        hist1.setGeneratedAt(now);
        hist1.setReason("現在データのテスト");
        dao.createOrUpdate(hist1);

        WeightHistory hist2 = new WeightHistory();
        hist2.setWeight(66.5);
        hist2.setGeneratedAt(past2);
        hist2.setReason("２年前は痩せていた");
        dao.createOrUpdate(hist2);

        WeightHistory hist3 = new WeightHistory();
        hist3.setWeight(77.0);
        hist3.setGeneratedAt(past1);
        hist3.setReason("");
        dao.createOrUpdate(hist3);
    }

    public void tearDown() throws Exception {
        MyOpenHelper helper = new MyOpenHelper(mContext);
        Dao<WeightHistory, Integer> dao = helper.getDao(WeightHistory.class);
        DeleteBuilder<WeightHistory, Integer> deleteBuilder = dao.deleteBuilder();
        deleteBuilder.where().isNotNull("reason");
        deleteBuilder.delete();

    }

    public void test001_履歴読み込み() throws Throwable {
        WeightHistory hist = model.readLatestHistory();
        assertEquals("体重は75.0", 75.0, hist.getWeight());
        assertEquals("理由は「現在データのテスト」", "現在データのテスト", hist.getReason());

        List<WeightHistory> histories = model.getHistories(true);
        assertEquals("レコード数は3", 3, histories.size());
        assertEquals("3レコード目の体重は75.0", 75.0, histories.get(2).getWeight());
        assertEquals("3レコード目の理由は「現在データのテスト」", "現在データのテスト", histories.get(2).getReason());
        assertEquals("2レコード目の体重は77.0", 77.0, histories.get(1).getWeight());
        assertEquals("2レコード目の理由は「」", "", histories.get(1).getReason());
        assertEquals("1レコード目の体重は66.5", 66.5, histories.get(0).getWeight());
        assertEquals("1レコード目の理由は「２年前は痩せていた」", "２年前は痩せていた", histories.get(0).getReason());
    }

    public void test002_履歴追加() throws Throwable {
        WeightHistory hist = new WeightHistory();

        Calendar cal = Calendar.getInstance();
        hist.setWeight(71.1);
        hist.setGeneratedAt(cal.getTimeInMillis());
        hist.setReason("現在地を更に追加");
        model.saveHist(hist);

        List<WeightHistory> list = model.getHistories(false);
        assertEquals("レコード数は1", 1, list.size());
        assertEquals("最新レコードの体重は71.1", 71.1, list.get(0).getWeight());
    }
}