package cn.feihutv.zhibofeihu.ui.me.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.BitmapRequestListener;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.me.DynamicItem;
import cn.feihutv.zhibofeihu.ui.widget.MergePictureView;
import cn.feihutv.zhibofeihu.ui.widget.recyclerloadmore.RecyclerLoadMoreAdapater;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.TimeUtil;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/11/09
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class MyDynamicPageAdapter extends RecyclerLoadMoreAdapater {


    private List<DynamicItem> mDynamicItemList;
    private Context mContext;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void goDetail(int position, int feedType);
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolderSuper(ViewGroup viewGroup, int viewType) {

        if (viewType == 2) {
            return (new DynamicMoreItemViewHolder(getView(viewGroup, R.layout.dynamic_my_more_view)));
        } else {
            return (new DynamicItemViewHolder(getView(viewGroup, R.layout.dynamic_my_item_view)));
        }
    }


    private View getView(ViewGroup parent, int layoutId) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
    }


    public MyDynamicPageAdapter(Context context, List<DynamicItem> mDynamicItemList) {
        this.mDynamicItemList = mDynamicItemList;
        this.mContext = context;
    }

    @Override
    public List getDataList() {
        return mDynamicItemList;
    }

    @Override
    public void addAll(@NonNull List data) {
        if (mDynamicItemList != null) {
            mDynamicItemList.addAll(data);
        }
    }

    @Override
    public int getItemCount() {
        return mDynamicItemList.size();
    }


    @Override
    public void onBindViewHolderSuper(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolderSuper(holder, position);
        // 普通
        final DynamicItem info = mDynamicItemList.get(position);
        switch (getItemViewTypeSuper(position)) {
            case 1:
                final DynamicItemViewHolder dynamicItemViewHolder = (DynamicItemViewHolder) holder;

                //设置普通动态
                dynamicItemViewHolder.rl_dy_layout.setVisibility(View.VISIBLE);
                dynamicItemViewHolder.day.setText(TimeUtil.getDay(info.getPublishtime()));
                dynamicItemViewHolder.month.setText(TimeUtil.getMonth(info.getPublishtime()) + "月");
                dynamicItemViewHolder.address.setText(info.getLocation());
                dynamicItemViewHolder.tv_dy_text.setText(info.getContent());
                if (info.getImgList().size() > 0) {
                    getPhotoList(info.getImgList(), dynamicItemViewHolder.mp_dy_mp_view);
                    int pSize = info.getImgList().size();
                    dynamicItemViewHolder.tv_dy_photo_number.setText("共 " + pSize + " 张");
                    dynamicItemViewHolder.fl_dy_photo_view.setVisibility(View.VISIBLE);
                    dynamicItemViewHolder.mp_dy_mp_view.setVisibility(View.VISIBLE);
                    dynamicItemViewHolder.tv_dy_photo_number.setVisibility(View.VISIBLE);
                } else {
                    dynamicItemViewHolder.fl_dy_photo_view.setVisibility(View.GONE);
                    dynamicItemViewHolder.mp_dy_mp_view.setVisibility(View.GONE);
                    dynamicItemViewHolder.tv_dy_photo_number.setVisibility(View.GONE);
                }
                dynamicItemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.goDetail(position, 1);
                        }
                    }
                });
                break;
            case 2:
                // 动态
                DynamicMoreItemViewHolder dynamicMoreItemViewHolder = (DynamicMoreItemViewHolder) holder;
                dynamicMoreItemViewHolder.day.setText(TimeUtil.getDay(info.getPublishtime()));
                dynamicMoreItemViewHolder.month.setText(TimeUtil.getMonth(info.getPublishtime()) + "月");
                dynamicMoreItemViewHolder.address.setText(info.getLocation());
                Glide.with(mContext).load(info.getImgList().get(0)).into(dynamicMoreItemViewHolder.ic_more_mv);
                if (info.getContent() != null) {
                    dynamicMoreItemViewHolder.tv_dy_text_more.setText(info.getContent());
                }

                dynamicMoreItemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.goDetail(position, 2);
                        }
                    }
                });
                break;
            default:
                break;
        }


    }


    private void getPhotoList(final List<String> photoList, final MergePictureView mp_dy_mp_view) {

        //网络获取集合图片生成bitmap 对象
        final List<Bitmap> mPhotoBitmap = new ArrayList<Bitmap>();
        AppLogger.i(" start get image ...");
        for (int i = 0; i < photoList.size(); i++) {
            String url = photoList.get(i);

            AndroidNetworking.get(url)
                    .setTag("imageRequestTag")
                    .setPriority(Priority.MEDIUM)
//                            .setBitmapMaxHeight(100)
//                            .setBitmapMaxWidth(100)
                    .setBitmapConfig(Bitmap.Config.ARGB_8888)
                    .build()
                    .getAsBitmap(new BitmapRequestListener() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            // do anything with bitmap
                            mPhotoBitmap.add(bitmap);
                            mp_dy_mp_view.setDrawableIds(mPhotoBitmap);
                            AppLogger.i(" 成功获取 图片");
                        }

                        @Override
                        public void onError(ANError error) {
                            // handle error
                        }
                    });
        }


        AppLogger.i("加载拼接图片图片");
