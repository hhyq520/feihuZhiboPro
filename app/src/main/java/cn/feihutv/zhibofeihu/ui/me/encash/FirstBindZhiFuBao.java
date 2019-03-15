package cn.feihutv.zhibofeihu.ui.me.encash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.widget.TCActivityTitle;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/3 16:58
 *      desc   : 首次绑定
 *      version: 1.0
 * </pre>
 */

public class FirstBindZhiFuBao extends BaseActivity {

    @BindView(R.id.zb_eui_edit)
    TCActivityTitle zbEuiEdit;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bind_zhifubao;
    }

    @Override
    protected void initWidget() {
        setUnBinder(ButterKnife.bind(this));
    }

    @Override
    protected void eventOnClick() {
        zbEuiEdit.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @OnClick(R.id.btn_bind)
    public void onViewClicked() {
        startActivity(new Intent(FirstBindZhiFuBao.this, AccountBindingActivity.class));
    }
}
