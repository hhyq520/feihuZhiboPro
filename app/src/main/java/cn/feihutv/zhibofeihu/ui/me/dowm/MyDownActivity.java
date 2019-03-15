package cn.feihutv.zhibofeihu.ui.me.dowm;


import android.content.pm.ActivityInfo;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.me.dowm.adapter.DownFileAdapter;
import cn.feihutv.zhibofeihu.ui.me.dowm.adapter.MyDownInfo;
import cn.feihutv.zhibofeihu.ui.widget.TCActivityTitle;
import cn.feihutv.zhibofeihu.utils.FileUtils;
import cn.feihutv.zhibofeihu.utils.rxdownload.CustomMission;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import zlc.season.rxdownload3.RxDownload;
import zlc.season.rxdownload3.core.Mission;

/**
 * <pre>
 *     author : liwen.chen
 *     time   :
 *     desc   : 下载中心
 *     version: 1.0
 * </pre>
 */
public class MyDownActivity extends BaseActivity implements MyDwomMvpView {

    @Inject
    MyDwomMvpPresenter<MyDwomMvpView> mPresenter;


    DownFileAdapter mDownFileAdapter;
    @BindView(R.id.zb_title)
    TCActivityTitle mZbTitle;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.check_layout)
    LinearLayout check_layout;

    @BindView(R.id.checkAll)
    CheckBox checkAll;

    @BindView(R.id.del)
    CheckBox del;

    @BindView(R.id.tv_sure)
    TextView tv_sure;

    @BindView(R.id.fl_empty)
    FrameLayout  fl_empty;

    boolean isEdit=false;
    boolean isChooseAll=false;
    boolean isChooseMode=false;//选择模式


    Map<String,CustomMission> chooseMissonMap=new HashMap<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_dwom;
    }


    @Override
    protected void initWidget() {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        //persenter调用初始化
        mPresenter.onAttach(MyDownActivity.this);

        mDownFileAdapter=new DownFileAdapter(MyDownActivity.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MyDownActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setAdapter(mDownFileAdapter);
        loadData();

        mZbTitle.setMoreListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEdit){
                    //取消
                    mDownFileAdapter.showCheckBoxView(false);
                    check_layout.setVisibility(View.GONE);
                    isEdit=false;
                    mZbTitle.setMoreText("编辑");
                    chooseMissonMap.clear();
                    del.setText("删除");
                }else{
                    //编辑
                    check_layout.setVisibility(View.VISIBLE);
                    mDownFileAdapter.showCheckBoxView(true);
                    isEdit=true;
                    mZbTitle.setMoreText("取消");


                }

            }
        });


        checkAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                mDownFileAdapter.checkBoxCheckedAll(isChecked);

                if(isChecked){
                    checkAll.setText("全不选");
                    del.setText("删除("+mDownFileAdapter.getItemCount()+")");
                    isChooseAll=true;
                }else{
                    checkAll.setText("全选");
                    del.setText("删除");
                    isChooseAll=false;
                }

            }
        });



        mDownFileAdapter.setDownMvCallBack(new DownFileAdapter.DownMvCallBack() {


            @Override
            public void onChecked(int pos, boolean isCheck, CustomMission mission) {

                if(isCheck) {
                    chooseMissonMap.put(mission.getTag(), mission);
                }else{
                    chooseMissonMap.remove(mission.getTag());
                }
                if(chooseMissonMap.size()>0){
                    del.setText("删除("+chooseMissonMap.size()+")");
                    del.setChecked(true);
                }else{
                    del.setText("删除");
                    del.setChecked(false);
                }
            }

            @Override
            public void onItemClick(int p, CustomMission mission) {

                if(isChooseMode){

                }else{
                    JZVideoPlayerStandard.startFullscreen(MyDownActivity.this,
                            JZVideoPlayerStandard.class,mission.getSavePath()+"/"+mission.getSaveName(),"");
                }
            }
        });

        //设置全屏部分横竖屏
        JZVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;  //横向
        JZVideoPlayer.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;  //纵向

//        JZVideoPlayer.SAVE_PROGRESS=true;
    }



    private void loadData() {
        RxDownload.INSTANCE.getAllMission()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Mission>>() {
                    @Override
                    public void accept(List<Mission> missions) throws Exception {
                        List<MyDownInfo>  myDownInfoList=new ArrayList<MyDownInfo>();
                        List<Mission> missions1=new ArrayList<Mission>();
                        List<Mission> missions2=new ArrayList<Mission>();
                        //加载正在下载的数据
                        for(Mission mission:missions){
                            myDownInfoList.add(new MyDownInfo(1,mission,null));
                        }
                   /*     List<MvDownLog> mvDownLogs=   mPresenter.getMyMvDownLog();
                        //加载已下载数据
                        for(MvDownLog mdl:mvDownLogs){
                            myDownInfoList.add(new MyDownInfo(0,null,mdl));
                        }*/

                        if(missions!=null&&missions.size()>0){
                            mZbTitle.getMoreView().setVisibility(View.VISIBLE);
                            fl_empty.setVisibility(View.GONE);
                        }else{
                            mZbTitle.getMoreView().setVisibility(View.GONE);
                            fl_empty.setVisibility(View.VISIBLE);
                        }

                        mDownFileAdapter.addData(myDownInfoList);
                    }
                });


    }



    @Override
    protected void onDestroy() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroy();
    }




    @OnClick({R.id.startAll, R.id.stopAll, R.id.deleteAll, R.id.del,R.id.tv_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.startAll:
                RxDownload.INSTANCE.startAll().subscribe();
                break;
            case R.id.stopAll:
                RxDownload.INSTANCE.stopAll().subscribe();
                break;
            case R.id.deleteAll:
                RxDownload.INSTANCE.deleteAll(false)
                        .subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(Object o) throws Exception {
                                loadData();
                            }
                        });
                break;
            case R.id.del:

                if(mDownFileAdapter.getItemCount()<1){
                    return;
                }

                if(isChooseAll){
                    showLoading("正在删除...");
                    //全部删除
                    RxDownload.INSTANCE.deleteAll(true)
                            .subscribe(new Consumer<Object>() {
                                @Override
                                public void accept(Object o) throws Exception {
                                    loadData();
                                }
                            });


                    //删除所有下载文件
                    for(MyDownInfo myDownInfo:mDownFileAdapter.getData()){
                        Mission mission=  myDownInfo.getMission();
                        FileUtils.delete(mission.getSavePath()+"/"
                                +mission.getSaveName());
                    }

                    hideLoading();
                }else{

                    int i=0;
                    for(Map.Entry<String,CustomMission>  cm:chooseMissonMap.entrySet()){
                        if(i==0){
                            showLoading("正在删除...");
                        }
                        RxDownload.INSTANCE.delete(cm.getValue(),true)
                                .subscribe(new Consumer<Object>() {
                                    @Override
                                    public void accept(Object o) throws Exception {
                                        loadData();
                                    }
                                });

                        FileUtils.delete(cm.getValue().getSavePath()+"/"
                                +cm.getValue().getSaveName());

                        i++;
                        if(i==chooseMissonMap.size()){
                            hideLoading();
                        }
                    }
                }

                mDownFileAdapter.showCheckBoxView(false);
                check_layout.setVisibility(View.GONE);
                isEdit=false;
                mZbTitle.setMoreText("编辑");
                chooseMissonMap.clear();
                del.setText("删除");
                break;
            case R.id.tv_sure:
                //跳转
                finish();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }
}
