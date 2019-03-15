package cn.feihutv.zhibofeihu.ui.mv.demand.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/11/14
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class MyDemandPageAdapter extends FragmentPagerAdapter {

    List<Fragment> mMyDemandPageFragmentList;
    public MyDemandPageAdapter(FragmentManager fm, List<Fragment>
            mMyDemandPageFragmentList) {
        super(fm);
        this.mMyDemandPageFragmentList=mMyDemandPageFragmentList;
    }
    public MyDemandPageAdapter(FragmentManager fm ) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        return mMyDemandPageFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mMyDemandPageFragmentList.size();
    }
}
