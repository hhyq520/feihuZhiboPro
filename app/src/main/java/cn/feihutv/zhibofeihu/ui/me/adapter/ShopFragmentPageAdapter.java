package cn.feihutv.zhibofeihu.ui.me.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cn.feihutv.zhibofeihu.ui.me.shop.BeautiFragment;
import cn.feihutv.zhibofeihu.ui.me.shop.CarFragment;
import cn.feihutv.zhibofeihu.ui.me.shop.GuardShopFragment;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/7 20:17
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class ShopFragmentPageAdapter extends FragmentPagerAdapter {

    public ShopFragmentPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return GuardShopFragment.newInstance();
            case 1:
                return BeautiFragment.newInstance();
            case 2:
                return CarFragment.newInstance();
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
