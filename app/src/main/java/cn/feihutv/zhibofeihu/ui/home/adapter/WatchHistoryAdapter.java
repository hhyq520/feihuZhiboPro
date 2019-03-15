package cn.feihutv.zhibofeihu.ui.home.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import java.util.List;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.db.model.HistoryRecordBean;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.feihutv.zhibofeihu.utils.glideutil.GlideApp;

/**
 * Created by Administrator on 2017/2/22.
 */

public class WatchHistoryAdapter extends BaseQuickAdapter<HistoryRecordBean, BaseViewHolder> {

    public WatchHistoryAdapter(@Nullable List<HistoryRecordBean> data) {
        super(R.layout.history_item_view, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HistoryRecordBean item) {
        GlideApp.loadImg(mContext, item.getHeadUrl(), R.drawable.bg, (ImageView) helper.getView(R.id.cover));
        helper.setText(R.id.live_title, item.getTitle());
        helper.setText(R.id.host_name, item.getHostName());
        helper.setText(R.id.time, TCUtils.getChatTime(item.getTime()));
    }
}
