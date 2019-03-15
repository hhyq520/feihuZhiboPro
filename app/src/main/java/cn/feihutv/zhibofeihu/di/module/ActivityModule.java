package cn.feihutv.zhibofeihu.di.module;


import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import cn.feihutv.zhibofeihu.activitys.SplashMvpPresenter;
import cn.feihutv.zhibofeihu.activitys.SplashMvpView;
import cn.feihutv.zhibofeihu.activitys.SplashPresenter;
import cn.feihutv.zhibofeihu.di.ActivityContext;
import cn.feihutv.zhibofeihu.di.PerActivity;
import cn.feihutv.zhibofeihu.ui.bangdan.ConMvpPresenter;
import cn.feihutv.zhibofeihu.ui.bangdan.ConMvpView;
import cn.feihutv.zhibofeihu.ui.bangdan.ConPresenter;
import cn.feihutv.zhibofeihu.ui.bangdan.GuardMvpPresenter;
import cn.feihutv.zhibofeihu.ui.bangdan.GuardMvpView;
import cn.feihutv.zhibofeihu.ui.bangdan.GuardPresenter;
import cn.feihutv.zhibofeihu.ui.bangdan.RankingMvpPresenter;
import cn.feihutv.zhibofeihu.ui.bangdan.RankingMvpView;
import cn.feihutv.zhibofeihu.ui.bangdan.RankingPresenter;
import cn.feihutv.zhibofeihu.ui.bangdan.luck.DayListMvpPresenter;
import cn.feihutv.zhibofeihu.ui.bangdan.luck.DayListMvpView;
import cn.feihutv.zhibofeihu.ui.bangdan.luck.DayListPresenter;
import cn.feihutv.zhibofeihu.ui.bangdan.luck.TotalListMvpPresenter;
import cn.feihutv.zhibofeihu.ui.bangdan.luck.TotalListMvpView;
import cn.feihutv.zhibofeihu.ui.bangdan.luck.TotalListPresenter;
import cn.feihutv.zhibofeihu.ui.home.HomeMvpPresenter;
import cn.feihutv.zhibofeihu.ui.home.HomeMvpView;
import cn.feihutv.zhibofeihu.ui.home.HomePresenter;
import cn.feihutv.zhibofeihu.ui.home.history.HistoryMvpPresenter;
import cn.feihutv.zhibofeihu.ui.home.history.HistoryMvpView;
import cn.feihutv.zhibofeihu.ui.home.history.HistoryPresenter;
import cn.feihutv.zhibofeihu.ui.home.search.SearchUserMvpPresenter;
import cn.feihutv.zhibofeihu.ui.home.search.SearchUserMvpView;
import cn.feihutv.zhibofeihu.ui.home.search.SearchUserPresenter;
import cn.feihutv.zhibofeihu.ui.home.signin.SignInDialogMvpPresenter;
import cn.feihutv.zhibofeihu.ui.home.signin.SignInDialogMvpView;
import cn.feihutv.zhibofeihu.ui.home.signin.SignInDialogPresenter;
import cn.feihutv.zhibofeihu.ui.live.ChatLiveMvpPresenter;
import cn.feihutv.zhibofeihu.ui.live.ChatLiveMvpView;
import cn.feihutv.zhibofeihu.ui.live.ChatLivePresenter;
import cn.feihutv.zhibofeihu.ui.live.ChatPlayFragmentMvpPresenter;
import cn.feihutv.zhibofeihu.ui.live.ChatPlayFragmentMvpView;
import cn.feihutv.zhibofeihu.ui.live.ChatPlayFragmentPresenter;
import cn.feihutv.zhibofeihu.ui.live.PLVideoViewMvpPresenter;
import cn.feihutv.zhibofeihu.ui.live.PLVideoViewMvpView;
import cn.feihutv.zhibofeihu.ui.live.PLVideoViewPresenter;
import cn.feihutv.zhibofeihu.ui.live.RankContriMvpPresenter;
import cn.feihutv.zhibofeihu.ui.live.RankContriMvpView;
import cn.feihutv.zhibofeihu.ui.live.RankContriPresenter;
import cn.feihutv.zhibofeihu.ui.live.ReportMvpPresenter;
import cn.feihutv.zhibofeihu.ui.live.ReportMvpView;
import cn.feihutv.zhibofeihu.ui.live.ReportPresenter;
import cn.feihutv.zhibofeihu.ui.live.SWCameraStreamingMvpPresenter;
import cn.feihutv.zhibofeihu.ui.live.SWCameraStreamingMvpView;
import cn.feihutv.zhibofeihu.ui.live.SWCameraStreamingPresenter;
import cn.feihutv.zhibofeihu.ui.live.UserOnlineFragmentMvpPresenter;
import cn.feihutv.zhibofeihu.ui.live.UserOnlineFragmentMvpView;
import cn.feihutv.zhibofeihu.ui.live.UserOnlineFragmentPresenter;
import cn.feihutv.zhibofeihu.ui.live.pclive.LiveChatFragmentMvpPresenter;
import cn.feihutv.zhibofeihu.ui.live.pclive.LiveChatFragmentMvpView;
import cn.feihutv.zhibofeihu.ui.live.pclive.LiveChatFragmentPresenter;
import cn.feihutv.zhibofeihu.ui.live.pclive.LivePlayInPCMvpPresenter;
import cn.feihutv.zhibofeihu.ui.live.pclive.LivePlayInPCMvpView;
import cn.feihutv.zhibofeihu.ui.live.pclive.LivePlayInPCPresenter;
import cn.feihutv.zhibofeihu.ui.live.renzheng.IdentityInfoMvpPresenter;
import cn.feihutv.zhibofeihu.ui.live.renzheng.IdentityInfoMvpView;
import cn.feihutv.zhibofeihu.ui.live.renzheng.IdentityInfoPresenter;
import cn.feihutv.zhibofeihu.ui.live.renzheng.RenzhengSuccessMvpPresenter;
import cn.feihutv.zhibofeihu.ui.live.renzheng.RenzhengSuccessMvpView;
import cn.feihutv.zhibofeihu.ui.live.renzheng.RenzhengSuccessPresenter;
import cn.feihutv.zhibofeihu.ui.live.renzheng.UploadFailureMvpPresenter;
import cn.feihutv.zhibofeihu.ui.live.renzheng.UploadFailureMvpView;
import cn.feihutv.zhibofeihu.ui.live.renzheng.UploadFailurePresenter;
import cn.feihutv.zhibofeihu.ui.live.renzheng.UploadSuccessMvpPresenter;
import cn.feihutv.zhibofeihu.ui.live.renzheng.UploadSuccessMvpView;
import cn.feihutv.zhibofeihu.ui.live.renzheng.UploadSuccessPresenter;
import cn.feihutv.zhibofeihu.ui.main.MainMvpPresenter;
import cn.feihutv.zhibofeihu.ui.main.MainMvpView;
import cn.feihutv.zhibofeihu.ui.main.MainPresenter;
import cn.feihutv.zhibofeihu.ui.me.ContributionListMvpPresenter;
import cn.feihutv.zhibofeihu.ui.me.ContributionListMvpView;
import cn.feihutv.zhibofeihu.ui.me.ContributionListPresenter;
import cn.feihutv.zhibofeihu.ui.me.HisGuardMvpPresenter;
import cn.feihutv.zhibofeihu.ui.me.HisGuardMvpView;
import cn.feihutv.zhibofeihu.ui.me.HisGuardPresenter;
import cn.feihutv.zhibofeihu.ui.me.MyMvpPresenter;
import cn.feihutv.zhibofeihu.ui.me.MyMvpView;
import cn.feihutv.zhibofeihu.ui.me.MyPresenter;
import cn.feihutv.zhibofeihu.ui.me.concern.ConcernMvpPresenter;
import cn.feihutv.zhibofeihu.ui.me.concern.ConcernMvpView;
import cn.feihutv.zhibofeihu.ui.me.concern.ConcernPresenter;
import cn.feihutv.zhibofeihu.ui.me.concern.FansMvpPresenter;
import cn.feihutv.zhibofeihu.ui.me.concern.FansMvpView;
import cn.feihutv.zhibofeihu.ui.me.concern.FansPresenter;
import cn.feihutv.zhibofeihu.ui.me.dowm.MyDwomMvpPresenter;
import cn.feihutv.zhibofeihu.ui.me.dowm.MyDwomMvpView;
import cn.feihutv.zhibofeihu.ui.me.dowm.MyDwomPresenter;
import cn.feihutv.zhibofeihu.ui.me.dynamic.DynamicDetailMvpPresenter;
import cn.feihutv.zhibofeihu.ui.me.dynamic.DynamicDetailMvpView;
import cn.feihutv.zhibofeihu.ui.me.dynamic.DynamicDetailPresenter;
import cn.feihutv.zhibofeihu.ui.me.dynamic.MyDynamicMvpPresenter;
import cn.feihutv.zhibofeihu.ui.me.dynamic.MyDynamicMvpView;
import cn.feihutv.zhibofeihu.ui.me.dynamic.MyDynamicPresenter;
import cn.feihutv.zhibofeihu.ui.me.dynamic.OtherPageMvpPresenter;
import cn.feihutv.zhibofeihu.ui.me.dynamic.OtherPageMvpView;
import cn.feihutv.zhibofeihu.ui.me.dynamic.OtherPagePresenter;
import cn.feihutv.zhibofeihu.ui.me.dynamic.OthersCommunityMvpPresenter;
import cn.feihutv.zhibofeihu.ui.me.dynamic.OthersCommunityMvpView;
import cn.feihutv.zhibofeihu.ui.me.dynamic.OthersCommunityPresenter;
import cn.feihutv.zhibofeihu.ui.me.dynamic.SendDynamicMvpPresenter;
import cn.feihutv.zhibofeihu.ui.me.dynamic.SendDynamicMvpView;
import cn.feihutv.zhibofeihu.ui.me.dynamic.SendDynamicPresenter;
import cn.feihutv.zhibofeihu.ui.me.encash.IncomeMvpPresenter;
import cn.feihutv.zhibofeihu.ui.me.encash.IncomeMvpView;
import cn.feihutv.zhibofeihu.ui.me.encash.IncomePresenter;
import cn.feihutv.zhibofeihu.ui.me.encash.MyEarningsMvpPresenter;
import cn.feihutv.zhibofeihu.ui.me.encash.MyEarningsMvpView;
import cn.feihutv.zhibofeihu.ui.me.encash.MyEarningsPresenter;
import cn.feihutv.zhibofeihu.ui.me.encash.WithdrawalrecordMvpPresenter;
import cn.feihutv.zhibofeihu.ui.me.encash.WithdrawalrecordMvpView;
import cn.feihutv.zhibofeihu.ui.me.encash.WithdrawalrecordPresenter;
import cn.feihutv.zhibofeihu.ui.me.guard.GuardListMvpPresenter;
import cn.feihutv.zhibofeihu.ui.me.guard.GuardListMvpView;
import cn.feihutv.zhibofeihu.ui.me.guard.GuardListPresenter;
import cn.feihutv.zhibofeihu.ui.me.guard.GuardsMvpPresenter;
import cn.feihutv.zhibofeihu.ui.me.guard.GuardsMvpView;
import cn.feihutv.zhibofeihu.ui.me.guard.GuardsPresenter;
import cn.feihutv.zhibofeihu.ui.me.mv.MyMvProductionMvpPresenter;
import cn.feihutv.zhibofeihu.ui.me.mv.MyMvProductionMvpView;
import cn.feihutv.zhibofeihu.ui.me.mv.MyMvProductionPresenter;
import cn.feihutv.zhibofeihu.ui.me.personalInfo.PersonalInfoMvpPresenter;
import cn.feihutv.zhibofeihu.ui.me.personalInfo.PersonalInfoMvpView;
import cn.feihutv.zhibofeihu.ui.me.personalInfo.PersonalInfoPresenter;
import cn.feihutv.zhibofeihu.ui.me.recharge.PrepaidrecordsMvpPresenter;
import cn.feihutv.zhibofeihu.ui.me.recharge.PrepaidrecordsMvpView;
import cn.feihutv.zhibofeihu.ui.me.recharge.PrepaidrecordsPresenter;
import cn.feihutv.zhibofeihu.ui.me.recharge.RechargeMvpPresenter;
import cn.feihutv.zhibofeihu.ui.me.recharge.RechargeMvpView;
import cn.feihutv.zhibofeihu.ui.me.recharge.RechargePresenter;
import cn.feihutv.zhibofeihu.ui.me.roommrgs.RoomMrgsMvpPresenter;
import cn.feihutv.zhibofeihu.ui.me.roommrgs.RoomMrgsMvpView;
import cn.feihutv.zhibofeihu.ui.me.roommrgs.RoomMrgsPresenter;
import cn.feihutv.zhibofeihu.ui.me.setting.BlacklistMvpPresenter;
import cn.feihutv.zhibofeihu.ui.me.setting.BlacklistMvpView;
import cn.feihutv.zhibofeihu.ui.me.setting.BlacklistPresenter;
import cn.feihutv.zhibofeihu.ui.me.setting.MsgReceSettingsMvpPresenter;
import cn.feihutv.zhibofeihu.ui.me.setting.MsgReceSettingsMvpView;
import cn.feihutv.zhibofeihu.ui.me.setting.MsgReceSettingsPresenter;
import cn.feihutv.zhibofeihu.ui.me.shop.BeautiMvpPresenter;
import cn.feihutv.zhibofeihu.ui.me.shop.BeautiMvpView;
import cn.feihutv.zhibofeihu.ui.me.shop.BeautiPresenter;
import cn.feihutv.zhibofeihu.ui.me.shop.CarMvpPresenter;
import cn.feihutv.zhibofeihu.ui.me.shop.CarMvpView;
import cn.feihutv.zhibofeihu.ui.me.shop.CarPresenter;
import cn.feihutv.zhibofeihu.ui.me.shop.GuardShopMvpPresenter;
import cn.feihutv.zhibofeihu.ui.me.shop.GuardShopMvpView;
import cn.feihutv.zhibofeihu.ui.me.shop.GuardShopPresenter;
import cn.feihutv.zhibofeihu.ui.me.vip.MyReceiveMvpPresenter;
import cn.feihutv.zhibofeihu.ui.me.vip.MyReceiveMvpView;
import cn.feihutv.zhibofeihu.ui.me.vip.MyReceivePresenter;
import cn.feihutv.zhibofeihu.ui.me.vip.MySendMvpPresenter;
import cn.feihutv.zhibofeihu.ui.me.vip.MySendMvpView;
import cn.feihutv.zhibofeihu.ui.me.vip.MySendPresenter;
import cn.feihutv.zhibofeihu.ui.me.vip.MyVipMvpPresenter;
import cn.feihutv.zhibofeihu.ui.me.vip.MyVipMvpView;
import cn.feihutv.zhibofeihu.ui.me.vip.MyVipPresenter;
import cn.feihutv.zhibofeihu.ui.mv.DemandSquareMvpPresenter;
import cn.feihutv.zhibofeihu.ui.mv.DemandSquareMvpView;
import cn.feihutv.zhibofeihu.ui.mv.DemandSquarePresenter;
import cn.feihutv.zhibofeihu.ui.mv.MvSquareMvpPresenter;
import cn.feihutv.zhibofeihu.ui.mv.MvSquareMvpView;
import cn.feihutv.zhibofeihu.ui.mv.MvSquarePresenter;
import cn.feihutv.zhibofeihu.ui.mv.MvVideoDetailsMvpPresenter;
import cn.feihutv.zhibofeihu.ui.mv.MvVideoDetailsMvpView;
import cn.feihutv.zhibofeihu.ui.mv.MvVideoDetailsPresenter;
import cn.feihutv.zhibofeihu.ui.mv.MvVideoListMvpPresenter;
import cn.feihutv.zhibofeihu.ui.mv.MvVideoListMvpView;
import cn.feihutv.zhibofeihu.ui.mv.MvVideoListPresenter;
import cn.feihutv.zhibofeihu.ui.mv.MyMvGiftMvpPresenter;
import cn.feihutv.zhibofeihu.ui.mv.MyMvGiftMvpView;
import cn.feihutv.zhibofeihu.ui.mv.MyMvGiftPresenter;
import cn.feihutv.zhibofeihu.ui.mv.demand.BuyMvDemandMvpPresenter;
import cn.feihutv.zhibofeihu.ui.mv.demand.BuyMvDemandMvpView;
import cn.feihutv.zhibofeihu.ui.mv.demand.BuyMvDemandPresenter;
import cn.feihutv.zhibofeihu.ui.mv.demand.MyDemandMvpPresenter;
import cn.feihutv.zhibofeihu.ui.mv.demand.MyDemandMvpView;
import cn.feihutv.zhibofeihu.ui.mv.demand.MyDemandPageMvpPresenter;
import cn.feihutv.zhibofeihu.ui.mv.demand.MyDemandPageMvpView;
import cn.feihutv.zhibofeihu.ui.mv.demand.MyDemandPagePresenter;
import cn.feihutv.zhibofeihu.ui.mv.demand.MyDemandPresenter;
import cn.feihutv.zhibofeihu.ui.mv.demand.MyMvCollectMvpPresenter;
import cn.feihutv.zhibofeihu.ui.mv.demand.MyMvCollectMvpView;
import cn.feihutv.zhibofeihu.ui.mv.demand.MyMvCollectPresenter;
import cn.feihutv.zhibofeihu.ui.mv.demand.PostDemandMvpPresenter;
import cn.feihutv.zhibofeihu.ui.mv.demand.PostDemandMvpView;
import cn.feihutv.zhibofeihu.ui.mv.demand.PostDemandPresenter;
import cn.feihutv.zhibofeihu.ui.mv.demand.PostMvVideoMvpPresenter;
import cn.feihutv.zhibofeihu.ui.mv.demand.PostMvVideoMvpView;
import cn.feihutv.zhibofeihu.ui.mv.demand.PostMvVideoPresenter;
import cn.feihutv.zhibofeihu.ui.user.forgetPsd.ForgetPsdMvpPresenter;
import cn.feihutv.zhibofeihu.ui.user.forgetPsd.ForgetPsdMvpView;
import cn.feihutv.zhibofeihu.ui.user.forgetPsd.ForgetPsdPresenter;
import cn.feihutv.zhibofeihu.ui.user.login.LoginMvpPresenter;
import cn.feihutv.zhibofeihu.ui.user.login.LoginMvpView;
import cn.feihutv.zhibofeihu.ui.user.login.LoginPresenter;
import cn.feihutv.zhibofeihu.ui.user.phoneLogin.PhoneLoginMvpPresenter;
import cn.feihutv.zhibofeihu.ui.user.phoneLogin.PhoneLoginMvpView;
import cn.feihutv.zhibofeihu.ui.user.phoneLogin.PhoneLoginPresenter;
import cn.feihutv.zhibofeihu.ui.user.register.RegisterMvpPresenter;
import cn.feihutv.zhibofeihu.ui.user.register.RegisterMvpView;
import cn.feihutv.zhibofeihu.ui.user.register.RegisterPresenter;
import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/09/25
 *     desc   : dagger2 注入  定义注入实现 相当于 new 某个对象实现
 *     version: 1.0
 * </pre>
 */

