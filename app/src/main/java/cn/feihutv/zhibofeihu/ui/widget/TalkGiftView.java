package cn.feihutv.zhibofeihu.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.feihutv.zhibofeihu.utils.SoftKeyBoardListener;


/**
 * Created by huanghao on 2017/4/13.
 */

public class TalkGiftView extends LinearLayout {
    @BindView(R.id.live_img_gift)
    ImageView liveImgGift;
    @BindView(R.id.et_input_message)
    public EditText messageTextView;
    @BindView(R.id.img_face)
    ImageView faceImg;
    @BindView(R.id.confrim_btn)
    Button confirmBtn;
    @BindView(R.id.face_pager)
    ViewPager facePager;
    @BindView(R.id.face_dots_container)
    LinearLayout mDotsLayout;
    @BindView(R.id.ll_face_container)
    LinearLayout faceView;
    @BindView(R.id.gift_img)
    ViewPager giftImg;
    @BindView(R.id.points)
    LinearLayout points;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.coin)
    TextView coin;
    @BindView(R.id.charge)
    TextView charge;
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.send_view)
    LinearLayout sendView;
    @BindView(R.id.donate)
    Button donate;
    @BindView(R.id.ll_gift_container)
    LinearLayout llGiftContainer;
    @BindView(R.id.inputBar)
    LinearLayout inputBar;
    @BindView(R.id.top_view)
    RelativeLayout topView;
    @BindView(R.id.img_laba)
    ImageView imgLaba;
    @BindView(R.id.text_xiaolaba)
    TextView text_xiaolaba;
    @BindView(R.id.text_xiaolaba_count)
    TextView textXiaolabaCount;
    @BindView(R.id.text_left)
    TextView textLeft;
    private View rootView;
    private Context mContext;
    private InputMethodManager imm;
    // 7列3行
    private int columns = 6;
    private int rows = 4;
    private List<String> staticFacesList;
    private List<View> views = new ArrayList<View>();
    private Handler handler = new Handler();
    private boolean mDanmuOpen = false;
    private CharSequence temp;

    @OnClick(R.id.img_laba)
    public void onClick() {

        mDanmuOpen = !mDanmuOpen;
        if (mDanmuOpen) {
            MobclickAgent.onEvent(getContext(), "10022");
            imgLaba.setImageResource(R.drawable.icon_pc_yellow);
            messageTextView.setHint("使用小喇叭发送的消息将全站公告");
            topView.setVisibility(View.VISIBLE);
            messageTextView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
            if (temp != null) {
                int left = 50 - temp.length();
                textLeft.setText("还能输入" + left + "字");
            }
            if (mOnTextSendListener != null) {
                mOnTextSendListener.showLogin();
            }
        } else {
            imgLaba.setImageResource(R.drawable.icon_pc_gray);
            messageTextView.setHint("说点什么好了...");
            topView.setVisibility(View.GONE);
            messageTextView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
        }
    }

    public interface OnTextSendListener {
        void onTextSend(String msg, boolean mDanmuOpen);

        void OnEditClick();

        void giftClick();

        void OnClick();

        void keyBoardHide();

        void showLogin();
    }

    private OnTextSendListener mOnTextSendListener;

    public void setmOnTextSendListener(OnTextSendListener onTextSendListener) {
        this.mOnTextSendListener = onTextSendListener;
    }

    public TalkGiftView(Context context) {
        this(context, null, 0);
    }

    public TalkGiftView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TalkGiftView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        softKeyboardListnenr();
        initView(context);
