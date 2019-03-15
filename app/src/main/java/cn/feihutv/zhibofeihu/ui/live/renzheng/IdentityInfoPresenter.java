package cn.feihutv.zhibofeihu.ui.live.renzheng;import cn.feihutv.zhibofeihu.ui.live.renzheng.IdentityInfoMvpView;
import cn.feihutv.zhibofeihu.ui.live.renzheng.IdentityInfoMvpPresenter;
import cn.feihutv.zhibofeihu.ui.base.BasePresenter;
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
public class IdentityInfoPresenter<V extends IdentityInfoMvpView> extends BasePresenter<V>
        implements IdentityInfoMvpPresenter<V> {


    @Inject
    public IdentityInfoPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

}
