package cn.feihutv.zhibofeihu.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import cn.feihutv.zhibofeihu.R;

/**
 * Created by Administrator on 2017/3/20.
 */

public class FHActivityCar extends RelativeLayout {

    ImageView luckImg;
    private int drawableId;
    private String giftNum;
    private String giftPrice;
    private String exp;

    private ImageView imgGift;
    private TextView tvNum;
    private FrameLayout frameLayout;
    private RelativeLayout relativeLayout;

    public FHActivityCar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_car, this);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TCActivityGift, 0, 0);
        try {
            drawableId = ta.getIndex(R.styleable.TCActivityGift_bgImage);
            giftNum = ta.getString(R.styleable.TCActivityGift_giftNum);
            giftPrice = ta.getString(R.styleable.TCActivityGift_giftPrice);
            exp = ta.getString(R.styleable.TCActivityGift_exp);
            setUpView();
        } finally {
            ta.recycle();
        }
    }

    private void setUpView() {
        luckImg= (ImageView) findViewById(R.id.luck_img);
        imgGift = (ImageView) findViewById(R.id.gift_bg);
        tvNum = (TextView) findViewById(R.id.tv_num);
        frameLayout = (FrameLayout) findViewById(R.id.fragment);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
    }

    public void setSelect(boolean flag) {
        relativeLayout.setSelected(flag);
    }
    public void setLuckImgVis(boolean flag) {
        if(flag){
            luckImg.setVisibility(VISIBLE);
        }else{
            luckImg.setVisibility(GONE);
        }
    }

    public void setLuckImg(String url) {
        luckImg.setVisibility(VISIBLE);
        Glide.with(getContext()).load(url).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)).into(luckImg);
    }

    /**
     * 设置数量
     *
     * @param giftNum 数量
     */
    public void setNum(String giftNum) {
        this.giftNum = giftNum;
        tvNum.setText(giftNum);
    }


    public void setImgGift(Bitmap bitmap) {
        imgGift.setImageBitmap(bitmap);
    }

    public void setImageUrl(String imgUrl) {
        Glide.with(getContext()).load(imgUrl).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)).into(imgGift);
    }


}
