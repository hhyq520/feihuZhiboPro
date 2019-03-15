package cn.feihutv.zhibofeihu.data.prefs;

import cn.feihutv.zhibofeihu.data.DataManager;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/09/25
 *     desc   : Preferences 保存数据接口，定义所需保存的方法
 *     version: 1.0
 * </pre>
 */
public interface PreferencesHelper {


    //获取保存的登录用户uid
    String getCurrentUserId();
    void setCurrentUserId(String userId);

    //保存和获取登录状态
    int getCurrentUserLoggedInMode();
    void setCurrentUserLoggedInMode(DataManager.LoggedInMode mode);

    //保存和获取token
    String getAccessToken();
    void setAccessToken(String accessToken);

    // 当前客户端手机与服务端时间的差值
    Long getTimeChaZhi();
    void setTimeChaZhi(Long chaZhi);

    //获取保存apiKey
    String getApiKey();
    void setApiKey(String key);

    //获取服务端返回的设备id
    int getDeviceId();
    void setDeviceId(int deviceId);

    //获取个人信息
    LoadUserDataBaseResponse.UserData getUserData();
    void saveUserData(LoadUserDataBaseResponse.UserData userData);

    //保存小喇叭数量
    int getXiaolabaCount();
    void saveXiaolabaCount(int count);
}
