package cn.feihutv.zhibofeihu.ui.home.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.BuildConfig;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.db.model.SearchHistoryInfo;
import cn.feihutv.zhibofeihu.data.network.http.model.home.RecommendResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.home.SearchResponse;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.home.adapter.RecommedHostAdapter;
import cn.feihutv.zhibofeihu.ui.home.adapter.SearchHistoryAdapter;
import cn.feihutv.zhibofeihu.ui.home.adapter.SearchResultAdapter;
import cn.feihutv.zhibofeihu.ui.me.dynamic.OthersCommunityActivity;
import cn.feihutv.zhibofeihu.ui.widget.Search_Listview;
import cn.feihutv.zhibofeihu.ui.widget.StatusView;
import cn.feihutv.zhibofeihu.utils.AppUtils;
import cn.feihutv.zhibofeihu.utils.CustomDialogUtils;
import cn.feihutv.zhibofeihu.utils.ListViewDecoration;

/**
 * <pre>
 *     author : liwen.chen
 *     time   :
 *     desc   : 模糊搜索
 *     version: 1.0
 * </pre>
 */
public class SearchUserActivity extends BaseActivity implements SearchUserMvpView {

    @Inject
    SearchUserMvpPresenter<SearchUserMvpView> mPresenter;

    public static final int START_LIVE_PLAY = 100;

    @BindView(R.id.find_edit)
    EditText mFindEdit; // 输入框

    @BindView(R.id.txt_cancel)
    TextView mTxtCancel;  // 取消

    @BindView(R.id.clear_history)
    TextView mClearHistory; // 情况历史

    @BindView(R.id.search_listView)
    Search_Listview mSearchListView; // 搜索历史列表

    @BindView(R.id.scr_history)
    ScrollView mScrHistory; // 搜索历史

    @BindView(R.id.textView4)
    TextView mTextView4; // 推荐一下主播文字

    @BindView(R.id.tv_no_result_view)
    TextView tv_no_result_view; // 搜索无结果文字

    @BindView(R.id.swipe_target)
    RecyclerView mRecyclerView; // 推荐主播列表

    @BindView(R.id.recommend_view)
    LinearLayout mRecommendView;  // 推荐历史


    @BindView(R.id.master_recycleview)
    RecyclerView mMasterRecycleview;  // 搜索列表

    @BindView(R.id.search_view)
    LinearLayout mSearchView; // 搜索结果

    @BindView(R.id.tv_content)
    TextView tv_content; // 搜索结果文字

    @BindView(R.id.view_status)
    StatusView mStatusView; // 状态

    @BindView(R.id.ll_history)
    LinearLayout llHistoty;

    private boolean isFirst = true;

    private List<RecommendResponse.RecommendResponseData> mRecommendResponseDatas = new ArrayList<>();  // 推荐主播

    private List<SearchResponse.SearchResponseData> mSearchResponseDatas = new ArrayList<>(); // 搜索返回的数据


    private RecommedHostAdapter mRecommedHostAdapter; // 推荐主播adapter

    private SearchResultAdapter mResultAdapter; // 搜索结果adapter

