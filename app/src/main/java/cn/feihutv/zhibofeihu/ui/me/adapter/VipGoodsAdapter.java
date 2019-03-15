package cn.feihutv.zhibofeihu.ui.me.adapter;

import android.support.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import java.util.List;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.db.model.SysVipGoodsBean;
import cn.feihutv.zhibofeihu.utils.FHUtils;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/14 18:59
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class VipGoodsAdapter extends BaseQuickAdapter<SysVipGoodsBean, BaseViewHolder> {

    public VipGoodsAdapter(@Nullable List<SysVipGoodsBean> data) {
        super(R.layout.item_layout_vipgoods, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SysVipGoodsBean item) {
        helper.setText(R.id.tv_goodsName, item.getGoodsName());
        helper.setText(R.id.tv_czz, "赠送座驾" + item.getVipMountDays() + "天，成长值" + item.getCzz() + "点，小喇叭" + item.getXiaolaba() + "个");
        if (item.getRecommend() == 1) {
            helper.setVisible(R.id.img_recommend, true);
        } else {
            helper.setVisible(R.id.img_recommend, false);
        }

        if (item.getDiscount() == 1) {
            helper.setVisible(R.id.img_discount, true);
        } else {
            helper.setVisible(R.id.img_discount, false);
        }

        if (item.getIsSelect() == 1) {
            // 选中item
            helper.getView(R.id.rl_vipgoods).setSelected(true);
        } else {
            // 未选中item
            helper.getView(R.id.rl_vipgoods).setSelected(false);
        }
        helper.setText(R.id.tv_hb, FHUtils.intToF(item.getHb()));
    }
}
