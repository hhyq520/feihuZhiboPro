package cn.feihutv.zhibofeihu.ui.live.adapter;

/**
 * Created by Administrator on 2017/2/22.
 */

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.db.model.SysFontColorBean;
import cn.feihutv.zhibofeihu.data.local.model.TCChatEntity;
import cn.feihutv.zhibofeihu.ui.live.OnStrClickListener;
import cn.feihutv.zhibofeihu.ui.widget.font.RxTextTool;
import cn.feihutv.zhibofeihu.utils.TCConstants;
import cn.feihutv.zhibofeihu.utils.TCUtils;

/**
 * 消息列表的Adapter
 */
public class TCChatMsgNewListAdapter extends BaseAdapter implements AbsListView.OnScrollListener {

    private static String TAG = TCChatMsgNewListAdapter.class.getSimpleName();
    private   int ITEMCOUNT = 7;
    private List<TCChatEntity> listMessage = null;
    private LayoutInflater inflater;
    private LinearLayout layout;
    private int mTotalHeight;
    public static final int TYPE_TEXT_SEND = 0;
    public static final int TYPE_TEXT_RECV = 1;
    private Context context;
    private ListView mListView;
    private ArrayList<TCChatEntity> myArray = new ArrayList<>();

    private boolean  mBLiveAnimator = false;
    private OnStrClickListener mListener;
    public void setOnStrClickListener(OnStrClickListener listener){
        mListener=listener;
    }

    class AnimatorInfo {
        long createTime;

        public AnimatorInfo(long uTime) {
            createTime = uTime;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }
    }

    private   int MAXANIMATORCOUNT = 8;
    private  int MAXLISTVIEWHEIGHT;
    private static final int ANIMATORDURING = 8000;
    private static final int MAXITEMCOUNT = 50;
    private LinkedList<AnimatorSet> mAnimatorSetList;
    private LinkedList<AnimatorInfo> mAnimatorInfoList;
    private boolean mScrolling = false;
    private boolean mCreateAnimator = false;
    private boolean isPc;
    public TCChatMsgNewListAdapter(Context context, ListView listview, List<TCChatEntity> objects, boolean isPC, int h) {
        this.context = context;
        mListView = listview;
        inflater = LayoutInflater.from(context);
        this.listMessage = objects;
        this.isPc=isPC;
        mAnimatorSetList = new LinkedList<>();
        mAnimatorInfoList = new LinkedList<>();
        MAXLISTVIEWHEIGHT=h;
        mListView.setOnScrollListener(this);
    }

    public void setLineCount(int height){
        ITEMCOUNT=100;
        MAXANIMATORCOUNT=100;
    }

    @Override
    public int getCount() {
        return listMessage.size();
    }

