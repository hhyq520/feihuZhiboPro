package cn.feihutv.zhibofeihu.ui.live.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.LoadRoomMemberResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.RoomVipResponce;
import cn.feihutv.zhibofeihu.utils.TCUtils;

/**
 * Created by huanghao on 2017/11/10.
 */

public class OnlineUserAdapter extends BaseQuickAdapter<LoadRoomMemberResponce.MemberData, BaseViewHolder> {

    public OnlineUserAdapter(@Nullable List<LoadRoomMemberResponce.MemberData> data) {
        super(R.layout.item_layout_roomvip, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, LoadRoomMemberResponce.MemberData item) {
        TCUtils.showPicWithUrl(mContext, (ImageView) helper.getView(R.id.img_head), item.getHeadUrl(), R.drawable.face);
        helper.setText(R.id.nick_name,item.getNickName());
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
        if(item.isMgr()){
            helper.setVisible(R.id.img_kangkong,true);
        }else{
            helper.setVisible(R.id.img_kangkong,false);
        }
        if(item.isLiang()){
            helper.setVisible(R.id.img_liang,true);
        }else{
            helper.setVisible(R.id.img_liang,false);
        }
    }
}
