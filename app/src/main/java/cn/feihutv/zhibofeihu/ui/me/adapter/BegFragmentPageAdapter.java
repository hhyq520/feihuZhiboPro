package cn.feihutv.zhibofeihu.ui.me.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cn.feihutv.zhibofeihu.ui.me.baoguo.GiftBagFragment;
import cn.feihutv.zhibofeihu.ui.me.baoguo.MountBagFragment;
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

public class BegFragmentPageAdapter extends FragmentPagerAdapter {

    public BegFragmentPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return GiftBagFragment.newInstance();
            case 1:
                return MountBagFragment.newInstance();
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
