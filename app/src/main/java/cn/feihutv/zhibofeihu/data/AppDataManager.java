package cn.feihutv.zhibofeihu.data;


import android.content.Context;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import cn.feihutv.zhibofeihu.data.db.DbHelper;
import cn.feihutv.zhibofeihu.data.db.model.HistoryRecordBean;
import cn.feihutv.zhibofeihu.data.db.model.MessageEntity;
import cn.feihutv.zhibofeihu.data.db.model.MvDownLog;
import cn.feihutv.zhibofeihu.data.db.model.NoticeSnsEntity;
import cn.feihutv.zhibofeihu.data.db.model.NoticeSysEntity;
import cn.feihutv.zhibofeihu.data.db.model.RecentItem;
import cn.feihutv.zhibofeihu.data.db.model.SearchHistoryInfo;
import cn.feihutv.zhibofeihu.data.db.model.SysConfigBean;
import cn.feihutv.zhibofeihu.data.db.model.SysFontColorBean;
import cn.feihutv.zhibofeihu.data.db.model.SysGameBetBean;
import cn.feihutv.zhibofeihu.data.db.model.SysGiftNewBean;
import cn.feihutv.zhibofeihu.data.db.model.SysGoodsNewBean;
import cn.feihutv.zhibofeihu.data.db.model.SysGuardGoodsBean;
import cn.feihutv.zhibofeihu.data.db.model.SysHBBean;
import cn.feihutv.zhibofeihu.data.db.model.SysItemBean;
import cn.feihutv.zhibofeihu.data.db.model.SysLaunchTagBean;
import cn.feihutv.zhibofeihu.data.db.model.SysLevelBean;
import cn.feihutv.zhibofeihu.data.db.model.SysMountNewBean;
import cn.feihutv.zhibofeihu.data.db.model.SysSignAwardBean;
import cn.feihutv.zhibofeihu.data.db.model.SysTipsBean;
import cn.feihutv.zhibofeihu.data.db.model.SysVipBean;
import cn.feihutv.zhibofeihu.data.db.model.SysVipGoodsBean;
import cn.feihutv.zhibofeihu.data.local.model.SysbagBean;
import cn.feihutv.zhibofeihu.data.network.http.ApiHeader;
import cn.feihutv.zhibofeihu.data.network.http.ApiHelper;
import cn.feihutv.zhibofeihu.data.network.http.model.bangdan.GetGuardRankRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.bangdan.GetGuardRankResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.bangdan.LoadContriRankListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.bangdan.LoadContriRankListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.bangdan.LoadLuckRankListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.bangdan.LoadLuckRecordListRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.bangdan.LoadLuckRecordListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.common.BuyGoodsRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.BuyGoodsResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.AwardsBean;
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
import cn.feihutv.zhibofeihu.data.network.socket.SocketApiHeader;
import cn.feihutv.zhibofeihu.data.network.socket.SocketApiHelper;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.SocketBaseResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.common.SendRoomMsgRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.common.SendRoomResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.common.SocketConnectError;
import cn.feihutv.zhibofeihu.data.network.socket.model.common.VerifySocketTokenRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.common.VerifySocketTokenResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.CloseLiveRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.CloseLiveResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.GetLiveStatusRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.GetLiveStatusResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.JoinRoomRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.JoinRoomResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.LeaveRoomRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.LeaveRoomResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.LoadOtherUserInfoRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.LoadOtherUserInfoResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.StartLiveRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.StartLiveResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.me.ModifyProfileLiveCoverRequest;
import cn.feihutv.zhibofeihu.data.network.socket.model.me.ModifyProfileLiveCoverResponse;
import cn.feihutv.zhibofeihu.data.prefs.PreferencesHelper;
import cn.feihutv.zhibofeihu.di.ApplicationContext;
import cn.feihutv.zhibofeihu.rxbus.RxBus;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import io.reactivex.Observable;

/**
 * <pre>
 *     @author : sichu.chen
 *     time   : 2017/09/25
 *     desc   : app 数据访问管理总接口实现,项目数据层访问都通过该管理实现
 *     version: 1.0
 * </pre>
 */

@Singleton
public class AppDataManager implements DataManager {


    private final Context mContext;
    private final DbHelper mDbHelper;
    private final PreferencesHelper mPreferencesHelper;
    private final ApiHelper mApiHelper;
    private final SocketApiHelper mSocketApiHelper;

    @Inject
    public AppDataManager(@ApplicationContext Context context,
                          DbHelper dbHelper,
                          PreferencesHelper preferencesHelper,
                          ApiHelper apiHelper,
                          SocketApiHelper socketApiHelper) {
        mContext = context;
        mDbHelper = dbHelper;
        mPreferencesHelper = preferencesHelper;
        mApiHelper = apiHelper;
        mSocketApiHelper = socketApiHelper;
    }


    @Override
    public ApiHeader getApiHeader() {
        return mApiHelper.getApiHeader();
    }

    @Override
    public Observable<WebHttpResponse> doWebHttpApiCall(WebHttpRequest request) {
        return mApiHelper.doWebHttpApiCall(request);
    }

    //----------------------------------分割线--->以下部分为http 网络操作------------------------------------------------------

    @Override
    public Observable<LoginResponse> doServerLoginApiCall(LoginRequest.ServerLoginRequest request) {
        return mApiHelper.doServerLoginApiCall(request);
    }

    @Override
    public Observable<LoginResponse> doPlatformLoginApiCall(LoginRequest.PlatformLoginRequest request) {
        return mApiHelper.doPlatformLoginApiCall(request);
    }

    @Override
    public Observable<LoginResponse> doGuestLoginApiCall(LoginRequest.GuestLoginRequest request) {
        return mApiHelper.doGuestLoginApiCall(request);
    }

    @Override
    public Observable<CheckUpdateResponse> doCheckUpdateApiCall(CheckUpdateRequest request) {
        return mApiHelper.doCheckUpdateApiCall(request);
    }

