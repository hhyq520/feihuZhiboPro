package cn.feihutv.zhibofeihu.ui.me.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetEncashListResponse;
import cn.feihutv.zhibofeihu.utils.TimeUtil;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/3 19:02
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class WithdrawalrecordAdapter extends BaseQuickAdapter<GetEncashListResponse.GetEncashListResponseData, BaseViewHolder> {


    public WithdrawalrecordAdapter(@Nullable List<GetEncashListResponse.GetEncashListResponseData> data) {
        super(R.layout.item_tixian, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GetEncashListResponse.GetEncashListResponseData item) {
        String minTime = TimeUtil.getMinTime((long) (item.getCreateTime()) * 1000);
        String maxTime = TimeUtil.getMonthTime((long) (item.getCreateTime()) * 1000);
        helper.setText(R.id.tv_month, minTime);
        helper.setText(R.id.tv_year, maxTime);
        helper.setText(R.id.tv_money, "￥" + item.getAmount());
        helper.setText(R.id.tv_hubi, item.getgHB() + "金虎币");
        if (item.getStatus() == 1) {
            helper.setText(R.id.tv_close, "交易进行中");
        } else if (item.getStatus() == 2) {
            helper.setText(R.id.tv_close, "交易关闭");
        } else if (item.getStatus() == 3) {
            helper.setText(R.id.tv_close, "交易完成");
        }
    }
}
