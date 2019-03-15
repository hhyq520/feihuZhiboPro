package cn.feihutv.zhibofeihu.ui.me.vip;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.liulishuo.magicprogresswidget.MagicProgressBar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.db.model.SysVipBean;
import cn.feihutv.zhibofeihu.data.db.model.SysVipGoodsBean;
import cn.feihutv.zhibofeihu.data.local.model.VipPrivilegeEntity;
import cn.feihutv.zhibofeihu.data.network.http.model.vip.BuyVipResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.push.ValueChangePush;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.rxbus.RxBusSubscribe;
import cn.feihutv.zhibofeihu.rxbus.ThreadMode;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.me.adapter.VipGoodsAdapter;
import cn.feihutv.zhibofeihu.ui.me.adapter.VipPrivilegeAdapter;
import cn.feihutv.zhibofeihu.ui.me.recharge.RechargeActivity;
import cn.feihutv.zhibofeihu.ui.widget.ExStaggeredGridLayoutManager;
import cn.feihutv.zhibofeihu.ui.widget.TCActivityTitle;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.ListViewDecoration;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.feihutv.zhibofeihu.utils.TimeUtil;

/**
 * <pre>
 *     @author : liwen.chen
 *     time   :
 *     desc   : 我的---VIP
 *     version: 1.0
 * </pre>
 */
public class MyVipActivity extends BaseActivity implements MyVipMvpView, BaseQuickAdapter.OnItemClickListener {

    @Inject
    MyVipMvpPresenter<MyVipMvpView> mPresenter;

    @BindView(R.id.vip_back)
    TCActivityTitle mTitle;

    // 虎币
    @BindView(R.id.tv_hb)
    TextView tvHb;

    // VIP商品RecyclerView
    @BindView(R.id.vip_rv)
    RecyclerView mRvVipGoods;

    // VIP特权RecyclerView
    @BindView(R.id.vip_rv2)
    RecyclerView mRvTqVip;

    // 尚未开通VIP
    @BindView(R.id.tv_not_open)
    TextView tvNotOpen;

    // 昵称
    @BindView(R.id.tv_nick_vip)
    TextView tvNickVip;

    // 头像
    @BindView(R.id.iv_head)
    ImageView ivHead;

    // VIP等级
    @BindView(R.id.iv_level)
    ImageView ivLevel;

    // 当前VIP等级
    @BindView(R.id.iv_level_now)
    ImageView ivLevelNow;

    // 下级VIP
    @BindView(R.id.iv_level_next)
    ImageView ivLevelNext;

    @BindView(R.id.rl_vip)
    RelativeLayout rlVip;

    // 开通会员按钮
    @BindView(R.id.btn_open)
    Button btnOpen;

    // vip有效时间
    @BindView(R.id.tv_time)
    TextView tvTime;

    // 到期提示
    @BindView(R.id.tv_expire)
    TextView tvExpire;
    @BindView(R.id.tv_record)
    TextView tv_record;
    @BindView(R.id.tv_vip_lessexprience)
    TextView tvVipLessExp;

    @BindView(R.id.vip_mpb)
    MagicProgressBar mProgressBar;

    private int goodsId = 3;
    private boolean isFromroom = false;
    private static final long REMAINING_DAYS = 7 * 24 * 60 * 60 * 1000L;

    private LoadUserDataBaseResponse.UserData mUserData;

    private VipGoodsAdapter mVipGoodsAdapter;  // VIP商品

    private VipPrivilegeAdapter mPrivilegeAdapter;

    private List<VipPrivilegeEntity> mPrivilegeEntities = new ArrayList<>();

    private List<SysVipGoodsBean> mSysVipGoodsBeen = new ArrayList<>();

    private List<SysVipBean> mSysVipBeen = new ArrayList<>();

    private int[] strName = {R.string.vip1_name, R.string.vip2_name, R.string.vip3_name, R.string.vip4_name, R.string.vip5_name, R.string.vip6_name, R.string.vip7_name, R.string.vip8_name, R.string.vip9_name, R.string.vip10_name};

    private int[] resId = {R.drawable.icon_car_tq, R.drawable.icon_badge_tq, R.drawable.icon_getinto_tq, R.drawable.icon_chat_tq, R.drawable.icon_game_tq, R.drawable.icon_gift_tq, R.drawable.icon_package_tq, R.drawable.icon_mv_tq, R.drawable.icon_defense_tq, R.drawable.icon_qucik_tq};

