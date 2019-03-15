package cn.feihutv.zhibofeihu.data.network.http.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/09/27
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class CheckUpdateResponse extends BaseResponse{


    @Expose
    @SerializedName("Data")
    private CheckUpdateData mCheckUpdateData;

    public CheckUpdateData getCheckUpdateData() {
        return mCheckUpdateData;
    }

    public void setCheckUpdateData(CheckUpdateData checkUpdateData) {
        mCheckUpdateData = checkUpdateData;
    }

    public static class CheckUpdateData {

        @Expose
        @SerializedName("Source")
        private String Source;//http://dev.feihutv.cn/fhzb/sysdata.json,
        @Expose
        @SerializedName("registerUrl")
        private String registerUrl;// http://img.feihutv.cn/uploadHtml/register.html,
        @Expose
        @SerializedName("sysdataVersion")
        private Long sysdataVersion;//20170602203140,
        @Expose
        @SerializedName("serverTime")
        private long serverTime;//1490858286
        @Expose
        @SerializedName("versionName")
        private String versionName;//string //当前可下载最新的版本名称 如 1.0.1
        @Expose
        @SerializedName("versionDesc")
        private String versionDesc;// //本次更新内容

        @Expose
        @SerializedName("apkUrlName")
        private String apkUrlName;//http://1.0.0_1500433881.apk,//新版本下载地址
        @Expose
        @SerializedName("needVersionId")
        private int needVersionId;//  10001,//前端当前版本小于该版本强制更新，大于等于 该值时提示是否更新并可取消
        @Expose
        @SerializedName("versionId")
        private int versionId;//10001,//当前可下载最新的版本id，如果前端版本大于等于该值  不做提示，也不更新，该值为0时不做任何处理
        @Expose
        @SerializedName("openHubao")
        private int openHubao;//  int   //1开启虎宝  0关闭虎宝
        @Expose
        @SerializedName("openPlayGame")
        private int openPlayGame;//1显示玩法 0隐藏玩法
        @Expose
        @SerializedName("openPlayLimitLevel")
        private int openPlayLimitLevel;//1低于六级隐藏玩法 0玩法显示不受等级制约

        @Expose
        @SerializedName("hbNeedLevel")
        private  int hbNeedLevel;

        @Expose
        @SerializedName("hubaoUrl")
        private String hubaoUrl;

        public int getHbNeedLevel() {
            return hbNeedLevel;
        }

        public void setHbNeedLevel(int hbNeedLevel) {
            this.hbNeedLevel = hbNeedLevel;
        }

        public String getHubaoUrl() {
            return hubaoUrl;
        }

        public void setHubaoUrl(String hubaoUrl) {
            this.hubaoUrl = hubaoUrl;
        }

        public String getSource() {
            return Source;
        }

        public void setSource(String source) {
            Source = source;
        }

        public String getRegisterUrl() {
            return registerUrl;
        }

        public void setRegisterUrl(String registerUrl) {
            this.registerUrl = registerUrl;
        }

        public Long getSysdataVersion() {
            return sysdataVersion;
        }

        public void setSysdataVersion(Long sysdataVersion) {
            this.sysdataVersion = sysdataVersion;
        }

        public long getServerTime() {
            return serverTime;
        }

        public void setServerTime(long serverTime) {
            this.serverTime = serverTime;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public String getVersionDesc() {
            return versionDesc;
        }

        public void setVersionDesc(String versionDesc) {
            this.versionDesc = versionDesc;
        }

        public String getApkUrlName() {
            return apkUrlName;
        }

        public void setApkUrlName(String apkUrlName) {
            this.apkUrlName = apkUrlName;
        }

        public int getNeedVersionId() {
            return needVersionId;
        }

        public void setNeedVersionId(int needVersionId) {
            this.needVersionId = needVersionId;
        }

        public int getVersionId() {
            return versionId;
        }

        public void setVersionId(int versionId) {
            this.versionId = versionId;
        }

        public int getOpenHubao() {
            return openHubao;
        }

        public void setOpenHubao(int openHubao) {
            this.openHubao = openHubao;
        }

        public int getOpenPlayGame() {
            return openPlayGame;
        }

        public void setOpenPlayGame(int openPlayGame) {
            this.openPlayGame = openPlayGame;
        }

        public int getOpenPlayLimitLevel() {
            return openPlayLimitLevel;
        }

        public void setOpenPlayLimitLevel(int openPlayLimitLevel) {
            this.openPlayLimitLevel = openPlayLimitLevel;
        }
    }
}
