package cn.feihutv.zhibofeihu.ui.live;

import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.ReportRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.ReportResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetCosSignRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetCosSignResponse;
import cn.feihutv.zhibofeihu.ui.live.ReportMvpView;
import cn.feihutv.zhibofeihu.ui.live.ReportMvpPresenter;
import cn.feihutv.zhibofeihu.ui.base.BasePresenter;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.DataManager;

import com.androidnetworking.error.ANError;

import javax.inject.Inject;

import cn.feihutv.zhibofeihu.utils.AppConstants;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * <pre>
 *     author : huang hao
 *     time   :
 *     desc   : p业务处理实现类
 *     version: 1.0
 * </pre>
 */
public class ReportPresenter<V extends ReportMvpView> extends BasePresenter<V>
        implements ReportMvpPresenter<V> {


    @Inject
    public ReportPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void getCosSign(int type, String ext, final String path) {
         getCompositeDisposable().add(getDataManager()
                         .doGetCosSignApiCall(new GetCosSignRequest(type,ext))
                         .subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribe(new Consumer<GetCosSignResponse>() {
                             @Override
                             public void accept(@NonNull GetCosSignResponse response) throws Exception {
                                getMvpView().notifyCosSignResponce(response,path);
                             }
                         },getConsumer())

                 );

    }

    @Override
    public void report(String userId, final int repType, String content, String img) {
        getCompositeDisposable().add(getDataManager()
                .doReportCall(new ReportRequest(userId,repType,content,img))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ReportResponce>() {
                    @Override
                    public void accept(@NonNull ReportResponce response) throws Exception {
                        getMvpView().reportResponce();
                        if(response.getCode()!=0){
                            AppLogger.e(response.getErrMsg());
                        }
                    }
                },getConsumer())

        );
    }
}
