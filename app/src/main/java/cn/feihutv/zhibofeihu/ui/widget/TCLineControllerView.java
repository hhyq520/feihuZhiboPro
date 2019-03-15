package cn.feihutv.zhibofeihu.ui.widget;

/**
 * Created by Administrator on 2017/2/15.
 */


import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.utils.TCUtils;

/**
 * 设置等页面条状控制或显示信息的控件
 */
public class TCLineControllerView extends LinearLayout {

    private String name;
    private boolean isBottom;
    private String content;
    private boolean canNav;
    private Context mcontext;
    private ImageView navArrow;

    public TCLineControllerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mcontext=context;
        LayoutInflater.from(context).inflate(R.layout.view_line_controller, this);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TCLineView, 0, 0);
        try {
            name = ta.getString(R.styleable.TCLineView_name);
            content = ta.getString(R.styleable.TCLineView_content);
            isBottom = ta.getBoolean(R.styleable.TCLineView_isBottom, false);
            canNav = ta.getBoolean(R.styleable.TCLineView_canNav,false);
            setUpView();
        } finally {
            ta.recycle();
        }
    }


    private void setUpView(){
        TextView tvName = (TextView) findViewById(R.id.ctl_name);
        tvName.setText(name);
        TextView tvContent = (TextView) findViewById(R.id.ctl_content);
        tvContent.setText(TCUtils.getLimitString(content, 20));
        View bottomLine = findViewById(R.id.ctl_bottomLine);
        bottomLine.setVisibility(isBottom ? VISIBLE : GONE);
        navArrow = (ImageView) findViewById(R.id.ctl_rightArrow);
        navArrow.setVisibility(canNav ? VISIBLE : GONE);
        LinearLayout contentPanel = (LinearLayout) findViewById(R.id.ctl_contentText);
    }


    public void hideRight(boolean flag) {
        if (flag) {
            navArrow.setVisibility(GONE);
        } else {
            navArrow.setVisibility(VISIBLE);
        }
    }

    /**
     * 设置文字内容
     *
     * @param content 内容
     */
    public void setContent(String content){
        this.content = content;
        TextView tvContent = (TextView) findViewById(R.id.ctl_content);
        tvContent.setText(TCUtils.getLimitString(content, 20));
    }

    public void setContentColor(int color){
        TextView tvContent = (TextView) findViewById(R.id.ctl_content);
        tvContent.setTextColor(ContextCompat.getColor(mcontext, color));
    }
    /**
     * 获取内容
     *
     */
    public String getContent() {
        TextView tvContent = (TextView) findViewById(R.id.ctl_content);
        return tvContent.getText().toString();
    }
}
