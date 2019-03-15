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


import cn.feihutv.zhibofeihu.ui.base.MvpPresenter;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/10/11
 *     desc   : Dialog
 *     version: 1.0
 * </pre>
 */
public interface SignInDialogMvpPresenter<V extends SignInDialogMvpView> extends MvpPresenter<V> {

    void querySignAwards();

    void sign();//签到

}
