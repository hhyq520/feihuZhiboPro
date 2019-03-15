package cn.feihutv.zhibofeihu.ui.widget.swiperefreshlayout;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.feihutv.zhibofeihu.R;


/**
 *
 * @author liwen.chen
 */

public class FHHeaderView extends RelativeLayout implements VRefreshLayout.UpdateHandler {

    private ImageView img_progress;
    private TextView mStatusTv;
    private AnimationDrawable mAnimationDrawable;

    public FHHeaderView(Context context) {
        this(context, null);
    }

    public FHHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_loading_custom, this, true);
        img_progress = (ImageView) findViewById(R.id.img_progress);
        mStatusTv = (TextView) findViewById(R.id.tv_status);
    }

    @Override
    public void onProgressUpdate(VRefreshLayout layout, VRefreshLayout.Progress progress, int status) {
        switch (status) {
            case VRefreshLayout.STATUS_INIT:
                img_progress.setScaleX(0.1f);
                img_progress.setScaleY(0.1f);
//                img_progress.setTranslationX(0);
                img_progress.setImageResource(R.drawable.loading_1);
                mStatusTv.setText(R.string.pull_to_refresh);
                if (mAnimationDrawable != null) {
                    mAnimationDrawable.stop();
                    mAnimationDrawable = null;
                }

                break;
            case VRefreshLayout.STATUS_DRAGGING:
                float currentY = progress.getCurrentY();
                float refreshY = progress.getRefreshY();
                float percent = Math.min(1.0f, currentY / refreshY);
                img_progress.setScaleX(percent);
                img_progress.setScaleY(percent);
//                img_progress.setTranslationX(percent * TCUtils.dp2px(getContext(), 18f));
                if (percent >= 1.0f) {
                    mStatusTv.setText(R.string.release_to_refresh);
                } else {
                    mStatusTv.setText(R.string.pull_to_refresh);
                }
                break;
            case VRefreshLayout.STATUS_RELEASE_PREPARE:
            case VRefreshLayout.STATUS_REFRESHING:
                if (mAnimationDrawable == null) {
                    mStatusTv.setText(R.string.refreshing);
                    img_progress.setImageResource(R.drawable.loading_anim);
                    mAnimationDrawable = (AnimationDrawable) img_progress.getDrawable();
                    mAnimationDrawable.start();
                }
                break;
            default:
                break;
        }
    }
}
