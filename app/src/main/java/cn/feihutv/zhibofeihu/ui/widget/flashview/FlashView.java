/*
copyright 2016 wanghongyu.
The project page：https://github.com/hardman/FlashAnimationToMobile
My blog page: http://blog.csdn.net/hard_man/
*/
package cn.feihutv.zhibofeihu.ui.widget.flashview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cn.feihutv.zhibofeihu.R;

/**
 * Created by wanghongyu on 10/12/15.
 * usage:
 <?xml version="1.0" encoding="utf-8"?>
 <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
 xmlns:tools="http://schemas.android.com/tools"
 xmlns:FlashView="http://schemas.android.com/apk/res-auto"
 android:layout_width="match_parent"
 android:layout_height="match_parent"
 tools:context="com.xcyo.yoyo.flashsupport.MainActivity">

 <com.xcyo.yoyo.flashsupport.view.FlashView
 android:layout_width="match_parent"
 android:layout_height="match_parent"
 FlashView:flashDir="flashAnims"
 FlashView:flashFileName="bieshu"
 FlashView:defaultAnim="bieshu"
 FlashView:designDPI="326"
 FlashView:loopTimes="0"
 android:id="@+id/flashview"
 />

 </RelativeLayout>

 */
public class FlashView extends View {
    //flash文件名
    private String mFlashName = null;
    //flash文件目录，可能在Asset中（Assets/[flash dir]/[flash name]），也可能在sdcard中（/sdcard/.[package name]/[flash dir]/[flash name]）。
    private String mFlashDir = FlashDataParser.DEFAULT_FLASH_DIR;

    //下面3个变量为加载完成后默认播放动画时的属性
    private String mDefaultAnimName = null;//默认播放的动画名
    private int mDefaultFromIndex = -1;//起始帧
    private int mDefaultToIndex = -1;//结束帧

    //设计DPI，默认为326，iPhone5s的dpi，制作flash时画布大小为640x1136时不用变，否则需要修改此值。
    //如果不懂此值的意思，请查阅dpi相关的更多资料
    private int mDesignDPI = FlashDataParser.DEFAULT_FLASH_DPI;

    //指定的动画重复次数，默认为1次
    private int mSetLoopTimes = FlashDataParser.FlashLoopTimeOnce;

    //用户解析动画描述文件和一些工具类，所有关键代码都在这里
    private FlashDataParser mDataParser;

    //下面两个变量用于stopAt函数
    private String mStopAtAnimName = null;
    private int mStopAtIndex = 0;

    private String animName;
    private int loopTimes, fromIndex;
    private String flashName, flashDir;
    private int designDPI = -1;
    private boolean isPC=false;
    /***
     * 下面3个构造方法可以在纯代码初始化时使用
     */
    public FlashView(Context c, String flashName){
        this(c, flashName, FlashDataParser.DEFAULT_FLASH_DIR);
    }

    public FlashView(Context c, String flashName, String flashDir){
        this(c, flashName, flashDir, FlashDataParser.DEFAULT_FLASH_DPI);
    }

    public FlashView(Context c, String flashName, String flashDir, int designDPI){
        super(c);
        mFlashName = flashName;
        mFlashDir = flashDir;
        mDesignDPI = designDPI;
        init();
    }

    public void setIsPc(){
        isPC=true;
    }

    /***
     * 以下3个构造方法为默认构造方法
     */
    public FlashView(Context context) {
        super(context);
        init();
    }

