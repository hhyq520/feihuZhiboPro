package cn.feihutv.zhibofeihu.ui.base;

import com.androidnetworking.error.ANError;

import cn.feihutv.zhibofeihu.data.network.http.model.user.LoginResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;
import io.reactivex.functions.Consumer;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/09/25
 *     desc   : view 基本persenter 操作基础类，定义view 控件界面的基本操作功能
 *     version: 1.0
 * </pre>
 */
public interface MvpPresenter <V extends MvpView> {

    void onAttach(V mvpView);

    void onDetach();

    void handleApiError(ANError error);

    void handleApiCode(int code);

    void setUserAsLoggedOut();

    boolean isGuestUser();

    //获取socket
    void enterLogin( String reconnect,Consumer<LoginResponse> response);


    String getLoinUserId();

    LoadUserDataBaseResponse.UserData getUserData();

}

