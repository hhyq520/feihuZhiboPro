package cn.feihutv.zhibofeihu.ui.widget.dialog.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.db.model.SysGiftNewBean;
import cn.feihutv.zhibofeihu.data.db.model.SysGoodsNewBean;
import cn.feihutv.zhibofeihu.data.local.model.SysDataEntity;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;


/**
 * Created by huanghao on 2017/4/25.
 */

public class GiftLandAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<SysGiftNewBean> lists;//数据源
    private boolean isBagGift;
    public GiftLandAdapter(Context context, List<SysGiftNewBean> lists, boolean isBagGift) {
        this.context = context;
        this.lists = lists;
        this.isBagGift=isBagGift;
    }

    public void setDatas(List<SysGiftNewBean> lists, boolean isBagGift){
        this.lists = lists;
        this.isBagGift=isBagGift;
        notifyDataSetChanged();
    }

    public interface GiftLandItemListener{
        void itemClick(int pos);
    }
    private GiftLandItemListener mListener;
    public void setGiftLandItemListener(GiftLandItemListener listener){
        mListener=listener;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        GViewHolder gViewHolder = new GViewHolder(getView(parent, R.layout.gift_land_item_view));
        return gViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int pos = position;//假设mPageSiez
        GViewHolder gViewHolder=(GViewHolder)holder;
        if(isBagGift){
            gViewHolder.giftPrice.setText(lists.get(pos).getGiftCount()+"个");
        }else{
            SysGoodsNewBean sysGoodsBean= FeihuZhiboApplication.getApplication().mDataManager.getGoodsBeanByGiftID(lists.get(pos).getId());
            if(sysGoodsBean!=null){
                gViewHolder.giftPrice.setText(sysGoodsBean.getPrice()+"币");
            }else{
                gViewHolder.giftPrice.setText("");
            }
        }
        gViewHolder.giftName.setText(lists.get(pos).getName());
        String url="";
        int vipLevel=SharePreferenceUtil.getSessionInt(FeihuZhiboApplication.getApplication(), "PREF_KEY_VIP");
        boolean isvipExpired=SharePreferenceUtil.getSessionBoolean(FeihuZhiboApplication.getApplication(), "PREF_KEY_VIPEXPIRED");
        boolean isGuard=SharePreferenceUtil.getSessionBoolean(FeihuZhiboApplication.getApplication(), "meisGuard",false);
        if(FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean()!=null&&FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().size()>0){
            if ((vipLevel> 0 && !isvipExpired)||isGuard) {
                if(lists.get(pos).getEnableVip()==1){
                    String iconName=lists.get(pos).getIcon().substring(0,lists.get(pos).getIcon().lastIndexOf("."))+"_vip.png";
                    url=FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosGiftRootPath()+"/"+iconName;
                }else{
                    url=FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosGiftRootPath()+"/"+lists.get(pos).getIcon();
                }
            }else {
                url=FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosGiftRootPath()+"/"+lists.get(pos).getIcon();
            }
            RequestBuilder<Bitmap> requestBuilder = Glide.with(context).asBitmap().apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL));
            requestBuilder.load(url).into(gViewHolder.giftImg);

        }

        if (lists.get(pos).isSelected()) {
            gViewHolder.checkbox.setSelected(true);
        } else {
            gViewHolder.checkbox.setSelected(false);
        }

        if(!android.text.TextUtils.isEmpty(lists.get(pos).getTagIcon())){
            if(FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean()!=null&&FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().size()>0){
                gViewHolder.luck_img.setVisibility(View.VISIBLE);
                String urla=FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosTagRootPath()+"/"+lists.get(pos).getTagIcon();
                RequestBuilder<Bitmap> requestBuilder = Glide.with(context).asBitmap().apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL));
                requestBuilder.load(urla).into(gViewHolder.luck_img);
            }else{
                gViewHolder.luck_img.setVisibility(View.GONE);
            }
        }else{
            gViewHolder.luck_img.setVisibility(View.GONE);
        }
        gViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null){
                    mListener.itemClick(pos);
                }
            }
        });
    }

    private View getView(ViewGroup parent, int layoutId) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }


     class GViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.checkbox)
        LinearLayout checkbox;
        @BindView(R.id.gift_img)
        ImageView giftImg;
        @BindView(R.id.gift_name)
        TextView giftName;
        @BindView(R.id.gift_price)
        TextView giftPrice;
         @BindView(R.id.luck_img)
         ImageView luck_img;
         public GViewHolder(View view) {
             super(view);
             ButterKnife.bind(this, view);
        }
    }
}
