package cn.feihutv.zhibofeihu.utils.weiget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import cn.feihutv.zhibofeihu.R;

/**
 * <pre>
 *      author : liwen.chen
 *      time   : 2017/10/17 09:34
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class FHAutoImageView extends FrameLayout {

    private ImageView mImageView;

    int resId;

    public FHAutoImageView(Context context) {
        super(context);
    }

    public FHAutoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //导入布局
        initView(context, attrs);
    }

    private void initView(final Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_auto_imageview, this);

        mImageView = (ImageView) findViewById(R.id.img_backgroud);

        //获得这个控件对应的属性。
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FHAutoImageView);

        try {
            //获得属性值
            resId = a.getResourceId(R.styleable.FHAutoImageView_ImageSrc, 0);
        } finally {
            //回收这个对象
            a.recycle();
        }

        if (resId != 0) {
            mImageView.setImageResource(resId);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
                mImageView.startAnimation(animation);
            }
        }, 200);
    }

}
