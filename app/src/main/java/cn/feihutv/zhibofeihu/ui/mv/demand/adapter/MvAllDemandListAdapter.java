package cn.feihutv.zhibofeihu.ui.mv.demand.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMyNeedListResponse;
import cn.iwgang.countdownview.CountdownView;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/10/24 17:08
 *      desc   : mv适配器
 *      version: 1.0
 * </pre>
 */

public class MvAllDemandListAdapter extends RecyclerView.Adapter<MvAllDemandListAdapter.ViewHolder> {


    private Context mContext;

    public void setClickCallBack(ItemClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

    public interface ItemClickCallBack{
        void onDelClick(View v,int position);
    }

    public List<GetMyNeedListResponse.GetMyNeedList> datas = null;
    private ItemClickCallBack clickCallBack;

    public MvAllDemandListAdapter(Context mContext, List<GetMyNeedListResponse.GetMyNeedList> datas) {
        this.datas = datas;
        this.mContext=mContext;
    }


    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_layout_mv_alldemand,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }



    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder,final int position) {

        GetMyNeedListResponse.GetMyNeedList item=  datas.get(position);

        viewHolder.mv_title.setText(item.getTitle());

        if(TextUtils.isEmpty(item.getSongName())&&TextUtils.isEmpty(item.getSingerName())){
            viewHolder.mv_title_song.setVisibility(View.GONE);
        }else{
            viewHolder.mv_title_song.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(item.getSongName())&&TextUtils.isEmpty( item.getSingerName())){
                viewHolder.mv_title_song.setText(item.getSongName());
            }
            if(TextUtils.isEmpty(item.getSongName())&&!TextUtils.isEmpty( item.getSingerName())){
                viewHolder.mv_title_song.setText(item.getSingerName());
            }
            if(!TextUtils.isEmpty(item.getSongName())&&!TextUtils.isEmpty( item.getSingerName())){
                viewHolder.mv_title_song.setText(item.getSongName() + "—" + item.getSingerName());
            }
        }

        viewHolder.mv_title_desc.setText(item.getRequire()+"");
        viewHolder.mv_item_coin.setText(item.gethB()+"");


        switch (item.getStatus()){//需求状态 1显示倒计时可修改 2无倒计时可修改  3显示过期可删除
            case 1:
                viewHolder.cv_countdownView.setVisibility(View.VISIBLE);
                viewHolder.cv_countdownView.start(item.getT()*1000);
                viewHolder.mv_item_time.setText("");
                viewHolder.mv_item_time.setVisibility(View.VISIBLE);
                viewHolder.item_mv_del.setImageResource(R.drawable.icon_mv_edit);
                viewHolder.layout.setBackgroundResource(R.drawable.shape_mv_my_bg2);
                break;
            case 2:
                viewHolder.cv_countdownView.setVisibility(View.GONE);
                viewHolder.mv_item_time.setVisibility(View.GONE);
                viewHolder.item_mv_del.setImageResource(R.drawable.icon_mv_edit);
                viewHolder.layout.setBackgroundResource(R.drawable.shape_mv_my_bg2);
                break;
            case 3:
                viewHolder.mv_item_time.setText("过期");
                viewHolder.cv_countdownView.setVisibility(View.GONE);
                viewHolder.item_mv_del.setImageResource(R.drawable.icon_mv_del);
                viewHolder.layout.setBackgroundResource(R.drawable.shape_mv_my_bg);
                break;
        }


        viewHolder.item_mv_del.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(clickCallBack != null){
                            clickCallBack.onDelClick(v,position);
                        }
                    }
                }
        );



    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas.size();
    }
    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mv_title;
        public TextView   mv_title_song;
        public TextView   mv_title_desc;
        public TextView   mv_item_coin;
        public TextView   mv_item_time;
        public ImageView item_mv_del;
        public View layout;
        public CountdownView cv_countdownView;

        public ViewHolder(View view){
            super(view);
                mv_title=view.findViewById(R.id.mv_title);
                  mv_title_song=view.findViewById(R.id.mv_title_song);
                  mv_title_desc=view.findViewById(R.id.mv_title_desc);
                  mv_item_coin=view.findViewById(R.id.mv_item_coin);
                  mv_item_time=view.findViewById(R.id.mv_item_time);
                item_mv_del=view.findViewById(R.id.item_mv_del);
                layout=view.findViewById(R.id.layout);
            cv_countdownView =view.findViewById(R.id.cv_countdownView);
        }




    }








}
