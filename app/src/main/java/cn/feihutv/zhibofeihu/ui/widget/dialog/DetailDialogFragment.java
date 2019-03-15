package cn.feihutv.zhibofeihu.ui.widget.dialog;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.utils.FastBlur;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.feihutv.zhibofeihu.utils.TCUtils;

/**
 * Created by clw on 2017/2/23.
 */

public class DetailDialogFragment extends DialogFragment {

    private Button btn_cancle;
    private ImageView imgBg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public interface OnCancelDialogListener {
        void cancel();
    }

    private OnCancelDialogListener mListener;

    public void setOnCancelDialogListener(OnCancelDialogListener listener) {
        mListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog mDetailDialog = new Dialog(getActivity(), R.style.dialog);
        mDetailDialog.setContentView(R.layout.dialog_publish_detail);
        mDetailDialog.setCancelable(false);

        final TextView tvDetailAdmires = (TextView) mDetailDialog.findViewById(R.id.tv_admires);
        final TextView tvDetailWatchCount = (TextView) mDetailDialog.findViewById(R.id.tv_members);
        final TextView tvHb = (TextView) mDetailDialog.findViewById(R.id.tv_hb);
        ImageView img_detail = (ImageView) mDetailDialog.findViewById(R.id.img_detail);
        TextView tvDetailTime = (TextView) mDetailDialog.findViewById(R.id.tv_time);
        imgBg = (ImageView) mDetailDialog.findViewById(R.id.img_bg);
        //确认则显示观看detail
        // 时间
        tvDetailTime.setText(getArguments().getString("time"));
        tvDetailAdmires.setText(getArguments().getInt("Followers") + "");
        tvDetailWatchCount.setText(getArguments().getInt("Watches") + "");
        tvHb.setText(getArguments().getInt("GHB") + "");
        // 头像
        TCUtils.showPicWithUrl(getContext(), img_detail, SharePreferenceUtil.getSession(getContext(),"PREF_KEY_HEADURL"), R.drawable.face);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            RequestBuilder<Bitmap> requestBuilder = Glide.with(this).asBitmap().apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE).transform(new FastBlur(getContext(), 2)));
            requestBuilder.load(SharePreferenceUtil.getSession(getContext(),"PREF_KEY_HEADURL")).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    try {
                        int width = resource.getWidth();
                        int height = resource.getHeight();
                        float rate = (float) getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().heightPixels;
                        int wTargrt = (int) (height * rate);
                        int edge = (width - wTargrt) / 2;
                        Bitmap bitmap = Bitmap.createBitmap(resource, edge, 0, wTargrt, height);
                        imgBg.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        init(mDetailDialog);
        return mDetailDialog;
    }

    private void init(final Dialog mDetailDialog) {
        btn_cancle = (Button) mDetailDialog.findViewById(R.id.btn_cancel);
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDetailDialog.dismiss();
                if (mListener != null) {
                    mListener.cancel();
                }
            }
        });
    }
}
