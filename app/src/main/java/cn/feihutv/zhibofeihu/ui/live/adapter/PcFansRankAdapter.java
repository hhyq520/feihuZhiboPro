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
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.TCUtils;

/**
 * Created by huanghao on 2017/11/11.
 */

public class PcFansRankAdapter extends BaseQuickAdapter<LoadRoomContriResponce.RoomContriData, BaseViewHolder> {

    public PcFansRankAdapter(@Nullable List<LoadRoomContriResponce.RoomContriData> data) {
        super(R.layout.item_pc_fansrank, data);
    }
    @Override
    protected void convert(BaseViewHolder helper, LoadRoomContriResponce.RoomContriData item) {
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
        TCUtils.showPicWithUrl(mContext, (ImageView) helper.getView(R.id.img_head), item.getHeadUrl(), R.drawable.face);
        helper.setText(R.id.text_name,item.getNickName());
        TCUtils.showLevelWithUrl(mContext,(ImageView) helper.getView(R.id.img_level),item.getLevel());
        if(item.getVip()>0){
            if(item.isVipExpired()){
                helper.setVisible(R.id.img_vip,false);
            }else{
                helper.setVisible(R.id.img_vip,true);
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
        helper.setText(R.id.text_hubi, FHUtils.intToF(item.getHB())+"");
    }
}
