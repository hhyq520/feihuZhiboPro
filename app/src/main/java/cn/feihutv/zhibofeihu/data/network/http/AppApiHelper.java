
package cn.feihutv.zhibofeihu.data.network.http;


import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;

import cn.feihutv.zhibofeihu.data.network.http.model.bangdan.GetGuardRankRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.bangdan.GetGuardRankResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.bangdan.LoadContriRankListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.bangdan.LoadContriRankListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.bangdan.LoadLuckRankListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.bangdan.LoadLuckRecordListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.bangdan.LoadLuckRecordListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.common.BuyGoodsRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.BuyGoodsResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.CertifiResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.common.CertifiResquest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.CheckUpdateRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.CheckUpdateResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.common.FollowRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.FollowResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.GetFriendsRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.GetFriendsResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.GetSocketTokenRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.GetSocketTokenResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.common.GetVerifyCodeRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.GetVerifyCodeResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadMsgListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadMsgListResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomContriRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomContriResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadSignRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadSignResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LocationRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LocationResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LogDeviceRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LogDeviceResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LogShareRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LogShareResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.MessageRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.MessageResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.RestPsdRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.RestPsdResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.common.SendGiftRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.SendGiftResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.SignRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.SignResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.common.UnFollowRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.UnFollowResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.WebHttpRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.WebHttpResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.home.GetBannerRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.home.GetBannerResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.home.LoadRoomListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.home.LoadRoomListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.home.RecommendRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.home.RecommendResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.home.SearchRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.home.SearchResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.BanRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.BanResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.CancelRoomMgrRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.CancelRoomMgrResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.GetCurrMountRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.GetCurrMountResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.GetExtGameIconRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.GetExtGameIconResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.GetRoomMrgsRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.GetRoomMrgsResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.LoadOtherRoomsRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.LoadOtherRoomsResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.LoadRoomGuestRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.LoadRoomGuestResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.LoadRoomMemberResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.LoadRoomMembersRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.ReportRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.ReportResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.RoomVipRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.RoomVipResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.SendLoudSpeakRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.SendLoudSpeakResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.SetRoomMgrRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.SetRoomMgrResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.SwitchAppFocusRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.SwitchAppFocusResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.TestStartLiveRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.TestStartLiveResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.me.AcceptTradeRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.AcceptTradeResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.BindAlipayRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.BindAlipayResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.BindPhoneRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.BindPhoneResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.BlockRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.BlockResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.BuyGuardRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.BuyGuardResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.BuyShopAccountIdRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.BuyShopAccountIdResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.CheckAccountIdRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.CheckAccountIdResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.CheckGuardInfoRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.CheckGuardInfoResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.CheckVerifiCodeRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.CheckVerifiCodeResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.DeleteFeedRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.DeleteFeedResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.EncashRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.EncashResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.FollowsRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.FollowsResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetBlacklistRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetBlacklistResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetCosSignRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetCosSignResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetEncashInfoRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetEncashInfoResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetEncashListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetEncashListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetMountListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetMountListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetMsgSwitchStatusRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetMsgSwitchStatusResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetPayListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetPayListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetUserGuardListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetUserGuardListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetUserGuardsRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.GetUserGuardsResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.IsFeedExistRequset;
import cn.feihutv.zhibofeihu.data.network.http.model.me.IsFeedExistResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LikeFeedRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LikeFeedResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LoadAllFeedListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LoadBagGiftsRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LoadBagGiftsResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LoadCommentListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LoadCommentListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LoadFeedDetailRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LoadFeedDetailResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LoadFeedListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LoadFeedListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LoadLiangSearchKeyRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LoadLiangSearchKeyResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LoadShopAccountIdListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LoadShopAccountIdListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LoadUserHBDataRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LoadUserHBDataResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.MakeTradeRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.MakeTradeResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.ModifyPhoneRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.ModifyPhoneResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.ModifyUserPassRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.ModifyUserPassResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.PayRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.PayResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.PostFeedCommentRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.PostFeedCommentResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.PostFeedRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.PostFeedResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.SetMountRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.SetMountResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.SetMsgSwitchStatusRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.SetMsgSwitchStatusResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.ShareFeedRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.ShareFeedResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.me.UnblockRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.me.UnblockResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.BuyMVRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.BuyMVResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.CancelMVNoticeRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.CancelMVNoticeResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.CollectNeedRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.CollectNeedResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.DeleteMVRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.DeleteMVResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.DeleteNeedRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.DeleteNeedResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.DownLoadMvRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.DownLoadMvResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.EditNeedRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.EditNeedResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.EnablePostMVRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.EnablePostMVResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.FeedbackMVRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.FeedbackMVResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetAllMvListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetAllMvListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetAllNeedListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetAllNeedListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMVCommentListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMVCommentListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMVDetailRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMVDetailResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMVGiftLogRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMVGiftLogResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMVTokenRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMVTokenResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMyCustomMadeMVListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMyCustomMadeMVListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMyMVListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMyMVListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMyNeedListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMyNeedListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMyNeedMVListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMyNeedMVListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetNeedCollectListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetNeedCollectListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetNeedSysHBRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetNeedSysHBResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetOtherMVListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetOtherMVListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GiftMVRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GiftMVResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.LikeCommentRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.LikeCommentResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.LikeMVRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.LikeMVResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.PlayMVRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.PlayMVResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.PostMVCommentRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.PostMVCommentResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.PostMvRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.PostMvResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.PostNeedRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.PostNeedResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.QueryMVNoticeRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.QueryMVNoticeResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.ShareMvRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.ShareMvResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.UnCollectNeedRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.UnCollectNeedResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.user.BagRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.user.BagResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.user.CreateUserRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.user.CreateUserResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.user.LoginRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.user.LoginResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.vip.BuyVipRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.vip.BuyVipResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.vip.GetVipReceiveLogRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.vip.GetVipReceiveLogResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.vip.GetVipSendLogRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.vip.GetVipSendLogResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.vip.SendVipRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.vip.SendVipResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GameBetRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GameBetResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GamePreemptRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GamePreemptResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GameSettleRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GameSettleResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameBetHistoryRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameBetHistoryResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameBetRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameBetResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameListResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameRoundResultDetailRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameRoundResultDetailResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameSettleHistoryRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameSettleHistoryResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameStatusRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetGameStatusResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetJackpotDataRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetJackpotDataResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetLuckLogsByIdRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetLuckLogsByIdResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetMyLuckLogsByIdRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetMyLuckLogsByIdResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetUserGameDataV3Request;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.GetUserGameDataV3Responce;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.JackpotCountDownRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.wanfa.JackpotCountDownResponce;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/09/25
 *     desc   : http 网络请求api 实现
 *     version: 1.0
 * </pre>
 */