    @Override
    public Observable<GetSocketTokenResponse> doGetSocketTokenApiCall(GetSocketTokenRequest request) {
        return mApiHelper.doGetSocketTokenApiCall(request);
    }

    @Override
    public Observable<BagResponse> doGetUserBagDataApiCall(BagRequest request) {
        return mApiHelper.doGetUserBagDataApiCall(request);
    }

    @Override
    public Observable<CertifiResponse> doGetUseCertifiDataApiCall(CertifiResquest request) {
        return mApiHelper.doGetUseCertifiDataApiCall(request);
    }

    @Override
    public Observable<LoadRoomResponce> doGetRoomDataApiCall(LoadRoomRequest request) {
        return mApiHelper.doGetRoomDataApiCall(request);
    }

    @Override
    public Observable<LoadSignResponce> doGetSignDataApiCall(LoadSignRequest request) {
        return mApiHelper.doGetSignDataApiCall(request);
    }

    @Override
    public Observable<SignResponse> doSignApiCall(SignRequest request) {
        return mApiHelper.doSignApiCall(request);
    }

    @Override
    public Observable<LogDeviceResponse> doLogDeviceApiCall(LogDeviceRequest request) {
        return mApiHelper.doLogDeviceApiCall(request);
    }

    @Override
    public Observable<LoadRoomListResponse> doLoadRoomListByTagApiCall(LoadRoomListRequest request) {
        return mApiHelper.doLoadRoomListByTagApiCall(request);
    }

    @Override
    public Observable<LoadRoomListResponse> doLoadCareRecommendedListApiCall(LoadRoomListRequest request) {
        return mApiHelper.doLoadCareRecommendedListApiCall(request);
    }

    @Override
    public Observable<LoadContriRankListResponse> doLoadContriRankListApiCall(LoadContriRankListRequest request) {
        return mApiHelper.doLoadContriRankListApiCall(request);
    }

    @Override
    public Observable<LoadContriRankListResponse> doLoadIncomeRankListApiCall(LoadContriRankListRequest request) {
        return mApiHelper.doLoadIncomeRankListApiCall(request);
    }

    @Override
    public Observable<LoadLuckRecordListResponse> doLoadLuckRecordListApiCall(LoadLuckRecordListRequest request) {
        return mApiHelper.doLoadLuckRecordListApiCall(request);
    }

    @Override
    public Observable<GetGuardRankResponse> doGetGuardRankApiCall(GetGuardRankRequest request) {
        return mApiHelper.doGetGuardRankApiCall(request);
    }

    @Override
    public Observable<LoadLuckRankListResponse> doLoadLuckRankListApiCall(LoadLuckRecordListRequest request) {
        return mApiHelper.doLoadLuckRankListApiCall(request);
    }


    @Override
    public Observable<GetVerifyCodeResponse> doGetVerifyCodeApiCall(GetVerifyCodeRequest request) {
        return mApiHelper.doGetVerifyCodeApiCall(request);
    }

    @Override
    public Observable<RestPsdResponse> doRestPsdApiCall(RestPsdRequest request) {
        return mApiHelper.doRestPsdApiCall(request);
    }

    @Override
    public Observable<CreateUserResponse> doCreateUserApiCall(CreateUserRequest request) {
        return mApiHelper.doCreateUserApiCall(request);
    }

    @Override
    public Observable<GetBannerResponse> doGetBannerByTypeApiCall(GetBannerRequest request) {
        return mApiHelper.doGetBannerByTypeApiCall(request);
    }

    @Override
    public Observable<LocationResponce> doGetLocationApiCall(LocationRequest request) {
        return mApiHelper.doGetLocationApiCall(request);
    }

    @Override
    public Observable<SwitchAppFocusResponce> doSwitchAppFocusApiCall(SwitchAppFocusRequest request) {
        return mApiHelper.doSwitchAppFocusApiCall(request);
    }

    @Override
    public Observable<LogShareResponce> doLogShareApiCall(LogShareRequest request) {
        return mApiHelper.doLogShareApiCall(request);
    }

    @Override
    public Observable<FollowResponce> doFollowApiCall(FollowRequest request) {
        return mApiHelper.doFollowApiCall(request);
    }

    @Override
    public Observable<UnFollowResponce> doUnFollowApiCall(UnFollowRequest request) {
        return mApiHelper.doUnFollowApiCall(request);
    }

    @Override
    public Observable<SetRoomMgrResponce> doSetRoomMgrApiCall(SetRoomMgrRequest request) {
        return mApiHelper.doSetRoomMgrApiCall(request);
    }

    @Override
    public Observable<CancelRoomMgrResponce> doCancelRoomMgrCall(CancelRoomMgrRequest request) {
        return mApiHelper.doCancelRoomMgrCall(request);
    }

    @Override
    public Observable<BanResponce> doBanApiCall(BanRequest request) {
        return mApiHelper.doBanApiCall(request);
    }

    @Override
    public Observable<LoadRoomContriResponce> doLoadRoomContriApiCall(LoadRoomContriRequest request) {
        return mApiHelper.doLoadRoomContriApiCall(request);
    }

    @Override
    public Observable<JackpotCountDownResponce> doJackpotCountDownRequestApiCall(JackpotCountDownRequest request) {
        return mApiHelper.doJackpotCountDownRequestApiCall(request);
    }

    @Override
    public Observable<SendLoudSpeakResponce> doSendLoudSpeakApiCall(SendLoudSpeakRequest request) {
        return mApiHelper.doSendLoudSpeakApiCall(request);
    }

    @Override
    public Observable<LoadRoomMemberResponce> doLoadRoomMembersApiCall(LoadRoomMembersRequest request) {
        return mApiHelper.doLoadRoomMembersApiCall(request);
    }

    @Override
    public Observable<LoadRoomGuestResponce> doLoadRoomGuestApiCall(LoadRoomGuestRequest request) {
        return mApiHelper.doLoadRoomGuestApiCall(request);
    }

    @Override
    public Observable<GetRoomMrgsResponce> doGetRoomMrgsRequestApiCall(GetRoomMrgsRequest request) {
        return mApiHelper.doGetRoomMrgsRequestApiCall(request);
    }

