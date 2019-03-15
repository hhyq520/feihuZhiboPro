package cn.feihutv.zhibofeihu.ui.me.vip;

import android.app.Dialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.DataManager;
import cn.feihutv.zhibofeihu.data.db.model.SysVipGoodsBean;
import cn.feihutv.zhibofeihu.data.network.http.model.me.CheckAccountIdRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.CheckAccountIdResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.CheckGuardInfoRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.CheckGuardInfoResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.vip.SendVipRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.vip.SendVipResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.ValueChangePush;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.rxbus.RxBusSubscribe;
import cn.feihutv.zhibofeihu.rxbus.ThreadMode;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.me.adapter.VipGoodsAdapter;
import cn.feihutv.zhibofeihu.ui.widget.TCActivityTitle;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.ListViewDecoration;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/16 15:09
 *      desc   : 赠送VIP给好友
 *      version: 1.0
 * </pre>
 */

public class GiveFriendsActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.iv_head)
    ImageView ivHead;

    @BindView(R.id.give_rv)
    RecyclerView mRecyclerView;

    @BindView(R.id.tv_hubi)
    TextView tvHb;

    @BindView(R.id.edit_id)
    EditText editId;

    @BindView(R.id.zb_eui_edit)
    TCActivityTitle mTitle;

    private DataManager mDataManager;
    private LoadUserDataBaseResponse.UserData mUserData;
    private List<SysVipGoodsBean> mSysVipGoodsDatas = new ArrayList<>();

    private String userId;
    private String nickName;

    private VipGoodsAdapter mVipGoodsAdapter;  // VIP商品
    private int goodsid = 3;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_give_friends;
    }

    @Override
    protected void initWidget() {
        setUnBinder(ButterKnife.bind(this));

        mDataManager = FeihuZhiboApplication.getApplication().mDataManager;
        mUserData = mDataManager.getUserData();
        tvHb.setText("虎币余额：" + FHUtils.intToF(mUserData.gethB()));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(GiveFriendsActivity.this));
        mRecyclerView.addItemDecoration(new ListViewDecoration());
        mVipGoodsAdapter = new VipGoodsAdapter(mSysVipGoodsDatas);
        mRecyclerView.setAdapter(mVipGoodsAdapter);
        mVipGoodsAdapter.setOnItemClickListener(this);

        Observable<List<SysVipGoodsBean>> sysVipGoodsObservable = mDataManager.getSysVipGoodsBean();
        sysVipGoodsObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<SysVipGoodsBean>>() {
                    @Override
                    public void accept(@NonNull List<SysVipGoodsBean> sysVipGoodsBeen) throws Exception {
                        mSysVipGoodsDatas.clear();
                        for (int i = 0; i < sysVipGoodsBeen.size(); i++) {
                            if (sysVipGoodsBeen.get(i).getGoodsId().equals("1")) {
                                sysVipGoodsBeen.remove(i);
                            }
                            if (sysVipGoodsBeen.get(i).getGoodsId().equals("3")) {
                                sysVipGoodsBeen.get(i).setIsSelect(1);
                            } else {
                                sysVipGoodsBeen.get(i).setIsSelect(0);
                            }
                        }
                        mSysVipGoodsDatas.addAll(sysVipGoodsBeen);
                        mVipGoodsAdapter.setNewData(mSysVipGoodsDatas);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                });
    }

    @Override
    protected void eventOnClick() {

        mTitle.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @OnClick({R.id.btn_give, R.id.iv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
                if (getEditId().length() != 8) {
                    onToast("请输入8位有效ID");
                    return;
                }
                checkFriendsInfo();

                break;
            case R.id.btn_give:
                if (TextUtils.isEmpty(userId)) {
                    onToast("请输入您要赠送的好友的ID", Gravity.CENTER, 0, 0);
                    return;
                }
                sendVipToFriends(goodsid, userId);
                break;
            default:
                break;
        }
    }

    private void sendVipToFriends(final int goodsId, String userId) {
        showLoading();
        new CompositeDisposable().add(mDataManager
                .doSendVipCall(new SendVipRequest(goodsId, userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SendVipResponse>() {
                    @Override
                    public void accept(@NonNull SendVipResponse sendVipResponse) throws Exception {
                        if (sendVipResponse.getCode() == 0) {
                            showBuyVipSuccDialog(goodsId);
                        } else {
                            onToast("赠送失败", Gravity.CENTER, 0, 0);
                        }
                        hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        hideLoading();
                    }
                }));
    }

    /**
     * 开通成功
     */
    public void showBuyVipSuccDialog(int goodsId) {

        String vipName = FeihuZhiboApplication.getApplication().mDataManager.getSysVipNameById(String.valueOf(goodsId)).getGoodsName();
        String[] split = vipName.split("\\s+");
        String content = "已成功赠送好友" + nickName + "\n" + split[0] + "套餐";

        final Dialog pickDialog2 = new Dialog(GiveFriendsActivity.this, R.style.color_dialog);
        pickDialog2.setContentView(R.layout.dialog_buy_vip_success);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog2.getWindow();
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.CENTER);
        lp.width = (int) (display.getWidth()); //设置宽度

        pickDialog2.getWindow().setAttributes(lp);
        TextView tvContent = (TextView) pickDialog2.findViewById(R.id.tv_content);
        tvContent.setText(content);
        Button button = (Button) pickDialog2.findViewById(R.id.btn_know);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDialog2.dismiss();
            }
        });
        pickDialog2.setCanceledOnTouchOutside(true);
        pickDialog2.show();
    }

    private void checkFriendsInfo() {
        if (getEditId().equals(mUserData.getAccountId())) {
            onToast("不能输入自己的ID", Gravity.CENTER, 0, 0);
            return;
        }
        showLoading();
        new CompositeDisposable().add(mDataManager
                .doCheckAccountIdCall(new CheckAccountIdRequest(getEditId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CheckAccountIdResponse>() {
                    @Override
                    public void accept(@NonNull CheckAccountIdResponse checkAccountIdResponse) throws Exception {
                        if (checkAccountIdResponse.getCode() == 0) {
                            // 检索成功
                            CheckAccountIdResponse.CheckAccountIdResponseData checkAccountIdResponseData = checkAccountIdResponse.getCheckAccountIdResponseData();
                            TCUtils.showPicWithUrl(GiveFriendsActivity.this, ivHead, checkAccountIdResponseData.getAvatar(), R.drawable.face);
                            editId.setText(checkAccountIdResponseData.getNickname());
                            userId = checkAccountIdResponseData.getUid();
                            nickName = checkAccountIdResponseData.getNickname();
                        } else {
                            if (checkAccountIdResponse.getCode() == 4041) {
                                onToast("该帐号不存在，请重新输入", Gravity.CENTER, 0, 0);
                            } else {
                                onToast("查询失败！", Gravity.CENTER, 0, 0);
                            }
                            AppLogger.i(checkAccountIdResponse.getCode() + " " + checkAccountIdResponse.getErrMsg());
                        }
                        hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        hideLoading();
                    }
                })

        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSysVipGoodsDatas != null) {
            mSysVipGoodsDatas = null;
        }
    }

    private String getEditId() {
        return editId.getText().toString().trim();
    }

    // 数值变化
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_VALUE_CHANGE, threadMode = ThreadMode.MAIN)
    public void onReceiveValueChangePush(ValueChangePush pushData) {
        if (pushData != null) {
            if ("HB".equals(pushData.getType())) {
                tvHb.setText("虎币余额：" + FHUtils.intToF(pushData.getNewVal()));
            }
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        goodsid = Integer.parseInt(mSysVipGoodsDatas.get(position).getGoodsId());
        for (SysVipGoodsBean sysVipGoodsBean : mSysVipGoodsDatas) {
            sysVipGoodsBean.setIsSelect(0);
        }
        mSysVipGoodsDatas.get(position).setIsSelect(1);
        adapter.notifyDataSetChanged();
    }
}
