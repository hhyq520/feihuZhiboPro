package cn.feihutv.zhibofeihu.di.component;


import cn.feihutv.zhibofeihu.activitys.SplashActiity;
import cn.feihutv.zhibofeihu.di.PerActivity;
import cn.feihutv.zhibofeihu.di.module.ActivityModule;
import cn.feihutv.zhibofeihu.ui.bangdan.ConFragment;
import cn.feihutv.zhibofeihu.ui.bangdan.GuardFragment;
import cn.feihutv.zhibofeihu.ui.bangdan.RankingFragment;
import cn.feihutv.zhibofeihu.ui.bangdan.luck.DayListFragment;
import cn.feihutv.zhibofeihu.ui.bangdan.luck.TotalListFragment;
import cn.feihutv.zhibofeihu.ui.home.HomeFragment;
import cn.feihutv.zhibofeihu.ui.home.history.HistoryActivity;
import cn.feihutv.zhibofeihu.ui.home.search.SearchUserActivity;
import cn.feihutv.zhibofeihu.ui.home.signin.SignInDialog;
import cn.feihutv.zhibofeihu.ui.live.ChatLiveFragment;
import cn.feihutv.zhibofeihu.ui.live.ChatPlayFragment;
import cn.feihutv.zhibofeihu.ui.live.PLVideoViewActivity;
import cn.feihutv.zhibofeihu.ui.live.RankContriFragment;
import cn.feihutv.zhibofeihu.ui.live.ReportActivity;
import cn.feihutv.zhibofeihu.ui.live.SWCameraStreamingActivity;
import cn.feihutv.zhibofeihu.ui.live.UserOnlineFragment;
import cn.feihutv.zhibofeihu.ui.live.pclive.LiveChatFragment;
import cn.feihutv.zhibofeihu.ui.live.pclive.LivePlayInPCActivity;
import cn.feihutv.zhibofeihu.ui.live.renzheng.IdentityInfoActivity;
import cn.feihutv.zhibofeihu.ui.live.renzheng.RenzhengSuccessActivity;
import cn.feihutv.zhibofeihu.ui.live.renzheng.UploadFailureActivity;
import cn.feihutv.zhibofeihu.ui.live.renzheng.UploadSuccessActivity;
import cn.feihutv.zhibofeihu.ui.main.MainActivity;
import cn.feihutv.zhibofeihu.ui.me.ContributionListActivity;
import cn.feihutv.zhibofeihu.ui.me.HisGuardActivity;
import cn.feihutv.zhibofeihu.ui.me.MyFragment;
import cn.feihutv.zhibofeihu.ui.me.concern.ConcernActivity;
import cn.feihutv.zhibofeihu.ui.me.concern.FansActivity;
import cn.feihutv.zhibofeihu.ui.me.dowm.MyDownActivity;
import cn.feihutv.zhibofeihu.ui.me.dynamic.DynamicDetailActivity;
import cn.feihutv.zhibofeihu.ui.me.dynamic.MyDynamicFragment;
import cn.feihutv.zhibofeihu.ui.me.dynamic.OtherPageFragment;
import cn.feihutv.zhibofeihu.ui.me.dynamic.OthersCommunityActivity;
import cn.feihutv.zhibofeihu.ui.me.dynamic.SendDynamicActivity;
import cn.feihutv.zhibofeihu.ui.me.encash.IncomeActivity;
import cn.feihutv.zhibofeihu.ui.me.encash.MyEarningsActivity;
import cn.feihutv.zhibofeihu.ui.me.encash.WithdrawalrecordActivity;
import cn.feihutv.zhibofeihu.ui.me.guard.GuardListFragment;
import cn.feihutv.zhibofeihu.ui.me.guard.GuardsFragment;
import cn.feihutv.zhibofeihu.ui.me.mv.MyMvProductionActivity;
import cn.feihutv.zhibofeihu.ui.me.personalInfo.PersonalInfoActivity;
import cn.feihutv.zhibofeihu.ui.me.recharge.PrepaidrecordsActivity;
import cn.feihutv.zhibofeihu.ui.me.recharge.RechargeActivity;
import cn.feihutv.zhibofeihu.ui.me.roommrgs.RoomMrgsActivity;
import cn.feihutv.zhibofeihu.ui.me.setting.BlacklistActivity;
import cn.feihutv.zhibofeihu.ui.me.setting.MsgReceSettingsActivity;
import cn.feihutv.zhibofeihu.ui.me.shop.BeautiFragment;
import cn.feihutv.zhibofeihu.ui.me.shop.CarFragment;
import cn.feihutv.zhibofeihu.ui.me.shop.GuardShopFragment;
import cn.feihutv.zhibofeihu.ui.me.vip.MyReceiveFragment;
import cn.feihutv.zhibofeihu.ui.me.vip.MySendFragment;
import cn.feihutv.zhibofeihu.ui.me.vip.MyVipActivity;
import cn.feihutv.zhibofeihu.ui.mv.DemandSquareFragment;
import cn.feihutv.zhibofeihu.ui.mv.MvSquareFragment;
import cn.feihutv.zhibofeihu.ui.mv.MvVideoDetailsActivity;
import cn.feihutv.zhibofeihu.ui.mv.MvVideoListFragment;
import cn.feihutv.zhibofeihu.ui.mv.MyMvGiftActivity;
import cn.feihutv.zhibofeihu.ui.mv.demand.BuyMvDemandActivity;
import cn.feihutv.zhibofeihu.ui.mv.demand.MyDemandActivity;
import cn.feihutv.zhibofeihu.ui.mv.demand.MyDemandPageFragment;
import cn.feihutv.zhibofeihu.ui.mv.demand.MyMvCollectActivity;
import cn.feihutv.zhibofeihu.ui.mv.demand.PostDemandActivity;
import cn.feihutv.zhibofeihu.ui.mv.demand.PostMvVideoActivity;
import cn.feihutv.zhibofeihu.ui.user.forgetPsd.ForgetPsdActivity;
import cn.feihutv.zhibofeihu.ui.user.login.LoginActivity;
import cn.feihutv.zhibofeihu.ui.user.phoneLogin.PhoneLoginActivity;
import cn.feihutv.zhibofeihu.ui.user.register.RegisterActivity;
import dagger.Component;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/09/25
 *     desc   : 依赖注入 activity 基本依赖：定义需要注入的业务类，具体实现
 *     version: 1.0
 * </pre>
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(SplashActiity activity);

    void inject(SWCameraStreamingActivity activity);

    void inject(LoginActivity activity);

    void inject(UploadFailureActivity activity);

    void inject(MainActivity activity);

    void inject(RenzhengSuccessActivity activity);

    void inject(UploadSuccessActivity activity);

    void inject(IdentityInfoActivity activity);

    void inject(PhoneLoginActivity activity);

    void inject(HomeFragment fragment);

    void inject(RegisterActivity activity);

    void inject(ForgetPsdActivity activity);

    void inject(SignInDialog fragment);

    void inject(ChatLiveFragment fragment);

    void inject(RankContriFragment fragment);

    void inject(UserOnlineFragment fragment);

    void inject(RechargeActivity activity);

    void inject(HistoryActivity activity);

    void inject(SearchUserActivity activity);

    void inject(PLVideoViewActivity activity);

    void inject(ChatPlayFragment fragment);

    void inject(LivePlayInPCActivity activity);

    void inject(LiveChatFragment fragment);

    void inject(MyFragment fragment);

    void inject(DynamicDetailActivity fragment);

    void inject(OthersCommunityActivity fragment);

    void inject(RankingFragment fragment);

    void inject(ConFragment fragment);

    void inject(DayListFragment fragment);

    void inject(TotalListFragment fragment);

    void inject(GuardFragment fragment);

    void inject(SendDynamicActivity activity);

    void inject(PersonalInfoActivity activity);

    void inject(ConcernActivity activity);

    void inject(ReportActivity activity);

    void inject(FansActivity activity);

    void inject(PrepaidrecordsActivity activity);

    void inject(IncomeActivity activity);

    void inject(MyEarningsActivity activity);

    void inject(WithdrawalrecordActivity activity);

    void inject(RoomMrgsActivity activity);

    void inject(BlacklistActivity activity);

    void inject(ContributionListActivity activity);

    void inject(GuardsFragment fragment);

    void inject(GuardListFragment fragment);

    void inject(GuardShopFragment fragment);

    void inject(CarFragment fragment);

    void inject(BeautiFragment fragment);

    //mv 广场
    void inject(MvSquareFragment fragment);

    void inject(DemandSquareFragment fragment);

    void inject(MvVideoListFragment fragment);

    void inject(MyVipActivity activity);

    void inject(MyDemandActivity activity);

    void inject(MyDemandPageFragment fragment);

    void inject(PostDemandActivity activity);

    void inject(PostMvVideoActivity activity);

    void inject(MvVideoDetailsActivity activity);

    void inject(BuyMvDemandActivity activity);

    void inject(MyReceiveFragment fragment);

    void inject(MySendFragment fragment);

    void inject(MsgReceSettingsActivity activity);

    void inject(OtherPageFragment fragment);

    void inject(MyMvGiftActivity activity);

    void inject(MyMvCollectActivity activity);

    void inject(MyMvProductionActivity activity);

    void inject(HisGuardActivity activity);

    void inject(MyDynamicFragment fragment);

    void inject(MyDownActivity activity);
}