    @Override
    public Observable<GetJackpotDataResponce> doGetJackpotDataApiCall(GetJackpotDataRequest request) {
        return mApiHelper.doGetJackpotDataApiCall(request);
    }

    @Override
    public Observable<GetLuckLogsByIdResponce> doGetLuckLogsByIdApiCall(GetLuckLogsByIdRequest request) {
        return mApiHelper.doGetLuckLogsByIdApiCall(request);
    }

    @Override
    public Observable<GetMyLuckLogsByIdResponce> doGetMyLuckLogsByIdApiCall(GetMyLuckLogsByIdRequest request) {
        return mApiHelper.doGetMyLuckLogsByIdApiCall(request);
    }

    @Override
    public Observable<GetGameStatusResponce> doGetGameStatusApiCall(GetGameStatusRequest request) {
        return mApiHelper.doGetGameStatusApiCall(request);
    }

    @Override
    public Observable<GameSettleResponce> doGameSettleApiCall(GameSettleRequest request) {
        return mApiHelper.doGameSettleApiCall(request);
    }

    @Override
    public Observable<GetGameBetResponce> doGetGameBetApiCall(GetGameBetRequest request) {
        return mApiHelper.doGetGameBetApiCall(request);
    }

    @Override
    public Observable<GetGameRoundResultDetailResponce> doGetGameRoundResultDetailApiCall(GetGameRoundResultDetailRequest request) {
        return mApiHelper.doGetGameRoundResultDetailApiCall(request);
    }

    @Override
    public Observable<GamePreemptResponce> doGamePreemptApiCall(GamePreemptRequest request) {
        return mApiHelper.doGamePreemptApiCall(request);
    }

    @Override
    public Observable<GameBetResponce> doGameBetApiCall(GameBetRequest request) {
        return mApiHelper.doGameBetApiCall(request);
    }

    @Override
    public Observable<GetUserGameDataV3Responce> doGetUserGameDataV3ApiCall(GetUserGameDataV3Request request) {
        return mApiHelper.doGetUserGameDataV3ApiCall(request);
    }

    @Override
    public Observable<GetGameSettleHistoryResponce> doGetGameSettleHistoryApiCall(GetGameSettleHistoryRequest request) {
        return mApiHelper.doGetGameSettleHistoryApiCall(request);
    }

    @Override
    public Observable<GetGameBetHistoryResponce> doGetGameBetHistoryApiCall(GetGameBetHistoryRequest request) {
        return mApiHelper.doGetGameBetHistoryApiCall(request);
    }

    @Override

    public Observable<GetCurrMountResponce> doGetCurrMountApiCall(GetCurrMountRequest request) {
        return mApiHelper.doGetCurrMountApiCall(request);
    }

    @Override
    public Observable<BuyGoodsResponce> doBuyGoodsApiCall(BuyGoodsRequest request) {
        return mApiHelper.doBuyGoodsApiCall(request);
    }

    @Override
    public Observable<SendGiftResponce> doSendGiftApiCall(SendGiftRequest request) {
        return mApiHelper.doSendGiftApiCall(request);
    }

    @Override
    public Observable<GetGameListResponce> doGetGameListApiCall(GetGameListRequest request) {
        return mApiHelper.doGetGameListApiCall(request);
    }

    @Override
    public Observable<LoadOtherRoomsResponce> doLoadOtherRoomsApiCall(LoadOtherRoomsRequest request) {
        return mApiHelper.doLoadOtherRoomsApiCall(request);

    }

    @Override
    public Observable<SearchResponse> doSearchApiCall(SearchRequest request) {
        return mApiHelper.doSearchApiCall(request);
    }

    @Override
    public Observable<RecommendResponse> doLoadRecommendedListApiCall(RecommendRequest request) {
        return mApiHelper.doLoadRecommendedListApiCall(request);

    }

    @Override
    public Observable<GetCosSignResponse> doGetCosSignApiCall(GetCosSignRequest request) {
        return mApiHelper.doGetCosSignApiCall(request);
    }

    @Override
    public Observable<LikeFeedResponse> doLikeFeedApiCall(LikeFeedRequest request) {
        return mApiHelper.doLikeFeedApiCall(request);
    }

    @Override
    public Observable<LoadFeedListResponse> doLoadFeedListApiCall(LoadFeedListRequest request) {
        return mApiHelper.doLoadFeedListApiCall(request);
    }

    @Override
    public Observable<LoadFeedDetailResponse> doLoadFeedDetailApiCall(LoadFeedDetailRequest request) {
        return mApiHelper.doLoadFeedDetailApiCall(request);
    }

    @Override
    public Observable<LoadCommentListResponse> doLoadCommentListApiCall(LoadCommentListRequest request) {
        return mApiHelper.doLoadCommentListApiCall(request);
    }

    @Override
    public Observable<DeleteFeedResponse> doDeleteFeedApiCall(DeleteFeedRequest request) {
        return mApiHelper.doDeleteFeedApiCall(request);
    }

    @Override
    public Observable<PostFeedCommentResponse> doPostFeedCommentApiCall(PostFeedCommentRequest request) {
        return mApiHelper.doPostFeedCommentApiCall(request);
    }

    @Override
    public Observable<ShareFeedResponse> doShareFeedApiCall(ShareFeedRequest request) {
        return mApiHelper.doShareFeedApiCall(request);
    }

    @Override
    public Observable<LoadUserHBDataResponse> doLoadUserHBDataApiCall(LoadUserHBDataRequest request) {
        return mApiHelper.doLoadUserHBDataApiCall(request);
    }

    @Override
    public Observable<LoadMsgListResponce> doLoadMsgListRequestApiCall(LoadMsgListRequest request) {
        return mApiHelper.doLoadMsgListRequestApiCall(request);
    }

    @Override
    public Observable<GetFriendsResponce> doGetFriendsApiCall(GetFriendsRequest request) {
        return mApiHelper.doGetFriendsApiCall(request);
    }

    @Override
    public Observable<MessageResponce> doMessageApiCall(MessageRequest request) {
        return mApiHelper.doMessageApiCall(request);
    }

