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


import android.view.Gravity;

import java.util.List;

import javax.inject.Inject;

import cn.feihutv.zhibofeihu.data.DataManager;
import cn.feihutv.zhibofeihu.data.db.model.SysSignAwardBean;
import cn.feihutv.zhibofeihu.data.network.http.model.common.AwardsBean;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadSignRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadSignResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.SignRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.SignResponse;
import cn.feihutv.zhibofeihu.ui.base.BasePresenter;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/10/11
 *     desc   : Dialog p
 *     version: 1.0
 * </pre>
 */
public class SignInDialogPresenter<V extends SignInDialogMvpView> extends BasePresenter<V>
        implements SignInDialogMvpPresenter<V> {


    @Inject
    public SignInDialogPresenter(DataManager dataManager,
                                 CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }


    @Override
    public void querySignAwards() {
         getCompositeDisposable().add(getDataManager()
                         .getSysSignAwardBean()
                         .subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribe(new Consumer<List<AwardsBean>>() {
                             @Override
                             public void accept(@NonNull List<AwardsBean> awardsBeen) throws Exception {
                                 getMvpView().getAllSignAwards(awardsBeen);
                             }
                         }, getConsumer())

                 );

    }

    @Override
    public void sign() {
        getCompositeDisposable().add(getDataManager()
                .doSignApiCall(new SignRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SignResponse>() {
                    @Override
                    public void accept(@NonNull SignResponse signResponse) throws Exception {
                        if (signResponse.getCode() == 0) {
                            if (signResponse.getSignData().isSigned()) {
                                getMvpView().dismissDialog();
                                getMvpView().signView(signResponse.getSignData());
                            }
                        } else {
                            if (signResponse.getCode() == 4111) {
                                getMvpView().onToast("已经签到", Gravity.CENTER, 0, 0);
                            }
                        }
                    }
                }, getConsumer())

        );

    }
}
