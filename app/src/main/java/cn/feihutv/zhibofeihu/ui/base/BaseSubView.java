package cn.feihutv.zhibofeihu.ui.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/09/25
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public abstract class BaseSubView extends ViewGroup implements SubMvpView {

    private MvpView mParentMvpView;

    public BaseSubView(Context context) {
        super(context);
    }

    public BaseSubView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseSubView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public BaseSubView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void attachParentMvpView(MvpView mvpView) {
        mParentMvpView = mvpView;
    }

    @Override
    public void showLoading() {
        if (mParentMvpView != null) {
            mParentMvpView.showLoading();
        }
    }

    @Override
    public void hideLoading() {
        if (mParentMvpView != null) {
            mParentMvpView.hideLoading();
        }
    }

    @Override
    public void onToast(@StringRes int resId) {
        if (mParentMvpView != null) {
            mParentMvpView.onToast(resId);
        }
    }

    @Override
    public void onToast(String msg, int gravity, int xOffset, int yOffset) {
        if (mParentMvpView != null) {
            mParentMvpView.onToast(msg, gravity, xOffset, yOffset);
        }
    }

    @Override
    public void onToast(String message) {
        if (mParentMvpView != null) {
            mParentMvpView.onToast(message);
        }
    }

    @Override
    public void hideKeyboard() {
        if (mParentMvpView != null) {
            mParentMvpView.hideKeyboard();
        }
    }

    @Override
    public boolean isNetworkConnected() {
        if (mParentMvpView != null) {
            return mParentMvpView.isNetworkConnected();
        }
        return false;
    }

    @Override
    public void openActivityOnTokenExpire() {
        if (mParentMvpView != null) {
            mParentMvpView.openActivityOnTokenExpire();
        }
    }

    protected abstract void bindViewsAndSetOnClickListeners();

    protected abstract void setUp();
}
