package cn.feihutv.zhibofeihu.ui.me.news;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.DataManager;
import cn.feihutv.zhibofeihu.data.db.model.NoticeSnsEntity;
import cn.feihutv.zhibofeihu.data.network.http.model.me.IsFeedExistRequset;
import cn.feihutv.zhibofeihu.data.network.http.model.me.IsFeedExistResponse;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.me.adapter.NoticeSnsAdapter;
import cn.feihutv.zhibofeihu.ui.me.dynamic.DynamicDetailActivity;
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
 *      time   : 2017/11/21 09:49
 *      desc   : 动态消息
 *      version: 1.0
 * </pre>
 */

public class NoticeSnsActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemLongClickListener {

    @BindView(R.id.notice_sms_title)
    TCActivityTitle mTitle;

    @BindView(R.id.notice_sns_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.tv_noticesns)
    TextView noNews;

    private NoticeSnsAdapter mAdapter;

    private List<NoticeSnsEntity> mNoticeSnsEntities = new ArrayList<>();
    private DataManager mDataManager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_notice_sns;
    }

    @Override
    protected void initWidget() {
        setUnBinder(ButterKnife.bind(this));

        mDataManager = FeihuZhiboApplication.getApplication().mDataManager;
        mAdapter = new NoticeSnsAdapter(mNoticeSnsEntities);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));// 布局管理器。
        mRecyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        mRecyclerView.addItemDecoration(new ListViewDecoration());// 添加分割线。
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(NoticeSnsActivity.this));

        new CompositeDisposable().add(mDataManager
                .getNoticeSnsEntity(mDataManager.getCurrentUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<NoticeSnsEntity>>() {
                    @Override
                    public void accept(@NonNull List<NoticeSnsEntity> noticeSnsEntities) throws Exception {
                        if (noticeSnsEntities.size() > 0) {
                            mNoticeSnsEntities.addAll(noticeSnsEntities);
                            mRecyclerView.setVisibility(View.VISIBLE);
                            noNews.setVisibility(View.GONE);
                            mAdapter.setNewData(noticeSnsEntities);
                        } else {
                            noNews.setVisibility(View.VISIBLE);
                            mRecyclerView.setVisibility(View.GONE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));

        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);
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

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
        showLoading();
        new CompositeDisposable().add(mDataManager
                .doIsFeedExistCall(new IsFeedExistRequset(mNoticeSnsEntities.get(position).getFeedId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<IsFeedExistResponse>() {
                    @Override
                    public void accept(@NonNull IsFeedExistResponse isFeedExistResponse) throws Exception {
                        if (isFeedExistResponse.getCode() == 0) {
                            if (isFeedExistResponse.isExist()) {
                                Intent intent = new Intent(NoticeSnsActivity.this, DynamicDetailActivity.class);
                                intent.putExtra("feedId", mNoticeSnsEntities.get(position).getFeedId());
                                if (mDataManager.getCurrentUserId().equals(mNoticeSnsEntities.get(position).getUid())) {
                                    intent.putExtra("isother_com", true);
                                }
                                startActivity(intent);
                            } else {
                                onToast("该动态已被删除", Gravity.CENTER, 0, 0);
                            }
                        } else {
                            AppLogger.i(isFeedExistResponse.getCode() + " " + isFeedExistResponse.getErrMsg());
                        }
                        hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                        hideLoading();
                    }
                }));
    }

    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        showDelete(position);
        return true;
    }

    // 清除缓存
    private void showDelete(final int position) {

        final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(NoticeSnsActivity.this);
        rxDialogSureCancel.setSure("确定");
        rxDialogSureCancel.setContent("确定要删除这条消息吗？");
        rxDialogSureCancel.setCancel("取消");
        rxDialogSureCancel.setSureListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CompositeDisposable().add(mDataManager
                        .deleteNoticeSnsEntityById(mNoticeSnsEntities.get(position).getId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(@NonNull Boolean aBoolean) throws Exception {
                                mNoticeSnsEntities.remove(position);
                                mAdapter.notifyItemRemoved(position);
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

    }
}
