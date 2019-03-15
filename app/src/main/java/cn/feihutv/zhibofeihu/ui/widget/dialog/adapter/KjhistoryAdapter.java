package cn.feihutv.zhibofeihu.ui.widget.dialog.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.R;

/**
 * Created by huanghao on 2017/6/13.
 */

public class KjhistoryAdapter extends RecyclerView.Adapter<KjhistoryAdapter.HViewHolder> {

    private Context context;
    private List<String> datas;
    private boolean isPcLand=false;
    public KjhistoryAdapter(Context context,boolean isPcland) {
        this.context = context;
        datas=new ArrayList<>();
        this.isPcLand=isPcland;
    }
    public void setDatas(List<String> datas){
        this.datas=datas;
        notifyDataSetChanged();
    }

    @Override
    public HViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_kj_history, parent, false);
        return new HViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(HViewHolder holder, int position) {
        if (position == 0) {
            holder.imgNew.setVisibility(View.VISIBLE);
        } else {
            holder.imgNew.setVisibility(View.INVISIBLE);
        }

        String item=datas.get(position);
        String[] split = item.split(",");
        if(split[0].equals("1")){
            holder.img1.setImageResource(R.drawable.pic_tiger_male);
        }else{
            holder.img1.setImageResource(R.drawable.pic_tiger_female);
        }
        if(split[1].equals("1")){
            holder.img2.setImageResource(R.drawable.pic_tiger_male);
        }else{
            holder.img2.setImageResource(R.drawable.pic_tiger_female);
        }
        if(split[2].equals("1")){
            holder.img3.setImageResource(R.drawable.pic_tiger_male);
        }else{
            holder.img3.setImageResource(R.drawable.pic_tiger_female);
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class HViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img1)
        ImageView img1;
        @BindView(R.id.img2)
        ImageView img2;
        @BindView(R.id.img3)
        ImageView img3;
        @BindView(R.id.img_new)
        ImageView imgNew;
        public HViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
