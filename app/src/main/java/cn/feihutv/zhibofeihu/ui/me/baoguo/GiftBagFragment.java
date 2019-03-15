package cn.feihutv.zhibofeihu.ui.me.baoguo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LoadBagGiftsRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LoadBagGiftsResponse;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.rxbus.RxBusSubscribe;
import cn.feihutv.zhibofeihu.rxbus.ThreadMode;
import cn.feihutv.zhibofeihu.ui.base.BaseFragment;
import cn.feihutv.zhibofeihu.ui.me.OnItemClickListener;
import cn.feihutv.zhibofeihu.ui.me.adapter.GiftBagAdapter;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *      @author : liwen.chen
 *      @Date   : 2017/11/10 19:24
 *       desc   : 包裹---礼物
 *      @version: 1.0
 * </pre>
 */

public class GiftBagFragment extends BaseFragment {

    @BindView(R.id.gift_gridView)
    GridView mGridView;

    @BindView(R.id.ll_null_dz)
    LinearLayout llNullData;

    private GiftBagAdapter mAdapter;

    private boolean isFirstLoad;

    private boolean isHaveDatas;


    private List<LoadBagGiftsResponse.LoadBagGiftsResponseData> mLoadBagGiftsResponseDatas = new ArrayList<>();

    public static GiftBagFragment newInstance() {
        GiftBagFragment fragment = new GiftBagFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gift_bag;
    }

    @Override
    protected void initWidget(View view) {
        setUnBinder(ButterKnife.bind(this, view));

        isFirstLoad = true;
        mAdapter = new GiftBagAdapter(getContext());
        mGridView.setAdapter(mAdapter);
        initDatas();

    }

    private void initDatas() {

        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doLoadBagGiftsCall(new LoadBagGiftsRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoadBagGiftsResponse>() {
                    @Override
                    public void accept(@NonNull LoadBagGiftsResponse loadBagGiftsResponse) throws Exception {
                        if (loadBagGiftsResponse.getCode() == 0) {
                            if (loadBagGiftsResponse.getBagGiftsResponseDatas().size() > 0) {
                                mGridView.setVisibility(View.VISIBLE);
                                llNullData.setVisibility(View.GONE);
                                mLoadBagGiftsResponseDatas.clear();
                                /**
                                 * 判断cnt > 0才展示
                                 */
                                for (int i = 0; i < loadBagGiftsResponse.getBagGiftsResponseDatas().size(); i++) {
                                    if (loadBagGiftsResponse.getBagGiftsResponseDatas().get(i).getCnt() > 0) {
                                        LoadBagGiftsResponse.LoadBagGiftsResponseData loadBagGiftsResponseData = new LoadBagGiftsResponse.LoadBagGiftsResponseData();
                                        loadBagGiftsResponseData.setId(loadBagGiftsResponse.getBagGiftsResponseDatas().get(i).getId());
                                        loadBagGiftsResponseData.setCnt(loadBagGiftsResponse.getBagGiftsResponseDatas().get(i).getCnt());
                                        mLoadBagGiftsResponseDatas.add(loadBagGiftsResponseData);
                                        isHaveDatas = true;
                                    }

                                    for (LoadBagGiftsResponse.LoadBagGiftsResponseData loadBagGiftsResponseData : mLoadBagGiftsResponseDatas) {
                                        if (loadBagGiftsResponseData.getId() == 20) {
                                            mLoadBagGiftsResponseDatas.remove(loadBagGiftsResponseData);
                                            mLoadBagGiftsResponseDatas.add(0, loadBagGiftsResponseData);
                                            break;
                                        }
                                    }
                                }

                                if (!isHaveDatas && isFirstLoad) {
                                    llNullData.setVisibility(View.VISIBLE);
                                    mGridView.setVisibility(View.GONE);
                                }
                                AppLogger.i("mLoadBagGiftsResponseDatas", mLoadBagGiftsResponseDatas.toString());
                                mAdapter.setDatas(mLoadBagGiftsResponseDatas);
                            } else {
                                if (isFirstLoad) {
                                    llNullData.setVisibility(View.VISIBLE);
                                    mGridView.setVisibility(View.GONE);
                                }
                            }
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int id, int count) {
                if (id == 20) {
                    TransactionDialog dialog = new TransactionDialog(getContext(), count);
                    dialog.setTradeMakeSure(new TransactionDialog.TradeMakeSure() {
                        @Override
                        public void isTrading() {
                            initDatas();
                        }
                    });
                    dialog.show();

                }
            }
        });

    }

    @Override
    protected void lazyLoad() {

    }
}