//        initViewPager();
        initListener();
    }

    public void setEditTextTip(String tip) {
        messageTextView.setHint(tip);
    }

    private void initListener() {
        confirmBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = messageTextView.getText().toString().trim();
                if (!TextUtils.isEmpty(msg)) {
                    if (faceView.getVisibility() == View.GONE) {

                    } else {
                        faceView.setVisibility(View.GONE);
                    }
                    mOnTextSendListener.onTextSend(msg, mDanmuOpen);
                    imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
                    messageTextView.setText("");
                } else {
                    Toast.makeText(mContext, "输入内容不能为空！", Toast.LENGTH_LONG).show();
                }
                messageTextView.setText(null);
            }
        });
        messageTextView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                faceView.setVisibility(View.GONE);
                if (mOnTextSendListener != null) {
                    mOnTextSendListener.OnEditClick();
                }
                if (mOnTextSendListener != null) {
                    mOnTextSendListener.OnClick();
                }
                return false;
            }
        });
        messageTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        messageTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if(labaToggleButton.getState()== LabaToggleButton.ToggleState.Close) {
                temp = charSequence;
//                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mDanmuOpen) {
                    if (temp != null) {
                        int left = 50 - temp.length();
                        textLeft.setText("还能输入" + left + "字");
                    }
                }
            }
        });
        messageTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case KeyEvent.KEYCODE_ENDCALL:
                    case KeyEvent.KEYCODE_ENTER:
                        if (messageTextView.getText().length() > 0) {
                            imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
                        } else {
                            Toast.makeText(mContext, "输入内容不能为空！", Toast.LENGTH_LONG).show();
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });
        faceImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (faceView.getVisibility() == View.GONE) {
                    imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
                    faceView.setVisibility(View.VISIBLE);
                } else {
                    faceView.setVisibility(View.GONE);
                    imm.showSoftInput(messageTextView, 2);
                }
            }
        });
        liveImgGift.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnTextSendListener != null) {
                    mOnTextSendListener.giftClick();
                }
            }
        });
    }

    public void setGiftImgGone() {
        liveImgGift.setVisibility(GONE);
    }

    public void setLabaImgGone() {
        imgLaba.setVisibility(GONE);
        messageTextView.setPadding(20, 0, 0, 0);

    }

    public boolean isTextNull() {
        if (TextUtils.isEmpty(messageTextView.getText())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 软键盘显示与隐藏的监听
     */
    private void softKeyboardListnenr() {
        SoftKeyBoardListener.setListener((Activity) mContext, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {/*软键盘显示：执行隐藏title动画，并修改listview高度和装载礼物容器的高度*/
                AppLogger.e("softKeyboardListnenr+keyBoardShow");
            }

            @Override
            public void keyBoardHide(int height) {/*软键盘隐藏：隐藏聊天输入框并显示聊天按钮，执行显示title动画，并修改listview高度和装载礼物容器的高度*/
                AppLogger.e("softKeyboardListnenr+keyBoardHide");
                if (mOnTextSendListener != null) {
                    mOnTextSendListener.keyBoardHide();
                }
                messageTextView.setHint("说点什么吧...");
            }
        });
    }

    public boolean isFaceViewVisual() {
        if (faceView.getVisibility() == VISIBLE) {
            return true;
        } else {
            return false;
        }
    }

    public void hideSoftInputFromWindow() {
        if (messageTextView != null)
            imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
        if (faceView != null)
            faceView.setVisibility(GONE);
    }

    public void showSoftInputFromWindow() {
        // inputBar.requestFocus();
        if (messageTextView != null) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    imm.showSoftInput(messageTextView, InputMethodManager.SHOW_FORCED);
                }
            }, 100);
        }

    }

    private void initView(Context context) {
        rootView = View.inflate(context, R.layout.talk_gift_view, this);
        ButterKnife.bind(this, rootView);
        imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        initStaticFaces();
        facePager.addOnPageChangeListener(new PageChange());
        messageTextView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
        int count = SharePreferenceUtil.getSessionInt(mContext, "PREF_KEY_XIAOLABA_COUNT");
        textXiaolabaCount.setText("(余" + count + "个)");
    }

    public void onFreshXiaolabaCount() {
        if (text_xiaolaba != null) {
            int count = SharePreferenceUtil.getSessionInt(mContext, "PREF_KEY_XIAOLABA_COUNT");
            textXiaolabaCount.setText("(余" + count + "个)");
        }
    }

    /**
     * 初始化表情列表staticFacesList
     */
    private void initStaticFaces() {
        try {
            staticFacesList = new ArrayList<String>();
            String[] faces = mContext.getAssets().list("face/png");
            //将Assets中的表情名称转为字符串一一添加进staticFacesList
            for (int i = 0; i < faces.length; i++) {
                staticFacesList.add(faces[i]);
            }
            //去掉删除图片
            staticFacesList.remove("emotion_del_normal.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * 初始表情 *
	 */
//    private void initViewPager() {
//        // 获取页数
//        for (int i = 0; i < getPagerCount(); i++) {
//            views.add(viewPagerItem(i));
//            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(16, 16);
//            mDotsLayout.addView(dotsItem(i), params);
//        }
//        FaceVPAdapter mVpAdapter = new FaceVPAdapter(views);
//        facePager.setAdapter(mVpAdapter);
//        mDotsLayout.getChildAt(0).setSelected(true);
//    }

    /**
     * 表情页改变时，dots效果也要跟着改变
     */
    class PageChange implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            for (int i = 0; i < mDotsLayout.getChildCount(); i++) {
                mDotsLayout.getChildAt(i).setSelected(false);
            }
            mDotsLayout.getChildAt(arg0).setSelected(true);
        }
    }

    /**
     * 根据表情数量以及GridView设置的行数和列数计算Pager数量
     *
     * @return
     */
    private int getPagerCount() {
        int count = staticFacesList.size();
        return count % (columns * rows - 1) == 0 ? count / (columns * rows - 1)
                : count / (columns * rows - 1) + 1;
    }

//    private View viewPagerItem(int position) {
//        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
//        View layout = inflater.inflate(R.layout.face_gridview, null);//表情布局
//        GridView gridview = (GridView) layout.findViewById(R.id.chart_face_gv);
//        /**
//         * 注：因为每一页末尾都有一个删除图标，所以每一页的实际表情columns *　rows　－　1; 空出最后一个位置给删除图标
//         * */
//        List<String> subList = new ArrayList<String>();
//        subList.addAll(staticFacesList
//            .subList(position * (columns * rows - 1),
//                (columns * rows - 1) * (position + 1) > staticFacesList
//                    .size() ? staticFacesList.size() : (columns
//                    * rows - 1)
//                    * (position + 1)));
//        /**
//         * 末尾添加删除图标
//         * */
//        subList.add("emotion_del_normal.png");
//        FaceGVAdapter mGvAdapter = new FaceGVAdapter(subList, mContext);
//        gridview.setAdapter(mGvAdapter);
//        gridview.setNumColumns(columns);
//        // 单击表情执行的操作
//        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                try {
//                    String png = ((TextView) ((LinearLayout) view).getChildAt(1)).getText().toString();
//                    if (!png.contains("emotion_del_normal")) {// 如果不是删除图标
//                        insert(getFace(png));
//                    } else {
//                        delete();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        return gridview;
//    }

    /**
     * 向输入框里添加表情
     */
    private void insert(CharSequence text) {
        int iCursorStart = Selection.getSelectionStart((messageTextView.getText()));
        int iCursorEnd = Selection.getSelectionEnd((messageTextView.getText()));
        if (iCursorStart != iCursorEnd) {
            ((Editable) messageTextView.getText()).replace(iCursorStart, iCursorEnd, "");
        }
        int iCursor = Selection.getSelectionEnd((messageTextView.getText()));
        ((Editable) messageTextView.getText()).insert(iCursor, text);
    }

    /**
     * 删除图标执行事件
     * 注：如果删除的是表情，在删除时实际删除的是tempText即图片占位的字符串，所以必需一次性删除掉tempText，才能将图片删除
     */
    private void delete() {
        if (messageTextView.getText().length() != 0) {
            int iCursorEnd = Selection.getSelectionEnd(messageTextView.getText());
            int iCursorStart = Selection.getSelectionStart(messageTextView.getText());
            if (iCursorEnd > 0) {
                if (iCursorEnd == iCursorStart) {
                    if (isDeletePng(iCursorEnd)) {
                        String st = "[f_static_000]";
                        ((Editable) messageTextView.getText()).delete(
                                iCursorEnd - st.length(), iCursorEnd);
                    } else {
                        ((Editable) messageTextView.getText()).delete(iCursorEnd - 1,
                                iCursorEnd);
                    }
                } else {
                    ((Editable) messageTextView.getText()).delete(iCursorStart,
                            iCursorEnd);
                }
            }
        }
    }

    /**
     * 判断即将删除的字符串是否是图片占位字符串tempText 如果是：则讲删除整个tempText
     **/
    private boolean isDeletePng(int cursor) {
        String st = "[f_static_000]";
        String content = messageTextView.getText().toString().substring(0, cursor);
        if (content.length() >= st.length()) {
            String checkStr = content.substring(content.length() - st.length(),
                    content.length());
            String regex = "(\\[f_static_)\\d{1,3}(\\])";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(checkStr);
            return m.matches();
        }
        return false;
    }

    private SpannableStringBuilder getFace(String png) {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        try {
            /**
             * 经过测试，虽然这里tempText被替换为png显示，但是但我单击发送按钮时，获取到輸入框的内容是tempText的值而不是png
             * 所以这里对这个tempText值做特殊处理
             * 格式：#[face/png/f_static_000.png]#，以方便判斷當前圖片是哪一個
             * */
            String tempText = "[" + png + "]";
            sb.append(tempText);
            sb.setSpan(
                    new ImageSpan(mContext, BitmapFactory
                            .decodeStream(mContext.getAssets().open("face/png/" + png + ".png"))), sb.length()
                            - tempText.length(), sb.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb;
    }

//    private ImageView dotsItem(int position) {
//        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
//        View layout = inflater.inflate(R.layout.dot_image, null);
//        ImageView iv = (ImageView) layout.findViewById(R.id.face_dot);
//        iv.setId(position);
//        return iv;
//    }
}
