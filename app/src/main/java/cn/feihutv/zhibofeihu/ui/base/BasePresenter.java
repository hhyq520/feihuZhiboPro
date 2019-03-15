package cn.feihutv.zhibofeihu.ui.base;


import android.util.Log;

import com.androidnetworking.common.ANConstants;
import com.androidnetworking.error.ANError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.util.List;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.DataManager;
import cn.feihutv.zhibofeihu.data.network.http.model.ApiError;
import cn.feihutv.zhibofeihu.data.network.http.model.common.GetSocketTokenRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.GetSocketTokenResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.user.LoginResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.SocketBaseResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.common.SocketConnectError;
import cn.feihutv.zhibofeihu.data.network.socket.model.common.VerifySocketTokenRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.common.VerifySocketTokenResponse;
import cn.feihutv.zhibofeihu.data.network.socket.xsocket.FHSocket;
import cn.feihutv.zhibofeihu.rxbus.RxBus;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.utils.AppConstants;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.DeviceInfoUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/09/25
 *     desc   : MVP 模式中 P:主持者基类
 *     version: 1.0
 * </pre>
 */

public class BasePresenter<V extends MvpView> implements MvpPresenter<V> {

    private static final String TAG = "BasePresenter";

    private final DataManager mDataManager;

    private final CompositeDisposable mCompositeDisposable;


    private V mMvpView;

