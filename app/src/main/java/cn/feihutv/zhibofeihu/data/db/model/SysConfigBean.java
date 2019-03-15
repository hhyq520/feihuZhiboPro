package cn.feihutv.zhibofeihu.data.db.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class SysConfigBean {
    @Id
    private Long id;
    private String CosGiftRootPath;
    private String CosGoodsRootPath;
    private String CosRootPath;
    private String CosTagRootPath;
    private String CosMountRootPath;
    private String CosAnimationRootPath;
    private String CosAnimtionTemplate520;
    private String CosAnimtionTemplate1314;
    private String CosAnimationLandRootPath;
    private String CosGiftTemplate1314;
    private String CosGiftTemplate520;
    private String CosAnimtionTemplate1314_android;

    /**
     * CosMountLandRootPath : https://img.feihutv.cn/mount/land/
     * PrivilegeDesc1 : 动画专属座驾，酷炫的入场方式
     * PrivilegeDesc2 : VIP专属徽章，身份的象征
     * PrivilegeDesc3 : 给你不一样的动画入场效果
     * PrivilegeDesc4 : 聊天飘屏显示，更醒目
     * PrivilegeDesc5 : 开通即赠送小喇叭
     * PrivilegeDesc6 : VIP专属特效礼物，与别人不同
     * PrivilegeDesc7 : VIP尊享下载MV特权
     * PrivilegeDesc8 : 额外每天赠送竞猜互动次数
     * PrivilegeDesc9 : 防主播场控禁言、踢出直播间
     * PrivilegeDesc10 : 赠送礼物额外增加经验值
     * CosVipIconRootPath : https://img.feihutv.cn/icon/VIP/
     * CosLuckGiftIconRootPath : https://img.feihutv.cn/icon/LuckGift/
     */

    private String CosMountLandRootPath;
    private String PrivilegeDesc1;
    private String PrivilegeDesc2;
    private String PrivilegeDesc3;
    private String PrivilegeDesc4;
    private String PrivilegeDesc5;
    private String PrivilegeDesc6;
    private String PrivilegeDesc7;
    private String PrivilegeDesc8;
    private String PrivilegeDesc9;
    private String PrivilegeDesc10;
    private String CosVipIconRootPath;
    private String CosLuckGiftIconRootPath;
    private String WeekStarHtml;
    private String MvShareUrl;
    private String MvDownUrlValidityTime;
    private String MvInstructionUrl;//mv玩法说明地址
    private Long MvIssueMinHb;//主播发布定制mv需要最小虎币数
    private String CriterionUrl; // 直播平台协议
    private String LyzbExplainUrl; // 乐瑶说明
    private String YxjcExplainUrl; // 游戏竞猜说明
    private String LuckyPoolDocUrl; // 幸运彩池
    private String RankRootPath;// 等级
    private String helpDocUrl; // 帮助说明
    private String UseragreementUrl; // 用户协议
    private String FaqUrl; // 常见问题
    private String UpgradeBookUrl; // 快速升级
    private Long  MvIssueOpenMinHb;//主播发布公开mv需要最小虎币数

    @Generated(hash = 1920531810)
    public SysConfigBean(Long id, String CosGiftRootPath, String CosGoodsRootPath,
            String CosRootPath, String CosTagRootPath, String CosMountRootPath,
            String CosAnimationRootPath, String CosAnimtionTemplate520,
            String CosAnimtionTemplate1314, String CosAnimationLandRootPath,
            String CosGiftTemplate1314, String CosGiftTemplate520,
            String CosAnimtionTemplate1314_android, String CosMountLandRootPath,
            String PrivilegeDesc1, String PrivilegeDesc2, String PrivilegeDesc3,
            String PrivilegeDesc4, String PrivilegeDesc5, String PrivilegeDesc6,
            String PrivilegeDesc7, String PrivilegeDesc8, String PrivilegeDesc9,
            String PrivilegeDesc10, String CosVipIconRootPath,
            String CosLuckGiftIconRootPath, String WeekStarHtml, String MvShareUrl,
            String MvDownUrlValidityTime, String MvInstructionUrl,
            Long MvIssueMinHb, String CriterionUrl, String LyzbExplainUrl,
            String YxjcExplainUrl, String LuckyPoolDocUrl, String RankRootPath,
            String helpDocUrl, String UseragreementUrl, String FaqUrl,
            String UpgradeBookUrl, Long MvIssueOpenMinHb) {
        this.id = id;
        this.CosGiftRootPath = CosGiftRootPath;
        this.CosGoodsRootPath = CosGoodsRootPath;
        this.CosRootPath = CosRootPath;
        this.CosTagRootPath = CosTagRootPath;
        this.CosMountRootPath = CosMountRootPath;
        this.CosAnimationRootPath = CosAnimationRootPath;
        this.CosAnimtionTemplate520 = CosAnimtionTemplate520;
        this.CosAnimtionTemplate1314 = CosAnimtionTemplate1314;
        this.CosAnimationLandRootPath = CosAnimationLandRootPath;
        this.CosGiftTemplate1314 = CosGiftTemplate1314;
        this.CosGiftTemplate520 = CosGiftTemplate520;
        this.CosAnimtionTemplate1314_android = CosAnimtionTemplate1314_android;
        this.CosMountLandRootPath = CosMountLandRootPath;
        this.PrivilegeDesc1 = PrivilegeDesc1;
        this.PrivilegeDesc2 = PrivilegeDesc2;
        this.PrivilegeDesc3 = PrivilegeDesc3;
        this.PrivilegeDesc4 = PrivilegeDesc4;
        this.PrivilegeDesc5 = PrivilegeDesc5;
        this.PrivilegeDesc6 = PrivilegeDesc6;
        this.PrivilegeDesc7 = PrivilegeDesc7;
        this.PrivilegeDesc8 = PrivilegeDesc8;
        this.PrivilegeDesc9 = PrivilegeDesc9;
        this.PrivilegeDesc10 = PrivilegeDesc10;
        this.CosVipIconRootPath = CosVipIconRootPath;
        this.CosLuckGiftIconRootPath = CosLuckGiftIconRootPath;
        this.WeekStarHtml = WeekStarHtml;
        this.MvShareUrl = MvShareUrl;
        this.MvDownUrlValidityTime = MvDownUrlValidityTime;
        this.MvInstructionUrl = MvInstructionUrl;
        this.MvIssueMinHb = MvIssueMinHb;
        this.CriterionUrl = CriterionUrl;
        this.LyzbExplainUrl = LyzbExplainUrl;
        this.YxjcExplainUrl = YxjcExplainUrl;
        this.LuckyPoolDocUrl = LuckyPoolDocUrl;
        this.RankRootPath = RankRootPath;
        this.helpDocUrl = helpDocUrl;
        this.UseragreementUrl = UseragreementUrl;
        this.FaqUrl = FaqUrl;
        this.UpgradeBookUrl = UpgradeBookUrl;
        this.MvIssueOpenMinHb = MvIssueOpenMinHb;
    }
    @Generated(hash = 564802582)
    public SysConfigBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCosGiftRootPath() {
        return this.CosGiftRootPath;
    }
    public void setCosGiftRootPath(String CosGiftRootPath) {
        this.CosGiftRootPath = CosGiftRootPath;
    }
    public String getCosGoodsRootPath() {
        return this.CosGoodsRootPath;
    }
    public void setCosGoodsRootPath(String CosGoodsRootPath) {
        this.CosGoodsRootPath = CosGoodsRootPath;
    }
    public String getCosRootPath() {
        return this.CosRootPath;
    }
    public void setCosRootPath(String CosRootPath) {
        this.CosRootPath = CosRootPath;
    }
    public String getCosTagRootPath() {
        return this.CosTagRootPath;
    }
    public void setCosTagRootPath(String CosTagRootPath) {
        this.CosTagRootPath = CosTagRootPath;
    }
    public String getCosMountRootPath() {
        return this.CosMountRootPath;
    }
    public void setCosMountRootPath(String CosMountRootPath) {
        this.CosMountRootPath = CosMountRootPath;
    }
    public String getCosAnimationRootPath() {
        return this.CosAnimationRootPath;
    }
    public void setCosAnimationRootPath(String CosAnimationRootPath) {
        this.CosAnimationRootPath = CosAnimationRootPath;
    }
    public String getCosAnimtionTemplate520() {
        return this.CosAnimtionTemplate520;
    }
    public void setCosAnimtionTemplate520(String CosAnimtionTemplate520) {
        this.CosAnimtionTemplate520 = CosAnimtionTemplate520;
    }
    public String getCosAnimtionTemplate1314() {
        return this.CosAnimtionTemplate1314;
    }
    public void setCosAnimtionTemplate1314(String CosAnimtionTemplate1314) {
        this.CosAnimtionTemplate1314 = CosAnimtionTemplate1314;
    }
    public String getCosAnimationLandRootPath() {
        return this.CosAnimationLandRootPath;
    }
    public void setCosAnimationLandRootPath(String CosAnimationLandRootPath) {
        this.CosAnimationLandRootPath = CosAnimationLandRootPath;
    }
    public String getCosGiftTemplate1314() {
        return this.CosGiftTemplate1314;
    }
    public void setCosGiftTemplate1314(String CosGiftTemplate1314) {
        this.CosGiftTemplate1314 = CosGiftTemplate1314;
    }
    public String getCosGiftTemplate520() {
        return this.CosGiftTemplate520;
    }
    public void setCosGiftTemplate520(String CosGiftTemplate520) {
        this.CosGiftTemplate520 = CosGiftTemplate520;
    }
    public String getCosAnimtionTemplate1314_android() {
        return this.CosAnimtionTemplate1314_android;
    }
    public void setCosAnimtionTemplate1314_android(
            String CosAnimtionTemplate1314_android) {
        this.CosAnimtionTemplate1314_android = CosAnimtionTemplate1314_android;
    }
    public String getCosMountLandRootPath() {
        return this.CosMountLandRootPath;
    }
    public void setCosMountLandRootPath(String CosMountLandRootPath) {
        this.CosMountLandRootPath = CosMountLandRootPath;
    }
    public String getPrivilegeDesc1() {
        return this.PrivilegeDesc1;
    }
    public void setPrivilegeDesc1(String PrivilegeDesc1) {
        this.PrivilegeDesc1 = PrivilegeDesc1;
    }
    public String getPrivilegeDesc2() {
        return this.PrivilegeDesc2;
    }
    public void setPrivilegeDesc2(String PrivilegeDesc2) {
        this.PrivilegeDesc2 = PrivilegeDesc2;
    }
    public String getPrivilegeDesc3() {
        return this.PrivilegeDesc3;
    }
    public void setPrivilegeDesc3(String PrivilegeDesc3) {
        this.PrivilegeDesc3 = PrivilegeDesc3;
    }
    public String getPrivilegeDesc4() {
        return this.PrivilegeDesc4;
    }
    public void setPrivilegeDesc4(String PrivilegeDesc4) {
        this.PrivilegeDesc4 = PrivilegeDesc4;
    }
    public String getPrivilegeDesc5() {
        return this.PrivilegeDesc5;
    }
    public void setPrivilegeDesc5(String PrivilegeDesc5) {
        this.PrivilegeDesc5 = PrivilegeDesc5;
    }
    public String getPrivilegeDesc6() {
        return this.PrivilegeDesc6;
    }
    public void setPrivilegeDesc6(String PrivilegeDesc6) {
        this.PrivilegeDesc6 = PrivilegeDesc6;
    }
    public String getPrivilegeDesc7() {
        return this.PrivilegeDesc7;
    }
    public void setPrivilegeDesc7(String PrivilegeDesc7) {
        this.PrivilegeDesc7 = PrivilegeDesc7;
    }
    public String getPrivilegeDesc8() {
        return this.PrivilegeDesc8;
    }
    public void setPrivilegeDesc8(String PrivilegeDesc8) {
        this.PrivilegeDesc8 = PrivilegeDesc8;
    }
    public String getPrivilegeDesc9() {
        return this.PrivilegeDesc9;
    }
    public void setPrivilegeDesc9(String PrivilegeDesc9) {
        this.PrivilegeDesc9 = PrivilegeDesc9;
    }
    public String getPrivilegeDesc10() {
        return this.PrivilegeDesc10;
    }
    public void setPrivilegeDesc10(String PrivilegeDesc10) {
        this.PrivilegeDesc10 = PrivilegeDesc10;
    }
    public String getCosVipIconRootPath() {
        return this.CosVipIconRootPath;
    }
    public void setCosVipIconRootPath(String CosVipIconRootPath) {
        this.CosVipIconRootPath = CosVipIconRootPath;
    }
    public String getCosLuckGiftIconRootPath() {
        return this.CosLuckGiftIconRootPath;
    }
    public void setCosLuckGiftIconRootPath(String CosLuckGiftIconRootPath) {
        this.CosLuckGiftIconRootPath = CosLuckGiftIconRootPath;
    }
    public String getWeekStarHtml() {
        return this.WeekStarHtml;
    }
    public void setWeekStarHtml(String WeekStarHtml) {
        this.WeekStarHtml = WeekStarHtml;
    }
    public String getMvShareUrl() {
        return this.MvShareUrl;
    }
    public void setMvShareUrl(String MvShareUrl) {
        this.MvShareUrl = MvShareUrl;
    }
    public String getMvDownUrlValidityTime() {
        return this.MvDownUrlValidityTime;
    }
    public void setMvDownUrlValidityTime(String MvDownUrlValidityTime) {
        this.MvDownUrlValidityTime = MvDownUrlValidityTime;
    }
    public String getMvInstructionUrl() {
        return this.MvInstructionUrl;
    }
    public void setMvInstructionUrl(String MvInstructionUrl) {
        this.MvInstructionUrl = MvInstructionUrl;
    }
    public Long getMvIssueMinHb() {
        return this.MvIssueMinHb;
    }
    public void setMvIssueMinHb(Long MvIssueMinHb) {
        this.MvIssueMinHb = MvIssueMinHb;
    }
    public String getCriterionUrl() {
        return this.CriterionUrl;
    }
    public void setCriterionUrl(String CriterionUrl) {
        this.CriterionUrl = CriterionUrl;
    }
    public String getLyzbExplainUrl() {
        return this.LyzbExplainUrl;
    }
    public void setLyzbExplainUrl(String LyzbExplainUrl) {
        this.LyzbExplainUrl = LyzbExplainUrl;
    }
    public String getYxjcExplainUrl() {
        return this.YxjcExplainUrl;
    }
    public void setYxjcExplainUrl(String YxjcExplainUrl) {
        this.YxjcExplainUrl = YxjcExplainUrl;
    }
    public String getLuckyPoolDocUrl() {
        return this.LuckyPoolDocUrl;
    }
    public void setLuckyPoolDocUrl(String LuckyPoolDocUrl) {
        this.LuckyPoolDocUrl = LuckyPoolDocUrl;
    }
    public String getRankRootPath() {
        return this.RankRootPath;
    }
    public void setRankRootPath(String RankRootPath) {
        this.RankRootPath = RankRootPath;
    }
    public String getHelpDocUrl() {
        return this.helpDocUrl;
    }
    public void setHelpDocUrl(String helpDocUrl) {
        this.helpDocUrl = helpDocUrl;
    }
    public String getUseragreementUrl() {
        return this.UseragreementUrl;
    }
    public void setUseragreementUrl(String UseragreementUrl) {
        this.UseragreementUrl = UseragreementUrl;
    }
    public String getFaqUrl() {
        return this.FaqUrl;
    }
    public void setFaqUrl(String FaqUrl) {
        this.FaqUrl = FaqUrl;
    }
    public String getUpgradeBookUrl() {
        return this.UpgradeBookUrl;
    }
    public void setUpgradeBookUrl(String UpgradeBookUrl) {
        this.UpgradeBookUrl = UpgradeBookUrl;
    }
    public Long getMvIssueOpenMinHb() {
        return this.MvIssueOpenMinHb;
    }
    public void setMvIssueOpenMinHb(Long MvIssueOpenMinHb) {
        this.MvIssueOpenMinHb = MvIssueOpenMinHb;
    }

    
}