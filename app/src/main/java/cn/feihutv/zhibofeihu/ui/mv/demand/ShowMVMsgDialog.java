package cn.feihutv.zhibofeihu.ui.mv.demand;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.utils.weiget.dialog.RxDialog;


/**
 *
 *显示需求弹窗
 */
public class ShowMVMsgDialog extends RxDialog {



    public TextView mTvSure;
    public TextView mTvCancel;
    public TextView mContextText;


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
        View dialog_view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_mv_msg, null);
        mTvSure = (TextView) dialog_view.findViewById(R.id.tv_sure);
        mTvCancel = (TextView) dialog_view.findViewById(R.id.tv_cancel);
        mContextText= (TextView) dialog_view.findViewById(R.id.tv_pop);
        setContentView(dialog_view);
    }




    public ShowMVMsgDialog(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public ShowMVMsgDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public ShowMVMsgDialog(Context context) {
        super(context);
        initView();
    }

    public ShowMVMsgDialog(Activity context) {
        super(context);
        initView();
    }

    public ShowMVMsgDialog(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }
}
