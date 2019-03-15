package cn.feihutv.zhibofeihu.ui.mv.demand;

import javax.inject.Inject;

import cn.feihutv.zhibofeihu.data.DataManager;
import cn.feihutv.zhibofeihu.ui.base.BasePresenter;
import io.reactivex.disposables.CompositeDisposable;


/**
 * <pre>
 *     author : sichu.chen
 *     time   :
 *     desc   : p业务处理实现类
 *     version: 1.0
 * </pre>
 */
public class MyDemandPresenter<V extends MyDemandMvpView> extends BasePresenter<V>
        implements MyDemandMvpPresenter<V> {


    @Inject
    public MyDemandPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

}
