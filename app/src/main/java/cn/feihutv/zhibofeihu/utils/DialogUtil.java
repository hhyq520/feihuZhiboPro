package cn.feihutv.zhibofeihu.utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import com.chinanetcenter.StreamPusher.sdk.SPManager;

import java.util.ArrayList;
import java.util.List;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.utils.weiget.view.PickerView;

/**
 * Created by chenliwen on 2017/7/31 15:21.
 * 佛祖保佑，永无BUG
 */

public class DialogUtil {

    public static SPManager.FilterType mCurrentFilter = SPManager.FilterType.BEAUTYG;

    public static Dialog showBeautyPickDialog(final Activity activity) {
        Dialog pickDialog = new Dialog(activity, R.style.floag_dialog);
        pickDialog.setContentView(R.layout.beauty_pick_view);
//        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Translucent_Diglog);
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View rootView = inflater.inflate(R.layout.beauty_pick_view, null);
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog.getWindow();
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = (int) (display.getWidth()); //设置宽度
        pickDialog.getWindow().setAttributes(lp);
        final SeekBar beautySeekBar = (SeekBar) pickDialog.findViewById(R.id.beauty_level);
        final TextView beautyValueTv = (TextView) pickDialog.findViewById(R.id.beauty_level_value);
        PickerView pickerView=(PickerView)pickDialog.findViewById(R.id.pickerView);
        List<String> data = new ArrayList<String>();
        data.add("美白");
        data.add("美颜平滑");
        data.add("美颜普通1");
        data.add("美颜普通2");
        pickerView.setData(data);
        pickerView.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                switch (text){
                    case "美白":
                        mCurrentFilter= SPManager.FilterType.SKINWHITEN;
                        SPManager.switchFilter(mCurrentFilter);
                        break;
                    case "美颜平滑":
                        mCurrentFilter= SPManager.FilterType.BEAUTYB;
                        SPManager.switchFilter(mCurrentFilter);
                        break;
                    case "美颜普通1":
                        mCurrentFilter= SPManager.FilterType.BEAUTYG;
                        SPManager.switchFilter(mCurrentFilter);
                        break;
                    case "美颜普通2":
                        mCurrentFilter= SPManager.FilterType.BEAUTYG1;
                        SPManager.switchFilter(mCurrentFilter);
                        break;
                }
            }
        });
        beautySeekBar.setMax(10);//美颜参数范围0~10
        if (mCurrentFilter.getLevel() < 0) {
            beautySeekBar.setEnabled(false);
            beautySeekBar.setProgress(0);
            beautyValueTv.setText("" + 0);
        } else {
            beautySeekBar.setEnabled(true);
            beautySeekBar.setProgress(mCurrentFilter.getLevel());
            beautyValueTv.setText("" + mCurrentFilter.getLevel());
        }
        SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!fromUser) return;
                switch (seekBar.getId()) {
                    case R.id.beauty_level:
                        if (progress != mCurrentFilter.getLevel()) {
                            mCurrentFilter.setLevel(progress);
                            beautyValueTv.setText("" + progress);
                            SPManager.switchFilter(mCurrentFilter);
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        beautySeekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        // 设置宽度为屏宽, 靠近屏幕底部。
        pickDialog.show();
        return pickDialog;
    }
}
