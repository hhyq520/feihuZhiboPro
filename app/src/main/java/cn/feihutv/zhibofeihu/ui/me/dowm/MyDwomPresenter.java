package cn.feihutv.zhibofeihu.ui.me.dowm;

import java.util.List;

import javax.inject.Inject;

import cn.feihutv.zhibofeihu.data.DataManager;
import cn.feihutv.zhibofeihu.data.db.model.MvDownLog;
import cn.feihutv.zhibofeihu.ui.base.BasePresenter;
import io.reactivex.disposables.CompositeDisposable;


/**
 * <pre>
 *     author : liwen.chen
 *     time   :
 *     desc   : p业务处理实现类
 *     version: 1.0
 * </pre>
 */
public class MyDwomPresenter<V extends MyDwomMvpView> extends BasePresenter<V>
        implements MyDwomMvpPresenter<V> {


    @Inject
    public MyDwomPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public List<MvDownLog> getMyMvDownLog() {

        return getDataManager().getMvDownLogList(getLoinUserId());
    }
}
