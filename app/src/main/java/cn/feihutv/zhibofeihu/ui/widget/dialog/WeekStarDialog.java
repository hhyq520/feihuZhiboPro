package cn.feihutv.zhibofeihu.ui.widget.dialog;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.BuildConfig;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadRoomResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.WebHttpRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.WebHttpResponse;
import cn.feihutv.zhibofeihu.rxbus.RxBus;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.ui.live.OnStrClickListener;
import cn.feihutv.zhibofeihu.ui.me.dynamic.OthersCommunityActivity;
import cn.feihutv.zhibofeihu.ui.me.recharge.RechargeActivity;
import cn.feihutv.zhibofeihu.ui.widget.WebViewJavaScriptFunction;
import cn.feihutv.zhibofeihu.ui.widget.X5WebView;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.AppUtils;
import cn.feihutv.zhibofeihu.utils.CustomDialogUtils;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.FloatViewWindowManger;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by huanghao on 2017/11/8.
 */

public class WeekStarDialog extends Dialog {
    @BindView(R.id.webview_container)
    FrameLayout webviewContainer;
    @BindView(R.id.title)
    TextView titleText;
    private Context mContext;
    private X5WebView mWebView;
    private boolean isGotoRoom;
    private boolean isPc;
    private String url;
    private Activity activity;
    private String content;
    private String imgUrl;
    private String targetUrl;
    public WeekStarDialog(@NonNull Context context, boolean isGotoRoom, boolean isPc, String url, Activity activity) {
        super(context, R.style.floag_dialog);
        mContext = context;
        this.isGotoRoom=isGotoRoom;
        this.isPc=isPc;
        this.url=url;
        this.activity=activity;
    }

    public interface GoRoomListener{
        void goRoom(LoadRoomResponce.LoadRoomData tcRoomInfo);
        void showLogin();
    }
    private  GoRoomListener  mListener;
    public void setOnStrClickListener(GoRoomListener listener){
        mListener=listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBus.get().register(this);
        if(isPc){
            setContentView(R.layout.week_star_pc_dialog);
        }else{
            setContentView(R.layout.week_star_dialog);
        }

        ButterKnife.bind(this);
        initview();
    }

