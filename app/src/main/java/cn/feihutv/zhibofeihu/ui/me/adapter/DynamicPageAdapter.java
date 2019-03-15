package cn.feihutv.zhibofeihu.ui.me.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.me.DynamicItem;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LikeFeedRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LikeFeedResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.PhotoInfo;
import cn.feihutv.zhibofeihu.ui.widget.ImagePagerActivity;
import cn.feihutv.zhibofeihu.ui.widget.MultiImageView;
import cn.feihutv.zhibofeihu.ui.widget.recyclerloadmore.RecyclerLoadMoreAdapater;
import cn.feihutv.zhibofeihu.utils.TimeUtil;
import cn.feihutv.zhibofeihu.utils.glideutil.GlideApp;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Administrator on 2017/2/22.
 */

public class DynamicPageAdapter extends RecyclerLoadMoreAdapater {

    public interface OnItemClickListener {
        void share(int position);

        void goDetail(int position, int feedType);
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    private Context mContext;
    private List<DynamicItem> datas;

    public DynamicPageAdapter(Context context, List<DynamicItem> objects) {
        mContext = context;
        datas = objects;
    }

    public void setDatas(List<DynamicItem> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolderSuper(RecyclerView.ViewHolder holder, final int position) {
        final DynamicItem info = datas.get(position);
        List<PhotoInfo> photos = info.getPhotos();
        switch (getItemViewTypeSuper(position)) {
            case 1:
                final DynamicItemViewHolder dynamicItemViewHolder = (DynamicItemViewHolder) holder;
                GlideApp.loadImgtransform(mContext, info.getHeadurl(),
                        R.drawable.face, dynamicItemViewHolder.headPic);
                dynamicItemViewHolder.userName.setText(info.getNickname());
                if (!TextUtils.isEmpty(info.getLocation())) {
                    dynamicItemViewHolder.position.setText(info.getLocation());
                    dynamicItemViewHolder.locationView.setVisibility(View.VISIBLE);
                } else {
                    dynamicItemViewHolder.locationView.setVisibility(View.GONE);
                }
                dynamicItemViewHolder.sendTime.setText(TimeUtil.converFeedTime(info.getPublishtime()));
                if (android.text.TextUtils.isEmpty(info.getContent())) {
                    dynamicItemViewHolder.dynamicContent.setVisibility(View.GONE);
                } else {
                    dynamicItemViewHolder.dynamicContent.setVisibility(View.VISIBLE);
                    dynamicItemViewHolder.dynamicContent.setText(info.getContent());
                }

                if (info.isLiked()) {
                    dynamicItemViewHolder.zanImg.setImageResource(R.drawable.praise);
                } else {
                    dynamicItemViewHolder.zanImg.setImageResource(R.drawable.zan);
                }


                if (photos != null && photos.size() > 0) {
                    dynamicItemViewHolder.multiImageView.setVisibility(View.VISIBLE);
                    dynamicItemViewHolder.multiImageView.setList(photos);
                    dynamicItemViewHolder.multiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            //imagesize是作为loading时的图片size
                            ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());

                            ImagePagerActivity.startImagePagerActivity(mContext,
                                    info.getImgList(), position, imageSize);

                        }
                    });
                } else {
                    dynamicItemViewHolder.multiImageView.setVisibility(View.GONE);
                }

                dynamicItemViewHolder.shareCount.setText(info.getForwarding() + "");
                dynamicItemViewHolder.zanCount.setText(info.getLikes() + "");
                dynamicItemViewHolder.commentCount.setText(info.getComments() + "");

