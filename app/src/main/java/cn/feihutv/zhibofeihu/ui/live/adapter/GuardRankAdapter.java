package cn.feihutv.zhibofeihu.ui.live.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomContriResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetUserGuardListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetUserGuardsResponse;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.TCUtils;

/**
 * Created by huanghao on 2017/11/11.
 */

public class GuardRankAdapter extends BaseQuickAdapter<GetUserGuardsResponse.GetUserGuardsResponseData, BaseViewHolder> {

    public GuardRankAdapter(@Nullable List<GetUserGuardsResponse.GetUserGuardsResponseData> data) {
        super(R.layout.item_layout_guardrank, data);
    }
    @Override
    protected void convert(BaseViewHolder helper,GetUserGuardsResponse.GetUserGuardsResponseData item) {
        helper.setText(R.id.rank_level,helper.getAdapterPosition()+3+"");
        TCUtils.showPicWithUrl(mContext, (ImageView) helper.getView(R.id.img_head), item.getAvatar(), R.drawable.face);
        helper.setText(R.id.text_name,item.getNickname());
        TCUtils.showLevelWithUrl(mContext,(ImageView) helper.getView(R.id.img_level),item.getLevel());

        helper.setText(R.id.text_hubi, FHUtils.intToF(item.getFriendliness()));
    }
}
