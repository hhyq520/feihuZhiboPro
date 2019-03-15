package cn.feihutv.zhibofeihu.utils.weiget.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cn.feihutv.zhibofeihu.R;



public class DialogForce extends RxDialog {

    private TextView mTvContent;

    public void setContent(String content) {
        this.mTvContent.setText(content);
    }

    public TextView getTvContent() {
        return mTvContent;
    }

    private void initView() {
        View dialog_view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_force, null);
        mTvContent = (TextView) dialog_view.findViewById(R.id.tv_content);
        mTvContent.setTextIsSelectable(true);
        setContentView(dialog_view);
    }

    public DialogForce(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public DialogForce(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public DialogForce(Context context) {
        super(context);
        initView();
    }

    public DialogForce(Activity context) {
        super(context);
        initView();
    }

    public DialogForce(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }
}