    @Override
    public Object getItem(int position) {
        return listMessage.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        SpannableString spanString;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.listview_msg_new_item, null);
            holder.sendContext = (TextView) convertView.findViewById(R.id.sendcontext);
            holder.imageView = (ImageView) convertView.findViewById(R.id.iv);
            holder.linearLayoutBg=(RelativeLayout) convertView.findViewById(R.id.linerlayout_bg);
            convertView.setTag(R.id.tag_first, holder);
        } else {
            holder = (ViewHolder) convertView.getTag(R.id.tag_first);
        }

        final TCChatEntity item = listMessage.get(position);

        if (mCreateAnimator && mBLiveAnimator) {
//            playViewAnimator(convertView, position, item);
        }
        Bitmap level=null;
        if(item.getLevel()==0){
            spanString = new SpannableString(item.getSenderName() + item.getContext());
        }else{
            if(TextUtils.isEmpty(item.getZuojia())){
                spanString = new SpannableString(""+item.getSenderName() + item.getContext());
            }else{
                spanString = new SpannableString(""+item.getSenderName()+item.getZuojia() + item.getContext());
            }
//            if(holder.imageView.getTag(R.id.tag_first)==null||(holder.imageView.getTag(R.id.tag_first)!=null&&!(item.getLevel()==(int)holder.imageView.getTag(R.id.tag_first)))){
//                TCUtils.showLevelWithUrl(context,holder.imageView,item.getLevel());
//                holder.imageView.setTag(R.id.tag_first,item.getLevel());
//            }else{
//
//            }
            TCUtils.showLevelWithUrl(context,holder.imageView,item.getLevel());
        }

        String text = "";

        if (item.getType() == TCConstants.GIFT_TYPE) {
//            spanString.setSpan(new ImageSpan(context, getDefaultBitmap(level), ImageSpan.ALIGN_BOTTOM), 0, text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            holder.imageView.setVisibility(View.VISIBLE);
            final SysFontColorBean sysFontColorBean=FeihuZhiboApplication.getApplication().mDataManager.getSysFontColorBeanByKey("nickname");
            final RxTextTool.Builder build=RxTextTool.getBuilder("").setAlign(Layout.Alignment.ALIGN_NORMAL);
            if(item.getVip()>0){
                if(item.isVipExpired()){
                    if(item.getGuardType()==0){

                    }else{
                        if(item.isGuardExpired()){

                        }else{
                            build.append("bitmap").setResourceId(R.drawable.icon_guard);
                        }
                    }
                    if(item.isLiang()){
                        build.append(item.getAccountId()).setForegroundColor(context.getResources().getColor(R.color.beautiColor)).setFontType("notnormal");
                    }else{
                        build.append(item.getAccountId()).setForegroundColor(context.getResources().getColor(R.color.app_white)).setFontType("normal");
                    }
                    String color= " ";
                    if(isPc){
                        color="#"+ sysFontColorBean.getPc();
                    }else{
                        color="#"+ sysFontColorBean.getPhone();
                    }
                    build.append(item.getSenderName()).setForegroundColor(Color.parseColor(color));
                    build.append(item.getContext()).setForegroundColor(context.getResources().getColor(R.color.nick_name_color));
                    build.into(holder.sendContext);
                }else{
                    String cosVipIconRootPath = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosVipIconRootPath();
                    RequestBuilder<Bitmap> req = Glide.with(context).asBitmap().apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL));
                    req.load(cosVipIconRootPath + "icon_vip" + item.getVip() + ".png").into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            build.append("bitmap").setBitmap(getDefaultBitmap(resource));
                            if(item.getGuardType()==0){

                            }else{
                                if(item.isGuardExpired()){

                                }else{
                                    build.append("bitmap").setResourceId(R.drawable.icon_guard);
                                }
                            }
                            if(item.isLiang()){
                                build.append(item.getAccountId()).setForegroundColor(context.getResources().getColor(R.color.beautiColor)).setFontType("notnormal");
                            }else{
                                build.append(item.getAccountId()).setForegroundColor(context.getResources().getColor(R.color.app_white)).setFontType("normal");
                            }
                            String color= " ";
                            if(isPc){
                                color="#"+ sysFontColorBean.getPc();
                            }else{
                                color="#"+ sysFontColorBean.getPhone();
                            }
                            build.append(item.getSenderName()).setForegroundColor(Color.parseColor(color));
                            build.append(item.getContext()).setForegroundColor(context.getResources().getColor(R.color.nick_name_color));
                            build.into(holder.sendContext);

                        }
                    });

                }
            }else{
                if(item.getGuardType()==0){

                }else{
                    if(item.isGuardExpired()){

                    }else{
                        build.append("bitmap").setResourceId(R.drawable.icon_guard);
                    }
                }
                if(item.isLiang()){
                    build.append(item.getAccountId()).setForegroundColor(context.getResources().getColor(R.color.beautiColor)).setFontType("notnormal");
                }else{
                    build.append(item.getAccountId()).setForegroundColor(context.getResources().getColor(R.color.app_white)).setFontType("normal");
                }
                String color= " ";
                if(isPc){
                    color="#"+ sysFontColorBean.getPc();
                }else{
                    color="#"+ sysFontColorBean.getPhone();
                }
                build.append(item.getSenderName()).setForegroundColor(Color.parseColor(color));
                build.append(item.getContext()).setForegroundColor(context.getResources().getColor(R.color.nick_name_color));
                build.into(holder.sendContext);
            }
        }else if(item.getType() ==TCConstants.SYSTEM_TYPE){
            if(isPc){
                holder.sendContext.setTextColor(context.getResources().getColor(R.color.btn_sure_forbidden));
            }else{
                holder.sendContext.setTextColor(context.getResources().getColor(R.color.app_white));
            }
            holder.imageView.setVisibility(View.GONE);
            holder.sendContext.setText(item.getSenderName()+item.getContext());
        } else if(item.getType() ==TCConstants.TEXT_TYPE) {
            holder.imageView.setVisibility(View.VISIBLE);
            final RxTextTool.Builder build=RxTextTool.getBuilder("").setAlign(Layout.Alignment.ALIGN_NORMAL);
            final SysFontColorBean sysFontColorBean=FeihuZhiboApplication.getApplication().mDataManager.getSysFontColorBeanByKey("nickname");
            SysFontColorBean sysFontColorBean1=FeihuZhiboApplication.getApplication().mDataManager.getSysFontColorBeanByKey("normalChat");
            final SysFontColorBean sysFontColorBean2=FeihuZhiboApplication.getApplication().mDataManager.getSysFontColorBeanByKey("vipChat");
            if(item.getVip()>0){
                if(item.isVipExpired()){
                    if(item.getGuardType()==0){

                    }else{
                        if(item.isGuardExpired()){

                        }else{
                            build.append("bitmap").setResourceId(R.drawable.icon_guard);
                        }
                    }
                    if(item.isLiang()){
                        build.append(item.getAccountId()).setForegroundColor(context.getResources().getColor(R.color.beautiColor)).setFontType("notnormal");
                    }else{
                        build.append(item.getAccountId()).setForegroundColor(context.getResources().getColor(R.color.app_white)).setFontType("normal");
                    }
                    String color= " ";
                    if(isPc){
                        color="#"+ sysFontColorBean.getPc();
                    }else{
                        color="#"+ sysFontColorBean.getPhone();
                    }
                    build.append(item.getSenderName()).setForegroundColor(Color.parseColor(color));
                    String color1= " ";
                    if(isPc){
                        color1="#"+ sysFontColorBean1.getPc();
                    }else{
                        color1="#"+ sysFontColorBean1.getPhone();
                    }
                    build.append(item.getContext()).setForegroundColor(Color.parseColor(color1));
                    build.into(holder.sendContext);
                }else{
                    String cosVipIconRootPath = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosVipIconRootPath();
                    RequestBuilder<Bitmap> req = Glide.with(context).asBitmap().apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL));
                    req.load(cosVipIconRootPath + "icon_vip" + item.getVip() + ".png").into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            build.append("bitmap").setBitmap(getDefaultBitmap(resource));
                            if(item.getGuardType()==0){

                            }else{
                                if(item.isGuardExpired()){

                                }else{
                                    build.append("bitmap").setResourceId(R.drawable.icon_guard);
                                }
                            }
                            if(item.isLiang()){
                                build.append(item.getAccountId()).setForegroundColor(context.getResources().getColor(R.color.beautiColor)).setFontType("notnormal");
                            }else{
                                build.append(item.getAccountId()).setForegroundColor(context.getResources().getColor(R.color.app_white)).setFontType("normal");
                            }
                            String color= " ";
                            if(isPc){
                                color="#"+ sysFontColorBean.getPc();
                            }else{
                                color="#"+ sysFontColorBean.getPhone();
                            }
                            build.append(item.getSenderName()).setForegroundColor(Color.parseColor(color));
                            String color1= " ";
                            if(isPc){
                                color1="#"+ sysFontColorBean2.getPc();
                            }else{
                                color1="#"+ sysFontColorBean2.getPhone();
                            }
                            build.append(item.getContext()).setForegroundColor(Color.parseColor(color1));
                            build.into(holder.sendContext);

                        }
                    });

                }
            }else{
                if(item.getGuardType()==0){

                }else{
                    if(item.isGuardExpired()){

                    }else{
                        build.append("bitmap").setResourceId(R.drawable.icon_guard);
                    }
                }
                if(item.isLiang()){
                    build.append(item.getAccountId()).setForegroundColor(context.getResources().getColor(R.color.beautiColor)).setFontType("notnormal");
                }else{
                    build.append(item.getAccountId()).setForegroundColor(context.getResources().getColor(R.color.app_white)).setFontType("normal");
                }
                String color= " ";
                if(isPc){
                    color="#"+ sysFontColorBean.getPc();
                }else{
                    color="#"+ sysFontColorBean.getPhone();
                }
                build.append(item.getSenderName()).setForegroundColor(Color.parseColor(color));
                String color1= " ";
                if(isPc){
                    color1="#"+ sysFontColorBean1.getPc();
                }else{
                    color1="#"+ sysFontColorBean1.getPhone();
                }
                build.append(item.getContext()).setForegroundColor(Color.parseColor(color1));
                build.into(holder.sendContext);
            }
        }else if(item.getType() == TCConstants.JOIN_TYPE){
            holder.imageView.setVisibility(View.VISIBLE);
            final RxTextTool.Builder build=RxTextTool.getBuilder("").setAlign(Layout.Alignment.ALIGN_NORMAL);
            final SysFontColorBean sysFontColorBean=FeihuZhiboApplication.getApplication().mDataManager.getSysFontColorBeanByKey("nickname");
            if(item.getVip()>0){
                if(item.isVipExpired()){
                    if(item.getGuardType()==0){

                    }else{
                        if(item.isGuardExpired()){

                        }else{
                            build.append("bitmap").setResourceId(R.drawable.icon_guard);
                        }
                    }
                    if(item.isLiang()){
                        build.append(item.getAccountId()).setForegroundColor(context.getResources().getColor(R.color.beautiColor)).setFontType("notnormal");
                    }else{
                        build.append(item.getAccountId()).setForegroundColor(context.getResources().getColor(R.color.app_white)).setFontType("normal");
                    }
                    String color= " ";
                    if(isPc){
                        color="#"+ sysFontColorBean.getPc();
                    }else{
                        color="#"+ sysFontColorBean.getPhone();
                    }
                    build.append(item.getSenderName()).setForegroundColor(Color.parseColor(color));
                    if(!TextUtils.isEmpty(item.getZuojia())) {
                        build.append(item.getZuojia()+item.getContext()).setForegroundColor(context.getResources().getColor(R.color.nick_name_color));
                    }else{
                        build.append(item.getContext()).setForegroundColor(context.getResources().getColor(R.color.nick_name_color));
                    }
                    build.into(holder.sendContext);
                }else{
                    String cosVipIconRootPath = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosVipIconRootPath();
                    RequestBuilder<Bitmap> req = Glide.with(context).asBitmap().apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL));
                    req.load(cosVipIconRootPath + "icon_vip" + item.getVip() + ".png").into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            build.append("bitmap").setBitmap(getDefaultBitmap(resource));
                            if(item.getGuardType()==0){

                            }else{
                                if(item.isGuardExpired()){

                                }else{
                                    build.append("bitmap").setResourceId(R.drawable.icon_guard);
                                }
                            }
                            if(item.isLiang()){
                                build.append(item.getAccountId()).setForegroundColor(context.getResources().getColor(R.color.beautiColor)).setFontType("notnormal");
                            }else{
                                build.append(item.getAccountId()).setForegroundColor(context.getResources().getColor(R.color.app_white)).setFontType("normal");
                            }
                            String color= " ";
                            if(isPc){
                                color="#"+ sysFontColorBean.getPc();
                            }else{
                                color="#"+ sysFontColorBean.getPhone();
                            }
                            build.append(item.getSenderName()).setForegroundColor(Color.parseColor(color));
                            if(!TextUtils.isEmpty(item.getZuojia())) {
                                build.append(item.getZuojia()+item.getContext()).setForegroundColor(context.getResources().getColor(R.color.nick_name_color));
                            }else{
                                build.append(item.getContext()).setForegroundColor(context.getResources().getColor(R.color.nick_name_color));
                            }
                            build.into(holder.sendContext);

                        }
                    });
                }
            }else{
                if(item.getGuardType()==0){

                }else{
                    if(item.isGuardExpired()){

                    }else{
                        build.append("bitmap").setResourceId(R.drawable.icon_guard);
                    }
                }
                if(item.isLiang()){
                    build.append(item.getAccountId()).setForegroundColor(context.getResources().getColor(R.color.beautiColor)).setFontType("notnormal");
                }else{
                    build.append(item.getAccountId()).setForegroundColor(context.getResources().getColor(R.color.app_white)).setFontType("normal");
                }
                String color= " ";
                if(isPc){
                    color="#"+ sysFontColorBean.getPc();
                }else{
                    color="#"+ sysFontColorBean.getPhone();
                }
                build.append(item.getSenderName()).setForegroundColor(Color.parseColor(color));
                if(!TextUtils.isEmpty(item.getZuojia())) {
                    build.append(item.getZuojia()+item.getContext()).setForegroundColor(context.getResources().getColor(R.color.nick_name_color));
                }else{
                    build.append(item.getContext()).setForegroundColor(context.getResources().getColor(R.color.nick_name_color));
                }
                build.into(holder.sendContext);
            }
        }else if(item.getType() == TCConstants.CONCERN_TYPE){
            holder.imageView.setVisibility(View.VISIBLE);
            final SysFontColorBean sysFontColorBean=FeihuZhiboApplication.getApplication().mDataManager.getSysFontColorBeanByKey("nickname");
            final RxTextTool.Builder build=RxTextTool.getBuilder("").setAlign(Layout.Alignment.ALIGN_NORMAL);
            if(item.getVip()>0){
                if(item.isVipExpired()){
                    if(item.getGuardType()==0){

                    }else{
                        if(item.isGuardExpired()){

                        }else{
                            build.append("bitmap").setResourceId(R.drawable.icon_guard);
                        }
                    }
                    if(item.isLiang()){
                        build.append(item.getAccountId()).setForegroundColor(context.getResources().getColor(R.color.beautiColor)).setFontType("notnormal");
                    }else{
                        build.append(item.getAccountId()).setForegroundColor(context.getResources().getColor(R.color.app_white)).setFontType("normal");
                    }
                    String color= " ";
                    if(isPc){
                        color="#"+ sysFontColorBean.getPc();
                    }else{
                        color="#"+ sysFontColorBean.getPhone();
                    }
                    build.append(item.getSenderName()).setForegroundColor(Color.parseColor(color));
                    build.append(item.getContext()).setForegroundColor(context.getResources().getColor(R.color.nick_name_color));
                    build.into(holder.sendContext);
                }else{
                    String cosVipIconRootPath = FeihuZhiboApplication.getApplication().mDataManager.getSysConfigBean().get(0).getCosVipIconRootPath();
                    RequestBuilder<Bitmap> req = Glide.with(context).asBitmap().apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL));
                    req.load(cosVipIconRootPath + "icon_vip" + item.getVip() + ".png").into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            build.append("bitmap").setBitmap(getDefaultBitmap(resource));
                            if(item.getGuardType()==0){

                            }else{
                                if(item.isGuardExpired()){

                                }else{
                                    build.append("bitmap").setResourceId(R.drawable.icon_guard);
                                }
                            }
                            if(item.isLiang()){
                                build.append(item.getAccountId()).setForegroundColor(context.getResources().getColor(R.color.beautiColor)).setFontType("notnormal");
                            }else{
                                build.append(item.getAccountId()).setForegroundColor(context.getResources().getColor(R.color.app_white)).setFontType("normal");
                            }
                            String color= " ";
                            if(isPc){
                                color="#"+ sysFontColorBean.getPc();
                            }else{
                                color="#"+ sysFontColorBean.getPhone();
                            }
                            build.append(item.getSenderName()).setForegroundColor(Color.parseColor(color));
                            build.append(item.getContext()).setForegroundColor(context.getResources().getColor(R.color.nick_name_color));
                            build.into(holder.sendContext);
                        }
                    });
                }
            }else{
                if(item.getGuardType()==0){

                }else{
                    if(item.isGuardExpired()){

                    }else{
                        build.append("bitmap").setResourceId(R.drawable.icon_guard);
                    }
                }
                if(item.isLiang()){
                    build.append(item.getAccountId()).setForegroundColor(context.getResources().getColor(R.color.beautiColor)).setFontType("notnormal");
                }else{
                    build.append(item.getAccountId()).setForegroundColor(context.getResources().getColor(R.color.app_white)).setFontType("normal");
                }
                String color= " ";
                if(isPc){
                    color="#"+ sysFontColorBean.getPc();
                }else{
                    color="#"+ sysFontColorBean.getPhone();
                }
                build.append(item.getSenderName()).setForegroundColor(Color.parseColor(color));
                build.append(item.getContext()).setForegroundColor(context.getResources().getColor(R.color.nick_name_color));
                build.into(holder.sendContext);
            }
        }else if(item.getType() == TCConstants.BAN_TYPE){
            holder.imageView.setVisibility(View.VISIBLE);
            final SysFontColorBean sysFontColorBean=FeihuZhiboApplication.getApplication().mDataManager.getSysFontColorBeanByKey("nickname");
            final RxTextTool.Builder build=RxTextTool.getBuilder("").setAlign(Layout.Alignment.ALIGN_NORMAL);
            String color=" ";
            if(isPc){
                color="#"+ sysFontColorBean.getPc();
            }else{
                color="#"+ sysFontColorBean.getPhone();
            }
            build.append(item.getSenderName()).setForegroundColor(Color.parseColor(color));
            String ss=item.getBanName()+item.getContext();
            build.append(ss).setForegroundColor(context.getResources().getColor(R.color.red_2));
            build.into(holder.sendContext);
        }

        // 设置控件实际宽度以便计算列表项实际高度
