package cn.feihutv.zhibofeihu.ui.me.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.me.LoadShopAccountIdListResponse;
import cn.feihutv.zhibofeihu.ui.me.ItemOnClick;


/**
 * <pre>
 *     @author : liwen.chen
 *     time   : 2017/
 *     desc   : 界面绘制
 *     version: 1.0
 * </pre>
 */

public class BeautiAdapter extends BaseAdapter {
    private Context context;
    private List<LoadShopAccountIdListResponse.LoadShopAccountIdListResponseData> datas;

    private ItemOnClick mItemOnClick;

    public void setItemOnClick(ItemOnClick itemOnClick) {
        mItemOnClick = itemOnClick;
    }

    public BeautiAdapter(Context context) {
        this.context = context;
        datas = new ArrayList<>();
    }

    public void setDatas(List<LoadShopAccountIdListResponse.LoadShopAccountIdListResponseData> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        BeautiViewHolder viewHolder = null;
        if (convertView != null) {
            viewHolder = (BeautiViewHolder) convertView.getTag();
        } else {
            viewHolder = new BeautiViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_layout_beauti, null);
            viewHolder.accountID = (TextView) convertView.findViewById(R.id.account_id);
            viewHolder.priceTxt = (TextView) convertView.findViewById(R.id.price_txt);
            viewHolder.buy = (Button) convertView.findViewById(R.id.btn_buy);
            viewHolder.linearLayout = (LinearLayout) convertView.findViewById(R.id.linearLayout);
            convertView.setTag(viewHolder);
        }
        viewHolder.accountID.setText(datas.get(position).getAccountId());
        viewHolder.priceTxt.setText(String.valueOf(datas.get(position).getPrice()));
        viewHolder.buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemOnClick != null) {
                    mItemOnClick.click(position);
                }
            }
        });
        return convertView;
    }

    class BeautiViewHolder {
        TextView accountID;
        TextView priceTxt;
        Button buy;
        LinearLayout linearLayout;
    }
}
