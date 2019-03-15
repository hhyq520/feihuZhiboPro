-dontshrink
-dontpreverify
-dontoptimize
-dontusemixedcaseclassnames

-flattenpackagehierarchy
-allowaccessmodification
-printmapping map.txt

-optimizationpasses 7
-verbose
-keepattributes Exceptions,InnerClasses
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-ignorewarnings
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends java.lang.Throwable {*;}
-keep public class * extends java.lang.Exception {*;}
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**
-dontwarn com.facebook.**
-keep public class javax.**
-keep public class android.webkit.**
-dontwarn android.support.v4.**
-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

# ButterKnife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

### greenDAO 3
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
    public static java.lang.String TABLENAME;
 }
 -keep class **$Properties

# If you do not use SQLCipher:
-dontwarn org.greenrobot.greendao.database.**
# If you do not use RxJava:
-dontwarn rx.**

#down
-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-keepattributes Exceptions

-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**


# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
# Gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }

-keep class cn.feihutv.zhibofeihu.data.db.model.** { *; }
-keep class cn.feihutv.zhibofeihu.data.network.http.model.** { *; }
-keep class cn.feihutv.zhibofeihu.data.network.socket.model.** { *; }
-keep class cn.feihutv.zhibofeihu.data.local.model.** { *; }
-keep class cn.feihutv.zhibofeihu.di.** { *; }


# OkHttp3
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**

# Okio
-dontwarn com.squareup.**
-dontwarn okio.**
-keep public class org.codehaus.* { *; }
-keep public class java.nio.* { *; }

# Dagger2
-dontwarn com.google.errorprone.annotations.*
-keepclasseswithmembernames class * {
    @Inject.* <fields>;
}

#rxjava
-keep class rx.schedulers.**
-keep class javax.inject.**
-keep class **$$ViewInjector { *; }
-dontobfuscate
-keep class javax.inject.** { *; }
-keep class dagger.** { *; }
-keepclassmembers,allowobfuscation class * {
 @javax.inject.* *;
 @dagger.* *;
}

#友盟
    -keep public interface com.tencent.**
    -keep public interface com.umeng.socialize.**
    -keep public interface com.umeng.socialize.sensor.**
    -keep public interface com.umeng.scrshot.**
    -keep class com.android.dingtalk.share.ddsharemodule.** { *; }
    -keep public class com.umeng.socialize.* {*;}

    -keep class com.umeng.scrshot.**
    -keep public class com.tencent.** {*;}
    -keep class com.umeng.socialize.sensor.**
    -keep class com.umeng.socialize.handler.**
    -keep class com.umeng.socialize.handler.*
    -keep class com.umeng.weixin.handler.**
    -keep class com.umeng.weixin.handler.*
    -keep class com.umeng.qq.handler.**
    -keep class com.umeng.qq.handler.*
    -keep class UMMoreHandler{*;}
    -keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
    -keep class com.tencent.mm.sdk.modelmsg.** implements   com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
    -keep class com.tencent.mm.sdk.** {
     *;
    }
    -keep class com.tencent.mm.opensdk.** {
   *;
    }
    -keep class com.tencent.** {*;}
    -dontwarn com.tencent.**
    -keep public class com.umeng.com.umeng.soexample.R$*{
    public static final int *;
    }
    -keep public class com.linkedin.android.mobilesdk.R$*{
    public static final int *;
        }
    -keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
    }

    -keep class com.tencent.open.TDialog$*
    -keep class com.tencent.open.TDialog$* {*;}
    -keep class com.tencent.open.PKDialog
    -keep class com.tencent.open.PKDialog {*;}
    -keep class com.tencent.open.PKDialog$*
    -keep class com.tencent.open.PKDialog$* {*;}

    -keep class com.sina.** {*;}
    -dontwarn com.sina.**
    -keep class  com.alipay.share.sdk.** {
       *;
    }


    #alipay
    -keep class android.net.SSLCertificateSocketFactory{*;}
-dontwarn android.net.SSLCertificateSocketFactory
    -keep class com.alipay.android.app.IAlixPay{*;}
    -keep class com.alipay.android.app.IAlixPay$Stub{*;}
    -keep class com.alipay.android.app.IRemoteServiceCallback{*;}
    -keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
    -keep class com.alipay.sdk.app.PayTask{ public *;}
    -keep class com.alipay.sdk.app.AuthTask{ public *;}
#极光推送
-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }

-dontwarn com.google.**
-keep class com.google.gson.** {*;}
-keep class com.google.protobuf.** {*;}


#保留跟 javascript相关的属性
-keepattributes JavascriptInterface
#保留JavascriptInterface中的方法
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
#网宿的混淆
-keep class com.chinanetcenter.StreamPusher.** {
*;
}
-keep class cnc.cad.validsdk.** {
*;
}
-keep class com.cnc.mediaplayer.sdk.** {
    *;
}
-keep class tv.danmaku.ijk.** {
    *;
}
-keepattributes InnerClasses
-dontoptimize

#mrgoplugin
-keepattributes *Annotation*
-dontwarn android.util.**
-dontwarn android.app.**
-dontwarn com.mrgo.drpg.**
-dontwarn com.mrgo.helper.**

-keep class android.util.** {*;}
-keep class android.app.** {*;}
-keep class com.mrgo.drpg.** {*;}
-keep class com.mrgo.helper.** {*;}

#混淆Bugly，在Proguard混淆文件中增加以下配置
#-dontwarn com.tencent.bugly.**
#-keep public class com.tencent.bugly.**{*;}