package cn.feihutv.zhibofeihu.ui.mv.demand.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetNeedCollectListResponse;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.feihutv.zhibofeihu.utils.TimeUtil;

/**
 * <pre>
 *      @author : sichu.chen
 *      time   : 2017/10/24 17:08
 *      desc   : 需求适配器
 *      version: 1.0
 * </pre>
 */

    public class MyMvCollectAdapter extends RecyclerView.Adapter<MyMvCollectAdapter.ViewHolder> {


        private Context mContext;

        public void setClickCallBack(ItemClickCallBack clickCallBack) {
            this.clickCallBack = clickCallBack;
        }

        public interface ItemClickCallBack{
            void onItemClick(View v,int position);

            void onCollectClick(View v,int position);
        }

        public List<GetNeedCollectListResponse.GetNeedCollectList> datas = null;
        private ItemClickCallBack clickCallBack;

        public MyMvCollectAdapter(Context mContext, List<GetNeedCollectListResponse.GetNeedCollectList> datas) {
            this.datas = datas;
            this.mContext=mContext;
        }


        //创建新View，被LayoutManager所调用
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_layout_mv_demand_square,viewGroup,false);
            return new ViewHolder(view);
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }



        //将数据与界面进行绑定的操作
        @Override
        public void onBindViewHolder(ViewHolder viewHolder,final int position) {

            GetNeedCollectListResponse.GetNeedCollectList item=  datas.get(position);


            TCUtils.showPicWithUrl(mContext, (ImageView) viewHolder.mv_user_head,
                    item.getAvatar(), R.drawable.face);
            viewHolder.iv_mv_collect.setVisibility(View.VISIBLE);
            viewHolder.mv_user_name.setText(item.getNickname());
            viewHolder.mv_title.setText(item.getSongName());
            viewHolder.mv_title_desc.setText(item.getRequire());
            viewHolder.mv_item_coin.setText(item.gethB()+"");
            if(item.getT()<0){
                viewHolder.mv_item_time.setText("已过期");
            }else{
                viewHolder.mv_item_time.setText(TimeUtil.getMVTime(item.getT())+"");
            }


            String forId=item.getForUid();
            if(TextUtils.isEmpty(forId)){
                forId="0";
            }
            if("0".equals(forId)){
                //指定主播标识，0标识公开
                viewHolder.iv_mv_zd.setVisibility(View.GONE);
            }else{
                String userId= FeihuZhiboApplication.getApplication().mDataManager.getCurrentUserId();
                if(userId.equals(forId)){
                    //如果是指定给我，那我肯定是主播
                    viewHolder.iv_mv_zd.setImageResource(R.drawable.icon_mv_sq_d1);
                }else{
                    //指定给其他主播的需求
                    viewHolder.iv_mv_zd.setImageResource(R.drawable.icon_mv_sq_d2);
                }
                viewHolder.iv_mv_zd.setVisibility(View.VISIBLE);
            }

            viewHolder.iv_mv_collect.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(clickCallBack != null){
                                clickCallBack.onCollectClick(v,position);
                            }
                        }
                    }
            );
            viewHolder.ll_mv_item_layout.setOnClickListener(
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
            public ImageView mv_user_head;
            public TextView mv_user_name;
            public TextView   mv_title,mv_item_time;
            public TextView mv_title_desc,mv_item_coin;

            public View iv_mv_collect,ll_mv_item_layout;
            public ImageView  iv_mv_zd;

            public ViewHolder(View view){
                super(view);
                mv_user_head=view.findViewById(R.id.mv_user_head);
                mv_user_name=view.findViewById(R.id.mv_user_name);
                mv_item_coin=view.findViewById(R.id.mv_item_coin);
                mv_title_desc=view.findViewById(R.id.mv_title_desc);
                mv_title=view.findViewById(R.id.mv_title);
                mv_item_time=view.findViewById(R.id.mv_item_time);
                iv_mv_collect=view.findViewById(R.id.iv_mv_collect);
                iv_mv_zd=view.findViewById(R.id.iv_mv_zd);
                ll_mv_item_layout=view.findViewById(R.id.ll_mv_item_layout);
            }




        }





    }