    @Override
    public Observable<ModifyUserPassResponse> doModifyUserPassApiCall(ModifyUserPassRequest request) {
        return mApiHelper.doModifyUserPassApiCall(request);
    }

    @Override
    public Observable<ReportResponce> doReportCall(ReportRequest request) {
        return mApiHelper.doReportCall(request);
    }

    @Override
    public Observable<PostFeedResponse> doPostFeedCall(PostFeedRequest request) {
        return mApiHelper.doPostFeedCall(request);
    }

    @Override
    public Observable<PayResponse> doPayCall(PayRequest request) {
        return mApiHelper.doPayCall(request);
    }

    @Override
    public Observable<GetPayListResponse> doGetPayListCall(GetPayListRequest request) {
        return mApiHelper.doGetPayListCall(request);
    }

    @Override
    public Observable<EncashResponse> doEncashCall(EncashRequest request) {
        return mApiHelper.doEncashCall(request);
    }

    @Override
    public Observable<GetEncashInfoResponse> doGetEncashInfoCall(GetEncashInfoRequest request) {
        return mApiHelper.doGetEncashInfoCall(request);
    }

    @Override
    public Observable<BindAlipayResponse> doBindAlipayCall(BindAlipayRequest request) {
        return mApiHelper.doBindAlipayCall(request);
    }

    @Override
    public Observable<GetEncashListResponse> doGetEncashListCall(GetEncashListRequest request) {
        return mApiHelper.doGetEncashListCall(request);
    }

    @Override
    public Observable<GetBlacklistResponse> doGetBlacklistCall(GetBlacklistRequest request) {
        return mApiHelper.doGetBlacklistCall(request);
    }

    @Override
    public Observable<UnblockResponse> doUnblockCall(UnblockRequest request) {
        return mApiHelper.doUnblockCall(request);
    }

    @Override
    public Observable<GetUserGuardsResponse> doGetUserGuardsCall(GetUserGuardsRequest request) {
        return mApiHelper.doGetUserGuardsCall(request);
    }

    @Override
    public Observable<GetUserGuardListResponse> doGetUserGuardListCall(GetUserGuardListRequest request) {
        return mApiHelper.doGetUserGuardListCall(request);
    }

    @Override
    public Observable<SetMountResponse> doSetMountCall(SetMountRequest request) {
        return mApiHelper.doSetMountCall(request);
    }

    @Override
    public Observable<LoadShopAccountIdListResponse> doLoadShopAccountIdListCall(LoadShopAccountIdListRequest request) {
        return mApiHelper.doLoadShopAccountIdListCall(request);
    }

    @Override
    public Observable<BuyShopAccountIdResponse> doBuyShopAccountIdCall(BuyShopAccountIdRequest request) {
        return mApiHelper.doBuyShopAccountIdCall(request);
    }

    @Override
    public Observable<RoomVipResponce> doRoomVipApiCall(RoomVipRequest request) {
        return mApiHelper.doRoomVipApiCall(request);
    }

    @Override
    public Observable<CheckAccountIdResponse> doCheckAccountIdCall(CheckAccountIdRequest request) {
        return mApiHelper.doCheckAccountIdCall(request);
    }

    @Override
    public Observable<CheckGuardInfoResponse> doCheckGuardInfoCall(CheckGuardInfoRequest request) {
        return mApiHelper.doCheckGuardInfoCall(request);
    }

    @Override
    public Observable<BuyGuardResponse> doBuyGuardCall(BuyGuardRequest request) {
        return mApiHelper.doBuyGuardCall(request);
    }

    @Override
    public Observable<LoadBagGiftsResponse> doLoadBagGiftsCall(LoadBagGiftsRequest request) {
        return mApiHelper.doLoadBagGiftsCall(request);
    }

    @Override
    public Observable<GetMountListResponse> doGetMountListCall(GetMountListRequest request) {
        return mApiHelper.doGetMountListCall(request);
    }

    @Override
    public Observable<GetMVTokenResponse> doGetMVTokenCall(GetMVTokenRequest request) {
        return mApiHelper.doGetMVTokenCall(request);
    }

    @Override
    public Observable<PostMvResponse> doPostMvCall(PostMvRequest request) {
        return mApiHelper.doPostMvCall(request);
    }

    @Override
    public Observable<GetAllMvListResponse> doGetAllMvListCall(GetAllMvListRequest request) {
        return mApiHelper.doGetAllMvListCall(request);
    }

    @Override
    public Observable<PostNeedResponse> doPostNeedCall(PostNeedRequest request) {
        return mApiHelper.doPostNeedCall(request);
    }

    @Override
    public Observable<GetAllNeedListResponse> doGetAllNeedListCall(GetAllNeedListRequest request) {
        return mApiHelper.doGetAllNeedListCall(request);
    }

    @Override
    public Observable<GetMyNeedListResponse> doGetMyNeedListCall(GetMyNeedListRequest request) {
        return mApiHelper.doGetMyNeedListCall(request);
    }

    @Override
    public Observable<MakeTradeResponse> doMakeTradeCall(MakeTradeRequest request) {
        return mApiHelper.doMakeTradeCall(request);
    }

    @Override
    public Observable<LoadLiangSearchKeyResponse> doLoadLiangSearchKeyCall(LoadLiangSearchKeyRequest request) {
        return mApiHelper.doLoadLiangSearchKeyCall(request);
    }

    @Override
    public Observable<GetNeedSysHBResponse> doGetNeedSysHBCall(GetNeedSysHBRequest request) {
        return mApiHelper.doGetNeedSysHBCall(request);
    }

    @Override
    public Observable<EditNeedResponse> doEditNeedCall(EditNeedRequest request) {
        return mApiHelper.doEditNeedCall(request);
    }

    @Override
    public Observable<BuyVipResponse> doBuyVipCall(BuyVipRequest request) {
        return mApiHelper.doBuyVipCall(request);
    }