    private void initview() {
        mWebView = new X5WebView(mContext, null);
        webviewContainer.addView(mWebView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        mWebView.getView().setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        mWebView.getView().setVerticalScrollBarEnabled(false);
        mWebView.getView().setHorizontalScrollBarEnabled(false);

        if (Build.VERSION.SDK_INT >= 11) {//用于判断是否为Android 3.0系统, 然后隐藏缩放控件
            mWebView.getSettings().setDisplayZoomControls(false);
        } else {
//            this.setZoomControlGone(mWebView.getView());// Android 3.0(11) 以下使用以下方法
        }
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onLoadResource(WebView webView, String s) {
                super.onLoadResource(webView, s);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("Loading", url);
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);


            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                sslErrorHandler.proceed();
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Log.d("ANDROID_LAB", "TITLE=" + title);
                titleText.setText(title);
            }
        });
        mWebView.loadUrl(url);
        mWebView.addJavascriptInterface(new WebViewJavaScriptFunction() {

            @Override
            public void onJsFunctionCalled(String tag) {

            }

            @JavascriptInterface
            public String HbCallPhoneAPI(final String json) {
//                data.getString("apiName")
                String returnString = "";
                Log.e("print", "HbCallPhoneAPI: ------>" + json.toString());
                JSONObject aa = new JSONObject();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String apiName = jsonObject.getString("apiName");
                    Log.e("print", "HbCallPhoneAPI: ------>" + apiName);
                    String reqId = jsonObject.getString("reqId");
                    if (apiName.equals("getLogin")) {

                    } else if (apiName.equals("showToast")) {
                        String text = jsonObject.getJSONObject("data").getString("text");
                        FHUtils.showToast(text);
                    }else if (apiName.equals("share")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        content = data.getString("content");
                        imgUrl = data.getString("imgUrl");
                        targetUrl = data.getString("targetUrl");
                        RxBus.get().send(RxBusCode.RX_BUS_NOTICE_CODE_SHARE,data);
                    } else if (apiName.equals("copyClipboard")) {
                        // 复制
                        String copyContent = jsonObject.getJSONObject("data").getString("content");
                        ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("copyContent", copyContent);
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(mContext, "复制成功!", Toast.LENGTH_SHORT).show();
                    } else if (apiName.equals("openHtml")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        boolean isSysBrowser = data.getBoolean("isSysBrowser");
                        String html = data.getString("html");
                        if (isSysBrowser) {
                            Intent intent = new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            Uri content_url = Uri.parse(html);
                            intent.setData(content_url);
                            mContext.startActivity(intent);
                        } else {
                            mWebView.loadUrl(html);
                        }
                    } else if (apiName.equals("openPay")) {
                        if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "uid").startsWith("g")) {
                            if (BuildConfig.isForceLoad.equals("1")) {
                                CustomDialogUtils.showQZLoginDialog(activity, false);
                            } else {
                                CustomDialogUtils.showLoginDialog(activity, false);
                            }
                        } else {
                            Intent intent = new Intent(mContext, RechargeActivity.class);
                            intent.putExtra("fromWhere", "banner页面");
                            mContext.startActivity(intent);
                        }
                    } else if (apiName.equals("getPostData")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        Log.e("print", "HbCallPhoneAPI: ----->" + data);
                        final String action = data.getString("action");
                        final String reqIdN = jsonObject.getString("reqId");
                        String jsonArray = data.getString("params");
                        final Map<String, String> req = new HashMap<String, String>();
                        if (jsonArray.contains("&")) {
                            String[] params = jsonArray.split("&");
                            for (int i = 0; i < params.length; i++) {
                                String target = params[i];
                                String[] reqs = target.split("=");
                                req.put(reqs[0], reqs[1]);
                            }
                        } else if (!jsonArray.contains("&") && !TextUtils.isEmpty(jsonArray)) {
                            String[] reqs = jsonArray.split("=");
                            req.put(reqs[0], reqs[1]);
                        } else {

                        }
                        FeihuZhiboApplication.getApplication().mDataManager
                                .doWebHttpApiCall(new WebHttpRequest(action, req))
                                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<WebHttpResponse>() {
                                    @Override
                                    public void accept(@io.reactivex.annotations.NonNull WebHttpResponse response) throws Exception {
                                        AppLogger.e("print", action + req.toString());
                                        JSONObject result = new JSONObject();
                                        result = response.getResponse();
                                        JSONObject jsonN = new JSONObject();
                                        try {
                                            jsonN.put("apiName", "getPostData");
                                            jsonN.put("data", result);
                                            jsonN.put("reqId", reqIdN);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        AppLogger.e("print", jsonN.toString());
                                        mWebView.loadUrl("javascript:PhoneCallHbAPI('" + jsonN.toString() + "')");
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {

                                    }
                                });

                    } else if (apiName.equals("gotoUserCenter")) {
                        if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                            if(mListener!=null){
                                mListener.showLogin();
                            }
                        } else {
                            String userId = jsonObject.getJSONObject("data").getString("userId");
                            Intent intent = new Intent(mContext, OthersCommunityActivity.class);
                            intent.putExtra("userId", userId);
                            mContext.startActivity(intent);
                        }
                    } else if (apiName.equals("gotoLiveRoom")) {
                        if (isGotoRoom) {
                            String userId = jsonObject.getJSONObject("data").getString("userId");
                            new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                                    .doGetRoomDataApiCall(new LoadRoomRequest(userId))
                                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<LoadRoomResponce>() {
                                        @Override
                                        public void accept(@io.reactivex.annotations.NonNull LoadRoomResponce loadRoomResponce) throws Exception {
                                            if (loadRoomResponce.getCode() == 0) {
                                                if(mListener!=null){
                                                    mListener.goRoom(loadRoomResponce.getLoadRoomData());
                                                }
//                                                gotoRoom(loadRoomResponce.getLoadRoomData());
                                            } else {
                                                AppLogger.e(loadRoomResponce.getErrMsg());
                                            }
                                        }
                                    }, new Consumer<Throwable>() {
                                        @Override
                                        public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {

                                        }
                                    }));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return returnString;
            }

        }, "webkit");
    }

    private void gotoRoom(final LoadRoomResponce.LoadRoomData tcRoomInfo) {
        if (tcRoomInfo == null) {
            return;
        }
        FloatViewWindowManger.removeSmallWindow();
        // 1为PC   2为手机
        AppUtils.startLiveActivity(mContext, tcRoomInfo.getRoomId(), tcRoomInfo.getHeadUrl(), tcRoomInfo.getBroadcastType(), true);
    }

    @Override
    public void dismiss() {
        if (mWebView != null) {
            mWebView.getSettings().setBuiltInZoomControls(true);
            mWebView.setVisibility(View.GONE);
            mWebView.destroy();
        }
        RxBus.get().unRegister(this);
        super.dismiss();
    }
}
