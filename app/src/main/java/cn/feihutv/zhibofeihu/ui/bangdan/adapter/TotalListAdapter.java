package cn.feihutv.zhibofeihu.ui.bangdan.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.bangdan.LoadContriRankListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.bangdan.LoadLuckRankListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.bangdan.LoadLuckRecordListResponse;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.TCUtils;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/10/26 17:53
 *      desc   : 幸运榜--总榜
 *      version: 1.0
 * </pre>
 */

public class TotalListAdapter extends BaseQuickAdapter<LoadLuckRankListResponse.LoadLuckRankListResponseData, BaseViewHolder> {


    public TotalListAdapter(@Nullable List<LoadLuckRankListResponse.LoadLuckRankListResponseData> data) {
        super(R.layout.item_layout_luck_total, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LoadLuckRankListResponse.LoadLuckRankListResponseData item) {
        TCUtils.showPicWithUrl(mContext, (ImageView) helper.getView(R.id.iv_head), item.getHeadUrl(), R.drawable.face);
        helper.setText(R.id.tv_rank, String.valueOf(item.getRank()));
        helper.setText(R.id.tv_nick, item.getNickName());
        helper.setText(R.id.tv_count, FHUtils.intToF(item.getObtainHB()));
        helper.setText(R.id.tv_awardcount, FHUtils.intToF(item.getAwardCnt()));
        helper.setVisible(R.id.iv_live, item.isLiveStatus());
    }


}
