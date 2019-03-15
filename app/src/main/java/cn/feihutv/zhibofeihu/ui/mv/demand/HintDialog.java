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
 *
 */
public class HintDialog extends RxDialog {

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

    private void initDialogView(Context context) {
        View dialog_view = LayoutInflater.from(context).inflate(R.layout.dialog_hint, null);
        mTvSure = (TextView) dialog_view.findViewById(R.id.tv_sure);
        mTvCancel = (TextView) dialog_view.findViewById(R.id.tv_cancel);
        mTvContent = (TextView) dialog_view.findViewById(R.id.tv_content);
        mTitle= (TextView) dialog_view.findViewById(R.id.tv_title);
        setContentView(dialog_view);
    }




    public HintDialog(Context context, int themeResId) {
        super(context, themeResId);
        initDialogView(context);
    }

    public HintDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initDialogView(context);
    }

    public HintDialog(Context context) {
        super(context);
        initDialogView(  context );
    }

    public HintDialog(Activity context) {
        super(context);
        initDialogView(context);
    }

    public HintDialog(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initDialogView(context);
    }
}
