package cn.feihutv.zhibofeihu.ui.me.shop;

import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.me.adapter.ShopFragmentPageAdapter;
import cn.feihutv.zhibofeihu.ui.widget.TCActivityTitle;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/7 19:25
 *      desc   : 商城
 *      version: 1.0
 * </pre>
 */

public class ShoppingActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.rg_shop)
    RadioGroup mRadioGroup;

    @BindView(R.id.mall_vp)
    ViewPager mViewPager;

    @BindView(R.id.view1)
    View mView1;

    @BindView(R.id.view2)
    View mView2;

    @BindView(R.id.mall_return)
    TCActivityTitle mTitle;

    private ShopFragmentPageAdapter mPageAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shopping;
    }

    @Override
    protected void initWidget() {
        setUnBinder(ButterKnife.bind(this));

        mRadioGroup.check(R.id.shop_rb2);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.shop_rb1:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.shop_rb2:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.shop_rb3:
                        mViewPager.setCurrentItem(2);
                        break;
                    default:
                        break;
                }
            }
        });

        mViewPager.addOnPageChangeListener(this);
        mViewPager.setOffscreenPageLimit(3);
        mPageAdapter = new ShopFragmentPageAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPageAdapter);

        mViewPager.setCurrentItem(1);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mView1.setVisibility(View.GONE);
                mView2.setVisibility(View.VISIBLE);
                mRadioGroup.check(R.id.shop_rb1);
                break;
            case 1:
                mView1.setVisibility(View.GONE);
                mView2.setVisibility(View.GONE);
                mRadioGroup.check(R.id.shop_rb2);
                break;
            case 2:
                mView1.setVisibility(View.VISIBLE);
                mView2.setVisibility(View.GONE);
                mRadioGroup.check(R.id.shop_rb3);
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
