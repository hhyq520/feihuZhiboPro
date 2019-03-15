package cn.feihutv.zhibofeihu.data.network.http;


import com.androidnetworking.utils.ParseUtil;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import cn.feihutv.zhibofeihu.data.prefs.PreferencesHelper;
import cn.feihutv.zhibofeihu.di.ApiInfo;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.FHUtils;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/09/25
 *     desc   : http 请求头
 *     version: 1.0
 * </pre>
 */

@Singleton
public class ApiHeader {

    private ProtectedApiHeader mProtectedApiHeader;
    private PublicApiHeader mPublicApiHeader;

    @Inject
    public ApiHeader(PublicApiHeader publicApiHeader, ProtectedApiHeader protectedApiHeader) {
        mPublicApiHeader = publicApiHeader;
        mProtectedApiHeader = protectedApiHeader;
    }

    public ProtectedApiHeader getProtectedApiHeader() {
        return mProtectedApiHeader;
    }

    public PublicApiHeader getPublicApiHeader() {
        return mPublicApiHeader;
    }


    //公有请求头
    public static final class PublicApiHeader {


        @Expose
        @SerializedName("c")
        private String market;

        @Expose
        @SerializedName("d")
        private String device;

        @Expose
        @SerializedName("i")
        private String info;

        @Inject
        public PublicApiHeader(@ApiInfo String market) {
            this.market = market;
            this.device = String.valueOf(3);
        }


        public String getDevice() {
            return device;
        }

        public void setDevice(String device) {
            this.device = device;
        }

        public String getMarket() {
            return market;
        }

        public void setMarket(String market) {
            this.market = market;
        }


    }


    //登录用户执行的请求头
    public static final class ProtectedApiHeader {

        @Expose
        @SerializedName("d")
        private String device;

        @Expose
        @SerializedName("c")
        private String market;

        @Expose
        @SerializedName("t")
        private String mAccessToken;

        @Expose
        @SerializedName("uid")
        private String mUserId;

        @Expose
        @SerializedName("sig")
        private String signature;

        @Expose
        @SerializedName("i")
        private String info;//设备信息


        private transient String apiKey;

        private transient PreferencesHelper preferencesHelper;

        public ProtectedApiHeader(String device, String market, String accessToken,
                                  String userId, String apiKey, PreferencesHelper preferencesHelper,
                                  String info) {
            this.device = device;
            this.market = market;
            mAccessToken = accessToken;
            mUserId = userId;
            this.apiKey = apiKey;
            this.preferencesHelper = preferencesHelper;
            this.info=info;
        }


        //设置请求秘钥
        public void setRequestSignature(Object reqObj, String apiEndPoint) {
            //每次请求前需要重新更新下mAccessToken，因为有失效性需要获取当前的时间作为参数否则会出现签名超时错误
            long t = FHUtils.getLongTime() + preferencesHelper.getTimeChaZhi();
            setAccessToken(String.valueOf(t));

            HashMap<String, String> requestParams = ParseUtil.getParserFactory().getStringMap(this);
            requestParams.remove("sig");
            requestParams.putAll(ParseUtil.getParserFactory().getStringMap(reqObj));

            String url = FHUtils.formatUrlMap(requestParams, false, true, "");
            //截取到请求方法名称
            String apiName = apiEndPoint.substring(apiEndPoint.lastIndexOf("/") + 1, apiEndPoint.length());
            AppLogger.i("请求方法名称  --->" + apiName + " url=" + url + "  apiKey=" + this.apiKey);
            setSignature(FHUtils.getMD5(apiName + url + this.apiKey));
        }

        //设置请求秘钥
        public void setRequestSignatureV(Object reqObj, String apiEndPoint) {
            //每次请求前需要重新更新下mAccessToken，因为有失效性需要获取当前的时间作为参数否则会出现签名超时错误
            long t = FHUtils.getLongTime() + preferencesHelper.getTimeChaZhi();
            setAccessToken(String.valueOf(t));

            HashMap<String, String> requestParams = ParseUtil.getParserFactory().getStringMap(this);
            requestParams.remove("sig");
            requestParams.putAll(ParseUtil.getParserFactory().getStringMap(reqObj));

            String url = FHUtils.formatUrlMap(requestParams, false, true, "");
            //截取到请求方法名称
            AppLogger.i("请求方法名称--->" + apiEndPoint + " url=" + url + "  apiKey=" + this.apiKey);
            setSignature(FHUtils.getMD5(apiEndPoint + url + this.apiKey));
        }


        //设置web请求秘钥
        public void setWebHttpRequestSignature(Map<String, String> reqMap, String apiName) {
            //每次请求前需要重新更新下mAccessToken，因为有失效性需要获取当前的时间作为参数否则会出现签名超时错误
            long t = FHUtils.getLongTime() + preferencesHelper.getTimeChaZhi();
            setAccessToken(String.valueOf(t));

            HashMap<String, String> requestParams = ParseUtil.getParserFactory().getStringMap(this);
            requestParams.remove("sig");
            requestParams.putAll(reqMap);

            String url = FHUtils.formatUrlMap(requestParams, false, true, "");
            //截取到请求方法名称
            AppLogger.i("请求方法名称  --->" + apiName + " url=" + url + "  apiKey=" + this.apiKey);
            setSignature(FHUtils.getMD5(apiName + url + this.apiKey));
        }

        //设置特殊请求秘钥(获取验证码 --- 注册和忘记密码)
        //设置请求秘钥
        public void setRequestSignatureVericode(Object reqObj, String apiEndPoint) {
            //每次请求前需要重新更新下mAccessToken，因为有失效性需要获取当前的时间作为参数否则会出现签名超时错误
            long t = FHUtils.getLongTime() + preferencesHelper.getTimeChaZhi();
            setAccessToken(String.valueOf(t));

            HashMap<String, String> requestParams = ParseUtil.getParserFactory().getStringMap(this);
            requestParams.remove("sig");
            requestParams.putAll(ParseUtil.getParserFactory().getStringMap(reqObj));

            String url = FHUtils.formatUrlMap(requestParams, false, true, "");
            //截取到请求方法名称
            String apiName = apiEndPoint.substring(apiEndPoint.lastIndexOf("/") + 1, apiEndPoint.length());
            AppLogger.i("请求方法名称  --->" + apiName + " url=" + url + "  apiKey=" + this.apiKey);
            setSignature(FHUtils.getMD5(apiName + url + this.apiKey));
        }


        public HashMap<String, String> getRequeestParams(Object reqObj) {
            HashMap<String, String> requestParams = ParseUtil.getParserFactory().getStringMap(this);
            requestParams.putAll(ParseUtil.getParserFactory().getStringMap(reqObj));
            return requestParams;
        }


        public HashMap<String, String> getRequestParams(Map<String, String> parms) {
            HashMap<String, String> requestParams = ParseUtil.getParserFactory().getStringMap(this);
            if (parms != null) {
                requestParams.putAll(parms);
            }
            return requestParams;
        }


        public String getDevice() {
            return device;
        }

        public void setDevice(String device) {
            this.device = device;
        }

        public String getMarket() {
            return market;
        }

        public void setMarket(String market) {
            this.market = market;
        }

        public String getAccessToken() {
            return mAccessToken;
        }

        public void setAccessToken(String accessToken) {
            mAccessToken = accessToken;
        }

        public String getUserId() {
            return mUserId;
        }

        public void setUserId(String userId) {
            mUserId = userId;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }
}