    @Override
    public Observable<GetVipReceiveLogResponse> doGetVipRecvLogCall(GetVipReceiveLogRequest request) {
        return mApiHelper.doGetVipRecvLogCall(request);
    }

    @Override
    public Observable<GetVipSendLogResponse> doGetVipSendLogCall(GetVipSendLogRequest request) {
        return mApiHelper.doGetVipSendLogCall(request);
    }

    @Override
    public Observable<SendVipResponse> doSendVipCall(SendVipRequest request) {
        return mApiHelper.doSendVipCall(request);
    }

    @Override
    public Observable<GetMsgSwitchStatusResponse> doGetMsgSwitchStatusCall(GetMsgSwitchStatusRequest request) {
        return mApiHelper.doGetMsgSwitchStatusCall(request);
    }

    @Override
    public Observable<SetMsgSwitchStatusResponse> doSetMsgSwitchStatusCall(SetMsgSwitchStatusRequest request) {
        return mApiHelper.doSetMsgSwitchStatusCall(request);
    }

    @Override
    public Observable<CollectNeedResponse> doCollectNeedCall(CollectNeedRequest request) {
        return mApiHelper.doCollectNeedCall(request);
    }

    @Override
    public Observable<UnCollectNeedResponse> doUnCollectNeedCall(UnCollectNeedRequest request) {
        return mApiHelper.doUnCollectNeedCall(request);
    }

    @Override
    public Observable<GetMyNeedMVListResponse> doGetMyNeedMVListCall(GetMyNeedMVListRequest request) {
        return mApiHelper.doGetMyNeedMVListCall(request);
    }

    @Override
    public Observable<BuyMVResponse> doBuyMVCall(BuyMVRequest request) {
        return mApiHelper.doBuyMVCall(request);
    }

    @Override
    public Observable<FeedbackMVResponse> doFeedbackMVCall(FeedbackMVRequest request) {
        return mApiHelper.doFeedbackMVCall(request);
    }

    @Override
    public Observable<GetMVDetailResponse> doGetMVDetailCall(GetMVDetailRequest request) {
        return mApiHelper.doGetMVDetailCall(request);
    }

    @Override
    public Observable<GetMVCommentListResponse> doGetMVCommentListCall(GetMVCommentListRequest request) {
        return mApiHelper.doGetMVCommentListCall(request);
    }

    @Override
    public Observable<LikeMVResponse> doGetLikeMVCall(LikeMVRequest request) {
        return mApiHelper.doGetLikeMVCall(request);
    }

    @Override
    public Observable<LikeCommentResponse> doLikeCommentCall(LikeCommentRequest request) {
        return mApiHelper.doLikeCommentCall(request);
    }

    @Override
    public Observable<GiftMVResponse> doGiftMVCall(GiftMVRequest request) {
        return mApiHelper.doGiftMVCall(request);
    }

    @Override
    public Observable<PostMVCommentResponse> doPostMVCommentCall(PostMVCommentRequest request) {
        return mApiHelper.doPostMVCommentCall(request);
    }

    @Override
    public Observable<PlayMVResponse> doPlayMVCall(PlayMVRequest request) {
        return mApiHelper.doPlayMVCall(request);
    }

    @Override
    public Observable<GetMVGiftLogResponse> doGetMVGiftLogCall(GetMVGiftLogRequest request) {
        return mApiHelper.doGetMVGiftLogCall(request);
    }

    @Override
    public Observable<GetNeedCollectListResponse> doGetNeedCollectCall(GetNeedCollectListRequest request) {
        return mApiHelper.doGetNeedCollectCall(request);
    }

    @Override
    public Observable<GetMyMVListResponse> doGetMyMVListCall(GetMyMVListRequest request) {
        return mApiHelper.doGetMyMVListCall(request);
    }

    @Override
    public Observable<TestStartLiveResponce> doGetTestStartLiveApiCall(TestStartLiveRequest request) {
        return mApiHelper.doGetTestStartLiveApiCall(request);
    }

    @Override
    public Observable<GetMyCustomMadeMVListResponse> doGetMyCustomMadeMVListCall(GetMyCustomMadeMVListRequest request) {
        return mApiHelper.doGetMyCustomMadeMVListCall(request);
    }

    @Override
    public Observable<EnablePostMVResponse> doEnablePostMVCall(EnablePostMVRequest request) {
        return mApiHelper.doEnablePostMVCall(request);
    }

    @Override
    public Observable<GetOtherMVListResponse> doGetOtherMVListCall(GetOtherMVListRequest request) {
        return mApiHelper.doGetOtherMVListCall(request);
    }

    @Override
    public Observable<GetExtGameIconResponce> doGetExtGameIconCall(GetExtGameIconRequest request) {
        return mApiHelper.doGetExtGameIconCall(request);
    }

    @Override
    public Observable<ShareMvResponse> doShareMvCall(ShareMvRequest request) {
        return mApiHelper.doShareMvCall(request);
    }

    @Override
    public Observable<DeleteMVResponse> doDeleteMVCall(DeleteMVRequest request) {
        return mApiHelper.doDeleteMVCall(request);
    }

    @Override
    public Observable<DeleteNeedResponse> doDeleteNeedCall(DeleteNeedRequest request) {
        return mApiHelper.doDeleteNeedCall(request);
    }

    @Override
    public Observable<QueryMVNoticeResponse> doQueryMVNoticeCall(QueryMVNoticeRequest request) {
        return mApiHelper.doQueryMVNoticeCall(request);
    }

    @Override
    public Observable<CancelMVNoticeResponse> doCancelMVNoticeCall(CancelMVNoticeRequest request) {
        return mApiHelper.doCancelMVNoticeCall(request);
    }

    @Override
    public Observable<DownLoadMvResponse> doDownLoadMvCall(DownLoadMvRequest request) {
        return mApiHelper.doDownLoadMvCall(request);
    }

    public Observable<LoadFeedListResponse> doLoadAllFeedListCall(LoadAllFeedListRequest request) {
        return mApiHelper.doLoadAllFeedListCall(request);
    }

    @Override
    public Observable<BlockResponse> doBlockCall(BlockRequest request) {
        return mApiHelper.doBlockCall(request);
    }

