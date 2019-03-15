package cn.feihutv.zhibofeihu.ui.me.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.DataManager;
import cn.feihutv.zhibofeihu.data.db.model.SysMountNewBean;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetMountListResponse;
import cn.feihutv.zhibofeihu.utils.UiUtil;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/13 17:21
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class MountBagAdapter extends BaseAdapter {

    private Context context;
    private List<GetMountListResponse.MountListResponseData> datas;

    public MountBagAdapter(Context context) {
        this.context = context;
        datas = new ArrayList<>();
    }

    public void setDatas(List<GetMountListResponse.MountListResponseData> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public interface UseBtnListener {
        void click(int type, int pos);
    }

    private UseBtnListener mListener;

    public void setUseBtnListener(UseBtnListener listener) {
        mListener = listener;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MountViewHolder viewHolder = null;
        if (convertView != null) {
            viewHolder = (MountViewHolder) convertView.getTag();
        } else {
            viewHolder = new MountViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.mount_bag_adapter, null);
            viewHolder.tvDays = (TextView) convertView.findViewById(R.id.days_count);
            viewHolder.tvUseing = (TextView) convertView.findViewById(R.id.is_use_text);
            viewHolder.mountImg = (ImageView) convertView.findViewById(R.id.mount_bg);
            viewHolder.mountFrm = (FrameLayout) convertView.findViewById(R.id.gift_frm);
            viewHolder.selectFrm = (FrameLayout) convertView.findViewById(R.id.select_frm);
            viewHolder.useBtn = (Button) convertView.findViewById(R.id.use_btn);
            convertView.setTag(viewHolder);
        }
        UiUtil.initialize(context);
        int screenWidth = UiUtil.getScreenWidth();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((screenWidth - 52) / 3, (screenWidth - 52) / 3);
        viewHolder.mountFrm.setLayoutParams(layoutParams);
        DataManager dataManager = FeihuZhiboApplication.getApplication().mDataManager;
        SysMountNewBean sysMountNewBean = dataManager.getMountBeanByID(String.valueOf(datas.get(position).getMount()));
        String cosMountRootPath = dataManager.getSysConfigBean().get(0).getCosMountRootPath();
        if (sysMountNewBean != null && cosMountRootPath != null) {
            String url = cosMountRootPath + "/" + sysMountNewBean.getIcon();
            Glide.with(context).
                    load(url).into(viewHolder.mountImg);
        }

        int type = datas.get(position).getType();
        long space = (long) datas.get(position).getExpireAt() - System.currentTimeMillis() / 1000;
        long day = space / (24 * 60 * 60) + ((space % (24 * 60 * 60)) == 0 ? 0 : 1);
        if (type == 0) {
            viewHolder.tvDays.setText("0天");
            viewHolder.tvDays.setTextColor(ContextCompat.getColor(context, R.color.btn_sure_forbidden));
            viewHolder.useBtn.setText("购买");
            viewHolder.tvUseing.setVisibility(View.INVISIBLE);
            viewHolder.useBtn.setBackgroundResource(R.drawable.selector_btn_background_forbidden);
        } else if (type == 1) {
            viewHolder.useBtn.setText("使用");
            viewHolder.tvDays.setText(day + "天");
            viewHolder.tvDays.setTextColor(ContextCompat.getColor(context, R.color.btn_sure_forbidden));
            viewHolder.tvUseing.setVisibility(View.INVISIBLE);
            viewHolder.useBtn.setBackgroundResource(R.drawable.selector_btn_background_forbidden);
            viewHolder.selectFrm.setVisibility(View.GONE);
        } else {
            viewHolder.useBtn.setText("使用");
            viewHolder.tvDays.setText(day + "天");
            viewHolder.tvDays.setTextColor(ContextCompat.getColor(context, R.color.app_white));
            viewHolder.tvUseing.setVisibility(View.VISIBLE);
            viewHolder.useBtn.setBackgroundResource(R.drawable.gray_bg);
            viewHolder.selectFrm.setVisibility(View.VISIBLE);
        }
        viewHolder.useBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.click(datas.get(position).getType(), position);
                }
            }
        });
        viewHolder.mountFrm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((datas.get(position).getType() == 2)) {
                    if (mListener != null) {
                        mListener.click(datas.get(position).getType(), position);
                    }
                }
            }
        });
        return convertView;
    }

    class MountViewHolder {
        TextView tvDays;
        TextView tvUseing;
        ImageView mountImg;
        FrameLayout mountFrm;
        FrameLayout selectFrm;
        Button useBtn;
    }

}
