package cn.feihutv.zhibofeihu.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;


import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.ui.widget.CBProgressBar;

/**
 * Created by huanghao on 2017/6/9.
 */

public class TimeLeftDialog extends Dialog {

    @BindView(R.id.my_progress)
    CBProgressBar myProgress;
    @BindView(R.id.btn_start)
    Button btnStart;
    @BindView(R.id.btn_begin)
    Button btnBegin;
    private Context mContext;
    private int countDown;
    private int progress = 0;
    Timer timer;

    @OnClick(R.id.btn_begin)
    public void onClick() {
        if(mListener!=null){
            mListener.begin();
        }
    }

    public interface TimeLeftInterface {
        void start();
        void begin();
    }

    private TimeLeftInterface mListener;

    public void setTimeLeftListener(TimeLeftInterface listener) {
        mListener = listener;
    }

    public TimeLeftDialog(Context context, int countDown) {
        super(context, R.style.floag_dialog);
        mContext = context;
        this.countDown = countDown;
        timer = new Timer();
    }

    public void setButtonEnable(boolean isSucess){
        if(btnStart!=null) {
            btnStart.setSelected(false);
            btnStart.setEnabled(false);
            if(isSucess) {
                btnStart.setText("已抢到");
            }else{
                btnStart.setText("已被抢");
            }
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (timer != null) {
            timer.cancel();
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (progress == 1979) {
                        btnStart.setSelected(true);
                        btnStart.setEnabled(true);
                    }
                    if (progress < 1980) {
                        progress++;
                        myProgress.setProgress(progress);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_left_dialog);
        ButterKnife.bind(this);
        if (countDown == 0) {
            myProgress.setMax(1980);
            myProgress.setProgress(1980);
            btnStart.setSelected(true);
            btnStart.setEnabled(true);
        } else {
            btnStart.setSelected(false);
            btnStart.setEnabled(false);
            myProgress.setMax(1980);
            progress = 1980 - countDown;
            myProgress.setProgress(progress);
            myProgress.setCountDown(countDown);
            timer.schedule(task, 1000, 1000);
        }
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progress >= countDown) {
                    if (mListener != null) {
                        mListener.start();
                    }
                }
            }
        });
    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };
}