    @Override
    public Observable<IsFeedExistResponse> doIsFeedExistCall(IsFeedExistRequset request) {
        return mApiHelper.doIsFeedExistCall(request);
    }

    @Override
    public Observable<AcceptTradeResponse> doAcceptTradeCall(AcceptTradeRequest request) {
        return mApiHelper.doAcceptTradeCall(request);
    }

    @Override
    public Observable<FollowsResponse> doGetFollowsApiCall(FollowsRequest request) {
        return mApiHelper.doGetFollowsApiCall(request);
    }

    @Override
    public Observable<FollowsResponse> doGetFollowersApiCall(FollowsRequest request) {
        return mApiHelper.doGetFollowersApiCall(request);
    }

    @Override
    public Observable<BindPhoneResponse> doBindPhoneApiCall(BindPhoneRequest request) {
        return mApiHelper.doBindPhoneApiCall(request);
    }

    @Override
    public Observable<CheckVerifiCodeResponse> doCheckVerifiCodeApiCall(CheckVerifiCodeRequest
                                                                                request) {
        return mApiHelper.doCheckVerifiCodeApiCall(request);
    }

    @Override
    public Observable<ModifyPhoneResponse> doModifyPhoneApiCall(ModifyPhoneRequest request) {
        return mApiHelper.doModifyPhoneApiCall(request);
    }


    //----------------------------------分割线--->以下部分为文件存储操作------------------------------------------------------

    @Override
    public String getAccessToken() {
        return mPreferencesHelper.getAccessToken();
    }

    @Override
    public void setAccessToken(String accessToken) {
        mPreferencesHelper.setAccessToken(accessToken);
        mApiHelper.getApiHeader().getProtectedApiHeader().setAccessToken(accessToken);
    }

    @Override
    public Long getTimeChaZhi() {
        return mPreferencesHelper.getTimeChaZhi();
    }

    @Override
    public void setTimeChaZhi(Long chaZhi) {
        mPreferencesHelper.setTimeChaZhi(chaZhi);
    }

    @Override
    public String getApiKey() {
        return mPreferencesHelper.getApiKey();
    }

    @Override
    public void setApiKey(String key) {
        mPreferencesHelper.setApiKey(key);
    }

    @Override
    public int getDeviceId() {
        return mPreferencesHelper.getDeviceId();
    }

    @Override
    public void setDeviceId(int deviceId) {
        mPreferencesHelper.setDeviceId(deviceId);
    }

    @Override
    public LoadUserDataBaseResponse.UserData getUserData() {
        return mPreferencesHelper.getUserData();
    }

    @Override
    public void saveUserData(LoadUserDataBaseResponse.UserData userData) {
        mPreferencesHelper.saveUserData(userData);
    }

    @Override
    public int getXiaolabaCount() {
        return mPreferencesHelper.getXiaolabaCount();
    }

    @Override
    public void saveXiaolabaCount(int count) {
        mPreferencesHelper.saveXiaolabaCount(count);
    }


    @Override
    public String getCurrentUserId() {
        return mPreferencesHelper.getCurrentUserId();
    }

    @Override
    public void setCurrentUserId(String userId) {
        mPreferencesHelper.setCurrentUserId(userId);
    }

    @Override
    public int getCurrentUserLoggedInMode() {
        return mPreferencesHelper.getCurrentUserLoggedInMode();
    }

    @Override
    public void setCurrentUserLoggedInMode(LoggedInMode mode) {
        mPreferencesHelper.setCurrentUserLoggedInMode(mode);
    }


    @Override
    public void updateApiHeader(String userId, String accessToken, String apiKey) {
        mApiHelper.getApiHeader().getProtectedApiHeader().setUserId(userId);
        mApiHelper.getApiHeader().getProtectedApiHeader().setAccessToken(accessToken);
        mApiHelper.getApiHeader().getProtectedApiHeader().setApiKey(apiKey);
    }


    @Override
    public void setUserAsLoggedOut() {

        mSocketApiHelper.getSocketApiHeader().closeSocket();

    }

    @Override
    public String handleResponseCode(int code) {

        if (code == 4005) {

            //重新走登录流程
            AppLogger.i("用户请求鉴权失败！即将重新登录......");
            RxBus.get().send(RxBusCode.RX_BUS_CODE_SOCKET_RECONNECT_COMPLETE,
                    new SocketConnectError("0"));
        }
        return "";
    }

    //---------------------------------分割线---->以下部分为数据库操作------------------------------------------

    @Override
    public Observable<Boolean> saveSysGiftNew(List<SysGiftNewBean> mGiftNewBeanList) {
        return mDbHelper.saveSysGiftNew(mGiftNewBeanList);
    }

    @Override
    public List<SysGiftNewBean> getSysGiftNew() {
        return mDbHelper.getSysGiftNew();
    }

    @Override
    public SysGiftNewBean getGiftBeanByID(String giftID) {
        return mDbHelper.getGiftBeanByID(giftID);
    }

    @Override
    public SysMountNewBean getMountBeanByID(String mountID) {
        return mDbHelper.getMountBeanByID(mountID);
    }

    @Override
    public SysGoodsNewBean getGoodsBeanByGiftID(String giftID) {
        return mDbHelper.getGoodsBeanByGiftID(giftID);
    }

    @Override
    public Observable<List<SysGiftNewBean>> getBagBeanList(List<SysbagBean> sysbagBeen) {
        return mDbHelper.getBagBeanList(sysbagBeen);
    }

    @Override
    public List<SysGameBetBean> getSysGameBet(int level) {
        return mDbHelper.getSysGameBet(level);
    }

    @Override
    public SysLevelBean getLevelBeanByID(String level) {
        return mDbHelper.getLevelBeanByID(level);
    }

    @Override
    public String getGoodIDByGiftID(String id) {
        return mDbHelper.getGoodIDByGiftID(id);
    }

    @Override
    public Observable<SysGoodsNewBean> getGoodIDByMountID(String id) {
        return mDbHelper.getGoodIDByMountID(id);
    }

