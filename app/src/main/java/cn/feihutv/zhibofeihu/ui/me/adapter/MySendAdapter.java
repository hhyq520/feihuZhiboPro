package cn.feihutv.zhibofeihu.ui.me.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.vip.GetVipReceiveLogResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.vip.GetVipSendLogResponse;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.feihutv.zhibofeihu.utils.TimeUtil;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/16 10:27
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class MySendAdapter extends BaseQuickAdapter<GetVipSendLogResponse.SendLogList, BaseViewHolder> {


    public MySendAdapter(@Nullable List<GetVipSendLogResponse.SendLogList> data) {
        super(R.layout.item_layout_my_receive, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GetVipSendLogResponse.SendLogList item) {
        helper.setText(R.id.tv_name, item.getNickname());
        TCUtils.showPicWithUrl(mContext, (ImageView) helper.getView(R.id.iv_head), item.getAvatar(), R.drawable.face);
        String vipName = FeihuZhiboApplication.getApplication().mDataManager.getSysVipNameById(String.valueOf(item.getGoodsId())).getGoodsName();
        String[] split = vipName.split("\\s+");
        helper.setText(R.id.tv_time_length, "赠送会员：" + split[0]);
        helper.setText(R.id.tv_time, TimeUtil.getChatTime(true, item.getTime() * 1000));
    }
}
