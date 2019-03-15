package cn.feihutv.zhibofeihu.ui.live.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.db.model.SysConfigBean;
import cn.feihutv.zhibofeihu.data.db.model.SysGiftNewBean;
import cn.feihutv.zhibofeihu.data.local.model.SysDataEntity;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.UserGiftBean;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.StartLiveResponce;
import cn.feihutv.zhibofeihu.ui.live.SWCameraStreamingMvpPresenter;
import cn.feihutv.zhibofeihu.ui.live.SWCameraStreamingMvpView;
import cn.feihutv.zhibofeihu.utils.TCUtils;


/**
 * Created by huanghao on 2017/5/4.
 */

public class OffineGiftAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<StartLiveResponce.OfflineGiftsData> datas;
    private Context mContext;
    private SysConfigBean sysConfigBean;
    private SWCameraStreamingMvpPresenter<SWCameraStreamingMvpView> mvpPresenter;

    public OffineGiftAdapter(Context context) {
        mContext = context;
        datas = new ArrayList<>();
    }

    public void setDatas(List<StartLiveResponce.OfflineGiftsData> datas, SysConfigBean sysConfigBean, SWCameraStreamingMvpPresenter<SWCameraStreamingMvpView> mvpPresenter) {
        this.datas = datas;
        this.sysConfigBean = sysConfigBean;
        this.mvpPresenter = mvpPresenter;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        OfflineViewHolder offlineViewHolder = new OfflineViewHolder(getView(parent, R.layout.offline_gift_adapter));
        return offlineViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        StartLiveResponce.OfflineGiftsData info = datas.get(position);
        OfflineViewHolder viewHolder = (OfflineViewHolder) holder;
        TCUtils.showPicWithUrl(mContext, viewHolder.headImg, info.getHeadUrl(), R.drawable.face);
        viewHolder.nickName.setText(info.getNickName());
        viewHolder.giftCount.setText("*" + info.getGiftNum());
        SysGiftNewBean sysGiftBean = mvpPresenter.getGiftBeanByID(String.valueOf(info.getGiftId()));
        if (sysGiftBean != null) {
            if (sysConfigBean != null) {
                String url = sysConfigBean.getCosGiftRootPath() + "/" + sysGiftBean.getIcon();
                Glide.with(mContext).load(url).into(viewHolder.giftImg);
            }
        }
    }

    private View getView(ViewGroup parent, int layoutId) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class OfflineViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.head_img)
        ImageView headImg;
        @BindView(R.id.nick_name)
        TextView nickName;
        @BindView(R.id.gift_img)
        ImageView giftImg;
        @BindView(R.id.gift_count)
        TextView giftCount;

        public OfflineViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
