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

package cn.feihutv.zhibofeihu.ui.home.signin;


import java.util.List;

import cn.feihutv.zhibofeihu.data.db.model.SysSignAwardBean;
import cn.feihutv.zhibofeihu.data.network.http.model.common.AwardsBean;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadSignResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.SignResponse;
import cn.feihutv.zhibofeihu.ui.base.DialogMvpView;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/10/11
 *     desc   : Dialog 界面
 *     version: 1.0
 * </pre>
 */
public interface SignInDialogMvpView extends DialogMvpView {


    void dismissDialog();

    void signView(SignResponse.SignData signData);

    void getAllSignAwards(List<AwardsBean> awardsBeen);
}
