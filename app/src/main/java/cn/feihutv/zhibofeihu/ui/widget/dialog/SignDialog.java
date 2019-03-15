package cn.feihutv.zhibofeihu.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.common.SignResponse;
import cn.feihutv.zhibofeihu.ui.widget.dialog.adapter.GridViewAdapter;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;

/**
 * Created by Administrator on 2017/2/15.
 */

public class SignDialog extends Dialog {

    @BindView(R.id.gridview)
    GridView gridview;
    @BindView(R.id.llBkg)
    FrameLayout llBkg;
    @BindView(R.id.btn_sign)
    Button btnSign;
    GridViewAdapter adapter;
    @BindView(R.id.btn_close)
    ImageView btnClose;
    private Context mContext;
    private List<Integer> list;
    private int maxDays;

    public interface SignListener{
        void sign();
    }
    private SignListener signListener;
    public void setSignListener(SignListener signListener){
        this.signListener=signListener;
    }

    public SignDialog(Context context, int theme, List<Integer> list, int maxDays) {
        super(context, R.style.color_dialog);
        mContext = context;
        this.list = list;
        this.maxDays = maxDays;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signdialog_fragment_view);
        ButterKnife.bind(this);

        adapter = new GridViewAdapter(mContext, list, maxDays);
        Log.e("print", "onCreate: ---->" + list + "  " + maxDays);
        gridview.setAdapter(adapter);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(signListener!=null){
                  signListener.sign();
              }
            }
        });
    }

    public void updata(SignResponse.SignData signData){
        FHUtils.showToast("签到成功");
        btnSign.setText("已签到");
        btnSign.setBackgroundResource(R.drawable.signed_btn_bg);

//        String tip="";
//        for (int i = 0; i < signData.getAwardList().size(); i++) {
//            String type = signData.getAwardList().get(i).getType();
//            if (type.equals("HB")) {
//                tip=tip+ "奖励虎币" + signData.getAwardList().get(i).getValue() + "个！";
//                // Toast.makeText(mContext, "奖励虎币" + award.getInt("Value") + "个！", Toast.LENGTH_SHORT).show();
//            } else {
//                tip=tip+ "奖励飞虎流星" + signData.getAwardList().get(i).getValue() + "个!";
//                //  Toast.makeText(mContext, "奖励飞虎流星" + award.getInt("Value") + "个!", Toast.LENGTH_SHORT).show();
//            }
//        }
//        Toast.makeText(mContext, tip, Toast.LENGTH_SHORT).show();
//        list.add(signData.getDay());
        adapter.setList(list);
        adapter.notifyDataSetChanged();
        SharePreferenceUtil.saveSeesionLong(mContext, "last_sign_time", System.currentTimeMillis());
    }

}
