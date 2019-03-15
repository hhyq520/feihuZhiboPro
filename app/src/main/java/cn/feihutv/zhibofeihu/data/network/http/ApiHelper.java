/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package cn.feihutv.zhibofeihu.data.network.http;


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

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/09/25
 *     desc   : http 网络请求api接口，定义所有网络请求方法
 *     version: 1.0
 * </pre>
 */

public interface ApiHelper {

    //获取请求头
    ApiHeader getApiHeader();


    //web 页面之间调用http 请求
    Observable<WebHttpResponse> doWebHttpApiCall(WebHttpRequest request);


    // 用户登录
    Observable<LoginResponse> doServerLoginApiCall(LoginRequest.ServerLoginRequest request);

    //第三方登录
    Observable<LoginResponse> doPlatformLoginApiCall(LoginRequest.PlatformLoginRequest request);

    //游客登录
    Observable<LoginResponse> doGuestLoginApiCall(LoginRequest.GuestLoginRequest request);

    //客户端检查升级更新
    Observable<CheckUpdateResponse> doCheckUpdateApiCall(CheckUpdateRequest request);

    //获取socket api
    Observable<GetSocketTokenResponse> doGetSocketTokenApiCall(GetSocketTokenRequest request);

    //获取用户背包数据
    Observable<BagResponse> doGetUserBagDataApiCall(BagRequest request);

    //获取认证情况
    Observable<CertifiResponse> doGetUseCertifiDataApiCall(CertifiResquest request);

    // 根据id获取房间信息
    Observable<LoadRoomResponce> doGetRoomDataApiCall(LoadRoomRequest request);

    //获取签到情况
    Observable<LoadSignResponce> doGetSignDataApiCall(LoadSignRequest request);

    //签到
    Observable<SignResponse> doSignApiCall(SignRequest request);

    //Android提交版本信息
    Observable<LogDeviceResponse> doLogDeviceApiCall(LogDeviceRequest request);

    //加载直播列表
    Observable<LoadRoomListResponse> doLoadRoomListByTagApiCall(LoadRoomListRequest request);

    //加载关注推荐列表
    Observable<LoadRoomListResponse> doLoadCareRecommendedListApiCall(LoadRoomListRequest request);

    //拉取贡献榜
    Observable<LoadContriRankListResponse> doLoadContriRankListApiCall(LoadContriRankListRequest request);

    //拉取排行榜
    Observable<LoadContriRankListResponse> doLoadIncomeRankListApiCall(LoadContriRankListRequest request);

    //拉取幸运榜--幸运礼物
    Observable<LoadLuckRecordListResponse> doLoadLuckRecordListApiCall(LoadLuckRecordListRequest request);

    //拉取幸运榜--幸运礼物
    Observable<GetGuardRankResponse> doGetGuardRankApiCall(GetGuardRankRequest request);

    //拉取幸运榜--总榜
    Observable<LoadLuckRankListResponse> doLoadLuckRankListApiCall(LoadLuckRecordListRequest request);

    //获取验证码
    Observable<GetVerifyCodeResponse> doGetVerifyCodeApiCall(GetVerifyCodeRequest request);

    //找回密码
    Observable<RestPsdResponse> doRestPsdApiCall(RestPsdRequest request);

    //注册用户
    Observable<CreateUserResponse> doCreateUserApiCall(CreateUserRequest request);

    //获取首页banner
    Observable<GetBannerResponse> doGetBannerByTypeApiCall(GetBannerRequest request);

    //获取地理位置
    Observable<LocationResponce> doGetLocationApiCall(LocationRequest request);

    //切换前后台
    Observable<SwitchAppFocusResponce> doSwitchAppFocusApiCall(SwitchAppFocusRequest request);

    //分享日志
    Observable<LogShareResponce> doLogShareApiCall(LogShareRequest request);

    //关注
    Observable<FollowResponce> doFollowApiCall(FollowRequest request);

    //取消关注
    Observable<UnFollowResponce> doUnFollowApiCall(UnFollowRequest request);

    //设置场控
    Observable<SetRoomMgrResponce> doSetRoomMgrApiCall(SetRoomMgrRequest request);

    //取消场控
    Observable<CancelRoomMgrResponce> doCancelRoomMgrCall(CancelRoomMgrRequest request);

    //禁言
    Observable<BanResponce> doBanApiCall(BanRequest request);

    //加载房间贡献榜
    Observable<LoadRoomContriResponce> doLoadRoomContriApiCall(LoadRoomContriRequest request);

    //获取幸运彩池开奖倒计时（进入直播间时）
    Observable<JackpotCountDownResponce> doJackpotCountDownRequestApiCall(JackpotCountDownRequest request);