//        holder.sendContext.fixViewWidth(mListView.getWidth());
        holder.sendContext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null){
                    mListener.onItemClick(item.getUserId());
                }
            }
        });
        return convertView;
    }
    private Bitmap getDefaultBitmap(Bitmap bitmap) {
        Bitmap mDefauleBitmap = null;
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            matrix.postScale(((float) 38) / width, ((float) 38) / height);
            mDefauleBitmap = Bitmap.createBitmap(bitmap, 0, 0, width,height,matrix,true);
        }
        return mDefauleBitmap;
    }

    private SpannableString handler(SpannableString sb, String content) {
        String regex = "(\\[f_static_)\\d{1,3}(\\])";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        while (m.find()) {
            String tempText = m.group();
            try {
                String png = "face/png/"+tempText.substring("[".length(),tempText.length() - "]".length())+".png";
                try {
                    sb.setSpan(new ImageSpan(context, BitmapFactory.decodeStream(context.getAssets().open(png))), m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            } catch (Exception e) {
                Log.i("amp", "haha");
                e.printStackTrace();
            }
        }
        return sb;
    }

    static class ViewHolder {
        public TextView sendContext;
        public ImageView imageView;
        public RelativeLayout linearLayoutBg;
    }

    /**
     * 停止View属性动画
     *
     * @param itemView 当前执行动画View
     */
    private void stopViewAnimator(View itemView) {
        AnimatorSet aniSet = (AnimatorSet) itemView.getTag(R.id.tag_second);
        if (null != aniSet) {
            aniSet.cancel();
            mAnimatorSetList.remove(aniSet);
        }
    }

    /**
     * 播放View属性动画
     *
     * @param itemView   动画对应View
     * @param startAlpha 初始透明度
     * @param duringTime 动画时长
     */
    private void playViewAnimator(View itemView, float startAlpha, long duringTime) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(itemView, "alpha", startAlpha, 0f);
        AnimatorSet aniSet = new AnimatorSet();
        aniSet.setDuration(duringTime);
        aniSet.play(animator);
        aniSet.start();
        mAnimatorSetList.add(aniSet);
        itemView.setTag(R.id.tag_second, aniSet);
    }

    /**
     * 播放渐消动画
     *
     * @param pos  位置
     * @param view 执行动画View
     */
    public void playDisappearAnimator(int pos, View view) {
        int firstVisable = mListView.getFirstVisiblePosition();
        if (firstVisable <= pos) {
            playViewAnimator(view, 1f, ANIMATORDURING);
        } else {
            Log.d(TAG, "playDisappearAnimator->unexpect pos: " + pos + "/" + firstVisable);
        }
    }

    /**
     * 继续播放渐消动画
     *
     * @param itemView 执行动画View
     * @param position 位置
     * @param item
     */
    private void continueAnimator(View itemView, int position, final TCChatEntity item) {
        int animatorIdx = listMessage.size() - 1 - position;

        if (animatorIdx < MAXANIMATORCOUNT) {
            float startAlpha = 1f;
            long during = ANIMATORDURING;

            stopViewAnimator(itemView);

            // 播放动画
            if (position < mAnimatorInfoList.size()) {
                AnimatorInfo info = mAnimatorInfoList.get(position);
                long time = info.getCreateTime();  //  获取列表项加载的初始时间
                during = during - (System.currentTimeMillis() - time);     // 计算动画剩余时长
                startAlpha = 1f * during / ANIMATORDURING;                    // 计算动画初始透明度
                if (during < 0) {   // 剩余时长小于0直接设置透明度为0并返回
                    itemView.setAlpha(0f);
                    Log.v(TAG, "continueAnimator->already end animator:" + position + "/" + item.getContext() + "-" + during);
                    return;
                }
            }

            // 创建属性动画并播放
            Log.v(TAG, "continueAnimator->pos: " + position + "/" + listMessage.size() + ", alpha:" + startAlpha + ", dur:" + during);
            playViewAnimator(itemView, startAlpha, during);
        } else {
            Log.v(TAG, "continueAnimator->ignore pos: " + position + "/" + listMessage.size());
        }
    }

    /**
     * 播放消失动画
     */
    private void playDisappearAnimator() {
        for (int i = 0; i < mListView.getChildCount(); i++) {
            View itemView = mListView.getChildAt(i);
            if (null == itemView) {
                Log.w(TAG, "playDisappearAnimator->view not found: " + i + "/" + mListView.getCount());
                break;
            }

            // 更新动画创建时间
            int position = mListView.getFirstVisiblePosition() + i;
            if (position < mAnimatorInfoList.size()) {
                mAnimatorInfoList.get(position).setCreateTime(System.currentTimeMillis());
            } else {
                Log.e(TAG, "playDisappearAnimator->error: " + position + "/" + mAnimatorInfoList.size());
            }

            playViewAnimator(itemView, 1f, ANIMATORDURING);
        }
    }

    /**
     * 播放列表项动画
     *
     * @param itemView 要播放动画的列表项
     * @param position 列表项的位置
     * @param item     列表数据
     */
    private void playViewAnimator(View itemView, int position, final TCChatEntity item) {
        if (!myArray.contains(item)) {  // 首次加载的列表项动画
            myArray.add(item);
            mAnimatorInfoList.add(new AnimatorInfo(System.currentTimeMillis()));
        }

        if (mScrolling) {  // 滚动时不播放动画，设置透明度为1
            itemView.setAlpha(1f);
        } else {
            continueAnimator(itemView, position, item);
        }
    }

    /**
     * 删除超过上限(MAXITEMCOUNT)的列表项
     */
    private void clearFinishItem() {
        // 删除超过的列表项
        while (listMessage.size() > MAXITEMCOUNT) {
            listMessage.remove(0);
            if (mAnimatorInfoList.size() > 0) {
                mAnimatorInfoList.remove(0);
            }
        }

        // 缓存列表延迟删除
        while (myArray.size() > (MAXITEMCOUNT << 1)) {
            myArray.remove(0);
        }

        while (mAnimatorInfoList.size() >= listMessage.size()) {
            Log.e(TAG, "clearFinishItem->error size: " + mAnimatorInfoList.size() + "/" + listMessage.size());
            if (mAnimatorInfoList.size() > 0) {
                mAnimatorInfoList.remove(0);
            } else {
                break;
            }
        }
    }

    /**
     * 重新计算ITEMCOUNT条记录的高度，并动态调整ListView的高度
     */
    private void redrawListViewHeight() {

        int totalHeight = 0;
        int start = 0, lineCount = 0;

        if (listMessage.size() <= 0) {
            return;
        }
        Log.e("tcad",mTotalHeight+"/"+MAXLISTVIEWHEIGHT);
        if (mTotalHeight >= MAXLISTVIEWHEIGHT) {
            return;
        }

        // 计算底部ITEMCOUNT条记录的高度
        mCreateAnimator = false;    // 计算高度时不播放属性动画
        for (int i = listMessage.size() - 1; i >= start && lineCount < ITEMCOUNT; i--, lineCount++) {
            View listItem = getView(i, null, mListView);

            listItem.measure(View.MeasureSpec.makeMeasureSpec(MAXLISTVIEWHEIGHT, View.MeasureSpec.AT_MOST)
                , View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            // add item height
            totalHeight += listItem.getMeasuredHeight();
            if (totalHeight > MAXLISTVIEWHEIGHT) {
                totalHeight = MAXLISTVIEWHEIGHT;
                break;
            }
        }
        mCreateAnimator = true;


        mTotalHeight = totalHeight;
        // 调整ListView高度
        ViewGroup.LayoutParams params = mListView.getLayoutParams();
        params.height = totalHeight + (mListView.getDividerHeight() * (lineCount - 1));
        mListView.setLayoutParams(params);
    }

    /**
     * 停止当前所有属性动画并重置透明度
     */
    private void stopAnimator() {
        // 停止动画
        for (AnimatorSet anSet : mAnimatorSetList) {
            anSet.cancel();
        }
        mAnimatorSetList.clear();
    }

    /**
     * 重置透明度
     */
    private void resetAlpha() {
        for (int i = 0; i < mListView.getChildCount(); i++) {
            View view = mListView.getChildAt(i);
            view.setAlpha(1f);
        }
    }

    /**
     * 继续可视范围内所有动画
     */
    private void continueAllAnimator() {
        int startPos = mListView.getFirstVisiblePosition();

        for (int i = 0; i < mListView.getChildCount(); i++) {
            View view = mListView.getChildAt(i);
            if (null != view && startPos + i < listMessage.size()) {
                continueAnimator(view, startPos + i, listMessage.get(startPos + i));
            }
        }
    }

    /**
     * 重载notifyDataSetChanged方法实现渐消动画并动态调整ListView高度
     */
    @Override
    public void notifyDataSetChanged() {
        if(isPc) {
            super.notifyDataSetChanged();
            mListView.post(new Runnable() {
                @Override
                public void run() {
                    mListView.setSelection(mListView.getCount() - 1);
                }
            });
        }else{
            Log.v(TAG, "notifyDataSetChanged->scroll: " + mScrolling);

            if (mScrolling) {
                // 滑动过程中不刷新
                super.notifyDataSetChanged();
                return;
            }

            // 删除多余项
            clearFinishItem();

            if (mBLiveAnimator) {
                // 停止之前动画W
                stopAnimator();

                // 清除动画
                mAnimatorSetList.clear();
            }

            super.notifyDataSetChanged();

            // 重置ListView高度
            redrawListViewHeight();

            if (mBLiveAnimator && listMessage.size() >= MAXITEMCOUNT) {
                continueAllAnimator();
            }

            // 自动滚动到底部
            mListView.post(new Runnable() {
                @Override
                public void run() {
                    mListView.setSelection(mListView.getCount() - 1);
                }
            });
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case SCROLL_STATE_FLING:
                break;
            case SCROLL_STATE_TOUCH_SCROLL:
                if (mBLiveAnimator) {
                    // 开始滚动时停止所有属性动画
//                    stopAnimator();
//                    resetAlpha();
                }
                mScrolling = true;
                break;
            case SCROLL_STATE_IDLE:
                mScrolling = false;
                if (mBLiveAnimator) {
                    // 停止滚动时播放渐消动画
//                    playDisappearAnimator();
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
