package cn.feihutv.zhibofeihu.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.R;

/**
 * @author liwen.chen
 * @date 2017/8/23
 */

public class StatusView extends RelativeLayout {

    @BindView(R.id.ll_error_view)
    LinearLayout llErrorView;

    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;

    @BindView(R.id.ll_progress_bar)
    LinearLayout llProgressBar;

    @BindView(R.id.tv_no_data)
    TextView mTvNoData;

    @BindView(R.id.tv_error)
    TextView mTvError;

    @BindView(R.id.img_no_data)
    ImageView ivNoData;

    public ClickRefreshListener clickRefreshListener;

    public void setTvNoData(String tvNoData) {
        mTvNoData.setText(tvNoData);
    }

    public void setTvError(String tvError) {
        mTvError.setText(tvError);
    }

    public void setClickRefreshListener(ClickRefreshListener clickRefreshListener) {
        this.clickRefreshListener = clickRefreshListener;
    }

    public StatusView(Context context) {
        this(context, null);
    }

    public StatusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.item_statusview_layout, this, true);
        ButterKnife.bind(this);
//        drawable = (AnimationDrawable) imgProgress.getDrawable();
    }

    @OnClick({R.id.ll_error_view, R.id.ll_no_data})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_error_view:
                if (clickRefreshListener != null) {
                    clickRefreshListener.clickTorefresh();
                }
                break;
            case R.id.ll_no_data:
                if (clickRefreshListener != null) {
                    clickRefreshListener.clickTorefresh();
                }
                break;
            default:
                break;
        }

    }

    public interface ClickRefreshListener {
        void clickTorefresh();
    }

    public void showErrorView() {
//        if (drawable.isRunning()) {
//            drawable.stop();
//        }

        if (llProgressBar.getVisibility() == View.VISIBLE) {
            llProgressBar.setVisibility(View.GONE);
        }

        if (llNoData.getVisibility() == View.VISIBLE) {
            llNoData.setVisibility(View.GONE);
        }

        if (llErrorView.getVisibility() != View.VISIBLE) {
            llErrorView.setVisibility(View.VISIBLE);
        }
    }

    public void showNoDataView() {
        if (llProgressBar.getVisibility() == View.VISIBLE) {
            llProgressBar.setVisibility(View.GONE);
        }

        if (llNoData.getVisibility() != View.VISIBLE) {
            llNoData.setVisibility(View.VISIBLE);
        }

        if (llErrorView.getVisibility() == View.VISIBLE) {
            llErrorView.setVisibility(View.GONE);
        }
    }

    public void showProgressView() {

        if (llNoData.getVisibility() == View.VISIBLE) {
            llNoData.setVisibility(View.GONE);
        }

        if (llErrorView.getVisibility() == View.VISIBLE) {
            llErrorView.setVisibility(View.GONE);
        }

//        if (!drawable.isRunning()) {
//            drawable.start();
//        }

        if (llProgressBar.getVisibility() != View.VISIBLE) {
            llProgressBar.setVisibility(View.VISIBLE);
        }
    }

    public void dismissProgressView() {
        if (llNoData.getVisibility() == View.VISIBLE) {
            llNoData.setVisibility(View.GONE);
        }

        if (llErrorView.getVisibility() == View.VISIBLE) {
            llErrorView.setVisibility(View.GONE);
        }

//        if (drawable.isRunning()) {
//            drawable.stop();
//        }

        if (llProgressBar.getVisibility() == View.VISIBLE) {
            llProgressBar.setVisibility(View.GONE);
        }
    }

    public void setNoDataBg(int resId) {
        ivNoData.setImageResource(resId);
    }

    public boolean isShowing() {
        if (llProgressBar.getVisibility() == View.VISIBLE) {
            return true;
        } else {
            return false;
        }
    }
}
