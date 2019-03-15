package cn.feihutv.zhibofeihu.ui.mv;

import android.app.Activity;
import android.app.Dialog;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import cn.feihutv.zhibofeihu.R;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/11/24
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class MvShareDialog  {

    public void createDialog(Activity context){
        final Dialog pickDialog1 = new Dialog(context, R.style.color_dialog);
        pickDialog1.setContentView(R.layout.share_dialog_mv);

        WindowManager windowManager = context.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog1.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = (int) (display.getWidth()); //设置宽度
        pickDialog1.getWindow().setAttributes(lp);

        ImageView btn_share_circle = (ImageView) pickDialog1.findViewById(R.id.btn_share_circle);
        ImageView btn_share_wx = (ImageView) pickDialog1.findViewById(R.id.btn_share_wx);
        ImageView btn_share_wb = (ImageView) pickDialog1.findViewById(R.id.btn_share_wb);
        ImageView btn_share_qq = (ImageView) pickDialog1.findViewById(R.id.btn_share_qq);
        ImageView btn_share_qzone = (ImageView) pickDialog1.findViewById(R.id.btn_share_qzone);

//        btn_share_circle.setOnClickListener(this);
//        btn_share_wx.setOnClickListener(this);
//        btn_share_wb.setOnClickListener(this);
//        btn_share_qq.setOnClickListener(this);
//        btn_share_qzone.setOnClickListener(this);

        pickDialog1.show();
    }
}
