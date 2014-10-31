package net.formula97.weightrecorder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by f97one on 14/10/31.
 */
public class WeightAdapter extends ArrayAdapter<WeightHistory> {

    private Context mContext;
    private int resId;
    private List<WeightHistory> list;

    public WeightAdapter(Context context, int resource, List<WeightHistory> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.resId = resource;
        this.list = objects;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(resId, null);

            convertView = v;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(list.get(position).getGeneratedAt());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
        TextView tvWeight = (TextView) convertView.findViewById(R.id.tvWeight);

        tvDate.setText(sdf.format(cal.getTime()));
        tvWeight.setText(String.valueOf(list.get(position).getWeight()));

        return convertView;
    }
}
