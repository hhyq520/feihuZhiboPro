package cn.feihutv.zhibofeihu.ui.me;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.feihutv.zhibofeihu.R;


/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/11/06
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class TabPageAdapter extends FragmentPagerAdapter {

    private Context context;
    List<BaseDynamicFragment> pageList;


    public TabPageAdapter(FragmentManager fm, Context context, List<BaseDynamicFragment> pageList) {
        super(fm);
        this.context = context;
        this.pageList = pageList;
    }


    @Override
    public BaseDynamicFragment getItem(int position) {
        return pageList.get(position);
    }

    @Override
    public int getCount() {
        return pageList.size();
    }

    //注意！！！这里就是我们自定义的布局tab_item
    public View getCustomView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_my_head_view, null);
        ImageView imageView = view.findViewById(R.id.tab_my_arrow);

        TextView tv = (TextView) view.findViewById(R.id.tab_text);
        switch (position) {
            case 0:
                //drawable代码在文章最后贴出
                tv.setText("全部动态");
                imageView.setVisibility(View.VISIBLE);
                break;
            case 1:
                tv.setText("MV音乐");
                imageView.setVisibility(View.GONE);
                break;
            case 2:
                tv.setText("个人中心");
                imageView.setVisibility(View.GONE);
                break;
            default:
                break;

        }

//        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) tv.getLayoutParams(); // 取控件mGrid当前的布局参数
//        linearParams.height = 100;// 当控件的高强制设成75象素
//        tv.setLayoutParams(linearParams); // 使设置好的布局参数应用到控件mGrid2
        return view;
    }


}
