package cn.feihutv.zhibofeihu.ui.live.renzheng;

import cn.feihutv.zhibofeihu.ui.base.BasePresenter;
import cn.feihutv.zhibofeihu.ui.live.renzheng.UploadSuccessMvpView;
import cn.feihutv.zhibofeihu.ui.live.renzheng.UploadSuccessMvpPresenter;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.DataManager;
import com.androidnetworking.error.ANError;
import javax.inject.Inject;
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
public class UploadSuccessPresenter<V extends UploadSuccessMvpView> extends BasePresenter<V>
        implements UploadSuccessMvpPresenter<V> {


    @Inject
    public UploadSuccessPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

}
