package cn.feihutv.zhibofeihu.ui.mv.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMVGiftLogResponse;
import cn.feihutv.zhibofeihu.utils.TCUtils;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/10/24 17:08
 *      desc   : mv适配器
 *      version: 1.0
 * </pre>
 */

public class MyMvGiftAdapter extends BaseQuickAdapter<GetMVGiftLogResponse.GetMVGiftLog, BaseViewHolder> {

    private Context mContext;


    public MyMvGiftAdapter(Context context, @Nullable List<GetMVGiftLogResponse.GetMVGiftLog> data
    ) {
        super(R.layout.item_layout_mv_gift, data);
        this.mContext=context;
    }



    @Override
    protected void convert(BaseViewHolder helper, GetMVGiftLogResponse.GetMVGiftLog item) {

        TCUtils.showPicWithUrl(mContext, (ImageView) helper.getView(R.id.mv_user_head),
                item.getAvatar(), R.drawable.face);

        helper.setText(R.id.mv_user_name,item.getNickname());
        helper.setText(R.id.mv_user_gift, item.getGiftName()+"x"+item.getGiftCnt());

        helper.setText(R.id.mv_coin,item.gethB()+"");


    }





}
