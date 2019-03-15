package cn.feihutv.zhibofeihu.ui.live.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.ui.widget.ZQImageViewRoundOval;
import cn.feihutv.zhibofeihu.utils.glideutil.GlideApp;

/**
 * Created by huanghao on 2017/7/28.
 */

/**
 * Created by huanghao on 2017/4/25.
 */

public class ShakeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private List<String> filmList;

    public ShakeListAdapter(Context context, List<String> filmList) {
        this.context = context;
        this.filmList = filmList;
    }

    public void setDatas(List<String> filmList){
        this.filmList = filmList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ShakeiewHolder gViewHolder = new ShakeiewHolder(getView(parent, R.layout.ly_imageview));
        return gViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ShakeiewHolder shakeiewHolder=(ShakeiewHolder)holder;
        try {
            shakeiewHolder.image.setType(3);
            shakeiewHolder.image.setRoundRadius(30);
            GlideApp.loadImg(context, filmList.get(position), R.drawable.face, 1000, shakeiewHolder.image);
            shakeiewHolder.tvNum.setText(position+"");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private View getView(ViewGroup parent, int layoutId) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
    }

    @Override
    public int getItemCount() {
        return filmList.size();
    }


    class ShakeiewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ZQImageViewRoundOval image;
        @BindView(R.id.tv_num)
        TextView tvNum;
        public ShakeiewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

