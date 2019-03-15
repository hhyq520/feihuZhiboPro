package cn.feihutv.zhibofeihu.ui.mv.demand;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.utils.weiget.dialog.RxDialog;


/**
 *
 *修改需求内容
 */
public class BuyMvModifyDialog extends RxDialog {

    private TextView mTvSure;
    private TextView mTvCancel;

    public TextView getTitle() {
        return mTitle;
    }

    private TextView mTitle;



    public EditText getEt_input_desc() {
        return et_input_desc;
    }

    private  EditText  et_input_desc;

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
        View dialog_view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_modify_buy_mv, null);
        mTvSure = (TextView) dialog_view.findViewById(R.id.tv_sure);
        mTvCancel = (TextView) dialog_view.findViewById(R.id.tv_cancel);
        mTitle= (TextView) dialog_view.findViewById(R.id.tv_title);
        et_input_desc= dialog_view.findViewById(R.id.tv_desc);
        setContentView(dialog_view);
    }





    public BuyMvModifyDialog(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public BuyMvModifyDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public BuyMvModifyDialog(Context context) {
        super(context);
        initView();
    }

    public BuyMvModifyDialog(Activity context) {
        super(context);
        initView();
    }

    public BuyMvModifyDialog(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }
}