                dynamicItemViewHolder.commentImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.goDetail(position, info.getFeedType());
                        }
                    }
                });
                dynamicItemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mListener != null) {
                            mListener.goDetail(position, info.getFeedType());
                        }
                    }
                });
                dynamicItemViewHolder.commentCount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.goDetail(position, info.getFeedType());
                        }
                    }
                });
                dynamicItemViewHolder.shareCount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.share(position);
                        }
                    }
                });
                dynamicItemViewHolder.shareImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.share(position);
                        }
                    }
                });
                dynamicItemViewHolder.zanImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        if (datas.get(position).isLiked()) {
                        } else {
                            //用户点赞
                            FeihuZhiboApplication.getApplication().mDataManager
                                    .doLikeFeedApiCall(new LikeFeedRequest(datas.get(position).getId()))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<LikeFeedResponse>() {
                                        @Override
                                        public void accept(@NonNull LikeFeedResponse response) throws Exception {
                                            if (response.getCode() == 0) {
                                                datas.get(position).setLiked(true);
                                                int likes = datas.get(position).getLikes();
                                                datas.get(position).setLikes(likes + 1);
                                                ImageView imageView = (ImageView) v;
                                                imageView.setImageResource(R.drawable.praise);
                                                notifyDataSetChanged();
                                            }
                                        }
                                    }, new Consumer<Throwable>() {
                                        @Override
                                        public void accept(@NonNull Throwable throwable) throws Exception {
                                            throwable.printStackTrace();
                                        }
                                    });

                        }
                    }
                });
                break;
            case 2:
                final DynamicItemMoreViewHolder dynamicItemMoreViewHolder = (DynamicItemMoreViewHolder) holder;
                GlideApp.loadImgtransform(mContext, info.getHeadurl(),
                        R.drawable.face, dynamicItemMoreViewHolder.headPic);
                dynamicItemMoreViewHolder.userName.setText(info.getNickname());
                if (!TextUtils.isEmpty(info.getLocation())) {
                    dynamicItemMoreViewHolder.position.setText(info.getLocation());
                    dynamicItemMoreViewHolder.locationView.setVisibility(View.VISIBLE);
                } else {
                    dynamicItemMoreViewHolder.locationView.setVisibility(View.GONE);
                }
                dynamicItemMoreViewHolder.sendTime.setText(TimeUtil.converFeedTime(info.getPublishtime()));
                if (android.text.TextUtils.isEmpty(info.getContent())) {
                    dynamicItemMoreViewHolder.dynamicContent.setVisibility(View.GONE);
                } else {
                    dynamicItemMoreViewHolder.dynamicContent.setVisibility(View.VISIBLE);
                    dynamicItemMoreViewHolder.dynamicContent.setText(info.getContent());
                }

                if (info.isLiked()) {
                    dynamicItemMoreViewHolder.zanImg.setImageResource(R.drawable.praise);
                } else {
                    dynamicItemMoreViewHolder.zanImg.setImageResource(R.drawable.zan);
                }

                GlideApp.loadImg(mContext, photos.get(0).getUrl(), R.drawable.bg, dynamicItemMoreViewHolder.mImageView);

                dynamicItemMoreViewHolder.shareCount.setText(info.getForwarding() + "");
                dynamicItemMoreViewHolder.zanCount.setText(info.getLikes() + "");
                dynamicItemMoreViewHolder.commentCount.setText(info.getComments() + "");

                dynamicItemMoreViewHolder.dynamicContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.goDetail(position, info.getFeedType());
                        }
                    }
                });
                dynamicItemMoreViewHolder.commentImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.goDetail(position, info.getFeedType());
                        }
                    }
                });
                dynamicItemMoreViewHolder.commentCount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.goDetail(position, info.getFeedType());
                        }
                    }
                });
                dynamicItemMoreViewHolder.shareCount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.share(position);
                        }
                    }
                });
                dynamicItemMoreViewHolder.shareImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.share(position);
                        }
                    }
                });
                dynamicItemMoreViewHolder.zanImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        if (datas.get(position).isLiked()) {
                        } else {
                            //用户点赞
                            FeihuZhiboApplication.getApplication().mDataManager
                                    .doLikeFeedApiCall(new LikeFeedRequest(datas.get(position).getId()))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<LikeFeedResponse>() {
                                        @Override
                                        public void accept(@NonNull LikeFeedResponse response) throws Exception {
                                            if (response.getCode() == 0) {
                                                datas.get(position).setLiked(true);
                                                int likes = datas.get(position).getLikes();
                                                datas.get(position).setLikes(likes + 1);
                                                ImageView imageView = (ImageView) v;
                                                imageView.setImageResource(R.drawable.praise);
                                                notifyDataSetChanged();
                                            }
                                        }
                                    }, new Consumer<Throwable>() {
                                        @Override
                                        public void accept(@NonNull Throwable throwable) throws Exception {
                                            throwable.printStackTrace();
                                        }
                                    });

                        }
                    }
                });

                dynamicItemMoreViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mListener != null) {
                            mListener.goDetail(position, info.getFeedType());
                        }
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolderSuper(ViewGroup viewGroup, int viewType) {
        if (viewType == 2) {
            return (new DynamicItemMoreViewHolder(getView(viewGroup, R.layout.dynamic_item_more_view)));
        } else {
            return (new DynamicItemViewHolder(getView(viewGroup, R.layout.dynamic_item_view)));
        }

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    private View getView(ViewGroup parent, int layoutId) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
    }


    @Override
    public int getItemViewTypeSuper(int position) {
        // 1,//普通动态  MV动态2
        if (datas.get(position).getFeedType() == 1) {
            return 1;
        } else if (datas.get(position).getFeedType() == 2) {
            return 2;
        } else {
            return super.getItemViewTypeSuper(position);
        }
    }

    static class DynamicItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.head_pic)
        ImageView headPic;
        @BindView(R.id.user_name)
        TextView userName;
        @BindView(R.id.position)
        TextView position;
        @BindView(R.id.dynamic_content)
        TextView dynamicContent;
        @BindView(R.id.multiImagView)
        MultiImageView multiImageView;
        @BindView(R.id.share_img)
        ImageView shareImg;
        @BindView(R.id.share_count)
        TextView shareCount;
        @BindView(R.id.send_time)
        TextView sendTime;
        @BindView(R.id.comment_img)
        ImageView commentImg;
        @BindView(R.id.comment_count)
        TextView commentCount;
        @BindView(R.id.zan_img)
        ImageView zanImg;
        @BindView(R.id.zan_count)
        TextView zanCount;
        @BindView(R.id.topPanel)
        RelativeLayout topPanpel;
        @BindView(R.id.location_view)
        LinearLayout locationView;

        public DynamicItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class DynamicItemMoreViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.head_pic)
        ImageView headPic;

        @BindView(R.id.user_name)
        TextView userName;

        @BindView(R.id.position)
        TextView position;

        @BindView(R.id.dynamic_content)
        TextView dynamicContent;

        @BindView(R.id.iv_more_mv)
        ImageView mImageView;

        @BindView(R.id.share_img)
        ImageView shareImg;

        @BindView(R.id.share_count)
        TextView shareCount;

        @BindView(R.id.send_time)
        TextView sendTime;

        @BindView(R.id.comment_img)
        ImageView commentImg;

        @BindView(R.id.comment_count)
        TextView commentCount;

        @BindView(R.id.zan_img)
        ImageView zanImg;

        @BindView(R.id.zan_count)
        TextView zanCount;

        @BindView(R.id.location_view)
        LinearLayout locationView;

        public DynamicItemMoreViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
