package cn.feihutv.zhibofeihu.ui.home;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

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
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.me.dynamic.OthersCommunityActivity;
import cn.feihutv.zhibofeihu.ui.me.recharge.RechargeActivity;
import cn.feihutv.zhibofeihu.ui.widget.ImagePagerActivity;
import cn.feihutv.zhibofeihu.ui.widget.LoadingView;
import cn.feihutv.zhibofeihu.ui.widget.TCActivityTitle;
import cn.feihutv.zhibofeihu.ui.widget.WebViewJavaScriptFunction;
import cn.feihutv.zhibofeihu.ui.widget.X5WebView;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.AppUtils;
import cn.feihutv.zhibofeihu.utils.CustomDialogUtils;
import cn.feihutv.zhibofeihu.utils.FloatViewWindowManger;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.feihutv.zhibofeihu.utils.ShareUtil;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static cn.feihutv.zhibofeihu.data.prefs.AppPreferencesHelper.PREF_KEY_USERID;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/12/6 11:12
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class WebViewActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.webView)
    FrameLayout mParent;
    @BindView(R.id.back_btn)
    TCActivityTitle backBtn;

    private X5WebView mWebView;
    private String url;
    private String content;
    private String imgUrl;
    private String targetUrl;
    private LoadingView loadingView;

    @Override
    protected int getLayoutId() {
        return R.layout.webview_activity;
    }

    // 分享
    private void showLiveShareDialog() {
        final Dialog pickDialog3 = new Dialog(this, R.style.floag_dialog);
        pickDialog3.setContentView(R.layout.live_share_gray_dialog);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog3.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = (int) (display.getWidth()); //设置宽度
        pickDialog3.getWindow().setAttributes(lp);

        TextView tv_share_friends = (TextView) pickDialog3.findViewById(R.id.tv_share_friends);
        TextView tv_share_weibo = (TextView) pickDialog3.findViewById(R.id.tv_share_weibo);
        TextView tv_share_wechat = (TextView) pickDialog3.findViewById(R.id.tv_share_wechat);
        TextView tv_share_qzone = (TextView) pickDialog3.findViewById(R.id.tv_share_qzone);
        TextView tv_share_qq = (TextView) pickDialog3.findViewById(R.id.tv_share_qq);
        tv_share_friends.setOnClickListener(this);
        tv_share_weibo.setOnClickListener(this);
        tv_share_wechat.setOnClickListener(this);
        tv_share_qzone.setOnClickListener(this);
        tv_share_qq.setOnClickListener(this);
        pickDialog3.show();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onDestroy() {
        if (mWebView != null) {
            mWebView.getSettings().setBuiltInZoomControls(true);
            mWebView.setVisibility(View.GONE);
            long timeout = ViewConfiguration.getZoomControlsTimeout();//timeout ==3000
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mWebView.destroy();
                        }
                    });

                }
            }, timeout);
        }
        loadingView = null;
        super.onDestroy();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            if (mWebView != null && mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            } else {
                finish();
            }
        }
        return true;
    }


    @Override
    protected void initWidget() {
        setUnBinder(ButterKnife.bind(this));

        {
            Intent intent = getIntent();
            url = intent.getStringExtra("url");
            mWebView = new X5WebView(this, null);
            loadingView = new LoadingView();
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
//                if (loadingView != null && !loadingView.isAdded()) {
//                    loadingView.show(getSupportFragmentManager(), "");
//                }
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.add(loadingView, "");
                    ft.commitAllowingStateLoss();
                    Log.e("Loading", s);

                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
//                if (loadingView != null) {
//                    loadingView.dismiss();
//                }
                    if (loadingView != null) {
                        loadingView.dismissAllowingStateLoss();
                    }
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
                    backBtn.setTitle(title);
                }

            });
            // mWebView.loadUrl("http://img.feihutv.cn/uploadHtml/test.html");
            mWebView.loadUrl(url);
            backBtn.setReturnListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            mWebView.addJavascriptInterface(new WebViewJavaScriptFunction() {

                @Override
                public void onJsFunctionCalled(String tag) {
                    // TODO Auto-generated method stub

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

                        } else if (apiName.equals("share")) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            content = data.getString("content");
                            imgUrl = data.getString("imgUrl");
                            targetUrl = data.getString("targetUrl");
                            showLiveShareDialog();
                        } else if (apiName.equals("showToast")) {
                            String text = jsonObject.getJSONObject("data").getString("text");
                            onToast(text, Gravity.CENTER, 0, 0);
                        } else if (apiName.equals("copyClipboard")) {
                            // 复制
                            String copyContent = jsonObject.getJSONObject("data").getString("content");
                            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("copyContent", copyContent);
                            clipboard.setPrimaryClip(clip);
                            Toast.makeText(WebViewActivity.this, "复制成功!", Toast.LENGTH_SHORT).show();
                        } else if (apiName.equals("callPhoneNum")) {
                            // 打电话
                            String phoneNum = jsonObject.getJSONObject("data").getString("num");
                            if (phoneNum == null || "".equals(phoneNum.trim())) {
                                Toast.makeText(WebViewActivity.this, "没有电话号码",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));
                                if (ActivityCompat.checkSelfPermission(WebViewActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    startActivity(intent);
                                }

                            }
                        } else if (apiName.equals("showImageView")) {
                            //显示图片详情，参考动态点击图片
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONArray imgUrls = data.getJSONArray("imgUrls");
                            List<String> imgList = new ArrayList<>();
                            for (int i = 0; i < imgUrls.length(); i++) {
                                imgList.add(imgUrls.getString(i));
                                Log.e("print", "showImageView: ----->" + imgUrls.getString(i));
                            }
                            int index = data.getInt("index");
                            ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            ImagePagerActivity.startImagePagerActivity(WebViewActivity.this, imgList, index, imageSize);
                        } else if (apiName.equals("openHtml")) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            boolean isSysBrowser = data.getBoolean("isSysBrowser");
                            String html = data.getString("html");
                            if (isSysBrowser) {
                                Intent intent = new Intent();
                                intent.setAction("android.intent.action.VIEW");
                                Uri content_url = Uri.parse(html);
                                intent.setData(content_url);
                                startActivity(intent);
                            } else {
                                mWebView.loadUrl(html);
                            }
                        } else if (apiName.equals("openPay")) {
                            if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), PREF_KEY_USERID).startsWith("g")) {
                                if (BuildConfig.isForceLoad.equals("1")) {
                                    CustomDialogUtils.showQZLoginDialog(WebViewActivity.this, false);
                                } else {
                                    CustomDialogUtils.showLoginDialog(WebViewActivity.this, false);
                                }
                            } else {
                                Intent intent = new Intent(WebViewActivity.this, RechargeActivity.class);
                                intent.putExtra("fromWhere", "banner页面");
                                startActivity(intent);
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
                            if (!TCUtils.checkGuest(WebViewActivity.this)) {
                                String userId = jsonObject.getJSONObject("data").getString("userId");
                                Intent intent = new Intent(WebViewActivity.this, OthersCommunityActivity.class);
                                intent.putExtra("userId", userId);
                                startActivity(intent);
                            }
                        } else if (apiName.equals("gotoLiveRoom")) {
                            String userId = jsonObject.getJSONObject("data").getString("userId");
                            new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                                    .doGetRoomDataApiCall(new LoadRoomRequest(userId))
                                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<LoadRoomResponce>() {
                                        @Override
                                        public void accept(@io.reactivex.annotations.NonNull LoadRoomResponce loadRoomResponce) throws Exception {
                                            if (loadRoomResponce.getCode() == 0) {
                                                gotoRoom(loadRoomResponce.getLoadRoomData());
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return returnString;
                }

            }, "webkit");
        }
    }

    private void gotoRoom(LoadRoomResponce.LoadRoomData loadRoomData) {
        if (loadRoomData == null) {
            return;
        }
        FloatViewWindowManger.removeSmallWindow();
        // 1为PC   2为手机
        AppUtils.startLiveActivity(WebViewActivity.this, loadRoomData.getRoomId(), loadRoomData.getHeadUrl(), loadRoomData.getBroadcastType(), true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_share_friends:
                share(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.tv_share_weibo:
                share(SHARE_MEDIA.SINA);
                break;
            case R.id.tv_share_wechat:
                share(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.tv_share_qzone:
                share(SHARE_MEDIA.QZONE);
                break;
            case R.id.tv_share_qq:
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPerssion()) {
                        share(SHARE_MEDIA.QQ);
                    }
                } else {
                    share(SHARE_MEDIA.QQ);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private boolean checkPerssion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 需要弹出dialog让用户手动赋予权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1011);
            return false;
        } else {
            return true;
        }
    }

    private void share(SHARE_MEDIA plat) {
        ShareUtil.ShareWeb(this, content, "飞虎直播",
                imgUrl, targetUrl, plat, umShareListener);
    }

    UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            onToast("分享成功", Gravity.CENTER, 0, 0);
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            onToast("分享失败", Gravity.CENTER, 0, 0);
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {

        }
    };

}
