package cn.feihutv.zhibofeihu.ui.mv;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.data.DataManager;
import cn.feihutv.zhibofeihu.data.db.model.SysGiftNewBean;
import cn.feihutv.zhibofeihu.data.local.model.SysbagBean;
import cn.feihutv.zhibofeihu.data.network.http.model.common.BuyGoodsRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.BuyGoodsResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.FollowRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.FollowResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.CncGetPublishCodeRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.CncGetPublishCodeResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.DownLoadMvRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.DownLoadMvResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMVCommentListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMVCommentListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMVDetailRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMVDetailResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMVTokenRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMVTokenResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GiftMVRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GiftMVResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.LikeCommentRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.LikeCommentResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.LikeMVRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.LikeMVResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.PlayMVRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.PlayMVResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.PostMVCommentRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.PostMVCommentResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.ShareMvRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.user.BagRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.user.BagResponse;
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
public class MvVideoDetailsPresenter<V extends MvVideoDetailsMvpView> extends BasePresenter<V>
        implements MvVideoDetailsMvpPresenter<V> {


    String giftMvId = "";


    @Inject
    public MvVideoDetailsPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void getMVDetail(String mvId) {
        getMvpView().showLoading();
         getCompositeDisposable().add(getDataManager()
                         .doGetMVDetailCall(new GetMVDetailRequest(mvId))
                         .subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribe(new Consumer<GetMVDetailResponse>() {
                             @Override
                             public void accept(@NonNull GetMVDetailResponse response) throws Exception {
                                getMvpView().hideLoading();
                                 getMvpView().onGetMVDetailResp(response);
                             }
                         }, getConsumer())

                 );
    }

    @Override
    public void getMVCommentList(String mvId, String offset, String count) {
        getCompositeDisposable().add(getDataManager()
                .doGetMVCommentListCall(new GetMVCommentListRequest(mvId, offset, count))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetMVCommentListResponse>() {
                    @Override
                    public void accept(@NonNull GetMVCommentListResponse response) throws Exception {
                        getMvpView().onGetMVCommentListResp(response);
                    }
                }, getConsumer())

        );
    }

    @Override
    public void likeMV(String mvId) {
        getCompositeDisposable().add(getDataManager()
                .doGetLikeMVCall(new LikeMVRequest(mvId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LikeMVResponse>() {
                    @Override
                    public void accept(@NonNull LikeMVResponse response) throws Exception {
                        getMvpView().onLikeMVResp(response);
                    }
                }, getConsumer())

        );
    }

    @Override
    public void likeComment(String commentId) {
        getCompositeDisposable().add(getDataManager()
                .doLikeCommentCall(new LikeCommentRequest(commentId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LikeCommentResponse>() {
                    @Override
                    public void accept(@NonNull LikeCommentResponse response) throws Exception {
                        getMvpView().onLikeCommentResp(response);
                    }
                }, getConsumer())

        );
    }

    @Override
    public void giftMV(String mvId, String giftId, String giftCnt) {
        getCompositeDisposable().add(getDataManager()
                .doGiftMVCall(new GiftMVRequest(mvId, giftId, giftCnt))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GiftMVResponse>() {
                    @Override
                    public void accept(@NonNull GiftMVResponse response) throws Exception {
                        getMvpView().onGiftMVResp(response);
                    }
                }, getConsumer())

        );
    }

    @Override
    public void postMVComment(String mvId, String commentId, String content) {
        getCompositeDisposable().add(getDataManager()
                .doPostMVCommentCall(new PostMVCommentRequest(mvId, commentId, content))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PostMVCommentResponse>() {
                    @Override
                    public void accept(@NonNull PostMVCommentResponse response) throws Exception {
                        getMvpView().onPostMVCommentResp(response);
                    }
                }, getConsumer())

        );
    }

    @Override
    public void followUser(String uid) {
        getCompositeDisposable().add(getDataManager()
                .doFollowApiCall(new FollowRequest(uid))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FollowResponce>() {
                    @Override
                    public void accept(@NonNull FollowResponce response) throws Exception {
                        getMvpView().onFollowResp(response);
                    }
                }, getConsumer())

        );


    }

    @Override
    public void playMV(String mvId) {
        getCompositeDisposable().add(getDataManager()
                .doPlayMVCall(new PlayMVRequest(mvId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PlayMVResponse>() {
                    @Override
                    public void accept(@NonNull PlayMVResponse response) throws Exception {
                        getMvpView().onPlayMVResp(response);
                    }
                }, getConsumer())

        );
    }


    @Override
    public void requestBagData(String mvId) {
        giftMvId = mvId;
        getCompositeDisposable().
                add(getDataManager()
                        .doGetUserBagDataApiCall(new BagRequest()).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<BagResponse>() {
                            @Override
                            public void accept(@NonNull BagResponse bagResponse) throws Exception {
                                List<SysbagBean> sysbagBeanList = new ArrayList<SysbagBean>();
                                if (bagResponse.getCode() == 0) {
                                    if (bagResponse.getBagResponseData().size() > 0) {
                                        for (BagResponse.BagResponseData info : bagResponse.getBagResponseData()) {
                                            if (info.getCnt() > 0) {
                                                SysbagBean bag = new SysbagBean();
                                                bag.setCnt(info.getCnt());
                                                bag.setId(info.getId());
                                                sysbagBeanList.add(bag);
                                            }
                                        }
                                    }
                                } else {
                                    AppLogger.i(bagResponse.getErrMsg());
                                }
                                getMvpView().showGiftDialog(sysbagBeanList);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                            }
                        }));
    }


    @Override
    public void dealBagSendGift(final List<SysbagBean> sysbagBeanList, final int id, final int count) {
        SysGiftNewBean gift = getDataManager().getGiftBeanByID(String.valueOf(id));
        if (gift == null) {
            return;
        }
        if (isExistbag(sysbagBeanList, id)) {
            for (final SysbagBean item : sysbagBeanList) {
                if (item.getId() == id) {
                    if (item.getCnt() < count) {
                        final int needCount = count - item.getCnt();
                        final String gooId = getDataManager().getGoodIDByGiftID(String.valueOf(id));
                        getCompositeDisposable().add(getDataManager()
                                .doBuyGoodsApiCall(new BuyGoodsRequest(Integer.valueOf(gooId), needCount))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<BuyGoodsResponce>() {
                                    @Override
                                    public void accept(@NonNull BuyGoodsResponce responce) throws Exception {
                                        if (responce.getCode() == 0) {
                                            sendGift(sysbagBeanList, id, count);
                                        } else {
                                            sendGift(sysbagBeanList, id, item.getCnt());
                                            if (responce.getCode() == 4202) {
                                                getMvpView().onToast("余额不足,请充值");
                                            } else if (responce.getCode() == 4203) {
                                                getMvpView().onToast("礼物已下架");
                                            }
                                        }
                                    }
                                }, getConsumer()));
                    } else {
                        sendGift(sysbagBeanList, id, count);
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void dealSendGift(final List<SysbagBean> sysbagBeanList, final int id, final int count) {
        SysGiftNewBean gift = getDataManager().getGiftBeanByID(String.valueOf(id));
        if (gift == null) {
            return;
        }
        String gooId = getDataManager().getGoodIDByGiftID(String.valueOf(id));
        if (isExistbag(sysbagBeanList, id)) {
            for (final SysbagBean item : sysbagBeanList) {
                if (item.getId() == id) {
                    if (item.getCnt() < count) {
                        int needCount = count - item.getCnt();
                        getCompositeDisposable().add(getDataManager()
                                .doBuyGoodsApiCall(new BuyGoodsRequest(Integer.valueOf(gooId), needCount))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<BuyGoodsResponce>() {
                                    @Override
                                    public void accept(@NonNull BuyGoodsResponce responce) throws Exception {
                                        if (responce.getCode() == 0) {
                                            sendGift(sysbagBeanList, id, count);
                                        } else {
                                            sendGift(sysbagBeanList, id, item.getCnt());
                                            if (responce.getCode() == 4202) {
                                                getMvpView().onToast("余额不足,请充值");
                                            } else if (responce.getCode() == 4203) {
                                                getMvpView().onToast("礼物已下架");
                                            }
                                        }
                                    }
                                }, getConsumer()));
                    } else {
                        sendGift(sysbagBeanList, id, count);
                    }
                    break;
                }
            }
        } else {
            if (gift != null) {
                buyGoods(sysbagBeanList, Integer.valueOf(gooId), id, count);
            }
        }
    }

    @Override
    public void shareMv(String mvId, int to) {
        getCompositeDisposable().add(getDataManager()
                .doShareMvCall(new ShareMvRequest(mvId, to))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object obj) throws Exception {

                    }
                }, getConsumer())

        );

    }


    public void buyGoods(final List<SysbagBean> sysbagBeanList, int goodId, final int giftid, final int count) {
        getCompositeDisposable().add(getDataManager()
                .doBuyGoodsApiCall(new BuyGoodsRequest(goodId, count))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BuyGoodsResponce>() {
                    @Override
                    public void accept(@NonNull BuyGoodsResponce responce) throws Exception {
                        if (responce.getCode() == 0) {
                            sendGift(sysbagBeanList, giftid, count);
                        } else {
                            if (responce.getCode() == 4202) {
                                getMvpView().onToast("余额不足,请充值");
                            } else if (responce.getCode() == 4203) {
                                getMvpView().onToast("礼物已下架");
                            }
                            AppLogger.e(responce.getErrMsg());
                        }
                    }
                }, getConsumer()));

    }

    public void sendGift(final List<SysbagBean> sysbagBeanList, int id, int count) {

        giftMV(giftMvId, id + "", count + "");

//        getCompositeDisposable().add(getDataManager()
//                .doSendGiftApiCall(new SendGiftRequest(id, count))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<SendGiftResponce>() {
//                    @Override
//                    public void accept(@NonNull SendGiftResponce responce) throws Exception {
//                        if(responce.getCode()==0){
////                            getMvpView().sendGiftSuccess(sysbagBeanList,id,count);
//                        }else{
//                            if (responce.getCode() == 4023) {
//                                getMvpView().onToast("你已掉线，请重新登录！");
//                            } else {
//                                getMvpView().onToast("赠送失败");
//                            }
//                            AppLogger.e(responce.getErrMsg());
//                        }
//                    }
//                },getConsumer()));


    }

    private boolean isExistbag(List<SysbagBean> sysbagBeanList, int id) {
        boolean isExist = false;
        for (SysbagBean item : sysbagBeanList) {
            if (item.getId() == id) {
                isExist = true;
                break;
            }
        }
        return isExist;
    }


    @Override
    public void getDownMVUrl(String mvId) {
        getCompositeDisposable().add(getDataManager()
                .doDownLoadMvCall(new DownLoadMvRequest(mvId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DownLoadMvResponse>() {
                    @Override
                    public void accept(@NonNull DownLoadMvResponse response) throws Exception {
                        if(response.getCode()==0){
                            getMvpView().onGetDownMvUrlResp(response.getMvUrl());
                        }

                    }
                }, getConsumer())

        );

    }


    @Override
    public void getMVUrl(final String videoId){

        new CompositeDisposable().add( FeihuZhiboApplication.getApplication().mDataManager
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
                            getCncPlayUrl(request,videoId);
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                })

        );


    }


    private void getCncPlayUrl(CncGetPublishCodeRequest request, final String videoId) {

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
                                if ("200".equals(response1.getCode())) {
                                    List<CncGetPublishCodeResponse.CncVideoUrlData> mCncVideoUrlDatas =
                                            response1.getCncGetPublishCodeData().getVideoUrlData();
                                    if (mCncVideoUrlDatas != null && mCncVideoUrlDatas.size() > 0) {
                                        for (CncGetPublishCodeResponse.CncVideoUrlData cncVideoUrlData :
                                                mCncVideoUrlDatas) {
                                            if ("移动端".equals(cncVideoUrlData.getUrlType())) {
                                                AppLogger.i("移动端视频播放url== " + cncVideoUrlData.getOriginUrl());
                                                String url = cncVideoUrlData.getOriginUrl();
                                                getMvpView().onGetMvUrlResp(url);
                                                break;
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
