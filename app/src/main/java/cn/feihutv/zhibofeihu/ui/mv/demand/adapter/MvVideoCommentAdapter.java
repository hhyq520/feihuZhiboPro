package cn.feihutv.zhibofeihu.ui.mv.demand.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMVCommentListResponse;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.feihutv.zhibofeihu.utils.TimeUtil;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/11/23
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class MvVideoCommentAdapter extends RecyclerView.Adapter<MvVideoCommentAdapter.ViewHolder> {

    private Context mContext;

    public void setClickCallBack(ItemClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

    public interface ItemClickCallBack {
        void onItemClick(int pos);

        void onUser_nameClick(int pos);

        void onCommentClick(int pos);

        void onUserHeadClick(int pos);
    }

    public List<GetMVCommentListResponse.GetMVCommentList> datas = null;
    private ItemClickCallBack clickCallBack;

    public MvVideoCommentAdapter(Context mContext, List<GetMVCommentListResponse.GetMVCommentList> datas) {
        this.datas = datas;
        this.mContext = mContext;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_layout_mv_comment_list, viewGroup, false);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        GetMVCommentListResponse.GetMVCommentList mvCommentList = datas.get(position);

        TCUtils.showPicWithUrl(mContext, (ImageView) viewHolder.mv_user_head,
                mvCommentList.getAvatar(), R.drawable.face);

        viewHolder.mv_user_name.setText(mvCommentList.getNickname());

        if (mvCommentList.getLikes() > 0) {
            viewHolder.like_num.setText(mvCommentList.getLikes() + "");
            viewHolder.like_img.setImageResource(R.drawable.mv_like_s);
        } else {
            viewHolder.like_num.setText("");
            viewHolder.like_img.setImageResource(R.drawable.mv_like_n);
        }

        viewHolder.mv_time.setText(TimeUtil.converFeedTime(mvCommentList.getT()));

        if (!"0".equals(mvCommentList.getReplyUid()) && !mvCommentList.getReplyUid().equals(mvCommentList.getUid())) {
            String replystring = "@" + mvCommentList.getReplyNickname() + ":";
            String huifu = "回复";
            SpannableString spanString = new SpannableString(huifu + replystring + mvCommentList.getContent());
            spanString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.btn_sure_forbidden)),
                    huifu.length(), replystring.length() + huifu.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            viewHolder.mv_comment.setText(spanString);
        } else {
            viewHolder.mv_comment.setText(mvCommentList.getContent());
        }

        viewHolder.rl_userInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickCallBack != null) {
                    clickCallBack.onUser_nameClick(position);
                }
            }
        });
        viewHolder.ll_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点赞
                if (clickCallBack != null) {
                    clickCallBack.onCommentClick(position);
                }

            }
        });
        viewHolder.mv_user_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickCallBack != null) {
                    clickCallBack.onUserHeadClick(position);
                }
            }
        });

    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mv_user_name, like_num, mv_time, mv_comment;
        ImageView mv_user_head, like_img;

        View ll_comment, rl_userInfo;


        public ViewHolder(View view) {
            super(view);
            mv_user_name = view.findViewById(R.id.mv_user_name);
            like_num = view.findViewById(R.id.like_num);
            mv_time = view.findViewById(R.id.mv_time);
            mv_comment = view.findViewById(R.id.mv_comment);
            mv_user_head = view.findViewById(R.id.mv_user_head);
            like_img = view.findViewById(R.id.like_img);
            rl_userInfo = view.findViewById(R.id.rl_userInfo);
            ll_comment = view.findViewById(R.id.ll_comment);
        }
    }


}