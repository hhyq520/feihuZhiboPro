package cn.feihutv.zhibofeihu.ui.me;

import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.db.model.SysLevelBean;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.widget.TCActivityTitle;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/6 17:56
 *      desc   : 等级页面
 *      version: 1.0
 * </pre>
 */

public class LevelActivity extends BaseActivity {

    @BindView(R.id.zb_eui_edit)
    TCActivityTitle mTitle;

    @BindView(R.id.tv_level)
    TextView tvLevel;

    @BindView(R.id.tv_level_exprience)
    TextView tvExprience;

    @BindView(R.id.tv_level_lessexprience)
    TextView tvLessExprience;

    private final String lv = "LV.";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_level;
    }

    @Override
    protected void initWidget() {
        setUnBinder(ButterKnife.bind(this));

        LoadUserDataBaseResponse.UserData userData = FeihuZhiboApplication.getApplication().mDataManager.getUserData();
        tvExprience.setText(String.valueOf(userData.getExp()));
        tvLevel.setText(lv + userData.getLevel());

        if (userData.getLevel() >= 100) {
            tvLessExprience.setText("，更高等级，敬请期待");
        } else {
            List<SysLevelBean> sysLevelBeen = FeihuZhiboApplication.getApplication().mDataManager.getSysLevelBean();
            String needExp = sysLevelBeen.get(userData.getLevel()).getNeedExp();
            tvLessExprience.setText("，距离下一级还差" + String.valueOf(Integer.parseInt(needExp) - userData.getExp()) + "经验值");
        }


    }

    @Override
    protected void eventOnClick() {

        mTitle.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
