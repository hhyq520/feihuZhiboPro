package cn.feihutv.zhibofeihu.ui.live.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameRoundResultDetailResponce;

/**
 * Created by huanghao on 2017/5/4.
 */

public class WanfaResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<GetGameRoundResultDetailResponce.GetGameRoundResultDetailData> datas;
    private Context mContext;
    private String name;

    public WanfaResultAdapter(Context context, String name) {
        mContext = context;
        datas = new ArrayList<>();
        this.name = name;
    }

    public void setDatas(List<GetGameRoundResultDetailResponce.GetGameRoundResultDetailData> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        WanfaViewHolder wanfaViewHolder = new WanfaViewHolder(getView(parent, R.layout.wanfa_result_adapter));
        return wanfaViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        WanfaViewHolder wanfaViewHolder = (WanfaViewHolder) holder;
        GetGameRoundResultDetailResponce.GetGameRoundResultDetailData model = datas.get(position);
        int opt = model.getOpt();
        if (name.equals("yxjc")) {
            if (opt == 1) {
                wanfaViewHolder.textType.setText("公虎多");
            } else if (opt == 2) {
                wanfaViewHolder.textType.setText("母虎多");
            } else if (opt == 3) {
                wanfaViewHolder.textType.setText("三只一样");
            } else if (opt == 4) {
                wanfaViewHolder.textType.setText("三只不一样");
            }
        } else {
            if (opt == 1) {
                wanfaViewHolder.textType.setText("总和为单");
            } else if (opt == 2) {
                wanfaViewHolder.textType.setText("总和为双");
            } else if (opt == 3) {
                wanfaViewHolder.textType.setText("有对子");
            } else if (opt == 4) {
                wanfaViewHolder.textType.setText("没有对子");
            } else if (opt == 5) {
                wanfaViewHolder.textType.setText("总和末尾为0");
            } else if (opt == 6) {
                wanfaViewHolder.textType.setText("总和末尾不为0");
            }
        }
        if (model.getSide().equals("banker")) {
            wanfaViewHolder.img_banker.setVisibility(View.VISIBLE);
        } else {
            wanfaViewHolder.img_banker.setVisibility(View.GONE);
        }
        if (model.getWinHB() >= 0) {
            if (model.getWinHB() == 0) {
                wanfaViewHolder.imgState.setImageResource(R.drawable.icon_pin);
            } else {
                wanfaViewHolder.imgState.setImageResource(R.drawable.icon_win);
            }
            String winHubi = "";
            if (model.getWinHB() >= 10000) {
                winHubi = new BigDecimal((double) model.getWinHB() / 10000).setScale(2, BigDecimal.ROUND_HALF_DOWN) + "万";
            } else {
                winHubi = model.getWinHB() + "";
            }

            wanfaViewHolder.textHubi.setText("+" + winHubi);
            if (model.getYHB() > 0) {
                String yinHubi = "";
                if (model.getYHB() >= 10000) {
                    yinHubi = new BigDecimal((double) model.getYHB() / 10000).setScale(2, BigDecimal.ROUND_HALF_DOWN) + "万";
                } else {
                    yinHubi = model.getYHB() + "";
                }

                wanfaViewHolder.yhubi_lin.setVisibility(View.VISIBLE);
                wanfaViewHolder.textYhb.setText("+" + yinHubi);
            } else {
                wanfaViewHolder.yhubi_lin.setVisibility(View.INVISIBLE);
            }
        } else {
            String winHubi = "";
            if ((-model.getWinHB()) >= 10000) {
                winHubi = new BigDecimal((double) (-model.getWinHB()) / 10000).setScale(2, BigDecimal.ROUND_HALF_DOWN) + "万";
            } else {
                winHubi = (-model.getWinHB()) + "";
            }
            wanfaViewHolder.textHubi.setText("-" + winHubi);
            wanfaViewHolder.imgState.setImageResource(R.drawable.icon);
            wanfaViewHolder.yhubi_lin.setVisibility(View.INVISIBLE);
        }

    }

    private View getView(ViewGroup parent, int layoutId) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class WanfaViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_type)
        TextView textType;
        @BindView(R.id.img_state)
        ImageView imgState;
        @BindView(R.id.text_hubi)
        TextView textHubi;
        @BindView(R.id.text_yhb)
        TextView textYhb;
        @BindView(R.id.img_banker)
        ImageView img_banker;
        @BindView(R.id.yhubi_lin)
        RelativeLayout yhubi_lin;
        @BindView(R.id.hubi_lin)
        RelativeLayout hubi_lin;

        public WanfaViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
