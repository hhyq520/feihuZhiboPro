package cn.feihutv.zhibofeihu.ui.widget.dialog.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameBetHistoryResponce;
import cn.feihutv.zhibofeihu.utils.TimeUtil;

/**
 * Created by huanghao on 2017/6/13.
 */

public class JcHistoryAdapter extends RecyclerView.Adapter<JcHistoryAdapter.JcHViewHolder> {

    private Context context;
    private List<GetGameBetHistoryResponce.BetLogs> datas;
    private int gameType;
    public JcHistoryAdapter(Context context, int gameType) {
        this.context = context;
        datas = new ArrayList<>();
        this.gameType=gameType;
    }

    public void setDatas(List<GetGameBetHistoryResponce.BetLogs> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public JcHViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_jc_history, parent, false);
        return new JcHViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(JcHViewHolder holder, int position) {
        GetGameBetHistoryResponce.BetLogs item=datas.get(position);
        holder.dateTv.setText(TimeUtil.getAllTime((long)(item.getBetTime())*1000));
        int style=item.getOpt();
        if(gameType==1) {
            if (style == 1) {
                holder.title.setText("公虎多");
                holder.bili.setText("1:1");
            } else if (style == 2) {
                holder.title.setText("母虎多");
                holder.bili.setText("1:1");
            } else if (style == 3) {
                holder.title.setText("三只一样");
                holder.bili.setText("1:4");
            } else if (style == 4) {
                holder.title.setText("三只不一样");
                holder.bili.setText("1:0.25");
            }
        }else{
            if (style == 1) {
                holder.title.setText("总和为单");
                holder.bili.setText("1:1");
            } else if (style == 2) {
                holder.title.setText("总和为双");
                holder.bili.setText("1:1");
            } else if (style == 3) {
                holder.title.setText("有对子");
                holder.bili.setText("1:2.5");
            } else if (style == 4) {
                holder.title.setText("没有对子");
                holder.bili.setText("1:0.4");
            }else if (style == 5) {
                holder.title.setText("总和末尾为0");
                holder.bili.setText("1:10");
            }else if (style == 6) {
                holder.title.setText("总和末尾不为0");
                holder.bili.setText("1:0.1");
            }
        }
        int state=item.getStatus();
        if(state==0){
            holder.tvPro.setText("进行中");
        }else {
            holder.tvPro.setText("已结束");
        }
       if(item.getSide().equals("banker")){
           holder.tv_wei_tou.setVisibility(View.VISIBLE);
           holder.tv_not_touzhu.setVisibility(View.VISIBLE);
           holder.frmPlayer.setVisibility(View.GONE);
           holder.frmZhuang.setVisibility(View.VISIBLE);
           holder.imgZhuang.setVisibility(View.VISIBLE);
           holder.imgWin.setVisibility(View.INVISIBLE);
           int wt=item.getTotalBet()-item.getPaired();
           String winwt="";
           if (wt >= 10000) {
               winwt = new BigDecimal((double)wt/ 10000).setScale(2, BigDecimal.ROUND_HALF_DOWN) + "万";
           } else {
               winwt = wt + "";
           }
           if(state==0){
               String getPaired="";
               if (item.getPaired() >= 10000) {
                   getPaired = new BigDecimal((double)item.getPaired()/ 10000).setScale(2, BigDecimal.ROUND_HALF_DOWN) + "万";
               } else {
                   getPaired = item.getPaired() + "";
               }
               holder.tvTz.setText(getPaired);
               holder.tvPro.setText("进行中");
               holder.endHubi.setText("已投");

               holder.tv_not_touzhu.setText(winwt+"");
               holder.endLin.setVisibility(View.GONE);
           }else {
               holder.tvPro.setText("已结束");
               holder.endHubi.setText("虎币");

               if(item.getWinHB()>=0){
                   String winHubi="";
                   if (item.getWinHB() >= 10000) {
                       winHubi = new BigDecimal((double) item.getWinHB() / 10000).setScale(2, BigDecimal.ROUND_HALF_DOWN) + "万";
                   } else {
                       winHubi = item.getWinHB() + "";
                   }
                   if(item.getWinHB()>0){
                   holder.tvTz.setText("+"+winHubi);
                   }else{
                       holder.tvTz.setText("0");
                   }
                   if(item.getYHB()>0) {
                       holder.endLin.setVisibility(View.VISIBLE);
                       holder.end_yin_coin.setVisibility(View.VISIBLE);
                       String yinHubi = "";
                       if (item.getYHB() >= 10000) {
                           yinHubi = new BigDecimal((double) item.getYHB() / 10000).setScale(2, BigDecimal.ROUND_HALF_DOWN) + "万";
                       } else {
                           yinHubi = item.getYHB() + "";
                       }
                       holder.end_yin_coin.setText("+" + yinHubi);
                   }else{
                       holder.endLin.setVisibility(View.GONE);
                   }
               }else{
                   String winHubi="";
                   if ((-item.getWinHB()) >= 10000) {
                       winHubi = new BigDecimal((double) (-item.getWinHB()) / 10000).setScale(2, BigDecimal.ROUND_HALF_DOWN) + "万";
                   } else {
                       winHubi = (-item.getWinHB()) + "";
                   }
                   holder.tvTz.setText("-"+winHubi);
                   holder.endLin.setVisibility(View.GONE);
               }
               holder.tv_not_touzhu.setText(winwt);

           }
       }else{
           holder.endLin.setVisibility(View.GONE);

           if(state==1){
               holder.frmPlayer.setVisibility(View.VISIBLE);
               holder.frmZhuang.setVisibility(View.GONE);
               holder.imgWin.setVisibility(View.VISIBLE);
               holder.imgWin.setImageResource(R.drawable.icon_win);
               holder.yin_coin_lin.setVisibility(View.VISIBLE);

               if(item.getWinHB()<0){
                   String winHubi="";
                   if ((-item.getWinHB()) >= 10000) {
                       winHubi = new BigDecimal((double) (-item.getWinHB()) / 10000).setScale(2, BigDecimal.ROUND_HALF_DOWN) + "万";
                   } else {
                       winHubi = (-item.getWinHB()) + "";
                   }
                   holder.tvHubi.setText("-"+winHubi+"");
                   holder.end_yin_coin.setVisibility(View.VISIBLE);
               }else{
                   String winHubi="";
                   if (item.getWinHB() >= 10000) {
                       winHubi = new BigDecimal((double) item.getWinHB() / 10000).setScale(2, BigDecimal.ROUND_HALF_DOWN) + "万";
                   } else {
                       winHubi = item.getWinHB() + "";
                   }
                   holder.tvHubi.setText("+"+winHubi);
                   holder.end_yin_coin.setVisibility(View.GONE);
               }
               String yinHubi="";
               if (item.getYHB() >= 10000) {
                   yinHubi = new BigDecimal((double) item.getYHB() / 10000).setScale(2, BigDecimal.ROUND_HALF_DOWN) + "万";
               } else {
                   yinHubi = item.getYHB() + "";
               }
               holder.yinCoin.setText("+"+yinHubi);

           }else if(state==2){
               holder.frmPlayer.setVisibility(View.VISIBLE);
               holder.frmZhuang.setVisibility(View.GONE);
               holder.imgWin.setVisibility(View.VISIBLE);
               holder.imgWin.setImageResource(R.drawable.icon);
               holder.yin_coin_lin.setVisibility(View.INVISIBLE);


               if(item.getWinHB()<0){
                   String winHubi="";
                   if ((-item.getWinHB()) >= 10000) {
                       winHubi = new BigDecimal((double) (-item.getWinHB()) / 10000).setScale(2, BigDecimal.ROUND_HALF_DOWN) + "万";
                   } else {
                       winHubi = (-item.getWinHB()) + "";
                   }
                   holder.tvHubi.setText("-"+winHubi+"");
                   holder.end_yin_coin.setVisibility(View.VISIBLE);
               }else{
                   String winHubi="";
                   if (item.getWinHB() >= 10000) {
                       winHubi = new BigDecimal((double) item.getWinHB() / 10000).setScale(2, BigDecimal.ROUND_HALF_DOWN) + "万";
                   } else {
                       winHubi = item.getWinHB() + "";
                   }
                   holder.tvHubi.setText("+"+winHubi);
                   holder.end_yin_coin.setVisibility(View.GONE);
               }
               String yinHubi="";
               if (item.getYHB() >= 10000) {
                   yinHubi = new BigDecimal((double) item.getYHB() / 10000).setScale(2, BigDecimal.ROUND_HALF_DOWN) + "万";
               } else {
                   yinHubi = item.getYHB() + "";
               }
               holder.yinCoin.setText("+"+yinHubi);

           }else {
               holder.frmPlayer.setVisibility(View.GONE);
               holder.frmZhuang.setVisibility(View.VISIBLE);
               String getTotalBet="";
               if (item.getTotalBet() >= 10000) {
                   getTotalBet = new BigDecimal((double) item.getTotalBet() / 10000).setScale(2, BigDecimal.ROUND_HALF_DOWN) + "万";
               } else {
                   getTotalBet =item.getTotalBet() + "";
               }
               holder.tvTz.setText(getTotalBet);
               holder.imgWin.setVisibility(View.INVISIBLE);
               holder.imgZhuang.setVisibility(View.INVISIBLE);
               holder.tv_wei_tou.setVisibility(View.INVISIBLE);
               holder.tv_not_touzhu.setVisibility(View.INVISIBLE);
           }

       }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class JcHViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.date_tv)
        TextView dateTv;
        @BindView(R.id.img_win)
        ImageView imgWin;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.bili)
        TextView bili;
        @BindView(R.id.tv_hubi)
        TextView tvHubi;
        @BindView(R.id.yin_coin)
        TextView yinCoin;
        @BindView(R.id.frm_player)
        FrameLayout frmPlayer;
        @BindView(R.id.tv_touzhu)
        TextView tvTz;
        @BindView(R.id.img_zhuang)
        ImageView imgZhuang;
        @BindView(R.id.frm_zhuang)
        FrameLayout frmZhuang;
        @BindView(R.id.tv_pro)
        TextView tvPro;
        @BindView(R.id.tv_not_touzhu)
        TextView tv_not_touzhu;
        @BindView(R.id.end_hubi)
        TextView endHubi;
        @BindView(R.id.endlin)
        LinearLayout endLin;
        @BindView(R.id.end_yin_coin)
        TextView end_yin_coin;
        @BindView(R.id.tv_wei_tou)
        TextView tv_wei_tou;
        @BindView(R.id.yin_coin_lin)
        LinearLayout yin_coin_lin;
        public JcHViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
