package cn.feihutv.zhibofeihu.ui.live.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.LoadRoomMemberResponce;
import cn.feihutv.zhibofeihu.ui.live.OnStrClickListener;
import cn.feihutv.zhibofeihu.utils.FileUtils;
import cn.feihutv.zhibofeihu.utils.TCUtils;


/**
 * Created by clw on 2017/3/10.
 */

public class UserOnlineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnStrClickListener mOnItemClickListener;
    //上拉加载更多
    public static final int PULLUP_LOAD_MORE = 0;
    //正在加载中
    public static final int LOADING_MORE = 1;

    // 没有数据了
    public static final int LOAD_COMPETEED = 2;
    //上拉加载更多状态-默认为0
    private int load_more_status = 0;
    private LayoutInflater mInflater;
    private List<LoadRoomMemberResponce.MemberData> datas;

    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView
    private Context context;

    public UserOnlineAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.datas = new ArrayList<>();
    }

    public void setDatas(List<LoadRoomMemberResponce.MemberData> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    /**
     * item显示类型
     *
     * @param parent
     * @param viewType
     * @return
     */
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TYPE_ITEM) {
            View view = mInflater.inflate(R.layout.item_recycler_lucklove, parent, false);
            //这边可以做一些属性设置，甚至事件监听绑定
            //view.setBackgroundColor(Color.RED);
            ItemViewHolder itemViewHolder = new ItemViewHolder(view);
            return itemViewHolder;
        }
        return null;
    }

    public void setOnItemClickListener(OnStrClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    /**
     * 数据的绑定显示
     *
     * @param holder
     * @param position
     */
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ItemViewHolder) {
            LoadRoomMemberResponce.MemberData onlineUserModel=datas.get(position);
//            ((ItemViewHolder) holder).item_name.setText(datas.get(position).getNickName());
            TCUtils.showPicWithUrl(context,((ItemViewHolder) holder).item_img_head,onlineUserModel.getHeadUrl(),R.drawable.face);
            ((ItemViewHolder) holder).item_name.setText(onlineUserModel.getNickName());
            if(onlineUserModel.isMgr()){
                ((ItemViewHolder) holder).img_changkong.setVisibility(View.VISIBLE);
            }else{
                ((ItemViewHolder) holder).img_changkong.setVisibility(View.INVISIBLE);
            }
            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(datas.get(position).getUserId());
                    }
                }
            });
            TCUtils.showLevelWithUrl(context,((ItemViewHolder) holder).item_img_level,onlineUserModel.getLevel());
        }
    }



    @Override
    public int getItemCount() {
        return datas.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView item_name;
        public ImageView item_img_level, item_img_head;
        public ImageView img_changkong;
        public ItemViewHolder(View view) {
            super(view);
            item_img_level = (ImageView) view.findViewById(R.id.item_img_level);

            item_img_head = (ImageView) view.findViewById(R.id.item_img_head);
            img_changkong = (ImageView) view.findViewById(R.id.tcl_changkong);
            item_name = (TextView) view.findViewById(R.id.item_name);

        }

    }

}

