package cn.feihutv.zhibofeihu.ui.live.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomContriResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.GetExtGameIconResponce;
import cn.feihutv.zhibofeihu.utils.TCUtils;

/**
 * Created by huanghao on 2017/11/30.
 */

public class GameImageAdapter extends BaseQuickAdapter<GetExtGameIconResponce.ExtGameIconData, BaseViewHolder> {
    public GameImageAdapter(@Nullable List<GetExtGameIconResponce.ExtGameIconData> data) {
        super(R.layout.item_game_view, data);
    }
    @Override
    protected void convert(BaseViewHolder helper, GetExtGameIconResponce.ExtGameIconData item) {
        TCUtils.showPicWithUrl(mContext, (ImageView) helper.getView(R.id.img_src), item.getGameIcon(), R.drawable.background_image_default);
    }
}
