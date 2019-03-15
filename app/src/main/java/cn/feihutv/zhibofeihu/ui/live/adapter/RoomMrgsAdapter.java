package cn.feihutv.zhibofeihu.ui.live.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.GetRoomMrgsResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.LoadOtherUserInfoResponce;
import cn.feihutv.zhibofeihu.ui.live.OnItemClickListener;
import cn.feihutv.zhibofeihu.ui.live.RoomMrgsCancleClickListener;
import cn.feihutv.zhibofeihu.utils.FileUtils;
import cn.feihutv.zhibofeihu.utils.TCUtils;

/**
 * Created by Administrator on 2017/2/20.
 */

public class RoomMrgsAdapter extends SwipeMenuAdapter<RoomMrgsAdapter.DefaultViewHolder> {

    private List<GetRoomMrgsResponce.RoomMrgData> datas;
    private static Context context;
    private  boolean isLive=false;
    private OnItemClickListener mOnItemClickListener;

    private RoomMrgsCancleClickListener mOnCancleListener;

    public RoomMrgsAdapter(Context context, boolean isLive) {
        this.datas = new ArrayList<>();
        this.context = context;
        this.isLive=isLive;
    }

    public void setmOnCancleListener(RoomMrgsCancleClickListener mOnCancleListener) {
        this.mOnCancleListener = mOnCancleListener;
    }

    public void setDatas(List<GetRoomMrgsResponce.RoomMrgData> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        if(isLive){
            return LayoutInflater.from(parent.getContext()).inflate(R.layout.zhibo_item_roommrgs, parent, false);
        }else {
            return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_roommrgs, parent, false);
        }

    }

    @Override
    public RoomMrgsAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {

        return new RoomMrgsAdapter.DefaultViewHolder(realContentView);
    }

    @Override
    public void onBindViewHolder(RoomMrgsAdapter.DefaultViewHolder holder, int position) {
        holder.setData(datas.get(position));
        holder.setOnItemClickListener(mOnItemClickListener);
        holder.setmOnCancleListener(mOnCancleListener);
    }

    static class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView head_pic,img_level;
        TextView tv_name, tv_cancle;
        OnItemClickListener mOnItemClickListener;
        RoomMrgsCancleClickListener mOnCancleListener;

        public DefaultViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            head_pic = (ImageView) itemView.findViewById(R.id.iv_headPic);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_cancle = (TextView) itemView.findViewById(R.id.tv_cancle);
            img_level=(ImageView)itemView.findViewById(R.id.iv_level);
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
        }

        public void setmOnCancleListener(RoomMrgsCancleClickListener mOnCancleListener) {
            this.mOnCancleListener = mOnCancleListener;
        }

        public void setData(GetRoomMrgsResponce.RoomMrgData entity) {
            TCUtils.showPicWithUrl(context,head_pic,entity.getHeadUrl(),R.drawable.face);
            TCUtils.showLevelWithUrl(context,img_level,entity.getLevel());
            this.tv_name.setText(entity.getNickName());
            this.tv_cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnCancleListener != null) {
                        mOnCancleListener.onCancleClick(getAdapterPosition());
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }
}
