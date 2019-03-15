package cn.feihutv.zhibofeihu.ui.base;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Unbinder;
import cn.feihutv.zhibofeihu.di.component.ActivityComponent;
import cn.feihutv.zhibofeihu.rxbus.RxBus;
import cn.feihutv.zhibofeihu.ui.widget.ScreenSwitch;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/09/25
 *     desc   : Fragment 页面基类，
 *     version: 1.0
 * </pre>
 */
public abstract class BaseFragment extends Fragment implements MvpView {

    private BaseActivity mActivity;
    private Unbinder mUnBinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        RxBus.get().register(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) context;
            this.mActivity = activity;
            activity.onFragmentAttached();
        }

    }

    /**
     * @return 布局id
     */
    protected abstract int getLayoutId();

    /**
     * 初始化控件
     */
    protected abstract void initWidget(View view);


    protected boolean isVisible;

    //fragment 懒加载
    protected abstract void lazyLoad();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=super.onCreateView(inflater, container, savedInstanceState);
        if(getLayoutId()!=0) {
            view = inflater.inflate(getLayoutId(), container, false);
        }
        initWidget(view);
        lazyLoad();
        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 在这里实现Fragment数据的懒加载
     *
     * @param isVisibleToUser Fragment UI对用户是否可见
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    protected void onVisible() {
        lazyLoad();
    }

    protected void onInvisible() {
    }

    @Override
    public void showLoading() {
        if (mActivity != null) {
            mActivity.showLoading();
        }
    }

    @Override
    public void showLoading(String msg) {
        if (mActivity != null) {
            mActivity.showLoading(msg);
        }
    }

    @Override
    public void hideLoading() {
        if (mActivity != null) {
            mActivity.hideLoading();
        }
    }

    @Override
    public void onToast(String message) {
        if (mActivity != null) {
            mActivity.onToast(message);
        }
    }

    @Override
    public void onToast(@StringRes int resId) {
        if (mActivity != null) {
            mActivity.onToast(resId);
        }
    }

    @Override
    public void onToast(String msg, int gravity, int xOffset, int yOffset) {
        if (mActivity != null) {
            mActivity.onToast(msg, gravity, xOffset, yOffset);
        }
    }

    @Override
    public boolean isNetworkConnected() {
        if (mActivity != null) {
            return mActivity.isNetworkConnected();
        }
        return false;
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }

    @Override
    public void hideKeyboard() {
        if (mActivity != null) {
            mActivity.hideKeyboard();
        }
    }

    @Override
    public void openActivityOnTokenExpire() {
        if (mActivity != null) {
            mActivity.openActivityOnTokenExpire();
        }
    }



    public ActivityComponent getActivityComponent() {
        return mActivity.getActivityComponent();
    }

    public BaseActivity getBaseActivity() {
        return mActivity;
    }

    public void setUnBinder(Unbinder unBinder) {
        mUnBinder = unBinder;
    }

    @Override
    public void onDestroy() {
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        RxBus.get().unRegister(this);
        super.onDestroy();
    }



    public interface Callback {

        void onFragmentAttached();

        void onFragmentDetached(String tag);
    }

    public void goActivityForResult(Class<?> descClass, int requestCode) {
        ScreenSwitch.switchActivity(mActivity, descClass, null, requestCode);
    }

    public void goActivityForResult(Class<?> descClass, Bundle bundle, int requestCode) {
        ScreenSwitch.switchActivity(mActivity, descClass, bundle, requestCode);
    }

    public void goActivity(Class<?> descClass, Bundle bundle) {
        goActivityForResult(descClass, bundle, 0);
    }

    public void goActivity(Class<?> descClass) {
        goActivity(descClass, null);
    }
}
