package cn.feihutv.zhibofeihu.ui.me.dynamic;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomContriResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.LoadOtherUserInfoResponce;
import cn.feihutv.zhibofeihu.ui.bangdan.GuardFragment;
import cn.feihutv.zhibofeihu.ui.base.BaseFragment;
import cn.feihutv.zhibofeihu.ui.me.BaseDynamicFragment;
import cn.feihutv.zhibofeihu.ui.me.ContributionListActivity;
import cn.feihutv.zhibofeihu.ui.me.HisGuardActivity;
import cn.feihutv.zhibofeihu.ui.me.guard.GuardActivity;
import cn.feihutv.zhibofeihu.ui.widget.TCLineControllerView;
import cn.feihutv.zhibofeihu.ui.widget.scrolllayoutview.ScrollableHelper;
import cn.feihutv.zhibofeihu.utils.AppUtils;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.TCGlideCircleTransform;

/**
 * <pre>
 *     @author : liwen.chen
 *     time   : 2017/
 *     desc   : 他人社区的个人中心
 *     version: 1.0
 * </pre>
 */
public class OtherPageFragment extends BaseDynamicFragment implements OtherPageMvpView, ScrollableHelper.ScrollableContainer {

    @Inject
    OtherPageMvpPresenter<OtherPageMvpView> mPresenter;

    @BindView(R.id.con_one)
    ImageView conOne;

    @BindView(R.id.con_two)
    ImageView conTwo;

    @BindView(R.id.con_three)
    ImageView conThree;

    @BindView(R.id.tcl_shouhu)
    TCLineControllerView shouHu;

    // 送礼
    @BindView(R.id.tcl_send_gift)
    TCLineControllerView sendGift;

    // 收礼
    @BindView(R.id.tcl_get_gift)
    TCLineControllerView getGift;

    // 他的直播间
    @BindView(R.id.tcl_zhibojian)
    TCLineControllerView zhiBoJian;

    // ID号
    @BindView(R.id.tcl_idnum)
    TextView tcl_idnum;

    @BindView(R.id.view)
    View mView;

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    private String userId;

    private static final String OTHER_USER_INFO = "otherUserInfo";
    private static final String USER_ID = "userId";

    private String[] titles = {"TA的守护", "TA守护的"};

    private LoadOtherUserInfoResponce.OtherUserInfo mOtherUserInfo;

    public static OtherPageFragment newInstance(LoadOtherUserInfoResponce.OtherUserInfo otherUserInfo, String userId) {
        OtherPageFragment fragment = new OtherPageFragment();
        Bundle args = new Bundle();
        args.putParcelable(OTHER_USER_INFO, otherUserInfo);
        args.putString(USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_other_page;
    }

    @Override
    protected void initWidget(View view) {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this, view));

        //Presenter调用初始化
        mPresenter.onAttach(OtherPageFragment.this);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mOtherUserInfo = bundle.getParcelable(OTHER_USER_INFO);
            userId = bundle.getString(USER_ID);
            initUserInfo(mOtherUserInfo);
        }
    }

    private void initUserInfo(LoadOtherUserInfoResponce.OtherUserInfo otherUserInfo) {
        tcl_idnum.setText(otherUserInfo.getAccountId());
        sendGift.setContent(FHUtils.intToF(otherUserInfo.getContri()));
        getGift.setContent(FHUtils.intToF(otherUserInfo.getIncome()));
        if (otherUserInfo.getCertifiStatus() == 1) {
            zhiBoJian.setVisibility(View.VISIBLE);
            shouHu.setVisibility(View.VISIBLE);
            mView.setVisibility(View.VISIBLE);
            mPresenter.loadRoomContriList(userId);
            if (otherUserInfo.isRoomStatus()) {
                zhiBoJian.setContent("直播ing");
            } else {
                zhiBoJian.setContent("休息ing");
            }
        } else {
            zhiBoJian.setVisibility(View.GONE);
            shouHu.setVisibility(View.GONE);
            mView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void lazyLoad() {

    }


    @Override
    public void onDestroyView() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroyView();
    }


    @OnClick({R.id.contribution_next, R.id.tcl_idnum, R.id.tcl_shouhu, R.id.tcl_zhibojian})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.contribution_next:
                if (mOtherUserInfo.getCertifiStatus() == 1) {
                    Intent intent = new Intent(getContext(), ContributionListActivity.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("noDataTitle", "暂无贡献哦，去TA的直播间送点吧~");
                    startActivity(intent);
                } else {
                    onToast("TA还不是主播，暂无粉丝贡献榜", Gravity.CENTER, 0, 0);
                }
                break;
            case R.id.tcl_idnum:
                ClipboardManager clipboard = (ClipboardManager) getContext()
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("id", mOtherUserInfo.getAccountId());
                clipboard.setPrimaryClip(clip);
                onToast("已复制", Gravity.CENTER, 0, 0);
                break;
            case R.id.tcl_shouhu:
                Intent intent = new Intent(getContext(), HisGuardActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("otherUserInfo", mOtherUserInfo);
                startActivity(intent);
                break;
            case R.id.tcl_zhibojian:
                goLiveRoom(mOtherUserInfo.getUserId(), mOtherUserInfo.getHeadUrl(), mOtherUserInfo.getBroadcastType(), true, 100);
                break;
            default:
                break;
        }
    }

    @Override
    public void getContriDatas(List<LoadRoomContriResponce.RoomContriData> roomContriDataList) {
        int size = roomContriDataList.size();
        switch (size) {
            case 0:
                conOne.setVisibility(View.INVISIBLE);
                conTwo.setVisibility(View.INVISIBLE);
                conThree.setVisibility(View.INVISIBLE);
                break;
            case 1:
                Glide.with(getContext()).load(roomContriDataList.get(0).getHeadUrl()).apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .transform(new TCGlideCircleTransform(getContext())))
                        .into(conThree);
                conOne.setVisibility(View.INVISIBLE);
                conTwo.setVisibility(View.INVISIBLE);
                conThree.setVisibility(View.VISIBLE);
                break;
            case 2:
                Glide.with(getContext()).load(roomContriDataList.get(0).getHeadUrl()).apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .transform(new TCGlideCircleTransform(getContext())))
                        .into(conTwo);

                Glide.with(getContext()).load(roomContriDataList.get(1).getHeadUrl()).apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .transform(new TCGlideCircleTransform(getContext())))
                        .into(conThree);
                conOne.setVisibility(View.INVISIBLE);
                conTwo.setVisibility(View.VISIBLE);
                conThree.setVisibility(View.VISIBLE);
                break;
            case 3:
            default:
                Glide.with(getContext()).load(roomContriDataList.get(0).getHeadUrl()).apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .transform(new TCGlideCircleTransform(getContext())))
                        .into(conOne);

                Glide.with(getContext()).load(roomContriDataList.get(1).getHeadUrl()).apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .transform(new TCGlideCircleTransform(getContext())))
                        .into(conTwo);

                Glide.with(getContext()).load(roomContriDataList.get(2).getHeadUrl()).apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .transform(new TCGlideCircleTransform(getContext())))
                        .into(conThree);
                conOne.setVisibility(View.VISIBLE);
                conTwo.setVisibility(View.VISIBLE);
                conThree.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public View getScrollableView() {
        return scrollView;
    }

    @Override
    public void pullToRefresh() {

    }

    @Override
    public void refreshComplete() {

    }

    private void goLiveRoom(String userId, String headUrl, int broadType, boolean isShowWindow, int requestCode) {
        AppUtils.startLiveActivity(OtherPageFragment.this, userId, headUrl, broadType, isShowWindow, requestCode);
    }
}
