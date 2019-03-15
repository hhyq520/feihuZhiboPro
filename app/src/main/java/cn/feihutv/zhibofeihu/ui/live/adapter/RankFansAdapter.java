package cn.feihutv.zhibofeihu.ui.live.adapter;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomContriResponce;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.FileUtils;
import cn.feihutv.zhibofeihu.utils.TCUtils;

/**
 * Created by Administrator on 2017/2/20.
 */

public class RankFansAdapter extends BaseQuickAdapter<LoadRoomContriResponce.RoomContriData, BaseViewHolder> {

    public RankFansAdapter(@Nullable List<LoadRoomContriResponce.RoomContriData> data) {
        super(R.layout.rank_fans_item_view, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LoadRoomContriResponce.RoomContriData item) {
        if(helper.getAdapterPosition()==0){
            helper.setImageResource(R.id.rank_img, R.drawable.rank_first);
            helper.setVisible(R.id.rank_img, true);
            helper.setVisible(R.id.rank_txt, false);

        }else if(helper.getAdapterPosition()==1){
            helper.setImageResource(R.id.rank_img, R.drawable.rank_second);
            helper.setVisible(R.id.rank_txt, false);
            helper.setVisible(R.id.rank_img, true);
        }else if(helper.getAdapterPosition()==2){
            helper.setImageResource(R.id.rank_img, R.drawable.rank_third);
            helper.setVisible(R.id.rank_txt, false);
            helper.setVisible(R.id.rank_img, true);
        }else{
            helper.setVisible(R.id.rank_img, false);
            helper.setVisible(R.id.rank_txt, true);
            helper.setText(R.id.rank_txt, String.valueOf(helper.getAdapterPosition()));
        }
        TCUtils.showPicWithUrl(mContext, (ImageView) helper.getView(R.id.host_img),item.getHeadUrl(),R.drawable.face);
        helper.setText(R.id.host_name, item.getNickName());
        TCUtils.showLevelWithUrl(mContext,(ImageView)helper.getView(R.id.grade_img),item.getLevel());
        helper.setText(R.id.count, FHUtils.intToF(item.getHB()));
        helper.setText(R.id.rank_txt, String.valueOf(item.getRank()));
    }
}

