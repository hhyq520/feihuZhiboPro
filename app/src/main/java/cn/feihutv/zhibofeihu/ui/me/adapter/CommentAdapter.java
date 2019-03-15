package cn.feihutv.zhibofeihu.ui.me.adapter;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.me.CommenItem;
import cn.feihutv.zhibofeihu.ui.me.dynamic.CommentListView;
import cn.feihutv.zhibofeihu.ui.widget.recyclerloadmore.RecyclerLoadMoreAdapater;
import cn.feihutv.zhibofeihu.utils.TCGlideCircleTransform;
import cn.feihutv.zhibofeihu.utils.TimeUtil;

/**
 * Created by Administrator on 2017/2/23.
 */

public class CommentAdapter extends RecyclerLoadMoreAdapater {
    public interface ReplyListener {
        void reply(int position);

        void headClick(int position);
    }

    private ReplyListener mListener;

    public void setReplyListener(ReplyListener listener) {
        this.mListener = listener;
    }

    private Context mContext;
    private CommentListView commentListView;
    private List<CommenItem> datas;

    public CommentAdapter(Context context) {
        mContext = context;
        datas = new ArrayList<>();
    }

    public void setDatas(List<CommenItem> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolderSuper(RecyclerView.ViewHolder holder, final int position) {
        CommenItem info = datas.get(position);
        CommentViewHolder commentViewHolder = (CommentViewHolder) holder;
        Glide.with(mContext).load(info.getHeadUrl())//
                .apply(new RequestOptions().placeholder(R.drawable.face)
                        .transform(new TCGlideCircleTransform(mContext))
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(commentViewHolder.icon);
        commentViewHolder.userName.setText(info.getNickName());
        commentViewHolder.commentTime.setText(TimeUtil.converFeedTime(info.getReplyTime()));
        commentViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.reply(position);
                }
            }
        });
        commentViewHolder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.headClick(position);
                }
            }
        });
        if (info.getReplyTo() != null) {
            String replystring = "@" + info.getReplyTo().getNickName() + ":";
            String huifu = "回复";
            SpannableString spanString = new SpannableString(huifu + replystring + info.getContent());
            spanString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.btn_sure_forbidden)),
                    huifu.length(), replystring.length() + huifu.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            commentViewHolder.commentContent.setText(spanString);
        } else {
            commentViewHolder.commentContent.setText(info.getContent());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolderSuper(ViewGroup parent, int viewType) {
        CommentViewHolder commentViewHolder = new CommentViewHolder(getView(parent,
                R.layout.comment_item_view));
        return commentViewHolder;
    }

    public void refresh() {
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    private View getView(ViewGroup parent, int layoutId) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.icon)
        ImageView icon;
        @BindView(R.id.user_name)
        TextView userName;
        @BindView(R.id.comment_content)
        TextView commentContent;
        @BindView(R.id.comment_time)
        TextView commentTime;

        public CommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