    public FlashView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        init();
    }

    public FlashView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        init();
    }

    /***
     * 加载xml文件中定义的属性
     * @param attrs
     */
    private void initAttrs(AttributeSet attrs){
        TypedArray arr = getContext().obtainStyledAttributes(attrs, R.styleable.FlashView);
        mFlashName = arr.getString(R.styleable.FlashView_flashFileName);
        mFlashDir = arr.getString(R.styleable.FlashView_flashDir);
        if(mFlashDir == null){
            mFlashDir = FlashDataParser.DEFAULT_FLASH_DIR;
        }
        mDefaultAnimName = arr.getString(R.styleable.FlashView_defaultAnim);
        mSetLoopTimes = arr.getInt(R.styleable.FlashView_loopTimes, FlashDataParser.FlashLoopTimeOnce);

        mDesignDPI = arr.getInt(R.styleable.FlashView_designDPI, FlashDataParser.DEFAULT_FLASH_DPI);//326为iphone5的dpi

        mDefaultFromIndex = arr.getInt(R.styleable.FlashView_fromIndex, mDefaultFromIndex);
        mDefaultToIndex = arr.getInt(R.styleable.FlashView_toIndex, mDefaultToIndex);
    }

    /***
     * 开始解析数据，并自动播放动画
     * @return
     */
    private boolean init(){
        mDataParser = new FlashDataParser(getContext(), mFlashName, mFlashDir, mDesignDPI);

        if(mDataParser.isInitOk()){
            if (mDefaultAnimName != null) {
                if (mDefaultFromIndex >= 0) {
                    if (mDefaultToIndex >= 0) {
                        play(mDefaultAnimName, mSetLoopTimes, mDefaultFromIndex, mDefaultToIndex);
                    } else {
                        play(mDefaultAnimName, mSetLoopTimes, mDefaultFromIndex);
                    }
                } else {
                    if (mDefaultToIndex >= 0) {
                        play(mDefaultAnimName, mSetLoopTimes, 0, mDefaultToIndex);
                    } else {
                        play(mDefaultAnimName, mSetLoopTimes);
                    }
                }
            }
            return true;
        }else{
            FlashDataParser.log("[ERROR] flash data parser init return false");
            return false;
        }
    }

    /***
     * 可以用此方法重新加载一个新的flash动画文件。
     * @param flashName 动画文件名
     * @return
     */
    public boolean reload(String flashName){
        stop();
        this.flashName = flashName;
        return mDataParser.reload(flashName);
    }

    /***
     * 可以用此方法重新加载一个新的flash动画文件。
     * @param flashName 动画文件名
     * @param flashDir 动画所在文件夹名
     * @return
     */
    public boolean reload(String flashName, String flashDir){
        stop();
        this.flashName = flashName;
        this.flashDir = flashDir;
        return mDataParser.reload(flashName, flashDir);
    }

    /***
     * 使用这个对象，重新加载一个新的动画
     * @param flashName: 动画名
     * @param flashDir: 动画目录
     * @param designDPI: 设计dpi 默认iphone5 为326
     * @return
     */
    public boolean reload(String flashName, String flashDir, int designDPI){
        stop();
        this.flashName = flashName;
        this.flashDir = flashDir;
        this.designDPI = designDPI;
        return mDataParser.reload(flashName, flashDir, designDPI);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if(isPC){
            return;
        }
//        if(visibility == View.VISIBLE){
//
//            if(!TextUtils.isEmpty(flashName) && !TextUtils.isEmpty(flashDir) && designDPI!=-1){
//                reload(flashName, flashDir, designDPI);
//            }else if(!TextUtils.isEmpty(flashName) && !TextUtils.isEmpty(flashDir)){
//                reload(flashName, flashDir);
//            }else if(!TextUtils.isEmpty(flashName)){
//                reload(flashName);
//            }
//            play(animName, loopTimes, fromIndex);
//        }else{
//            stop();
//        }
    }

    /***
     * 事件回调
     * @param callback
     */
    public void setEventCallback(FlashDataParser.IFlashViewEventCallback callback){
        mDataParser.setEventCallback(callback);
    }

    /***
     * 替换动画中所有相同图片
     * @param texName: 对应的图片名
     * @param bitmap: 新的Bitmap
     */
    public void replaceBitmap(String texName, Bitmap bitmap){
        mDataParser.replaceBitmap(texName, bitmap);
    }

    public void replaceBitmap(Bitmap bitmap){
        String textureName = mDataParser.getDefaultTexTureName();
        if (!TextUtils.isEmpty(textureName)){
            replaceBitmap(textureName,bitmap);
        }
    }
    /***
     * 暂时没用，因为这样会让第一次显示的比较慢，起初的10几帧都不见了。
     */
    private Handler mHandler = new Handler();
    private Runnable mUpdateOnMainThreadRunnable = new Runnable() {
        @Override
        public void run() {
            mHandler.post(mUpdateRunnable);
        }
    };

    /***
     * 开启线程每次间隔one frame time刷新一次。
     */
    private ScheduledExecutorService mScheduledExecutorService;
    private Runnable mUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isShown() || isPaused() || isStoped()){
                return;
            }
            mDataParser.increaseTotalTime(mDataParser.getOneFrameTime());
            postInvalidate();
        }
    };

    /***
     *
     * 播放动画
     * @param animName: 动画名
     * @param loopTimes: 循环次数，0表示无限循环
     * @param fromIndex: 从第几帧开始播放
     * @param toIndex: 播放到第几帧结束
     */
    public void play(String animName, int loopTimes, int fromIndex, int toIndex) {
        if (!mDataParser.isInitOk()){
            FlashDataParser.log("[Error] data parser is not init ok");
            return;
        }
        mDataParser.play(animName, loopTimes, fromIndex, toIndex);
        mScheduledExecutorService = Executors.newScheduledThreadPool(1);
        mScheduledExecutorService.scheduleAtFixedRate(mUpdateRunnable, 0, (int) (mDataParser.getOneFrameTime() * 1000000), TimeUnit.MICROSECONDS);
    }

    public void play(String animName, int loopTimes, int fromIndex) {
        this.animName = animName;
        this.loopTimes = loopTimes;
        this.fromIndex = fromIndex;
        play(animName, loopTimes, fromIndex, mDataParser.getParseFrameMaxIndex());
    }

    public void play(String animName, int loopTimes){
        play(animName, loopTimes, 0);
    }

    public void play(int loopTimes) {
        if (TextUtils.isEmpty(mDefaultAnimName)){
            mDefaultAnimName = mDataParser.getDefaultAnimName();
        }
        play(mDefaultAnimName, loopTimes);
    }

    public void play(){
        play(mSetLoopTimes);
    }
    /***
     * @return 是否动画正在播放
     */
    public boolean isPlaying(){
        return mDataParser.isPlaying();
    }

    /***
     * @return 是否动画已停止，或还未开始播放
     */
    public boolean isStoped() {
        return mDataParser.isStoped();
    }

    /***
     *
     * @return 是否暂停
     */
    public boolean isPaused() {
        return mDataParser.isPaused();
    }

    public void stopAtLastIndex(){
        String animName = mDataParser.getDefaultAnimName();
        stopAt(animName,mDataParser.getAnimFrameMaxIndex(animName) - 1);
    }
    /***
     * 显示动画的某一帧，这个不同于pause，此函数在动画停止时强制显示某一帧的图像。
     * @param animName：动画名
     * @param index：停在哪一帧
     */
    public void stopAt(String animName, int index){
        stop();
        mStopAtAnimName = animName;
        mStopAtIndex = index;
        postInvalidate();
    }

    /***
     * 设置图像的scale
     * @param x: scale x
     * @param y: scale y
     * @param isDpiEffect: 是否乘以dpi
     */
    public void setScale(float x, float y, boolean isDpiEffect){
        mDataParser.setScale(x, y, isDpiEffect);
    }
    public void setScale(float x, float y){
        setScale(x, y, true);
    }

    /***
     * 获取动画帧数
     * @return 动画帧数
     */
    public int getLength(){
        return mDataParser.getLength();
    }

    /***
     * 停止动画
     */
    public void stop(){
        mDataParser.stop();
        if(mScheduledExecutorService != null && !mScheduledExecutorService.isTerminated() && !mScheduledExecutorService.isShutdown()) {
            mScheduledExecutorService.shutdown();
        }
        mStopAtAnimName = null;
        mStopAtIndex = 0;
    }

    /***
     * 暂停
     */
    public void pause(){
        mDataParser.pause();
    }

    /***
     * 恢复
     */
    public void resume(){
        mDataParser.resume();
    }

    /***
     * 绘制某一帧的图像，如果当前是stop的状态，那么就当作stopAt逻辑来处理。
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!mDataParser.drawCanvas(canvas) && mStopAtAnimName != null){
            mDataParser.drawCanvas(canvas, mStopAtIndex, mStopAtAnimName, false);
        }
    }

    public void clearBitmap(){
        if(mDataParser!=null) {
            mDataParser.clearBitmap();
        }
    }
}
