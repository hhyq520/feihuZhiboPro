package cn.feihutv.zhibofeihu.ui.me.dynamic;

import android.location.Location;

import java.util.List;

import javax.inject.Inject;

import cn.feihutv.zhibofeihu.data.DataManager;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LocationRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LocationResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetCosSignRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetCosSignResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.PostFeedRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.PostFeedResponse;
import cn.feihutv.zhibofeihu.ui.base.BasePresenter;
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
public class SendDynamicPresenter<V extends SendDynamicMvpView> extends BasePresenter<V>
        implements SendDynamicMvpPresenter<V> {


    @Inject
    public SendDynamicPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void getLocation(Location location) {
        String latitude = location.getLatitude() + "";
        String longitude = location.getLongitude() + "";
//        String url = "http://maps.google.cn/maps/api/geocode/json?latlng=" + latitude + "," + longitude + "&sensor=true,language=zh-CN";
        String latlng=latitude+","+longitude;
        getCompositeDisposable().add(getDataManager()
                .doGetLocationApiCall(new LocationRequest(latlng,true,"zh-CN"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LocationResponce>() {
                    @Override
                    public void accept(@NonNull LocationResponce locationResponce) throws Exception {
                        List<LocationResponce.LocationEntity> list=locationResponce.getLocationEntities();
                        for (final LocationResponce.LocationEntity locationEntity : list) {
                            if (locationEntity.getTypes().size() == 2 && locationEntity
                                    .getTypes().get(0).equals("locality") && locationEntity
                                    .getTypes().get(1).equals("political")) {
                                getMvpView().onGetLocationResp(locationEntity.getFormatted_address());
                                break;
                            }
                        }
                    }
                },getConsumer()));

    }


    @Override
    public void postFeed(PostFeedRequest request) {

         getCompositeDisposable().add(getDataManager()
                         .doPostFeedCall(request)
                         .subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribe(new Consumer<PostFeedResponse>() {
                             @Override
                             public void accept(@NonNull PostFeedResponse postFeedResponse) throws Exception {
                                 getMvpView().onPostFeedResp(postFeedResponse);
                             }
                         }, getConsumer())

                 );
    }

    @Override
    public void getCosSign(int type, String ext, final String path,final  String oldPath) {
        getCompositeDisposable().add(getDataManager()
                .doGetCosSignApiCall(new GetCosSignRequest(type,ext))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetCosSignResponse>() {
                    @Override
                    public void accept(@NonNull GetCosSignResponse response) throws Exception {
                        getMvpView().notifyCosSignResponce(response,path,oldPath);
                    }
                },getConsumer())

        );

    }
}
