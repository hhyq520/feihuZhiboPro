package cn.feihutv.zhibofeihu.ui.me.setting;

import cn.feihutv.zhibofeihu.data.network.http.model.me.GetMsgSwitchStatusRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetMsgSwitchStatusResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.SetMsgSwitchStatusRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.SetMsgSwitchStatusResponse;
import cn.feihutv.zhibofeihu.ui.me.setting.MsgReceSettingsMvpView;
import cn.feihutv.zhibofeihu.ui.me.setting.MsgReceSettingsMvpPresenter;
import cn.feihutv.zhibofeihu.ui.base.BasePresenter;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.DataManager;

import com.androidnetworking.error.ANError;

import javax.inject.Inject;

import cn.feihutv.zhibofeihu.utils.AppLogger;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * <pre>
 *     author : liwen.chen
 *     time   :
 *     desc   : p业务处理实现类
 *     version: 1.0
 * </pre>
 */
public class MsgReceSettingsPresenter<V extends MsgReceSettingsMvpView> extends BasePresenter<V>
        implements MsgReceSettingsMvpPresenter<V> {


    @Inject
    public MsgReceSettingsPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void getMsgSwitchStatus() {

        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .doGetMsgSwitchStatusCall(new GetMsgSwitchStatusRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetMsgSwitchStatusResponse>() {
                    @Override
                    public void accept(@NonNull GetMsgSwitchStatusResponse getMsgSwitchStatusResponse) throws Exception {
                        if (getMsgSwitchStatusResponse.getCode() == 0) {
                            getMvpView().getMsgSwitchStatus(getMsgSwitchStatusResponse.getSwitchStatusResponseData());
                        } else {
                            AppLogger.i(getMsgSwitchStatusResponse.getCode() + " " + getMsgSwitchStatusResponse.getErrMsg());
                        }
                        getMvpView().hideLoading();
                    }
                }, getConsumer())

        );

    }

    @Override
    public void setMsgSwitchStatus(String key, String value, final int type) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .doSetMsgSwitchStatusCall(new SetMsgSwitchStatusRequest(value, key))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SetMsgSwitchStatusResponse>() {
                    @Override
                    public void accept(@NonNull SetMsgSwitchStatusResponse setMsgSwitchStatusResponse) throws Exception {
                        if (setMsgSwitchStatusResponse.getCode() == 0) {
                            getMvpView().setMsgStatusSucc(type);
                        } else {
                            AppLogger.i(setMsgSwitchStatusResponse.getCode() + " " + setMsgSwitchStatusResponse.getErrMsg());
                        }
                        getMvpView().hideLoading();
                    }
                }, getConsumer())

        );

    }
}