    private SearchHistoryAdapter mHistoryAdapter; // 搜索历史adapter

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_user;
    }

    @Override
    protected void initWidget() {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        //persenter调用初始化
        mPresenter.onAttach(SearchUserActivity.this);

        initRecommedView();

        initSearchView();
    }

    private void initSearchView() {

        mResultAdapter = new SearchResultAdapter(mSearchResponseDatas);
        mMasterRecycleview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMasterRecycleview.addItemDecoration(new ListViewDecoration());
        mMasterRecycleview.setAdapter(mResultAdapter);
        mResultAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mSearchResponseDatas != null) {
                    clickUser(mSearchResponseDatas.get(position).getUserId(), mSearchResponseDatas.get(position).getHeadUrl(), mSearchResponseDatas.get(position).getBroadcastType(), mSearchResponseDatas.get(position).isMaster());
                }
            }
        });

        mResultAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (mPresenter.isGuestUser()) {
                    if (BuildConfig.isForceLoad.equals("1")) {
                        CustomDialogUtils.showQZLoginDialog(getActivity(), false);
                    } else {
                        CustomDialogUtils.showLoginDialog(getActivity(), false);
                    }
                } else {
                    // 点击关注
                    if (mPresenter.getLoinUserId().equals(mSearchResponseDatas.get(position).getUserId())) {
                        onToast("不能关注自己哦~", Gravity.CENTER, 0, 0);
                        return;
                    }
                    mPresenter.follow(mSearchResponseDatas.get(position).getUserId(), position);

                }
            }
        });

    }

    private void initRecommedView() {

        mRecommedHostAdapter = new RecommedHostAdapter(mRecommendResponseDatas);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setAdapter(mRecommedHostAdapter);

        mRecommedHostAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mRecommendResponseDatas != null) {
                    clickUser(mRecommendResponseDatas.get(position).getRoomId(), mRecommendResponseDatas.get(position).getHeadUrl(), mRecommendResponseDatas.get(position).getBroadcastType(), true);
                }
            }
        });

        mPresenter.loadRecommed(false);
    }

    @Override
    protected void eventOnClick() {

        mFindEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.querySearch();
            }
        });

        mFindEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!TextUtils.isEmpty(mFindEdit.getText().toString().trim())) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        find();
                    }
                    return true;
                }
                return false;
            }
        });

        mClearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.deleteAllSearchInfo();
                mScrHistory.setVisibility(View.GONE);
                mRecommendView.setVisibility(View.VISIBLE);
            }
        });

        mFindEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 0) {
                    mScrHistory.setVisibility(View.GONE);
                    mSearchView.setVisibility(View.GONE);

                    if (mRecommendResponseDatas.size() > 0) {
                        mRecommendView.setVisibility(View.VISIBLE);
                        tv_no_result_view.setVisibility(View.GONE);
                    } else {
                        mRecommendView.setVisibility(View.GONE);
                    }
                } else {
                    mScrHistory.setVisibility(View.VISIBLE);
                    mRecommendView.setVisibility(View.GONE);
                    mSearchView.setVisibility(View.GONE);
                }
            }
        });

    }


    private void find() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mFindEdit.getWindowToken(), 0);
        // 搜索用户
        mPresenter.search(mFindEdit.getText().toString().trim());
        SearchHistoryInfo searchHistoryInfo = new SearchHistoryInfo();
        searchHistoryInfo.setContent(mFindEdit.getText().toString());
        searchHistoryInfo.setTime(System.currentTimeMillis());

        mPresenter.saveSearchInfo(searchHistoryInfo);
    }

    @Override
    protected void onDestroy() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroy();
    }


    @Override
    public void getDatas(List<SearchResponse.SearchResponseData> searchResponseDatas) {

        mStatusView.setVisibility(View.GONE);
        mScrHistory.setVisibility(View.GONE);
        mRecommendView.setVisibility(View.GONE);

        mSearchView.setVisibility(View.VISIBLE);

        mSearchResponseDatas.clear();
        mSearchResponseDatas.addAll(searchResponseDatas);
        mResultAdapter.setNewData(mSearchResponseDatas);
    }

    @Override
    public void showNoDatas() {
        mPresenter.loadRecommed(true);
    }

    // 关注成功
    @Override
    public void followSuccess(int position) {
        mSearchResponseDatas.get(position).setFollowed(true);
        mResultAdapter.notifyDataSetChanged();
    }

    @Override
    public void getRecommedDatas(List<RecommendResponse.RecommendResponseData> responseDatas) {
        isFirst = false;
        mRecommendResponseDatas.clear();
        mRecommendResponseDatas.addAll(responseDatas);
        mRecommedHostAdapter.setNewData(mRecommendResponseDatas);
    }

    @Override
    public void getSearchInfo(final List<SearchHistoryInfo> historyInfo) {
        if (historyInfo.size() > 0) {
            llHistoty.setVisibility(View.VISIBLE);
            mScrHistory.setVisibility(View.VISIBLE);
            mRecommendView.setVisibility(View.GONE);
            mSearchView.setVisibility(View.GONE);
            mHistoryAdapter = new SearchHistoryAdapter(getActivity(), historyInfo);
            mSearchListView.setAdapter(mHistoryAdapter);
            mHistoryAdapter.setClearHistoryListener(new SearchHistoryAdapter.ClearHistoryListener() {
                @Override
                public void click(int pos) {
                    mFindEdit.setText(historyInfo.get(pos).getContent());
                }

                @Override
                public void ivClick(int position) {
                    // 删除
                    mPresenter.deleteSearchInfoByKey(historyInfo.get(position).getContent());
                    historyInfo.remove(position);
                    if (historyInfo.size() == 0) {
                        llHistoty.setVisibility(View.GONE);
                        mRecommendView.setVisibility(View.VISIBLE);
                    } else {
                        mHistoryAdapter.notifyDataSetChanged();
                    }

                }
            });
        } else {
            llHistoty.setVisibility(View.GONE);
        }
    }

    /**
     * 推荐主播返回数据为空
     */
    @Override
    public void showNoRemmendHost() {
        if (isFirst) {
            mStatusView.setVisibility(View.VISIBLE);
            mStatusView.showNoDataView();
        } else {
            mStatusView.setVisibility(View.GONE);
        }
    }

    /**
     * 推荐主播请求失败
     */
    @Override
    public void showErrorView() {
        if (isFirst) {
            mStatusView.setVisibility(View.VISIBLE);
            mStatusView.showErrorView();
        } else {
            mStatusView.setVisibility(View.GONE);
        }
    }

    /**
     * 推荐主播请求成功并有数据返回
     */
    @Override
    public void showContent(boolean isFromSearch) {
        mRecommendView.setVisibility(View.VISIBLE);
        mTextView4.setVisibility(View.VISIBLE);
        mStatusView.setVisibility(View.GONE);
        mScrHistory.setVisibility(View.GONE);
        mSearchView.setVisibility(View.GONE);
        if (isFromSearch) {
            tv_no_result_view.setVisibility(View.VISIBLE);
        } else {
            tv_no_result_view.setVisibility(View.GONE);
        }
    }

    @Override
    public Activity getActivity() {
        return SearchUserActivity.this;
    }

    @OnClick({R.id.btn_find, R.id.txt_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_find:
                find();
                break;
            case R.id.txt_cancel:
                finish();
                break;
            default:
                break;
        }
    }

    private void clickUser(String userId, String headUrl, int broadcastType, boolean isMaster) {
        if (isMaster) {
            AppUtils.startLiveActivity(getActivity(), userId, headUrl
                    , broadcastType, true, START_LIVE_PLAY);
        } else {
            if (mPresenter.isGuestUser()) {
                // 弹出登录框
                if (BuildConfig.isForceLoad.equals("1")) {
                    CustomDialogUtils.showQZLoginDialog(getActivity(), false);
                } else {
                    CustomDialogUtils.showLoginDialog(getActivity(), false);
                }
            } else {
                if (mPresenter.getLoinUserId().equals(userId)) {
                    return;
                }
                Intent intent = new Intent(getActivity(), OthersCommunityActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        }
    }
}
