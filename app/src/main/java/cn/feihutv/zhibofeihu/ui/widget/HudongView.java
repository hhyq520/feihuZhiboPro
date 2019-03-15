package cn.feihutv.zhibofeihu.ui.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.ui.live.OnItemClickListener;

/**
 * Created by huanghao on 2017/6/13.
 */

public class HudongView {
    @BindView(R.id.rb_left)
    RadioButton rbLeft;
    @BindView(R.id.rb_right)
    RadioButton rbRight;
//    @BindView(R.id.rb_group)
//    RadioGroup rbGroup;
    private Context context;
    private View rootView;
    private int style;
    private OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }
    public HudongView(Context mContext, int style,boolean isPcLand) {
        this.context = mContext;
        if(isPcLand){
            rootView = LayoutInflater.from(mContext).inflate(
                    R.layout.hudong_view, null);
        }else{
            rootView = LayoutInflater.from(mContext).inflate(
                    R.layout.hudong_view_zhibo, null);
        }

        ButterKnife.bind(this, rootView);
        this.style = style;
        intit();
    }

    private void intit() {
        if (style == 1) {
            rbLeft.setText("公虎多 (1:1)");
            rbRight.setText("母虎多 (1:1)");
        } else if(style==2){
            rbLeft.setText("三只一样 (1:4)");
            rbRight.setText("三只不一样 (1:0.25)");
        }else if(style==3){
            rbLeft.setText("总和为单 (1:1)");
            rbRight.setText("总和为双 (1:1)");
        }else if(style==4){
            rbLeft.setText("有对子 (1:2.5)");
            rbRight.setText("没有对子 (1:0.4)");
        }else if(style==5){
            rbLeft.setText("总和末尾为0 (1:10)");
            rbRight.setText("总和末尾不为0 (1:0.1)");
        }
       rbRight.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(mListener!=null){
                   if(style==1){
                       mListener.onItemClick(2);
                   }else if(style==2){
                       mListener.onItemClick(4);
                   }else if(style==3){
                       mListener.onItemClick(6);
                   }else if(style==4){
                       mListener.onItemClick(8);
                   }else if(style==5){
                       mListener.onItemClick(10);
                   }

               }
               rbRight.setChecked(true);
               rbLeft.setChecked(false);
           }
       });
        rbLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null){
                    if(style==1){
                        mListener.onItemClick(1);
                    }else if(style==2){
                        mListener.onItemClick(3);
                    }else if(style==3){
                        mListener.onItemClick(5);
                    }else if(style==4){
                        mListener.onItemClick(7);
                    }else if(style==5){
                        mListener.onItemClick(9);
                    }
                }
                rbLeft.setChecked(true);
                rbRight.setChecked(false);
            }
        });
    }

    public View getView() {
        return rootView;
    }

    public void clearSelect(){

        rbLeft.setChecked(false);
        rbRight.setChecked(false);
    }
}
