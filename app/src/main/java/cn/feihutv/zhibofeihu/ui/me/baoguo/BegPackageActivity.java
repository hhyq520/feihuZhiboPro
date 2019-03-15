package cn.feihutv.zhibofeihu.ui.me.baoguo;

import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.me.adapter.BegFragmentPageAdapter;
import cn.feihutv.zhibofeihu.ui.widget.TCActivityTitle;
import cn.feihutv.zhibofeihu.utils.FHUtils;

/**
 * <pre>
 *      @author : liwen.chen
 *      @date   : 2017/11/10 18:42
 *      desc   : 包裹
 *      version: 1.0
 * </pre>
 */

public class BegPackageActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.rg_beg)
    RadioGroup mRadioGroup;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindView(R.id.yin_coin)
    TextView yin_coin;

    @BindView(R.id.hubi)
    TextView hubi;

    @BindView(R.id.tca_baoguo_reture)
    TCActivityTitle mTitle;

    private BegFragmentPageAdapter mBegFragmentPageAdapter;
    private LoadUserDataBaseResponse.UserData mUserData;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_begpackage;
    }

    @Override
    protected void initWidget() {
        setUnBinder(ButterKnife.bind(this));

        mRadioGroup.getChildAt(0).performClick();
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.beg_rb1:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.beg_rb2:
                        mViewPager.setCurrentItem(1);
                        break;
                    default:
                        break;
                }
            }
        });

        mViewPager.addOnPageChangeListener(this);
        mViewPager.setOffscreenPageLimit(2);
        mBegFragmentPageAdapter = new BegFragmentPageAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mBegFragmentPageAdapter);

        mViewPager.setCurrentItem(0);

        mUserData = FeihuZhiboApplication.getApplication().mDataManager.getUserData();

        hubi.setText(FHUtils.intToF(mUserData.gethB()));
        yin_coin.setText(FHUtils.intToF(mUserData.getyHB()));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mRadioGroup.check(R.id.beg_rb1);
                break;
            case 1:
                mRadioGroup.check(R.id.beg_rb2);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

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