    //发送全站小喇叭
    Observable<SendLoudSpeakResponce> doSendLoudSpeakApiCall(SendLoudSpeakRequest request);

    //房间在线列表
    Observable<LoadRoomMemberResponce> doLoadRoomMembersApiCall(LoadRoomMembersRequest request);

    //游客数量
    Observable<LoadRoomGuestResponce> doLoadRoomGuestApiCall(LoadRoomGuestRequest request);

    //房管列表
    Observable<GetRoomMrgsResponce> doGetRoomMrgsRequestApiCall(GetRoomMrgsRequest request);

    //请求彩池数据
    Observable<GetJackpotDataResponce> doGetJackpotDataApiCall(GetJackpotDataRequest request);

    //幸运彩池获取幸运记录
    Observable<GetLuckLogsByIdResponce> doGetLuckLogsByIdApiCall(GetLuckLogsByIdRequest request);

    //幸运彩池获取我的幸运记录
    Observable<GetMyLuckLogsByIdResponce> doGetMyLuckLogsByIdApiCall(GetMyLuckLogsByIdRequest request);

    //获取玩法当前状态
    Observable<GetGameStatusResponce> doGetGameStatusApiCall(GetGameStatusRequest request);

    //主播开奖
    Observable<GameSettleResponce> doGameSettleApiCall(GameSettleRequest request);

    //获取押注情况（用于5s轮询更新界面）
    Observable<GetGameBetResponce> doGetGameBetApiCall(GetGameBetRequest request);

    //收到每局结果后，前端查询输赢详情
    Observable<GetGameRoundResultDetailResponce> doGetGameRoundResultDetailApiCall(GetGameRoundResultDetailRequest request);

    //主播抢权限
    Observable<GamePreemptResponce> doGamePreemptApiCall(GamePreemptRequest request);

    //参加玩法
    Observable<GameBetResponce> doGameBetApiCall(GameBetRequest request);

    //今日押注情况
    Observable<GetUserGameDataV3Responce> doGetUserGameDataV3ApiCall(GetUserGameDataV3Request request);

    //开奖记录
    Observable<GetGameSettleHistoryResponce> doGetGameSettleHistoryApiCall(GetGameSettleHistoryRequest request);

    //竞猜历史
    Observable<GetGameBetHistoryResponce> doGetGameBetHistoryApiCall(GetGameBetHistoryRequest request);


    //获取当前坐骑
    Observable<GetCurrMountResponce> doGetCurrMountApiCall(GetCurrMountRequest request);

    //购买商品
    Observable<BuyGoodsResponce> doBuyGoodsApiCall(BuyGoodsRequest request);

    //赠送礼物
    Observable<SendGiftResponce> doSendGiftApiCall(SendGiftRequest request);

    //获取玩法页面玩法列表（观众端)
    Observable<GetGameListResponce> doGetGameListApiCall(GetGameListRequest request);

    //主播下播后推荐2个直播间
    Observable<LoadOtherRoomsResponce> doLoadOtherRoomsApiCall(LoadOtherRoomsRequest request);

    //搜索主播/用户
    Observable<SearchResponse> doSearchApiCall(SearchRequest request);

    //搜索主播/用户
    Observable<RecommendResponse> doLoadRecommendedListApiCall(RecommendRequest request);

    //获取上传图片到cos的sign
    Observable<GetCosSignResponse> doGetCosSignApiCall(GetCosSignRequest request);

    //用户点赞
    Observable<LikeFeedResponse> doLikeFeedApiCall(LikeFeedRequest request);

    //社区-获取动态列表
    Observable<LoadFeedListResponse> doLoadFeedListApiCall(LoadFeedListRequest request);


    //社区-获取动态列表
    Observable<LoadFeedDetailResponse> doLoadFeedDetailApiCall(LoadFeedDetailRequest request);

    //加载评论列表
    Observable<LoadCommentListResponse> doLoadCommentListApiCall(LoadCommentListRequest request);

    //删除动态
    Observable<DeleteFeedResponse> doDeleteFeedApiCall(DeleteFeedRequest request);


    //发表动态评论
    Observable<PostFeedCommentResponse> doPostFeedCommentApiCall(PostFeedCommentRequest request);

    //分享
    Observable<ShareFeedResponse> doShareFeedApiCall(ShareFeedRequest request);

    //加载收礼和送礼明细
    Observable<LoadUserHBDataResponse> doLoadUserHBDataApiCall(LoadUserHBDataRequest request);

    //消息
    Observable<LoadMsgListResponce> doLoadMsgListRequestApiCall(LoadMsgListRequest request);

    //获取好友列表
    Observable<GetFriendsResponce> doGetFriendsApiCall(GetFriendsRequest request);

