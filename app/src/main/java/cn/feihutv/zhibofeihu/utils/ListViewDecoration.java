package cn.feihutv.zhibofeihu.utils;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;


/**
 * Created by clw on 2017/2/20.
 * 分割线
 */

public class ListViewDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDrawable;
    private int tag;
    public ListViewDecoration() {
        mDrawable = ContextCompat.getDrawable(FeihuZhiboApplication.getApplication(), R.drawable.divider_recycler);
    }

    public ListViewDecoration(int i) {
        tag=i;
        if (i == 1) {
            mDrawable = ContextCompat.getDrawable(FeihuZhiboApplication.getApplication(), R.drawable.divider_c1_recycler);
        } else if (i == 2) {
            mDrawable = ContextCompat.getDrawable(FeihuZhiboApplication.getApplication(), R.drawable.divider_c2_recycler);
        } else if (i == 3) {
            mDrawable = ContextCompat.getDrawable(FeihuZhiboApplication.getApplication(), R.drawable.divider_c3_recycler);
        }else if(i == 4) {
            mDrawable = ContextCompat.getDrawable(FeihuZhiboApplication.getApplication(), R.drawable.divider_c1_recycler);
        }else if(i == 5) {
            mDrawable = ContextCompat.getDrawable(FeihuZhiboApplication.getApplication(), R.drawable.divider_c5_recycler);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();

        for(int i = 0; i < childCount; ++i) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)child.getLayoutParams();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + this.mDrawable.getIntrinsicHeight();
            this.mDrawable.setBounds(left, top, right, bottom);
            this.mDrawable.draw(c);
        }

    }


    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if(tag!=4) {
            final int left = parent.getPaddingLeft();
            final int right = parent.getWidth() - parent.getPaddingRight();

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount - 1; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                // 以下计算主要用来确定绘制的位置
                final int top = child.getBottom() + params.bottomMargin;
                final int bottom = top + mDrawable.getIntrinsicHeight();
                mDrawable.setBounds(left, top, right, bottom);
                mDrawable.draw(c);
            }
        }else{
            this.drawVertical(c,parent);
        }

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, mDrawable.getIntrinsicHeight());
    }
}
