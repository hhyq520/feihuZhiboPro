package cn.feihutv.zhibofeihu.utils.weiget.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cn.feihutv.zhibofeihu.R;


/**
 * Created by vondear on 2016/7/19.
 * Mainly used for confirmation and cancel.
 */
public class RxDialogSureCancel2 extends RxDialog {

    private TextView mTvContent;
    private TextView mTvSure;
    private TextView mTvCancel;

    public TextView getTitle() {
        return mTitle;
    }

    private TextView mTitle;

    public void setContent(String content) {
        this.mTvContent.setText(content);
    }
    public void setContent(int content) {
        this.mTvContent.setText(content);
    }

    public TextView getTvContent() {
        return mTvContent;
    }

    public void setSure(String strSure) {
        this.mTvSure.setText(strSure);
    }

    public TextView getTvSure() {
        return mTvSure;
    }

    public void setCancel(String strCancel) {
        this.mTvCancel.setText(strCancel);
    }

    public TextView getTvCancel() {
        return mTvCancel;
    }

    public void setSureListener(View.OnClickListener sureListener) {
        mTvSure.setOnClickListener(sureListener);
    }

    public void setCancelListener(View.OnClickListener cancelListener) {
        mTvCancel.setOnClickListener(cancelListener);
    }

    private void initView() {
        View dialog_view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_hint_content, null);
        mTvSure = (TextView) dialog_view.findViewById(R.id.tv_sure);
        mTvCancel = (TextView) dialog_view.findViewById(R.id.tv_cancel);
        mTvContent = (TextView) dialog_view.findViewById(R.id.tv_content);
        mTitle= (TextView) dialog_view.findViewById(R.id.tv_title);
        mTvContent.setTextIsSelectable(true);
        setContentView(dialog_view);
    }



    public RxDialogSureCancel2(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogSureCancel2(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogSureCancel2(Context context) {
        super(context);
        initView();
    }

    public RxDialogSureCancel2(Activity context) {
        super(context);
        initView();
    }

    public RxDialogSureCancel2(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }
}
