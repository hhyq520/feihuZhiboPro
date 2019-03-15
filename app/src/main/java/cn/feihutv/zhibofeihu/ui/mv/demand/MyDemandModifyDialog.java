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
 * 修改需求内容
 */
public class MyDemandModifyDialog extends RxDialog {

    private TextView mTvContent;
    private TextView mTvSure;
    private TextView mTvCancel;

    public TextView getTitle() {
        return mTitle;
    }

    private TextView mTitle;

    public void setTv_songName(String tv_songName) {
        this.tv_songName.setText(tv_songName);
    }

    private TextView tv_songName;

    public EditText getEt_input_desc() {
        return et_input_desc;
    }

    private EditText et_input_desc;

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
        View dialog_view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_modify_demand, null);
        mTvSure = (TextView) dialog_view.findViewById(R.id.tv_sure);
        mTvCancel = (TextView) dialog_view.findViewById(R.id.tv_cancel);
        mTvContent = (TextView) dialog_view.findViewById(R.id.tv_content);
        mTitle = (TextView) dialog_view.findViewById(R.id.tv_title);
        tv_songName = dialog_view.findViewById(R.id.tv_songName);
        et_input_desc = dialog_view.findViewById(R.id.tv_desc);
        setContentView(dialog_view);
    }


    public MyDemandModifyDialog(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public MyDemandModifyDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public MyDemandModifyDialog(Context context) {
        super(context);
        initView();
    }

    public MyDemandModifyDialog(Activity context) {
        super(context);
        initView();
    }

    public MyDemandModifyDialog(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }
}
