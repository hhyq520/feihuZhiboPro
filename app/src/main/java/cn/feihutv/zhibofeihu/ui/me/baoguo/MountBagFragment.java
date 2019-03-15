package cn.feihutv.zhibofeihu.ui.me.baoguo;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.DataManager;
import cn.feihutv.zhibofeihu.data.db.model.SysGoodsNewBean;
import cn.feihutv.zhibofeihu.data.db.model.SysMountNewBean;
import cn.feihutv.zhibofeihu.data.network.http.model.common.BuyGoodsRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.BuyGoodsResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetMountListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetMountListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.SetMountRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.SetMountResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;
import cn.feihutv.zhibofeihu.ui.base.BaseFragment;
import cn.feihutv.zhibofeihu.ui.me.adapter.MountBagAdapter;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.feihutv.zhibofeihu.utils.TCConstants;
import cn.feihutv.zhibofeihu.utils.weiget.dialog.RxDialogSureCancel;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/10 19:31
 *      desc   : 包裹---座驾
 *      version: 1.0
 * </pre>
 */

public class MountBagFragment extends BaseFragment {

    @BindView(R.id.mount_gridView)
    GridView mGridView;

    @BindView(R.id.ll_null_dz)
    LinearLayout llNullDz;

    private MountBagAdapter mAdapter;

    private List<GetMountListResponse.MountListResponseData> mMountListResponseDatas = new ArrayList<>();
    private DataManager mDataManager;
    private LoadUserDataBaseResponse.UserData mUserData;

    public static MountBagFragment newInstance() {
        MountBagFragment fragment = new MountBagFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mount_bag;
    }

    @Override
    protected void initWidget(View view) {
        setUnBinder(ButterKnife.bind(this, view));
        mAdapter = new MountBagAdapter(getContext());
        mGridView.setAdapter(mAdapter);
        mDataManager = FeihuZhiboApplication.getApplication().mDataManager;
        mUserData = mDataManager.getUserData();
        new CompositeDisposable().add(mDataManager
                .doGetMountListCall(new GetMountListRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetMountListResponse>() {
                    @Override
                    public void accept(@NonNull GetMountListResponse getMountListResponse) throws Exception {
                        if (getMountListResponse.getCode() == 0) {
                            if (getMountListResponse.getMountListResponseDatas().size() > 0) {

                                llNullDz.setVisibility(View.GONE);
                                mGridView.setVisibility(View.VISIBLE);
                                mMountListResponseDatas.addAll(getMountListResponse.getMountListResponseDatas());
                                for (GetMountListResponse.MountListResponseData mountListResponseData : mMountListResponseDatas) {
                                    long space = (long) mountListResponseData.getExpireAt() - System.currentTimeMillis() / 1000;
                                    if (space <= 0) {
                                        mountListResponseData.setType(0);
                                    } else {
                                        if (mountListResponseData.getMount() == mDataManager.getUserData().getCrtMount()) {
                                            mountListResponseData.setType(2);
                                        } else {
                                            mountListResponseData.setType(1);
                                        }
                                    }
                                }
                                mAdapter.setDatas(mMountListResponseDatas);
                            } else {
                                llNullDz.setVisibility(View.VISIBLE);
                                mGridView.setVisibility(View.GONE);
                            }
                        } else {
                            AppLogger.i(getMountListResponse.getCode() + " " + getMountListResponse.getErrMsg());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));

        mAdapter.setUseBtnListener(new MountBagAdapter.UseBtnListener() {
            @Override
            public void click(int type, final int pos) {
                if (type == 0) {
                    showOkDialog(pos);
                } else if (type == 1) {
                    new CompositeDisposable().add(mDataManager
                            .doSetMountCall(new SetMountRequest(mMountListResponseDatas.get(pos).getMount()))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<SetMountResponse>() {
                                @Override
                                public void accept(@NonNull SetMountResponse setMountResponse) throws Exception {
                                    if (setMountResponse.getCode() == 0) {
                                        for (GetMountListResponse.MountListResponseData mountListResponseData : mMountListResponseDatas) {
                                            if (mountListResponseData.getType() == 2) {
                                                mountListResponseData.setType(1);
                                                break;
                                            }
                                        }
                                        mMountListResponseDatas.get(pos).setType(2);
                                        mAdapter.notifyDataSetChanged();
                                        mUserData.setCrtMount(mMountListResponseDatas.get(pos).getMount());
                                        mDataManager.saveUserData(mUserData);
                                    } else {
                                        AppLogger.i(setMountResponse.getCode() + " " + setMountResponse.getErrMsg());
                                    }
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(@NonNull Throwable throwable) throws Exception {

                                }
                            }));
                } else {
                    new CompositeDisposable().add(mDataManager
                            .doSetMountCall(new SetMountRequest(0))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<SetMountResponse>() {
                                @Override
                                public void accept(@NonNull SetMountResponse setMountResponse) throws Exception {
                                    if (setMountResponse.getCode() == 0) {
                                        mMountListResponseDatas.get(pos).setType(1);
                                        mAdapter.notifyDataSetChanged();
                                        mUserData.setCrtMount(0);
                                        mDataManager.saveUserData(mUserData);
                                    } else {
                                        AppLogger.i(setMountResponse.getCode() + " " + setMountResponse.getErrMsg());
                                    }

                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(@NonNull Throwable throwable) throws Exception {

                                }
                            }));
                }
            }
        });
    }


    private void showOkDialog(final int pos) {
        final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(getContext());
        rxDialogSureCancel.setCancel("取消");
        rxDialogSureCancel.setSure("确定");
        rxDialogSureCancel.setContent("确认购买该座驾？");
        rxDialogSureCancel.setCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rxDialogSureCancel.dismiss();
            }
        });

        rxDialogSureCancel.setSureListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CompositeDisposable().add(mDataManager
                        .getGoodIDByMountID(String.valueOf(mMountListResponseDatas.get(pos).getMount()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<SysGoodsNewBean>() {
                            @Override
                            public void accept(@NonNull SysGoodsNewBean sysGoodsNewBean) throws Exception {
                                int id = Integer.parseInt(sysGoodsNewBean.getId());
                                new CompositeDisposable().add(mDataManager
                                        .doBuyGoodsApiCall(new BuyGoodsRequest(id, 1))
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Consumer<BuyGoodsResponce>() {
                                            @Override
                                            public void accept(@NonNull BuyGoodsResponce buyGoodsResponce) throws Exception {
                                                if (buyGoodsResponce.getCode() == 0) {
                                                    mMountListResponseDatas.get(pos).setType(1);
                                                    int time = (int) (System.currentTimeMillis() / 1000) + 30 * 24 * 60 * 60;
                                                    mMountListResponseDatas.get(pos).setExpireAt(time);
                                                    mAdapter.notifyDataSetChanged();
                                                } else {
                                                    if (buyGoodsResponce.getCode() == 4202) {
                                                        FHUtils.showToast("余额不足，请充值");
                                                    } else if (buyGoodsResponce.getCode() == 4203) {
                                                        FHUtils.showToast("礼物已下架");
                                                    } else {
                                                        FHUtils.showToast("购买失败");
                                                    }
                                                }
                                            }
                                        }, new Consumer<Throwable>() {
                                            @Override
                                            public void accept(@NonNull Throwable throwable) throws Exception {

                                            }
                                        }));
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {

                            }
                        }));

            }
        });
        rxDialogSureCancel.show();
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMountListResponseDatas != null) {
            mMountListResponseDatas.clear();
        }
    }
}
