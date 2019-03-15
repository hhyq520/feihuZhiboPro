package cn.feihutv.zhibofeihu.ui.me;


import cn.feihutv.zhibofeihu.ui.base.BaseFragment;
import cn.feihutv.zhibofeihu.ui.widget.scrolllayoutview.ScrollableHelper;

/**
 * Created by Administrator on 2017/2/25.
 */

public abstract class BaseDynamicFragment extends BaseFragment implements ScrollableHelper.ScrollableContainer {

    public abstract void pullToRefresh();

    public abstract void refreshComplete();
}
