package cn.feihutv.zhibofeihu.ui.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.ui.live.OnItemClickListener;

/**
 * Created by huanghao on 2017/6/9.
 */

public class JincaiView {
    @BindView(R.id.tv_name_top)
    TextView tvNameTop;
    @BindView(R.id.tv_name_bottom)
    TextView tvNameBottom;
    @BindView(R.id.peilv_top)
    TextView peilvTop;
    @BindView(R.id.progress_top)
    TextView progressTop;
    @BindView(R.id.up_progress)
    ProgressBar upProgress;
    @BindView(R.id.btn_up)
    Button btnUp;
    @BindView(R.id.peilv_down)
    TextView peilvDown;
    @BindView(R.id.progress_down)
    TextView progressDown;
    @BindView(R.id.down_progress)
    ProgressBar downProgress;
    @BindView(R.id.btn_down)
    Button btnDown;
    private Context context;
    private View rootView;
    private int style;
    private int topValue;
    private int downValue;
    private int sumtop;
    private int sumdown;
    private boolean isPc=false;
    private OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }

    public JincaiView(Context mContext, int style, int topValue, int downValue, int sumtop, int sumdown, boolean isPc,boolean isPcLand) {
        this.context = mContext;
        if(isPcLand){
            rootView = LayoutInflater.from(mContext).inflate(
                    R.layout.jincai_pc_land_view, null);
        }else{
            if(isPc){
                rootView = LayoutInflater.from(mContext).inflate(
                        R.layout.jincai_pc_view, null);
            }else{
                rootView = LayoutInflater.from(mContext).inflate(
                        R.layout.jincai_view, null);
            }
        }


        ButterKnife.bind(this, rootView);
        this.style=style;
        this.topValue=topValue;
        this.downValue=downValue;
        this.sumtop=sumtop;
        this.sumdown=sumdown;
        this.isPc=isPc;
        intit();
    }

    private void intit() {
        switch (style){
            case 1:
                tvNameTop.setText("三只一样");
                tvNameBottom.setText("三只不一样");
                peilvTop.setText("1:4");
                peilvDown.setText("1:0.25");
                break;
            case 2:
                tvNameTop.setText("公虎多");
                tvNameBottom.setText("母虎多");
                peilvTop.setText("1:1");
                peilvDown.setText("1:1");
                peilvDown.setTextSize(12);
                break;
            case 3:
                tvNameTop.setText("总和为单");
                tvNameBottom.setText("总和为双");
                peilvTop.setText("1:1");
                peilvDown.setText("1:1");
                break;
            case 4:
                tvNameTop.setText("有对子");
                tvNameBottom.setText("没有对子");
                peilvTop.setText("1:2.5");
                peilvDown.setText("1:0.4");
                break;
            case 5:
                tvNameTop.setText("总和末尾为0");
                tvNameBottom.setText("总和末尾不为0");
                peilvTop.setText("1:10");
                peilvDown.setText("1:0.1");
                break;
        }
        String topStr="";
        if (topValue >= 10000) {
            topStr = new BigDecimal((double) topValue / 10000).setScale(2, BigDecimal.ROUND_DOWN) + "万";
        } else {
            topStr = topValue + "";
        }
        progressTop.setText(topStr+"");

        String downStr="";
        if (downValue >= 10000) {
            downStr = new BigDecimal((double) downValue / 10000).setScale(2, BigDecimal.ROUND_DOWN) + "万";
        } else {
            downStr = downValue + "";
        }
        progressDown.setText(downStr+"");

        upProgress.setMax(100);
        downProgress.setMax(100);
        if(sumtop!=0){
            upProgress.setProgress(topValue*100/sumtop);
        }else{
            upProgress.setProgress(0);
        }
        if(sumdown!=0){
            downProgress.setProgress(downValue*100/sumdown);
        }else{
            downProgress.setProgress(0);
        }

    }


    public void setButtonEnable(boolean isenble){
        btnUp.setEnabled(isenble);
        btnDown.setEnabled(isenble);
        if(isenble){
            btnUp.setBackgroundResource(R.drawable.yellow_bg);
            btnDown.setBackgroundResource(R.drawable.yellow_bg);
        }else{
            btnUp.setBackgroundResource(R.drawable.gray_shape_100);
            btnDown.setBackgroundResource(R.drawable.gray_shape_100);
        }

    }
    public void setDownButtonEnable(boolean isenble){
        btnDown.setEnabled(isenble);
        if(isenble){
            btnDown.setBackgroundResource(R.drawable.yellow_bg);
        }else{
            btnDown.setBackgroundResource(R.drawable.gray_shape_100);
        }

    }
    public void setUpButtonEnable(boolean isenble){
        btnUp.setEnabled(isenble);
        if(isenble){
            btnUp.setBackgroundResource(R.drawable.yellow_bg);
        }else{
            btnUp.setBackgroundResource(R.drawable.gray_shape_100);
        }

    }

    public void updateData(int topValueN,int downValueN,int sumtopN,int sumdownN){
        String topStr="";
        if (topValueN >= 10000) {
            topStr = new BigDecimal((double) topValueN / 10000).setScale(2, BigDecimal.ROUND_DOWN) + "万";
        } else {
            topStr = topValueN + "";
        }
        progressTop.setText(topStr+"");

        String downStr="";
        if (downValueN >= 10000) {
            downStr = new BigDecimal((double) downValueN / 10000).setScale(2, BigDecimal.ROUND_DOWN) + "万";
        } else {
            downStr = downValueN + "";
        }
        progressDown.setText(downStr+"");
        upProgress.setMax(100);
        downProgress.setMax(100);
        if(sumtopN!=0){
            upProgress.setProgress(topValueN*100/sumtopN);
        }else{
            upProgress.setProgress(0);
        }
        if(sumdownN!=0){
            downProgress.setProgress(downValueN*100/sumdownN);
        }else{
            downProgress.setProgress(0);
        }
    }

    public View getView() {
        return rootView;
    }

    @OnClick({R.id.btn_up, R.id.btn_down})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_up:
                if(mListener!=null){
                    mListener.onItemClick(1);
                }
                break;
            case R.id.btn_down:
                if(mListener!=null){
                    mListener.onItemClick(0);
                }
                break;
        }
    }
}
