package cn.feihutv.zhibofeihu.ui.bangdan.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.bangdan.LoadLuckRecordListResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.TCUtils;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/10/26 17:53
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class DayListAdapter extends BaseQuickAdapter<LoadLuckRecordListResponse.LoadLuckRecordListResponseData.ListResponseData, BaseViewHolder> {

    private LoadUserDataBaseResponse.UserData userData;

    public DayListAdapter(@Nullable List<LoadLuckRecordListResponse.LoadLuckRecordListResponseData.ListResponseData> data, LoadUserDataBaseResponse.UserData userData) {
        super(R.layout.item_layout_daylist, data);
        this.userData = userData;
    }

    @Override
    protected void convert(BaseViewHolder helper, LoadLuckRecordListResponse.LoadLuckRecordListResponseData.ListResponseData item) {
        TCUtils.showPicWithUrl(mContext, (ImageView) helper.getView(R.id.iv_user), item.getHeadUrl(), R.drawable.face);
        TCUtils.showPicWithUrl(mContext, (ImageView) helper.getView(R.id.iv_host), item.getMasterHeadUrl(), R.drawable.face);
        String luckGiftIconRootPath = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosLuckGiftIconRootPath();
        helper.setText(R.id.tv_user_nick, item.getNickName());
        helper.setText(R.id.tv_host_nick, item.getMasterNickname());
        helper.setText(R.id.tv_gift_count, "x" + String.valueOf(item.getObtainGiftCnt()));
        helper.setText(R.id.tv_hb, "获得" + FHUtils.intToF(item.getObtainHB()));
        helper.setText(R.id.tv_time, TCUtils.getTime(item.getAwardTime() * 1000));
        helper.setVisible(R.id.iv_host_live, item.isLiveStatus());
        helper.addOnClickListener(R.id.rl_user).addOnClickListener(R.id.rl_host);


        if (userData.getVip() > 0) {
            if (!userData.isVipExpired()) {
                // vip未过期
                if (FeihuZhiboApplication.getApplication().mDataManager.getGiftBeanByID(String.valueOf(item.getObtainGiftId())).getEnableVip() == 1) {
                    Glide.with(mContext).load(luckGiftIconRootPath + item.getObtainGiftId() + "_vip.png").into((ImageView) helper.getView(R.id.iv_gift));
                } else {
                    Glide.with(mContext).load(luckGiftIconRootPath + item.getObtainGiftId() + ".png").into((ImageView) helper.getView(R.id.iv_gift));
                }
            } else {
                // vip已过期
                Glide.with(mContext).load(luckGiftIconRootPath + item.getObtainGiftId() + ".png").into((ImageView) helper.getView(R.id.iv_gift));
            }
        } else {
            // 为开通过vip
            Glide.with(mContext).load(luckGiftIconRootPath + item.getObtainGiftId() + ".png").into((ImageView) helper.getView(R.id.iv_gift));
        }


    }

}
