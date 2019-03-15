package cn.feihutv.zhibofeihu.ui.mv;

import android.app.Activity;

import java.util.List;

import cn.feihutv.zhibofeihu.data.local.model.SysbagBean;
import cn.feihutv.zhibofeihu.data.network.http.model.common.FollowResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMVCommentListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMVDetailResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GiftMVResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.LikeCommentResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.LikeMVResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.PlayMVResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.PostMVCommentResponse;
import cn.feihutv.zhibofeihu.ui.base.MvpView;

/**
 * <pre>
 *     author : sichu.chen
 *     time   :
 *     desc   : 定义UI操作
 *     version: 1.0
 * </pre>
 */
public interface MvVideoDetailsMvpView extends MvpView {
    void onGetMVDetailResp(GetMVDetailResponse response);

    void onGetMVCommentListResp(GetMVCommentListResponse response);

    void onLikeMVResp(LikeMVResponse response);

    void onLikeCommentResp(LikeCommentResponse response);

    void onGiftMVResp(GiftMVResponse response);

    void onPostMVCommentResp(PostMVCommentResponse response);

    void onFollowResp(FollowResponce response);

    void onPlayMVResp(PlayMVResponse response);

    void showGiftDialog(List<SysbagBean> sysbagBeanList);

    Activity getActivity();

    //定义view接口

    void onGetMvUrlResp(String url);

    void onGetDownMvUrlResp(String mvUrl);
}
