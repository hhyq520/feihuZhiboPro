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

public class RankAdapter extends BaseQuickAdapter<LoadContriRankListResponse.LoadContriRankListResponseData, BaseViewHolder> {


    public RankAdapter(@Nullable List<LoadContriRankListResponse.LoadContriRankListResponseData> data) {
        super(R.layout.item_layout_ranking, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LoadContriRankListResponse.LoadContriRankListResponseData item) {
        helper.setText(R.id.tv_rank, String.valueOf(item.getRank()));
        TCUtils.showPicWithUrl(mContext, (ImageView) helper.getView(R.id.iv_head), item.getHeadUrl(), R.drawable.face);
        helper.setText(R.id.tv_nick, item.getNickName());
        TCUtils.showLevelWithUrl(mContext,(ImageView)helper.getView(R.id.iv_level),item.getLevel());
        helper.setText(R.id.tv_count, String.valueOf(FHUtils.intToF(item.gethB())));

        if (item.isLiveStatus()) {
            helper.setVisible(R.id.iv_live, true);
        } else {
            helper.setVisible(R.id.iv_live, false);
        }

        if (item.isLiang()) {
            helper.setText(R.id.tv_ttf, item.getShowId());
            TCUtils.setFontBeauti(mContext, (TextView) helper.getView(R.id.tv_ttf));
        } else {
            helper.setText(R.id.tv_ttf, item.getShowId());
            TCUtils.setFontNormal(mContext, (TextView) helper.getView(R.id.tv_ttf));
        }

        String cosVipIconRootPath = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosVipIconRootPath();
        if (!item.isVipExpired()) {
            helper.setVisible(R.id.iv_vip, true);
            Glide.with(mContext).load(cosVipIconRootPath + "icon_vip" + item.getVip() + ".png").into((ImageView) helper.getView(R.id.iv_vip));
        } else {
            helper.setVisible(R.id.iv_vip, false);
        }
    }
}
