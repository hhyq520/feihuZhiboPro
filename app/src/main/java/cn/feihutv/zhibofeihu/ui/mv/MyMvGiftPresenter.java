package cn.feihutv.zhibofeihu.ui.mv;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cn.feihutv.zhibofeihu.data.DataManager;
import cn.feihutv.zhibofeihu.data.db.model.SysGiftNewBean;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMVGiftLogRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMVGiftLogResponse;
import cn.feihutv.zhibofeihu.ui.base.BasePresenter;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
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
public class MyMvGiftPresenter<V extends MyMvGiftMvpView> extends BasePresenter<V>
        implements MyMvGiftMvpPresenter<V> {


    @Inject
    public MyMvGiftPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void getMVGiftLog(String mvId, String offset, String count) {
         getCompositeDisposable().add(getDataManager()
                         .doGetMVGiftLogCall(new GetMVGiftLogRequest(mvId,offset,count))
                         .subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribe(new Consumer<GetMVGiftLogResponse>() {
                             @Override
                             public void accept(@NonNull GetMVGiftLogResponse response) throws Exception {

                                 if(response.getCode()==0){
                                     mGetMVGiftLogResponse=response;
                                     getMvGiftListData();
                                 }else{
                                     getMvpView().onGetMVGiftLogResp(response);
                                 }
                             }
                         }, getConsumer())

                 );
    }




    GetMVGiftLogResponse mGetMVGiftLogResponse;

    private void getMvGiftListData( ){

        Observable.create(new ObservableOnSubscribe<GetMVGiftLogResponse>() {
            @Override
            public void subscribe(ObservableEmitter<GetMVGiftLogResponse> oe) throws Exception {
                List<GetMVGiftLogResponse.GetMVGiftLog> mvGiftLogs=     mGetMVGiftLogResponse.getGetMVGiftLogData().getGetMVGiftLogs();

                List<GetMVGiftLogResponse.GetMVGiftLog> mvGiftLogList=new ArrayList<GetMVGiftLogResponse.GetMVGiftLog>();
                for(GetMVGiftLogResponse.GetMVGiftLog log:mvGiftLogs){
                    SysGiftNewBean  giftBean=getDataManager().getGiftBeanByID(log.getGiftId());
                    log.setGiftName(giftBean.getName());
                    mvGiftLogList.add(log);
                }
                mGetMVGiftLogResponse.getGetMVGiftLogData().setGetMVGiftLogs(mvGiftLogList);
                oe.onNext(mGetMVGiftLogResponse);
                oe.onComplete();

            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetMVGiftLogResponse>() {
                    @Override
                    public void accept(@NonNull GetMVGiftLogResponse response) throws Exception {

                        getMvpView().onGetMVGiftLogResp(response);
                    }
                }, getConsumer())
        ;
    }


}
