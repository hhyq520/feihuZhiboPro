package cn.feihutv.zhibofeihu.ui.home.signin;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.feihutv.zhibofeihu.BuildConfig;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.common.AwardsBean;
import cn.feihutv.zhibofeihu.data.network.http.model.common.SignResponse;
import cn.feihutv.zhibofeihu.di.component.ActivityComponent;
import cn.feihutv.zhibofeihu.ui.base.BaseDialog;
import cn.feihutv.zhibofeihu.ui.home.adapter.SignAdapter;
import cn.feihutv.zhibofeihu.utils.CustomDialogUtils;


/**
 * <pre>
 *     @author : sichu.chen
 *     time   : 2017/10/11
 *     desc   : Dialog 界面
 *     version: 1.0
 * </pre>
 */
public class SignInDialog extends BaseDialog implements SignInDialogMvpView {

    private static final String TAG = "SignInDialog";

    @Inject
    SignInDialogMvpPresenter<SignInDialogMvpView> mPresenter;

    @BindView(R.id.sign_recyclerview)
    RecyclerView mRecyclerView;

    Unbinder unbinder;

    private SignAdapter mSignAdapter;

    private List<AwardsBean> mAwardsBeen = new ArrayList<>();
    private int mSignDay;

    public static SignInDialog newInstance(int signDay) {
        SignInDialog dialog = new SignInDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("signDay", signDay);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_signin, container, false);

        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            mPresenter.onAttach(this);
        }
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    public void show(FragmentManager fragmentManager) {
        super.show(fragmentManager, TAG);
    }

    @Override
    protected void setUp(View view) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mSignDay = bundle.getInt("signDay", -1);
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSignAdapter = new SignAdapter(mAwardsBeen);
        mRecyclerView.setAdapter(mSignAdapter);
        mPresenter.querySignAwards();
    }


    @Override
    public void dismissDialog() {
        super.dismissDialog(TAG);
    }

    @Override
    public void signView(SignResponse.SignData signData) {
        // 显示签到成功 获取奖励
        SignSuccessDialog successDialog = new SignSuccessDialog();
        successDialog.show(getFragmentManager());
        successDialog.getDatas(signData);

    }

    @Override
    public void getAllSignAwards(List<AwardsBean> awardsBeen) {
        if (mSignDay != -1) {
            for (int i = 0; i < mSignDay; i++) {
                awardsBeen.get(i).setSignDay(true);
            }
        }
        mSignAdapter.setNewData(awardsBeen);

    }

    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showLoading(String msg) {

    }

    @OnClick({R.id.btn_sign, R.id.iv_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_sign:
                if (mPresenter.isGuestUser()) {
                    if ("1".equals(BuildConfig.isForceLoad)) {
                        CustomDialogUtils.showQZLoginDialog(getActivity(), false);
                    } else {
                        CustomDialogUtils.showLoginDialog(getActivity(), false);
                    }
                } else {
                    mPresenter.sign();
                }
                break;
            case R.id.iv_close:
                dismissDialog();
                break;
            default:
                break;
        }
    }
}