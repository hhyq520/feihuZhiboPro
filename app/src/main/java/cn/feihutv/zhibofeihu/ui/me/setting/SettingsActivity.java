package cn.feihutv.zhibofeihu.ui.me.setting;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.main.MainActivity;
import cn.feihutv.zhibofeihu.ui.user.UserAgreement;
import cn.feihutv.zhibofeihu.ui.user.login.LoginActivity;
import cn.feihutv.zhibofeihu.ui.widget.TCActivityTitle;
import cn.feihutv.zhibofeihu.ui.widget.TCLineControllerView;
import cn.feihutv.zhibofeihu.utils.DataCleanManager;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.feihutv.zhibofeihu.utils.TCConstants;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.feihutv.zhibofeihu.utils.weiget.dialog.RxDialogSureCancel;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/6 09:28
 *      desc   : 设置界面
 *      version: 1.0
 * </pre>
 */

public class SettingsActivity extends BaseActivity {

    @BindView(R.id.identity_return)
    TCActivityTitle mTitle;

    @BindView(R.id.switch_button)
    ImageView switchButton;

    @BindView(R.id.clear_cache)
    TCLineControllerView clearCache;

    @BindView(R.id.setting_version)
    TCLineControllerView rlVersion;

    private String totalCacheSize;
    private boolean isClick = true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_settings;
    }

    @Override
    protected void initWidget() {
        setUnBinder(ButterKnife.bind(this));

        try {
            totalCacheSize = DataCleanManager.getTotalCacheSize(SettingsActivity.this);
            clearCache.setContent(totalCacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            rlVersion.setContent(getVersionName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        FeihuZhiboApplication.getApplication().addActivity(SettingsActivity.this);
        switchButton.setSelected(SharePreferenceUtil.getSessionBoolean(this, "xuanfuchuan", true));
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

    @OnClick({R.id.switch_button, R.id.lcv_recevice_news, R.id.lcv_blacklist, R.id.lcv_faq, R.id.clear_cache, R.id.lcv_about_us, R.id.lcv_give_us_score, R.id.btn_launch_login, R.id.setting_management})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.switch_button:
                MobclickAgent.onEvent(SettingsActivity.this, "10141");
                if (isClick) {
                    switchButton.setSelected(false);
                    isClick = false;
                    SharePreferenceUtil.saveSeesionBoolean(SettingsActivity.this, "xuanfuchuan", false);
                } else {
                    switchButton.setSelected(true);
                    isClick = true;
                    SharePreferenceUtil.saveSeesionBoolean(SettingsActivity.this, "xuanfuchuan", true);
                }
                break;
            case R.id.lcv_recevice_news:
                startActivity(new Intent(this, MsgReceSettingsActivity.class));
                break;
            case R.id.lcv_blacklist:
                startActivity(new Intent(this, BlacklistActivity.class));
                break;
            case R.id.lcv_faq:
                Intent intent1 = new Intent(this, UserAgreement.class);
                intent1.putExtra("url", FeihuZhiboApplication.getApplication().mDataManager.getSysConfig().getFaqUrl());
                intent1.putExtra("title", "常见问题");
                startActivity(intent1);
                break;
            case R.id.clear_cache:
                showClearDialog();
                break;
            case R.id.setting_management:
                Intent intent2 = new Intent(this, UserAgreement.class);
                intent2.putExtra("url", FeihuZhiboApplication.getApplication().mDataManager.getSysConfig().getUseragreementUrl() + "?rand=" + System.currentTimeMillis());
                intent2.putExtra("title", "平台管理规范");
                startActivity(intent2);
                break;
            case R.id.lcv_about_us:
                startActivity(new Intent(this, AboutUsActivity.class));
                break;
            case R.id.lcv_give_us_score:
                goToMarket(this, getPackageName());
                break;
            case R.id.btn_launch_login:
                Intent intent = new Intent();
                //B为你按退出按钮所在的activity
                intent.setClass(SettingsActivity.this, LoginActivity.class);
                startActivity(intent);
                TCUtils.saveLoginInfo("", "");
                FeihuZhiboApplication.getApplication().mDataManager.setUserAsLoggedOut();
                if (MainActivity.MainActivityself != null) {
                    MainActivity.MainActivityself.finish();
                }
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 清除缓存
      */

    private void showClearDialog() {

        final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(this);
        rxDialogSureCancel.setContent("确定清除缓存吗？");
        rxDialogSureCancel.setSure("确定");
        rxDialogSureCancel.setCancel("取消");
        rxDialogSureCancel.setSureListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //清除操作
                DataCleanManager.clearAllCache(SettingsActivity.this);
                try {
                    //清除后的操作
                    totalCacheSize = DataCleanManager.getTotalCacheSize(SettingsActivity.this);
                    clearCache.setContent(totalCacheSize);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                onToast("清除缓存成功", Gravity.CENTER, 0, 0);
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

    public static void goToMarket(Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前应用的版本号：
     */
    private String getVersionName() throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        String version = packInfo.versionName;
        return version;
    }
}
