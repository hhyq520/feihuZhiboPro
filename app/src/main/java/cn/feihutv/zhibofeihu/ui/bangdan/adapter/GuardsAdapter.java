package cn.feihutv.zhibofeihu.ui.bangdan.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.bangdan.LoadContriRankListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetUserGuardsResponse;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.TCUtils;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/10/24 17:08
 *      desc   : 排行榜适配器
 *      version: 1.0
 * </pre>
 */

public class GuardsAdapter extends BaseQuickAdapter<GetUserGuardsResponse.GetUserGuardsResponseData, BaseViewHolder> {


    public GuardsAdapter(@Nullable List<GetUserGuardsResponse.GetUserGuardsResponseData> data) {
        super(R.layout.item_layout_guards, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GetUserGuardsResponse.GetUserGuardsResponseData item) {
        helper.setText(R.id.tv_rank, String.valueOf(item.getRank()));
        TCUtils.showPicWithUrl(mContext, (ImageView) helper.getView(R.id.iv_head), item.getAvatar(), R.drawable.face);
        helper.setText(R.id.tv_nick, item.getNickname());
        TCUtils.showLevelWithUrl(mContext, (ImageView) helper.getView(R.id.iv_level), item.getLevel());
        helper.setText(R.id.tv_count, String.valueOf(FHUtils.intToF(item.getFriendliness())));

    }
}
