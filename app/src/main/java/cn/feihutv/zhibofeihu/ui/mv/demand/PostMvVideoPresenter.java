package cn.feihutv.zhibofeihu.ui.mv.demand;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import javax.inject.Inject;

import cn.feihutv.zhibofeihu.data.DataManager;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetCosSignRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetCosSignResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.CncGetUploadTokenRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.CncGetUploadTokenResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.EnablePostMVRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.EnablePostMVResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetAllNeedListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetAllNeedListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMVTokenRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMVTokenResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.PostMvRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.PostMvResponse;
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
public class PostMvVideoPresenter<V extends PostMvVideoMvpView> extends BasePresenter<V>
        implements PostMvVideoMvpPresenter<V> {


    @Inject
    public PostMvVideoPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void getCncUploadToken(final String originFileName, final String fileMd5, final String originFileSize ) {

         getCompositeDisposable().add(getDataManager()
                         .doGetMVTokenCall(new GetMVTokenRequest())
                         .subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribe(new Consumer<GetMVTokenResponse>() {
                             @Override
                             public void accept(@NonNull GetMVTokenResponse response) throws Exception {
                                 if(response.getCode()==0) {
                                     GetMVTokenResponse.GetMVTokenData mvTokenData=response.getGetMVTokenData();
                                     CncGetUploadTokenRequest request=new CncGetUploadTokenRequest(
                                      mvTokenData.getUserId(),mvTokenData.getToken(),mvTokenData.getTimeStamp()
                                     );
                                     request.setOriginFileName(originFileName);
                                     request.setFileMd5(fileMd5);
                                     request.setOriginFileSize(originFileSize);
                                     request.setOverwrite("1");
                                     executeGetCncUploadToken(request);
                                 }
                             }
                         }, getConsumer())

                 );


    }



    private void executeGetCncUploadToken(CncGetUploadTokenRequest request){

        //请求网宿接口 进行鉴权 获取视频播放地址
        AndroidNetworking.post("http://api.cloudv.haplat.net/vod/videoManage/getUploadToken")
                .addBodyParameter(request)
                .setTag("getUploadToken")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(CncGetUploadTokenResponse.class,
                        new ParsedRequestListener<CncGetUploadTokenResponse>() {
                            @Override
                            public void onResponse(CncGetUploadTokenResponse response1) {
                                // do anything with response

                                if(response1.getCode()==200){
                                    getMvpView().onGetCncUploadTokenResp(response1);
                                }

                                AppLogger.i("获取上传视频token："+response1.toString());
                            }
                            @Override
                            public void onError(ANError anError) {
                                // handle error

                            }
                        });

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
    public void postMV(String title, String cover, String videoId, String needId) {
        getCompositeDisposable().add(getDataManager()
                .doPostMvCall(new PostMvRequest(title,cover,videoId,needId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PostMvResponse>() {
                    @Override
                    public void accept(@NonNull PostMvResponse response) throws Exception {
                        getMvpView().onPostMvResp(response);
                    }
                }, getConsumer())

        );

    }


    @Override
    public void getAllNeedList(String forPostMV, String forMe, String offset, String count) {
        getCompositeDisposable().add(getDataManager()
                .doGetAllNeedListCall(new GetAllNeedListRequest(forPostMV,forMe,offset,count))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetAllNeedListResponse>() {
                    @Override
                    public void accept(@NonNull GetAllNeedListResponse response) throws Exception {

                        getMvpView().onGetAllNeedList(response);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        getMvpView().onToast("请求失败，请稍后重试!");
                        getMvpView().onGetAllNeedList(null);
                    }
                })

        );
    }


    @Override
    public void enablePostMV(String mvId) {
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .doEnablePostMVCall(new EnablePostMVRequest(mvId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<EnablePostMVResponse>() {
                    @Override
                    public void accept(@NonNull EnablePostMVResponse response) throws Exception {
                        getMvpView().hideLoading();
                        getMvpView().onEnablePostMVResp(response);
                    }
                }, getConsumer())

        );
    }

}