@Module
public class ActivityModule {

    private AppCompatActivity mActivity;

    public ActivityModule(AppCompatActivity activity) {
        this.mActivity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    AppCompatActivity provideActivity() {
        return mActivity;
    }

    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }


    @Provides
    @PerActivity
    SplashMvpPresenter<SplashMvpView> provideSplashPresenter(
            SplashPresenter<SplashMvpView> presenter) {
        return presenter;
    }


    @Provides
    @PerActivity
    LoginMvpPresenter<LoginMvpView> provideLoginPresenter(
            LoginPresenter<LoginMvpView> presenter) {
        return presenter;
    }


    @Provides
    @PerActivity
    SWCameraStreamingMvpPresenter<SWCameraStreamingMvpView> provideSWCameraStreamingPresenter(
            SWCameraStreamingPresenter<SWCameraStreamingMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    UploadFailureMvpPresenter<UploadFailureMvpView> provideUploadFailurePresenter(
            UploadFailurePresenter<UploadFailureMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    IdentityInfoMvpPresenter<IdentityInfoMvpView> provideIdentityInfoPresenter(
            IdentityInfoPresenter<IdentityInfoMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    RenzhengSuccessMvpPresenter<RenzhengSuccessMvpView> provideRenzhengSuccessPresenter(
            RenzhengSuccessPresenter<RenzhengSuccessMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    UploadSuccessMvpPresenter<UploadSuccessMvpView> provideUploadSuccessPresenter(
            UploadSuccessPresenter<UploadSuccessMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    PhoneLoginMvpPresenter<PhoneLoginMvpView> providePhoneLoginPresenter(
            PhoneLoginPresenter<PhoneLoginMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    MainMvpPresenter<MainMvpView> provideMainPresenter(
            MainPresenter<MainMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    ForgetPsdMvpPresenter<ForgetPsdMvpView> provideForgetPsdPresenter(
            ForgetPsdPresenter<ForgetPsdMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    RegisterMvpPresenter<RegisterMvpView> provideRegisterPresenter(
            RegisterPresenter<RegisterMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    HomeMvpPresenter<HomeMvpView> provideHomePresenter(
            HomePresenter<HomeMvpView> presenter) {
        return presenter;
    }

    @Provides
    LinearLayoutManager provideLinearLayoutManager(AppCompatActivity activity) {
        return new LinearLayoutManager(activity);
    }

    @Provides
    SignInDialogMvpPresenter<SignInDialogMvpView> provideSignInDialogPresenter(
            SignInDialogPresenter<SignInDialogMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    ChatLiveMvpPresenter<ChatLiveMvpView> provideChatLivePresenter(
            ChatLivePresenter<ChatLiveMvpView> presenter) {
        return presenter;
    }

    @Provides
//    @PerActivity
    RankContriMvpPresenter<RankContriMvpView> provideRankContriPresenter(
            RankContriPresenter<RankContriMvpView> presenter) {
        return presenter;
    }

    @Provides
    UserOnlineFragmentMvpPresenter<UserOnlineFragmentMvpView> provideUserOnlineFragmentPresenter(
            UserOnlineFragmentPresenter<UserOnlineFragmentMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    RechargeMvpPresenter<RechargeMvpView> provideRechargePresenter(
            RechargePresenter<RechargeMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    HistoryMvpPresenter<HistoryMvpView> provideHistoryPresenter(
            HistoryPresenter<HistoryMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    SearchUserMvpPresenter<SearchUserMvpView> provideSearchUserPresenter(
            SearchUserPresenter<SearchUserMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    PLVideoViewMvpPresenter<PLVideoViewMvpView> providePLVideoViewPresenter(
            PLVideoViewPresenter<PLVideoViewMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    ChatPlayFragmentMvpPresenter<ChatPlayFragmentMvpView> provideChatPlayFragmentPresenter(
            ChatPlayFragmentPresenter<ChatPlayFragmentMvpView> presenter) {
        return presenter;
    }

    @Provides
    RankingMvpPresenter<RankingMvpView> provideRankingPresenter(
            RankingPresenter<RankingMvpView> presenter) {
        return presenter;
    }

    @Provides
    ConMvpPresenter<ConMvpView> provideConPresenter(
            ConPresenter<ConMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    LivePlayInPCMvpPresenter<LivePlayInPCMvpView> provideLivePlayInPCPresenter(
            LivePlayInPCPresenter<LivePlayInPCMvpView> presenter) {
        return presenter;
    }

    @Provides
    LiveChatFragmentMvpPresenter<LiveChatFragmentMvpView> provideLiveChatFragmentPresenter(
            LiveChatFragmentPresenter<LiveChatFragmentMvpView> presenter) {
        return presenter;
    }

    @Provides
    DayListMvpPresenter<DayListMvpView> provideDayListPresenter(
            DayListPresenter<DayListMvpView> presenter) {
        return presenter;
    }

    @Provides
    TotalListMvpPresenter<TotalListMvpView> provideTotalListPresenter(
            TotalListPresenter<TotalListMvpView> presenter) {
        return presenter;
    }

    @Provides
    GuardMvpPresenter<GuardMvpView> provideGuardPresenter(
            GuardPresenter<GuardMvpView> presenter) {
        return presenter;
    }

    @Provides
    MyMvpPresenter<MyMvpView> provideMyPresenter(
            MyPresenter<MyMvpView> presenter) {
        return presenter;
    }


    @Provides
    DynamicDetailMvpPresenter<DynamicDetailMvpView> provideDynamicDetailPresenter(
            DynamicDetailPresenter<DynamicDetailMvpView> presenter) {
        return presenter;
    }

    @Provides
    OthersCommunityMvpPresenter<OthersCommunityMvpView> provideOthersCommunityPresenter(
            OthersCommunityPresenter<OthersCommunityMvpView> presenter) {
        return presenter;
    }

    @Provides
    SendDynamicMvpPresenter<SendDynamicMvpView> provideSendDynamicPresenter(
            SendDynamicPresenter<SendDynamicMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    PersonalInfoMvpPresenter<PersonalInfoMvpView> providePersonalInfoPresenter(
            PersonalInfoPresenter<PersonalInfoMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    ConcernMvpPresenter<ConcernMvpView> provideConcernPresenter(
            ConcernPresenter<ConcernMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    ReportMvpPresenter<ReportMvpView> provideReportPresenter(
            ReportPresenter<ReportMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    FansMvpPresenter<FansMvpView> provideFansPresenter(
            FansPresenter<FansMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    PrepaidrecordsMvpPresenter<PrepaidrecordsMvpView> providePrepaidrecordsPresenter(
            PrepaidrecordsPresenter<PrepaidrecordsMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    IncomeMvpPresenter<IncomeMvpView> provideIncomePresenter(
            IncomePresenter<IncomeMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    MyEarningsMvpPresenter<MyEarningsMvpView> provideMyEarningsPresenter(
            MyEarningsPresenter<MyEarningsMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    WithdrawalrecordMvpPresenter<WithdrawalrecordMvpView> provideWithdrawalrecordPresenter(
            WithdrawalrecordPresenter<WithdrawalrecordMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    RoomMrgsMvpPresenter<RoomMrgsMvpView> provideRoomMrgsPresenter(
            RoomMrgsPresenter<RoomMrgsMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    BlacklistMvpPresenter<BlacklistMvpView> provideBlacklistPresenter(
            BlacklistPresenter<BlacklistMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    ContributionListMvpPresenter<ContributionListMvpView> provideContributionListPresenter(
            ContributionListPresenter<ContributionListMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    GuardsMvpPresenter<GuardsMvpView> provideGuardsPresenter(
            GuardsPresenter<GuardsMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    GuardListMvpPresenter<GuardListMvpView> provideGuardListPresenter(
            GuardListPresenter<GuardListMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    GuardShopMvpPresenter<GuardShopMvpView> provideGuardShopPresenter(
            GuardShopPresenter<GuardShopMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    CarMvpPresenter<CarMvpView> provideCarPresenter(
            CarPresenter<CarMvpView> presenter) {
        return presenter;
    }


    @Provides
    @PerActivity
    BeautiMvpPresenter<BeautiMvpView> provideBeautiPresenter(
            BeautiPresenter<BeautiMvpView> presenter) {
        return presenter;
    }


    @Provides
    MvSquareMvpPresenter<MvSquareMvpView> provideMvSquarePresenter(
            MvSquarePresenter<MvSquareMvpView> presenter) {
        return presenter;
    }

    @Provides
    DemandSquareMvpPresenter<DemandSquareMvpView> provideDemandSquarePresenter(
            DemandSquarePresenter<DemandSquareMvpView> presenter) {
        return presenter;
    }

    @Provides
    MvVideoListMvpPresenter<MvVideoListMvpView> provideMvVideoListPresenter(
            MvVideoListPresenter<MvVideoListMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    MyVipMvpPresenter<MyVipMvpView> proMyVipPresenter(
            MyVipPresenter<MyVipMvpView> presenter) {
        return presenter;
    }

    @Provides
    MyDemandMvpPresenter<MyDemandMvpView> provideMyDemandPresenter(
            MyDemandPresenter<MyDemandMvpView> presenter) {
        return presenter;
    }


    @Provides
    MyDemandPageMvpPresenter<MyDemandPageMvpView> provideMyDemandPagePresenter(
            MyDemandPagePresenter<MyDemandPageMvpView> presenter) {
        return presenter;
    }


    @Provides
    PostDemandMvpPresenter<PostDemandMvpView> providePostDemandPresenter(
            PostDemandPresenter<PostDemandMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    MyReceiveMvpPresenter<MyReceiveMvpView> proMyReceivePresenter(
            MyReceivePresenter<MyReceiveMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    MySendMvpPresenter<MySendMvpView> proMySendPresenter(
            MySendPresenter<MySendMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    PostMvVideoMvpPresenter<PostMvVideoMvpView> providePostMvVideoPresenter(
            PostMvVideoPresenter<PostMvVideoMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    MvVideoDetailsMvpPresenter<MvVideoDetailsMvpView> provideMvVideoDetailsPresenter(
            MvVideoDetailsPresenter<MvVideoDetailsMvpView> presenter) {
        return presenter;
    }


    @Provides
    @PerActivity
    OtherPageMvpPresenter<OtherPageMvpView> proOtherPagePresenter(
            OtherPagePresenter<OtherPageMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    BuyMvDemandMvpPresenter<BuyMvDemandMvpView> provideBuyMvDemandPresenter(
            BuyMvDemandPresenter<BuyMvDemandMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    MsgReceSettingsMvpPresenter<MsgReceSettingsMvpView> proMsgReceSettingsPresenter(
            MsgReceSettingsPresenter<MsgReceSettingsMvpView> presenter) {
        return presenter;
    }


    @Provides
    @PerActivity
    MyMvGiftMvpPresenter<MyMvGiftMvpView> provideMyMvGiftPresenter(
            MyMvGiftPresenter<MyMvGiftMvpView> presenter) {
        return presenter;
    }


    @Provides
    @PerActivity
    MyMvCollectMvpPresenter<MyMvCollectMvpView> provideMyMvCollectPresenter(
            MyMvCollectPresenter<MyMvCollectMvpView> presenter) {
        return presenter;
    }


    @Provides
    @PerActivity
    MyMvProductionMvpPresenter<MyMvProductionMvpView> provideMyMvProductionPresenter(
            MyMvProductionPresenter<MyMvProductionMvpView> presenter) {
        return presenter;
    }


    @Provides
    @PerActivity
    HisGuardMvpPresenter<HisGuardMvpView> proHisGuardPresenter(
            HisGuardPresenter<HisGuardMvpView> presenter) {
        return presenter;
    }


    @Provides
    @PerActivity
    MyDynamicMvpPresenter<MyDynamicMvpView> proMyDynamicPresenter(
            MyDynamicPresenter<MyDynamicMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    MyDwomMvpPresenter<MyDwomMvpView> proMyDwomPresenter(
            MyDwomPresenter<MyDwomMvpView> presenter) {
        return presenter;
    }

}