    //私信
    Observable<MessageResponce> doMessageApiCall(MessageRequest request);

    //修改密码
    Observable<ModifyUserPassResponse> doModifyUserPassApiCall(ModifyUserPassRequest request);

    //拉取关注列表
    Observable<FollowsResponse> doGetFollowsApiCall(FollowsRequest request);

    //拉取粉丝列表
    Observable<FollowsResponse> doGetFollowersApiCall(FollowsRequest request);

    //绑定手机号码
    Observable<BindPhoneResponse> doBindPhoneApiCall(BindPhoneRequest request);

    //绑定手机号码
    Observable<CheckVerifiCodeResponse> doCheckVerifiCodeApiCall(CheckVerifiCodeRequest request);

    //更换手机号码
    Observable<ModifyPhoneResponse> doModifyPhoneApiCall(ModifyPhoneRequest request);

    //举报
    Observable<ReportResponce> doReportCall(ReportRequest request);

    //发布动态
    Observable<PostFeedResponse> doPostFeedCall(PostFeedRequest request);

    //支付
    Observable<PayResponse> doPayCall(PayRequest request);

    //充值列表
    Observable<GetPayListResponse> doGetPayListCall(GetPayListRequest request);

    //提现
    Observable<EncashResponse> doEncashCall(EncashRequest request);

    //获取账号提现相关信息
    Observable<GetEncashInfoResponse> doGetEncashInfoCall(GetEncashInfoRequest request);

    //绑定支付宝
    Observable<BindAlipayResponse> doBindAlipayCall(BindAlipayRequest request);

    //提现列表
    Observable<GetEncashListResponse> doGetEncashListCall(GetEncashListRequest request);

    //拉取黑名单列表
    Observable<GetBlacklistResponse> doGetBlacklistCall(GetBlacklistRequest request);

    //移除黑名单列表
    Observable<UnblockResponse> doUnblockCall(UnblockRequest request);

    //我的守护 ta的守护
    Observable<GetUserGuardsResponse> doGetUserGuardsCall(GetUserGuardsRequest request);

    // 我守护的  ta守护的
    Observable<GetUserGuardListResponse> doGetUserGuardListCall(GetUserGuardListRequest request);

    // 设置座驾
    Observable<SetMountResponse> doSetMountCall(SetMountRequest request);

    // 加载靓号列表
    Observable<LoadShopAccountIdListResponse> doLoadShopAccountIdListCall(LoadShopAccountIdListRequest request);

    // 加载靓号列表
    Observable<BuyShopAccountIdResponse> doBuyShopAccountIdCall(BuyShopAccountIdRequest request);


    // 房间贵宾席
    Observable<RoomVipResponce> doRoomVipApiCall(RoomVipRequest request);

    // 检索飞虎id
    Observable<CheckAccountIdResponse> doCheckAccountIdCall(CheckAccountIdRequest request);

    // 商城守护检索主播
    Observable<CheckGuardInfoResponse> doCheckGuardInfoCall(CheckGuardInfoRequest request);

    // 商城守护主播
    Observable<BuyGuardResponse> doBuyGuardCall(BuyGuardRequest request);

    // 获得背包礼物列表
    Observable<LoadBagGiftsResponse> doLoadBagGiftsCall(LoadBagGiftsRequest request);

    // 获取座驾列表
    Observable<GetMountListResponse> doGetMountListCall(GetMountListRequest request);

    //获取上传mv视频的token和时间戳
    Observable<GetMVTokenResponse> doGetMVTokenCall(GetMVTokenRequest request);

    //发布MV
    Observable<PostMvResponse> doPostMvCall(PostMvRequest request);

    //获取mv 列表-广场
    Observable<GetAllMvListResponse> doGetAllMvListCall(GetAllMvListRequest request);

    //发布需求
    Observable<PostNeedResponse> doPostNeedCall(PostNeedRequest request);

    //获取需求列表
    Observable<GetAllNeedListResponse> doGetAllNeedListCall(GetAllNeedListRequest request);

    //获取我的需求列表
    Observable<GetMyNeedListResponse> doGetMyNeedListCall(GetMyNeedListRequest request);


    // 发起交易
    Observable<MakeTradeResponse> doMakeTradeCall(MakeTradeRequest request);

    // 搜索靓号价格区间
    Observable<LoadLiangSearchKeyResponse> doLoadLiangSearchKeyCall(LoadLiangSearchKeyRequest request);

    // 发布需求页面-获取发布赏金列表
    Observable<GetNeedSysHBResponse> doGetNeedSysHBCall(GetNeedSysHBRequest request);

    // 发布需求页面-修改需求
    Observable<EditNeedResponse> doEditNeedCall(EditNeedRequest request);

