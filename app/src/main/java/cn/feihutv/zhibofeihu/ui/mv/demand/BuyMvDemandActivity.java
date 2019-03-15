package cn.feihutv.zhibofeihu.ui.mv.demand;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.BuyMVResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.FeedbackMVResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMyNeedMVListResponse;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.feihutv.zhibofeihu.utils.TimeUtil;
import cn.feihutv.zhibofeihu.utils.glideutil.GlideApp;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/
 *     desc   : 购买mv
 *     version: 1.0
 * </pre>
 */
public class BuyMvDemandActivity extends BaseActivity implements BuyMvDemandMvpView {

    @Inject
    BuyMvDemandMvpPresenter<BuyMvDemandMvpView> mPresenter;

    @BindView(R.id.videoplayer)
    JZVideoPlayerStandard videoplayer;

    @BindView(R.id.mv_user_head)
    ImageView mv_user_head;

    @BindView(R.id.mv_user_ing)
    ImageView mv_user_ing;

    @BindView(R.id.mv_user_name)
    TextView mv_user_name;

    @BindView(R.id.mv_time)
    TextView mv_time;

    @BindView(R.id.mv_title)
    TextView mv_title;

    @BindView(R.id.mv_detail_songName)
    TextView mv_detail_songName;

    @BindView(R.id.mv_detail_desc)
    TextView mv_detail_desc;

    String mvId;

    GetMyNeedMVListResponse.GetMyNeedMVList myNeedMVList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_buy_mv_demand;
    }


    @Override
    protected void initWidget() {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        //Presenter调用初始化
        mPresenter.onAttach(BuyMvDemandActivity.this);


        videoplayer.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        myNeedMVList =
                (GetMyNeedMVListResponse.GetMyNeedMVList) getIntent().getSerializableExtra("myNeedMVList");
        if (myNeedMVList != null) {
            mvId = myNeedMVList.getMvId();
            GlideApp.loadImg(BuyMvDemandActivity.this, myNeedMVList.getCover(), R.drawable.bg
                    , videoplayer.thumbImageView);

            TCUtils.showPicWithUrl(BuyMvDemandActivity.this, mv_user_head,
                    myNeedMVList.getAvatar(), R.drawable.face);


            mv_user_name.setText(myNeedMVList.getNickname());

            mv_time.setText(TimeUtil.getMVTimeYYmmdd(myNeedMVList.getVPostTime()));

            mv_title.setText(myNeedMVList.getTitle());

            mv_detail_songName.setText(myNeedMVList.getSongName() + "-" + myNeedMVList.getSingerName());

            mv_detail_desc.setText(myNeedMVList.getRequire());

            mPresenter.getCncPlayUrl(myNeedMVList.getVideoId());
        }

    }


    @Override
    public void onGetCncPlayUrlResp(String url) {
        videoplayer.setUp(url
                , JZVideoPlayerStandard.SCREEN_WINDOW_FULLSCREEN, "");

    }

    @Override
    protected void onDestroy() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }


    BuyMvModifyDialog buyMvModifyDialog;
    String edesc;

    @OnClick({R.id.mv_edit, R.id.mv_buy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mv_edit:

                buyMvModifyDialog = new BuyMvModifyDialog(BuyMvDemandActivity.this);

                buyMvModifyDialog.setCancelListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buyMvModifyDialog.dismiss();
                    }
                });
                buyMvModifyDialog.setSureListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buyMvModifyDialog.dismiss();
                        edesc = buyMvModifyDialog.getEt_input_desc().getText().toString();

                        if (TextUtils.isEmpty(edesc)) {
                            onToast("内容不能为空");
                            return;
                        }
                        showHintDialog();

                    }
                });
                buyMvModifyDialog.show();
                break;
            case R.id.mv_buy:

                long hb = mPresenter.getUserData().gethB();
                if (hb < myNeedMVList.getNeedHb()) {
                    onToast("您的余额不足，赶紧充值吧");
                } else {
                    showLoading();
                    mPresenter.buyMv(mvId);

                }

                break;
        }
    }


    HintDialog hintDialog;

    private void showHintDialog() {
        hintDialog = new HintDialog(BuyMvDemandActivity.this);

        hintDialog.setSureListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Long hb = mPresenter.getUserData().gethB();
                if ((myNeedMVList.getNeedHb() * 0.2f) > hb) {
                    //余额不足
                    onToast("余额不足!请充值");
                    hintDialog.dismiss();
                    return;
                }

                if (hintDialog != null && hintDialog.isShowing()) {
                    hintDialog.dismiss();
                }

                showLoading();
                mPresenter.feedbackMV(mvId, edesc);
            }
        });
        hintDialog.setCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hintDialog.dismiss();
            }
        });
        hintDialog.show();

    }


    @Override
    public void onFeedbackMVResp(FeedbackMVResponse response) {
        hideLoading();
        if (response.getCode() == 0) {
            onToast("修改成功");
            setResult(MyDemandActivity.buyMvModifyResult);
            finish();
        } else {
            onToast("修改失败:" + response.getErrMsg());
        }

    }


    @Override
    public void onBuyMVResp(BuyMVResponse response) {

        hideLoading();
        if (response.getCode() == 0) {
            onToast("购买成功");
            setResult(MyDemandActivity.buySuccessMVResult);
            finish();
        } else {
            onToast("购买失败:" + response.getErrMsg());
        }

    }
}
