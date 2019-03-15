package cn.feihutv.zhibofeihu.ui.widget.dialog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.utils.TCUtils;

/**
 * Created by Administrator on 2017/2/16.
 */

public class GridViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> datas;
    private List<Integer> list;
    private ImageView signImg;

    public GridViewAdapter(Context context, List<Integer> days, int maxDays) {
        mContext = context;
        this.list = days;
        intidatas(maxDays);
    }

    public void setList(List<Integer> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    private void intidatas(int maxDays) {
        datas = new ArrayList<String>();
        for (int i = 1; i <= maxDays; i++) {
            datas.add(i + "");
        }
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.sign_item_view, parent, false);
        }
        TextView text = (TextView) convertView.findViewById(R.id.text_count);
        text.setText(datas.get(position));
        signImg = (ImageView) convertView.findViewById(R.id.qiandao_ok);
        if (list.contains(position + 1)) {
            signImg.setVisibility(View.VISIBLE);
        } else {
            signImg.setVisibility(View.INVISIBLE);
        }

        LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.linearLayout);
        int width = mContext.getResources().getDisplayMetrics().widthPixels - TCUtils.dipToPx(mContext, 80);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.width = width / 7;
        linearLayout.setLayoutParams(layoutParams);
        return convertView;
    }
}
