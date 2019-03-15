package cn.feihutv.zhibofeihu.ui.mv.demand.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMyNeedMVListResponse;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.feihutv.zhibofeihu.utils.glideutil.GlideApp;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/10/24 17:08
 *      desc   : mv适配器
 *      version: 1.0
 * </pre>
 */




    public class MvDemandMvListAdapter extends RecyclerView.Adapter<MvDemandMvListAdapter.ViewHolder> {


        private Context mContext;


         int mvType=5;

        public void setClickCallBack(ItemClickCallBack clickCallBack) {
            this.clickCallBack = clickCallBack;
        }

        public interface ItemClickCallBack{
            void onItemClick(View v,int position);
        }

        public List<GetMyNeedMVListResponse.GetMyNeedMVList> datas = null;
        private ItemClickCallBack clickCallBack;

        public MvDemandMvListAdapter(Context mContext,int mvType,
                                     List<GetMyNeedMVListResponse.GetMyNeedMVList> datas) {
            this.mvType=mvType;
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

            viewHolder. driver.setVisibility(View.GONE);
            viewHolder. item_tool.setVisibility(View.GONE);

            GetMyNeedMVListResponse.GetMyNeedMVList item=  datas.get(position);

            TCUtils.showPicWithUrl(mContext, (ImageView) viewHolder.mv_user_head,
                    item.getAvatar(), R.drawable.face);

            GlideApp.loadImg(mContext,item.getCover(),R.drawable.bg
                    ,(ImageView) viewHolder.item_mv_image);

            viewHolder.mv_user_name.setText(item.getNickname());
            viewHolder.mv_title_song.setText(item.getSongName()+"-"+item.getSingerName());
            viewHolder.mv_title_desc.setText(item.getRequire()+"");

            if(mvType==5) {
                viewHolder.mv_state.setImageResource(R.drawable.icon_mv_bargain);
            }else if(mvType==3){
                viewHolder.mv_state.setImageResource(R.drawable.icon_mv_accomplish);
            }else{
                viewHolder.mv_state.setVisibility(View.GONE);
            }
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
            public View rl_userInfo,driver,item_tool;



            public ViewHolder(View view){
                super(view);
                mv_user_head=view.findViewById(R.id.mv_user_head);
                item_mv_image=view.findViewById(R.id.item_mv_image);
                mv_user_name=view.findViewById(R.id.mv_user_name);
                mv_title_song=view.findViewById(R.id.mv_title_song);
                mv_title_desc=view.findViewById(R.id.mv_title_desc);
                mv_state=view.findViewById(R.id.mv_state);
                driver=view.findViewById(R.id.driver);
                item_tool=view.findViewById(R.id.item_tool);
                rl_userInfo=view.findViewById(R.id.rl_userInfo);
            }




        }








    }
