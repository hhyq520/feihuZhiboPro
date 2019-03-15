package cn.feihutv.zhibofeihu.ui.main;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.data.DataManager;
import cn.feihutv.zhibofeihu.data.db.model.SysConfigBean;
import cn.feihutv.zhibofeihu.data.db.model.SysGiftNewBean;
import cn.feihutv.zhibofeihu.data.db.model.SysMountNewBean;
import cn.feihutv.zhibofeihu.data.local.SysdataHelper;
import cn.feihutv.zhibofeihu.data.local.model.SysbagBean;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadSignRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadSignResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.SignRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.SignResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.RoomVipRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.RoomVipResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.TestStartLiveRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.TestStartLiveResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.user.BagRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.user.BagResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;
import cn.feihutv.zhibofeihu.ui.base.BasePresenter;
import cn.feihutv.zhibofeihu.ui.widget.flashview.FlashDataParser;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * <pre>
 *     author : sichu.chen
 *     time   :
 *     desc   : p业务处理实现类
 *     version: 1.0
 * </pre>
 */
public class MainPresenter<V extends MainMvpView> extends BasePresenter<V>
        implements MainMvpPresenter<V> {


    @Inject
    public MainPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void requestBagData() {
        getCompositeDisposable().
                add(getDataManager().
                        doGetUserBagDataApiCall(new BagRequest()).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<BagResponse>() {
                            @Override
                            public void accept(@NonNull BagResponse bagResponse) throws Exception {
                                if (bagResponse.getCode() == 0) {
                                    if (bagResponse.getBagResponseData().size() > 0) {
                                        List<SysbagBean> sysbagBeanList = new ArrayList<SysbagBean>();
                                        for (BagResponse.BagResponseData info : bagResponse.getBagResponseData()) {
                                            SysbagBean bag = new SysbagBean();
                                            bag.setCnt(info.getCnt());
                                            bag.setId(info.getId());
                                            sysbagBeanList.add(bag);
                                        }
                                    }
                                } else {
                                    AppLogger.i(bagResponse.getErrMsg());
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {

                            }
                        }));
    }

    @Override
    public void loadSignData() {
        getCompositeDisposable().add(getDataManager()
                .doGetSignDataApiCall(new LoadSignRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoadSignResponce>() {
                    @Override
                    public void accept(@NonNull LoadSignResponce loadSignResponce) throws Exception {
                        if (loadSignResponce.getCode() == 0) {
                            LoadSignResponce.LoadSignData signData = loadSignResponce.getSignData();
                            if (!signData.isSigned()) {
                                getMvpView().showSignDialog(signData.getDays());
                            }
                        } else {
                            AppLogger.i(loadSignResponce.getCode() + " " + loadSignResponce.getErrMsg());
                        }
                    }
                }, getConsumer())

        );
    }

    @Override
    public void downLoadGiftResource() {
        downLoadResource();
    }

    @Override
    public void loadRoomById(String roomId) {
        getCompositeDisposable().add(getDataManager()
                .doGetRoomDataApiCall(new LoadRoomRequest(roomId))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoadRoomResponce>() {
                    @Override
                    public void accept(@NonNull LoadRoomResponce loadRoomResponce) throws Exception {
                        if (loadRoomResponce.getCode() == 0) {
                            getMvpView().gotoRoom(loadRoomResponce.getLoadRoomData());
                        } else {
                            AppLogger.e(loadRoomResponce.getErrMsg());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));

    }

    @Override
    public void saveUserData(LoadUserDataBaseResponse.UserData userData) {
        getDataManager().saveUserData(userData);
    }

    @Override
    public void testStartLive() {
        getCompositeDisposable().add(getDataManager()
                .doGetTestStartLiveApiCall(new TestStartLiveRequest())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<TestStartLiveResponce>() {
                    @Override
                    public void accept(@NonNull TestStartLiveResponce responce) throws Exception {
                        getMvpView().notifyLive(responce);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));
    }

    private void down520TemplateUrl(SysConfigBean sysConfigBean) {
        String down520TemplateUrl = sysConfigBean.getCosAnimtionTemplate520();
        final String zip520TemplateName = down520TemplateUrl.substring(down520TemplateUrl.lastIndexOf("/") + 1, down520TemplateUrl.length());
        if (!isFileExists(zip520TemplateName.split("_")[0], "flashAnims") || !isFileExists(zip520TemplateName.split("_")[0] + ".flajson", "flashAnims") || !isFileExists(zip520TemplateName, "flashAnimZips")) {
            FlashDataParser.Downloader.removeAnimFiles(getMvpView().getActivity(), false, zip520TemplateName.split("_")[0], zip520TemplateName);
            AndroidNetworking.download(down520TemplateUrl, isExistDir(getMvpView().getActivity(), "flashAnimZips"), getNameFromUrl(down520TemplateUrl))
                    .setTag("down520TemplateUrl")
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
                            AppLogger.e("down520TemplateUrl");
                            FlashDataParser.Downloader.upZipResource(getMvpView().getActivity(), "flashAnimZips", "flashAnims", zip520TemplateName);
                        }

                        @Override
                        public void onError(ANError error) {
                            // handle error
                        }
                    });
        }
    }

    private void downTemplate1314Url(SysConfigBean sysConfigBean) {
        String downTemplate1314Url = sysConfigBean.getCosAnimtionTemplate1314_android();
        final String zipNameTemplate1314 = downTemplate1314Url.substring(downTemplate1314Url.lastIndexOf("/") + 1, downTemplate1314Url.length());
        if (!isFileExists(zipNameTemplate1314.split("_")[0], "flashAnims") || !isFileExists(zipNameTemplate1314.split("_")[0] + ".flajson", "flashAnims") || !isFileExists(zipNameTemplate1314, "flashAnimZips")) {
            FlashDataParser.Downloader.removeAnimFiles(getMvpView().getActivity(), false, zipNameTemplate1314.split("_")[0], zipNameTemplate1314);
            AndroidNetworking.download(downTemplate1314Url, isExistDir(getMvpView().getActivity(), "flashAnimZips"), getNameFromUrl(downTemplate1314Url))
                    .setTag("downTemplate1314Url")
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
                            AppLogger.e("downTemplate1314Url");
                            FlashDataParser.Downloader.upZipResource(getMvpView().getActivity(), "flashAnimZips", "flashAnims", zipNameTemplate1314);
                        }

                        @Override
                        public void onError(ANError error) {
                            // handle error
                        }
                    });
        }
    }

    private void downLoadNewGift(SysConfigBean sysConfigBean, List<SysGiftNewBean> sysGiftNewBeanList) {
        for (final SysGiftNewBean item : sysGiftNewBeanList) {
            if (item.getIsAnimation().equals("1")) {
                String downUrl = sysConfigBean.getCosAnimationRootPath() + item.getAnimName();
                String zipName = item.getAnimName().split("_")[0];
                if (sysConfigBean != null) {
                    if (!isFileExists(zipName, "flashAnims") || !isFileExists(zipName + ".flajson", "flashAnims") || !isFileExists(item.getAnimName(), "flashAnimZips")) {
                        FlashDataParser.Downloader.removeAnimFiles(getMvpView().getActivity(), false, zipName, item.getAnimName());
                        AndroidNetworking.download(downUrl, isExistDir(getMvpView().getActivity(), "flashAnimZips"), getNameFromUrl(downUrl))
                                .setTag("downUrl")
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
                                        AppLogger.e("downUrl");
                                        FlashDataParser.Downloader.upZipResource(getMvpView().getActivity(), "flashAnimZips", "flashAnims", item.getAnimName());
                                    }

                                    @Override
                                    public void onError(ANError error) {
                                        // handle error
                                    }
                                });
                    }
                    if (!isFileExists(zipName, "landFlash") || !isFileExists(zipName + ".flajson", "landFlash") || !isFileExists(item.getAnimName(), "landFlashAnimZips")) {
                        String landdownUrl = sysConfigBean.getCosAnimationLandRootPath() + item.getAnimName();
                        FlashDataParser.Downloader.removeAnimFiles(getMvpView().getActivity(), true, zipName, item.getAnimName());
                        AndroidNetworking.download(landdownUrl, isExistDir(getMvpView().getActivity(), "landFlashAnimZips"), getNameFromUrl(landdownUrl))
                                .setTag("landdownUrl")
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
                                        AppLogger.e("landdownUrl");
                                        FlashDataParser.Downloader.upZipResource(getMvpView().getActivity(), "landFlashAnimZips", "landFlash", item.getAnimName());
                                    }

                                    @Override
                                    public void onError(ANError error) {
                                        // handle error
                                    }
                                });
                    }
                }
            }
        }
    }

    private void downLoadPic(SysConfigBean sysConfigBean, List<SysGiftNewBean> sysGiftNewBeanList) {
        final String olddownUrl520 = sysConfigBean.getCosGiftTemplate520();
        String downUrl520 =    sysConfigBean.getCosGiftTemplate520();
        downUrl520=downUrl520.substring(0,downUrl520.lastIndexOf("_"))+".zip";
        String zipName = downUrl520.substring(downUrl520.lastIndexOf("/") + 1, downUrl520.length());
        final String zipFile520 = downUrl520.substring(downUrl520.lastIndexOf("/") + 1, downUrl520.length() - 4);
        final String picFile520=olddownUrl520.substring(olddownUrl520.lastIndexOf("/") + 1,olddownUrl520.length() - 4);
        AppLogger.e("aaaaaaaa",zipFile520);
        if (!isPicFileExists(picFile520) || !isFileExists(getNameFromUrl(olddownUrl520), "flashAnimZips") || !(FlashDataParser.getFileListLength(getMvpView().getActivity(), picFile520) == SysdataHelper.getStaticGiftLength(sysGiftNewBeanList))) {
            FlashDataParser.Downloader.removePicFiles(getMvpView().getActivity(), picFile520);
            AndroidNetworking.download(olddownUrl520, isExistDir(getMvpView().getActivity(), "flashAnimZips"), getNameFromUrl(olddownUrl520))
                    .setTag("downUrl520")
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
                            AppLogger.e("downUrl520");
                            // do anything after completion
                            String zipfile = FlashDataParser.getExternalStorageDirectory(getMvpView().getActivity()) + "/flashAnimZips/" + getNameFromUrl(olddownUrl520);
                            String destFile = FlashDataParser.getExternalStorageDirectory(getMvpView().getActivity()) + "/" + picFile520;
                            FlashDataParser.Downloader.upZipFile(new File(zipfile), destFile);
                        }

                        @Override
                        public void onError(ANError error) {
                            // handle error
                        }
                    });
        }

        String downPicUrl1314 = sysConfigBean.getCosGiftTemplate1314();
        final String olddownPicUrl1314 = sysConfigBean.getCosGiftTemplate1314();
        downPicUrl1314=downPicUrl1314.substring(0,downPicUrl1314.lastIndexOf("_"))+".zip";
        final String zipFile1314 = downPicUrl1314.substring(downPicUrl1314.lastIndexOf("/") + 1, downPicUrl1314.length() - 4);
        final String picFile1314=olddownPicUrl1314.substring(olddownPicUrl1314.lastIndexOf("/") + 1,olddownPicUrl1314.length() - 4);
        AppLogger.e("aaaaaaaa",zipFile1314);
        String zipNamePic1314 = downPicUrl1314.substring(downPicUrl1314.lastIndexOf("/") + 1, downPicUrl1314.length());
        if (!isPicFileExists(picFile1314) || !isFileExists(getNameFromUrl(olddownPicUrl1314), "flashAnimZips") || !(FlashDataParser.getFileListLength(getMvpView().getActivity(), picFile1314) == SysdataHelper.getStaticGiftLength(sysGiftNewBeanList))) {
            FlashDataParser.Downloader.removePicFiles(getMvpView().getActivity(), picFile1314);
            AndroidNetworking.download(olddownPicUrl1314, isExistDir(getMvpView().getActivity(), "flashAnimZips"), getNameFromUrl(olddownPicUrl1314))
                    .setTag("downPicUrl1314")
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
                            AppLogger.e("downPicUrl1314");
                            String zipfile = FlashDataParser.getExternalStorageDirectory(getMvpView().getActivity()) + "/flashAnimZips/" + getNameFromUrl(olddownPicUrl1314);
                            String destFile = FlashDataParser.getExternalStorageDirectory(getMvpView().getActivity()) + "/" + picFile1314;
                            FlashDataParser.Downloader.upZipFile(new File(zipfile), destFile);
                        }

                        @Override
                        public void onError(ANError error) {
                            // handle error
                        }
                    });
        }
    }

    private void downLoadMount(SysConfigBean sysConfigBean, List<SysMountNewBean> sysMountNewBeanList) {
        for (final SysMountNewBean item : sysMountNewBeanList) {
            if (item.getIsAnimation().equals("1")&& !TextUtils.isEmpty(item.getAnimName())) {
                String downUrl = sysConfigBean.getCosMountRootPath() + item.getAnimName();
                String zipNameMount = item.getAnimName().split("_")[0];
                if (!FlashDataParser.isFileExist(getMvpView().getActivity(), "mountFlashAnimZips", item.getAnimName()) ||
                        !FlashDataParser.isFileExist(getMvpView().getActivity(), "mountFlashAnim", zipNameMount + ".flajson")
                        || !FlashDataParser.isFileExist(getMvpView().getActivity(), "mountFlashAnim", zipNameMount)) {
                    FlashDataParser.Downloader.removeMountFiles(getMvpView().getActivity(), zipNameMount, item.getAnimName());
                    AndroidNetworking.download(downUrl, isExistDir(getMvpView().getActivity(), "mountFlashAnimZips"), getNameFromUrl(downUrl))
                            .setTag("downUrl")
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
                                    AppLogger.e("downUrl");
                                    FlashDataParser.Downloader.upZipResource(getMvpView().getActivity(), "mountFlashAnimZips", "mountFlashAnim", item.getAnimName());
                                }

                                @Override
                                public void onError(ANError error) {
                                    // handle error
                                }
                            });
                }
                if (!FlashDataParser.isFileExist(getMvpView().getActivity(), "mountLandFlashAnimZips", item.getAnimName()) ||
                        !FlashDataParser.isFileExist(getMvpView().getActivity(), "mountLandFlashAnim", zipNameMount + ".flajson")
                        || !FlashDataParser.isFileExist(getMvpView().getActivity(), "mountLandFlashAnim", zipNameMount)) {
                    FlashDataParser.Downloader.removeMountFiles(getMvpView().getActivity(), zipNameMount, item.getAnimName());
                    AndroidNetworking.download(downUrl, isExistDir(getMvpView().getActivity(), "mountLandFlashAnimZips"), getNameFromUrl(downUrl))
                            .setTag("downUrlm")
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
                                    AppLogger.e("downUrlm");
                                    FlashDataParser.Downloader.upZipResource(getMvpView().getActivity(), "mountLandFlashAnimZips", "mountLandFlashAnim", item.getAnimName());
                                }

                                @Override
                                public void onError(ANError error) {
                                    // handle error
                                }
                            });
                }
            }
        }
    }

    private void downLoadResource() {
        checkPerssion(1011);
        List<SysConfigBean> sysConfigBeanList = getDataManager().getSysConfigBean();
        final SysConfigBean sysConfigBean = sysConfigBeanList.get(0);
        if (sysConfigBean != null) {
            down520TemplateUrl(sysConfigBean);
            downTemplate1314Url(sysConfigBean);
            List<SysGiftNewBean> sysGiftNewBeanList = getDataManager().getSysGiftNew();
            downLoadNewGift(sysConfigBean, sysGiftNewBeanList);
            downLoadPic(sysConfigBean, sysGiftNewBeanList);
            downLoadMount(sysConfigBean, getDataManager().getSysMountNewBean());
        }
    }

    @android.support.annotation.NonNull
    private static String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    private static String isExistDir(Context ctx, String saveDir) {
        // 下载位置
        File downloadFile = new File(Environment.getExternalStorageDirectory() + "/." + ctx.getPackageName(), saveDir);
        if (!downloadFile.mkdirs()) {
            try {
                downloadFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }

    private boolean isFileExists(String fileName, String pathName) {
        String path = FlashDataParser.getExternalStorageDirectory(getMvpView().getActivity()) + "/" + pathName;
        return new File(path, fileName).exists();
    }

    public boolean isPicFileExists(String fileName) {
        String path = FlashDataParser.getExternalStorageDirectory(getMvpView().getActivity());
        return new File(path, fileName).exists();
    }

    private boolean checkPerssion(int code) {
        if (ActivityCompat.checkSelfPermission(getMvpView().getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 需要弹出dialog让用户手动赋予权限
            ActivityCompat.requestPermissions(getMvpView().getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, code);
            return false;
        } else {
            return true;
        }
    }

}
