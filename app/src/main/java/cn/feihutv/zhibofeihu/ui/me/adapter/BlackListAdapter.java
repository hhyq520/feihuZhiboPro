package cn.feihutv.zhibofeihu.ui.me.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetBlacklistResponse;
import cn.feihutv.zhibofeihu.utils.TCUtils;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/6 15:40
 *      desc   : 黑名单
 *      version: 1.0
 * </pre>
 */

public class BlackListAdapter extends BaseQuickAdapter<GetBlacklistResponse.GetBlacklistResponseData, BaseViewHolder> {


    public BlackListAdapter(@Nullable List<GetBlacklistResponse.GetBlacklistResponseData> data) {
        super(R.layout.item_blacklist, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GetBlacklistResponse.GetBlacklistResponseData item) {
        TCUtils.showPicWithUrl(mContext, (ImageView) helper.getView(R.id.iv_headPic), item.getHeadUrl(), R.drawable.face);
        helper.setText(R.id.tv_id, item.getAccountId());
        helper.addOnClickListener(R.id.delete_btn);
        helper.setText(R.id.tv_name, item.getNickName());
        TCUtils.showLevelWithUrl(mContext, (ImageView) helper.getView(R.id.img_level), item.getLevel());
    }
}
