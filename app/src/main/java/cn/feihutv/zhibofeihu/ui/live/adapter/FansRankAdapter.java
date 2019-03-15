package cn.feihutv.zhibofeihu.ui.live.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomContriResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.RoomVipResponce;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.TCUtils;

/**
 * Created by huanghao on 2017/11/11.
 */

public class FansRankAdapter extends BaseQuickAdapter<LoadRoomContriResponce.RoomContriData, BaseViewHolder> {

    public FansRankAdapter(@Nullable List<LoadRoomContriResponce.RoomContriData> data) {
        super(R.layout.item_layout_fansrank, data);
    }
    @Override
    protected void convert(BaseViewHolder helper, LoadRoomContriResponce.RoomContriData item) {
        helper.setText(R.id.rank_level,helper.getAdapterPosition()+3+"");
        TCUtils.showPicWithUrl(mContext, (ImageView) helper.getView(R.id.img_head), item.getHeadUrl(), R.drawable.face);
        helper.setText(R.id.text_name,item.getNickName());
        TCUtils.showLevelWithUrl(mContext,(ImageView) helper.getView(R.id.img_level),item.getLevel());
        if(item.getVip()>0){
            if(item.isVipExpired()){
                helper.setVisible(R.id.img_vip,false);
            }else{
                String cosVipIconRootPath = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosVipIconRootPath();
                Glide.with(mContext).load(cosVipIconRootPath + "icon_vip" + item.getVip() + ".png").into((ImageView) helper.getView(R.id.img_vip));
            }
        }else {
            helper.setVisible(R.id.img_vip,false);
        }
        if(item.getGuardType()==0){
            helper.setVisible(R.id.img_guard,false);
        }else{
            if(item.isGuardExpired()){
                helper.setVisible(R.id.img_guard,false);
            }else{
                helper.setVisible(R.id.img_guard,true);
            }
        }
        helper.setText(R.id.text_hubi, FHUtils.intToF(item.getHB()));
    }
}