    @Override
    public List<SysGiftNewBean> getLiveGiftList() {
        return mDbHelper.getLiveGiftList();
    }

    @Override
    public Observable<List<SysGoodsNewBean>> getStoreGiftList() {
        return mDbHelper.getStoreGiftList();
    }

    @Override
    public Observable<List<SysGoodsNewBean>> getStoreZuojia() {
        return mDbHelper.getStoreZuojia();
    }

    @Override
    public Observable<Boolean> saveHistoryRecord(HistoryRecordBean recordBean) {
        return mDbHelper.saveHistoryRecord(recordBean);
    }

    @Override
    public Observable<List<HistoryRecordBean>> getAllHistory(String userId) {
        return mDbHelper.getAllHistory(userId);
    }

    @Override
    public Observable<Boolean> deleteAllHistory(String userId) {
        return mDbHelper.deleteAllHistory(userId);
    }

    @Override
    public Observable<Boolean> saveSearchInfo(SearchHistoryInfo historyInfo) {
        return mDbHelper.saveSearchInfo(historyInfo);
    }

    @Override
    public Observable<List<SearchHistoryInfo>> getAllSearchInfo() {
        return mDbHelper.getAllSearchInfo();
    }

    @Override
    public Observable<Boolean> deleteAllSearchInfo() {
        return mDbHelper.deleteAllSearchInfo();
    }

    @Override
    public Observable<Boolean> deleteSearchInfoByAccountId(String accountId) {
        return mDbHelper.deleteSearchInfoByAccountId(accountId);
    }

    @Override
    public Observable<Boolean> saveNoticeSysEntity(NoticeSysEntity entity) {
        return mDbHelper.saveNoticeSysEntity(entity);
    }

    @Override
    public Observable<Boolean> updateNoticeSys(NoticeSysEntity entity) {
        return mDbHelper.updateNoticeSys(entity);
    }

    @Override
    public Observable<List<NoticeSysEntity>> queryNoticeSysByUid(String userId) {
        return mDbHelper.queryNoticeSysByUid(userId);
    }

    @Override
    public Observable<Boolean> saveNoticeSnsEntity(NoticeSnsEntity entity) {
        return mDbHelper.saveNoticeSnsEntity(entity);
    }

    @Override
    public Observable<List<NoticeSnsEntity>> getNoticeSnsEntity(String uid) {
        return mDbHelper.getNoticeSnsEntity(uid);
    }

    @Override
    public Observable<Boolean> deleteNoticeSnsEntityById(long id) {
        return mDbHelper.deleteNoticeSnsEntityById(id);
    }

    @Override
    public Observable<Boolean> saveMessageEntity(MessageEntity entity) {
        return mDbHelper.saveMessageEntity(entity);
    }

    @Override
    public Observable<Boolean> updataMessageEntity(MessageEntity entity) {
        return mDbHelper.updataMessageEntity(entity);
    }

    @Override
    public Observable<Boolean> saveRecentItem(RecentItem entity) {
        return mDbHelper.saveRecentItem(entity);
    }

    @Override
    public Observable<Boolean> deleteRecentItem(RecentItem enity) {
        return mDbHelper.deleteRecentItem(enity);
    }

    @Override
    public List<RecentItem> queryRecentMessageItem(String uid) {
        return mDbHelper.queryRecentMessageItem(uid);
    }

    @Override
    public Observable<Boolean> updateRecentItem(RecentItem enity) {
        return mDbHelper.updateRecentItem(enity);
    }

    @Override
    public List<MessageEntity> queryListMessage(String senderId, String uid) {
        return mDbHelper.queryListMessage(senderId, uid);
    }

    @Override
    public List<SysLaunchTagBean> getkaiboSysLaunchTagBean() {
        return mDbHelper.getkaiboSysLaunchTagBean();
    }

    @Override
    public Observable<Boolean> saveSysFontColorBean(List<SysFontColorBean> beanList) {
        return mDbHelper.saveSysFontColorBean(beanList);
    }

    @Override
    public SysFontColorBean getSysFontColorBeanByKey(String key) {
        return mDbHelper.getSysFontColorBeanByKey(key);
    }

    @Override
    public SysVipBean getSysVipBeanByVip(int vip) {
        return mDbHelper.getSysVipBeanByVip(vip);
    }

    @Override
    public SysVipGoodsBean getSysVipNameById(String id) {
        return mDbHelper.getSysVipNameById(id);
    }

    @Override
    public void saveMvDownLog(MvDownLog downLog) {
          mDbHelper.saveMvDownLog(downLog);
    }

    @Override
    public MvDownLog getMvDownLogByMvId(String mvId, String userId) {
        return  mDbHelper.getMvDownLogByMvId(mvId,userId);
    }

    @Override
    public List<MvDownLog> getMvDownLogList(String userId) {
        return mDbHelper.getMvDownLogList(userId);
    }

    @Override
    public void deleteMvDownLogById(String id) {
        mDbHelper.deleteMvDownLogById(id);
    }

    @Override
    public Observable<Boolean> saveSysHBBean(List<SysHBBean> beanList) {
        return mDbHelper.saveSysHBBean(beanList);
    }

    @Override
    public List<SysHBBean> getSysHBBean() {
        return mDbHelper.getSysHBBean();
    }

    @Override
    public Observable<Boolean> saveSysLevelBean(List<SysLevelBean> beanList) {
        return mDbHelper.saveSysLevelBean(beanList);
    }

    @Override
    public List<SysLevelBean> getSysLevelBean() {
        return mDbHelper.getSysLevelBean();
    }

    @Override
    public Observable<Boolean> saveSysTipsBean(List<SysTipsBean> beanList) {
        return mDbHelper.saveSysTipsBean(beanList);
    }

    @Override
    public List<SysTipsBean> getSysTipsBean() {
        return mDbHelper.getSysTipsBean();
    }

    @Override
    public Observable<Boolean> saveSysGoodsNewBean(List<SysGoodsNewBean> beanList) {
        return mDbHelper.saveSysGoodsNewBean(beanList);
    }

