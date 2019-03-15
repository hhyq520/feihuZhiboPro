package cn.feihutv.zhibofeihu.ui.me.adapter;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.db.model.SysGoodsNewBean;
import cn.feihutv.zhibofeihu.ui.widget.FHActivityCar;
import cn.feihutv.zhibofeihu.utils.UiUtil;


/**
 * @author clw
 * @date 2017/3/20
 */

public class ShopCarAdapter extends BaseQuickAdapter<SysGoodsNewBean, BaseViewHolder> {

    public ShopCarAdapter(@Nullable List<SysGoodsNewBean> data) {
        super(R.layout.item_shop_car, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SysGoodsNewBean item) {

        String cosGoodsRootPath = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosGoodsRootPath();
        String cosTagRootPath = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosTagRootPath();

        UiUtil.initialize(mContext);
        int screenWidth = UiUtil.getScreenWidth();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(screenWidth / 3 - 20, screenWidth / 3 - 20);
        layoutParams.setMargins(10, 20, 10, 20);
        helper.getView(R.id.tcag).setLayoutParams(layoutParams);
        ((FHActivityCar)helper.getView(R.id.tcag)).setImageUrl(cosGoodsRootPath + "/" + item.getIcon());
        ((FHActivityCar)helper.getView(R.id.tcag)).setNum(item.getName());
        helper.setText(R.id.tv_price, item.getPrice());
        helper.setText(R.id.expreience, "+" + item.getPrice());
        if (!TextUtils.isEmpty(item.getTagIcon())) {
            ((FHActivityCar)helper.getView(R.id.tcag)).setLuckImg(cosTagRootPath + "/" + item.getTagIcon());
        } else {
            ((FHActivityCar)helper.getView(R.id.tcag)).setLuckImgVis(false);
        }

        ((FHActivityCar)helper.getView(R.id.tcag)).setSelect(item.getIsSelected());

    }
}
