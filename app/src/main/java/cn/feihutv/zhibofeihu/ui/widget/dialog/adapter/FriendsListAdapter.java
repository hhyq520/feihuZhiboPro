package cn.feihutv.zhibofeihu.ui.widget.dialog.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.common.GetFriendsResponce;
import cn.feihutv.zhibofeihu.ui.widget.dialog.OnClickItem;
import cn.feihutv.zhibofeihu.utils.FileUtils;
import cn.feihutv.zhibofeihu.utils.TCUtils;

/**
 * Created by chenliwen on 2017/6/5 16:33.
 * 佛祖保佑，永无BUG
 */

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.MyViewHolder> {

    private Context context;
    private List<GetFriendsResponce.GetFriendsData> datas;
    private OnClickItem onClickItem;

    public void setOnClickItem(OnClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    public FriendsListAdapter(Context context) {
        this.context = context;
        this.datas = new ArrayList<>();
    }

    public void setDatas(List<GetFriendsResponce.GetFriendsData> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_friendslist, parent, false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        TCUtils.showPicWithUrl(context, holder.ivHead, datas.get(position).getHeadUrl(), R.drawable.login_img_head_default);
        holder.tvNickName.setText(datas.get(position).getNickName());
        TCUtils.showLevelWithUrl(context, holder.ivLevel,datas.get(position).getLevel());

        if (datas.get(position).isOnlineStatus()) {

        } else {
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            holder.ivHead.setColorFilter(filter);
        }
        if(datas.get(position).isLiang()){
            holder.ivLang.setVisibility(View.VISIBLE);
        }else {
            holder.ivLang.setVisibility(View.GONE);
        }
        if(datas.get(position).getVip()>0){
            if(datas.get(position).isVipExpired()){
                holder.ivVip.setVisibility(View.GONE);
            }else{
                String cosVipIconRootPath = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosVipIconRootPath();
                Glide.with(context).load(cosVipIconRootPath + "icon_vip" + datas.get(position).getVip() + ".png").into( holder.ivVip);
            }
        }else{
            holder.ivVip.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickItem != null) {
                    onClickItem.onItemClick(datas.get(position).getUserId(), datas.get(position).getHeadUrl(), datas.get(position).getNickName());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_head_friendlist)
        ImageView ivHead;
        @BindView(R.id.iv_vip)
        ImageView ivVip;
        @BindView(R.id.iv_liang)
        ImageView ivLang;
        @BindView(R.id.iv_level_friendlist)
        ImageView ivLevel;
        @BindView(R.id.tv_nickname_friendlist)
        TextView tvNickName;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

}
