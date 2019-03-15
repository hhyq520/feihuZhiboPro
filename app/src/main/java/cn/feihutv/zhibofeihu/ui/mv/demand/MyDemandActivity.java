package cn.feihutv.zhibofeihu.ui.mv.demand;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.mv.demand.adapter.MyDemandPageAdapter;
import cn.feihutv.zhibofeihu.ui.widget.TCActivityTitle;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.TCUtils;


/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/
 *     desc   : 我的需求
 *     version: 1.0
 * </pre>
 */
public class MyDemandActivity extends BaseActivity implements MyDemandMvpView, ViewPager.OnPageChangeListener {

    @Inject
    MyDemandMvpPresenter<MyDemandMvpView> mPresenter;


    @BindView(R.id.rg_bd)
    RadioGroup mRadioGroup;

    @BindView(R.id.vp_bd)
    ViewPager viewpager;

    @BindView(R.id.zb_eui_edit)
    TCActivityTitle zb_eui_edit;

    @BindView(R.id.mv_user_head)
    ImageView mv_user_head;

    @BindView(R.id.mv_user_name)
    TextView mv_user_name;


    @BindView(R.id.mv_desc)
    TextView mv_desc;

    @BindView(R.id.mv_collect)
    TextView mv_collect;



    List<Fragment> mMyDemandPageFragmentList;

    public static final int buyMvModifyResult=300;//购买界面修改需求
    public static final int buySuccessMVResult=400;//购买界面修改需求



    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_demand;
    }


    @Override
    protected void initWidget() {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        //Presenter调用初始化
        mPresenter.onAttach(MyDemandActivity.this);
        mMyDemandPageFragmentList = new ArrayList<>();
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
                    default:
                        break;
                }
            }
        });

        mMyDemandPageFragmentList.add(MyDemandPageFragment.newInstance(0));
        mMyDemandPageFragmentList.add(MyDemandPageFragment.newInstance(1));
        mMyDemandPageFragmentList.add(MyDemandPageFragment.newInstance(2));
        viewpager.addOnPageChangeListener(this);
        viewpager.setOffscreenPageLimit(3);
        MyDemandPageAdapter mFragmentPageAdapter =
                new MyDemandPageAdapter(getSupportFragmentManager(), mMyDemandPageFragmentList);
        viewpager.setAdapter(mFragmentPageAdapter);


        zb_eui_edit.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LoadUserDataBaseResponse.UserData userData = mPresenter.getUserData();

        TCUtils.showPicWithUrl(MyDemandActivity.this, mv_user_head,
                userData.getHeadUrl(), R.drawable.face);

        mv_user_name.setText(userData.getNickName());

        mv_desc.setText("我的定制");


        //判断是否为主播，非主播没有收藏功能
        int status=  mPresenter.getUserData().getCertifiStatus();
        if(1!=status) {
            //不是主播
            mv_collect.setVisibility(View.GONE);
        }else{
            mv_collect.setVisibility(View.VISIBLE);
        }

    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        AppLogger.e("返回到mv需求界面   "+resultCode);
        switch (resultCode){
            case 8000:
                //发布成功后返回该界面刷新
                MyDemandPageFragment myDemandPageFragment= (MyDemandPageFragment)
                        mMyDemandPageFragmentList.get(0);
                myDemandPageFragment.mRecyclerView.refresh();
                break;
            case MyDemandActivity.buyMvModifyResult:
                //修改 待成交作品mv需求
                MyDemandPageFragment myFragment= (MyDemandPageFragment)
                        mMyDemandPageFragmentList.get(1);
                myFragment.mRecyclerView.refresh();
                break;
            case MyDemandActivity.buySuccessMVResult:
                //购买mv成功后 刷新列表
                MyDemandPageFragment myFragment1= (MyDemandPageFragment)
                        mMyDemandPageFragmentList.get(1);
                myFragment1.mRecyclerView.refresh();
                MyDemandPageFragment  myFragment2= (MyDemandPageFragment)
                        mMyDemandPageFragmentList.get(2);
                myFragment2.mRecyclerView.refresh();
                break;
        }



    }




    @Override
    protected void onDestroy() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroy();
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
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @OnClick({R.id.mv_user_head, R.id.mv_collect})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mv_user_head:

                break;
            case R.id.mv_collect:
                goActivity(MyMvCollectActivity.class);
                break;
        }
    }
}
