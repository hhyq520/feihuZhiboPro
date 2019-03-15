package cn.feihutv.zhibofeihu.ui.me;

import android.content.Context;

import com.tencent.cos.COSClient;
import com.tencent.cos.COSClientConfig;
import com.tencent.cos.common.COSEndPoint;
import com.tencent.cos.model.PutObjectRequest;
import com.tencent.cos.model.PutObjectResult;
import com.tencent.cos.task.listener.IUploadTaskListener;

import javax.inject.Inject;

import cn.feihutv.zhibofeihu.data.DataManager;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetCosSignRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetCosSignResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.me.ModifyProfileLiveCoverRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.me.ModifyProfileLiveCoverResponse;
import cn.feihutv.zhibofeihu.ui.base.BasePresenter;
import cn.feihutv.zhibofeihu.utils.AppConstants;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * <pre>
 *     author : sichu.chen
 *     time   :
 *     desc   : p业务处理实现类
 *     version: 1.0
 * </pre>
 */
public class MyPresenter<V extends MyMvpView> extends BasePresenter<V>
        implements MyMvpPresenter<V> {


    @Inject
    public MyPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void loadUserDataBase() {
        getDataManager()
                .doLoadUserDataBaseSocketApiCall(new LoadUserDataBaseRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoadUserDataBaseResponse>() {
                    @Override
                    public void accept(@NonNull LoadUserDataBaseResponse response) throws Exception {
                       getMvpView().onLoadUserDataBaseResp(response);
                    }
                }, getConsumer());

    }


}
