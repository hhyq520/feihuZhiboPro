package cn.feihutv.zhibofeihu.ui.bangdan.adapter;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.bangdan.GetGuardRankResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.bangdan.LoadContriRankListResponse;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.FileUtils;
import cn.feihutv.zhibofeihu.utils.TCUtils;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/10/24 17:08
 *      desc   : 守护榜适配器
 *      version: 1.0
 * </pre>
 */

public class GuardAdapter extends BaseQuickAdapter<GetGuardRankResponse.GetGuardRankResponseData, BaseViewHolder> {


    public GuardAdapter(@Nullable List<GetGuardRankResponse.GetGuardRankResponseData> data) {
        super(R.layout.item_layout_guard, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GetGuardRankResponse.GetGuardRankResponseData item) {

        helper.setText(R.id.tv_rank, String.valueOf(item.getRank()));
        TCUtils.showPicWithUrl(mContext, (ImageView) helper.getView(R.id.iv_head), item.getAvatar(), R.drawable.face);
        helper.setText(R.id.tv_nick, item.getNickname());
        TCUtils.showLevelWithUrl(mContext, (ImageView) helper.getView(R.id.iv_level), item.getLevel());
        helper.setText(R.id.tv_count, String.valueOf(FHUtils.intToPeople(item.getGuardCnt())) + "人");

        helper.setVisible(R.id.iv_live, item.isLiveStatus());

        helper.setVisible(R.id.iv_beauti, item.isLiang());
        String mCosVipIconRootPath = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosVipIconRootPath();
        if (item.getVip() > 0) {
            if (item.isVipExpired()) {
                // 已过期
                helper.setVisible(R.id.iv_vip, false);
            } else {

                helper.setVisible(R.id.iv_vip, true);
                Glide.with(mContext).load(mCosVipIconRootPath + "icon_vip" + item.getVip() + ".png").into((ImageView) helper.getView(R.id.iv_vip));
            }
        } else {
            helper.setVisible(R.id.iv_vip, false);
        }
    }
}
