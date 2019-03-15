package cn.feihutv.zhibofeihu.ui.me.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.me.FollowsResponse;
import cn.feihutv.zhibofeihu.utils.TCUtils;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/2 11:06
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class ConcernAdapter extends BaseQuickAdapter<FollowsResponse.FollowsResponseData, BaseViewHolder> {


    public ConcernAdapter(@Nullable List<FollowsResponse.FollowsResponseData> data) {
        super(R.layout.item_concern_view, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FollowsResponse.FollowsResponseData item) {
        TCUtils.showPicWithUrl(mContext, (ImageView) helper.getView(R.id.item_img_head), item.getHeadUrl(), R.drawable.face);
        TCUtils.showLevelWithUrl(mContext, (ImageView) helper.getView(R.id.item_img_level), item.getLevel());
        helper.setText(R.id.item_name, item.getNickName());
        helper.setVisible(R.id.item_img_beauti, item.isLiang());
        if (item.isVipExpired()) {
            helper.setVisible(R.id.item_img_vip, false);
        } else {
            String cosVipIconRootPath = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosVipIconRootPath();
            Glide.with(mContext).load(cosVipIconRootPath + "icon_vip" + item.getVip() + ".png").into((ImageView) helper.getView(R.id.item_img_vip));
            helper.setVisible(R.id.item_img_vip, true);
        }

    }
}
