package cn.feihutv.zhibofeihu.ui.me.adapter;

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
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomContriResponce;
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

public class ContriAdapter extends BaseQuickAdapter<LoadRoomContriResponce.RoomContriData, BaseViewHolder> {


    public ContriAdapter(@Nullable List<LoadRoomContriResponce.RoomContriData> data) {
        super(R.layout.item_contri_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LoadRoomContriResponce.RoomContriData item) {
        helper.setText(R.id.tv_rank, String.valueOf(item.getRank()));
        TCUtils.showPicWithUrl(mContext, (ImageView) helper.getView(R.id.iv_head), item.getHeadUrl(), R.drawable.face);
        helper.setText(R.id.tv_nick, item.getNickName());
        TCUtils.showLevelWithUrl(mContext,(ImageView)helper.getView(R.id.iv_level),item.getLevel());
        helper.setText(R.id.tv_count, String.valueOf(FHUtils.intToF(item.getHB())));
        helper.setVisible(R.id.iv_beauti, item.isLiang());

        String cosVipIconRootPath = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosVipIconRootPath();
        if (!item.isVipExpired()) {
            helper.setVisible(R.id.iv_vip, true);
            Glide.with(mContext).load(cosVipIconRootPath + "icon_vip" + item.getVip() + ".png").into((ImageView) helper.getView(R.id.iv_vip));
        } else {
            helper.setVisible(R.id.iv_vip, false);
        }
    }
}
