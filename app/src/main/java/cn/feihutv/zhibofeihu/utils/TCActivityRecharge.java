package cn.feihutv.zhibofeihu.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.feihutv.zhibofeihu.R;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/2 21:15
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class TCActivityRecharge extends RelativeLayout {


    private TextView tvMoney;
    private TextView tvNum;
    private LinearLayout linearLayout;
    private onLinstenerTextColor onLinstenerTextColor;

    private String moneyStr;
    private String numStr;

    public void setOnLinstenerTextColor(TCActivityRecharge.onLinstenerTextColor onLinstenerTextColor) {
        this.onLinstenerTextColor = onLinstenerTextColor;
    }

    public TCActivityRecharge(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_recharge, this);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TCActivityRecharge, 0, 0);
        try {
            moneyStr = ta.getString(R.styleable.TCActivityRecharge_rechargeMoney);
            numStr = ta.getString(R.styleable.TCActivityRecharge_rechargeNum);
            setUpView();
        } finally {
            ta.recycle();
        }
    }

    private void setUpView(){
        tvMoney = (TextView)findViewById(R.id.tv_money);
        tvNum = (TextView)findViewById(R.id.tv_num);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        tvMoney.setText(moneyStr);
        tvNum.setText(numStr);
        linearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onLinstenerTextColor != null){
                    onLinstenerTextColor.onSetColor(tvNum, tvMoney);
                }
            }
        });
    }

//    /**
//     * 设置选中消息事件
//     * 返回消息listener
//     */
//    public void setRechargeListener(final onLinstenerTextColor onLinstenerTextColor){
//
//    }

    /**
     * 设置数据
     * @param num
     */
    public void setNumText(String num){
        numStr = num;
        tvNum.setText(num);
    }

    /**
     * 设置价格
     * @param price
     */
    public void setPriceText(String price){
        moneyStr = price;
        tvMoney.setText(price);
    }

    public interface onLinstenerTextColor{
        void onSetColor(TextView tvNum, TextView tvMoney);
    }
    public void setTextColor(){
        tvMoney.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
        tvNum.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
    }
}
