package cn.feihutv.zhibofeihu.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.annotation.GlideOption;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.db.model.SysLaunchTagBean;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LocationRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetJackpotDataRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetJackpotDataResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.GetLiveStatusRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.GetLiveStatusResponce;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.feihutv.zhibofeihu.utils.glideutil.GlideApp;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by huanghao on 2017/6/19.
 */

public class OrigCaichiDialog extends Dialog {
    @BindView(R.id.icon_history)
    TextView iconHistory;
    @BindView(R.id.img_cucmb)
    ImageView imgCucmb;
    @BindView(R.id.text_cucmb)
    TextView textCucmb;
    @BindView(R.id.img_banana)
    ImageView imgBanana;
    @BindView(R.id.text_banana)
    TextView textBanana;
    @BindView(R.id.img_love)
    ImageView imgLove;
    @BindView(R.id.text_love)
    TextView textLove;
    @BindView(R.id.img_meteor)
    ImageView imgMeteor;
    @BindView(R.id.text_meteor)
    TextView textMeteor;
    @BindView(R.id.caichi_view)
    LinearLayout caichiView;
    @BindView(R.id.wanfa_text)
    ImageView wanfaText;
    @BindView(R.id.text_cucmb_zb)
    TextView textCucmbZb;
    @BindView(R.id.text_banana_zb)
    TextView textBananaZb;
    @BindView(R.id.text_love_zb)
    TextView textLoveZb;
    private Context context;

    public interface CaiOnclickListener {
        void historyClick();

        void helpClick();
    }

    private CaiOnclickListener clickListener;

    public void setOnItemClickListener(CaiOnclickListener listener) {
        clickListener = listener;
    }

    public OrigCaichiDialog(Context context) {
        super(context, R.style.floag_dialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.origcaichi_view_dialog);
        ButterKnife.bind(this);
        getCaichiData();
        wanfaText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.helpClick();
                }
            }
        });
        int vipLevel= SharePreferenceUtil.getSessionInt(FeihuZhiboApplication.getApplication(), "PREF_KEY_VIP");
        boolean isvipExpired=SharePreferenceUtil.getSessionBoolean(FeihuZhiboApplication.getApplication(), "PREF_KEY_VIPEXPIRED");
        boolean isGuard=SharePreferenceUtil.getSessionBoolean(FeihuZhiboApplication.getApplication(), "meisGuard");

        if(FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().size()>0) {
            String giftUrl = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosLuckGiftIconRootPath();
            int enableVip1=FeihuZhiboApplication.getApplication().mDataManager.getGiftBeanByID(String.valueOf("1")).getEnableVip();
            int enableVip2=FeihuZhiboApplication.getApplication().mDataManager.getGiftBeanByID(String.valueOf("2")).getEnableVip();
            int enableVip8=FeihuZhiboApplication.getApplication().mDataManager.getGiftBeanByID(String.valueOf("8")).getEnableVip();
            if((vipLevel>0&&!isvipExpired)||isGuard) {
                if(enableVip1==1){
                    GlideApp.loadImg(getContext(), giftUrl + "1_base_vip.png", DiskCacheStrategy.ALL, imgCucmb);
                }else{
                    GlideApp.loadImg(getContext(), giftUrl + "1_base.png", DiskCacheStrategy.ALL, imgCucmb);
                }
                if(enableVip2==1){
                    GlideApp.loadImg(getContext(), giftUrl + "2_base_vip.png", DiskCacheStrategy.ALL, imgBanana);
                }else{
                    GlideApp.loadImg(getContext(), giftUrl + "2_base.png", DiskCacheStrategy.ALL, imgBanana);
                }
                if(enableVip8==1){
                    GlideApp.loadImg(getContext(), giftUrl + "8_base_vip.png", DiskCacheStrategy.ALL, imgLove);
                }else{
                    GlideApp.loadImg(getContext(), giftUrl + "8_base.png", DiskCacheStrategy.ALL, imgLove);
                }



            }else{
                GlideApp.loadImg(getContext(), giftUrl + "1_base.png", DiskCacheStrategy.ALL, imgCucmb);
                GlideApp.loadImg(getContext(), giftUrl + "2_base.png", DiskCacheStrategy.ALL, imgBanana);
                GlideApp.loadImg(getContext(), giftUrl + "8_base.png", DiskCacheStrategy.ALL, imgLove);
            }
            GlideApp.loadImg(context,giftUrl+"20_base.png", DiskCacheStrategy.ALL,imgMeteor);

        }
    }

    private void getCaichiData() {
        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doGetJackpotDataApiCall(new GetJackpotDataRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetJackpotDataResponce>() {
                    @Override
                    public void accept(@NonNull GetJackpotDataResponce getJackpotDataResponce) throws Exception {
                        if(getJackpotDataResponce.getCode()==0) {
                            int hugua = getJackpotDataResponce.getJackpotData().getYi();
                            int banana = getJackpotDataResponce.getJackpotData().getEr();
                            int liux = getJackpotDataResponce.getJackpotData().getErshi();
                            int love = getJackpotDataResponce.getJackpotData().getBa();
                            int master1 = getJackpotDataResponce.getJackpotData().getMaster1();
                            int master2 = getJackpotDataResponce.getJackpotData().getMaster2();
                            int master8 = getJackpotDataResponce.getJackpotData().getMaster8();
                            textLove.setText(String.valueOf(love));
                            textBanana.setText(String.valueOf(banana));
                            textMeteor.setText(String.valueOf(liux));
                            textCucmb.setText(String.valueOf(hugua));
                            textCucmbZb.setText(String.valueOf(master1));
                            textBananaZb.setText(String.valueOf(master2));
                            textLoveZb.setText(String.valueOf(master8));
                        }else{
                            AppLogger.e(getJackpotDataResponce.getErrMsg());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                })

        );
    }

    @OnClick(R.id.icon_history)
    public void onClick() {
        if (clickListener != null) {
            clickListener.historyClick();
        }
    }
}