    @Override
    public List<SysGoodsNewBean> getSysGoodsNewBean() {
        return mDbHelper.getSysGoodsNewBean();
    }

    @Override
    public Observable<Boolean> saveSysItemBean(List<SysItemBean> beanList) {
        return mDbHelper.saveSysItemBean(beanList);
    }

    @Override
    public List<SysItemBean> getSysItemBean() {
        return mDbHelper.getSysItemBean();
    }

    @Override
    public Observable<Boolean> saveSysGameBetBean(List<SysGameBetBean> beanList) {
        return mDbHelper.saveSysGameBetBean(beanList);
    }

    @Override
    public List<SysGameBetBean> getSysGameBetBean() {
        return mDbHelper.getSysGameBetBean();
    }

    @Override
    public Observable<Boolean> saveSysMountNewBean(List<SysMountNewBean> beanList) {
        return mDbHelper.saveSysMountNewBean(beanList);
    }

    @Override
    public List<SysMountNewBean> getSysMountNewBean() {
        return mDbHelper.getSysMountNewBean();
    }

    @Override
    public Observable<Boolean> saveSysConfigBean(SysConfigBean mSysConfigBean) {
        return mDbHelper.saveSysConfigBean(mSysConfigBean);
    }

    @Override
    public List<SysConfigBean> getSysConfigBean() {
        return mDbHelper.getSysConfigBean();
    }

    @Override
    public SysConfigBean getSysConfig() {
        return mDbHelper.getSysConfig();
    }

    @Override
    public Observable<Boolean> saveSysLaunchTagBean
            (List<SysLaunchTagBean> mSysLaunchTagBean) {
        return mDbHelper.saveSysLaunchTagBean(mSysLaunchTagBean);
    }

    @Override
    public Observable<List<SysLaunchTagBean>> getSysLaunchTagBean() {
        return mDbHelper.getSysLaunchTagBean();
    }

    @Override
    public Observable<Boolean> saveSysGuardGoodsBean
            (List<SysGuardGoodsBean> sysGuardGoodsBeen) {
        return mDbHelper.saveSysGuardGoodsBean(sysGuardGoodsBeen);
    }

    @Override
    public Observable<List<SysGuardGoodsBean>> getSysGuardGoodsBean() {
        return mDbHelper.getSysGuardGoodsBean();
    }

    @Override
    public Observable<Boolean> saveSysSignAwardBean(List<SysSignAwardBean> sysSignAwardBeen) {
        return mDbHelper.saveSysSignAwardBean(sysSignAwardBeen);
    }

    @Override
    public Observable<List<AwardsBean>> getSysSignAwardBean() {
        return mDbHelper.getSysSignAwardBean();
    }

    @Override
    public Observable<Boolean> saveSysVipGoodsBean(List<SysVipGoodsBean> sysVipGoodsBeen) {
        return mDbHelper.saveSysVipGoodsBean(sysVipGoodsBeen);
    }

    @Override
    public Observable<List<SysVipGoodsBean>> getSysVipGoodsBean() {
        return mDbHelper.getSysVipGoodsBean();
    }

    @Override
    public Observable<Boolean> saveSysVipBean(List<SysVipBean> sysVipGoodsBeen) {
        return mDbHelper.saveSysVipBean(sysVipGoodsBeen);
    }

    @Override
    public Observable<List<SysVipBean>> getSysVipBean() {
        return mDbHelper.getSysVipBean();
    }


    //---------------------------------分割线---->以下部分为socket api 操作------------------------------------------

    @Override
    public SocketApiHeader getSocketApiHeader() {
        return mSocketApiHelper.getSocketApiHeader();
    }

    @Override
    public Observable<SocketBaseResponse> doConnectSocketApiCall(List<String> socketIp) {
        return mSocketApiHelper.doConnectSocketApiCall(socketIp);
    }

    @Override
    public Observable<VerifySocketTokenResponse> doVerifySocketTokenSocketApiCall
            (VerifySocketTokenRequest request) {
        return mSocketApiHelper.doVerifySocketTokenSocketApiCall(request);
    }

    @Override
    public Observable<LoadUserDataBaseResponse> doLoadUserDataBaseSocketApiCall
            (LoadUserDataBaseRequest request) {
        return mSocketApiHelper.doLoadUserDataBaseSocketApiCall(request);
    }

    @Override
    public Observable<JoinRoomResponce> doLoadJoinRoomSocketApiCall(JoinRoomRequest request) {
        return mSocketApiHelper.doLoadJoinRoomSocketApiCall(request);
    }

    @Override
    public Observable<StartLiveResponce> doLoadStartLiveApiCall(StartLiveRequest request) {
        return mSocketApiHelper.doLoadStartLiveApiCall(request);
    }

    @Override
    public Observable<LeaveRoomResponce> doLoadLeaveRoomApiCall(LeaveRoomRequest request) {
        return mSocketApiHelper.doLoadLeaveRoomApiCall(request);
    }

    @Override
    public Observable<CloseLiveResponse> doCloseLiveLiveApiCall(CloseLiveRequest request) {
        return mSocketApiHelper.doCloseLiveLiveApiCall(request);
    }

    @Override
    public Observable<LoadOtherUserInfoResponce> doLoadOtherUserInfoApiCall
            (LoadOtherUserInfoRequest request) {
        return mSocketApiHelper.doLoadOtherUserInfoApiCall(request);
    }

    @Override
    public Observable<SendRoomResponce> doSendRoomMsgApiCall(SendRoomMsgRequest request) {
        return mSocketApiHelper.doSendRoomMsgApiCall(request);
    }

    @Override
    public Observable<GetLiveStatusResponce> doGetLiveStatusApiCall(GetLiveStatusRequest
                                                                            request) {
        return mSocketApiHelper.doGetLiveStatusApiCall(request);
    }

    @Override
    public Observable<ModifyProfileLiveCoverResponse> doModifyProfileLiveCoverApiCall
            (ModifyProfileLiveCoverRequest request) {
        return mSocketApiHelper.doModifyProfileLiveCoverApiCall(request);
    }

}
