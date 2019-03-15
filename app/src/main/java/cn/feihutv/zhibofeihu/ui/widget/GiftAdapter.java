package cn.feihutv.zhibofeihu.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import cn.feihutv.zhibofeihu.ui.live.pclive.LivePlayInPCActivity;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;

/**
 * Created by huanghao on 2017/3/11.
 */

public class GiftAdapter extends BaseAdapter {

    private Context context;
    private List<SysGiftNewBean> lists;//数据源
    private int mIndex; // 页数下标，标示第几页，从0开始
    private int mPargerSize;// 每页显示的最大的数量
    private boolean isBagGift;

    public GiftAdapter(Context context, List<SysGiftNewBean> lists,
                       int mIndex, int mPargerSize, boolean isBagGift) {
        this.context = context;
        this.lists = lists;
        this.mIndex = mIndex;
        this.mPargerSize = mPargerSize;
        this.isBagGift=isBagGift;
    }

    public void setDatas(List<SysGiftNewBean> lists,
                    int mIndex, int mPargerSize,boolean isBagGift){
        this.lists = lists;
        this.mIndex = mIndex;
        this.mPargerSize = mPargerSize;
        this.isBagGift=isBagGift;
        notifyDataSetChanged();
    }

    /**
     * 先判断数据及的大小是否显示满本页lists.size() > (mIndex + 1)*mPagerSize
     * 如果满足，则此页就显示最大数量lists的个数
     * 如果不够显示每页的最大数量，那么剩下几个就显示几个
     */
    @Override
    public int getCount() {
        return lists.size() > (mIndex + 1) * mPargerSize ?
            mPargerSize : (lists.size() - mIndex * mPargerSize);
    }

    @Override
    public SysGiftNewBean getItem(int arg0) {
        return lists.get(arg0 + mIndex * mPargerSize);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0 + mIndex * mPargerSize;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.gift_item_view, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //重新确定position因为拿到的总是数据源，数据源是分页加载到每页的GridView上的
        final int pos = position + mIndex * mPargerSize;//假设mPageSiez
        //假设mPagerSize=8，假如点击的是第二页（即mIndex=1）上的第二个位置item(position=1),那么这个item的实际位置就是pos=9
        if(isBagGift){
                holder.giftPrice.setText(lists.get(pos).getGiftCount()+"个");
        }else{
            SysGoodsNewBean sysGoodsBean= FeihuZhiboApplication.getApplication().mDataManager.getGoodsBeanByGiftID(lists.get(pos).getId());
            if(sysGoodsBean!=null){
                holder.giftPrice.setText(sysGoodsBean.getPrice()+"币");
            }else{
                holder.giftPrice.setText("");
            }
        }
        holder.giftName.setText(lists.get(pos).getName());
        String url="";
        int vipLevel=SharePreferenceUtil.getSessionInt(FeihuZhiboApplication.getApplication(), "PREF_KEY_VIP");
        boolean isvipExpired=SharePreferenceUtil.getSessionBoolean(FeihuZhiboApplication.getApplication(), "PREF_KEY_VIPEXPIRED");
        boolean isGuard=SharePreferenceUtil.getSessionBoolean(FeihuZhiboApplication.getApplication(), "meisGuard");
        if(FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().size()>0){
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
            requestBuilder.load(url).into(holder.giftImg);

        }

        if (lists.get(pos).isSelected()) {
            holder.checkbox.setSelected(true);
        } else {
            holder.checkbox.setSelected(false);
        }
//        if(lists.get(pos).getId().equals("1") || lists.get(pos).getId().equals("2")||lists.get(pos).getId().equals("8")){
//            holder.luckImg.setVisibility(View.VISIBLE);
//        }else{
//            holder.luckImg.setVisibility(View.GONE);
//        }
        if(!android.text.TextUtils.isEmpty(lists.get(pos).getTagIcon())){
            if(FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().size()>0){
                holder.luckImg.setVisibility(View.VISIBLE);
                String urla=FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosTagRootPath()+"/"+lists.get(pos).getTagIcon();
                RequestBuilder<Bitmap> requestBuilder = Glide.with(context).asBitmap().apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL));
                requestBuilder.load(urla).into(holder.luckImg);
            }else{
                holder.luckImg.setVisibility(View.GONE);
            }
        }else{
            holder.luckImg.setVisibility(View.GONE);
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.checkbox)
        LinearLayout checkbox;
        @BindView(R.id.gift_img)
        ImageView giftImg;
        @BindView(R.id.gift_name)
        TextView giftName;
        @BindView(R.id.gift_price)
        TextView giftPrice;
        @BindView(R.id.luck_img)
        ImageView luckImg;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
