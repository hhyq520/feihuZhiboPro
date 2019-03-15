package cn.feihutv.zhibofeihu.ui.widget.dialog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.db.model.RecentItem;
import cn.feihutv.zhibofeihu.ui.widget.swipelistview.SwipeListView;
import cn.feihutv.zhibofeihu.utils.TCGlideCircleTransform;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RecentAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<RecentItem> mData;
    private SwipeListView mListView;
    private Context mContext;

    public RecentAdapter(Context context, List<RecentItem> data,
                         SwipeListView listview) {
        mContext = context;
        this.mInflater = LayoutInflater.from(context);
        mData = data;
        this.mListView = listview;
    }

    public void setData(List<RecentItem> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void remove(int position) {
        if (position < mData.size()) {
            mData.remove(position);
            notifyDataSetChanged();
        }
    }

    public void remove(RecentItem item) {
        if (mData.contains(item)) {
            mData.remove(item);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder myViewHolder = null;
        final RecentItem item = mData.get(position);
        if (convertView != null) {
            myViewHolder = (MyViewHolder) convertView.getTag();
        } else {
            convertView = mInflater.inflate(R.layout.recent_contact_item_view, null);
            myViewHolder = new MyViewHolder();
            myViewHolder.nickTV = (TextView) convertView.findViewById(R.id.recent_list_item_name);
            myViewHolder.msgTV = (TextView) convertView.findViewById(R.id.recent_list_item_msg);
            myViewHolder.numTV = (TextView) convertView.findViewById(R.id.unreadmsg);
            myViewHolder.timeTV = (TextView) convertView.findViewById(R.id.recent_list_item_time);
            myViewHolder.headIV = (ImageView) convertView.findViewById(R.id.icon);
            myViewHolder.imgMsgStatus = (ImageView) convertView.findViewById(R.id.img_msg_status);
            myViewHolder.deleteBtn = (Button) convertView.findViewById(R.id.recent_del_btn);
            myViewHolder.view_dot = convertView.findViewById(R.id.view_dot);
            convertView.setTag(myViewHolder);
        }
        myViewHolder.nickTV.setText(item.getName());
        myViewHolder.msgTV.setText(item.getMessage());
        if(item.getTime()==0){
            myViewHolder.timeTV.setVisibility(View.GONE);
        }else{
            myViewHolder.timeTV.setVisibility(View.VISIBLE);
            myViewHolder.timeTV.setText(TCUtils.getChatTime(item.getTime()));
        }

        Glide.with(mContext)
                .load(item.getHeadImg())
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE).transform(new TCGlideCircleTransform(mContext)))
                .into(myViewHolder.headIV);
        myViewHolder.deleteBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.remove(position);
                new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                        .deleteRecentItem(item)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(@NonNull Object obj) throws Exception {

                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {

                            }
                        })

                );
                notifyDataSetChanged();
                if (mListView != null)
                    mListView.closeOpenedItems();
            }
        });
        if (item.getIsRead()) {
            myViewHolder.view_dot.setVisibility(View.GONE);
        } else {
            myViewHolder.view_dot.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    class MyViewHolder {
        TextView nickTV, msgTV, numTV, timeTV;
        Button deleteBtn;
        ImageView headIV, imgMsgStatus;
        View view_dot;
    }
}
