package cn.feihutv.zhibofeihu.ui.widget.dialog.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetLuckLogsByIdResponce;
import cn.feihutv.zhibofeihu.ui.widget.recyclerloadmore.RecyclerLoadMoreAdapater;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.feihutv.zhibofeihu.utils.glideutil.GlideApp;

/**
 * Created by huanghao on 2017/6/16.
 */

public class CaichiGiftAdapter extends RecyclerLoadMoreAdapater {

    private Context mContext;
    private List<GetLuckLogsByIdResponce.LuckLogs> datas;
    private int giftId;
    private boolean isPcland;
    public CaichiGiftAdapter(Context context, int id,boolean isPcLand) {
        mContext = context;
        datas = new ArrayList<>();
        giftId = id;
        this.isPcland=isPcLand;
    }

    public void setDatas(List<GetLuckLogsByIdResponce.LuckLogs> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolderSuper(ViewGroup viewGroup, int viewType) {
        if(isPcland) {
            CaichiViewGiftViewHolder commentViewHolder = new CaichiViewGiftViewHolder(getView(viewGroup, R.layout.caichi_pcland_gift_item));
            return commentViewHolder;
        }else {
            CaichiViewGiftViewHolder commentViewHolder = new CaichiViewGiftViewHolder(getView(viewGroup, R.layout.caichi_view_gift_item));
            return commentViewHolder;
        }
    }

    @Override
    public void onBindViewHolderSuper(RecyclerView.ViewHolder holder, final int position) {
        GetLuckLogsByIdResponce.LuckLogs model = datas.get(position);
        CaichiViewGiftViewHolder viewGiftViewHolder = (CaichiViewGiftViewHolder) holder;
        String date = TCUtils.getMonth((long) (model.getLogTime()) * 1000);
        String min = TCUtils.getHourAndMin((long) (model.getLogTime()) * 1000);
        viewGiftViewHolder.tvMon.setText(date);
        viewGiftViewHolder.tvHour.setText(min);
        boolean showVipGiftIcon=model.isShowVipGiftIcon();
        if(FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().size()>0) {
            String giftUrl = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosLuckGiftIconRootPath();
            int enableVip=FeihuZhiboApplication.getApplication().mDataManager.getGiftBeanByID(String.valueOf(model.getGiftId())).getEnableVip();
            if (model.getGiftId() == 1) {
                if(showVipGiftIcon&&enableVip==1){
                    GlideApp.loadImg(mContext,giftUrl+"1_base_vip.png", DiskCacheStrategy.ALL, viewGiftViewHolder.imgGift);
                }else{
                    GlideApp.loadImg(mContext,giftUrl+"1_base.png", DiskCacheStrategy.ALL, viewGiftViewHolder.imgGift);
                }

            } else if (model.getGiftId() == 2) {
                if(showVipGiftIcon&&enableVip==1){
                    GlideApp.loadImg(mContext,giftUrl+"2_base_vip.png", DiskCacheStrategy.ALL, viewGiftViewHolder.imgGift);
                }else{
                    GlideApp.loadImg(mContext,giftUrl+"2_base.png", DiskCacheStrategy.ALL, viewGiftViewHolder.imgGift);
                }

            } else if (model.getGiftId() == 8) {
                if(showVipGiftIcon&&enableVip==1){
                    GlideApp.loadImg(mContext,giftUrl+"8_base_vip.png", DiskCacheStrategy.ALL, viewGiftViewHolder.imgGift);
                }else{
                    GlideApp.loadImg(mContext,giftUrl+"8_base.png", DiskCacheStrategy.ALL, viewGiftViewHolder.imgGift);
                }

            } else if(model.getGiftId()==20){
                GlideApp.loadImg(mContext,giftUrl+"20_base.png", DiskCacheStrategy.ALL, viewGiftViewHolder.imgGift);
            }
        }
        viewGiftViewHolder.tvName.setText(model.getNickname());
        viewGiftViewHolder.tvId.setText(model.getRoomAccountId() + "");
        viewGiftViewHolder.tvGiftCount.setText(FHUtils.intToF(model.getGiftCnt()));
        viewGiftViewHolder.tvHubi.setText(FHUtils.intToF(model.getHb()));
        viewGiftViewHolder.tvCount.setText(model.getAwardCnt() + "");
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    private View getView(ViewGroup parent, int layoutId) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
    }

    static class CaichiViewGiftViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_mon)
        TextView tvMon;
        @BindView(R.id.tv_hour)
        TextView tvHour;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_id)
        TextView tvId;
        @BindView(R.id.img_gift)
        ImageView imgGift;
        @BindView(R.id.tv_gift_count)
        TextView tvGiftCount;
        @BindView(R.id.tv_hubi)
        TextView tvHubi;
        @BindView(R.id.tv_count)
        TextView tvCount;

        public CaichiViewGiftViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
