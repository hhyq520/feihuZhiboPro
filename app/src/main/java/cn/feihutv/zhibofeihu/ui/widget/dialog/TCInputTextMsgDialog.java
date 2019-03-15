package cn.feihutv.zhibofeihu.ui.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.umeng.analytics.MobclickAgent;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.ui.widget.LabaToggleButton;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.feihutv.zhibofeihu.utils.SoftKeyBoardListener;

/**
 * 文本输入框
 */
public class TCInputTextMsgDialog extends Dialog {

    public interface OnTextSendListener {
        void showLogin();
        void onTextSend(String msg, boolean tanmuOpen);
    }

    private TextView confirmBtn;
    private TextView textXiaolaba;
    private LinearLayout mBarrageArea;
    private EditText messageTextView;
    private static final String TAG = TCInputTextMsgDialog.class.getSimpleName();
    private Context mContext;
    private InputMethodManager imm;
    private RelativeLayout rlDlg;
    private int mLastDiff = 0;
    private LinearLayout mConfirmArea;
    private OnTextSendListener mOnTextSendListener;
    private LinearLayout rldlgview;
    private boolean mDanmuOpen = false;
    private FrameLayout editView;
    private RelativeLayout topView;
    private TextView countText;
    private TextView text_xiaolaba_count;
    private TextView text_left;
    private LabaToggleButton labaToggleButton;
//    private final String reg = "[`~@#$%^&*()-_+=|{}':;,/.<>￥…（）—【】‘；：”“’。，、]";
//    private Pattern pattern = Pattern.compile(reg);
    private CharSequence temp;
    public TCInputTextMsgDialog(final Context context, int theme) {
        super(context, theme);
        mContext = context;
        setContentView(R.layout.dialog_input_text);
        softKeyboardListnenr();
        imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        messageTextView = (EditText) findViewById(R.id.et_input_message);
//        messageTextView.setInputType(InputType.TYPE_CLASS_TEXT);
        messageTextView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
        topView = (RelativeLayout) findViewById(R.id.top_view);
        topView.setVisibility(View.GONE);
        countText = (TextView) findViewById(R.id.confrim_btn);
        confirmBtn = (TextView) findViewById(R.id.confrim_btn);
        textXiaolaba=(TextView) findViewById(R.id.text_xiaolaba);
        text_xiaolaba_count=(TextView) findViewById(R.id.text_xiaolaba_count);
        text_left=(TextView) findViewById(R.id.text_left);
        labaToggleButton=(LabaToggleButton) findViewById(R.id.laba_togglebutton);
        labaToggleButton.setState(LabaToggleButton.ToggleState.Open);
        int count=SharePreferenceUtil.getSessionInt(mContext,"PREF_KEY_XIAOLABA_COUNT");
        text_xiaolaba_count.setText("(余"+count+"个)");
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = messageTextView.getText().toString().trim();
                if (!TextUtils.isEmpty(msg)) {
                    mOnTextSendListener.onTextSend(msg, mDanmuOpen);
                    imm.showSoftInput(messageTextView, InputMethodManager.SHOW_FORCED);
                    imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
                    messageTextView.setText("");
                    dismiss();
                } else {
                    Toast.makeText(mContext, "输入不能为空!", Toast.LENGTH_LONG).show();
                }
                messageTextView.setText(null);
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
                if(labaToggleButton.getState()== LabaToggleButton.ToggleState.Close) {
                    int left=50-temp.length();
                    text_left.setText("还能输入"+left+"字");
                }
            }
        });


        labaToggleButton.SetOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(labaToggleButton.getState()== LabaToggleButton.ToggleState.Open){
                    mDanmuOpen=false;
                    messageTextView.setHint("说点什么好了...");
                    topView.setVisibility(View.GONE);
                    messageTextView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
                }else{
                    if(mOnTextSendListener!=null){
                        mOnTextSendListener.showLogin();
                    }
                    mDanmuOpen=true;
                    MobclickAgent.onEvent(context, "10022");
                    messageTextView.setHint("使用小喇叭发送的消息将全站公告");
                    if(temp!=null) {
                        int left = 50 - temp.length();
                        text_left.setText("还能输入" + left + "字");
                    }
                    topView.setVisibility(View.VISIBLE);
                    messageTextView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
                }
            }
        });


        mBarrageArea = (LinearLayout) findViewById(R.id.barrage_area);
