package cn.feihutv.zhibofeihu.ui.me.news;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.DataManager;
import cn.feihutv.zhibofeihu.data.db.model.NoticeSysEntity;
import cn.feihutv.zhibofeihu.data.network.http.model.me.AcceptTradeRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.AcceptTradeResponse;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.me.adapter.NoticeSysAdapter;
import cn.feihutv.zhibofeihu.ui.widget.TCActivityTitle;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.ListViewDecoration;
import cn.feihutv.zhibofeihu.utils.weiget.dialog.RxDialogSureCancel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/21 16:33
 *      desc   : 消息---系统消息
 *      version: 1.0
 * </pre>
 */

public class NoticeSysActivity extends BaseActivity {

    @BindView(R.id.notice_sms_title)
    TCActivityTitle mTitle;

    @BindView(R.id.notice_sms_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.tv)
    TextView noDatas;
    private DataManager mDataManager;

    private NoticeSysAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_notice_sys;
    }

    @Override
    protected void initWidget() {
        setUnBinder(ButterKnife.bind(this));
        mDataManager = FeihuZhiboApplication.getApplication().mDataManager;

        mAdapter = new NoticeSysAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));// 布局管理器。
        mRecyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        mRecyclerView.addItemDecoration(new ListViewDecoration());// 添加分割线。
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(NoticeSysActivity.this));

        mAdapter.setAcceptRequestListener(new NoticeSysAdapter.AcceptRequestListener() {
            @Override
            public void accept(NoticeSysEntity sysEntity) {
                showAcceptDialog(sysEntity);
            }
        });


        initDatas();
    }

    private void initDatas() {
        new CompositeDisposable().add(mDataManager
                .queryNoticeSysByUid(mDataManager.getCurrentUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<NoticeSysEntity>>() {
                    @Override
                    public void accept(@NonNull List<NoticeSysEntity> noticeSysEntities) throws Exception {
                        if (noticeSysEntities.size() > 0) {
                            mRecyclerView.setVisibility(View.VISIBLE);
                            noDatas.setVisibility(View.GONE);

                            mAdapter.setDatas(noticeSysEntities);
                        } else {
                            mRecyclerView.setVisibility(View.GONE);
                            noDatas.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));
    }

    private void showAcceptDialog(final NoticeSysEntity sysEntity) {

        final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(this);
        rxDialogSureCancel.setContent("确定支付" + sysEntity.getAmount() + "虎币给" + sysEntity.getNickName() + "(" + sysEntity.getAccountId() + ")" + "吗?点击确定完成支付");

        rxDialogSureCancel.setSure("确定");
        rxDialogSureCancel.setCancel("取消");
        rxDialogSureCancel.setSureListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CompositeDisposable().add(mDataManager
                        .doAcceptTradeCall(new AcceptTradeRequest(sysEntity.getTradeld(), sysEntity.getUserId()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<AcceptTradeResponse>() {
                            @Override
                            public void accept(@NonNull AcceptTradeResponse acceptTradeResponse) throws Exception {
                                if (acceptTradeResponse.getCode() == 0) {
                                    onToast("支付成功", Gravity.CENTER, 0, 0);
                                    sysEntity.setIsAccept(true);
                                    new CompositeDisposable().add(mDataManager
                                            .updateNoticeSys(sysEntity)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Consumer<Boolean>() {
                                                @Override
                                                public void accept(@NonNull Boolean aBoolean) throws Exception {

                                                }
                                            }, new Consumer<Throwable>() {
                                                @Override
                                                public void accept(@NonNull Throwable throwable) throws Exception {
                                                    mAdapter.notifyDataSetChanged();
//                                                    requestSysbag();
                                                }
                                            }));
                                } else {
                                    if (acceptTradeResponse.getCode() == 4321) {
                                        onToast("交易已过期", Gravity.CENTER, 0, 0);
                                    } else if (acceptTradeResponse.getCode() == 4202) {
                                        onToast("余额不足", Gravity.CENTER, 0, 0);
                                    } else {
                                        AppLogger.i(acceptTradeResponse.getCode() + " " + acceptTradeResponse.getErrMsg());
                                    }
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {

                            }
                        }));
                rxDialogSureCancel.dismiss();
            }
        });

        rxDialogSureCancel.setCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rxDialogSureCancel.dismiss();
            }
        });

        rxDialogSureCancel.show();
    }

    @Override
    protected void eventOnClick() {

        mTitle.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
