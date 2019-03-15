package cn.feihutv.zhibofeihu.ui.me.mv.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMyMVListResponse;
import cn.feihutv.zhibofeihu.utils.CommonUtils;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.feihutv.zhibofeihu.utils.TimeUtil;
import cn.feihutv.zhibofeihu.utils.glideutil.GlideApp;


/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/10/24 17:08
 *      desc   : mv适配器
 *      version: 1.0
 * </pre>
 */

    public class MyMvProductionAdapter extends RecyclerView.Adapter<MyMvProductionAdapter.ViewHolder> {


        private Context mContext;

        public void setClickCallBack(ItemClickCallBack clickCallBack) {
            this.clickCallBack = clickCallBack;
        }

        public interface ItemClickCallBack{
            void onItemClick(View v, int position);
        }


        public List<GetMyMVListResponse.GetMyMVList> datas = null;
        private ItemClickCallBack clickCallBack;

        public MyMvProductionAdapter(Context mContext, List<GetMyMVListResponse.GetMyMVList> datas) {
            this.datas = datas;
            this.mContext=mContext;
        }


        //创建新View，被LayoutManager所调用
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_layout_mv_mylist,viewGroup,false);
            return new ViewHolder(view);
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }



        //将数据与界面进行绑定的操作
        @Override
        public void onBindViewHolder(ViewHolder viewHolder,final int position) {

            GetMyMVListResponse.GetMyMVList item=  datas.get(position);

            TCUtils.showPicWithUrl(mContext, (ImageView) viewHolder.mv_user_head,
                    item.getAvatar(), R.drawable.face);

            GlideApp.loadImg(mContext,item.getCover(),R.drawable.bg
                    ,(ImageView) viewHolder.item_mv_image);

            viewHolder.mv_user_name.setText(item.getNickname());
            if(TextUtils.isEmpty(item.getSongName())){
            }
            viewHolder.mv_title_song.setText(item.getSongName()+"  "+item.getSingerName());
            viewHolder.mv_title_desc.setText(item.getRequire()+"");
            viewHolder.mv_zan.setVisibility(View.GONE);
            viewHolder.mv_comment.setVisibility(View.GONE);
            viewHolder.mv_gift.setVisibility(View.GONE);
            switch (item.getStatus()){//MV状态 0全部  1已过期 2审核失败 3已完成 4待审核 5待成交 6待修改
                case 0:
                    viewHolder.mv_state.setVisibility(View.GONE);
                    break;
                case 1:
                    viewHolder.mv_state.setImageResource(R.drawable.icon_mv_due);
                    break;
                case 2:
                    viewHolder.mv_state.setImageResource(R.drawable.icon_mv_defeated);
                    break;
                case 3:
                    //3已完成
                    viewHolder.mv_zan.setVisibility(View.VISIBLE);
                    viewHolder.mv_comment.setVisibility(View.VISIBLE);
                    viewHolder.mv_gift.setVisibility(View.VISIBLE);
                    viewHolder.mv_zan.setText(CommonUtils.getCoinText(item.getLikes())+"");
                    viewHolder.mv_comment.setText(CommonUtils.getCoinText(item.getComments())+"");
                    viewHolder.mv_gift.setText(CommonUtils.getCoinText(item.getGiftIncome())+"");

                    viewHolder.mv_state.setImageResource(R.drawable.icon_mv_accomplish);
                    break;
                case 4:
                    viewHolder.mv_state.setImageResource(R.drawable.icon_mv_audit);
                    break;
                case 5:
                    viewHolder.mv_state.setImageResource(R.drawable.icon_mv_bargain);
                    break;
                case 6:
                    viewHolder.mv_state.setImageResource(R.drawable.icon_mv_amend);
                    break;
            }

            viewHolder.mv_item_time.setText(TimeUtil.getMVTimeYYmmdd(item.getT())+"");


            viewHolder.rl_userInfo.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(clickCallBack != null){
                                clickCallBack.onItemClick(v,position);
                            }
                        }
                    }
            );

        }




        //获取数据的数量
        @Override
        public int getItemCount() {
            return datas.size();
        }
        //自定义的ViewHolder，持有每个Item的的所有界面元素
        public static class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView mv_user_head,item_mv_image,mv_state;
            public TextView mv_user_name;
            public TextView   mv_title_song;
            public TextView   mv_title_desc;
            public View rl_userInfo;

            public TextView mv_item_time;
            public TextView mv_zan;
            public TextView mv_comment;
            public TextView mv_gift;

            public ViewHolder(View view){
                super(view);
                mv_user_head=view.findViewById(R.id.mv_user_head);
                item_mv_image=view.findViewById(R.id.item_mv_image);
                mv_user_name=view.findViewById(R.id.mv_user_name);
                mv_title_song=view.findViewById(R.id.mv_title_song);
                mv_title_desc=view.findViewById(R.id.mv_title_desc);
                rl_userInfo=view.findViewById(R.id.rl_userInfo);
                mv_state=view.findViewById(R.id.mv_state);
                mv_item_time=view.findViewById(R.id.mv_item_time);
                mv_zan=view.findViewById(R.id.mv_zan);
                mv_comment=view.findViewById(R.id.mv_comment);
                mv_gift=view.findViewById(R.id.mv_gift);
            }




        }








    }
