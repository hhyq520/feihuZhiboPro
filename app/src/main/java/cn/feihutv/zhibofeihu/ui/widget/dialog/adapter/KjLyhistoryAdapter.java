package cn.feihutv.zhibofeihu.ui.widget.dialog.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.R;

/**
 * Created by huanghao on 2017/6/13.
 */

public class KjLyhistoryAdapter extends RecyclerView.Adapter<KjLyhistoryAdapter.HViewHolder> {

    private Context context;
    private List<String> datas;
    private boolean isPCland;
    public KjLyhistoryAdapter(Context context,boolean isPCland) {
        this.context = context;
        datas = new ArrayList<>();
        this.isPCland=isPCland;
    }

    public void setDatas(List<String> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public HViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_lykj_history, parent, false);
        return new HViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(HViewHolder holder, int position) {
        if (position == 0) {
            holder.imageNew.setVisibility(View.VISIBLE);
        } else {
            holder.imageNew.setVisibility(View.INVISIBLE);
        }
        String item = datas.get(position);
        String[] split = item.split(",");
         holder.tv1.setText(split[0]);
        holder.tv2.setText(split[1]);
        holder.tv3.setText(split[2]);
        int sum= Integer.valueOf(split[0])+ Integer.valueOf(split[1])+ Integer.valueOf(split[2]);
        holder.textSum.setText(sum+"");
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class HViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv1)
        TextView tv1;
        @BindView(R.id.tv2)
        TextView tv2;
        @BindView(R.id.tv3)
        TextView tv3;
        @BindView(R.id.text_sum)
        TextView textSum;
        @BindView(R.id.img_new)
        ImageView imageNew;
        public HViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
