package cn.feihutv.zhibofeihu.ui.me.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.db.model.NoticeSnsEntity;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.feihutv.zhibofeihu.utils.TimeUtil;
import cn.feihutv.zhibofeihu.utils.glideutil.GlideApp;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/21 09:49
 *      desc   : 动态消息
 *      version: 1.0
 * </pre>
 */
public class NoticeSnsAdapter extends BaseQuickAdapter<NoticeSnsEntity, BaseViewHolder> {

    public NoticeSnsAdapter(@Nullable List<NoticeSnsEntity> data) {
        super(R.layout.item_notice_sns, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NoticeSnsEntity item) {
        TCUtils.showPicWithUrl(mContext, (ImageView) helper.getView(R.id.item_img_head), item.getHeadUrl(), R.drawable.face);
        helper.setText(R.id.tv_nick, item.getNickName());
        helper.setText(R.id.item_notice_sms_time, TimeUtil.getChatTime(false, item.getTime() * 1000));
        if (item.getFeedMsgType() == 1) {
            // 点赞
            helper.setVisible(R.id.iv_like, true);
            helper.setVisible(R.id.tv_replycontent, false);
        } else if (item.getFeedMsgType() == 2) {
            // 评论
            helper.setVisible(R.id.iv_like, false);
            helper.setVisible(R.id.tv_replycontent, true);
            helper.setText(R.id.tv_replycontent, item.getReplyContent());
        } else if (item.getFeedMsgType() == 3) {
            // 转发
        } else {

        }

        if (item.getFeedType() == 1) {
            // 普通动态
            if (TextUtils.isEmpty(item.getImgUrl())) {
                // 无图时
                helper.setVisible(R.id.iv_play, false);
                helper.setVisible(R.id.img_content, false);
                helper.setVisible(R.id.tv_content, true);
                helper.setText(R.id.tv_content, item.getFeedContent());
            } else {
                // 有图
                GlideApp.loadImg(mContext, item.getImgUrl(), R.drawable.bg, (ImageView) helper.getView(R.id.img_content));
                helper.setVisible(R.id.iv_play, false);
                helper.setVisible(R.id.img_content, true);
                helper.setVisible(R.id.tv_content, false);
            }
        } else {
            // MV
            helper.setVisible(R.id.iv_play, true);
            helper.setVisible(R.id.img_content, true);
            helper.setVisible(R.id.tv_content, false);
            GlideApp.loadImg(mContext, item.getImgUrl(), R.drawable.bg, (ImageView) helper.getView(R.id.img_content));
        }
    }
//    public void onBindViewHolder(MyViewHolder holder, final int position) {
//        Glide.with(context)
//            .load(datas.get(position).getImgUrl())
//            .crossFade()
//            .thumbnail(0.1f)
//            .diskCacheStrategy(DiskCacheStrategy.ALL)
//            .into(holder.item_img_content);
//    }
}

