package cn.feihutv.zhibofeihu.ui.widget;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetJackpotDataRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetJackpotDataResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetLuckLogsByIdRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetLuckLogsByIdResponce;
import cn.feihutv.zhibofeihu.data.network.socket.xsocket.IFHCallBack;
import cn.feihutv.zhibofeihu.ui.widget.dialog.adapter.CaichiGiftAdapter;
import cn.feihutv.zhibofeihu.ui.widget.recyclerloadmore.EndlessRecyclerOnScrollListener;
import cn.feihutv.zhibofeihu.ui.widget.recyclerloadmore.LoadingFooter;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.ListViewDecoration;
import cn.feihutv.zhibofeihu.utils.weiget.view.wraprecyclerview.WrapRecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by huanghao on 2017/6/19.
 */

public class CaichiView {
    @BindView(R.id.recycle_view)
    WrapRecyclerView recycleView;
    private Context context;
    private View rootView;
    CaichiGiftAdapter adapter;
    private int id;
    private int offset = 0;
    private List<GetLuckLogsByIdResponce.LuckLogs> datas;
    private LoadingFooter mLoadingFooter;
    private boolean isPcLand;
    public CaichiView(Context mContext, int id,boolean isPcLand) {
        this.context = mContext;
        rootView = LayoutInflater.from(mContext).inflate(
                R.layout.caichi_view, null);
        ButterKnife.bind(this, rootView);
        this.id = id;
        this.isPcLand=isPcLand;
        datas = new ArrayList<>();
        init();
    }

    public View getView() {
        return rootView;
    }

    private void init() {
        recycleView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recycleView.setLayoutManager(mLayoutManager);
        //这句就是添加我们自定义的分隔线
        recycleView.addItemDecoration(new ListViewDecoration(1));
        recycleView.setItemAnimator(new DefaultItemAnimator());
        adapter = new CaichiGiftAdapter(context, id,isPcLand);
        recycleView.setAdapter(adapter);
        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doGetLuckLogsByIdApiCall(new GetLuckLogsByIdRequest(id, 0, 20))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetLuckLogsByIdResponce>() {
                    @Override
                    public void accept(@NonNull GetLuckLogsByIdResponce getLuckLogsByIdResponce) throws Exception {
                        if(getLuckLogsByIdResponce.getCode()==0) {
                            offset = getLuckLogsByIdResponce.getLuckLogsById().getNextOffset();
                            datas =getLuckLogsByIdResponce.getLuckLogsById().getLuckLogsList();
                            adapter.setDatas(datas);
                        }else{
                            AppLogger.e(getLuckLogsByIdResponce.getErrMsg());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));
        recycleView.addOnScrollListener(mOnScrollListener);
        if (mLoadingFooter == null) {
            mLoadingFooter = new LoadingFooter(context);
            //adapter.setFooterView(mLoadingFooter);
        }
    }

    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener(2) {
        @Override
        public void onLoadMore(View view) {
            super.onLoadMore(view);
            loadMoreDatas();
        }
    };

    private void loadMoreDatas() {
        if (datas.size() <= 0) {
            return;
        }
        if (mLoadingFooter.getState() == LoadingFooter.State.Loading || (mLoadingFooter.getState() == LoadingFooter.State.TheEnd)) {
            return;
        }
        mLoadingFooter.setState(LoadingFooter.State.Loading);
        adapter.setFooterView(mLoadingFooter);
        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doGetLuckLogsByIdApiCall(new GetLuckLogsByIdRequest(id, offset, 20))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetLuckLogsByIdResponce>() {
                    @Override
                    public void accept(@NonNull GetLuckLogsByIdResponce getLuckLogsByIdResponce) throws Exception {
                        if(getLuckLogsByIdResponce.getCode()==0) {
                            offset = getLuckLogsByIdResponce.getLuckLogsById().getNextOffset();
                            if (getLuckLogsByIdResponce.getLuckLogsById().getLuckLogsList() == null) {
                                mLoadingFooter.setState(LoadingFooter.State.TheEnd, false);
                                adapter.removeFooterView();
                            } else {
                                mLoadingFooter.setState(LoadingFooter.State.Normal);
                                adapter.removeFooterView();
                            }
                            if (getLuckLogsByIdResponce.getLuckLogsById().getLuckLogsList() != null && getLuckLogsByIdResponce.getLuckLogsById().getLuckLogsList().size() > 0) {
                                datas.addAll(getLuckLogsByIdResponce.getLuckLogsById().getLuckLogsList());
                                adapter.setDatas(datas);
                            }
                        }else{
                            mLoadingFooter.setState(LoadingFooter.State.TheEnd, false);
                            adapter.removeFooterView();
                            AppLogger.e(getLuckLogsByIdResponce.getErrMsg());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        mLoadingFooter.setState(LoadingFooter.State.TheEnd, false);
                        adapter.removeFooterView();
                    }
                }));
    }
}
