package net.formula97.weightrecorder;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.Calendar;
import java.util.List;


public class MainActivity extends Activity implements ConfirmDialog.OnButtonSelectedListener {

    private ListView listView;
    private EditText editText;
    private Button button;

    private String savedWeightValue;
    private WeightAdapter adapter;
    private WeightHistory selectedHist;

    private final String weightKey = "weightKey";

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String input = editText.getText().toString();

            if (!TextUtils.isEmpty(input)) {

                long now = Calendar.getInstance().getTimeInMillis();
                WeightHistory hist = new WeightHistory();
                hist.setWeight(Double.parseDouble(input));
                hist.setGeneratedAt(now);

                WeightHistoryModel model = new WeightHistoryModel(MainActivity.this);
                model.saveHist(hist);

                List<WeightHistory> list = model.getHistories(false);
                updateList(list);
            }

            editText.setText("");
        }
    };

    void updateList(List<WeightHistory> list) {
        adapter.clear();
        adapter.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        editText = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button);

        savedWeightValue = "";

        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                if (source.toString().matches(AppConst.INPUT_FILTER_NUMERIC_WITH_DOT)) {
                    return source;
                } else {
                    return "";
                }
            }
        };

        InputFilter[] filters = {
                filter
        };

        editText.setFilters(filters);
        button.setOnClickListener(clickListener);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectedHist = (WeightHistory) parent.getAdapter().getItem(position);
                ConfirmDialog dialog = new ConfirmDialog();
                dialog.show(getFragmentManager(), ConfirmDialog.DIALOG_TAG);

                return false;
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        editText.setText(savedWeightValue);

        WeightHistoryModel model = new WeightHistoryModel(this);
        List<WeightHistory> histories = model.getHistories(false);
        adapter = new WeightAdapter(this, R.layout.list_element, histories);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(weightKey, editText.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        savedWeightValue = savedInstanceState.getString(weightKey);
    }

    @Override
    public void onButtonSelected(int whichButton) {
        if (whichButton == DialogInterface.BUTTON_POSITIVE) {
            WeightHistoryModel model = new WeightHistoryModel(MainActivity.this);
            model.erase(selectedHist);

            List<WeightHistory> list = model.getHistories(false);
            updateList(list);
        }
    }
}
