package cn.feihutv.zhibofeihu.ui.home.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.common.FollowRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.FollowResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.home.SearchResponse;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.feihutv.zhibofeihu.utils.glideutil.GlideApp;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/30 14:24
 *      desc   : 搜索结果adapter
 *      version: 1.0
 * </pre>
 */

public class SearchResultAdapter extends BaseQuickAdapter<SearchResponse.SearchResponseData, BaseViewHolder> {


    public SearchResultAdapter(@Nullable List<SearchResponse.SearchResponseData> data) {
        super(R.layout.rec_user_item_view, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchResponse.SearchResponseData item) {

        TCUtils.showPicWithUrl(mContext, (ImageView) helper.getView(R.id.host_img), item.getHeadUrl(), R.drawable.face);

        helper.setText(R.id.host_name, item.getNickName());

        TCUtils.showLevelWithUrl(mContext, (ImageView) helper.getView(R.id.iv_grade), item.getLevel());

        helper.setText(R.id.tv_sign, item.getSignature());

        if (!item.isFollowed()) {
            // 已关注
            helper.setVisible(R.id.tv_add, true);
            helper.setVisible(R.id.back_btn, false);
        } else {
            // 未关注
            helper.setVisible(R.id.back_btn, true);
            helper.setVisible(R.id.tv_add, false);
        }

        helper.addOnClickListener(R.id.tv_add);

    }
}
