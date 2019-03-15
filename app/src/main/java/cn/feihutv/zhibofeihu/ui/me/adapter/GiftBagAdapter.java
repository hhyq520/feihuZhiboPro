package cn.feihutv.zhibofeihu.ui.me.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.DataManager;
import cn.feihutv.zhibofeihu.data.db.model.SysGiftNewBean;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LoadBagGiftsResponse;
import cn.feihutv.zhibofeihu.ui.me.OnItemClickListener;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.UiUtil;
import cn.feihutv.zhibofeihu.utils.glideutil.GlideApp;

/**
 * Created by huanghao on 2017/5/3.
 */

public class GiftBagAdapter extends BaseAdapter {
    private Context context;

    private List<LoadBagGiftsResponse.LoadBagGiftsResponseData> datas;

    public GiftBagAdapter(Context context) {
        this.context = context;
        datas = new ArrayList<>();
    }

    public void setDatas(List<LoadBagGiftsResponse.LoadBagGiftsResponseData> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolderBag viewHolder = null;
        if (convertView != null) {
            viewHolder = (ViewHolderBag) convertView.getTag();
        } else {
            viewHolder = new ViewHolderBag();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_layout_gift_bag, null);
            viewHolder.tvCount = (TextView) convertView.findViewById(R.id.gift_count);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.gift_name);
            viewHolder.giftImg = (ImageView) convertView.findViewById(R.id.gift_bg);
            viewHolder.giftFrm = (FrameLayout) convertView.findViewById(R.id.gift_frm);
            viewHolder.clickView = (FrameLayout) convertView.findViewById(R.id.click_view);
            convertView.setTag(viewHolder);
        }
        UiUtil.initialize(context);
        int screenWidth = UiUtil.getScreenWidth();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((screenWidth - 52) / 3, (screenWidth - 52) / 3);
        viewHolder.giftFrm.setLayoutParams(layoutParams);

        viewHolder.tvCount.setText(datas.get(position).getCnt() + "个");
        if (datas.get(position).getId() == 20) {
            viewHolder.clickView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.clickView.setVisibility(View.INVISIBLE);
        }
        viewHolder.giftFrm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(datas.get(position).getId(), datas.get(position).getCnt());
                }
            }
        });

        try {
            DataManager dataManager = FeihuZhiboApplication.getApplication().mDataManager;
            SysGiftNewBean sysGiftNewBean = dataManager.getGiftBeanByID(String.valueOf(datas.get(position).getId()));
            if (sysGiftNewBean != null) {
                String url = dataManager.getSysConfigBean().get(0).getCosGiftRootPath() + "/" + sysGiftNewBean.getIcon();
                GlideApp.loadImg(context, url, DiskCacheStrategy.ALL, viewHolder.giftImg);
                viewHolder.tvName.setText(sysGiftNewBean.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    class ViewHolderBag {
        TextView tvCount;
        TextView tvName;
        ImageView giftImg;
        FrameLayout giftFrm;
        FrameLayout clickView;
    }
}
