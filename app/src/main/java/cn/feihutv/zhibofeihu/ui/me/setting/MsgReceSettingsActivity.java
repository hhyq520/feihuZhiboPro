package cn.feihutv.zhibofeihu.ui.me.setting;

import android.view.View;
import android.widget.ImageView;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetMsgSwitchStatusResponse;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;

/**
 * <pre>
 *     @author : liwen.chen
 *     time   :
 *     desc   : 消息接收设置
 *     version: 1.0
 * </pre>
 */
public class MsgReceSettingsActivity extends BaseActivity implements MsgReceSettingsMvpView {

    @Inject
    MsgReceSettingsMvpPresenter<MsgReceSettingsMvpView> mPresenter;

    @BindView(R.id.switch_button1)
    ImageView switchBtn1;

    @BindView(R.id.switch_button2)
    ImageView switchBtn2;

    private boolean switchBtn1Select = false;
    private boolean switchBtn2Select = false;
    private static final String VALUE1 = "MsgSwitchFeed";
    private static final String VALUE2 = "MsgSwitchMsrMessage";
    private String KEY1 = "1";
    private String KEY2 = "1";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_msg_rece_settings;
    }


    @Override
    protected void initWidget() {

        //初始化注入该类
        getActivityComponent().inject(this);

        //初始化注入控件
        setUnBinder(ButterKnife.bind(this));

        //persenter调用初始化
        mPresenter.onAttach(MsgReceSettingsActivity.this);

        mPresenter.getMsgSwitchStatus();
    }


    @Override
    protected void onDestroy() {
        //销毁之前必须调用
        mPresenter.onDetach();
        super.onDestroy();
    }

    @OnClick({R.id.identity_return, R.id.switch_button1, R.id.switch_button2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.identity_return:
                finish();
                break;
            case R.id.switch_button1:
                mPresenter.setMsgSwitchStatus(KEY1, VALUE1, 1);
                break;
            case R.id.switch_button2:
                mPresenter.setMsgSwitchStatus(KEY2, VALUE2, 2);
                break;
            default:
                break;
        }
    }

    @Override
    public void getMsgSwitchStatus(GetMsgSwitchStatusResponse.GetMsgSwitchStatusResponseData msgSwitchStatusResponseData) {
        switchBtn1Select = msgSwitchStatusResponseData.isMsgSwitchFeed();
        KEY1 = switchBtn1Select ? "0" : "1";
        switchBtn2Select = msgSwitchStatusResponseData.isMsgSwitchMsrMessage();
        KEY2 = switchBtn2Select ? "0" : "1";
        switchBtn1.setSelected(switchBtn1Select);
        switchBtn2.setSelected(switchBtn2Select);
    }

    @Override
    public void setMsgStatusSucc(int type) {
        if (type == 1) {
            switchBtn1Select = !switchBtn1Select;
            switchBtn1.setSelected(switchBtn1Select);
            KEY1 = switchBtn1Select ? "0" : "1";
        } else {
            switchBtn2Select = !switchBtn2Select;
            switchBtn2.setSelected(switchBtn2Select);
            KEY2 = switchBtn2Select ? "0" : "1";
        }
    }
}
