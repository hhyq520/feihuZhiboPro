package cn.feihutv.zhibofeihu.ui.bangdan.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetUserGuardListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetUserGuardsResponse;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.TCUtils;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/10/24 17:08
 *      desc   :
 *      version: 1.0
 * </pre>
 */

public class GuardListAdapter extends BaseQuickAdapter<GetUserGuardListResponse.GetUserGuardListResponseData, BaseViewHolder> {


    public GuardListAdapter(@Nullable List<GetUserGuardListResponse.GetUserGuardListResponseData> data) {
        super(R.layout.item_layout_guardlist, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GetUserGuardListResponse.GetUserGuardListResponseData item) {
        TCUtils.showPicWithUrl(mContext, (ImageView) helper.getView(R.id.iv_head), item.getAvatar(), R.drawable.face);
        helper.setText(R.id.tv_nick, item.getNickname());
        TCUtils.showLevelWithUrl(mContext, (ImageView) helper.getView(R.id.iv_level), item.getLevel());
        helper.setText(R.id.tv_count, ((int) item.getTime() / (60 * 60 * 24)) + "天");

        helper.addOnClickListener(R.id.tv_renew);
        if (((int) item.getTime() / (60 * 60 * 24)) <= 7) {
            helper.setVisible(R.id.tv_renew, true);
        } else {
            helper.setVisible(R.id.tv_renew, false);
        }
    }
}