    // 购买VIP
    Observable<BuyVipResponse> doBuyVipCall(BuyVipRequest request);

    // vip-我的收获记录
    Observable<GetVipReceiveLogResponse> doGetVipRecvLogCall(GetVipReceiveLogRequest request);

    // vip-我的赠送
    Observable<GetVipSendLogResponse> doGetVipSendLogCall(GetVipSendLogRequest request);

    // vip-赠送好友VIP
    Observable<SendVipResponse> doSendVipCall(SendVipRequest request);

    // 获取消息接收状态
    Observable<GetMsgSwitchStatusResponse> doGetMsgSwitchStatusCall(GetMsgSwitchStatusRequest request);

    // 设置消息接收状态
    Observable<SetMsgSwitchStatusResponse> doSetMsgSwitchStatusCall(SetMsgSwitchStatusRequest request);

    //  收藏需求
    Observable<CollectNeedResponse> doCollectNeedCall(CollectNeedRequest request);

    //  取消收藏需求
    Observable<UnCollectNeedResponse> doUnCollectNeedCall(UnCollectNeedRequest request);

    //  获取待成交、已完成作品列表
    Observable<GetMyNeedMVListResponse> doGetMyNeedMVListCall(GetMyNeedMVListRequest request);

    //  购买mv
    Observable<BuyMVResponse> doBuyMVCall(BuyMVRequest request);
    //  加入黑名单
    Observable<BlockResponse> doBlockCall(BlockRequest request);

    //  检查动态是否存在
    Observable<IsFeedExistResponse> doIsFeedExistCall(IsFeedExistRequset request);

    //  接受交易请求
    Observable<AcceptTradeResponse> doAcceptTradeCall(AcceptTradeRequest request);

    //  反馈给主播mv作品修改
    Observable<FeedbackMVResponse> doFeedbackMVCall(FeedbackMVRequest request);

    // mv详情
    Observable<GetMVDetailResponse> doGetMVDetailCall(GetMVDetailRequest request);

    //获取mv评论列表
    Observable<GetMVCommentListResponse>  doGetMVCommentListCall(GetMVCommentListRequest request);

    //  mv点赞
    Observable<LikeMVResponse> doGetLikeMVCall(LikeMVRequest request);

    //  对评论点赞
    Observable<LikeCommentResponse> doLikeCommentCall(LikeCommentRequest request);


    //     mv送礼
    Observable<GiftMVResponse> doGiftMVCall(GiftMVRequest request);


    //     发表mv评论
    Observable<PostMVCommentResponse> doPostMVCommentCall(PostMVCommentRequest request);

    //播放记录
    Observable<PlayMVResponse> doPlayMVCall(PlayMVRequest request);

    //获取送礼记录
    Observable<GetMVGiftLogResponse> doGetMVGiftLogCall(GetMVGiftLogRequest request);


    // 收藏列表
    Observable<GetNeedCollectListResponse> doGetNeedCollectCall(GetNeedCollectListRequest request);


    // 我的mv作品列表
    Observable<GetMyMVListResponse> doGetMyMVListCall(GetMyMVListRequest request);


    //检查是否被禁播
    Observable<TestStartLiveResponce> doGetTestStartLiveApiCall(TestStartLiveRequest request);

    // 我的mv作品列表
    Observable<LoadFeedListResponse> doLoadAllFeedListCall(LoadAllFeedListRequest request);

    // 我的定制MV作品
    Observable<GetMyCustomMadeMVListResponse> doGetMyCustomMadeMVListCall(GetMyCustomMadeMVListRequest request);


    // 检查是否可以提交MV
    Observable<EnablePostMVResponse> doEnablePostMVCall(EnablePostMVRequest request);


    // 获取他人的mv 列表
    Observable<GetOtherMVListResponse> doGetOtherMVListCall(GetOtherMVListRequest request);

    //获取动态增加活动入口
    Observable<GetExtGameIconResponce> doGetExtGameIconCall(GetExtGameIconRequest request);

    //分享mv
    Observable<ShareMvResponse> doShareMvCall(ShareMvRequest request);

    //删除mv
    Observable<DeleteMVResponse> doDeleteMVCall(DeleteMVRequest request);

    //删除need
    Observable<DeleteNeedResponse> doDeleteNeedCall(DeleteNeedRequest request);

    //查询是否有mv系统消息
    Observable<QueryMVNoticeResponse> doQueryMVNoticeCall(QueryMVNoticeRequest request);


    //设置mv系统消息为已读状态
    Observable<CancelMVNoticeResponse> doCancelMVNoticeCall(CancelMVNoticeRequest request);

    //下载mv
    Observable<DownLoadMvResponse> doDownLoadMvCall(DownLoadMvRequest request);


}