@Singleton
public class AppApiHelper implements ApiHelper {

    private ApiHeader mApiHeader;

    @Inject
    public AppApiHelper(ApiHeader apiHeader) {
        mApiHeader = apiHeader;
    }

    @Override
    public ApiHeader getApiHeader() {
        return mApiHeader;
    }

    @Override
    public Observable<WebHttpResponse> doWebHttpApiCall(final WebHttpRequest request) {
        return Observable.create(new ObservableOnSubscribe<WebHttpResponse>() {
            @Override
            public void subscribe(final ObservableEmitter<WebHttpResponse> observable) throws Exception {
                boolean needSign = true;
                String apiName = request.getApiName();
                if (apiName.equals("createUser") || apiName.equals("userLogin") || apiName.equals("platformLogin") || apiName.equals("guestLogin") || apiName.equals("getVerifiCode") || apiName.equals("resetPassword") || apiName.equals("checkVersion") || apiName.equals("logDevice")) {
                    needSign = false;
                }
                if (apiName.equals("getVerifiCode")) {
                    if (Integer.valueOf(request.getReq().get("codeId")) > 2) {
                        needSign = true;
                    }
                }
                if (needSign) {
                    ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
                    protectedApiHeader.setWebHttpRequestSignature(request.getReq(), request.getApiName());
                    Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_SERVER_RUL + request.getApiName())
                            .addHeaders(mApiHeader.getProtectedApiHeader())
                            .addBodyParameter(protectedApiHeader.getRequestParams(request.getReq()))
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    // do anything with response
                                    WebHttpResponse mWHttpResponse = new WebHttpResponse();


                                    mWHttpResponse.setResponse(response);
                                    observable.onNext(mWHttpResponse);
                                    observable.onComplete();
                                }

                                @Override
                                public void onError(ANError error) {
                                    // handle error
                                    WebHttpResponse mWHttpResponse = new WebHttpResponse();
                                    mWHttpResponse.setCode(-1);//请求失败
                                    observable.onNext(mWHttpResponse);
                                    observable.onComplete();
                                }
                            });
                } else {
                    Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_SERVER_RUL + request.getApiName())
                            .addHeaders(mApiHeader.getPublicApiHeader())
                            .addBodyParameter(request)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    // do anything with response
                                    WebHttpResponse mWHttpResponse = new WebHttpResponse();
                                    mWHttpResponse.setResponse(response);
                                    observable.onNext(mWHttpResponse);
                                    observable.onComplete();
                                }

                                @Override
                                public void onError(ANError error) {
                                    // handle error
                                    WebHttpResponse mWHttpResponse = new WebHttpResponse();
                                    mWHttpResponse.setCode(-1);//请求失败
                                    observable.onNext(mWHttpResponse);
                                    observable.onComplete();
                                }
                            });
                }

            }
        });
    }

    @Override
    public Observable<LoginResponse> doServerLoginApiCall(LoginRequest.ServerLoginRequest request) {
        return Rx2AndroidNetworking.post(ApiEndPoint.UserPoint.ENDPOINT_SERVER_LOGIN)
                .addHeaders(mApiHeader.getPublicApiHeader())
                .addBodyParameter(request)
                .build()
                .getObjectObservable(LoginResponse.class);
    }

    @Override
    public Observable<LoginResponse> doPlatformLoginApiCall(LoginRequest.PlatformLoginRequest request) {
        return Rx2AndroidNetworking.post(ApiEndPoint.UserPoint.ENDPOINT_PLATFORM_LOGIN)
                .addHeaders(mApiHeader.getPublicApiHeader())
                .addBodyParameter(request)
                .build()
                .getObjectObservable(LoginResponse.class);
    }

    @Override
    public Observable<LoginResponse> doGuestLoginApiCall(LoginRequest.GuestLoginRequest request) {
        return Rx2AndroidNetworking.post(ApiEndPoint.UserPoint.ENDPOINT_GUEST_LOGIN)
                .addHeaders(mApiHeader.getPublicApiHeader())
                .addBodyParameter(request)
                .build()
                .getObjectObservable(LoginResponse.class);
    }

    @Override
    public Observable<CheckUpdateResponse> doCheckUpdateApiCall(CheckUpdateRequest request) {
        return Rx2AndroidNetworking.post(ApiEndPoint.CommonPoint.ENDPOINT_CHECK_UPDATE)
                .addHeaders(mApiHeader.getPublicApiHeader())
                .addBodyParameter(request)
                .build()
                .getObjectObservable(CheckUpdateResponse.class);
    }

    @Override
    public Observable<GetSocketTokenResponse> doGetSocketTokenApiCall(GetSocketTokenRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.CommonPoint.ENDPOINT_GET_SOCKET_TOKEN);

        return Rx2AndroidNetworking.post(ApiEndPoint.CommonPoint.ENDPOINT_GET_SOCKET_TOKEN)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetSocketTokenResponse.class);
    }

    @Override
    public Observable<LogDeviceResponse> doLogDeviceApiCall(LogDeviceRequest request) {
        return Rx2AndroidNetworking.post(ApiEndPoint.CommonPoint.ENDPOINT_LOG_DEVICE)
                .addHeaders(mApiHeader.getPublicApiHeader())
                .addBodyParameter(request)
                .build()
                .getObjectObservable(LogDeviceResponse.class);
    }

    @Override
    public Observable<LoadRoomListResponse> doLoadRoomListByTagApiCall(LoadRoomListRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.TopPoint.ENDPOINT_LOAD_ROOM_LIST);

        return Rx2AndroidNetworking.post(ApiEndPoint.TopPoint.ENDPOINT_LOAD_ROOM_LIST)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(LoadRoomListResponse.class);
    }

    @Override
    public Observable<LoadRoomListResponse> doLoadCareRecommendedListApiCall(LoadRoomListRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.TopPoint.ENDPOINT_LOAD_CARE_RECOMMENDED_LIST);

        return Rx2AndroidNetworking.post(ApiEndPoint.TopPoint.ENDPOINT_LOAD_CARE_RECOMMENDED_LIST)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(LoadRoomListResponse.class);
    }

    @Override
    public Observable<BagResponse> doGetUserBagDataApiCall(BagRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.UserPoint.ENDPOINT_USER_BAG);
        return Rx2AndroidNetworking.post(ApiEndPoint.UserPoint.ENDPOINT_USER_BAG)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(BagResponse.class);
    }

    @Override
    public Observable<CertifiResponse> doGetUseCertifiDataApiCall(CertifiResquest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.UserPoint.ENDPOINT_CERTIFI_DATA);
        return Rx2AndroidNetworking.post(ApiEndPoint.UserPoint.ENDPOINT_CERTIFI_DATA)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(CertifiResponse.class);
    }

    @Override
    public Observable<LoadRoomResponce> doGetRoomDataApiCall(LoadRoomRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.CommonPoint.ENDPOINT_GET_ROOM_INFO);
        return Rx2AndroidNetworking.post(ApiEndPoint.CommonPoint.ENDPOINT_GET_ROOM_INFO)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(LoadRoomResponce.class);
    }

    @Override
    public Observable<LoadSignResponce> doGetSignDataApiCall(LoadSignRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignatureV(request, ApiEndPoint.CommonPoint.ENDPOINT_GET_SIGN_V);
        return Rx2AndroidNetworking.post(ApiEndPoint.CommonPoint.ENDPOINT_GET_SIGN_DATA)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(LoadSignResponce.class);
    }

    @Override
    public Observable<SignResponse> doSignApiCall(SignRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignatureV(request, ApiEndPoint.CommonPoint.ENDPOINT_TO_SIGN_V);
        return Rx2AndroidNetworking.post(ApiEndPoint.CommonPoint.ENDPOINT_TO_SIGN)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(SignResponse.class);
    }


    @Override
    public Observable<LoadContriRankListResponse> doLoadContriRankListApiCall(LoadContriRankListRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.BandDanPoint.ENDPOINT_LOAD_CONTRI_RANK_LIST);
        return Rx2AndroidNetworking.post(ApiEndPoint.BandDanPoint.ENDPOINT_LOAD_CONTRI_RANK_LIST)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(LoadContriRankListResponse.class);
    }

    @Override
    public Observable<LoadContriRankListResponse> doLoadIncomeRankListApiCall(LoadContriRankListRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.BandDanPoint.ENDPOINT_LOAD_INCOME_RANK_LIST);
        return Rx2AndroidNetworking.post(ApiEndPoint.BandDanPoint.ENDPOINT_LOAD_INCOME_RANK_LIST)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(LoadContriRankListResponse.class);
    }

    @Override
    public Observable<LoadLuckRecordListResponse> doLoadLuckRecordListApiCall(LoadLuckRecordListRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.BandDanPoint.ENDPOINT_LOAD_LUCK_RECORD_LIST);
        return Rx2AndroidNetworking.post(ApiEndPoint.BandDanPoint.ENDPOINT_LOAD_LUCK_RECORD_LIST)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(LoadLuckRecordListResponse.class);
    }

    @Override
    public Observable<GetGuardRankResponse> doGetGuardRankApiCall(GetGuardRankRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.BandDanPoint.ENDPOINT_GET_GUARD_RANK);
        return Rx2AndroidNetworking.post(ApiEndPoint.BandDanPoint.ENDPOINT_GET_GUARD_RANK)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetGuardRankResponse.class);
    }

    @Override
    public Observable<LoadLuckRankListResponse> doLoadLuckRankListApiCall(LoadLuckRecordListRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.BandDanPoint.ENDPOINT_LOAD_LUCKRANK_LIST);
        return Rx2AndroidNetworking.post(ApiEndPoint.BandDanPoint.ENDPOINT_LOAD_LUCKRANK_LIST)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(LoadLuckRankListResponse.class);
    }

    @Override
    public Observable<GetVerifyCodeResponse> doGetVerifyCodeApiCall(GetVerifyCodeRequest request) {
        if (request.getCodeId() > 2) {
            //需要签名
            ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
            protectedApiHeader.setRequestSignature(request, ApiEndPoint.CommonPoint.ENDPOINT_VERIFYCODE);
            return Rx2AndroidNetworking.post(ApiEndPoint.CommonPoint.ENDPOINT_VERIFYCODE)
                    .addHeaders(protectedApiHeader)
                    .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                    .build()
                    .getObjectObservable(GetVerifyCodeResponse.class);
        } else {
            //需要特殊签名
            ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
            protectedApiHeader.setUserId("0");
            protectedApiHeader.setApiKey("Hda@ui8F56$Hgf");
            protectedApiHeader.setRequestSignatureVericode(request, ApiEndPoint.CommonPoint.ENDPOINT_VERIFYCODE);
            return Rx2AndroidNetworking.post(ApiEndPoint.CommonPoint.ENDPOINT_VERIFYCODE)
                    .addHeaders(mApiHeader.getPublicApiHeader())
                    .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                    .build()
                    .getObjectObservable(GetVerifyCodeResponse.class);
        }
    }

    @Override
    public Observable<RestPsdResponse> doRestPsdApiCall(RestPsdRequest request) {
        return Rx2AndroidNetworking.post(ApiEndPoint.CommonPoint.ENDPOINT_RESTPASSWORD)
                .addHeaders(mApiHeader.getPublicApiHeader())
                .addBodyParameter(request)
                .build()
                .getObjectObservable(RestPsdResponse.class);
    }

    @Override
    public Observable<CreateUserResponse> doCreateUserApiCall(CreateUserRequest request) {
        return Rx2AndroidNetworking.post(ApiEndPoint.CommonPoint.ENDPOINT_CREATEUSER)
                .addHeaders(mApiHeader.getPublicApiHeader())
                .addBodyParameter(request)
                .build()
                .getObjectObservable(CreateUserResponse.class);
    }

    @Override
    public Observable<GetBannerResponse> doGetBannerByTypeApiCall(GetBannerRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.TopPoint.ENDPOINT_GET_BANNER_BY_TYPE);
        return Rx2AndroidNetworking.post(ApiEndPoint.TopPoint.ENDPOINT_GET_BANNER_BY_TYPE)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetBannerResponse.class);
    }

    @Override
    public Observable<LocationResponce> doGetLocationApiCall(LocationRequest request) {
        return Rx2AndroidNetworking.get(ApiEndPoint.CommonPoint.ENDPOINT_LOCATION)
                .addQueryParameter(request)
                .build()
                .getObjectObservable(LocationResponce.class);
    }

    @Override
    public Observable<SwitchAppFocusResponce> doSwitchAppFocusApiCall(SwitchAppFocusRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.LiveRoomPoint.ENDPOINT_SWITCHAPPFOCUS);
        return Rx2AndroidNetworking.post(ApiEndPoint.LiveRoomPoint.ENDPOINT_SWITCHAPPFOCUS)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(SwitchAppFocusResponce.class);
    }

    @Override
    public Observable<LogShareResponce> doLogShareApiCall(LogShareRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.CommonPoint.ENDPOINT_LOGSHARE);
        return Rx2AndroidNetworking.post(ApiEndPoint.CommonPoint.ENDPOINT_LOGSHARE)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(LogShareResponce.class);
    }

    @Override
    public Observable<FollowResponce> doFollowApiCall(FollowRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.CommonPoint.ENDPOINT_FOLLOW);
        return Rx2AndroidNetworking.post(ApiEndPoint.CommonPoint.ENDPOINT_FOLLOW)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(FollowResponce.class);
    }

    @Override
    public Observable<UnFollowResponce> doUnFollowApiCall(UnFollowRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.CommonPoint.ENDPOINT_UNFOLLOW);
        return Rx2AndroidNetworking.post(ApiEndPoint.CommonPoint.ENDPOINT_UNFOLLOW)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(UnFollowResponce.class);
    }

    @Override
    public Observable<SetRoomMgrResponce> doSetRoomMgrApiCall(SetRoomMgrRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.LiveRoomPoint.ENDPOINT_SETROOMMGR);
        return Rx2AndroidNetworking.post(ApiEndPoint.LiveRoomPoint.ENDPOINT_SETROOMMGR)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(SetRoomMgrResponce.class);
    }

    @Override
    public Observable<CancelRoomMgrResponce> doCancelRoomMgrCall(CancelRoomMgrRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.LiveRoomPoint.ENDPOINT_CANCEL_ROOMMGR);
        return Rx2AndroidNetworking.post(ApiEndPoint.LiveRoomPoint.ENDPOINT_CANCEL_ROOMMGR)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(CancelRoomMgrResponce.class);
    }

    @Override
    public Observable<BanResponce> doBanApiCall(BanRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.LiveRoomPoint.ENDPOINT_BAN);
        return Rx2AndroidNetworking.post(ApiEndPoint.LiveRoomPoint.ENDPOINT_BAN)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(BanResponce.class);
    }

    @Override
    public Observable<LoadRoomContriResponce> doLoadRoomContriApiCall(LoadRoomContriRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.CommonPoint.ENDPOINT_LOADROOMCONTRILIST);
        return Rx2AndroidNetworking.post(ApiEndPoint.CommonPoint.ENDPOINT_LOADROOMCONTRILIST)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(LoadRoomContriResponce.class);
    }

    @Override
    public Observable<JackpotCountDownResponce> doJackpotCountDownRequestApiCall(JackpotCountDownRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.WanFaPoint.ENDPOINT_GETJACKPOTCOUNTDOWN);
        return Rx2AndroidNetworking.post(ApiEndPoint.WanFaPoint.ENDPOINT_GETJACKPOTCOUNTDOWN)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(JackpotCountDownResponce.class);
    }

    @Override
    public Observable<SendLoudSpeakResponce> doSendLoudSpeakApiCall(SendLoudSpeakRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.LiveRoomPoint.ENDPOINT_SENDLOUDSPEAKER);
        return Rx2AndroidNetworking.post(ApiEndPoint.LiveRoomPoint.ENDPOINT_SENDLOUDSPEAKER)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(SendLoudSpeakResponce.class);
    }

    @Override
    public Observable<LoadRoomMemberResponce> doLoadRoomMembersApiCall(LoadRoomMembersRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.LiveRoomPoint.ENDPOINT_LOADROOMMEMBERS);
        return Rx2AndroidNetworking.post(ApiEndPoint.LiveRoomPoint.ENDPOINT_LOADROOMMEMBERS)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(LoadRoomMemberResponce.class);
    }

    @Override
    public Observable<LoadRoomGuestResponce> doLoadRoomGuestApiCall(LoadRoomGuestRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.LiveRoomPoint.ENDPOINT_LOADROOMGUESTCNT);
        return Rx2AndroidNetworking.post(ApiEndPoint.LiveRoomPoint.ENDPOINT_LOADROOMGUESTCNT)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(LoadRoomGuestResponce.class);
    }

    @Override
    public Observable<GetRoomMrgsResponce> doGetRoomMrgsRequestApiCall(GetRoomMrgsRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.LiveRoomPoint.ENDPOINT_GETROOMMGRS);
        return Rx2AndroidNetworking.post(ApiEndPoint.LiveRoomPoint.ENDPOINT_GETROOMMGRS)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetRoomMrgsResponce.class);
    }

    @Override
    public Observable<GetJackpotDataResponce> doGetJackpotDataApiCall(GetJackpotDataRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.WanFaPoint.ENDPOINT_GETJACKPOTDATA);
        return Rx2AndroidNetworking.post(ApiEndPoint.WanFaPoint.ENDPOINT_GETJACKPOTDATA)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetJackpotDataResponce.class);
    }

    @Override
    public Observable<GetLuckLogsByIdResponce> doGetLuckLogsByIdApiCall(GetLuckLogsByIdRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.WanFaPoint.ENDPOINT_GETLUCKLOGSBYID);
        return Rx2AndroidNetworking.post(ApiEndPoint.WanFaPoint.ENDPOINT_GETLUCKLOGSBYID)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetLuckLogsByIdResponce.class);
    }

    @Override
    public Observable<GetMyLuckLogsByIdResponce> doGetMyLuckLogsByIdApiCall(GetMyLuckLogsByIdRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.WanFaPoint.ENDPOINT_GETMYLUCKLOGS);
        return Rx2AndroidNetworking.post(ApiEndPoint.WanFaPoint.ENDPOINT_GETMYLUCKLOGS)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetMyLuckLogsByIdResponce.class);
    }

    @Override
    public Observable<GetGameStatusResponce> doGetGameStatusApiCall(GetGameStatusRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.WanFaPoint.ENDPOINT_GETGAMESTATUS);
        return Rx2AndroidNetworking.post(ApiEndPoint.WanFaPoint.ENDPOINT_GETGAMESTATUS)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetGameStatusResponce.class);
    }

    @Override
    public Observable<GameSettleResponce> doGameSettleApiCall(GameSettleRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.WanFaPoint.ENDPOINT_GAMESETTLE);
        return Rx2AndroidNetworking.post(ApiEndPoint.WanFaPoint.ENDPOINT_GAMESETTLE)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GameSettleResponce.class);
    }

    @Override
    public Observable<GetGameBetResponce> doGetGameBetApiCall(GetGameBetRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.WanFaPoint.ENDPOINT_GETGAMEBET);
        return Rx2AndroidNetworking.post(ApiEndPoint.WanFaPoint.ENDPOINT_GETGAMEBET)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetGameBetResponce.class);
    }

    @Override
    public Observable<GetGameRoundResultDetailResponce> doGetGameRoundResultDetailApiCall(GetGameRoundResultDetailRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.WanFaPoint.ENDPOINT_GETGAMEROUNDRESULTDETAIL);
        return Rx2AndroidNetworking.post(ApiEndPoint.WanFaPoint.ENDPOINT_GETGAMEROUNDRESULTDETAIL)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetGameRoundResultDetailResponce.class);
    }

    @Override
    public Observable<GamePreemptResponce> doGamePreemptApiCall(GamePreemptRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.WanFaPoint.ENDPOINT_GAMEPREEMPT);
        return Rx2AndroidNetworking.post(ApiEndPoint.WanFaPoint.ENDPOINT_GAMEPREEMPT)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GamePreemptResponce.class);
    }

    @Override
    public Observable<GameBetResponce> doGameBetApiCall(GameBetRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.WanFaPoint.ENDPOINT_GAMEBET);
        return Rx2AndroidNetworking.post(ApiEndPoint.WanFaPoint.ENDPOINT_GAMEBET)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GameBetResponce.class);
    }

    @Override
    public Observable<GetUserGameDataV3Responce> doGetUserGameDataV3ApiCall(GetUserGameDataV3Request request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.WanFaPoint.ENDPOINT_GETUSERGAMEDATAV3);
        return Rx2AndroidNetworking.post(ApiEndPoint.WanFaPoint.ENDPOINT_GETUSERGAMEDATAV3)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetUserGameDataV3Responce.class);
    }

    @Override
    public Observable<GetGameSettleHistoryResponce> doGetGameSettleHistoryApiCall(GetGameSettleHistoryRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.WanFaPoint.ENDPOINT_GETGAMESETTLEHISTORY);
        return Rx2AndroidNetworking.post(ApiEndPoint.WanFaPoint.ENDPOINT_GETGAMESETTLEHISTORY)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetGameSettleHistoryResponce.class);
    }

    @Override
    public Observable<GetGameBetHistoryResponce> doGetGameBetHistoryApiCall(GetGameBetHistoryRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.WanFaPoint.ENDPOINT_GETGAMEBETHISTORY);
        return Rx2AndroidNetworking.post(ApiEndPoint.WanFaPoint.ENDPOINT_GETGAMEBETHISTORY)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetGameBetHistoryResponce.class);
    }

    @Override
    public Observable<GetCurrMountResponce> doGetCurrMountApiCall(GetCurrMountRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.LiveRoomPoint.ENDPOINT_GETCURRMOUNT);
        return Rx2AndroidNetworking.post(ApiEndPoint.LiveRoomPoint.ENDPOINT_GETCURRMOUNT)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetCurrMountResponce.class);
    }

    @Override
    public Observable<BuyGoodsResponce> doBuyGoodsApiCall(BuyGoodsRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignatureV(request, ApiEndPoint.CommonPoint.ENDPOINT_BUYGOODSV);
        return Rx2AndroidNetworking.post(ApiEndPoint.CommonPoint.ENDPOINT_BUYGOODS)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(BuyGoodsResponce.class);
    }

    @Override
    public Observable<SendGiftResponce> doSendGiftApiCall(SendGiftRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignatureV(request, ApiEndPoint.CommonPoint.ENDPOINT_SENDGIFTV);
        return Rx2AndroidNetworking.post(ApiEndPoint.CommonPoint.ENDPOINT_SENDGIFT)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(SendGiftResponce.class);
    }

    @Override
    public Observable<GetGameListResponce> doGetGameListApiCall(GetGameListRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.WanFaPoint.ENDPOINT_GETGAMELIST);
        return Rx2AndroidNetworking.post(ApiEndPoint.WanFaPoint.ENDPOINT_GETGAMELIST)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetGameListResponce.class);
    }

    @Override
    public Observable<LoadOtherRoomsResponce> doLoadOtherRoomsApiCall(LoadOtherRoomsRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.LiveRoomPoint.ENDPOINT_LOADOTHERROOMS);
        return Rx2AndroidNetworking.post(ApiEndPoint.LiveRoomPoint.ENDPOINT_LOADOTHERROOMS)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(LoadOtherRoomsResponce.class);
    }

    @Override
    public Observable<SearchResponse> doSearchApiCall(SearchRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignatureV(request, ApiEndPoint.TopPoint.ENDPOINT_SEARCH_V);
        return Rx2AndroidNetworking.post(ApiEndPoint.TopPoint.ENDPOINT_SEARCH)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(SearchResponse.class);
    }

    @Override
    public Observable<RecommendResponse> doLoadRecommendedListApiCall(RecommendRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.TopPoint.ENDPOINT_LOAD_RECOMMENDED_LIST);
        return Rx2AndroidNetworking.post(ApiEndPoint.TopPoint.ENDPOINT_LOAD_RECOMMENDED_LIST)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(RecommendResponse.class);
    }

    @Override
    public Observable<GetCosSignResponse> doGetCosSignApiCall(GetCosSignRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_GET_COS_SIGN);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_GET_COS_SIGN)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetCosSignResponse.class);
    }

    @Override
    public Observable<LikeFeedResponse> doLikeFeedApiCall(LikeFeedRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_LIKE_FEED);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_LIKE_FEED)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(LikeFeedResponse.class);
    }

    @Override
    public Observable<LoadFeedListResponse> doLoadFeedListApiCall(LoadFeedListRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_LOAD_FEED_LIST);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_LOAD_FEED_LIST)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(LoadFeedListResponse.class);
    }

    @Override
    public Observable<LoadFeedDetailResponse> doLoadFeedDetailApiCall(LoadFeedDetailRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_LOAD_FEED_DETAIL);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_LOAD_FEED_DETAIL)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(LoadFeedDetailResponse.class);
    }

    @Override
    public Observable<LoadCommentListResponse> doLoadCommentListApiCall(LoadCommentListRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_LOAD_COMMENT_LIST);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_LOAD_COMMENT_LIST)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(LoadCommentListResponse.class);
    }

    @Override
    public Observable<DeleteFeedResponse> doDeleteFeedApiCall(DeleteFeedRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_DELETE_FEED);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_DELETE_FEED)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(DeleteFeedResponse.class);
    }

    @Override
    public Observable<PostFeedCommentResponse> doPostFeedCommentApiCall(PostFeedCommentRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_POST_FEED_COMMENT);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_POST_FEED_COMMENT)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(PostFeedCommentResponse.class);
    }

    @Override
    public Observable<ShareFeedResponse> doShareFeedApiCall(ShareFeedRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_SHARE_FEED);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_SHARE_FEED)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(ShareFeedResponse.class);
    }

    @Override
    public Observable<LoadUserHBDataResponse> doLoadUserHBDataApiCall(LoadUserHBDataRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_LOAD_USER_HB_DATA);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_LOAD_USER_HB_DATA)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(LoadUserHBDataResponse.class);
    }


    @Override
    public Observable<LoadMsgListResponce> doLoadMsgListRequestApiCall(LoadMsgListRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.CommonPoint.ENDPOINT_LOADMSGLIST);
        return Rx2AndroidNetworking.post(ApiEndPoint.CommonPoint.ENDPOINT_LOADMSGLIST)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(LoadMsgListResponce.class);
    }

    @Override
    public Observable<GetFriendsResponce> doGetFriendsApiCall(GetFriendsRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.CommonPoint.ENDPOINT_GETFRIENDS);
        return Rx2AndroidNetworking.post(ApiEndPoint.CommonPoint.ENDPOINT_GETFRIENDS)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetFriendsResponce.class);
    }

    @Override
    public Observable<MessageResponce> doMessageApiCall(MessageRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.CommonPoint.ENDPOINT_MESSAGE);
        return Rx2AndroidNetworking.post(ApiEndPoint.CommonPoint.ENDPOINT_MESSAGE)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(MessageResponce.class);
    }

    @Override
    public Observable<ModifyUserPassResponse> doModifyUserPassApiCall(ModifyUserPassRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_MODIFY_USERPASS);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_MODIFY_USERPASS)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(ModifyUserPassResponse.class);
    }

    @Override
    public Observable<FollowsResponse> doGetFollowsApiCall(FollowsRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_GETFOLLOWS);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_GETFOLLOWS)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(FollowsResponse.class);
    }


    @Override
    public Observable<FollowsResponse> doGetFollowersApiCall(FollowsRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_GETFOLLOWERS);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_GETFOLLOWERS)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(FollowsResponse.class);
    }

    @Override
    public Observable<BindPhoneResponse> doBindPhoneApiCall(BindPhoneRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_BINDPHONE);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_BINDPHONE)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(BindPhoneResponse.class);
    }

    @Override
    public Observable<CheckVerifiCodeResponse> doCheckVerifiCodeApiCall(CheckVerifiCodeRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_CHECKVERIFICODE);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_CHECKVERIFICODE)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(CheckVerifiCodeResponse.class);
    }

    @Override
    public Observable<ModifyPhoneResponse> doModifyPhoneApiCall(ModifyPhoneRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_MODIFYPHONE);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_MODIFYPHONE)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(ModifyPhoneResponse.class);
    }

    @Override
    public Observable<ReportResponce> doReportCall(ReportRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.LiveRoomPoint.ENDPOINT_REPORT);
        return Rx2AndroidNetworking.post(ApiEndPoint.LiveRoomPoint.ENDPOINT_REPORT)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(ReportResponce.class);
    }

    @Override
    public Observable<PostFeedResponse> doPostFeedCall(PostFeedRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_POST_FEED);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_POST_FEED)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(PostFeedResponse.class);
    }

    @Override
    public Observable<PayResponse> doPayCall(PayRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_PAY);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_PAY)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(PayResponse.class);
    }

    @Override
    public Observable<GetPayListResponse> doGetPayListCall(GetPayListRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_GETPAYLIST);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_GETPAYLIST)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetPayListResponse.class);
    }

    @Override
    public Observable<EncashResponse> doEncashCall(EncashRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_ENCASH);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_ENCASH)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(EncashResponse.class);
    }

    @Override
    public Observable<GetEncashInfoResponse> doGetEncashInfoCall(GetEncashInfoRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_GET_ENCASHINFO);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_GET_ENCASHINFO)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetEncashInfoResponse.class);
    }

    @Override
    public Observable<BindAlipayResponse> doBindAlipayCall(BindAlipayRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_BINDALIPAY);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_BINDALIPAY)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(BindAlipayResponse.class);
    }

    @Override
    public Observable<GetEncashListResponse> doGetEncashListCall(GetEncashListRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_GETENCASHLIST);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_GETENCASHLIST)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetEncashListResponse.class);
    }

    @Override
    public Observable<GetBlacklistResponse> doGetBlacklistCall(GetBlacklistRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_GETBLACKLIST);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_GETBLACKLIST)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetBlacklistResponse.class);
    }

    @Override
    public Observable<UnblockResponse> doUnblockCall(UnblockRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_UNBLOCK);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_UNBLOCK)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(UnblockResponse.class);
    }

    @Override
    public Observable<GetUserGuardsResponse> doGetUserGuardsCall(GetUserGuardsRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_GET_USERGUARDS);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_GET_USERGUARDS)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetUserGuardsResponse.class);
    }

    @Override
    public Observable<GetUserGuardListResponse> doGetUserGuardListCall(GetUserGuardListRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_GET_USERGUARDLIST);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_GET_USERGUARDLIST)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetUserGuardListResponse.class);
    }

    @Override
    public Observable<SetMountResponse> doSetMountCall(SetMountRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_SETMOUNT);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_SETMOUNT)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(SetMountResponse.class);
    }

    @Override
    public Observable<LoadShopAccountIdListResponse> doLoadShopAccountIdListCall(LoadShopAccountIdListRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_LOAD_SHOPACCOUNTIDLIST);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_LOAD_SHOPACCOUNTIDLIST)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(LoadShopAccountIdListResponse.class);
    }

    @Override
    public Observable<BuyShopAccountIdResponse> doBuyShopAccountIdCall(BuyShopAccountIdRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_BUY_SHOPACCOUNTID);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_BUY_SHOPACCOUNTID)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(BuyShopAccountIdResponse.class);
    }

    @Override
    public Observable<RoomVipResponce> doRoomVipApiCall(RoomVipRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.LiveRoomPoint.ENDPOINT_ROOMVIP);
        return Rx2AndroidNetworking.post(ApiEndPoint.LiveRoomPoint.ENDPOINT_ROOMVIP)
                .addHeaders(protectedApiHeader)
                .setTag("RoomVipRequest")
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(RoomVipResponce.class);
    }

    @Override
    public Observable<CheckAccountIdResponse> doCheckAccountIdCall(CheckAccountIdRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_CHECK_ACCOUNTID);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_CHECK_ACCOUNTID)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(CheckAccountIdResponse.class);
    }

    @Override
    public Observable<CheckGuardInfoResponse> doCheckGuardInfoCall(CheckGuardInfoRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_CHECK_GUARDINFO);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_CHECK_GUARDINFO)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(CheckGuardInfoResponse.class);
    }

    @Override
    public Observable<BuyGuardResponse> doBuyGuardCall(BuyGuardRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_BUYGUARD);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_BUYGUARD)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(BuyGuardResponse.class);
    }

    @Override
    public Observable<LoadBagGiftsResponse> doLoadBagGiftsCall(LoadBagGiftsRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_LOADBAGGIFTS);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_LOADBAGGIFTS)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(LoadBagGiftsResponse.class);
    }

    @Override
    public Observable<GetMountListResponse> doGetMountListCall(GetMountListRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_GET_MOUNTLIST);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_GET_MOUNTLIST)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetMountListResponse.class);
    }

    @Override
    public Observable<GetMVTokenResponse> doGetMVTokenCall(GetMVTokenRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MVPoint.ENDPOINT_GET_MV_TOKEN);
        return Rx2AndroidNetworking.post(ApiEndPoint.MVPoint.ENDPOINT_GET_MV_TOKEN)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetMVTokenResponse.class);
    }

    @Override
    public Observable<PostMvResponse> doPostMvCall(PostMvRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MVPoint.ENDPOINT_POST_MV);
        return Rx2AndroidNetworking.post(ApiEndPoint.MVPoint.ENDPOINT_POST_MV)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(PostMvResponse.class);
    }

    @Override
    public Observable<GetAllMvListResponse> doGetAllMvListCall(GetAllMvListRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MVPoint.ENDPOINT_GET_ALL_MV_LIST);
        return Rx2AndroidNetworking.post(ApiEndPoint.MVPoint.ENDPOINT_GET_ALL_MV_LIST)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetAllMvListResponse.class);
    }

    @Override
    public Observable<PostNeedResponse> doPostNeedCall(PostNeedRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MVPoint.ENDPOINT_POST_NEED);
        return Rx2AndroidNetworking.post(ApiEndPoint.MVPoint.ENDPOINT_POST_NEED)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(PostNeedResponse.class);
    }

    @Override
    public Observable<GetAllNeedListResponse> doGetAllNeedListCall(GetAllNeedListRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MVPoint.ENDPOINT_GET_ALL_NEED_LIST);
        return Rx2AndroidNetworking.post(ApiEndPoint.MVPoint.ENDPOINT_GET_ALL_NEED_LIST)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetAllNeedListResponse.class);
    }

    @Override
    public Observable<GetMyNeedListResponse> doGetMyNeedListCall(GetMyNeedListRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MVPoint.ENDPOINT_GET_MY_NEED_LIST);
        return Rx2AndroidNetworking.post(ApiEndPoint.MVPoint.ENDPOINT_GET_MY_NEED_LIST)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetMyNeedListResponse.class);
    }

    @Override
    public Observable<MakeTradeResponse> doMakeTradeCall(MakeTradeRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_MAKE_TRADE);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_MAKE_TRADE)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(MakeTradeResponse.class);
    }

    @Override
    public Observable<LoadLiangSearchKeyResponse> doLoadLiangSearchKeyCall(LoadLiangSearchKeyRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_LOAD_LIANGSEARCHKEY);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_LOAD_LIANGSEARCHKEY)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(LoadLiangSearchKeyResponse.class);
    }


    @Override
    public Observable<GetNeedSysHBResponse> doGetNeedSysHBCall(GetNeedSysHBRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MVPoint.ENDPOINT_GET_NEED_SYS_HB);
        return Rx2AndroidNetworking.post(ApiEndPoint.MVPoint.ENDPOINT_GET_NEED_SYS_HB)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetNeedSysHBResponse.class);
    }

    @Override
    public Observable<EditNeedResponse> doEditNeedCall(EditNeedRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MVPoint.ENDPOINT_EDIT_NEED);
        return Rx2AndroidNetworking.post(ApiEndPoint.MVPoint.ENDPOINT_EDIT_NEED)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(EditNeedResponse.class);
    }
    @Override
    public Observable<BuyVipResponse> doBuyVipCall(BuyVipRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.VipPoint.ENDPOINT_BUY_VIP);
        return Rx2AndroidNetworking.post(ApiEndPoint.VipPoint.ENDPOINT_BUY_VIP)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(BuyVipResponse.class);
    }

    @Override
    public Observable<GetVipReceiveLogResponse> doGetVipRecvLogCall(GetVipReceiveLogRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.VipPoint.ENDPOINT_GET_VIP_RECEIVE_LOG);
        return Rx2AndroidNetworking.post(ApiEndPoint.VipPoint.ENDPOINT_GET_VIP_RECEIVE_LOG)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetVipReceiveLogResponse.class);
    }

    @Override
    public Observable<GetVipSendLogResponse> doGetVipSendLogCall(GetVipSendLogRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.VipPoint.ENDPOINT_GET_VIP_SEND_LOG);
        return Rx2AndroidNetworking.post(ApiEndPoint.VipPoint.ENDPOINT_GET_VIP_SEND_LOG)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetVipSendLogResponse.class);
    }

    @Override
    public Observable<SendVipResponse> doSendVipCall(SendVipRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.VipPoint.ENDPOINT_SEND_VIP);
        return Rx2AndroidNetworking.post(ApiEndPoint.VipPoint.ENDPOINT_SEND_VIP)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(SendVipResponse.class);
    }

    @Override
    public Observable<GetMsgSwitchStatusResponse> doGetMsgSwitchStatusCall(GetMsgSwitchStatusRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_GET_MSGSWITCH_STATUS);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_GET_MSGSWITCH_STATUS)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetMsgSwitchStatusResponse.class);
    }

    @Override
    public Observable<SetMsgSwitchStatusResponse> doSetMsgSwitchStatusCall(SetMsgSwitchStatusRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_SET_MSGSWITCH_STATUS);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_SET_MSGSWITCH_STATUS)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(SetMsgSwitchStatusResponse.class);
    }
    @Override
    public Observable<CollectNeedResponse> doCollectNeedCall(CollectNeedRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MVPoint.ENDPOINT_COLLECT_NEED);
        return Rx2AndroidNetworking.post(ApiEndPoint.MVPoint.ENDPOINT_COLLECT_NEED)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(CollectNeedResponse.class);
    }

    @Override
    public Observable<UnCollectNeedResponse> doUnCollectNeedCall(UnCollectNeedRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MVPoint.ENDPOINT_UN_COLLECT_NEED);
        return Rx2AndroidNetworking.post(ApiEndPoint.MVPoint.ENDPOINT_UN_COLLECT_NEED)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(UnCollectNeedResponse.class);
    }

    @Override
    public Observable<GetMyNeedMVListResponse> doGetMyNeedMVListCall(GetMyNeedMVListRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MVPoint.ENDPOINT_GET_MY_NEED_MV_LIST);
        return Rx2AndroidNetworking.post(ApiEndPoint.MVPoint.ENDPOINT_GET_MY_NEED_MV_LIST)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetMyNeedMVListResponse.class);
    }

    @Override
    public Observable<BuyMVResponse> doBuyMVCall(BuyMVRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MVPoint.ENDPOINT_BUY_MV);
        return Rx2AndroidNetworking.post(ApiEndPoint.MVPoint.ENDPOINT_BUY_MV)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(BuyMVResponse.class);
    }

    @Override
    public Observable<FeedbackMVResponse> doFeedbackMVCall(FeedbackMVRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MVPoint.ENDPOINT_FEEDBACK_MV);
        return Rx2AndroidNetworking.post(ApiEndPoint.MVPoint.ENDPOINT_FEEDBACK_MV)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(FeedbackMVResponse.class);
    }

    @Override
    public Observable<GetMVDetailResponse> doGetMVDetailCall(GetMVDetailRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MVPoint.ENDPOINT_GET_MV_DETAIL);
        return Rx2AndroidNetworking.post(ApiEndPoint.MVPoint.ENDPOINT_GET_MV_DETAIL)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetMVDetailResponse.class);
    }

    @Override
    public Observable<GetMVCommentListResponse> doGetMVCommentListCall(GetMVCommentListRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MVPoint.ENDPOINT_GET_MV_COMMENT_LIST);
        return Rx2AndroidNetworking.post(ApiEndPoint.MVPoint.ENDPOINT_GET_MV_COMMENT_LIST)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetMVCommentListResponse.class);
    }


    @Override
    public Observable<LikeMVResponse> doGetLikeMVCall(LikeMVRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MVPoint.ENDPOINT_LIKE_MV);
        return Rx2AndroidNetworking.post(ApiEndPoint.MVPoint.ENDPOINT_LIKE_MV)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(LikeMVResponse.class);
    }

    @Override
    public Observable<LikeCommentResponse> doLikeCommentCall(LikeCommentRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MVPoint.ENDPOINT_LIKE_COMMENT);
        return Rx2AndroidNetworking.post(ApiEndPoint.MVPoint.ENDPOINT_LIKE_COMMENT)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(LikeCommentResponse.class);
    }

    @Override
    public Observable<GiftMVResponse> doGiftMVCall(GiftMVRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MVPoint.ENDPOINT_GIFT_MV);
        return Rx2AndroidNetworking.post(ApiEndPoint.MVPoint.ENDPOINT_GIFT_MV)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GiftMVResponse.class);
    }

    @Override
    public Observable<PostMVCommentResponse> doPostMVCommentCall(PostMVCommentRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MVPoint.ENDPOINT_POST_MV_COMMENT);
        return Rx2AndroidNetworking.post(ApiEndPoint.MVPoint.ENDPOINT_POST_MV_COMMENT)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(PostMVCommentResponse.class);
    }

    @Override
    public Observable<BlockResponse> doBlockCall(BlockRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_BLOCK);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_BLOCK)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(BlockResponse.class);
    }

    @Override
    public Observable<IsFeedExistResponse> doIsFeedExistCall(IsFeedExistRequset request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.CommonPoint.ENDPOINT_IS_FEEDEXIST);
        return Rx2AndroidNetworking.post(ApiEndPoint.CommonPoint.ENDPOINT_IS_FEEDEXIST)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(IsFeedExistResponse.class);
    }

    @Override
    public Observable<AcceptTradeResponse> doAcceptTradeCall(AcceptTradeRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_ACCEPT_TRADE);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_ACCEPT_TRADE)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(AcceptTradeResponse.class);
    }

    @Override
    public Observable<PlayMVResponse> doPlayMVCall(PlayMVRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MVPoint.ENDPOINT_PLAY_MV);
        return Rx2AndroidNetworking.post(ApiEndPoint.MVPoint.ENDPOINT_PLAY_MV)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(PlayMVResponse.class);
    }

    @Override
    public Observable<GetMVGiftLogResponse> doGetMVGiftLogCall(GetMVGiftLogRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MVPoint.ENDPOINT_GET_MV_GIFT_LOG);
        return Rx2AndroidNetworking.post(ApiEndPoint.MVPoint.ENDPOINT_GET_MV_GIFT_LOG)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetMVGiftLogResponse.class);
    }

    @Override
    public Observable<GetNeedCollectListResponse> doGetNeedCollectCall(GetNeedCollectListRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MVPoint.ENDPOINT_GET_NEED_COLLECT_LIST);
        return Rx2AndroidNetworking.post(ApiEndPoint.MVPoint.ENDPOINT_GET_NEED_COLLECT_LIST)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetNeedCollectListResponse.class);
    }


    @Override
    public Observable<GetMyMVListResponse> doGetMyMVListCall(GetMyMVListRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MVPoint.ENDPOINT_GET_MY_MV_LIST);
        return Rx2AndroidNetworking.post(ApiEndPoint.MVPoint.ENDPOINT_GET_MY_MV_LIST)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetMyMVListResponse.class);
    }

    @Override
    public Observable<TestStartLiveResponce> doGetTestStartLiveApiCall(TestStartLiveRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.LiveRoomPoint.ENDPOINT_TESTSTARTLIVE);
        return Rx2AndroidNetworking.post(ApiEndPoint.LiveRoomPoint.ENDPOINT_TESTSTARTLIVE)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(TestStartLiveResponce.class);
    }

    @Override
    public Observable<GetMyCustomMadeMVListResponse> doGetMyCustomMadeMVListCall(GetMyCustomMadeMVListRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MVPoint.ENDPOINT_GET_MY_CUSTOM_MADE_MV_LIST);
        return Rx2AndroidNetworking.post(ApiEndPoint.MVPoint.ENDPOINT_GET_MY_CUSTOM_MADE_MV_LIST)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetMyCustomMadeMVListResponse.class);
    }


    @Override
    public Observable<EnablePostMVResponse> doEnablePostMVCall(EnablePostMVRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MVPoint.ENDPOINT_ENABLE_POST_MV);
        return Rx2AndroidNetworking.post(ApiEndPoint.MVPoint.ENDPOINT_ENABLE_POST_MV)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(EnablePostMVResponse.class);
    }


    @Override
    public Observable<LoadFeedListResponse> doLoadAllFeedListCall(LoadAllFeedListRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MePoint.ENDPOINT_LOAD_ALL_FEED_LIST);
        return Rx2AndroidNetworking.post(ApiEndPoint.MePoint.ENDPOINT_LOAD_ALL_FEED_LIST)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(LoadFeedListResponse.class);
    }


    @Override
    public Observable<GetOtherMVListResponse> doGetOtherMVListCall(GetOtherMVListRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MVPoint.ENDPOINT_GET_OTHER_MV_LIST);
        return Rx2AndroidNetworking.post(ApiEndPoint.MVPoint.ENDPOINT_GET_OTHER_MV_LIST)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetOtherMVListResponse.class);
    }

    @Override
    public Observable<GetExtGameIconResponce> doGetExtGameIconCall(GetExtGameIconRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.WanFaPoint.ENDPOINT_GETGAMEICON);
        return Rx2AndroidNetworking.post(ApiEndPoint.WanFaPoint.ENDPOINT_GETGAMEICON)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(GetExtGameIconResponce.class);
    }

    @Override
    public Observable<ShareMvResponse> doShareMvCall(ShareMvRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MVPoint.ENDPOINT_SHARE_MV);
        return Rx2AndroidNetworking.post(ApiEndPoint.MVPoint.ENDPOINT_SHARE_MV)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(ShareMvResponse.class);
    }

    @Override
    public Observable<DeleteMVResponse> doDeleteMVCall(DeleteMVRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MVPoint.ENDPOINT_DELETE_MV);
        return Rx2AndroidNetworking.post(ApiEndPoint.MVPoint.ENDPOINT_DELETE_MV)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(DeleteMVResponse.class);
    }

    @Override
    public Observable<DeleteNeedResponse> doDeleteNeedCall(DeleteNeedRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MVPoint.ENDPOINT_DELETE_NEED);
        return Rx2AndroidNetworking.post(ApiEndPoint.MVPoint.ENDPOINT_DELETE_NEED)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(DeleteNeedResponse.class);
    }

    @Override
    public Observable<QueryMVNoticeResponse> doQueryMVNoticeCall(QueryMVNoticeRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MVPoint.ENDPOINT_QUERY_MV_NOTICE);
        return Rx2AndroidNetworking.post(ApiEndPoint.MVPoint.ENDPOINT_QUERY_MV_NOTICE)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(QueryMVNoticeResponse.class);
    }

    @Override
    public Observable<CancelMVNoticeResponse> doCancelMVNoticeCall(CancelMVNoticeRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MVPoint.ENDPOINT_CANCEL_MV_NOTICE);
        return Rx2AndroidNetworking.post(ApiEndPoint.MVPoint.ENDPOINT_CANCEL_MV_NOTICE)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(CancelMVNoticeResponse.class);
    }

    @Override
    public Observable<DownLoadMvResponse> doDownLoadMvCall(DownLoadMvRequest request) {
        ApiHeader.ProtectedApiHeader protectedApiHeader = mApiHeader.getProtectedApiHeader();
        protectedApiHeader.setRequestSignature(request, ApiEndPoint.MVPoint.ENDPOINT_DOWN_LOAD_MV);
        return Rx2AndroidNetworking.post(ApiEndPoint.MVPoint.ENDPOINT_DOWN_LOAD_MV)
                .addHeaders(protectedApiHeader)
                .addBodyParameter(protectedApiHeader.getRequeestParams(request))
                .build()
                .getObjectObservable(DownLoadMvResponse.class);
    }




}


