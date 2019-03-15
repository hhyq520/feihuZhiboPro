package cn.feihutv.zhibofeihu.ui.bangdan.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cn.feihutv.zhibofeihu.ui.bangdan.ContributionListFragment;
import cn.feihutv.zhibofeihu.ui.bangdan.GuardFragment;
import cn.feihutv.zhibofeihu.ui.bangdan.RankinglistFragment;
import cn.feihutv.zhibofeihu.ui.bangdan.luck.LuckListFragment;

/**
 * Created by quantan.liu on 2017/3/22.
 */

public class BDFragmentPageAdapter extends FragmentPagerAdapter {


    public BDFragmentPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return RankinglistFragment.getInstance();
            case 1:
                return ContributionListFragment.getInstance();
            case 2:
                return LuckListFragment.getInstance();
            case 3:
                return GuardFragment.newInstance();
            default:
                break;

        }
        return null;

    }

    @Override
    public int getCount() {
        return 4;
    }
}
