package cn.feihutv.zhibofeihu.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.feihutv.zhibofeihu.utils.TCUtils;

/**
 * @author huanghao
 * @date 2017/5/10
 */

public class TabBottomView extends FrameLayout {
    @BindView(R.id.tab_main_img)
    ImageView tabMainImg;
    @BindView(R.id.tab_main_txt)
    TextView tabMainTxt;
    @BindView(R.id.tab_main)
    LinearLayout tabMain;
    @BindView(R.id.tab_bd_img)
    ImageView tabBdImg;
    @BindView(R.id.tab_bd_txt)
    TextView tabBdTxt;
    @BindView(R.id.tab_bd)
    LinearLayout tabBd;
    @BindView(R.id.tab_kaibo)
    LinearLayout tabKaibo;
    @BindView(R.id.tab_hb_img)
    ImageView tabHbImg;
    @BindView(R.id.tab_hb_txt)
    TextView tabHbTxt;
    @BindView(R.id.tab_hb)
    LinearLayout tabHb;
    @BindView(R.id.tab_mine_img)
    ImageView tabMineImg;
    @BindView(R.id.tab_mine_txt)
    TextView tabMineTxt;
    @BindView(R.id.tab_mine)
    LinearLayout tabMine;
    @BindView(R.id.img_kaibo)
    ImageView imgKaibo;
    private Context context;

    public interface TabBottomClickListener {
        void click(int type);
    }

    private TabBottomClickListener listener;

    public void setTabBottomClickListener(TabBottomClickListener listener) {
        this.listener = listener;
    }

    public TabBottomView(Context context) {
        this(context, null);
    }

    public TabBottomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabBottomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.tab_bottom_view, this,
                true);
        ButterKnife.bind(this, view);
        init();
    }

    private void init() {
        changeState(1);
    }

    @OnClick({R.id.tab_main, R.id.tab_bd, R.id.tab_kaibo, R.id.tab_hb, R.id.tab_mine, R.id.img_kaibo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab_main:
                TCUtils.setScalse(tabMainImg);
                if (listener != null) {
                    listener.click(1);
                }
                changeState(1);
                break;
            case R.id.tab_bd:
                TCUtils.setScalse(tabBdImg);
                if (listener != null) {
                    listener.click(2);
                }
                changeState(2);
                break;
            case R.id.img_kaibo:
            case R.id.tab_kaibo:
                if (listener != null) {
                    listener.click(3);
                }
                changeState(3);
                break;
            case R.id.tab_hb:
                TCUtils.setScalse(tabHbImg);
                if (listener != null) {
                    listener.click(4);
                }
                changeState(4);
                break;
            case R.id.tab_mine:
                TCUtils.setScalse(tabMineImg);
                if (listener != null) {
                    listener.click(5);
                }

                if (SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
                    return;
                }
                changeState(5);
                break;
            default:
                break;
        }
    }


    public void changeState(int type) {
        switch (type) {
            case 1:
                tabMainImg.setSelected(true);
                tabMainTxt.setSelected(true);
                tabBdTxt.setSelected(false);
                tabBdImg.setSelected(false);
                tabHbImg.setSelected(false);
                tabHbTxt.setSelected(false);
                tabMineTxt.setSelected(false);
                tabMineImg.setSelected(false);
                break;
            case 2:
                tabMainImg.setSelected(false);
                tabMainTxt.setSelected(false);
                tabBdTxt.setSelected(true);
                tabBdImg.setSelected(true);
                tabHbImg.setSelected(false);
                tabHbTxt.setSelected(false);
                tabMineTxt.setSelected(false);
                tabMineImg.setSelected(false);
                break;
            case 3:
                break;
            case 4:
                tabMainImg.setSelected(false);
                tabMainTxt.setSelected(false);
                tabBdTxt.setSelected(false);
                tabBdImg.setSelected(false);
                tabHbImg.setSelected(true);
                tabHbTxt.setSelected(true);
                tabMineTxt.setSelected(false);
                tabMineImg.setSelected(false);
                break;
            case 5:
                tabMainImg.setSelected(false);
                tabMainTxt.setSelected(false);
                tabBdTxt.setSelected(false);
                tabBdImg.setSelected(false);
                tabHbImg.setSelected(false);
                tabHbTxt.setSelected(false);
                tabMineTxt.setSelected(true);
                tabMineImg.setSelected(true);
                break;
            default:
                break;
        }
    }
}
