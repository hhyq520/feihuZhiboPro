package cn.feihutv.zhibofeihu.ui.live.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetUserGuardsResponse;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.TCUtils;

/**
 * Created by huanghao on 2017/11/11.
 */

public class PcGuardRankAdapter extends BaseQuickAdapter<GetUserGuardsResponse.GetUserGuardsResponseData, BaseViewHolder> {

    public PcGuardRankAdapter(@Nullable List<GetUserGuardsResponse.GetUserGuardsResponseData> data) {
        super(R.layout.item_pc_guardrank, data);
    }
    @Override
    protected void convert(BaseViewHolder helper,GetUserGuardsResponse.GetUserGuardsResponseData item) {
        if(helper.getAdapterPosition()==0){
            helper.setVisible(R.id.rank_level,false);
            helper.setVisible(R.id.img_rank,true);
            helper.setBackgroundRes(R.id.img_rank,R.drawable.icon_first_logo);
        }else if(helper.getAdapterPosition()==1){
            helper.setVisible(R.id.rank_level,false);
            helper.setVisible(R.id.img_rank,true);
            helper.setBackgroundRes(R.id.img_rank,R.drawable.icon_second_logo);
        }else if(helper.getAdapterPosition()==2){
            helper.setVisible(R.id.rank_level,false);
            helper.setVisible(R.id.img_rank,true);
            helper.setBackgroundRes(R.id.img_rank,R.drawable.icon_third_logo);
        }else {
            helper.setVisible(R.id.rank_level,true);
            helper.setVisible(R.id.img_rank,false);
        }
        helper.setText(R.id.rank_level,helper.getAdapterPosition()+1+"");
        TCUtils.showPicWithUrl(mContext, (ImageView) helper.getView(R.id.img_head), item.getAvatar(), R.drawable.face);
        helper.setText(R.id.text_name,item.getNickname());
        TCUtils.showLevelWithUrl(mContext,(ImageView) helper.getView(R.id.img_level),item.getLevel());

        helper.setText(R.id.text_hubi, FHUtils.intToF(item.getFriendliness())+"");
    }
}
