package cn.feihutv.zhibofeihu.ui.me.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetPayListResponse;
import cn.feihutv.zhibofeihu.utils.TimeUtil;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/3 15:23
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class CzRecordAdapter extends BaseQuickAdapter<GetPayListResponse.GetPayListResponseData, BaseViewHolder> {

    public CzRecordAdapter(@Nullable List<GetPayListResponse.GetPayListResponseData> data) {
        super(R.layout.item_tixian, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GetPayListResponse.GetPayListResponseData item) {
        String minTime = TimeUtil.getMinTime((long) (item.getCreatetime()) * 1000);
        String maxTime = TimeUtil.getMonthTime((long) (item.getCreatetime()) * 1000);
        helper.setText(R.id.tv_month, minTime);
        helper.setText(R.id.tv_year, maxTime);
        helper.setText(R.id.tv_money, "￥" + item.getAmount());
        helper.setText(R.id.tv_hubi, item.gethB() + "虎币");
        if (item.getStatus() == 1) {
            helper.setText(R.id.tv_close, "未支付");
        } else if (item.getStatus() == 2) {
            helper.setText(R.id.tv_close, "充值失败");
        } else if (item.getStatus() == 3) {
            helper.setText(R.id.tv_close, "充值成功");
        }
        if (item.getPf().equals("weixin")) {
            helper.setImageResource(R.id.iv_icon, R.drawable.icon_weixin);
        } else {
            helper.setImageResource(R.id.iv_icon, R.drawable.icon_alipay);
        }
    }
}
