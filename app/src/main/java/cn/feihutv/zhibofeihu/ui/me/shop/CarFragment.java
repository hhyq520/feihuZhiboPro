package cn.feihutv.zhibofeihu.ui.me.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.db.model.SysGoodsNewBean;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.ValueChangePush;
import cn.feihutv.zhibofeihu.data.prefs.AppPreferencesHelper;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.rxbus.RxBusSubscribe;
import cn.feihutv.zhibofeihu.rxbus.ThreadMode;
import cn.feihutv.zhibofeihu.ui.base.BaseFragment;
import cn.feihutv.zhibofeihu.ui.me.adapter.ShopCarAdapter;
import cn.feihutv.zhibofeihu.ui.me.baoguo.BegPackageActivity;
import cn.feihutv.zhibofeihu.ui.me.recharge.RechargeActivity;
import cn.feihutv.zhibofeihu.ui.me.vip.MyVipActivity;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.feihutv.zhibofeihu.utils.TCConstants;
import cn.feihutv.zhibofeihu.utils.weiget.dialog.RxDialogSureCancel;

/**
 * <pre>
 *     author : liwen.chen
 *     time   : 2017/
 *     desc   : 座驾
 *     version: 1.0
 * </pre>
 */
public class CarFragment extends BaseFragment implements CarMvpView {

    @Inject
    CarMvpPresenter<CarMvpView> mPresenter;
    @BindView(R.id.tv_gift_hubi)
    TextView hb;
    @BindView(R.id.car_rv)
    RecyclerView mRecyclerView;

    private boolean isPrepared;

    private int isNeedVip = 0; // 0：不需要VIP  1：需要VIP

    private ShopCarAdapter mCarAdapter;
    private LoadUserDataBaseResponse.UserData mUserData;

    private List<SysGoodsNewBean> mSysGoodsNewBeen = new ArrayList<>();

    private int id = -1;


    public static CarFragment newInstance() {
        Bundle args = new Bundle();
        CarFragment fragment = new CarFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_car;
    }


    @Override
    protected void initWidget(View view) {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this, view));

        //Presenter调用初始化
        mPresenter.onAttach(CarFragment.this);

        isPrepared = true;

        mUserData = mPresenter.getUserData();

        hb.setText(FHUtils.intToF(mUserData.gethB()));

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mCarAdapter = new ShopCarAdapter(mSysGoodsNewBeen);
        mRecyclerView.setAdapter(mCarAdapter);

        mCarAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                for (SysGoodsNewBean sysGoodsNewBean : mSysGoodsNewBeen) {
                    sysGoodsNewBean.setIsSelected(false);
                }
                mSysGoodsNewBeen.get(position).setIsSelected(true);
                id = Integer.valueOf(mSysGoodsNewBeen.get(position).getId());
                isNeedVip = mSysGoodsNewBeen.get(position).getNeedVip();
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void lazyLoad() {
        if (isPrepared && isVisible) {
            mPresenter.getStoreZuojia();
        }
    }

    @OnClick({R.id.img_recharge, R.id.btn_buy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_recharge:
                Intent intent = new Intent(getContext(), RechargeActivity.class);
                intent.putExtra("fromWhere", "座驾页面");
                startActivity(intent);
                break;
            case R.id.btn_buy:
                MobclickAgent.onEvent(getContext(), "10117");
                if (id == -1) {
                    onToast("请选择需要购买的座驾", Gravity.CENTER, 0, 0);
                    return;
                }
                if (isNeedVip == 0 || (isNeedVip == 1 && mUserData.getVip() > 0 && !mUserData.isVipExpired())) {
                    mPresenter.buyGoods(id, 1);
                } else {
                    needVipDialog();
                }
                break;
            default:
                break;
        }
    }

    private void needVipDialog() {
        final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(getContext());
        rxDialogSureCancel.setContent("您还不是VIP会员哦");
        rxDialogSureCancel.setSure("前往开通");
        rxDialogSureCancel.setCancel("取消");
        rxDialogSureCancel.setSureListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MyVipActivity.class));
                getActivity().finish();
                rxDialogSureCancel.dismiss();

            }
        });
        rxDialogSureCancel.setCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rxDialogSureCancel.dismiss();
            }
        });

        rxDialogSureCancel.show();
    }

    @Override
    public void onDestroyView() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public void getZjSucc(List<SysGoodsNewBean> sysGoodsNewBeen) {
        mSysGoodsNewBeen.addAll(sysGoodsNewBeen);
        mCarAdapter.setNewData(mSysGoodsNewBeen);
    }

    @Override
    public void setMountSucc() {
        mUserData.setCrtMount(id);
        mPresenter.saveUserDatas(mUserData);
    }

    @Override
    public void buyGoodsSucc() {
        SharePreferenceUtil.saveSeesionBoolean(getContext(), "isFirstBuy", false);
        final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(getContext());
        rxDialogSureCancel.setContent("购买成功！是否前往包裹查看");
        rxDialogSureCancel.setSure("前往");
        rxDialogSureCancel.setCancel("取消");
        rxDialogSureCancel.setSureListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), BegPackageActivity.class));
                rxDialogSureCancel.dismiss();
            }
        });
        rxDialogSureCancel.setCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rxDialogSureCancel.dismiss();
            }
        });

        rxDialogSureCancel.show();
    }

    @Override
    public void hbNotEnough() {
        final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(getContext());//提示弹窗
        rxDialogSureCancel.setContent("虎币不足，是否前往充值？");
        rxDialogSureCancel.setSure("前往");
        rxDialogSureCancel.setCancel("取消");
        rxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), RechargeActivity.class));
                getActivity().finish();
                rxDialogSureCancel.cancel();
            }
        });
        rxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rxDialogSureCancel.cancel();
            }
        });
        rxDialogSureCancel.show();
    }

    @Override
    public void showNotVip() {
        showNotVip();
    }

    // 数值变化
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_VALUE_CHANGE, threadMode = ThreadMode.MAIN)
    public void onReceiveValueChangePush(ValueChangePush pushData) {
        if (pushData != null) {
            if ("HB".equals(pushData.getType())) {
                hb.setText(String.valueOf(pushData.getNewVal()));
            }
        }
    }
}
