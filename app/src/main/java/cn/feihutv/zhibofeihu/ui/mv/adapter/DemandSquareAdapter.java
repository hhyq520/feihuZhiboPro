package cn.feihutv.zhibofeihu.ui.mv.adapter;

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
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetAllNeedListResponse;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.iwgang.countdownview.CountdownView;

/**
 * <pre>
 *      @author : sichu.chen
 *      time   : 2017/10/24 17:08
 *      desc   : 需求广场适配器
 *      version: 1.0
 * </pre>
 */
public class DemandSquareAdapter extends RecyclerView.Adapter<DemandSquareAdapter.ViewHolder> {


        private Context mContext;

        public void setClickCallBack(ItemClickCallBack clickCallBack) {
            this.clickCallBack = clickCallBack;
        }

        public interface ItemClickCallBack{
            void onItemClick(View v,int position);
        }

        public List<GetAllNeedListResponse.GetAllNeedList> datas = null;
        private ItemClickCallBack clickCallBack;

        public DemandSquareAdapter(Context mContext, List<GetAllNeedListResponse.GetAllNeedList> datas) {
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

            GetAllNeedListResponse.GetAllNeedList item=  datas.get(position);

            TCUtils.showPicWithUrl(mContext, (ImageView) viewHolder.mv_user_head,
                    item.getAvatar(), R.drawable.face);
            viewHolder.iv_mv_collect.setVisibility(View.GONE);
            viewHolder.mv_user_name.setText(item.getNickname()+"  需求");
            viewHolder.mv_title.setText(item.getTitle());
            viewHolder.mv_title_desc.setText(item.getRequire());
            viewHolder.mv_item_coin.setText(item.gethB()+"");

            if(item.getT()<0){
                viewHolder.mv_item_time.setText("已过期");
                viewHolder.cv_countdownView.setVisibility(View.GONE);
            }else{
                viewHolder.cv_countdownView.start(item.getT()*1000);

            }

            String forId=item.getForUid();
            if(TextUtils.isEmpty(forId)){
                forId="0";
            }
            if("0".equals(forId)){
                //指定主播标识，0标识公开
                viewHolder.iv_mv_zd.setVisibility(View.GONE);
            }else{
                String userId=FeihuZhiboApplication.getApplication().mDataManager.getCurrentUserId();
                if(userId.equals(forId)){
                    //如果是指定给我，那我肯定是主播
                    viewHolder.iv_mv_zd.setImageResource(R.drawable.icon_mv_sq_d1);
                }else{
                    //指定给其他主播的需求
                    viewHolder.iv_mv_zd.setImageResource(R.drawable.icon_mv_sq_d2);
                }
                viewHolder.iv_mv_zd.setVisibility(View.VISIBLE);
            }

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


            if(position==(datas.size()-1)){
                //最后一项

            }


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
            public CountdownView cv_countdownView;
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
                cv_countdownView=view.findViewById(R.id.cv_countdownView);
            }




        }




    }
