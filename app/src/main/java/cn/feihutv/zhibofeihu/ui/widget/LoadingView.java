package cn.feihutv.zhibofeihu.ui.widget;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.ui.widget.flashview.FlashDataParser;
import cn.feihutv.zhibofeihu.ui.widget.flashview.FlashView;


/**
 * Created by huanghao on 2017/6/19.
 */

public class LoadingView extends DialogFragment {
    private FlashView flashView;
    private GraduallyTextView graduallyTextView;
    Dialog mDialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mDialog == null) {
            mDialog = new Dialog(getActivity(), R.style.cart_dialog);
            mDialog.setContentView(R.layout.catloading_main);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.getWindow().setGravity(Gravity.CENTER);
            flashView = (FlashView) mDialog.findViewById(R.id.flashview);

            graduallyTextView = (GraduallyTextView) mDialog.findViewById(R.id.graduallyTextView);
            flashView.reload("loading", "flashAnims");
            flashView.play("loading", FlashDataParser.FlashLoopTimeForever);
            flashView.setScale(0.2f, 0.2f);

        }
        return mDialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        // graduallyTextView.startLoading();
    }

    @Override
    public void onPause() {
        super.onPause();
        // graduallyTextView.stopLoading();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mDialog = null;
    }
}
