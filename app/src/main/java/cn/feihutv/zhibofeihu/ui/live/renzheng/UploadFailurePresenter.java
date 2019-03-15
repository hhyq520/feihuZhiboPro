package cn.feihutv.zhibofeihu.ui.live.renzheng;

import cn.feihutv.zhibofeihu.data.network.http.model.common.CertifiResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.common.CertifiResquest;
import cn.feihutv.zhibofeihu.ui.live.renzheng.UploadFailureMvpView;
import cn.feihutv.zhibofeihu.ui.live.renzheng.UploadFailureMvpPresenter;
import cn.feihutv.zhibofeihu.ui.base.BasePresenter;
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
 *     author : huang hao
 *     desc   : p业务处理实现类
 *     version: 1.0
 * </pre>
 */
public class UploadFailurePresenter<V extends UploadFailureMvpView> extends BasePresenter<V>
        implements UploadFailureMvpPresenter<V> {


    @Inject
    public UploadFailurePresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void getCertifiData() {
        getCompositeDisposable().add(getDataManager()
                .doGetUseCertifiDataApiCall(new CertifiResquest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CertifiResponse>() {
                    @Override
                    public void accept(@NonNull CertifiResponse certifiResponse) throws Exception {
                        if (certifiResponse.getCode() == 0) {
                            getMvpView().changeText(certifiResponse.getCertifiData().getCertifiMsg());
                        } else {
                            getMvpView().changeText("很抱歉,由于您提交的身份证与所填的内容不符,信息审核未能通过,请修改信息,重新上传");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        getMvpView().changeText("很抱歉,由于您提交的身份证与所填的内容不符,信息审核未能通过,请修改信息,重新上传");
                    }
                }));
    }
}