    @Inject
    public BasePresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        this.mDataManager = dataManager;
        this.mCompositeDisposable = compositeDisposable;
    }

    @Override
    public void onAttach(V mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void onDetach() {
        mCompositeDisposable.dispose();
        mMvpView = null;
    }

    //判null 防止出现null 异常
    public boolean isViewAttached() {
        return mMvpView != null;
    }

    public V getMvpView() {
        return mMvpView;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    public DataManager getDataManager() {
        return mDataManager;
    }

    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }

    @Override
    public void handleApiError(ANError error) {


        if (error == null || error.getErrorBody() == null) {
            getMvpView().onToast(R.string.api_default_error);
            return;
        }
        AppLogger.e("请求失败：" + error.toString());
        if (error.getErrorCode() == AppConstants.API_STATUS_CODE_LOCAL_ERROR
                && error.getErrorDetail().equals(ANConstants.CONNECTION_ERROR)) {
            getMvpView().onToast(R.string.connection_error);
            return;
        }

        if (error.getErrorCode() == AppConstants.API_STATUS_CODE_LOCAL_ERROR
                && error.getErrorDetail().equals(ANConstants.REQUEST_CANCELLED_ERROR)) {
            getMvpView().onToast(R.string.api_retry_error);
            return;
        }

        final GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        final Gson gson = builder.create();

        //统一处理http 请求错误
        try {
            ApiError apiError = gson.fromJson(error.getErrorBody(), ApiError.class);

            if (apiError == null || apiError.getMessage() == null) {
                getMvpView().onToast(R.string.api_default_error);
                return;
            }

            switch (error.getErrorCode()) {
                case HttpsURLConnection.HTTP_UNAUTHORIZED:
                case HttpsURLConnection.HTTP_FORBIDDEN:
                    setUserAsLoggedOut();
                    getMvpView().openActivityOnTokenExpire();
                case HttpsURLConnection.HTTP_INTERNAL_ERROR:
                case HttpsURLConnection.HTTP_NOT_FOUND:
                default:
                    getMvpView().onToast(apiError.getMessage());
            }
        } catch (JsonSyntaxException | NullPointerException e) {
            Log.e(TAG, "handleApiError", e);
            getMvpView().onToast(R.string.api_default_error);
        }
    }

    @Override
    public void handleApiCode(int code) {
        //处理 请求code
    }

    @Override
    public void setUserAsLoggedOut() {
        getDataManager().setAccessToken(null);
    }

    @Override
    public boolean isGuestUser() {
        //判断是否为游客身份
        if (getDataManager().getCurrentUserId().startsWith("g")) {
            return true;
        }
        return false;
    }

    @Override
    public void enterLogin(final String reconnect, final Consumer<LoginResponse> response) {
        //获取socket api
        String vr = DeviceInfoUtil.getBuildVersion();
        String mac = DeviceInfoUtil.getDeviceId(FeihuZhiboApplication.getApplication());
        String model = DeviceInfoUtil.getPhoneModel();
        getCompositeDisposable().add(getDataManager()
                        .doGetSocketTokenApiCall(new GetSocketTokenRequest("1", vr, model, mac, reconnect))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<GetSocketTokenResponse>() {
                            @Override
                            public void accept(@NonNull GetSocketTokenResponse getSocketTokenResponse) throws Exception {

                                //设置返回回调
                                LoginResponse loginResponse = new LoginResponse();
                                loginResponse.setCode(getSocketTokenResponse.getCode());
                                loginResponse.setErrMsg(getSocketTokenResponse.getErrMsg());
                                loginResponse.setErrExtMsg(getSocketTokenResponse.getErrExtMsg());
                                loginResponse.setErrExtMsgQ(getSocketTokenResponse.getErrExtMsgQ());
                                loginResponse.setErrExtMsgTime(getSocketTokenResponse.getErrExtMsgTime());
//                                response.accept(loginResponse);

                                int code = getSocketTokenResponse.getCode();
                                switch (code) {
                                    case 0:
                                        //成功
                                        GetSocketTokenResponse.GetSocketTokenData tokenData = getSocketTokenResponse.getTokenData();
                                        String token = tokenData.getToken();
                                        List<String> address = tokenData.getAddress();

                                        if ("0".equals(reconnect)) {
                                            //首次连接

                                            connectSocketAndVerifyToken(token, address, reconnect, response, loginResponse);
                                        } else {
                                            //重连
                                            verifySocketToken(token, "1", response, loginResponse);
                                        }
                                        break;
                                    case 4005:

                                        //鉴权失败
                                        if ("1".equals(reconnect)) {
                                            //重连失败，需要跳转登录页面重新登录
                                            RxBus.get().send(RxBusCode.RX_BUS_CODE_HTTP_RECONNECT_SIG_FAILED, new SocketConnectError());
                                            AppLogger.i("-----重连鉴权失败，即将退出重新登录！---->" + getSocketTokenResponse);
//                                    getMvpView().onToast("");
                                            //退出登录操作
                                            getDataManager().setUserAsLoggedOut();
                                            //跳转登录页面
                                            getMvpView().openActivityOnTokenExpire();

                                        }
                                        break;
                                    default:
                                        break;
                                }

                                if (code != 0) {
                                    response.accept(loginResponse);
                                }

                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                if ("0".equals(reconnect)) {
                                    throwable.printStackTrace();
                                    if (!isViewAttached()) {
                                        return;
                                    }
                                    getMvpView().hideLoading();
                                    if (throwable instanceof ANError) {
                                        ANError anError = (ANError) throwable;
                                        handleApiError(anError);
                                    }
                                } else {
                                    AppLogger.i("------网络异常，重连失败！");
                                }

                            }
                        })

        );
    }

    @Override
    public String getLoinUserId() {
        return getDataManager().getCurrentUserId();
    }

    @Override
    public LoadUserDataBaseResponse.UserData getUserData() {
        return getDataManager().getUserData();
    }

    //socket 验证连接有效性
    private void connectSocketAndVerifyToken(final String token, List<String> address, final String reconnect, final Consumer<LoginResponse> response, final LoginResponse loginResponse) {


        //连接socket
        getDataManager()
                .doConnectSocketApiCall(address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SocketBaseResponse>() {
                    @Override
                    public void accept(@NonNull SocketBaseResponse socketBaseResponse) throws Exception {
                        //连接socket 回应
                        AppLogger.e("socketVerifyToken----->" + socketBaseResponse);
                        if (socketBaseResponse.success) {
                            //连接成功
                            verifySocketToken(token, reconnect, response, loginResponse);

                        } else {
                            //连接失败
                            loginResponse.setCode(AppConstants.ERROR_CODE_1);
                            response.accept(loginResponse);

                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        AppLogger.e(throwable.toString());
                    }
                });


    }


    //验证socket toke
    private void verifySocketToken(String token, final String reconnect, final Consumer<LoginResponse> response, final LoginResponse loginResponse) {
        getDataManager()
                .doVerifySocketTokenSocketApiCall(new VerifySocketTokenRequest(token, reconnect))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<VerifySocketTokenResponse>() {
                    @Override
                    public void accept(@NonNull VerifySocketTokenResponse socketBaseResponse) throws Exception {
                        AppLogger.i("SocketBaseResponse==" + socketBaseResponse);
                        if (socketBaseResponse.getCode() == 0) {
                            AppLogger.i("发送成功 reconnect=" + reconnect);
                            //验证成功后拉取用户基本信息
                            if (!isGuestUser()) {
                                loadUserBaseData(response, loginResponse);
                            } else {
                                loginResponse.setCode(socketBaseResponse.getCode());
                                response.accept(loginResponse);
                            }
                            if ("1".equals(reconnect)) {
                                //重连成功
                                RxBus.get().send(RxBusCode.RX_BUS_CODE_SOCKET_RECONNECT_SUCCEED, new SocketConnectError());
                            }

                        } else {
                            loginResponse.setCode(AppConstants.ERROR_CODE_2);
                            response.accept(loginResponse);
//
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                });

    }


    //加载用户基础信息
    private void loadUserBaseData(final Consumer<LoginResponse> loginResponseConsumer, final LoginResponse loginResponse) {
        getDataManager()
                .doLoadUserDataBaseSocketApiCall(new LoadUserDataBaseRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoadUserDataBaseResponse>() {
                    @Override
                    public void accept(@NonNull LoadUserDataBaseResponse response) throws Exception {
                        if (response.getCode() == 0) {
                            getDataManager().saveUserData(response.getUserData());
                        }
                        loginResponse.setCode(response.getCode());
                        loginResponseConsumer.accept(loginResponse);
                    }
                }, getConsumer());

    }


    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.onAttach(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }


    public boolean checkNetConnected() {
        boolean netStat = getMvpView().isNetworkConnected();
        if (!netStat) {
            getMvpView().onToast("网络连接失败，请退出后重试!");
        }
        return netStat;
    }


    /**
     * 获得处理异常统一方法,基类处理，如有特殊需求，需要对非正常异常情况进行处理，请单独重写
     */
    public Consumer getConsumer() {
        return new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                throwable.printStackTrace();
                if (!isViewAttached()) {
                    return;
                }
                getMvpView().hideLoading();
                if (throwable instanceof ANError) {
                    ANError anError = (ANError) throwable;
                    handleApiError(anError);
                }
            }
        };
    }

}