//        mBarrageArea.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mDanmuOpen = !mDanmuOpen;
//                if (mDanmuOpen) {
//                    labaToggleButton.setState(LabaToggleButton.ToggleState.Open);
//                    messageTextView.setHint("使用小喇叭发送的消息将全站公告");
//                    topView.setVisibility(View.VISIBLE);
//                    messageTextView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
//                } else {
//                    labaToggleButton.setState(LabaToggleButton.ToggleState.Close);
//                    messageTextView.setHint("说点什么好了...");
//                    topView.setVisibility(View.GONE);
//                    messageTextView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
//                }
//            }
//        });

        messageTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case KeyEvent.KEYCODE_ENDCALL:
                    case KeyEvent.KEYCODE_ENTER:
                        if (messageTextView.getText().length() > 0) {
//                            mOnTextSendListener.onTextSend("" + messageTextView.getText(), mDanmuOpen);
                            //sendText("" + messageTextView.getText());
                            //imm.showSoftInput(messageTextView, InputMethodManager.SHOW_FORCED);
                            imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
//                            messageTextView.setText("");
                            dismiss();
                        } else {
                            Toast.makeText(mContext, "输入不能为空", Toast.LENGTH_LONG).show();
                        }
                        return true;
                    case KeyEvent.KEYCODE_BACK:
                        dismiss();
                        return false;
                    default:
                        return false;
                }
            }
        });

        mConfirmArea = (LinearLayout) findViewById(R.id.confirm_area);
        mConfirmArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = messageTextView.getText().toString().trim();
                if (!TextUtils.isEmpty(msg)) {

                    mOnTextSendListener.onTextSend(msg, mDanmuOpen);
                    imm.showSoftInput(messageTextView, InputMethodManager.SHOW_FORCED);
                    imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
                    messageTextView.setText("");
                    dismiss();
                } else {
                    Toast.makeText(mContext, "输入不能为空", Toast.LENGTH_LONG).show();
                }
                messageTextView.setText(null);
            }
        });

        messageTextView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Log.d("My test", "onKey " + keyEvent.getCharacters());
                return false;
            }
        });

        rlDlg = (RelativeLayout) findViewById(R.id.rl_outside_view);
        rlDlg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() != R.id.rl_inputdlg_view)
                    dismiss();
            }
        });

        rldlgview = (LinearLayout) findViewById(R.id.rl_inputdlg_view);
        editView = (FrameLayout) findViewById(R.id.edit_view);
        rldlgview.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                Rect r = new Rect();
                //获取当前界面可视部分
                getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                //获取屏幕的高度
                int screenHeight = getWindow().getDecorView().getRootView().getHeight();
                //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
                int heightDifference = screenHeight - r.bottom;

                if (heightDifference <= 0 && mLastDiff > 0) {
//                    imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
//                    dismiss();
                }
                mLastDiff = heightDifference;
            }
        });
        rldlgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
                dismiss();
            }
        });
    }

    public void onFreshXiaolabaCount(){
        if(textXiaolaba!=null) {
            int count = SharePreferenceUtil.getSessionInt(mContext, "PREF_KEY_XIAOLABA_COUNT");
            text_xiaolaba_count.setText("(余"+count+"个)");
        }
    }

    public void setmOnTextSendListener(OnTextSendListener onTextSendListener) {
        this.mOnTextSendListener = onTextSendListener;
    }

    /**
     * 软键盘显示与隐藏的监听
     */
    private void softKeyboardListnenr() {
        SoftKeyBoardListener.setListener((Activity) mContext, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {/*软键盘显示：执行隐藏title动画，并修改listview高度和装载礼物容器的高度*/
            }

            @Override
            public void keyBoardHide(int height) {/*软键盘隐藏：隐藏聊天输入框并显示聊天按钮，执行显示title动画，并修改listview高度和装载礼物容器的高度*/
                dismiss();
                imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);

            }
        });
    }

    public void hideKeyBoard() {
        if (imm.isActive()) {//如果开启
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);//关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
        }
    }

    @Override
    public void dismiss() {

        //dismiss之前重置mLastDiff值避免下次无法打开
        mLastDiff = 0;
        imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
        super.dismiss();
    }


    @Override
    public void show() {
        super.show();
    }
}
