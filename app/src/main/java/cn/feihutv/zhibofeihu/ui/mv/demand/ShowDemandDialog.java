package cn.feihutv.zhibofeihu.ui.mv.demand;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.utils.weiget.dialog.RxDialog;


/**
 *
 *显示需求弹窗
 */
public class ShowDemandDialog extends RxDialog {



    private TextView mTvContent;

    private TextView tv_userName;
    private TextView mTvSure;
    private TextView mTvCancel;
    private ImageView iv_head,iv_save;





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


    public TextView getTv_userName() {
        return tv_userName;
    }

    public ImageView getIv_head() {
        return iv_head;
    }



    private void initView() {
        View dialog_view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_show_demand, null);
        mTvSure = (TextView) dialog_view.findViewById(R.id.tv_sure);
        mTvCancel = (TextView) dialog_view.findViewById(R.id.tv_cancel);
        mTvContent = (TextView) dialog_view.findViewById(R.id.tv_desc);
        tv_userName= (TextView) dialog_view.findViewById(R.id.tv_userName);
        iv_head= dialog_view.findViewById(R.id.iv_head);
        iv_save= dialog_view.findViewById(R.id.iv_save);
        setContentView(dialog_view);
    }



    public ImageView getIv_save(){
        return iv_save;
    }

    public void setSaveOnClickListener(View.OnClickListener cancelListener){
        iv_save.setOnClickListener(cancelListener);
    }




    public ShowDemandDialog(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public ShowDemandDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public ShowDemandDialog(Context context) {
        super(context);
        initView();
    }

    public ShowDemandDialog(Activity context) {
        super(context);
        initView();
    }

    public ShowDemandDialog(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }
}
