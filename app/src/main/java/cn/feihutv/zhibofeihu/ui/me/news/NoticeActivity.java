package cn.feihutv.zhibofeihu.ui.me.news;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.widget.TCActivityTitle;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/17 15:25
 *      desc   : 消息
 *      version: 1.0
 * </pre>
 */

public class NoticeActivity extends BaseActivity {

    @BindView(R.id.sys_tip_img)
    ImageView sysTipImg;

    @BindView(R.id.feed_tip_img)
    ImageView feedTipImg;

    @BindView(R.id.notice_title)
    TCActivityTitle mTitle;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_notice;
    }

    @Override
    protected void initWidget() {
        setUnBinder(ButterKnife.bind(this));

        if(SharePreferenceUtil.getSessionBoolean(NoticeActivity.this,"haveNoticeSys",false)){
            sysTipImg.setVisibility(View.VISIBLE);
        }
        if(SharePreferenceUtil.getSessionBoolean(NoticeActivity.this,"haveNoticeSns",false)){
            feedTipImg.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.rl_sys, R.id.rl_dynamic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_sys:
                SharePreferenceUtil.saveSeesionBoolean(NoticeActivity.this, "haveNoticeSys", false);
                sysTipImg.setVisibility(View.INVISIBLE);
                Intent intent1 = new Intent(NoticeActivity.this, NoticeSysActivity.class);
                startActivity(intent1);
                break;
            case R.id.rl_dynamic:
                SharePreferenceUtil.saveSeesionBoolean(this, "haveNoticeSns", false);
                feedTipImg.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(NoticeActivity.this, NoticeSnsActivity.class);
                startActivity(intent);
                break;
            default:
                break;
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

    @Override
    protected void onResume() {
        super.onResume();
        SharePreferenceUtil.saveSeesionBoolean(NoticeActivity.this, "Mine_notice", false);
    }
}
