package cn.feihutv.zhibofeihu.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetJackpotDataRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetJackpotDataResponce;
import cn.feihutv.zhibofeihu.ui.widget.CaichiView;
import cn.feihutv.zhibofeihu.ui.widget.MyCaichiView;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.feihutv.zhibofeihu.utils.glideutil.GlideApp;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by huanghao on 2017/6/19.
 */

public class CaichiDialog extends Dialog {
    @BindView(R.id.icon_history)
    TextView iconHistory;
    @BindView(R.id.img_love)
    ImageView imgLove;
    @BindView(R.id.text_love)
    TextView textLove;
    @BindView(R.id.img_cucmb)
    ImageView imgCucmb;
    @BindView(R.id.text_cucmb)
    TextView textCucmb;
    @BindView(R.id.img_banana)
    ImageView imgBanana;
    @BindView(R.id.text_banana)
    TextView textBanana;
    @BindView(R.id.img_meteor)
    ImageView imgMeteor;
    @BindView(R.id.text_meteor)
    TextView textMeteor;
    @BindView(R.id.caichi_view)
    LinearLayout caichiView;
    @BindView(R.id.icon_finish)
    ImageView iconFinish;
    @BindView(R.id.fans_contri_tabs)
    SlidingTabLayout fansContriTabs;
    @BindView(R.id.gift_lin)
    LinearLayout giftLin;
    @BindView(R.id.my_lin)
    LinearLayout myLin;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.record_view)
    LinearLayout recordView;
    @BindView(R.id.text_cucmb_zb)
    TextView textCucmbZb;
    @BindView(R.id.text_banana_zb)
    TextView textBananaZb;
    @BindView(R.id.text_love_zb)
    TextView textLoveZb;
    @BindView(R.id.webView)
    WebView webView;
    private Context context;
    private CaichiView view1;
    private CaichiView view2;
    private MyCaichiView view5;
    private List<View> views = new ArrayList<>();

    public CaichiDialog(Context context) {
        super(context, R.style.floag_dialog);
        this.context = context;
    }

    public interface CaichiListener {
        void wanfaClick();

        void showLogin();
    }

    private CaichiListener mListener;

    public void setOnItemClickListener(CaichiListener listener) {
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caichi_view_dialog);
        ButterKnife.bind(this);
        getCaichiData();
        view1 = new CaichiView(getContext(), 0,true);
        view2 = new CaichiView(getContext(), 20,true);
        view5 = new MyCaichiView(getContext());
        views.add(view1.getView());
        views.add(view2.getView());
        views.add(view5.getView());
        viewpager.setAdapter(new MyPagerAdapter(views));
        viewpager.setCurrentItem(0);
        viewpager.setOffscreenPageLimit(4);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewpager.setCurrentItem(position);
                if (position == 0) {
                    myLin.setVisibility(View.GONE);
                    giftLin.setVisibility(View.VISIBLE);
                } else if (position == 1) {
                    myLin.setVisibility(View.GONE);
                    giftLin.setVisibility(View.VISIBLE);
                } else if (position == 2) {
                    if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                        if (mListener != null) {
                            viewpager.setCurrentItem(1);
                            mListener.showLogin();
                            return;
                        }
                    }
                    myLin.setVisibility(View.VISIBLE);
                    giftLin.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        fansContriTabs.setViewPager(viewpager);

        webView.loadUrl(FeihuZhiboApplication.getApplication().mDataManager.getSysConfig().getLuckyPoolDocUrl());
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
                    public void accept(@NonNull GetJackpotDataResponce responce) throws Exception {
                        if (responce.getCode() == 0) {
                            int hugua = responce.getJackpotData().getYi();
                            int banana = responce.getJackpotData().getEr();
                            int liux = responce.getJackpotData().getErshi();
                            int love = responce.getJackpotData().getBa();
                            textLove.setText(String.valueOf(love));
                            textBanana.setText(String.valueOf(banana));
                            textMeteor.setText(String.valueOf(liux));
                            textCucmb.setText(String.valueOf(hugua));
                            int master1 = responce.getJackpotData().getMaster1();
                            int master2 = responce.getJackpotData().getMaster2();
                            int master8 = responce.getJackpotData().getMaster8();
                            textCucmbZb.setText(String.valueOf(master1));
                            textBananaZb.setText(String.valueOf(master2));
                            textLoveZb.setText(String.valueOf(master8));
                        } else {

                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));


    }

    @OnClick({R.id.icon_finish, R.id.icon_history})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.icon_finish:
                recordView.setVisibility(View.GONE);
                caichiView.setVisibility(View.VISIBLE);
                break;
            case R.id.icon_history:
                recordView.setVisibility(View.VISIBLE);
                caichiView.setVisibility(View.GONE);
                break;
        }
    }




    class MyPagerAdapter extends PagerAdapter {
        private List<View> mViewList;
        // 标题数组
        private String[] titles = {"幸运礼物", "飞虎流星", "我的中奖"};

        public MyPagerAdapter(List<View> mViewList) {
            this.mViewList = mViewList;
        }

        @Override
        public int getCount() {
            return mViewList.size();//页卡数
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;//官方推荐写法
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View v = mViewList.get(position);
            ViewGroup parent = (ViewGroup) v.getParent();
            //Log.i("ViewPaperAdapter", parent.toString());
            if (parent != null) {
                parent.removeAllViews();
            }

            container.addView(mViewList.get(position));//添加页卡
            return mViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));//删除页卡
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];//页卡标题
        }

    }
}
