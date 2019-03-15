package cn.feihutv.zhibofeihu.ui.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.db.model.SysGiftNewBean;
import cn.feihutv.zhibofeihu.ui.live.OnItemClickListener;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;

/**
 * Created by huanghao on 2017/4/22.
 */

public class GiftView {
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.points)
    LinearLayout points;
    @BindView(R.id.text_empty)
    TextView textViewEmpty;
    private Context mContext;
    private View rootView;
    private List<SysGiftNewBean> datas;

    private ImageView[] ivPoints;//小圆点图片的集合
    private int totalPage; //总的页数
    private int mPageSize = 8; //每页显示的最大的数量
    private List<View> viewPagerList;//GridView作为一个View对象添加到ViewPager集合中
    private boolean isBagGift;
    private int selectGiftId=-1;
    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener=listener;
    }
    final List<GiftAdapter> adapters = new ArrayList<>();
    public GiftView(Context mContext, List<SysGiftNewBean> datas, boolean isBagGift) {
        this.mContext = mContext;
        this.datas = datas;
        this.isBagGift=isBagGift;
        rootView = LayoutInflater.from(mContext).inflate(
                R.layout.gift_view, null);
        ButterKnife.bind(this, rootView);
        intit();
    }

    public void setDatas(List<SysGiftNewBean> datas){
        this.datas = datas;
        if(isBagGift&&datas.size()==0){
            textViewEmpty.setVisibility(View.VISIBLE);
        }else{
            textViewEmpty.setVisibility(View.GONE);
        }
        if(!isBagGift) {
            selectShowGifts();
        }
        for (int i=0;i<adapters.size();i++) {
            adapters.get(i).setDatas(datas,i,10,isBagGift);
        }
    }

    private void selectShowGifts(){
        if(datas!=null && datas.size()>0){
            for (int i = datas.size()-1; i >=0; i--) {
                SysGiftNewBean item = datas.get(i);
                long chazhi= SharePreferenceUtil.getSessionLong(FeihuZhiboApplication.getApplication(), "chazhi");
                long second = (long)(System.currentTimeMillis()/1000)+chazhi;
                if (second >= Long.valueOf(item.getShelfBegin()) && second<= Long.valueOf(item.getShelfEnd())){

                }else{
                    datas.remove(item);
                }
            }
        }
    }

    private void intit() {
        if(!isBagGift) {
            selectShowGifts();
        }
        for (SysGiftNewBean info : datas) {
            info.setSelected(false);
        }
        if(isBagGift&&datas.size()==0){
            textViewEmpty.setVisibility(View.VISIBLE);
        }else{
            textViewEmpty.setVisibility(View.GONE);
        }
        totalPage = (int) Math.ceil(datas.size() * 1.0 / mPageSize);
        for (int i = 0; i < totalPage; i++) {
            GiftAdapter adapter = new GiftAdapter(mContext, datas, i, mPageSize,isBagGift);
            adapters.add(adapter);
        }
        viewPagerList = new ArrayList<View>();
        for (int i = 0; i < totalPage; i++) {
            //每个页面都是inflate出一个新实例
            final GridView gridView = (GridView) View.inflate(mContext, R.layout.item_gridview, null);
//            final GiftAdapter adapter=new GiftAdapter(mContext, listDatas, i, mPageSize);
//            gridView.setVerticalSpacing(TCUtils.dip2px(mContext,2));
//            gridView.setHorizontalSpacing(TCUtils.dip2px(mContext,2));
            gridView.setAdapter(adapters.get(i));
            //添加item点击监听
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int position, long arg3) {
                    Object obj = gridView.getItemAtPosition(position);
                    if (obj != null && obj instanceof SysGiftNewBean) {
                        selectGiftId = Integer.valueOf(((SysGiftNewBean) obj).getId());
                        for (SysGiftNewBean info : datas) {
                            info.setSelected(false);
                        }
                        datas.get((int) arg3).setSelected(true);
                        for (GiftAdapter info : adapters) {
                            info.notifyDataSetChanged();
                        }
                        if(onItemClickListener!=null){
                            onItemClickListener.onItemClick(selectGiftId);
                        }
                    }
                }
            });
            //每一个GridView作为一个View对象添加到ViewPager集合中
            viewPagerList.add(gridView);
        }
        //设置ViewPager适配器
        viewpager.setAdapter(new MyViewPagerAdapter(viewPagerList) {
        });

        //添加小圆点
        ivPoints = new ImageView[totalPage];
        for (int i = 0; i < totalPage; i++) {
            //循坏加入点点图片组
            ivPoints[i] = new ImageView(mContext);
            if (i == 0) {
                ivPoints[i].setImageResource(R.drawable.page_focuese);
            } else {
                ivPoints[i].setImageResource(R.drawable.dot_focus);
            }
            ivPoints[i].setPadding(8, 8, 8, 8);
            points.addView(ivPoints[i]);
        }
        //设置ViewPager的滑动监听，主要是设置点点的背景颜色的改变
        viewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                //currentPage = position;
                for (int i = 0; i < totalPage; i++) {
                    if (i == position) {
                        ivPoints[i].setImageResource(R.drawable.page_focuese);
                    } else {
                        ivPoints[i].setImageResource(R.drawable.dot_focus);
                    }
                }
            }
        });
    }

    public int getSelectGiftID(){
        return selectGiftId;
    }

    public class MyViewPagerAdapter extends PagerAdapter {

        private List<View> viewList;//View就二十GridView


        public MyViewPagerAdapter(List<View> viewList) {
            this.viewList = viewList;
        }

        @Override
        public int getCount() {
            return viewList != null ? viewList.size() : 0;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        /**
         * 将当前的View添加到ViewGroup容器中
         * 这个方法，return一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPage上
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));
            return viewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
    public View getView() {
        return rootView;
    }
}
