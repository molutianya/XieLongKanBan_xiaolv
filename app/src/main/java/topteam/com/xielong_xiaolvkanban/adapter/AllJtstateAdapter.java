package topteam.com.xielong_xiaolvkanban.adapter;
/**
 * 机台数据的适配器类
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;


import topteam.com.xielong_xiaolvkanban.R;
import topteam.com.xielong_xiaolvkanban.entity.AllJtstateEntity;

public class AllJtstateAdapter extends BaseAdapter {

    private List<AllJtstateEntity> dataList;
    private Context context;

    public AllJtstateAdapter(List<AllJtstateEntity> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.all_jtstate_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.jtno = view.findViewById(R.id.jtno);
            viewHolder.gdno = view.findViewById(R.id.gdno);
            viewHolder.cpno = view.findViewById(R.id.cpno);
            //   viewHolder.mjname = view.findViewById(R.id.mjname);
            viewHolder.jhsl = view.findViewById(R.id.jhsl);
            viewHolder.lpsl = view.findViewById(R.id.lpsl);
            viewHolder.blsl = view.findViewById(R.id.blsl);
            viewHolder.bt_a = view.findViewById(R.id.bt_a);
            viewHolder.bt_c = view.findViewById(R.id.bt_c);
            viewHolder.bt_r = view.findViewById(R.id.bt_r);
            viewHolder.bt_p = view.findViewById(R.id.bt_p);
            viewHolder.bt_t = view.findViewById(R.id.bt_t);
            viewHolder.bt_m = view.findViewById(R.id.bt_m);
            viewHolder.user1 = view.findViewById(R.id.user1);
            viewHolder.user2 = view.findViewById(R.id.user2);
            viewHolder.user3 = view.findViewById(R.id.user3);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        try {
            viewHolder.jtno.setBackgroundColor(dataList.get(position).getColor());
            viewHolder.jtno.setText(dataList.get(position).getJtno());
            viewHolder.gdno.setText(dataList.get(position).getGdno());
            viewHolder.cpno.setText(dataList.get(position).getCpno());
            //   viewHolder.mjname.setText(dataList.get(position).getMjname());
            viewHolder.jhsl.setText(dataList.get(position).getScsl());
            viewHolder.lpsl.setText(dataList.get(position).getLpsl());
            viewHolder.blsl.setText(dataList.get(position).getBlsl());

            if (dataList.get(position).isBt_a() == true) {
                viewHolder.bt_a.setVisibility(View.VISIBLE);
            } else {
                viewHolder.bt_a.setVisibility(View.INVISIBLE);
            }

            if (dataList.get(position).isBt_c() == true) {
                viewHolder.bt_c.setVisibility(View.VISIBLE);
            } else {
                viewHolder.bt_c.setVisibility(View.INVISIBLE);
            }

            if (dataList.get(position).isBt_r() == true) {
                viewHolder.bt_r.setVisibility(View.VISIBLE);
            } else {
                viewHolder.bt_r.setVisibility(View.INVISIBLE);
            }


            if (dataList.get(position).isBt_p() == true) {
                viewHolder.bt_p.setVisibility(View.VISIBLE);
            } else {
                viewHolder.bt_p.setVisibility(View.INVISIBLE);
            }

            if (dataList.get(position).isBt_t() == true) {
                viewHolder.bt_t.setVisibility(View.VISIBLE);
            } else {
                viewHolder.bt_t.setVisibility(View.INVISIBLE);
            }

            if (dataList.get(position).isBt_m() == true) {
                viewHolder.bt_m.setVisibility(View.VISIBLE);
            } else {
                viewHolder.bt_m.setVisibility(View.INVISIBLE);
            }

            if (dataList.get(position).getUser1().length() > 1) {
                viewHolder.user1.setVisibility(View.VISIBLE);
            } else {
                viewHolder.user1.setVisibility(View.INVISIBLE);
            }
            if (dataList.get(position).getUser2().length() > 1) {
                viewHolder.user2.setVisibility(View.VISIBLE);
            } else {
                viewHolder.user2.setVisibility(View.INVISIBLE);
            }
            if (dataList.get(position).getUser3().length() > 1) {
                viewHolder.user3.setVisibility(View.VISIBLE);
            } else {
                viewHolder.user3.setVisibility(View.INVISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    private class ViewHolder {
        TextView jtno;
        TextView gdno;
        TextView cpno;
        // TextView mjname;
        TextView jhsl;
        TextView lpsl;
        TextView blsl;
        Button bt_a;
        Button bt_c;
        Button bt_r;
        Button bt_p;
        Button bt_t;
        Button bt_m;
        TextView user1;
        TextView user2;
        TextView user3;

    }
}
