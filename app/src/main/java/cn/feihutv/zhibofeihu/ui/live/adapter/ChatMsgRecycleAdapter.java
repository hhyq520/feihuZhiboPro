package cn.feihutv.zhibofeihu.ui.live.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.db.model.SysFontColorBean;
import cn.feihutv.zhibofeihu.data.local.model.TCChatEntity;
import cn.feihutv.zhibofeihu.ui.live.OnStrClickListener;
import cn.feihutv.zhibofeihu.ui.widget.font.RxTextTool;
import cn.feihutv.zhibofeihu.utils.TCConstants;
import cn.feihutv.zhibofeihu.utils.TCUtils;

/**
 * Created by huanghao on 2017/12/8.
 */

public class ChatMsgRecycleAdapter extends RecyclerView.Adapter<ChatMsgRecycleAdapter.ChatViewHolder> {

    private Context context;
    private boolean isPc;
    private List<TCChatEntity> datas;

    public ChatMsgRecycleAdapter(Context context, boolean isPC) {
        this.context = context;
        datas = new ArrayList<>();
        this.isPc = isPC;
    }
    public ChatMsgRecycleAdapter(Context context, boolean isPC,List<TCChatEntity> datas) {
        this.context = context;
        this.datas = datas;
        this.isPc = isPC;
    }
    private OnStrClickListener mListener;
    public void setOnStrClickListener(OnStrClickListener listener){
        mListener=listener;
    }

    public void setDatas(List<TCChatEntity> datas) {
        this.datas = datas;
        notifyItemChanged(datas.size()-1);
    }
    public void setDatasNew(List<TCChatEntity> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.listview_msg_new_item, parent, false);
        return new ChatViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final ChatViewHolder holder, int position) {
        final TCChatEntity item = datas.get(position);

        Bitmap level=null;
        if(item.getLevel()==0){

        }else{
            TCUtils.showLevelWithUrl(context,holder.imageView,item.getLevel());
        }

        if(isPc){
            holder.sendContext.setShadowLayer(1,0,2,0x7f000000);
        }else{
            holder.sendContext.setShadowLayer(1,0,2,0xcc000000);
        }

        if (item.getType() == TCConstants.GIFT_TYPE) {
//            spanString.setSpan(new ImageSpan(context, getDefaultBitmap(level), ImageSpan.ALIGN_BOTTOM), 0, text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            holder.imageView.setVisibility(View.VISIBLE);
            final SysFontColorBean sysFontColorBean= FeihuZhiboApplication.getApplication().mDataManager.getSysFontColorBeanByKey("nickname");
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
    }

    @Override
    public int getItemCount() {
         return datas.size();
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

    class ChatViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv)
        ImageView imageView;
        @BindView(R.id.sendcontext)
        TextView sendContext;
        @BindView(R.id.linerlayout_bg)
        RelativeLayout linearLayoutBg;
        public ChatViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
