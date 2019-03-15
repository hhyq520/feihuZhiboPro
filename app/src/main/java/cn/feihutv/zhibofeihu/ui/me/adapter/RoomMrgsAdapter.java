package cn.feihutv.zhibofeihu.ui.me.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.GetRoomMrgsResponce;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.feihutv.zhibofeihu.utils.glideutil.GlideApp;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/4 15:26
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class RoomMrgsAdapter extends BaseQuickAdapter<GetRoomMrgsResponce.RoomMrgData, BaseViewHolder> {

    private boolean isisZhibo;
    public RoomMrgsAdapter(@Nullable List<GetRoomMrgsResponce.RoomMrgData> data,boolean isZhibo) {
        super(R.layout.item_roommrgs, data);
        this.isisZhibo=isZhibo;
    }

    @Override
    protected void convert(BaseViewHolder helper, GetRoomMrgsResponce.RoomMrgData item) {
        TCUtils.showPicWithUrl(mContext, (ImageView) helper.getView(R.id.iv_headPic), item.getHeadUrl(), R.drawable.face);
        helper.setText(R.id.tv_name, item.getNickName());
        TCUtils.showLevelWithUrl(mContext, (ImageView) helper.getView(R.id.iv_level), item.getLevel());
        helper.setVisible(R.id.iv_beauti, item.isLiang());
        helper.addOnClickListener(R.id.tv_cancle);
        if(item.getVip()>0) {
            if (item.isVipExpired()) {
                helper.setVisible(R.id.iv_vip, false);
            } else {
                helper.setVisible(R.id.iv_vip, true);
                String cosVipIconRootPath = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosVipIconRootPath();
                GlideApp.loadImg(mContext, cosVipIconRootPath + "icon_vip" + item.getVip() + ".png", R.drawable.icon_vip_1, (ImageView) helper.getView(R.id.iv_vip));
            }
        }else{
            helper.setVisible(R.id.iv_vip, false);
        }
        if(isisZhibo){
            if(item.getGuardType()==0){
                helper.setVisible(R.id.iv_guard, false);
            }else{
                if(item.isGuardExpired()){
                    helper.setVisible(R.id.iv_guard, false);
                }else {
                    helper.setVisible(R.id.iv_guard, true);
                }
            }
        }
    }
}
