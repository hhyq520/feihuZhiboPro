package cn.feihutv.zhibofeihu.ui.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.feihutv.zhibofeihu.R;



/**
 * Created by chenliwen on 2017/6/14 14:35.
 * 佛祖保佑，永无BUG
 */

public class BottomBarView extends RelativeLayout {
    private int msgCount;
    private TextView bar_num, secret_txt;
    private OnCliclMsg onCliclMsg;

    public void setOnCliclMsg(OnCliclMsg onCliclMsg) {
        this.onCliclMsg = onCliclMsg;
    }

    public BottomBarView(Context context) {
        this(context, null);
    }

    public BottomBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        RelativeLayout rl = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.custom_notice_view, this, true);
        bar_num = (TextView) rl.findViewById(R.id.bar_num);
        secret_txt = (TextView) rl.findViewById(R.id.secret_txt);
        secret_txt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onCliclMsg != null) {
                    onCliclMsg.clickMsg();
                }
            }
        });
    }



    public void setMessageCount(int count) {
        msgCount = count;
        if (count == 0) {
            bar_num.setVisibility(View.GONE);
        } else {
            bar_num.setVisibility(View.VISIBLE);
            if (count < 100) {
                bar_num.setText(count + "");
            } else {
                bar_num.setText("99+");
            }
        }
        invalidate();
    }

    public void addMsg() {
        setMessageCount(msgCount + 1);
    }

    public interface OnCliclMsg{
        void clickMsg();
    }
}
