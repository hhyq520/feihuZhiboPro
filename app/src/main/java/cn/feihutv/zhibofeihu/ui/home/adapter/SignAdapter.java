package cn.feihutv.zhibofeihu.ui.home.adapter;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.common.AwardsBean;


/**
 * <pre>
 *      author : liwen.chen
 *      time   : 2017/10/20 16:02
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class SignAdapter extends BaseQuickAdapter<AwardsBean, BaseViewHolder> {

    public SignAdapter(@Nullable List<AwardsBean> data) {
        super(R.layout.item_layout_sign, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AwardsBean item) {
        if (item != null) {
            LinearLayout linearLayout = helper.getView(R.id.ll_add);
            String cosRootPath = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosRootPath();
            if (item.getAwardsDatases().size() > 0 && item.getAwardsDatases().size() <= 3) {

                linearLayout.removeAllViews();
                for (int i = 0; i < item.getAwardsDatases().size(); i++) {
                    View view = LayoutInflater.from(mContext).inflate(R.layout.layout_sign_award, null);
                    ImageView imgExp = (ImageView) view.findViewById(R.id.iv_exp);
                    TextView tvCnt = (TextView) view.findViewById(R.id.tv_cnt);
                    Glide.with(mContext)
                            .load(cosRootPath + "icon/signIcon/" + item.getAwardsDatases().get(i).getType() + "_" + item.getAwardsDatases().get(i).getId() + ".png")
                            .into(imgExp);

                    tvCnt.setText("x" + item.getAwardsDatases().get(i).getCnt());
                    view.setPadding(20, 0, 0, 0);
                    linearLayout.addView(view);
                }
            } else if (item.getAwardsDatases().size() > 3) {
                linearLayout.removeAllViews();
                for (int i = 0; i < 3; i++) {
                    View view = LayoutInflater.from(mContext).inflate(R.layout.layout_sign_award, null);
                    ImageView imgExp = (ImageView) view.findViewById(R.id.iv_exp);
                    TextView tvCnt = (TextView) view.findViewById(R.id.tv_cnt);
                    if (i == 2) {
                        tvCnt.setText("x" + 1);
                        imgExp.setImageResource(R.drawable.icon_sign_gift);
                        view.setPadding(20, 0, 0, 0);
                        linearLayout.addView(view);
                    } else {
                        imgExp.setImageResource(R.drawable.icon_exp);
                        Glide.with(mContext)
                                .load(cosRootPath + "icon/signIcon/" + item.getAwardsDatases().get(i).getType() + "_" + item.getAwardsDatases().get(i).getId() + ".png")
                                .into(imgExp);

                        tvCnt.setText("x" + item.getAwardsDatases().get(i).getCnt());
                        view.setPadding(20, 0, 0, 0);
                        linearLayout.addView(view);
                    }
                }
            }


            helper.setVisible(R.id.iv_sign, item.isSignDay());

            switch (item.getDay()) {
                case 1:
                    helper.setText(R.id.tv_day, "第一天");
                    helper.setBackgroundRes(R.id.iv, R.drawable.pic_prpbar_top);
                    break;
                case 2:
                    helper.setText(R.id.tv_day, "第二天");
                    helper.setBackgroundRes(R.id.iv, R.drawable.pic_prpbar_mid);
                    break;
                case 3:
                    helper.setText(R.id.tv_day, "第三天");
                    helper.setBackgroundRes(R.id.iv, R.drawable.pic_prpbar_mid);
                    break;
                case 4:
                    helper.setText(R.id.tv_day, "第四天");
                    helper.setBackgroundRes(R.id.iv, R.drawable.pic_prpbar_mid);
                    break;
                case 5:
                    helper.setText(R.id.tv_day, "第五天");
                    helper.setBackgroundRes(R.id.iv, R.drawable.pic_prpbar_mid);
                    break;
                case 6:
                    helper.setText(R.id.tv_day, "第六天");
                    helper.setBackgroundRes(R.id.iv, R.drawable.pic_prpbar_mid);
                    break;
                case 7:
                    helper.setText(R.id.tv_day, "第七天");

                    ViewGroup.LayoutParams layoutParams1 = ((RelativeLayout) helper.getView(R.id.rel_bg)).getLayoutParams();
                    layoutParams1.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    ((RelativeLayout) helper.getView(R.id.rel_bg)).setLayoutParams(layoutParams1);

                    ViewGroup.LayoutParams layoutParams = ((ImageView) helper.getView(R.id.iv)).getLayoutParams();
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    ((ImageView) helper.getView(R.id.iv)).setLayoutParams(layoutParams);
                    helper.setBackgroundRes(R.id.iv, R.drawable.yuan_down);
                    break;
                default:
                    break;

            }
        }
    }
}
