package cn.feihutv.zhibofeihu.ui.widget;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.db.model.SysGuardGoodsBean;
import cn.feihutv.zhibofeihu.data.network.http.model.me.BuyGuardRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.BuyGuardResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.CheckGuardInfoRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.CheckGuardInfoResponse;
import cn.feihutv.zhibofeihu.ui.me.recharge.RechargeActivity;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.feihutv.zhibofeihu.utils.TimeUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by huanghao on 2017/11/16.
 */

public class GuardDialogFragment extends DialogFragment {

    @BindView(R.id.img_user)
    ImageView imgUser;
    @BindView(R.id.img_zhubo)
    ImageView imgZhubo;
    @BindView(R.id.iv_yinhu)
    ImageView ivYinhu;
    @BindView(R.id.tv_yinhu)
    TextView tvYinhu;
    @BindView(R.id.iv_jinhu)
    ImageView ivJinhu;
    @BindView(R.id.tv_jinhu)
    TextView tvJinhu;
    @BindView(R.id.tv_card1)
    TextView tvCard1;
    @BindView(R.id.tv_xiaolaba1)
    TextView tvXiaolaba1;
    @BindView(R.id.ll_month_card)
    LinearLayout llMonthCard;
    @BindView(R.id.tv_card2)
    TextView tvCard2;
    @BindView(R.id.tv_xiaolaba2)
    TextView tvXiaolaba2;
    @BindView(R.id.ll_year_card)
    LinearLayout llYearCard;
    @BindView(R.id.rl)
    RelativeLayout rl;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv_now_price)
    TextView tvNowPrice;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.btn_kait)
    Button btnKait;
    @BindView(R.id.iv8)
    ImageView iv8;
    @BindView(R.id.iv1)
    ImageView iv1;
    @BindView(R.id.iv2)
    ImageView iv2;
    @BindView(R.id.iv7)
    ImageView iv7;
    @BindView(R.id.iv3)
    ImageView iv3;
    @BindView(R.id.iv6)
    ImageView iv6;
    @BindView(R.id.iv4)
    ImageView iv4;
    @BindView(R.id.iv5)
    ImageView iv5;
    @BindView(R.id.iv_bottom)
    ImageView ivBottom;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.user_nickname)
    TextView userNickname;
    @BindView(R.id.zhubo_nickname)
    TextView zhuboNickname;
    @BindView(R.id.title_top)
    TCActivityTitle titleTop;
    private String headUrl;
    private String nickname;
    private String userId;
    private int huType = 2; // 守护类型 1 银虎 2 金虎
    private int goodsId = 1; // 商品id 1 月卡  2 年卡
    private int guardType = 0; //0未守护，1银虎守护，2金虎守护
    public static final long MONTHTIME = 30L * 24 * 60 * 60 * 1000;  //30天的毫秒数
    public static final long YEARTIME = 365L * 24 * 60 * 60 * 1000;  //365天的毫秒数
    private long currentTime = System.currentTimeMillis();
    private List<SysGuardGoodsBean> mSysGuardGoodsBeen = new ArrayList<>();
    private boolean isVisiable = true;
    private String accountId;
    public static GuardDialogFragment newInstance(String headUrl, String nickname, String userId,String accountId) {
        Bundle args = new Bundle();
        GuardDialogFragment fragment = new GuardDialogFragment();
        fragment.setArguments(args);
        fragment.headUrl = headUrl;
        fragment.nickname = nickname;
        fragment.accountId = accountId;
        fragment.userId = userId;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.cb_dialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guard_dialog, container);
        ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    private void initView(View view) {

        tvTime.setText(TimeUtil.getDayTime((currentTime + MONTHTIME)));
        tvXiaolaba1.setText("(赠送3个小喇叭)");
        tvXiaolaba2.setText("(赠送38个小喇叭)");

        TCUtils.showPicWithUrl(getContext(), imgUser, SharePreferenceUtil.getSession(getContext(), "PREF_KEY_HEADURL"), R.drawable.face);
        userNickname.setText(SharePreferenceUtil.getSession(getContext(), "PREF_KEY_NICKNAME"));
        TCUtils.showPicWithUrl(getContext(), imgZhubo, headUrl, R.drawable.face);
        zhuboNickname.setText(nickname);
        titleTop.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .getSysGuardGoodsBean()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<SysGuardGoodsBean>>() {
                    @Override
                    public void accept(@NonNull List<SysGuardGoodsBean> sysGuardGoodsBeen) throws Exception {
                        mSysGuardGoodsBeen.clear();
                        if (sysGuardGoodsBeen.size() > 0) {
                            mSysGuardGoodsBeen.addAll(sysGuardGoodsBeen);
                            tvCard1.setText(sysGuardGoodsBeen.get(0).getGoodsName());
                            tvCard2.setText(sysGuardGoodsBeen.get(1).getGoodsName());
                            try {
                                JSONObject jsonObject = new JSONObject(sysGuardGoodsBeen.get(0).getJinhu().toString());
                                int hb = jsonObject.getInt("hb");
                                tvNowPrice.setText(String.valueOf(hb));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));
        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doCheckGuardInfoCall(new CheckGuardInfoRequest(accountId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CheckGuardInfoResponse>() {
                    @Override
                    public void accept(@NonNull CheckGuardInfoResponse checkGuardInfoResponse) throws Exception {
                        if (checkGuardInfoResponse.getCode() == 0) {
                            CheckGuardInfoResponse.CheckGuardInfoResponseData checkGuardInfoResponseData=checkGuardInfoResponse.getCheckGuardInfoResponseData();
                            currentTime = checkGuardInfoResponseData.getExpiredTime() * 1000;
                            if (goodsId == 1) {
                                tvTime.setText(TimeUtil.getDayTime((currentTime + MONTHTIME)));
                            } else {
                                tvTime.setText(TimeUtil.getDayTime((currentTime + YEARTIME)));
                            }
                            guardType = checkGuardInfoResponseData.getGuardType();
                        } else {
                            AppLogger.e(checkGuardInfoResponse.getErrMsg());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                })

        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.btn_kait,R.id.iv_bottom,R.id.iv_yinhu, R.id.iv_jinhu, R.id.ll_month_card, R.id.ll_year_card})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_yinhu:
                huType = 1;
                ivYinhu.setBackgroundResource(R.drawable.shape_shop_guard);
                tvYinhu.setTextColor(getResources().getColor(R.color.btn_sure_forbidden));
                ivJinhu.setBackgroundResource(R.drawable.shape_shop_guard_unselect);
                tvJinhu.setTextColor(getResources().getColor(R.color.textNormal));
                initDatas(mSysGuardGoodsBeen);
                break;
            case R.id.iv_jinhu:
                huType = 2;
                ivYinhu.setBackgroundResource(R.drawable.shape_shop_guard_unselect);
                tvYinhu.setTextColor(getResources().getColor(R.color.textNormal));
                ivJinhu.setBackgroundResource(R.drawable.shape_shop_guard);
                tvJinhu.setTextColor(getResources().getColor(R.color.btn_sure_forbidden));
                initDatas(mSysGuardGoodsBeen);
                break;
            case R.id.ll_month_card:
                goodsId = 1;
                llMonthCard.setBackgroundResource(R.drawable.shape_shop_guard_select);
                tvCard1.setTextColor(getResources().getColor(R.color.app_white));
                llYearCard.setBackgroundResource(R.drawable.shape_shop_guard);
                tvCard2.setTextColor(getResources().getColor(R.color.textNormal));
                tvTime.setText(TimeUtil.getDayTime((currentTime + MONTHTIME)));
                initDatas(mSysGuardGoodsBeen);
                break;
            case R.id.ll_year_card:
                goodsId = 2;
                llMonthCard.setBackgroundResource(R.drawable.shape_shop_guard);
                tvCard1.setTextColor(getResources().getColor(R.color.textNormal));
                llYearCard.setBackgroundResource(R.drawable.shape_shop_guard_select);
                tvCard2.setTextColor(getResources().getColor(R.color.app_white));
                tvTime.setText(TimeUtil.getDayTime((currentTime + YEARTIME)));
                initDatas(mSysGuardGoodsBeen);
                break;
            case R.id.iv_bottom:
                if (isVisiable) {
                    ivBottom.setImageResource(R.drawable.icon_top);
                    llBottom.setVisibility(View.GONE);
                    isVisiable = false;
                } else {
                    ivBottom.setImageResource(R.drawable.icon_bottom);
                    llBottom.setVisibility(View.VISIBLE);
                    isVisiable = true;
                }
                break;
            case R.id.btn_kait:
                if (guardType == 2 && huType == 1) {
                    String title = "已是金守护哦";
                    String content = "若开通银虎，剩余的金虎时长将直接累计到银虎守护的时长上";
                    showSureDialog(title, content);
                } else if (guardType == 1 && huType == 2) {
                    String title = "已是银守护哦";
                    String content = "若开通金虎，剩余的银虎时长将直接累计到金虎守护的时长上";
                    showSureDialog(title, content);
                } else {
                    buyGuard();
                }
            default:
                break;
        }
    }

    private void showSureDialog(String title, String content) {
        final Dialog pickDialog2 = new Dialog(getContext(), R.style.color_dialog);
        pickDialog2.setContentView(R.layout.dialog_shop_guard_sure);

        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog2.getWindow();
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.CENTER);
        lp.width = (int) (display.getWidth()); //设置宽度

        pickDialog2.getWindow().setAttributes(lp);
        TextView tvTitle = (TextView) pickDialog2.findViewById(R.id.tv_pop);
        TextView tvContent = (TextView) pickDialog2.findViewById(R.id.tv);
        ImageView ivClose = (ImageView) pickDialog2.findViewById(R.id.iv_close);
        tvTitle.setText(title);
        tvContent.setText(content);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDialog2.dismiss();
            }
        });
        Button button = (Button) pickDialog2.findViewById(R.id.btn_pop_renzheng);
        button.setText("开通");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buyGuard();
                pickDialog2.dismiss();
            }
        });
        pickDialog2.setCanceledOnTouchOutside(false);
        pickDialog2.show();
    }

    private void buyGuard(){
        if(userId.equals(SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(),"PREF_KEY_USERID"))){
            FHUtils.showToast("不能自己守护自己哦~ ");
            return;
        }
        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doBuyGuardCall(new BuyGuardRequest(userId, goodsId, huType))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BuyGuardResponse>() {
                    @Override
                    public void accept(@NonNull BuyGuardResponse buyGuardResponse) throws Exception {
                        if (buyGuardResponse.getCode() == 0) {
                            showGuardSuccess();
                            SharePreferenceUtil.saveSeesionBoolean(FeihuZhiboApplication.getApplication(),"meisGuard",true);
                        } else {
                            if (buyGuardResponse.getCode() == 4202) {
                                // 虎币不足
                               showHbNotEnoughDialog();
                            } else if (buyGuardResponse.getCode() == 4041) {
                                FHUtils.showToast("用户不存在");
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));
    }

    private void showGuardSuccess(){
        final Dialog pickDialog2 = new Dialog(getContext(), R.style.color_dialog);
        pickDialog2.setContentView(R.layout.dialog_shop_guard_success);

        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog2.getWindow();
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.CENTER);
        lp.width = (int) (display.getWidth()); //设置宽度

        pickDialog2.getWindow().setAttributes(lp);
        Button button = (Button) pickDialog2.findViewById(R.id.btn_pop_renzheng);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDialog2.dismiss();
            }
        });
        pickDialog2.setCanceledOnTouchOutside(false);
        pickDialog2.show();
    }

    /**
     * 显示虎币不足的dialog
     */
    public void showHbNotEnoughDialog() {
        final Dialog pickDialog2 = new Dialog(getContext(), R.style.color_dialog);
        pickDialog2.setContentView(R.layout.dialog_shop_guard_nohb);

        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog2.getWindow();
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.CENTER);
        lp.width = (int) (display.getWidth()-TCUtils.dip2px(getContext(),60)); //设置宽度

        pickDialog2.getWindow().setAttributes(lp);
        ImageView ivClose = (ImageView) pickDialog2.findViewById(R.id.iv_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDialog2.dismiss();
            }
        });
        Button button = (Button) pickDialog2.findViewById(R.id.btn_pop_renzheng);
        button.setText("前往充值");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), RechargeActivity.class));
                pickDialog2.dismiss();
            }
        });
        pickDialog2.setCanceledOnTouchOutside(false);
        pickDialog2.show();
    }

    private void initDatas(List<SysGuardGoodsBean> mSysGuardGoodsBeen) {
        if (mSysGuardGoodsBeen.size() == 0) {
            return;
        }
        try {
            String yinhu1 = mSysGuardGoodsBeen.get(0).getYinhu();
            String jinhu1 = mSysGuardGoodsBeen.get(0).getJinhu();
            String yinhu2 = mSysGuardGoodsBeen.get(1).getYinhu();
            String jinhu2 = mSysGuardGoodsBeen.get(1).getJinhu();
            JSONObject jbyh1 = new JSONObject(yinhu1); // 月卡 银虎
            JSONObject jbjh1 = new JSONObject(jinhu1);  // 月卡 金虎
            JSONObject jbyh2 = new JSONObject(yinhu2); // 年卡 银虎
            JSONObject jbjh2 = new JSONObject(jinhu2);  // 年卡 金虎
            int yhlb1 = jbyh1.getInt("xiaolaba"); // 银虎 月卡 喇叭
            int jhlb1 = jbjh1.getInt("xiaolaba"); // 金虎 月卡 喇叭
            int yhlb2 = jbyh2.getInt("xiaolaba"); // 银虎 年卡 喇叭
            int jhlb2 = jbjh2.getInt("xiaolaba"); // 金虎 年卡 喇叭
            if (huType == 1) {
                // 银虎
                tvXiaolaba1.setText("(赠送" + yhlb1 + "个小喇叭)");
                tvXiaolaba2.setText("(赠送" + yhlb2 + "个小喇叭)");
            } else if (huType == 2) {
                // 金虎
                tvXiaolaba1.setText("(赠送" + jhlb1 + "个小喇叭)");
                tvXiaolaba2.setText("(赠送" + jhlb2 + "个小喇叭)");
            }

            int yhhb1 = jbyh1.getInt("hb"); // 月卡：银虎  虎币
            int jhhb1 = jbjh1.getInt("hb"); // 月卡: 金虎  虎币
            int yhhb2 = jbyh2.getInt("hb"); // 年卡：银虎  虎币
            int jhhb2 = jbjh2.getInt("hb"); // 年卡：金虎  虎币
            if (goodsId == 1 && huType == 1) {
                // 月卡、银虎
                tvPrice.setVisibility(View.GONE);
                tvNowPrice.setText(String.valueOf(yhhb1));
            } else if (goodsId == 1 && huType == 2) {
                // 月卡、金虎
                tvPrice.setVisibility(View.GONE);
                tvNowPrice.setText(String.valueOf(jhhb1));
            } else if (goodsId == 2 && huType == 1) {
                // 年卡、银虎
                tvNowPrice.setText(String.valueOf(yhhb2));
                int orig = jbyh2.getInt("orig");
                tvPrice.setVisibility(View.VISIBLE);
                tvPrice.setText("(原价：" + String.valueOf(orig) + ")");
            } else if (goodsId == 2 && huType == 2) {
                // 年卡、金虎
                tvNowPrice.setText(String.valueOf(jhhb2));
                int orig = jbjh2.getInt("orig");
                tvPrice.setVisibility(View.VISIBLE);
                tvPrice.setText("(原价：" + String.valueOf(orig) + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
