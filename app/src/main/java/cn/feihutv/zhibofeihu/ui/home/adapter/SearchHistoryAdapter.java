package cn.feihutv.zhibofeihu.ui.home.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.db.model.SearchHistoryInfo;

/**
 * Created by huanghao on 2017/4/8.
 */

public class SearchHistoryAdapter extends BaseAdapter {
    private Context mContext;
    private List<SearchHistoryInfo> searchHistoryInfos;
    private LayoutInflater mInflater;

    public SearchHistoryAdapter(Context context, List<SearchHistoryInfo> searchHistoryInfos) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.searchHistoryInfos = searchHistoryInfos;
    }

    @Override
    public int getCount() {
        return searchHistoryInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return searchHistoryInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder viewHolder = null;
        if (convertView != null) {
            viewHolder = (MyViewHolder) convertView.getTag();
        } else {
            viewHolder = new MyViewHolder();
            convertView = mInflater.inflate(R.layout.search_history_item_view, null);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.text_content);
            viewHolder.ivClose = (ImageView) convertView.findViewById(R.id.iv_delete);
            convertView.setTag(viewHolder);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.click(position);
                }
            }
        });
        viewHolder.tvContent.setText(searchHistoryInfos.get(position).getContent());
        viewHolder.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.ivClick(position);
                }
            }
        });

        return convertView;
    }

    public interface ClearHistoryListener {
        void click(int pos);

        void ivClick(int position);
    }

    ClearHistoryListener mListener;

    public void setClearHistoryListener(ClearHistoryListener listener) {
        mListener = listener;
    }

    class MyViewHolder {
        TextView tvContent;

        ImageView ivClose;
    }

}
