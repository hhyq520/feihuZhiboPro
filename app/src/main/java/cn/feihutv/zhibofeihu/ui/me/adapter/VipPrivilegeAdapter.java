package cn.feihutv.zhibofeihu.ui.me.adapter;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.local.model.VipPrivilegeEntity;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/14 20:22
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class VipPrivilegeAdapter extends BaseQuickAdapter<VipPrivilegeEntity, BaseViewHolder> {


    public VipPrivilegeAdapter(@Nullable List<VipPrivilegeEntity> data) {
        super(R.layout.item_layout_vipprivilege, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VipPrivilegeEntity item) {
        helper.setText(R.id.tv, item.getNameId());
        helper.setImageResource(R.id.iv, item.getResId());
    }
}
