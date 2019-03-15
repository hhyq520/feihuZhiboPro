package cn.feihutv.zhibofeihu.ui.home.signin;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.common.SignResponse;
import cn.feihutv.zhibofeihu.ui.base.BaseDialog;

/**
 * <pre>
 *      author : liwen.chen
 *      time   : 2017/10/21 10:39
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class SignSuccessDialog extends BaseDialog {

    Unbinder unbinder;
    @BindView(R.id.sign_success_rl)
    LinearLayout mLinearLayout;

    private static final String TAG = "SignSuccessDialog";

    private List<SignResponse.SignData.AwardsResponseData> mSignDatas = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_sign_success, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    public void show(FragmentManager fragmentManager) {
        super.show(fragmentManager, TAG);
    }

    @Override
    public void showLoading(String msg) {

    }

    @Override
    protected void setUp(View view) {
        String cosRootPath = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosRootPath();

        if (mSignDatas.size() > 3) {
            LinearLayout linearLayout1 = new LinearLayout(getContext());
            linearLayout1.setHorizontalGravity(LinearLayout.HORIZONTAL);
            linearLayout1.setGravity(Gravity.CENTER);
            for (int j = 0; j < 3; j++) {
                View inflate = LayoutInflater.from(getContext()).inflate(R.layout.layout_sign_award, null);
                ImageView imgExp = (ImageView) inflate.findViewById(R.id.iv_exp);
                TextView tvCnt = (TextView) inflate.findViewById(R.id.tv_cnt);
                Glide.with(getContext())
                        .load(cosRootPath + "icon/signIcon/" + mSignDatas.get(j).getType() + "_" + mSignDatas.get(j).getId() + ".png").into(imgExp);
                tvCnt.setText("x" + mSignDatas.get(j).getCnt());
                linearLayout1.addView(inflate);
            }
            mLinearLayout.addView(linearLayout1);
            LinearLayout linearLayout2 = new LinearLayout(getContext());
            linearLayout2.setHorizontalGravity(LinearLayout.HORIZONTAL);
            linearLayout2.setGravity(Gravity.CENTER);
            for (int i = 3; i < mSignDatas.size(); i++) {
                View inflate = LayoutInflater.from(getContext()).inflate(R.layout.layout_sign_award, null);
                ImageView imgExp = (ImageView) inflate.findViewById(R.id.iv_exp);
                TextView tvCnt = (TextView) inflate.findViewById(R.id.tv_cnt);
                Glide.with(getContext())
                        .load(cosRootPath + "icon/signIcon/" + mSignDatas.get(i).getType() + "_" + mSignDatas.get(i).getId() + ".png").into(imgExp);
                tvCnt.setText("x" + mSignDatas.get(i).getCnt());
                linearLayout2.addView(inflate);
            }
            mLinearLayout.addView(linearLayout2);
        } else {
            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.setHorizontalGravity(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.CENTER);
            for (int i = 0; i < mSignDatas.size(); i++) {
                View inflate = LayoutInflater.from(getContext()).inflate(R.layout.layout_sign_award, null);
                ImageView imgExp = (ImageView) inflate.findViewById(R.id.iv_exp);
                TextView tvCnt = (TextView) inflate.findViewById(R.id.tv_cnt);
                Glide.with(getContext())
                        .load(cosRootPath + "icon/signIcon/" + mSignDatas.get(i).getType() + "_" + mSignDatas.get(i).getId() + ".png").into(imgExp);
                tvCnt.setText("x" + mSignDatas.get(i).getCnt());
                linearLayout.addView(inflate);
            }
            mLinearLayout.addView(linearLayout);
        }


    }

    public void getDatas(SignResponse.SignData signData) {
        if (signData != null) {
            mSignDatas.addAll(signData.getAwardsBeen());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_sure)
    public void onViewClicked() {
        dismiss();
    }
}
