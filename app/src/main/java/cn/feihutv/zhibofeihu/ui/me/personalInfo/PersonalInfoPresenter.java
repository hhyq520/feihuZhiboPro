package cn.feihutv.zhibofeihu.ui.me.personalInfo;

import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import cn.feihutv.zhibofeihu.data.network.http.model.me.GetCosSignRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetCosSignResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.me.ModifyProfileLiveCoverRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.me.ModifyProfileLiveCoverResponse;
import cn.feihutv.zhibofeihu.ui.me.personalInfo.PersonalInfoMvpView;
import cn.feihutv.zhibofeihu.ui.me.personalInfo.PersonalInfoMvpPresenter;
import cn.feihutv.zhibofeihu.ui.base.BasePresenter;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.DataManager;

import com.androidnetworking.error.ANError;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * <pre>
 *     @author : liwen.chen
 *     time   :
 *     desc   : p业务处理实现类
 *     version: 1.0
 * </pre>
 */
public class PersonalInfoPresenter<V extends PersonalInfoMvpView> extends BasePresenter<V>
        implements PersonalInfoMvpPresenter<V> {


    @Inject
    public PersonalInfoPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void saveUserData(LoadUserDataBaseResponse.UserData userData) {
        getDataManager().saveUserData(userData);
    }

    @Override
    public void modifyProfileGender(final int gender) {
        getMvpView().showLoading();
        getDataManager().doModifyProfileLiveCoverApiCall(new ModifyProfileLiveCoverRequest("Gender", String.valueOf(gender)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ModifyProfileLiveCoverResponse>() {
                    @Override
                    public void accept(@NonNull ModifyProfileLiveCoverResponse modifyProfileLiveCoverResponse) throws Exception {
                        if (modifyProfileLiveCoverResponse.getCode() == 0) {
                            LoadUserDataBaseResponse.UserData userData = getUserData();
                            userData.setGender(gender);
                            saveUserData(userData);
                            getMvpView().onToast("修改成功！", Gravity.CENTER, 0, 0);
                            getMvpView().modifyProfileGenderSucc(gender);
                        } else {
                            if (modifyProfileLiveCoverResponse.getCode() == 4046) {
                                getMvpView().onToast("您已经修改过了!", Gravity.CENTER, 0, 0);
                            } else {
                                getMvpView().onToast("设置失败!", Gravity.CENTER, 0, 0);
                            }
                        }
                        getMvpView().hideLoading();
                    }
                }, getConsumer());
    }

    @Override
    public void getCosSign(int type, String ext, final String path) {
        getCompositeDisposable().add(getDataManager()
                .doGetCosSignApiCall(new GetCosSignRequest(type, ext))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetCosSignResponse>() {
                    @Override
                    public void accept(@NonNull GetCosSignResponse response) throws Exception {
                        getMvpView().notifyCosSignResponce(response, path);
                    }
                }, getConsumer())

        );
    }

    @Override
    public void modifyProfile_HeadUrl(final String v) {
        getDataManager().doModifyProfileLiveCoverApiCall(new ModifyProfileLiveCoverRequest("HeadUrl", v))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ModifyProfileLiveCoverResponse>() {
                    @Override
                    public void accept(@NonNull ModifyProfileLiveCoverResponse modifyProfileLiveCoverResponse) throws Exception {
                        if (modifyProfileLiveCoverResponse.getCode() == 0) {
                            LoadUserDataBaseResponse.UserData userData = getUserData();
                            userData.setHeadUrl(v);
                            saveUserData(userData);
                        } else {

                        }
                    }
                }, getConsumer());
    }

    @Override
    public void modifyProfile_LiveCover(final String v) {
        getDataManager().doModifyProfileLiveCoverApiCall(new ModifyProfileLiveCoverRequest("LiveCover", v))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ModifyProfileLiveCoverResponse>() {
                    @Override
                    public void accept(@NonNull ModifyProfileLiveCoverResponse modifyProfileLiveCoverResponse) throws Exception {
                        if (modifyProfileLiveCoverResponse.getCode() == 0) {
                            LoadUserDataBaseResponse.UserData userData = getUserData();
                            userData.setLiveCover(v);
                            saveUserData(userData);
                        } else {

                        }
                    }
                }, getConsumer());
    }
}
