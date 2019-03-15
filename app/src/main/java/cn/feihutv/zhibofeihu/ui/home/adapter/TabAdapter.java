package cn.feihutv.zhibofeihu.ui.home.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.feihutv.zhibofeihu.data.db.model.SysLaunchTagBean;
import cn.feihutv.zhibofeihu.ui.home.ConcernFragment;
import cn.feihutv.zhibofeihu.ui.home.HotFragment;

/**
 * <pre>
 *      author : liwen.chen
 *      time   : 2017/10/17 14:21
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class TabAdapter extends FragmentPagerAdapter {

    private List<SysLaunchTagBean> mSysLaunchTagBeanList;

    public TabAdapter(FragmentManager fm, List<SysLaunchTagBean> mSysLaunchTagBeanList) {
        super(fm);
        this.mSysLaunchTagBeanList = mSysLaunchTagBeanList;
    }

    @Override
    public Fragment getItem(int position) {
        if (mSysLaunchTagBeanList.get(position).getId().equals("8")) {
            return ConcernFragment.newInstance("8", position);
        }
        return HotFragment.newInstance(mSysLaunchTagBeanList.get(position).getId(), position);
    }

    @Override
    public int getCount() {
        return mSysLaunchTagBeanList.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return mSysLaunchTagBeanList.get(position).getName();
    }
}
