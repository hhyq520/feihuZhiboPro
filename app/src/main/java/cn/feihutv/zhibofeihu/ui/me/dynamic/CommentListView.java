package cn.feihutv.zhibofeihu.ui.me.dynamic;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.me.CommenItem;

/**
 * Created by yiwei on 16/7/9.
 */
public class CommentListView extends LinearLayout {
    private int itemColor;
    private int itemSelectorColor;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private List<CommenItem> mDatas;
    private LayoutInflater layoutInflater ;

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OnItemLongClickListener getOnItemLongClickListener() {
        return onItemLongClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setDatas(List<CommenItem> datas){
        if(datas == null ){
            datas = new ArrayList<CommenItem>();
        }
        mDatas = datas;
        notifyDataSetChanged();
    }

    public List<CommenItem> getDatas(){
        return mDatas;
    }

    public CommentListView(Context context) {
        super(context);
    }

    public CommentListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public CommentListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    protected void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.PraiseListView, 0, 0);
        try {
            //textview的默认颜色
            itemColor = typedArray.getColor(R.styleable.PraiseListView_item_color, getResources().getColor(R.color.textNormal));
            itemSelectorColor = typedArray.getColor(R.styleable.PraiseListView_item_selector_color, getResources().getColor(R.color.appColor));

        }finally {
            typedArray.recycle();
        }
    }

    public void notifyDataSetChanged(){

        removeAllViews();
        if(mDatas == null || mDatas.size() == 0){
            return;
        }
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for(int i=0; i<mDatas.size(); i++){
            final int index = i;
            View view = getView(index);
            if(view == null){
                throw new NullPointerException("listview item layout is null, please check getView()...");
            }

            addView(view, index, layoutParams);
        }

    }

    private View getView(final int position){
        if(layoutInflater == null){
            layoutInflater = LayoutInflater.from(getContext());
        }
        View convertView = layoutInflater.inflate(R.layout.item_comment, null, false);

        TextView commentTv = (TextView) convertView.findViewById(R.id.commentTv);

//        final CommenItem bean = mDatas.get(position);
//        String name = bean.getUser().getNickname();
//        String id = bean.getId();
//
//        SpannableStringBuilder builder = new SpannableStringBuilder();
//        builder.append(setColorSpan(name));
//
//        if (!TextUtils.isEmpty(bean.getReplyname())) {
//
//            builder.append(" 回复 ");
//            builder.append(setColorSpan(bean.getReplyname()));
//        }
//        builder.append(": ");

//        String contentBodyStr = bean.getContent();
//        builder.append(contentBodyStr);
//
//        commentTv.setText(builder);

        commentTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(onItemClickListener!=null){
                        onItemClickListener.onItemClick(position);
                    }
            }
        });
        commentTv.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                    if(onItemLongClickListener!=null){
                        onItemLongClickListener.onItemLongClick(position);
                    }
                return false;
            }
        });

        return convertView;
    }

    @NonNull
    private SpannableString setColorSpan(final String textStr) {
        SpannableString subjectSpanText = new SpannableString(textStr);
        subjectSpanText.setSpan(new ForegroundColorSpan(Color.parseColor("#ff963a")){
                                    }, 0, subjectSpanText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return subjectSpanText;
    }

    public static interface OnItemClickListener{
        public void onItemClick(int position);
    }

    public static interface OnItemLongClickListener{
        public void onItemLongClick(int position);
    }



}
