package cn.feihutv.zhibofeihu.ui.live.adapter;

/**
 * Created by Administrator on 2017/3/3.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.LinkedList;
import java.util.List;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomContriResponce;
import cn.feihutv.zhibofeihu.ui.live.OnStrClickListener;
import cn.feihutv.zhibofeihu.utils.TCUtils;

/**
 * Created by clw on 2016/8/21.
 * 直播头像列表Adapter
 */
public class TCUserContriListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<LoadRoomContriResponce.RoomContriData> mUserAvatarList;
    Context mContext;
    //主播id
    private String mPusherId;
    //最大容纳量
    private final static int TOP_STORGE_MEMBER = 20;
    private OnStrClickListener mOnItemClickListener;


    public TCUserContriListAdapter(Context context, String pusherId) {
        this.mContext = context;
        this.mPusherId = pusherId;
        this.mUserAvatarList = new LinkedList<>();
    }

    public void setDatas(List<LoadRoomContriResponce.RoomContriData> infos){
        this.mUserAvatarList=infos;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnStrClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
            .inflate(R.layout.item_user_avatar, parent, false);
        AvatarViewHolder avatarViewHolder = new AvatarViewHolder(view);
        return avatarViewHolder;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        AvatarViewHolder avatarViewHolder=(AvatarViewHolder)holder;
        if(!mUserAvatarList.get(position).getHeadUrl().equals(((AvatarViewHolder)holder).ivAvatar.getTag(R.id.tag_first))){
            TCUtils.showPicWithUrlfade(mContext, ((AvatarViewHolder)holder).ivAvatar,mUserAvatarList.get(position).getHeadUrl(), R.drawable.face);
            ((AvatarViewHolder)holder).ivAvatar.setTag(R.id.tag_first,mUserAvatarList.get(position).getHeadUrl());
        }else{

        }
//        TCUtils.showPicWithUrlfade(mContext, ((AvatarViewHolder)holder).ivAvatar,mUserAvatarList.get(position).getHeadUrl(), R.drawable.face);
        avatarViewHolder.ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(mUserAvatarList.get(position).getUserId());
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mUserAvatarList != null? mUserAvatarList.size(): 0;
    }

    static class AvatarViewHolder extends RecyclerView.ViewHolder{

        ImageView ivAvatar;


        public AvatarViewHolder(View itemView) {
            super(itemView);
            ivAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
        }
    }
}
