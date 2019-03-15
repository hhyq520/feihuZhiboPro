package cn.feihutv.zhibofeihu.ui.me;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lljjcoder.citypickerview.widget.CityPicker;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.umeng.analytics.MobclickAgent;
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
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.DataManager;
import cn.feihutv.zhibofeihu.data.network.http.model.common.WebHttpRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.WebHttpResponse;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.me.dynamic.OthersCommunityActivity;
import cn.feihutv.zhibofeihu.ui.me.recharge.RechargeActivity;
import cn.feihutv.zhibofeihu.ui.widget.BaskDialogFragment;
import cn.feihutv.zhibofeihu.ui.widget.ImagePagerActivity;
import cn.feihutv.zhibofeihu.ui.widget.WebViewJavaScriptFunction;
import cn.feihutv.zhibofeihu.ui.widget.X5WebView;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.feihutv.zhibofeihu.utils.ShareUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/13 20:03
 *      desc   : 虎宝页面
 *      version: 1.0
 * </pre>
 */

public class HubaoActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.webView)
    FrameLayout mParent;

    private X5WebView mWebView;
    private String content;
    private String imgUrl;
    private String targetUrl;
    //晒单
    private BaskDialogFragment baskDialogFragment;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_hubao;
    }

    @Override
    protected void initWidget() {
        setUnBinder(ButterKnife.bind(this));
        initView();
    }

    protected void initView() {
        mWebView = new X5WebView(this, null);
        mWebView.setBackgroundColor(ContextCompat.getColor(HubaoActivity.this, R.color.pupel));
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
        initHuBaoDialog();
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
                showLoading();

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                if (loadingView != null) {
//                    loadingView.dismiss();
//                }
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
        String hubaoUrl = SharePreferenceUtil.getSession(HubaoActivity.this, "hubaoUrl", "https://mm-dev.feihutv.cn/fhzb_hubao/index.html");
        mWebView.loadUrl(hubaoUrl);
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
                    DataManager dataManager = FeihuZhiboApplication.getApplication().mDataManager;
                    final String token = dataManager.getApiKey();
                    String uid = dataManager.getCurrentUserId();
                    AppLogger.i("tokenuid", token + "/" + uid);
                    JSONObject jsonObject = new JSONObject(json);
                    String apiName = jsonObject.getString("apiName");
                    AppLogger.i("print", "HbCallPhoneAPI: ------>" + apiName);
                    String reqId = jsonObject.getString("reqId");
                    long serverTime = dataManager.getTimeChaZhi() + System.currentTimeMillis() / 1000;
                    if (apiName.equals("getLogin")) {
                        JSONObject bb = new JSONObject();
                        bb.put("reqId", reqId);
                        bb.put("uid", uid);
                        bb.put("token", token);
                        bb.put("serverTime", serverTime);
                        aa.put("Code", 0);
                        aa.put("Data", bb);
                        Log.e("getLogin", aa.toString());
                        returnString = aa.toString();
                        return returnString;
                    } else if ("share".equals(apiName)) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        content = data.getString("content");
                        imgUrl = data.getString("imgUrl");
                        targetUrl = data.getString("targetUrl");
                        showShareDialog();
                    } else if (apiName.equals("showToast")) {
                        String text = jsonObject.getJSONObject("data").getString("text");
                        FHUtils.showToast(text);
                    } else if (apiName.equals("copyClipboard")) {
                        // 复制
                        String copyContent = jsonObject.getJSONObject("data").getString("content");
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("copyContent", copyContent);
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(HubaoActivity.this, "复制成功!", Toast.LENGTH_SHORT).show();
                    } else if (apiName.equals("callPhoneNum")) {
                        // 打电话
                        String phoneNum = jsonObject.getJSONObject("data").getString("num");
                        if (phoneNum == null || "".equals(phoneNum.trim())) {
                            Toast.makeText(HubaoActivity.this, "没有电话号码",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));
                            if (ActivityCompat.checkSelfPermission(HubaoActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
                        ImagePagerActivity.startImagePagerActivity(HubaoActivity.this, imgList, index, imageSize);
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
                        Intent intent = new Intent(HubaoActivity.this, RechargeActivity.class);
                        intent.putExtra("fromWhere", "banner页面");
                        startActivity(intent);
                    } else if (apiName.equals("getPostData")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        Log.e("print", "HbCallPhoneAPI: ----->" + data);
                        String action = data.getString("action");
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
                        FeihuZhiboApplication.getApplication().mDataManager.doWebHttpApiCall(new WebHttpRequest(action, req))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<WebHttpResponse>() {
                                    @Override
                                    public void accept(@NonNull WebHttpResponse webHttpResponse) throws Exception {
                                        if (webHttpResponse.getCode() == 0) {
                                            JSONObject result = webHttpResponse.getResponse();
                                            JSONObject jsonN = new JSONObject();
                                            jsonN.put("apiName", "getPostData");
                                            jsonN.put("data", result);
                                            jsonN.put("reqId", reqIdN);
                                        }
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(@NonNull Throwable throwable) throws Exception {

                                    }
                                });
                    } else if (apiName.equals("gotoUserCenter")) {
                        String userId = jsonObject.getJSONObject("data").getString("userId");
                        Intent intent = new Intent(HubaoActivity.this, OthersCommunityActivity.class);
                        intent.putExtra("userId", userId);
                        startActivity(intent);
                    } else if (apiName.equals("needLogin")) {
                        try {
                            JSONObject jsonN = new JSONObject();
                            jsonN.put("apiName", "networkSucess");
                            JSONObject a1 = new JSONObject();
                            a1.put("uid", uid);
                            a1.put("token", token);
                            jsonN.put("data", a1);
                            final String extraUrl = jsonN.toString();
                            if (mWebView != null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mWebView.loadUrl("javascript:PhoneCallHbAPI('" + extraUrl + "')");
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (apiName.equals("closeHubao")) {
                        finish();
                    } else if (apiName.equals("uploadMobclickAgent")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        String agentId = data.getString("agentId");
                        MobclickAgent.onEvent(HubaoActivity.this, agentId);
                    } else if (apiName.equals("chooseArea")) {
                        chooseAddress();
                    } else if (apiName.equals("callPhoneNum")) {
                        if (checkCallPerssion()) {
                            // 打电话
                            String phoneNum = jsonObject.getJSONObject("data").getString("num");
                            if (phoneNum == null || "".equals(phoneNum.trim())) {
                                onToast("没有电话号码", Gravity.CENTER, 0, 0);
                            } else {
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));
                                startActivity(intent);
                            }
                        } else {
                            FHUtils.showToast("请前往设置手动打开拨打电话权限！");
                        }
                    } else if (apiName.equals("showSharePoup")) {
//                        String hubaoIssueNo = jsonObject.getJSONObject("data").getString("hubaoIssueNo");
//                        if (baskDialogFragment != null) {
//                            baskDialogFragment.setHubaoIssueNo(hubaoIssueNo);
//                            baskDialogFragment.show(getSupportFragmentManager(), "");
//                        }
                        // 晒单
//                        showHuBaoDialog(hubaoIssueNo);
//
//                        Message message = new Message();
//                        message.what = 3;
//                        message.obj = hubaoIssueNo;
//                        handler.sendMessage(message);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return returnString;
            }

        }, "webkit");
    }

    public void initHuBaoDialog() {
        if (baskDialogFragment == null) {
            baskDialogFragment = new BaskDialogFragment();
        }
//        baskDialogFragment.setHubaoIssueNo(hubaoIssueNo);
//        baskDialogFragment.show(getChildFragmentManager(), "");
        baskDialogFragment.setOnCancelDialogListener(new BaskDialogFragment.OnCancelDialogListener() {
            @Override
            public void cancel() {
                mWebView.loadUrl("javascript:PhoneCallHbAPI('" + "{\"apiName\":\"ShareSucess\",\"data\":{\"sucess\": 1}}" + "')");
            }
        });
    }

    private boolean checkCallPerssion() {
        if (ActivityCompat.checkSelfPermission(HubaoActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // 需要弹出dialog让用户手动赋予权限
            ActivityCompat.requestPermissions(HubaoActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1012);
            return false;
        } else {
            return true;
        }
    }

    public String area(String address) {
        String json = "{\"apiName\":\"showArea\",\"data\":{\"area\":\" " + address + "\"}}";
        return json;
    }


    private void chooseAddress() {
        CityPicker cityPicker = new CityPicker.Builder(this)
                .textSize(14)
                .title("地址选择")
                .backgroundPop(Color.parseColor("#dbdbdd"))
                .titleBackgroundColor("#dbdbdd")
                .titleTextColor("#a0a0a0")
                .backgroundPop(0xa0000000)
                .confirTextColor("#303030")
                .cancelTextColor("#303030")
                .province("江苏省")
                .city("常州市")
                .district("天宁区")
                .textColor(Color.parseColor("#303030"))
                .provinceCyclic(true)
                .cityCyclic(false)
                .districtCyclic(false)
                .visibleItemsCount(7)
                .itemPadding(14)
                .onlyShowProvinceAndCity(false)
                .build();
        cityPicker.show();

        //监听方法，获取选择结果
        cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省份
                final String province = citySelected[0];
                //城市
                final String city = citySelected[1];
                //区县（如果设定了两级联动，那么该项返回空）
                final String district = citySelected[2];
                //邮编
                String code = citySelected[3];
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mWebView.loadUrl("javascript:PhoneCallHbAPI('" + area(province + city + district) + "')");
                    }
                });

            }

            @Override
            public void onCancel() {
            }
        });
    }

    // 分享
    private void showShareDialog() {
        final Dialog pickDialog3 = new Dialog(this, R.style.floag_dialog);
        pickDialog3.setContentView(R.layout.share_dialog);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog3.getWindow();
        dlgwin.setWindowAnimations(R.style.anim);
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.BOTTOM);
        lp.width = (int) (display.getWidth()); //设置宽度
        pickDialog3.getWindow().setAttributes(lp);

        ImageView tv_share_friends = (ImageView) pickDialog3.findViewById(R.id.btn_share_circle);
        ImageView tv_share_weibo = (ImageView) pickDialog3.findViewById(R.id.btn_share_wb);
        ImageView tv_share_wechat = (ImageView) pickDialog3.findViewById(R.id.btn_share_wx);
        ImageView tv_share_qzone = (ImageView) pickDialog3.findViewById(R.id.btn_share_qzone);
        ImageView tv_share_qq = (ImageView) pickDialog3.findViewById(R.id.btn_share_qq);
        tv_share_friends.setOnClickListener(this);
        tv_share_weibo.setOnClickListener(this);
        tv_share_wechat.setOnClickListener(this);
        tv_share_qzone.setOnClickListener(this);
        tv_share_qq.setOnClickListener(this);
        pickDialog3.show();
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mWebView.destroy();
                        }
                    });

                }
            }, timeout);
        }
        super.onDestroy();

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_share_circle:
                share(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.btn_share_wb:
                share(SHARE_MEDIA.SINA);
                break;
            case R.id.btn_share_wx:
                share(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.btn_share_qzone:
                share(SHARE_MEDIA.QZONE);
                break;
            case R.id.btn_share_qq:
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
            FHUtils.showToast("分享成功");
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            FHUtils.showToast("分享失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {

        }
    };
}
