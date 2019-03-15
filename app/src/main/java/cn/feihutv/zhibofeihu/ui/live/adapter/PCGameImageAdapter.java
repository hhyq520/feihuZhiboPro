package cn.feihutv.zhibofeihu.ui.live.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.GetExtGameIconResponce;
import cn.feihutv.zhibofeihu.utils.TCUtils;

/**
 * Created by huanghao on 2017/11/30.
 */

public class PCGameImageAdapter extends BaseQuickAdapter<GetExtGameIconResponce.ExtGameIconData, BaseViewHolder> {
    public PCGameImageAdapter(@Nullable List<GetExtGameIconResponce.ExtGameIconData> data) {
        super(R.layout.item_pcgame_view, data);
    }
    @Override
    protected void convert(BaseViewHolder helper, GetExtGameIconResponce.ExtGameIconData item) {
        TCUtils.showPicWithUrl(mContext, (ImageView) helper.getView(R.id.img_lucky), item.getGameIcon(), R.drawable.background_image_default);
        helper.setText(R.id.txt_name,item.getGameName());
    }
}
