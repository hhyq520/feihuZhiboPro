package cn.feihutv.zhibofeihu.activitys;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import cn.feihutv.zhibofeihu.BuildConfig;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.data.DataManager;
import cn.feihutv.zhibofeihu.data.local.SysdataHelper;
import cn.feihutv.zhibofeihu.data.local.model.SysDataEntity;
import cn.feihutv.zhibofeihu.data.network.http.model.common.CheckUpdateRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.CheckUpdateResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.user.LoginResponse;
import cn.feihutv.zhibofeihu.ui.base.BasePresenter;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.ResCopyUtil;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/05/14
 *     desc   : p业务处理实现类
 *     version: 1.0
 * </pre>
 */
public class SplashPresenter<V extends SplashMvpView> extends BasePresenter<V>
        implements SplashMvpPresenter<V> {


    @Inject
    public SplashPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void checkDataUpdate() {
        getCompositeDisposable().add(getDataManager()
                .doCheckUpdateApiCall(new CheckUpdateRequest(BuildConfig.market_value))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CheckUpdateResponse>() {
                    @Override
                    public void accept(@NonNull CheckUpdateResponse checkUpdateResponse) throws Exception {

                        if (checkUpdateResponse.getCode() == 0) {
                            CheckUpdateResponse.CheckUpdateData mCheckUpdateData = checkUpdateResponse.getCheckUpdateData();
                            AppLogger.i(checkUpdateResponse.toString());
                            Context mContext = FeihuZhiboApplication.getApplication();
                            SharePreferenceUtil.saveSeesionInt(mContext, "openPlayGame", mCheckUpdateData.getOpenPlayGame());
                            SharePreferenceUtil.saveSeesionInt(mContext, "openPlayLimitLevel", mCheckUpdateData.getOpenPlayLimitLevel());
                            SharePreferenceUtil.saveSeesion(mContext, "registerUrl", mCheckUpdateData.getRegisterUrl());
                            SharePreferenceUtil.saveSeesion(mContext, "versionName", mCheckUpdateData.getVersionName());
                            SharePreferenceUtil.saveSeesion(mContext, "apkUrlName", mCheckUpdateData.getApkUrlName());
                            SharePreferenceUtil.saveSeesionInt(mContext, "needVersionId", mCheckUpdateData.getNeedVersionId());
                            SharePreferenceUtil.saveSeesionInt(mContext, "versionId", mCheckUpdateData.getVersionId());
                            SharePreferenceUtil.saveSeesion(mContext, "versionDesc", mCheckUpdateData.getVersionDesc());
                            SharePreferenceUtil.saveSeesionInt(mContext, "openHubao", mCheckUpdateData.getOpenHubao());
                            SharePreferenceUtil.saveSeesion(mContext, "hubaoUrl", mCheckUpdateData.getHubaoUrl());
                            SharePreferenceUtil.saveSeesionInt(mContext, "hbNeedLevel", mCheckUpdateData.getHbNeedLevel());
                            //  chazhi 表示当前手机时间和服务器时间的差值
                            long chazhi = mCheckUpdateData.getServerTime() - System.currentTimeMillis() / 1000;
                            SharePreferenceUtil.saveSeesionLong(mContext, "chazhi", chazhi);
                            getDataManager().setTimeChaZhi(chazhi);
                            Long newVercode = mCheckUpdateData.getSysdataVersion();
                            if (newVercode > SharePreferenceUtil.getSessionLong(mContext, "versionCode", 20170802195455L)) {
                                SharePreferenceUtil.saveSeesionLong(mContext, "versionCode", newVercode);

                                final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + SysdataHelper.SYSDATA_URL + "/";

                                AndroidNetworking.download(mCheckUpdateData.getSource(), dirPath, "sysdata.json")
                                        .setTag("downloadSysdata")
                                        .setPriority(Priority.MEDIUM)
                                        .build()
                                        .setDownloadProgressListener(new DownloadProgressListener() {
                                            @Override
                                            public void onProgress(long bytesDownloaded, long totalBytes) {
                                                // do anything with progress
                                            }
                                        })
                                        .startDownload(new DownloadListener() {
                                            @Override
                                            public void onDownloadComplete() {
                                                // do anything after completion
                                                executeLogin();
                                            }

                                            @Override
                                            public void onError(ANError error) {
                                                // handle error

                                                executeLogin();
                                            }
                                        });

                            } else {
                                executeLogin();
                            }

                        } else {
                            //请求失败
                            executeLogin();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        executeLogin();
                    }
                })

        );
    }

    private void executeLogin() {
        getSysdataFromFile(getMvpView().getActivity());
        if (getMvpView().isNetworkConnected()) {
            if (!TextUtils.isEmpty(getDataManager().getApiKey())) {
                //获取socket api
                enterLogin("0", new Consumer<LoginResponse>() {
                    @Override
                    public void accept(@NonNull LoginResponse baseResponse) throws Exception {
                        //获取成功 连接socket
                        if (baseResponse.getCode() == 0) {
                            //连接socket 成功跳转主页
                            getMvpView().openMainActivity();
                        } else {
                            getMvpView().openLoginActivity();
                        }
                    }
                });
            } else {
                getMvpView().openLoginActivity();

            }
        } else {
            getMvpView().openLoginActivity();
        }
    }


    public void getSysdataFromFile(Activity activity) {
        if (!ResCopyUtil.checkFileExist(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + SysdataHelper.SYSDATA_URL)) {
            getSysdata(activity);
        } else {
            String json = ResCopyUtil.readJsonFromFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + SysdataHelper.SYSDATA_URL + "/sysdata.json");
            if (json != null) {
                getSysdata(json);
            } else {
                getSysdata(activity);
            }
        }
    }

    public void getSysdata(Activity activity) {
        String json = ResCopyUtil.getJson(activity, "sysdata/sysdata.json");
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            SysDataEntity sysDataEntity = new Gson().fromJson(jsonObject.toString(), SysDataEntity.class);
            saveSysData(sysDataEntity);
            AssetManager assetManager = FeihuZhiboApplication.getApplication().getAssets();
            if (checkPermission(activity)) {
                ResCopyUtil.copyAssetToSD(assetManager, "sysdata", SysdataHelper.SYSDATA_URL);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getSysdata(String json) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            SysDataEntity sysDataEntity = new Gson().fromJson(jsonObject.toString(), SysDataEntity.class);
            saveSysData(sysDataEntity);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean checkPermission(Activity activity) {
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED) {
            return true;
        } // 需要弹出dialog让用户手动赋予权限
        else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
            return false;
        }
    }

    private void saveSysData(SysDataEntity sysDataEntity) {
        getCompositeDisposable().add(getDataManager()
                .saveSysGiftNew(sysDataEntity.getSysGift())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object obj) throws Exception {

                    }
                }, getConsumer())
        );

        getCompositeDisposable().add(getDataManager()
                .saveSysHBBean(sysDataEntity.getSysHB())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object obj) throws Exception {

                    }
                }, getConsumer())
        );
        getCompositeDisposable().add(getDataManager()
                .saveSysLevelBean(sysDataEntity.getSysLevel())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object obj) throws Exception {

                    }
                }, getConsumer())
        );
        getCompositeDisposable().add(getDataManager()
                .saveSysTipsBean(sysDataEntity.getSysTips())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object obj) throws Exception {

                    }
                }, getConsumer())
        );
        getCompositeDisposable().add(getDataManager()
                .saveSysGoodsNewBean(sysDataEntity.getSysGoods())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object obj) throws Exception {

                    }
                }, getConsumer())
        );
        getCompositeDisposable().add(getDataManager()
                .saveSysItemBean(sysDataEntity.getSysItem())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object obj) throws Exception {

                    }
                }, getConsumer())
        );
        getCompositeDisposable().add(getDataManager().saveSysGameBetBean(sysDataEntity.getSysGameBetX())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object obj) throws Exception {

                    }
                }, getConsumer())
        );

        getCompositeDisposable().add(getDataManager()
                .saveSysMountNewBean(sysDataEntity.getSysMount())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object obj) throws Exception {

                    }
                }, getConsumer())
        );
        getCompositeDisposable().add(getDataManager()
                .saveSysConfigBean(sysDataEntity.getSysConfig())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object obj) throws Exception {

                    }
                }, getConsumer())
        );

        getCompositeDisposable().add(getDataManager()
                .saveSysLaunchTagBean(sysDataEntity.getSysLaunchTag())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object obj) throws Exception {

                    }
                }, getConsumer())
        );

        getCompositeDisposable().add(getDataManager()
                .saveSysSignAwardBean(sysDataEntity.getSysSignAward())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object obj) throws Exception {

                    }
                }, getConsumer())
        );

        getCompositeDisposable().add(getDataManager()
                .saveSysGuardGoodsBean(sysDataEntity.getSysGuardGoodsBeen())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object obj) throws Exception {

                    }
                }, getConsumer())
        );
        getCompositeDisposable().add(getDataManager()
                .saveSysFontColorBean(sysDataEntity.getSysFontColor())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object obj) throws Exception {
                    }
                }, getConsumer())
        );

        getCompositeDisposable().add(getDataManager()
                .saveSysVipGoodsBean(sysDataEntity.getSysVipGoods())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object obj) throws Exception {

                    }
                }, getConsumer())

        );

        getCompositeDisposable().add(getDataManager()
                .saveSysVipBean(sysDataEntity.getSysVip())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object obj) throws Exception {

                    }
                }, getConsumer())

        );


    }
}