    private int[] descId = {R.string.vip1_desc, R.string.vip2_desc, R.string.vip3_desc, R.string.vip4_desc, R.string.vip5_desc, R.string.vip6_desc, R.string.vip7_desc, R.string.vip8_desc, R.string.vip9_desc, R.string.vip10_desc};

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_vip;
    }


    @Override
    protected void initWidget() {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        //persenter调用初始化
        mPresenter.onAttach(MyVipActivity.this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        Intent intent = getIntent();
        if (intent != null) {
            isFromroom = intent.getBooleanExtra("isFromRoom", false);
        }
        if (isFromroom) {
            mTitle.setMoreText("");
            tv_record.setVisibility(View.GONE);
        }
        mUserData = mPresenter.getUserData();


        mPresenter.getSysVipGoodsBean();
        mPresenter.getSysVipBean();

        mRvVipGoods.setLayoutManager(linearLayoutManager);
        mRvVipGoods.addItemDecoration(new ListViewDecoration());
        mVipGoodsAdapter = new VipGoodsAdapter(mSysVipGoodsBeen);
        mRvVipGoods.setAdapter(mVipGoodsAdapter);
        mVipGoodsAdapter.setOnItemClickListener(this);
        initUserInfo();

        initPrivilegeDatas();


    }

    // 初始化用户信息
    private void initUserInfo() {
        tvHb.setText(FHUtils.intToF(mUserData.gethB()));
        tvNickVip.setText(mUserData.getNickName());
        TCUtils.showPicWithUrl(MyVipActivity.this, ivHead, mUserData.getHeadUrl(), R.drawable.face);
        if (mUserData.getVip() > 0) {
            rlVip.setVisibility(View.VISIBLE);
            tvNotOpen.setVisibility(View.GONE);
            ivLevel.setVisibility(View.VISIBLE);
            TCUtils.showVipLevelWithUrl(MyVipActivity.this, ivLevel, mUserData.getVip(), mUserData.isVipExpired());
            TCUtils.showVipLevelWithUrl(MyVipActivity.this, ivLevelNow, mUserData.getVip(), mUserData.isVipExpired());
            if (mUserData.getVip() == 6) {
                ivLevelNext.setVisibility(View.GONE);
            } else {
                TCUtils.showVipLevelWithUrl(MyVipActivity.this, ivLevelNext, mUserData.getVip() + 1, mUserData.isVipExpired());
            }

            if (mUserData.isVipExpired()) {
                // VIP已过期
                btnOpen.setText("开通会员");
                tvTime.setText("您的VIP已过期");
                tvTime.setTextColor(getResources().getColor(R.color.btn_sure_forbidden));
                tvExpire.setVisibility(View.GONE);
            } else {
                // VIP未过期
                btnOpen.setText("续费会员");
                tvTime.setText("有效期至: " + TimeUtil.getDayTime(mUserData.getVipExpireTime() * 1000));
                tvTime.setTextColor(getResources().getColor(R.color.app_white));
                AppLogger.i("TIME", System.currentTimeMillis() + " " + mUserData.getVipExpireTime() * 1000);
                if ((System.currentTimeMillis() - mUserData.getVipExpireTime() * 1000) > REMAINING_DAYS) {
                    tvExpire.setVisibility(View.VISIBLE);
                }
            }
        } else {
            rlVip.setVisibility(View.GONE);
            tvNotOpen.setVisibility(View.VISIBLE);
            ivLevel.setVisibility(View.GONE);
            btnOpen.setText("开通会员");
        }
    }

    // 初始化VIP特权说明
    private void initPrivilegeDatas() {


        mRvTqVip.setLayoutManager(new ExStaggeredGridLayoutManager(5, ExStaggeredGridLayoutManager.VERTICAL) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mPrivilegeAdapter = new VipPrivilegeAdapter(mPrivilegeEntities);
        mRvTqVip.setAdapter(mPrivilegeAdapter);

        mPrivilegeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                VipPrivilegeEntity vipPrivilegeEntity = mPrivilegeEntities.get(position);
                if (mUserData.getVip() > 0 && !mUserData.isVipExpired()) {
                    showReminderDialog(vipPrivilegeEntity.getResId(), vipPrivilegeEntity.getNameId(), vipPrivilegeEntity.getDescId(), "续费会员");
                } else {
                    showReminderDialog(vipPrivilegeEntity.getResId(), vipPrivilegeEntity.getNameId(), vipPrivilegeEntity.getDescId(), "开通会员");
                }

            }
        });
        for (int i = 0; i < strName.length; i++) {
            VipPrivilegeEntity vipPrivilegeEntity = new VipPrivilegeEntity();
            vipPrivilegeEntity.setNameId(strName[i]);
            vipPrivilegeEntity.setResId(resId[i]);
            vipPrivilegeEntity.setDescId(descId[i]);
            mPrivilegeEntities.add(vipPrivilegeEntity);
        }
        mPrivilegeAdapter.setNewData(mPrivilegeEntities);
    }

    // 查询SysVipGoods表成功
    @Override
    public void getSysVipGoodsSucc(List<SysVipGoodsBean> sysVipGoodsBeanList) {
        mSysVipGoodsBeen.clear();
        if (mUserData.getVip() > 0) {
            // 开通过VIP 隐藏首次开通
            for (SysVipGoodsBean sysVipGoodsBean : sysVipGoodsBeanList) {
                if (!sysVipGoodsBean.getGoodsId().equals("1")) {
                    mSysVipGoodsBeen.add(sysVipGoodsBean);
                }
            }
        } else {
            // 未开通过，显示首次开通 隐藏月卡
            for (SysVipGoodsBean sysVipGoodsBean : sysVipGoodsBeanList) {
                if (!sysVipGoodsBean.getGoodsId().equals("2")) {
                    mSysVipGoodsBeen.add(sysVipGoodsBean);
                }
            }
        }
        for (int i = 0; i < mSysVipGoodsBeen.size(); i++) {
            if (mSysVipGoodsBeen.get(i).getGoodsId().equals("3")) {
                mSysVipGoodsBeen.get(i).setIsSelect(1);
            } else {
                mSysVipGoodsBeen.get(i).setIsSelect(0);
            }
        }
        mVipGoodsAdapter.setNewData(mSysVipGoodsBeen);
    }

    // 查询SysVip表成功
    @Override
    public void getSysVipSucc(List<SysVipBean> sysVipBeanList) {
        mSysVipBeen.addAll(sysVipBeanList);
        if (mUserData.getVip() > 0) {
            if (mUserData.getVip() < 6) {
                int needCZZ = mSysVipBeen.get(mUserData.getVip()).getNeedCzz() - mUserData.getVipCZZ();
                mProgressBar.setSmoothPercent(mUserData.getVipCZZ() * 1.0f / mSysVipBeen.get(mUserData.getVip()).getNeedCzz());
                tvVipLessExp.setText("还差" + needCZZ + "成长值升级");
            } else {
                tvVipLessExp.setText("更高等级，敬请期待");
                mProgressBar.setSmoothPercent(1);
            }
        }
    }

    // 购买成功
    @Override
    public void buyVipSucc(BuyVipResponse.BuyVipData vipData, int goodsId) {
        showBuyVipSuccDialog(goodsId);
        rlVip.setVisibility(View.VISIBLE);
        tvNotOpen.setVisibility(View.GONE);
        ivLevel.setVisibility(View.VISIBLE);
        TCUtils.showVipLevelWithUrl(MyVipActivity.this, ivLevel, vipData.getVip(), false);
        TCUtils.showVipLevelWithUrl(MyVipActivity.this, ivLevelNow, vipData.getVip(), false);
        if (mUserData.getVip() == 6) {
            ivLevelNext.setVisibility(View.GONE);
        } else {
            TCUtils.showVipLevelWithUrl(MyVipActivity.this, ivLevelNext, vipData.getVip() + 1, false);
        }
        tvTime.setText("有效期至: " + TimeUtil.getDayTime(vipData.getVipExpireTime() * 1000));
        tvTime.setTextColor(getResources().getColor(R.color.app_white));
        btnOpen.setText("续费会员");

        if (mUserData.getVip() < 6) {
            int needCZZ = mSysVipBeen.get(vipData.getVip()).getNeedCzz() - vipData.getVipCZZ();
            mProgressBar.setSmoothPercent(vipData.getVipCZZ() * 1.0f / mSysVipBeen.get(vipData.getVip()).getNeedCzz());
            tvVipLessExp.setText("还差" + needCZZ + "成长值升级");
        } else {
            tvVipLessExp.setText("更高等级，敬请期待");
            mProgressBar.setSmoothPercent(1);
        }

        mUserData.setVip(vipData.getVip());
        mUserData.setVipCZZ(vipData.getVipCZZ());
        mUserData.setVipExpireTime(vipData.getVipExpireTime());
        mUserData.setVipExpired(false);
        mPresenter.saveUserDatas(mUserData);
    }

    // 购买失败， 虎币不足
    @Override
    public void failToBuy() {
        showReminderDialog(R.drawable.pic_remind, "支付失败", "账户余额不足", "前往充值");
    }

    @OnClick({R.id.tv_record, R.id.btn_open, R.id.btn_quick})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_record:
                startActivity(new Intent(this, ReceAndSendRecordActivity.class));
                break;
            case R.id.btn_open:
                mPresenter.buyVip(goodsId);
                break;
            case R.id.btn_quick:
                startActivity(new Intent(MyVipActivity.this, QuickUpgradeActivity.class));
                break;
            default:
                break;
        }
    }

    /**
     * 开通成功
     */
    public void showBuyVipSuccDialog(int goodsId) {

        String vipName = FeihuZhiboApplication.getApplication().mDataManager.getSysVipNameById(String.valueOf(goodsId)).getGoodsName();
        String[] split = vipName.split("\\s+");
        String content = "成功开通" + split[0] + "套餐，\n赶紧去向小伙伴们炫耀下吧!";

        final Dialog pickDialog2 = new Dialog(MyVipActivity.this, R.style.color_dialog);
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

    /**
     * 提示框
     */
    public void showReminderDialog(int imgResId, String title, String content, String btnContent) {
        final Dialog pickDialog2 = new Dialog(MyVipActivity.this, R.style.color_dialog);
        pickDialog2.setContentView(R.layout.dialog_vip_recommend);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog2.getWindow();
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.CENTER);
        lp.width = (int) (display.getWidth()); //设置宽度

        pickDialog2.getWindow().setAttributes(lp);
        ImageView iv_remind = (ImageView) pickDialog2.findViewById(R.id.iv_remind);
        ImageView ivClose = (ImageView) pickDialog2.findViewById(R.id.iv_close);
        TextView tvNoHb = (TextView) pickDialog2.findViewById(R.id.tv_title);
        TextView tvDesc = (TextView) pickDialog2.findViewById(R.id.tv_desc);
        Button button = (Button) pickDialog2.findViewById(R.id.btn_sure);
        iv_remind.setImageResource(imgResId);
        tvDesc.setText(content);
        button.setText(btnContent);
        tvNoHb.setText(title);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyVipActivity.this, RechargeActivity.class));
                pickDialog2.dismiss();
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDialog2.dismiss();
            }
        });
        pickDialog2.setCanceledOnTouchOutside(false);
        pickDialog2.show();
    }

    /**
     * 提示框
     */
    public void showReminderDialog(int imgResId, int titleResId, int contentResId, String btnContent) {
        final Dialog pickDialog2 = new Dialog(MyVipActivity.this, R.style.color_dialog);
        pickDialog2.setContentView(R.layout.dialog_vip_recommend);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog2.getWindow();
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.CENTER);
        lp.width = (int) (display.getWidth()); //设置宽度

        pickDialog2.getWindow().setAttributes(lp);
        ImageView iv_remind = (ImageView) pickDialog2.findViewById(R.id.iv_remind);
        ImageView ivClose = (ImageView) pickDialog2.findViewById(R.id.iv_close);
        TextView tvNoHb = (TextView) pickDialog2.findViewById(R.id.tv_title);
        TextView tvDesc = (TextView) pickDialog2.findViewById(R.id.tv_desc);
        Button button = (Button) pickDialog2.findViewById(R.id.btn_sure);
        iv_remind.setImageResource(imgResId);
        tvDesc.setText(contentResId);
        button.setText(btnContent);
        tvNoHb.setText(titleResId);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.buyVip(goodsId);
                pickDialog2.dismiss();
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDialog2.dismiss();
            }
        });
        pickDialog2.setCanceledOnTouchOutside(false);
        pickDialog2.show();
    }


    @Override
    protected void eventOnClick() {
        mTitle.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTitle.setMoreTextColor(getResources().getColor(R.color.btn_sure_forbidden));
        mTitle.setMoreListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFromroom) {
                    startActivity(new Intent(MyVipActivity.this, GiveFriendsActivity.class));
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        goodsId = Integer.parseInt(mSysVipGoodsBeen.get(position).getGoodsId());
        for (SysVipGoodsBean sysVipGoodsBean : mSysVipGoodsBeen) {
            sysVipGoodsBean.setIsSelect(0);
        }
        mSysVipGoodsBeen.get(position).setIsSelect(1);
        adapter.notifyDataSetChanged();
    }


    // 数值变化
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_VALUE_CHANGE, threadMode = ThreadMode.MAIN)
    public void onReceiveValueChangePush(ValueChangePush pushData) {
        if (pushData != null) {
            mUserData = mPresenter.getUserData();
            if ("HB".equals(pushData.getType())) {
                tvHb.setText(FHUtils.intToF(pushData.getNewVal()));
                mUserData.sethB(pushData.getNewVal());
                mPresenter.saveUserDatas(mUserData);
            }
        }
    }
}
