package cn.feihutv.zhibofeihu.ui.widget.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SlidingTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.ui.live.OnLineUserFragment;
import cn.feihutv.zhibofeihu.ui.live.OnStrClickListener;
import cn.feihutv.zhibofeihu.ui.live.VipUserFragment;

/**
 * Created by huanghao on 2017/11/10.
 */

public class UserDialogFragment extends DialogFragment {

    @BindView(R.id.use_tabs)
    SlidingTabLayout useTabs;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    private String[] titles = {"贵宾席", "在线用户"};
    private OnStrClickListener listener;
    public void setOnStrClickListener(OnStrClickListener listener){
        this.listener=listener;
    }
    private boolean isHost=false;

    public static UserDialogFragment newInstance(boolean isHost){
        UserDialogFragment userDialogFragment=new UserDialogFragment();
        userDialogFragment.isHost=isHost;
        return userDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_dialog_fragment, container);
        ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.cb_dialog);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if(listener!=null){
            listener.onItemClick(" ");
        }
        super.onDismiss(dialog);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels), (int) (dm.heightPixels * 0.64));
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.setCanceledOnTouchOutside(true);
        }
    }

    private void initView(View view) {
        viewPager.setCurrentItem(0);
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return VipUserFragment.newInstance(isHost);
                    case 1:
                        return OnLineUserFragment.newInstance();
                    default:
                        break;
                }
                return null;
            }

            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });
        useTabs.setViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
