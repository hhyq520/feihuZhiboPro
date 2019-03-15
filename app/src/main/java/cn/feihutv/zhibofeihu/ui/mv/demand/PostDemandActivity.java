package cn.feihutv.zhibofeihu.ui.mv.demand;

import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetNeedSysHBResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.PostNeedRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.PostNeedResponse;
import cn.feihutv.zhibofeihu.rxbus.RxBus;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.widget.TCActivityTitle;
import cn.feihutv.zhibofeihu.utils.CommonUtils;
import cn.feihutv.zhibofeihu.utils.weiget.dialog.RxDialogSureCancel2;

import static cn.feihutv.zhibofeihu.rxbus.RxBusCode.RX_BUS_CLICK_CODE_MV_POST_DEMAND;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/
 *     desc   : 发布需求
 *     version: 1.0
 * </pre>
 */
public class PostDemandActivity extends BaseActivity implements PostDemandMvpView {

    @Inject
    PostDemandMvpPresenter<PostDemandMvpView> mPresenter;

    @BindView(R.id.ll_post_coin)
    LinearLayout ll_post_coin;

    @BindView(R.id.tc_title)
    TCActivityTitle tc_title;

    @BindView(R.id.submit)
    TextView   submit;

    @BindView(R.id.et_demand_title)
    EditText   et_demand_title;

    @BindView(R.id.et_demand_songName)
    EditText   et_demand_songName;


    @BindView(R.id.et_demand_songAuthor)
    EditText   et_demand_songAuthor;


    @BindView(R.id.mv_user_head)
    ImageView mv_user_head;

    @BindView(R.id.et_demand_desc)
    EditText   et_demand_desc;

    @BindView(R.id.cb_demand_sender)
    CheckBox cb_demand_sender;
    @BindView(R.id.cb_demand_my)
    CheckBox cb_demand_my;


    List<String> mCoinList=new ArrayList<>();
    PostNeedRequest request = new PostNeedRequest();

    private List<RadioButton> mCoinRadioList=new ArrayList<>();

    private String userChooseCoin="";
    private String forUid="";
    private String pForuid="";
    @Override
    protected int getLayoutId() {
        return R.layout.activity_post_demand;
    }


    @Override
    protected void initWidget() {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        //Presenter调用初始化
        mPresenter.onAttach(PostDemandActivity.this);
        Intent intent=getIntent();
        if(intent!=null){
            forUid=intent.getStringExtra("userId");
        }
        pForuid=forUid;
        if(!TextUtils.isEmpty(forUid)){
            mv_user_head.setVisibility(View.GONE);
            cb_demand_sender.setChecked(false);
            cb_demand_my.setChecked(true);
            cb_demand_my.setVisibility(View.VISIBLE);
        }else{
            cb_demand_my.setVisibility(View.GONE);
            cb_demand_sender.setChecked(true);
            cb_demand_sender.setClickable(false);
        }


        mPresenter.getNeedSysHB();

        tc_title.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tc_title.getReturnView().setTextColor(getResources().getColor(R.color.black));
        cb_demand_sender.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    forUid="";
                    cb_demand_my.setChecked(false);
                }else{
                    forUid=pForuid;
                    cb_demand_my.setChecked(true);
                }
            }
        });
        cb_demand_my.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    forUid=pForuid;
                    cb_demand_sender.setChecked(false);
                }else{
                    forUid="";
                    cb_demand_sender.setChecked(true);
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //提交
                String  title=et_demand_title.getText().toString();

                String  songName=et_demand_songName.getText().toString();

                String  songAuthor=et_demand_songAuthor.getText().toString();

                String  desc=et_demand_desc.getText().toString();
                if(TextUtils.isEmpty(title)){
                    onToast("标题未填写!");
                    return;
                }
                if(TextUtils.isEmpty(desc)){
                    onToast("要求未填写!");
                    return;
                }
                if(TextUtils.isEmpty(userChooseCoin)){
                    onToast("未选择发布赏金!");
                    return;
                }
                Long inputHb=Long.parseLong(userChooseCoin);
               Long hb=  mPresenter.getUserData().gethB();
                if(inputHb>hb){
                    onToast("您的虎币余额不足!"); // goActivity(RechargeActivity.class);
                    return;
                }
                request.setForUid(forUid);
                request.setHb(Integer.parseInt(userChooseCoin));
                request.setTitle(title);
                request.setSongName(songName);
                request.setSingerName(songAuthor);
                request.setRequire(desc);

                showSubmitDialog();

            }
        });
    }



    private void showSubmitDialog(){
        final RxDialogSureCancel2 rxDialogSureCancel = new RxDialogSureCancel2(PostDemandActivity.this);//提示弹窗
        rxDialogSureCancel.getTitle().setText("确定发布需求？");
        rxDialogSureCancel.setContent(Html.fromHtml("(系统将会冻结您发布需求使用的虎币)").toString());
        rxDialogSureCancel.setSure("确定");
        rxDialogSureCancel.setCanceledOnTouchOutside(false);
        rxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showLoading("正在发布...");
                mPresenter.postNeedData(request);
                rxDialogSureCancel.cancel();
            }
        });
        rxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rxDialogSureCancel.cancel();
            }
        });
        rxDialogSureCancel.show();
    }

    @Override
    public void onGetNeedSysHBResp(GetNeedSysHBResponse response) {

        if(response.getCode()==0){
            initUi(response.getHbList());
            mCoinList=response.getHbList();
        }else{
           onToast("系统异常，请重试");
            finish();
        }
    }

    private void initUi(List<String> coinList) {
        ll_post_coin.removeAllViews();

        for(int i=0;i<coinList.size();i++) {
            double coinD;
            String coin=coinList.get(i);
            coinD=Double.parseDouble(coin);
            LinearLayout view = (LinearLayout) View.inflate(this, R.layout.view_postdemand_coin, null);
            RadioButton conText=view.findViewById(R.id.text_coin);
            if(coinD>9999){
                coin= CommonUtils.get2MaximumFractionDigits((coinD/10000))+"万";
            }
            conText.setText(coin+"虎币");
            conText.setId(i);
            view.setLayoutParams(new LinearLayout
                    .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
            ll_post_coin.addView(view);
            conText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RadioButton rb= (RadioButton) v;
                     for(RadioButton rbv:mCoinRadioList){
                         rbv.setChecked(false);
                     }
                    rb.setChecked(true);
                    userChooseCoin=mCoinList.get(v.getId());

                }
            });
            mCoinRadioList.add(conText);

        }

    }



    @Override
    public void onPostNeedDataResp(PostNeedResponse response) {
        hideLoading();
        if(response.getCode()==0){
            onToast("发布成功");
            RxBus.get().send(RX_BUS_CLICK_CODE_MV_POST_DEMAND,8000);
            setResult(8000);
            finish();
        }else if(4202==response.getCode()){
            onToast("发布失败：余额不足");
        } else if(response.getCode()==4009){
            onToast(getString(R.string.mv_title_error_hint));
        }else{
            onToast("发布失败："+response.getErrMsg());
        }
    }

    @Override
    protected void onDestroy() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroy();
    }






}
