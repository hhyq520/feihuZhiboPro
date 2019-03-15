package cn.feihutv.zhibofeihu.ui.mv.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetAllNeedListResponse;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.iwgang.countdownview.CountdownView;

/**
 */
public class DemandSquareAdapter2 extends RecyclerView.Adapter<DemandSquareAdapter2.ViewHolder> {


    private Context mContext;
    private Map<String, View> chooseView = new HashMap<>();

    public void setClickCallBack(ItemClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

    public interface ItemClickCallBack {
        void onItemClick(int pos);
    }

    public List<GetAllNeedListResponse.GetAllNeedList> datas = null;
    private ItemClickCallBack clickCallBack;

    public DemandSquareAdapter2(Context mContext, List<GetAllNeedListResponse.GetAllNeedList> datas) {
        this.datas = datas;
        this.mContext = mContext;
    }


    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_layout_mv_post_demand_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void clearChooseItem() {
        chooseView.clear();
    }


    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        final GetAllNeedListResponse.GetAllNeedList mvList = datas.get(position);

        viewHolder.rl_my_post_layout.getBackground().setAlpha(40);

        ///
        TCUtils.showPicWithUrl(mContext, viewHolder.mv_user_head,
                mvList.getAvatar(), R.drawable.face);
        viewHolder.mv_user_name.setText(mvList.getNickname());
        viewHolder.mv_title.setText(mvList.getTitle());

        if(TextUtils.isEmpty(mvList.getSongName())&&TextUtils.isEmpty(mvList.getSingerName())){
            viewHolder.mv_title_song.setVisibility(View.GONE);
        }else{
            viewHolder.mv_title_song.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(mvList.getSongName())&&TextUtils.isEmpty( mvList.getSingerName())){
                viewHolder.mv_title_song.setText(mvList.getSongName());
            }
            if(TextUtils.isEmpty(mvList.getSongName())&&!TextUtils.isEmpty( mvList.getSingerName())){
                viewHolder.mv_title_song.setText(mvList.getSingerName());
            }
            if(!TextUtils.isEmpty(mvList.getSongName())&&!TextUtils.isEmpty( mvList.getSingerName())){
                viewHolder.mv_title_song.setText(mvList.getSongName() + "—" + mvList.getSingerName());
            }

        }




        viewHolder.mv_title_desc.setText(mvList.getRequire() + "");
        viewHolder.mv_item_coin.setText(mvList.gethB() + "");
        chooseView.put(mvList.getNeedId(), viewHolder.rl_my_post_layout);
        if (mvList.getT() < 0) {
            viewHolder.mv_item_time.setText("已过期");
            viewHolder.cv_countdownView.setVisibility(View.GONE);

        } else {

            viewHolder.bindTimeData(mvList);
        }


        String forId = mvList.getForUid();
        if (TextUtils.isEmpty(forId)) {
            forId = "0";
        }
        if ("0".equals(forId)) {
            //指定主播标识，0标识公开
            viewHolder.iv_mv_zd.setVisibility(View.GONE);
        } else {
            String userId = FeihuZhiboApplication.getApplication().mDataManager.getCurrentUserId();
            if (userId.equals(forId)) {
                //如果是指定给我，那我肯定是主播
                viewHolder.iv_mv_zd.setImageResource(R.drawable.icon_mv_sq_d1);
            } else {
                //指定给其他主播的需求
                viewHolder.iv_mv_zd.setImageResource(R.drawable.icon_mv_sq_d2);
            }
            viewHolder.iv_mv_zd.setVisibility(View.VISIBLE);
        }
        final String nid = mvList.getNeedId();
        viewHolder.ll_mv_item_layout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clickCallBack != null) {
                            clickCallBack.onItemClick(position);
                        }
                    }
                }
        );

    }



    public void setItemChooseState(String nid){
        for (Iterator ite = chooseView.entrySet().iterator(); ite.hasNext(); ) {
            Map.Entry entry = (Map.Entry) ite.next();
            View itemView = (View) entry.getValue();
            itemView.setVisibility(View.GONE);
        }
        chooseView.get(nid).setVisibility(View.VISIBLE);
    }



    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mv_user_head;
        public TextView mv_user_name,mv_title_song;

        public TextView mv_title, mv_title_desc, mv_item_coin,mv_item_time;

        public ImageView iv_mv_zd;

        public View ll_mv_item_layout;
        public View rl_my_post_layout,ll_item_time_layout;

        public  CountdownView cv_countdownView;

        public    GetAllNeedListResponse.GetAllNeedList mItemInfo;

        public ViewHolder(View view) {
            super(view);
            mv_user_head = view.findViewById(R.id.mv_user_head);
            mv_user_name = (TextView) view.findViewById(R.id.mv_user_name);
            mv_title = view.findViewById(R.id.mv_title);
            mv_title_desc = view.findViewById(R.id.mv_title_desc);
            mv_item_coin = view.findViewById(R.id.mv_item_coin);
            mv_item_time = view.findViewById(R.id.mv_item_time);
            iv_mv_zd = view.findViewById(R.id.iv_mv_zd);
            ll_mv_item_layout = view.findViewById(R.id.ll_mv_item_layout);
            rl_my_post_layout = view.findViewById(R.id.rl_my_post_layout);
            mv_title_song = view.findViewById(R.id.mv_title_song);
            cv_countdownView= view.findViewById(R.id.cv_countdownView);
            ll_item_time_layout= view.findViewById(R.id.ll_item_time_layout);
        }


       public void bindTimeData(GetAllNeedListResponse.GetAllNeedList itemInfo) {
            mItemInfo = itemInfo;
            refreshTime(mItemInfo.getT()*1000  );
        }
        public void refreshTime(long leftTime) {
            if (leftTime > 0) {
                cv_countdownView.start(leftTime);
            } else {
                cv_countdownView.stop();
                cv_countdownView.allShowZero();
                cv_countdownView.setVisibility(View.GONE);
                mv_item_time.setText("已过期");
            }
        }
    }


}





















