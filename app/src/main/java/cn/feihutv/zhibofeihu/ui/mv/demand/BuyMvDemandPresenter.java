package cn.feihutv.zhibofeihu.ui.mv.demand;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.util.List;

import javax.inject.Inject;

import cn.feihutv.zhibofeihu.data.DataManager;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.BuyMVRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.BuyMVResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.CncGetPublishCodeRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.CncGetPublishCodeResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.FeedbackMVRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.FeedbackMVResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMVTokenRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMVTokenResponse;
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
 *     time   :
 *     desc   : p业务处理实现类
 *     version: 1.0
 * </pre>
 */
public class BuyMvDemandPresenter<V extends BuyMvDemandMvpView> extends BasePresenter<V>
        implements BuyMvDemandMvpPresenter<V> {


    @Inject
    public BuyMvDemandPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void buyMv(String mvId) {
         getCompositeDisposable().add(getDataManager()
                         .doBuyMVCall(new BuyMVRequest(mvId))
                         .subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribe(new Consumer<BuyMVResponse>() {
                             @Override
                             public void accept(@NonNull BuyMVResponse response) throws Exception {
                                 getMvpView().onBuyMVResp(response);
                             }
                         }, getConsumer())

                 );
    }

    @Override
    public void feedbackMV(String mvId, String require) {
        getCompositeDisposable().add(getDataManager()
                .doFeedbackMVCall(new FeedbackMVRequest(mvId,require))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FeedbackMVResponse>() {
                    @Override
                    public void accept(@NonNull FeedbackMVResponse response) throws Exception {
                        getMvpView().onFeedbackMVResp(response);
                    }
                }, getConsumer())

        );
    }

    @Override
    public void getCncPlayUrl(final String videoId) {

         getCompositeDisposable().add(getDataManager()
                         .doGetMVTokenCall(new GetMVTokenRequest())
                         .subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribe(new Consumer<GetMVTokenResponse>() {
                             @Override
                             public void accept(@NonNull GetMVTokenResponse response) throws Exception {
                                 if(response.getCode()==0) {

                                     GetMVTokenResponse.GetMVTokenData mvTokenData= response.getGetMVTokenData();
                                     CncGetPublishCodeRequest request =
                                             new CncGetPublishCodeRequest(mvTokenData.getUserId(),
                                                     mvTokenData.getToken(), mvTokenData.getTimeStamp(),videoId);
                                     request.setFormat("json");
                                     request.setCodeType(4);
                                     executeGetCncPlayUrl(request,videoId);
                                 }
                             }
                         }, getConsumer())

                 );

    }



    private void executeGetCncPlayUrl(CncGetPublishCodeRequest request, String videoId) {

        //请求网宿接口 进行鉴权 获取视频播放地址
        AndroidNetworking.post("http://api.cloudv.haplat.net/vod/videoManage/getPublishCode")
                .addBodyParameter(request)
                .setTag("getPublishCode")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(CncGetPublishCodeResponse.class,
                        new ParsedRequestListener<CncGetPublishCodeResponse>() {
                            @Override
                            public void onResponse(CncGetPublishCodeResponse response1) {
                                // do anything with response
                                if("200".equals(response1.getCode())){
                                    List<CncGetPublishCodeResponse.CncVideoUrlData> mCncVideoUrlDatas=
                                            response1.getCncGetPublishCodeData().getVideoUrlData();
                                    if(mCncVideoUrlDatas!=null&&mCncVideoUrlDatas.size()>0){
                                        for(CncGetPublishCodeResponse.CncVideoUrlData cncVideoUrlData:
                                                mCncVideoUrlDatas) {
                                            if("移动端".equals(cncVideoUrlData.getUrlType())) {
                                                AppLogger.i("移动端视频播放url== "+cncVideoUrlData.getOriginUrl());
                                                String url = cncVideoUrlData.getOriginUrl();
//
                                                getMvpView().onGetCncPlayUrlResp(url);

                                            }
                                        }
                                    }

                                }

                            }
                            @Override
                            public void onError(ANError anError) {
                                // handle error
                            }
                        });



    }
}
