package cn.feihutv.zhibofeihu.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameBetHistoryRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameBetHistoryResponce;
import cn.feihutv.zhibofeihu.ui.widget.dialog.adapter.JcHistoryAdapter;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by huanghao on 2017/6/13.
 */

public class JcHistoryDialog extends Dialog {
    @BindView(R.id.tv_hubi)
    TextView tvHubi;
    @BindView(R.id.yin_coin)
    TextView yinCoin;
    @BindView(R.id.recycle_view)
    RecyclerView recyclerView;
    private Context mContext;
    private JcHistoryAdapter adapter;
    private String gameName;
    private boolean isPc = false;

    public JcHistoryDialog(Context context, String gameName, boolean isPc) {
        super(context, R.style.floag_dialog);
        mContext = context;
        this.gameName = gameName;
        this.isPc = isPc;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isPc) {
            setContentView(R.layout.jc_pc_history_dialog);
        } else {
            setContentView(R.layout.jc_history_dialog);
        }

        ButterKnife.bind(this);
        init();
    }

    private void init() {
        final int leftRightPadding = TCUtils.dipToPx(mContext, 15);
        final int topBottomPadding = TCUtils.dipToPx(mContext, 10);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));// 布局管理器。
        recyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        recyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(leftRightPadding, topBottomPadding, leftRightPadding, topBottomPadding);
//                super.getItemOffsets(outRect, view, parent, state);
            }
        });
        if (gameName.equals("yxjc")) {
            adapter = new JcHistoryAdapter(mContext, 1);
        } else {
            adapter = new JcHistoryAdapter(mContext, 2);
        }
        recyclerView.setAdapter(adapter);

         new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                         .doGetGameBetHistoryApiCall(new GetGameBetHistoryRequest(gameName))
                         .subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribe(new Consumer<GetGameBetHistoryResponce>() {
                             @Override
                             public void accept(@NonNull GetGameBetHistoryResponce responce) throws Exception {
                                    if(responce.getCode()==0){
                                        int hubi = responce.getGetGameBetHistoryData().getHB();
                                        int yhubi = responce.getGetGameBetHistoryData().getYHB();
                                        tvHubi.setText(String.valueOf(hubi));
                                        yinCoin.setText(String.valueOf(yhubi));
                                        adapter.setDatas(responce.getGetGameBetHistoryData().getBetLogsList());
                                    }else {
                                        AppLogger.e(responce.getErrMsg());
                                    }
                             }
                         }, new Consumer<Throwable>() {
                             @Override
                             public void accept(@NonNull Throwable throwable) throws Exception {

                             }
                         })

                 );
    }
}
