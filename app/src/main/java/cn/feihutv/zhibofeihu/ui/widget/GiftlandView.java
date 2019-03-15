package cn.feihutv.zhibofeihu.ui.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.db.model.SysGiftNewBean;
import cn.feihutv.zhibofeihu.data.local.model.SysDataEntity;
import cn.feihutv.zhibofeihu.ui.live.OnItemClickListener;
import cn.feihutv.zhibofeihu.ui.widget.dialog.adapter.GiftLandAdapter;
import cn.feihutv.zhibofeihu.utils.ListViewDecoration;
import cn.feihutv.zhibofeihu.utils.TCUtils;

/**
 * Created by huanghao on 2017/4/25.
 */

public class GiftlandView {
    @BindView(R.id.gift_gridView)
    RecyclerView giftGridView;
    @BindView(R.id.text_empty)
    TextView textViewEmpty;
    private Context mContext;
    private View rootView;
    private List<SysGiftNewBean> datas;
    private GiftLandAdapter adapter;
    private boolean isBagGift;
    private int selectGiftId=-1;
    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener=listener;
    }
    public GiftlandView(Context mContext, List<SysGiftNewBean> datas, boolean isBagGift) {
        this.mContext = mContext;
        this.datas=datas;
        this.isBagGift=isBagGift;
        rootView = LayoutInflater.from(mContext).inflate(
                R.layout.gift_land_view, null);
        ButterKnife.bind(this, rootView);
        intit();
    }

    public void setDatas(List<SysGiftNewBean> datas){
        this.datas = datas;
        if(adapter!=null){
            adapter.setDatas(datas,isBagGift);
        }
        if(isBagGift){
            if(datas==null || (datas!=null&&datas.size()==0)){
                textViewEmpty.setVisibility(View.VISIBLE);
            }else{
                textViewEmpty.setVisibility(View.GONE);
            }

        }
    }
    private void intit() {
        for (SysGiftNewBean info : datas) {
            info.setSelected(false);
        }
        if(isBagGift){
            if(datas==null || (datas!=null&&datas.size()==0)){
                textViewEmpty.setVisibility(View.VISIBLE);
            }else{
                textViewEmpty.setVisibility(View.GONE);
            }

        }else{
            textViewEmpty.setVisibility(View.GONE);
        }
        adapter = new GiftLandAdapter(mContext, datas,isBagGift);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        final int leftRightPadding = TCUtils.dipToPx(mContext,1);
        final int bottom = TCUtils.dipToPx(mContext,0);
        giftGridView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(0,0,0,0);
//                super.getItemOffsets(outRect, view, parent, state);
            }
        });
        giftGridView.setLayoutManager(linearLayoutManager);
//        giftGridView.addItemDecoration(new ListViewDecoration(1));
        giftGridView.setAdapter(adapter);
        adapter.setGiftLandItemListener(new GiftLandAdapter.GiftLandItemListener() {

            @Override
            public void itemClick(int pos) {
                    selectGiftId = Integer.valueOf(datas.get(pos).getId());
                    for (SysGiftNewBean info : datas) {
                        info.setSelected(false);
                    }
                    datas.get(pos).setSelected(true);
                    adapter.notifyDataSetChanged();
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClick(selectGiftId);
                }
            }
        });
    }

    public int getSelectGiftID(){
        return selectGiftId;
    }

    public View getView() {
        return rootView;
    }
}
