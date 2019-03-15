package cn.feihutv.zhibofeihu.ui.home.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.home.LoadRoomListResponse;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.UiUtil;
import cn.feihutv.zhibofeihu.utils.glideutil.GlideApp;

/**
 * <pre>
 *      author : liwen.chen
 *      time   : 2017/10/17 16:19
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class HotAdapter extends BaseQuickAdapter<LoadRoomListResponse.LoadRoomListResponseData.ListData, BaseViewHolder> {


    public HotAdapter(@Nullable List<LoadRoomListResponse.LoadRoomListResponseData.ListData> data) {
        super(R.layout.layout_hot, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LoadRoomListResponse.LoadRoomListResponseData.ListData item) {


        if ((helper.getLayoutPosition() - getHeaderLayoutCount()) % 2 == 0) {
            //双数
            setViewMargin(helper.getView(R.id.relativeLayout), false, 24, 10, 24, 0);
        } else {
            //单数
            setViewMargin(helper.getView(R.id.relativeLayout), false, 10, 24, 24, 0);
        }

        helper.setText(R.id.tv_title, item.getRoomName());
        //直播封面
        GlideApp.loadImg(mContext, item.getHeadUrl(), R.drawable.bg, (ImageView) helper.getView(R.id.iv_cover));

        helper.setText(R.id.tv_nick, item.getNickName());
        helper.setText(R.id.tv_counts, item.getOnlineUserCnt());

        helper.setVisible(R.id.iv_playtype, item.isRoomStatus());

        if (!item.isRoomStatus()) {
            helper.setVisible(R.id.iv_playtype, false);
        } else {
            if (item.getBroadcastType() == 1) {
                // 设置电脑图标
                helper.setImageResource(R.id.iv_playtype, R.drawable.icon_pc);
                helper.setVisible(R.id.iv_playtype, true);
            } else if (item.getBroadcastType() == 2) {
                // 设置手机图标
                helper.setImageResource(R.id.iv_playtype, R.drawable.icon_phone);
                helper.setVisible(R.id.iv_playtype, true);
            } else {
                // 隐藏图标
                helper.setVisible(R.id.iv_playtype, false);
            }
        }

        UiUtil.initialize(mContext);
        int width = UiUtil.getScreenWidth();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) helper.getView(R.id.layout_hot_cardview).getLayoutParams();
        layoutParams.width = (width - 68) / 2;
        layoutParams.height = (10 * (width - 68)) / 27;
        helper.getView(R.id.layout_hot_cardview).setLayoutParams(layoutParams);
    }

    /**
     * 设置某个View的margin
     *
     * @param view   需要设置的view
     * @param isDp   需要设置的数值是否为DP
     * @param left   左边距
     * @param right  右边距
     * @param top    上边距
     * @param bottom 下边距
     * @return
     */
    public static ViewGroup.LayoutParams setViewMargin(View view, boolean isDp, int left, int right, int top, int bottom) {
        if (view == null) {
            return null;
        }

        int leftPx = left;
        int rightPx = right;
        int topPx = top;
        int bottomPx = bottom;
        ViewGroup.LayoutParams params = view.getLayoutParams();
        ViewGroup.MarginLayoutParams marginParams = null;
        //获取view的margin设置参数
        if (params instanceof ViewGroup.MarginLayoutParams) {
            marginParams = (ViewGroup.MarginLayoutParams) params;
        } else {
            //不存在时创建一个新的参数
            marginParams = new ViewGroup.MarginLayoutParams(params);
        }

        //根据DP与PX转换计算值
        if (isDp) {
            leftPx = dip2px(left);
            rightPx = dip2px(right);
            topPx = dip2px(top);
            bottomPx = dip2px(bottom);
        }
        //设置margin
        marginParams.setMargins(leftPx, topPx, rightPx, bottomPx);
        view.setLayoutParams(marginParams);
        view.requestLayout();
        return marginParams;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        final float scale = FeihuZhiboApplication.getApplication().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
