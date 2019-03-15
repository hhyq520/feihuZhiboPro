package cn.feihutv.zhibofeihu.ui.me.setting;

import android.content.Intent;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.me.news.ChatActivity;
import cn.feihutv.zhibofeihu.ui.me.news.RecentNewsActivity;
import cn.feihutv.zhibofeihu.ui.widget.TCActivityTitle;
import cn.feihutv.zhibofeihu.ui.widget.TCLineControllerView;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/6 16:43
 *      desc   : 关于我们
 *      version: 1.0
 * </pre>
 */

public class AboutUsActivity extends BaseActivity {

    @BindView(R.id.line_1)
    TCLineControllerView line_1;

    @BindView(R.id.line_2)
    TCLineControllerView line_2;

    @BindView(R.id.line_3)
    TCLineControllerView line_3;

    @BindView(R.id.at_eui_edit)
    TCActivityTitle mTitle;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_aboutus;
    }

    @Override
    protected void initWidget() {
        setUnBinder(ButterKnife.bind(this));

        line_1.hideRight(true);
        line_2.hideRight(true);
        line_3.hideRight(true);
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

    @OnClick({R.id.about_contact})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.about_contact:
                Intent toChatIntent = new Intent(AboutUsActivity.this, ChatActivity.class);
                toChatIntent.putExtra("userId", "");
                toChatIntent.putExtra("nickName", "飞虎客服");
                startActivity(toChatIntent);
                break;
            default:
                break;
        }
    }
}
