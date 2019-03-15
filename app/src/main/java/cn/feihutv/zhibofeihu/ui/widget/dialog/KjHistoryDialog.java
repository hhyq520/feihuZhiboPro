package cn.feihutv.zhibofeihu.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameSettleHistoryRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameSettleHistoryResponce;
import cn.feihutv.zhibofeihu.ui.widget.dialog.adapter.KjhistoryAdapter;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.ListViewDecoration;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by huanghao on 2017/6/13.
 */

public class KjHistoryDialog extends Dialog {
    @BindView(R.id.recycle_view)
    RecyclerView recyclerView;
    private Context mContext;
    private KjhistoryAdapter adapter;
    private boolean isPc=false;
    public KjHistoryDialog(Context context, boolean isPc) {
        super(context, R.style.floag_dialog);
        mContext = context;
        this.isPc=isPc;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isPc){
            setContentView(R.layout.kj_pc_history_dialog);
        }else{
            setContentView(R.layout.kj_history_dialog);
        }

        ButterKnife.bind(this);
        init();
    }
    private void init(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));// 布局管理器。
        recyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        recyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        recyclerView.addItemDecoration(new ListViewDecoration(4));// 添加分割线。
        adapter=new KjhistoryAdapter(mContext,false);
        recyclerView.setAdapter(adapter);
        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doGetGameSettleHistoryApiCall(new GetGameSettleHistoryRequest("yxjc"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetGameSettleHistoryResponce>() {
                    @Override
                    public void accept(@NonNull GetGameSettleHistoryResponce responce) throws Exception {
                        if(responce.getCode()==0){
                            List<String> datas=responce.getDatas();
                            adapter.setDatas(datas);
                        }else{
                            AppLogger.e(responce.getErrMsg());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));
    }
}
