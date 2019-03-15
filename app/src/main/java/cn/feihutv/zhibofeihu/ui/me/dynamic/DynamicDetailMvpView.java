package cn.feihutv.zhibofeihu.ui.me.dynamic;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.common.LogShareResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.me.CommenItem;
import cn.feihutv.zhibofeihu.data.network.http.model.me.DeleteFeedResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LikeFeedResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LoadFeedDetailResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.PostFeedCommentResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.ShareFeedResponse;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : sichu.chen
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface DynamicDetailMvpView extends MvpView {

    //获取动态详情
    void onLoadFeedDetailResp(LoadFeedDetailResponse.LoadFeedDetailResponseData loadFeedDetailResponseData);

    //加载评论列表
    void onLoadCommentListResp(List<CommenItem> list);


    void onLikeFeedResp(LikeFeedResponse response);


    void onLogShareResp(LogShareResponce response);


    void onDeleteFeedResp(DeleteFeedResponse response);


    void onPostFeedCommentResp(PostFeedCommentResponse response);


    void onShareFeedResp(ShareFeedResponse response);

    void feedNotExist();

}