//        Observable.create(new ObservableOnSubscribe<List<Bitmap>>() {
//            @Override
//            public void subscribe(ObservableEmitter<List<Bitmap>> observable) throws Exception {
//
//            }
//        })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<List<Bitmap>>() {
//                    @Override
//                    public void accept(@io.reactivex.annotations.NonNull List<Bitmap> bitmaps)
//                            throws Exception {
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable)
//                            throws Exception {
//
//                    }
//                });
    }

    @Override
    public int getItemViewTypeSuper(int position) {
        // 1,//普通动态  MV动态2
        if (mDynamicItemList.get(position).getFeedType() == 1) {
            return 1;
        } else if (mDynamicItemList.get(position).getFeedType() == 2) {
            return 2;
        } else {
            return super.getItemViewTypeSuper(position);
        }
    }

    public void setDatas(List<DynamicItem> datas) {
        this.mDynamicItemList = datas;
        notifyDataSetChanged();
    }


    static class DynamicMoreItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_dy_text_more)
        TextView tv_dy_text_more; // MV 内容

        @BindView(R.id.ic_more_mv)
        ImageView ic_more_mv; // MV 封面

        @BindView(R.id.day)
        TextView day;

        @BindView(R.id.month)
        TextView month;

        @BindView(R.id.address)
        TextView address;

        public DynamicMoreItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class DynamicItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.day)
        TextView day;
        @BindView(R.id.month)
        TextView month;
        @BindView(R.id.address)
        TextView address;
        @BindView(R.id.ll_mv_item_layout)
        LinearLayout ll_mv_item_layout;
        @BindView(R.id.dy_mv_aPhoto)
        ImageView dy_mv_aPhoto;//mv 封面

        @BindView(R.id.dy_mv_title)
        TextView dy_mv_title;//mv 标题
        @BindView(R.id.rl_dy_mv_order_layout)
        RelativeLayout rl_dy_mv_order_layout;

        @BindView(R.id.tv_mv_order_title)
        TextView tv_mv_order_title;


        @BindView(R.id.tv_mv_order_mName)
        TextView tv_mv_order_mName;


        @BindView(R.id.tv_mv_order_desc)
        TextView tv_mv_order_desc;


        @BindView(R.id.rl_dy_layout)
        RelativeLayout rl_dy_layout;

        @BindView(R.id.mp_dy_mp_view)
        MergePictureView mp_dy_mp_view; //普通动态图片 view

        @BindView(R.id.fl_dy_photo_view)
        FrameLayout fl_dy_photo_view;

        @BindView(R.id.tv_dy_text)
        TextView tv_dy_text;

        @BindView(R.id.tv_dy_photo_number)
        TextView tv_dy_photo_number;

        public DynamicItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
