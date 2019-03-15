package cn.feihutv.zhibofeihu.ui.widget;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.me.PhotoInfo;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.feihutv.zhibofeihu.utils.glideutil.GlideApp;

/**
 * @author shoyu
 * @ClassName MultiImageView.java
 * @Description: 显示1~N张图片的View
 */

public class MultiImageView extends LinearLayout {
    public static int MAX_WIDTH = 0;

    // 照片的Url列表
    private List<PhotoInfo> imagesList;

    /**
     * 长度 单位为Pixel
     **/
    private int pxOneMaxWandH;  // 单张图最大允许宽高
    private int pxMoreWandH = 0;// 多张图的宽高
    private int pxImagePadding = TCUtils.dipToPx(getContext(), 3);// 图片间的间距

    private int MAX_PER_ROW_COUNT = 3;// 每行显示最大数

    private LayoutParams onePicPara;
    private LayoutParams morePara, moreParaColumnFirst;
    private LayoutParams rowPara;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public MultiImageView(Context context) {
        super(context);
    }

    public MultiImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setList(List<PhotoInfo> lists) throws IllegalArgumentException {
        if (lists == null) {
            throw new IllegalArgumentException("imageList is null...");
        }
        imagesList = lists;

        if (MAX_WIDTH > 0) {
            pxMoreWandH = (MAX_WIDTH - pxImagePadding * 2) / 3; //解决右侧图片和内容对不齐问题
            pxOneMaxWandH = MAX_WIDTH * 2 / 3;
            initImageLayoutParams();
        }

        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MAX_WIDTH == 0) {
            int width = measureWidth(widthMeasureSpec);
            if (width > 0) {
                MAX_WIDTH = width;
                if (imagesList != null && imagesList.size() > 0) {
                    setList(imagesList);
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * Determines the width of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @return The width of the view, honoring constraints from measureSpec
     */
    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text
            // result = (int) mTextPaint.measureText(mText) + getPaddingLeft()
            // + getPaddingRight();
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by
                // measureSpec
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private void initImageLayoutParams() {
        int wrap = LayoutParams.WRAP_CONTENT;
        int match = LayoutParams.MATCH_PARENT;

        onePicPara = new LayoutParams(wrap, wrap);

        moreParaColumnFirst = new LayoutParams(pxMoreWandH, pxMoreWandH);
        morePara = new LayoutParams(pxMoreWandH, pxMoreWandH);
        morePara.setMargins(pxImagePadding, 0, 0, 0);

        rowPara = new LayoutParams(match, wrap);
    }

    // 根据imageView的数量初始化不同的View布局,还要为每一个View作点击效果
    private void initView() {
        this.setOrientation(VERTICAL);
        this.removeAllViews();
        if (MAX_WIDTH == 0) {
            //为了触发onMeasure()来测量MultiImageView的最大宽度，MultiImageView的宽设置为match_parent
            addView(new View(getContext()));
            return;
        }

        if (imagesList == null || imagesList.size() == 0) {
            return;
        }

        if (imagesList.size() == 1) {
            addView(createImageView(0, false));
        } else {
            int allCount = imagesList.size();
            if (allCount == 4) {
                MAX_PER_ROW_COUNT = 2;
            } else {
                MAX_PER_ROW_COUNT = 3;
            }
            int rowCount = allCount / MAX_PER_ROW_COUNT
                    + (allCount % MAX_PER_ROW_COUNT > 0 ? 1 : 0);// 行数
            for (int rowCursor = 0; rowCursor < rowCount; rowCursor++) {
                LinearLayout rowLayout = new LinearLayout(getContext());
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);

                rowLayout.setLayoutParams(rowPara);
                if (rowCursor != 0) {
                    rowLayout.setPadding(0, pxImagePadding, 0, 0);
                }

                int columnCount = allCount % MAX_PER_ROW_COUNT == 0 ? MAX_PER_ROW_COUNT
                        : allCount % MAX_PER_ROW_COUNT;//每行的列数
                if (rowCursor != rowCount - 1) {
                    columnCount = MAX_PER_ROW_COUNT;
                }
                addView(rowLayout);

                int rowOffset = rowCursor * MAX_PER_ROW_COUNT;// 行偏移
                for (int columnCursor = 0; columnCursor < columnCount; columnCursor++) {
                    int position = columnCursor + rowOffset;
                    rowLayout.addView(createImageView(position, true));
                }
            }
        }
    }

    private ImageView createImageView(final int position, final boolean isMultiImage) {
        final PhotoInfo photoInfo = imagesList.get(position);
        final ImageView imageView = new ColorFilterImageView(getContext());
        if (isMultiImage) {
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(position % MAX_PER_ROW_COUNT == 0 ? moreParaColumnFirst : morePara);
            imageView.setId(photoInfo.getUrl().hashCode());
            imageView.setOnClickListener(new ImageOnClickListener(position));
            imageView.setBackgroundColor(getResources().getColor(R.color.im_font_color_text_hint));
            GlideApp.loadImg(getContext(), photoInfo.getUrl(), DiskCacheStrategy.ALL, imageView);

        } else {
//			imageView.setAdjustViewBounds(true);
//			imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            RequestBuilder<Bitmap> req = Glide.with(getContext()).asBitmap().apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL));
            req.load(photoInfo.getUrl()).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    int w = resource.getWidth();
                    int h = resource.getHeight();
                    int MAX_W_H_RATIO = 3;
                    int newW;
                    int newH;
                    if (h > w * MAX_W_H_RATIO) {//h:w = 5:3
                        newW = pxOneMaxWandH / 2;
                        newH = newW * 5 / 3;
                    } else if (h < w) {//h:w = 2:3
                        newW = pxOneMaxWandH * 2 / 3;
                        newH = newW * 2 / 3;
                    } else {//newH:h = newW :w
                        newW = pxOneMaxWandH / 2;
                        newH = h * newW / w;
                    }
                    imageView.setLayoutParams(new LayoutParams(newW, newH));
                    imageView.setId(photoInfo.getUrl().hashCode());
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setOnClickListener(new ImageOnClickListener(position));
                    imageView.setBackgroundColor(getResources().getColor(R.color.im_font_color_text_hint));
                    imageView.setImageBitmap(resource);
                }
            });


//
//            int expectW = photoInfo.getW();
//            int expectH = photoInfo.getH();
//            if (expectW == 0 || expectH == 0) {
//                imageView.setLayoutParams(onePicPara);
//            } else {
//                int actualW = 0;
//                int actualH = 0;
//                float scale = ((float) expectH) / ((float) expectW);
//                if (expectW > pxOneMaxWandH) {
//                    actualW = pxOneMaxWandH;
//                    actualH = (int) (actualW * scale);
//                } else if (expectW < pxMoreWandH) {
//                    actualW = pxMoreWandH;
//                    actualH = (int) (actualW * scale);
//                } else {
//                    actualW = expectW;
//                    actualH = expectH;
//                }
//                imageView.setLayoutParams(new LayoutParams(actualW, actualH));
//            }
//
//        }
//
//        imageView.setId(photoInfo.getUrl().hashCode());
//        imageView.setOnClickListener(new ImageOnClickListener(position));
//        imageView.setBackgroundColor(getResources().getColor(R.color.im_font_color_text_hint));
        }
        return imageView;
    }
    private class ImageOnClickListener implements OnClickListener {

        private int position;

        public ImageOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, position);
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }
}