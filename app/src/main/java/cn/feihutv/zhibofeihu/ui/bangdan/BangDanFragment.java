package cn.feihutv.zhibofeihu.ui.bangdan;

import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.ui.bangdan.adapter.BDFragmentPageAdapter;
import cn.feihutv.zhibofeihu.ui.base.BaseFragment;


public class BangDanFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

    @BindView(R.id.rg_bd)
    RadioGroup mRadioGroup;

    @BindView(R.id.vp_bd)
    ViewPager viewpager;

    private BDFragmentPageAdapter mFragmentPageAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_livelist;
    }

    @Override
    protected void initWidget(View view) {
        setUnBinder(ButterKnife.bind(this, view));

        mRadioGroup.getChildAt(0).performClick();
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.bd_rb1:
                        viewpager.setCurrentItem(0);
                        break;
                    case R.id.bd_rb2:
                        viewpager.setCurrentItem(1);
                        break;
                    case R.id.bd_rb3:
                        viewpager.setCurrentItem(2);
                        break;
                    case R.id.bd_rb4:
                        viewpager.setCurrentItem(3);
                        break;
                    default:
                        break;
                }
            }
        });

        viewpager.addOnPageChangeListener(this);
        viewpager.setOffscreenPageLimit(3);
        mFragmentPageAdapter = new BDFragmentPageAdapter(getFragmentManager());
        viewpager.setAdapter(mFragmentPageAdapter);
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mRadioGroup.check(R.id.bd_rb1);
                break;
            case 1:
                mRadioGroup.check(R.id.bd_rb2);
                break;
            case 2:
                mRadioGroup.check(R.id.bd_rb3);
                break;
            case 3:
                mRadioGroup.check(R.id.bd_rb4);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
