package cn.feihutv.zhibofeihu.ui.widget.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import cn.feihutv.zhibofeihu.ui.mv.demand.ShowMVMsgDialog;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/12/06
 *     desc   : mv 消息弹窗
 *     version: 1.0
 * </pre>
 */
public class MvMsgDialog {


    public ShowMVMsgDialog mShowMVMsgDialog;

    //显示提示框
    public void openShowMVMsgDialog(Context context,String msg){
        mShowMVMsgDialog=new ShowMVMsgDialog(context);
        if(!TextUtils.isEmpty(msg)){
            mShowMVMsgDialog.mContextText.setText(msg);
        }
        mShowMVMsgDialog.setSureListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShowMVMsgDialog.dismiss();

            }
        });
        mShowMVMsgDialog.show();
    }


    public void openShowMVMsgDialog(Context context,String msg,String btnStr){
        mShowMVMsgDialog=new ShowMVMsgDialog(context);
        if(!TextUtils.isEmpty(msg)){
            mShowMVMsgDialog.mContextText.setText(msg);
        }
        if(!TextUtils.isEmpty(btnStr)){
            mShowMVMsgDialog.mTvSure.setText(btnStr);
        }
        mShowMVMsgDialog.setSureListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShowMVMsgDialog.dismiss();

            }
        });
        mShowMVMsgDialog.show();
    }

    public void openShowMVMsgCancelDialog(Context context,String msg,String btnStr,View.OnClickListener okLis){
        mShowMVMsgDialog=new ShowMVMsgDialog(context);
        mShowMVMsgDialog.mTvCancel.setVisibility(View.VISIBLE);
        mShowMVMsgDialog.mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShowMVMsgDialog.dismiss();
            }
        });
        if(!TextUtils.isEmpty(msg)){
            mShowMVMsgDialog.mContextText.setText(msg);
        }
        if(!TextUtils.isEmpty(btnStr)){
            mShowMVMsgDialog.mTvSure.setText(btnStr);
        }
        mShowMVMsgDialog.setSureListener(okLis);
        mShowMVMsgDialog.show();
    }



}
