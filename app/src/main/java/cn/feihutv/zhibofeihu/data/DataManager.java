/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package cn.feihutv.zhibofeihu.data;


import cn.feihutv.zhibofeihu.data.db.DbHelper;
import cn.feihutv.zhibofeihu.data.network.http.ApiHelper;
import cn.feihutv.zhibofeihu.data.network.socket.SocketApiHelper;
import cn.feihutv.zhibofeihu.data.prefs.PreferencesHelper;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/03/08
 *     desc   : 数据访问管理基础接口
 *     version: 1.0
 * </pre>
 */
public interface DataManager extends DbHelper, PreferencesHelper, ApiHelper, SocketApiHelper {

    void updateApiHeader(String userId, String accessToken, String apiKey);

    void setUserAsLoggedOut();


    //处理请求回应code 编码，返回错误码对应错误信息
    String handleResponseCode(int code);

    enum LoggedInMode {

        LOGGED_IN_MODE_LOGGED_OUT(0),
        LOGGED_IN_MODE_GUEST(1),
        LOGGED_IN_MODE_FB(2),
        LOGGED_IN_MODE_SERVER(3);

        private final int mType;

        LoggedInMode(int type) {
            mType = type;
        }

        public int getType() {
            return mType;
        }
    }
}
