package cn.feihutv.zhibofeihu.ui.me.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LoadLiangSearchKeyResponse;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/14 13:54
 *      desc   : 搜索靓号
 *      version: 1.0
 * </pre>
 */

public class SearchLiangAdapter extends BaseQuickAdapter<LoadLiangSearchKeyResponse.LoadLiangSearchKeyResponseData, BaseViewHolder> {


    public SearchLiangAdapter(@Nullable List<LoadLiangSearchKeyResponse.LoadLiangSearchKeyResponseData> data) {
        super(R.layout.item_layout_search_liang, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LoadLiangSearchKeyResponse.LoadLiangSearchKeyResponseData item) {
        if (item.getPriceStart() == item.getPriceEnd()) {
            helper.setText(R.id.tv_start_price, String.valueOf(item.getPriceStart()));
            helper.setVisible(R.id.tv_end_price, false);
            helper.setVisible(R.id.tv_spilt, false);
        } else {
            helper.setText(R.id.tv_start_price, String.valueOf(item.getPriceStart()));
            helper.setText(R.id.tv_end_price, String.valueOf(item.getPriceEnd()));
            helper.setVisible(R.id.tv_end_price, true);
            helper.setVisible(R.id.tv_spilt, true);
        }


    }
}
