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
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetMyLuckLogsByIdResponce;
import cn.feihutv.zhibofeihu.ui.widget.recyclerloadmore.RecyclerLoadMoreAdapater;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.feihutv.zhibofeihu.utils.glideutil.GlideApp;

/**
 * Created by huanghao on 2017/6/16.
 */

public class CaichiMyAdapter extends RecyclerLoadMoreAdapater {


    private Context mContext;
    private List<GetMyLuckLogsByIdResponce.MyLuckLogs> datas;

    public CaichiMyAdapter(Context context) {
        mContext = context;
        datas = new ArrayList<>();
    }

    public void setDatas(List<GetMyLuckLogsByIdResponce.MyLuckLogs> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolderSuper(ViewGroup viewGroup, int viewType) {
        CaichiViewMyViewHolder commentViewHolder = new CaichiViewMyViewHolder(getView(viewGroup, R.layout.caichi_view_my_item));
        return commentViewHolder;
    }

    @Override
    public void onBindViewHolderSuper(RecyclerView.ViewHolder holder, final int position) {
        GetMyLuckLogsByIdResponce.MyLuckLogs model = datas.get(position);
        CaichiViewMyViewHolder viewGiftViewHolder = (CaichiViewMyViewHolder) holder;
        String min = TCUtils.getMHTime((long) (model.getLogTime()) * 1000);
        viewGiftViewHolder.tvMon.setText(min);
        if(FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().size()>0) {
            String giftUrl = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosLuckGiftIconRootPath();
            boolean isVip=false;
            if(SharePreferenceUtil.getSessionInt(FeihuZhiboApplication.getApplication(),"PREF_KEY_VIP")>0&&!SharePreferenceUtil.getSessionBoolean(FeihuZhiboApplication.getApplication(),"PREF_KEY_VIPEXPIRED")){
                isVip=true;
            }else{
                isVip=false;
            }
            if(SharePreferenceUtil.getSessionBoolean(FeihuZhiboApplication.getApplication(),"meisGuard",false)){
                isVip=true;
            }
            if (model.getGiftId() == 1) {
                int enableVip=FeihuZhiboApplication.getApplication().mDataManager.getGiftBeanByID("1").getEnableVip();
                if(isVip&&enableVip==1){
                    GlideApp.loadImg(mContext,giftUrl+"1_base_vip.png", DiskCacheStrategy.ALL, viewGiftViewHolder.imgGift);
                }else{
                    GlideApp.loadImg(mContext,giftUrl+"1_base.png", DiskCacheStrategy.ALL, viewGiftViewHolder.imgGift);
                }

            } else if (model.getGiftId() == 2) {
                int enableVip=FeihuZhiboApplication.getApplication().mDataManager.getGiftBeanByID("2").getEnableVip();
                if(isVip&&enableVip==1){
                    GlideApp.loadImg(mContext,giftUrl+"2_base_vip.png", DiskCacheStrategy.ALL, viewGiftViewHolder.imgGift);
                }else{
                    GlideApp.loadImg(mContext,giftUrl+"2_base.png", DiskCacheStrategy.ALL, viewGiftViewHolder.imgGift);
                }

            } else if (model.getGiftId() == 8) {
                int enableVip=FeihuZhiboApplication.getApplication().mDataManager.getGiftBeanByID("8").getEnableVip();
                if(isVip&&enableVip==1){
                    GlideApp.loadImg(mContext,giftUrl+"8_base_vip.png", DiskCacheStrategy.ALL, viewGiftViewHolder.imgGift);
                }else{
                    GlideApp.loadImg(mContext,giftUrl+"8_base.png", DiskCacheStrategy.ALL, viewGiftViewHolder.imgGift);
                }
            } else if(model.getGiftId()==20){
                GlideApp.loadImg(mContext,giftUrl+"20_base.png", DiskCacheStrategy.ALL, viewGiftViewHolder.imgGift);
            }
        }
        viewGiftViewHolder.tvGiftCount.setText(" " + model.getGiftCnt());
        viewGiftViewHolder.tvHubi.setText(model.getHb()+"");
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    private View getView(ViewGroup parent, int layoutId) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
    }

    static class CaichiViewMyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_mon)
        TextView tvMon;
        @BindView(R.id.img_gift)
        ImageView imgGift;
        @BindView(R.id.tv_gift_count)
        TextView tvGiftCount;
        @BindView(R.id.tv_hubi)
        TextView tvHubi;
        public CaichiViewMyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
