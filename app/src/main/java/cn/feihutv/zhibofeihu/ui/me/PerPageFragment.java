package cn.feihutv.zhibofeihu.ui.me;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomContriRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomContriResponce;
import cn.feihutv.zhibofeihu.ui.me.adapter.PersonPageModel;
import cn.feihutv.zhibofeihu.ui.widget.TCLineControllerView;
import cn.feihutv.zhibofeihu.ui.widget.scrolllayoutview.ScrollableHelper;
import cn.feihutv.zhibofeihu.utils.TCGlideCircleTransform;
import cn.feihutv.zhibofeihu.utils.glideutil.GlideApp;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Administrator on 2017/2/25.
 */

public class PerPageFragment extends BaseDynamicFragment implements
        ScrollableHelper.ScrollableContainer {
    @BindView(R.id.con_one)
    ImageView conOne;
    @BindView(R.id.con_two)
    ImageView conTwo;
    @BindView(R.id.con_three)
    ImageView conThree;
    @BindView(R.id.contribution_next)
    RelativeLayout contributionNext;
    @BindView(R.id.tcl_idnum)
    TCLineControllerView tclIdnum;
    @BindView(R.id.tcl_send_gift)
    TCLineControllerView tclSendGift;
    @BindView(R.id.tcl_get_gift)
    TCLineControllerView tclGetGift;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.scrollView)
    ScrollView scrollView;

    private PersonpageClickListener mListener;
    private Context context;
    private PersonPageModel model;

    public void setPersonpageClickListener(PersonpageClickListener listener) {
        mListener = listener;
    }

    public static PerPageFragment newInstance(Context context, PersonPageModel model, PersonpageClickListener listener) {
        PerPageFragment fragment = new PerPageFragment();
        fragment.context = context;
        fragment.model = model;
        fragment.setPersonpageClickListener(listener);
        return fragment;
    }

    private void init(View view) {
        contributionNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.goRank(model.userId);
                }
            }
        });
        tclIdnum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.copyId();
                }
            }
        });
        setTclSendGift(model.sendgift);
        setTclGetGift(model.getgift);
        setTclIdnum(model.id);
//        setConOneImg(model.oneImgUrl);
//        setConTwoImg(model.twoImgUrl);
//        setConThreeImg(model.threeImgUrl);
       new CompositeDisposable().add( FeihuZhiboApplication.getApplication().mDataManager
                .doLoadRoomContriApiCall(new LoadRoomContriRequest(model.userId, 3))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoadRoomContriResponce>() {
                    @Override
                    public void accept(@NonNull LoadRoomContriResponce resp) throws Exception {

                        if(resp.getCode()==0){
                            List<LoadRoomContriResponce.RoomContriData> contributeModels=resp.
                                    getRoomContriDataList();
                            int size = contributeModels.size();
                            switch (size) {
                                case 0:
                                    conOne.setVisibility(View.INVISIBLE);
                                    conTwo.setVisibility(View.INVISIBLE);
                                    conThree.setVisibility(View.INVISIBLE);
                                    break;
                                case 1:
                                    Glide.with(context).load(contributeModels.get(0).getHeadUrl()).apply(new RequestOptions()
                                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                            .transform(new TCGlideCircleTransform(context)))
                                            .into(conThree);
                                    conOne.setVisibility(View.INVISIBLE);
                                    conTwo.setVisibility(View.INVISIBLE);
                                    conThree.setVisibility(View.VISIBLE);
                                    break;
                                case 2:
                                    Glide.with(context).load(contributeModels.get(0).getHeadUrl()).apply(new RequestOptions()
                                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                            .transform(new TCGlideCircleTransform(context)))
                                            .into(conTwo);

                                    Glide.with(context).load(contributeModels.get(1).getHeadUrl()).apply(new RequestOptions()
                                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                            .transform(new TCGlideCircleTransform(context)))
                                            .into(conThree);

                                    conOne.setVisibility(View.INVISIBLE);
                                    conTwo.setVisibility(View.VISIBLE);
                                    conThree.setVisibility(View.VISIBLE);
                                    break;
                                case 3:
                                default:
                                    Glide.with(context).load(contributeModels.get(0).getHeadUrl()).apply(new RequestOptions()
                                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                            .transform(new TCGlideCircleTransform(context)))
                                            .into(conOne);

                                    Glide.with(context).load(contributeModels.get(1).getHeadUrl()).apply(new RequestOptions()
                                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                            .transform(new TCGlideCircleTransform(context)))
                                            .into(conTwo);

                                    Glide.with(context).load(contributeModels.get(2).getHeadUrl()).apply(new RequestOptions()
                                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                            .transform(new TCGlideCircleTransform(context)))
                                            .into(conThree);
                                    conOne.setVisibility(View.VISIBLE);
                                    conTwo.setVisibility(View.VISIBLE);
                                    conThree.setVisibility(View.VISIBLE);
                                    break;
                            }

                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }) );

    }


    private void setTclSendGift(String text) {
        tclSendGift.setContent(text);
    }

    private void setTclGetGift(String text) {
        tclGetGift.setContent(text);
    }

    private void setTclIdnum(String text) {
        tclIdnum.setContent(text);
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initWidget(View view) {

    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.my_person_page, null);
        ButterKnife.bind(this, rootView);
        init(rootView);
        return rootView;
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

}
