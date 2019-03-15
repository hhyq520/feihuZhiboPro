package cn.feihutv.zhibofeihu.ui.me.shop;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LoadLiangSearchKeyResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LoadShopAccountIdListResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.ValueChangePush;
import cn.feihutv.zhibofeihu.rxbus.RxBus;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.rxbus.RxBusSubscribe;
import cn.feihutv.zhibofeihu.rxbus.ThreadMode;
import cn.feihutv.zhibofeihu.ui.base.BaseFragment;
import cn.feihutv.zhibofeihu.ui.me.ItemOnClick;
import cn.feihutv.zhibofeihu.ui.me.adapter.BeautiAdapter;
import cn.feihutv.zhibofeihu.ui.me.adapter.SearchLiangAdapter;
import cn.feihutv.zhibofeihu.ui.me.encash.ChangeZFActivty;
import cn.feihutv.zhibofeihu.ui.me.encash.IncomeActivity;
import cn.feihutv.zhibofeihu.ui.me.recharge.RechargeActivity;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.ListViewDecoration;
import cn.feihutv.zhibofeihu.utils.weiget.dialog.RxDialog;
import cn.feihutv.zhibofeihu.utils.weiget.dialog.RxDialogSureCancel;

/**
 * <pre>
 *     author : liwen.chen
 *     time   : 2017/
 *     desc   : 靓号
 *     version: 1.0
 * </pre>
 */
public class BeautiFragment extends BaseFragment implements BeautiMvpView, ItemOnClick {

    @Inject
    BeautiMvpPresenter<BeautiMvpView> mPresenter;

    @BindView(R.id.tv_hubi)
    TextView tvHubi;

    @BindView(R.id.beauti_gridView)
    GridView beautiGridView;

    @BindView(R.id.rv_liang)
    RecyclerView rvLiang;

    @BindView(R.id.beauti_ll)
    LinearLayout beautill;

    @BindView(R.id.choose_btn)
    Button chooseBtn;

    private BeautiAdapter mBeautiAdapter;

    private boolean isPrepared;

    private SearchLiangAdapter mLiangAdapter;

    private boolean isHide = true;

    private boolean isFirst = true;

    private int liangId = 0;

    private List<LoadShopAccountIdListResponse.LoadShopAccountIdListResponseData> mAccountIdListResponseDatas = new ArrayList<>();
    private List<LoadLiangSearchKeyResponse.LoadLiangSearchKeyResponseData> mLiangSearchKeyResponseDatas = new ArrayList<>();


    public static BeautiFragment newInstance() {
        Bundle args = new Bundle();
        BeautiFragment fragment = new BeautiFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_beauti;
    }


    @Override
    protected void initWidget(View view) {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this, view));

        //Presenter调用初始化
        mPresenter.onAttach(BeautiFragment.this);

        isPrepared = true;

        tvHubi.setText(FHUtils.intToF(mPresenter.getUserData().gethB()));
        mBeautiAdapter = new BeautiAdapter(getContext());
        beautiGridView.setAdapter(mBeautiAdapter);
        mBeautiAdapter.setItemOnClick(this);


        rvLiang.setLayoutManager(new LinearLayoutManager(getContext()));
        rvLiang.addItemDecoration(new ListViewDecoration());
        mLiangAdapter = new SearchLiangAdapter(mLiangSearchKeyResponseDatas);
        rvLiang.setAdapter(mLiangAdapter);

        mLiangAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                liangId = mLiangSearchKeyResponseDatas.get(position).getLiangId();
                chooseBtn.setText(String.valueOf(mLiangSearchKeyResponseDatas.get(position).getPriceStart()));
                mPresenter.loadShopAccountIdList(liangId);
                beautill.setVisibility(View.GONE);
                isHide = true;
            }
        });

    }

    @Override
    protected void lazyLoad() {
        if (isPrepared && isVisible) {
            mPresenter.loadShopAccountIdList(liangId);
        }
    }


    @Override
    public void onDestroyView() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroyView();
    }

    @OnClick({R.id.chongzhi, R.id.refresh_btn, R.id.choose_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.chongzhi:
                Intent intent = new Intent(getContext(), RechargeActivity.class);
                intent.putExtra("fromWhere", "靓号页面");
                startActivity(intent);
                break;
            case R.id.refresh_btn:
                mPresenter.loadShopAccountIdList(liangId);
                break;
            case R.id.choose_btn:
                if (isHide) {
                    beautill.setVisibility(View.VISIBLE);
                    if (isFirst) {
                        mPresenter.loadLiangSearchKey();
                    }
                    isHide = false;
                } else {
                    beautill.setVisibility(View.GONE);
                    isHide = true;
                }
                break;
            default:
                break;
        }
    }

    // 数值变化
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_VALUE_CHANGE, threadMode = ThreadMode.MAIN)
    public void onReceiveValueChangePush(ValueChangePush pushData) {
        if (pushData != null) {
            if ("HB".equals(pushData.getType())) {
                tvHubi.setText(String.valueOf(pushData.getNewVal()));
            }
        }
    }

    @Override
    public void getDatas(List<LoadShopAccountIdListResponse.LoadShopAccountIdListResponseData> loadShopAccountIdListResponseDatas) {
        mAccountIdListResponseDatas.clear();
        mAccountIdListResponseDatas.addAll(loadShopAccountIdListResponseDatas);
        mBeautiAdapter.setDatas(mAccountIdListResponseDatas);
    }

    @Override
    public void showNotEnough() {
        final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(getContext());//提示弹窗
        rxDialogSureCancel.setContent("虎币不足，是否前往充值？");
        rxDialogSureCancel.setSure("前往");
        rxDialogSureCancel.setCancel("取消");
        rxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), RechargeActivity.class));
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
    public void buySucc(String accountId) {
        mPresenter.saveaccountId(accountId);
        RxBus.get().send(RxBusCode.RX_BUS_CODE_NOTICE_ACCOUNTID_CHANGE, accountId);
        showDialog();
        mPresenter.loadShopAccountIdList(liangId);
    }

    @Override
    public void succLiangSearch(List<LoadLiangSearchKeyResponse.LoadLiangSearchKeyResponseData> loadLiangSearchKeyResponseDatas) {
        mLiangSearchKeyResponseDatas.clear();
        isFirst = false;
        mLiangSearchKeyResponseDatas.addAll(loadLiangSearchKeyResponseDatas);
        mLiangAdapter.setNewData(mLiangSearchKeyResponseDatas);
    }

    private void showDialog() {
        final Dialog pickDialog2 = new Dialog(getContext(), R.style.color_dialog);
        pickDialog2.setContentView(R.layout.dialog_bind_succ);

        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog2.getWindow();
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.CENTER);
        lp.width = (int) (display.getWidth()); //设置宽度

        TextView tvTitle = pickDialog2.findViewById(R.id.tv_pop);
        tvTitle.setText("购买成功！\n您的ID已更换为您购买的靓号ID");

        pickDialog2.getWindow().setAttributes(lp);

        Button button = (Button) pickDialog2.findViewById(R.id.btn_pop_renzheng);
        button.setText("确定");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDialog2.dismiss();
            }
        });
        pickDialog2.setCanceledOnTouchOutside(false);
        pickDialog2.show();
    }

    @Override
    public void click(int position) {
        mPresenter.buyShopAccountId(mAccountIdListResponseDatas.get(position).getAccountId());
    }
}
