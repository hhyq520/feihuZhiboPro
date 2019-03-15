package cn.feihutv.zhibofeihu.data.network.http.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by chenliwen on 2017/10/10 09:35.
 * 佛祖保佑，永无BUG
 */

public class LogDeviceRequest {

    @Expose
    @SerializedName("channelId")
    private String channelId;

    @Expose
    @SerializedName("isSysApp")
    private String isSysApp;

    @Expose
    @SerializedName("versionCode")
    private String versionCode;

    @Expose
    @SerializedName("versionName")
    private String versionName;

    @Expose
    @SerializedName("imei")
    private String imei;

    @Expose
    @SerializedName("imsi")
    private String imsi;

    @Expose
    @SerializedName("mac")
    private String mac;

    @Expose
    @SerializedName("androidId")
    private String androidId;

    @Expose
    @SerializedName("operation")
    private String operation;

    @Expose
    @SerializedName("phoneModel")
    private String phoneModel;

    @Expose
    @SerializedName("buildVersion")
    private String buildVersion;

    public LogDeviceRequest() {
    }

    public LogDeviceRequest(String channelId, String isSysApp, String versionCode, String versionName, String imei, String imsi, String mac, String androidId, String operation, String phoneModel, String buildVersion) {
        this.channelId = channelId;
        this.isSysApp = isSysApp;
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.imei = imei;
        this.imsi = imsi;
        this.mac = mac;
        this.androidId = androidId;
        this.operation = operation;
        this.phoneModel = phoneModel;
        this.buildVersion = buildVersion;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getIsSysApp() {
        return isSysApp;
    }

    public void setIsSysApp(String isSysApp) {
        this.isSysApp = isSysApp;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }

    public String getBuildVersion() {
        return buildVersion;
    }

    public void setBuildVersion(String buildVersion) {
        this.buildVersion = buildVersion;
    }
}
