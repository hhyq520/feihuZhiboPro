package cn.feihutv.zhibofeihu.ui.home.history;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.db.model.HistoryRecordBean;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomResponce;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.ui.home.adapter.WatchHistoryAdapter;
import cn.feihutv.zhibofeihu.ui.widget.TCActivityTitle;
import cn.feihutv.zhibofeihu.utils.AppUtils;
import cn.feihutv.zhibofeihu.utils.FloatViewWindowManger;
import cn.feihutv.zhibofeihu.utils.ListViewDecoration;
import cn.feihutv.zhibofeihu.utils.weiget.dialog.RxDialogSureCancel;

/**
 * <pre>
 *     author : liwen.chen
 *     time   :
 *     desc   : 观看历史记录
 *     version: 1.0
 * </pre>
 */
public class HistoryActivity extends BaseActivity implements HistoryMvpView {

    @Inject
    HistoryMvpPresenter<HistoryMvpView> mPresenter;

    @BindView(R.id.history_title)
    TCActivityTitle mTitle;

    @BindView(R.id.history_rl)
    RecyclerView mRecyclerView;

    @BindView(R.id.tv_record)
    TextView mTextView;

    private WatchHistoryAdapter mAdapter;

    private List<HistoryRecordBean> mRecordBeanList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_history;
    }

    private boolean isNoData;

    @Override
    protected void initWidget() {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        //persenter调用初始化
        mPresenter.onAttach(HistoryActivity.this);

        StatusBarUtil.setColor(getActivity(), getResources().getColor(R.color.app_white));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new ListViewDecoration());
        mAdapter = new WatchHistoryAdapter(mRecordBeanList);
        mRecyclerView.setAdapter(mAdapter);

        mPresenter.queryHistory();

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mPresenter.loadRoomById(mRecordBeanList.get(position).getRoomId());
            }
        });
    }

    @Override
    protected void eventOnClick() {
        mTitle.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mTitle.setMoreListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 点击清空
                if (!isNoData) {
                    final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(getActivity(), R.style.color_dialog);//提示弹窗
                    rxDialogSureCancel.setContent("确定要清空所有历史记录？");
                    rxDialogSureCancel.setSure("确定");
                    rxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mPresenter.deleteAllHistory();
                            mRecordBeanList.clear();
                            isNoData = true;
                            mAdapter.notifyDataSetChanged();
                            rxDialogSureCancel.cancel();
                        }
                    });
                    rxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            rxDialogSureCancel.cancel();
                        }
                    });
                    rxDialogSureCancel.show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroy();
    }


    @Override
    public Activity getActivity() {
        return HistoryActivity.this;
    }

    @Override
    public void getAllHistory(List<HistoryRecordBean> recordBeanList) {
        mRecordBeanList.clear();
        if (recordBeanList.size() >0) {
            mRecordBeanList.addAll(recordBeanList);
            isNoData = false;
            mTextView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mAdapter.setNewData(mRecordBeanList);
        } else {
            isNoData = true;
            mTextView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void gotoRoom(LoadRoomResponce.LoadRoomData loadRoomData) {
        FloatViewWindowManger.removeSmallWindow();
        // 1为PC   2为手机
        AppUtils.startLiveActivity(getActivity(), loadRoomData.getRoomId(), loadRoomData.getHeadUrl(), loadRoomData.getBroadcastType(), true);
    }

}
