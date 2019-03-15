package cn.feihutv.zhibofeihu.ui.me.vip;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;

import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.common.WebHttpRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.WebHttpResponse;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.widget.TCActivityTitle;
import cn.feihutv.zhibofeihu.ui.widget.WebViewJavaScriptFunction;
import cn.feihutv.zhibofeihu.ui.widget.X5WebView;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.TCConstants;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/28 17:51
 *      desc   : 升级宝典
 *      version: 1.0
 * </pre>
 */

public class QuickUpgradeActivity extends BaseActivity {

    @BindView(R.id.upgrade_back)
    TCActivityTitle mTitle;

    @BindView(R.id.webView)
    FrameLayout mParent;

    private X5WebView mWebView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_quick_upgrade;
    }

    @Override
    protected void initWidget() {
        setUnBinder(ButterKnife.bind(this));
        initView();
    }

    private void initView() {
        mWebView = new X5WebView(this, null);
        mWebView.setBackgroundColor(ContextCompat.getColor(QuickUpgradeActivity.this, R.color.bg_color));
        mParent.addView(mWebView, new FrameLayout.LayoutParams(
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
                showLoading();

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideLoading();
            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, com.tencent.smtt.export.external.interfaces.SslError sslError) {
                sslErrorHandler.proceed();
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Log.d("ANDROID_LAB", "TITLE=" + title);
            }

        });
        mWebView.loadUrl(FeihuZhiboApplication.getApplication().mDataManager.getSysConfig().getUpgradeBookUrl());
        mWebView.addJavascriptInterface(new WebViewJavaScriptFunction() {

            @Override
            public void onJsFunctionCalled(String tag) {

            }

            @JavascriptInterface
            public String HbCallPhoneAPI(final String json) {
                String returnString = "";
                Log.e("print", "HbCallPhoneAPI: ------>" + json.toString());
                JSONObject aa = new JSONObject();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String apiName = jsonObject.getString("apiName");
                    AppLogger.i("print", "HbCallPhoneAPI: ------>" + apiName);
                    final String reqId = jsonObject.getString("reqId");
                    if (apiName.equals("getLogin")) {
                    } else if (apiName.equals("getVipData")) {
                        final Map<String, String> req = new HashMap<String, String>();
                        FeihuZhiboApplication.getApplication().mDataManager.doWebHttpApiCall(new WebHttpRequest(apiName, req))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<WebHttpResponse>() {
                                    @Override
                                    public void accept(@NonNull WebHttpResponse webHttpResponse) throws Exception {
                                        if (webHttpResponse.getCode() == 0) {
                                            JSONObject result = webHttpResponse.getResponse();
                                            JSONObject data = result.getJSONObject("Data");
                                            JSONObject jsonN = new JSONObject();
                                            jsonN.put("apiName", "getVipData");
                                            jsonN.put("data", data);
                                            jsonN.put("reqId", reqId);
                                            AppLogger.e("print", jsonN.toString());
                                            mWebView.loadUrl("javascript:PhoneCallHbAPI('" + jsonN.toString() + "')");
                                        }
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(@NonNull Throwable throwable) throws Exception {

                                    }
                                });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return returnString;
            }

        }, "webkit");
    }

    @Override
    protected void eventOnClick() {

        mTitle.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
