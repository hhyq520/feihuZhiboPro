package cn.feihutv.zhibofeihu.ui.me;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.luck.picture.lib.tools.ScreenUtils;

import cn.feihutv.zhibofeihu.R;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/11/07
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class DynamicPopWindow {


   public  PopupWindow  mPopTop;
    TextView mTextView;



    public void  createDialog(Context context){
        mPopTop = new PopupWindow(context);
        int w = ScreenUtils.getScreenWidth(context);
        int h = ScreenUtils.getScreenHeight(context);
        mPopTop.setWidth(w / 2);
        mPopTop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopTop.setFocusable(true);////获取焦点
        mPopTop.setTouchable(true);
        mPopTop.setOutsideTouchable(true);//设置popupwindow外部可点击
        //    mPopTop.update();// 刷新状态
        ColorDrawable dw = new ColorDrawable(0000000000);// 实例化一个ColorDrawable颜色为半透明
        mPopTop.setBackgroundDrawable(dw);// 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
//        mPopTop.setAnimationStyle(R.style.popWindow_anim_style);//设置显示和消失动画
        LayoutInflater inflater = (LayoutInflater) context

                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View  cView = inflater.inflate(R.layout.pop_dynamic_top, null);
        mTextView=cView.findViewById(R.id.text);
        mPopTop.setContentView(cView);

    }


    public void setViewOnClickListener(@Nullable View.OnClickListener l){
        mTextView.setOnClickListener(l);
    }

    public void show(Context context,View view){
        mPopTop.showAtLocation(view,
                Gravity.TOP, 0, view.getHeight()
                        +ScreenUtils.getStatusBarHeight(context)); //titleBar 正下方中间位置



    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!mPopTop.isShowing()) {
            // 以下拉方式显示popupwindow
            mPopTop.showAsDropDown(parent, parent.getLayoutParams().width / 2, 5);
        } else {
            this.dismiss();
        }
    }
    public void dismiss(){
        if(mPopTop!=null&&mPopTop.isShowing()){
            mPopTop.dismiss();
        }

    }



}
