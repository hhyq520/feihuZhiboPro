package cn.feihutv.zhibofeihu.ui.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.ui.live.OnItemClickListener;
import cn.feihutv.zhibofeihu.utils.SoftKeyBoardListener;

/**
 * Created by huanghao on 2017/8/2.
 */

public class GiftNumDialog extends Dialog {
    @BindView(R.id.et_input_message)
    EditText messageTextView;
    @BindView(R.id.edit_view)
    FrameLayout editView;
    @BindView(R.id.confrim_btn)
    Button confrimBtn;
    @BindView(R.id.confirm_area)
    LinearLayout confirmArea;
    @BindView(R.id.rl_inputdlg_view)
    LinearLayout rlInputdlgView;
    @BindView(R.id.sc)
    ScrollView sc;
    @BindView(R.id.rl_outside_view)
    RelativeLayout rlOutsideView;
    private Context mContext;
    private InputMethodManager imm;
    private OnItemClickListener mListener;
    private int mLastDiff = 0;
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }
    public GiftNumDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
        setContentView(R.layout.dialog_giftnum_text);
        ButterKnife.bind(this);
        imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inti();
    }

    private void inti() {
        softKeyboardListnenr();
        confirmArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = messageTextView.getText().toString().trim();
                if (!TextUtils.isEmpty(msg)) {
                   if(mListener!=null){
                       mListener.onItemClick(Integer.valueOf(msg));
                   }
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

        confrimBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = messageTextView.getText().toString().trim();
                if (!TextUtils.isEmpty(msg)) {
                    if(mListener!=null){
                        mListener.onItemClick(Integer.valueOf(msg));
                    }
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

        rlInputdlgView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
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

        rlOutsideView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() != R.id.rl_inputdlg_view)
                    dismiss();
            }
        });

        messageTextView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Log.d("My test", "onKey " + keyEvent.getCharacters());
                return false;
            }
        });
        messageTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                int len = editable.toString().length();
                if (len == 1 && text.equals("0")) {
                    editable.clear();
                }
        }});

        messageTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case KeyEvent.KEYCODE_ENDCALL:
                    case KeyEvent.KEYCODE_ENTER:
                        if (messageTextView.getText().length() > 0) {
                        if(mListener!=null){
                            mListener.onItemClick(Integer.valueOf(messageTextView.getText().toString()));
                         }
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
    }


    private void softKeyboardListnenr() {
        SoftKeyBoardListener.setListener((Activity) mContext, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {/*软键盘显示：执行隐藏title动画，并修改listview高度和装载礼物容器的高度*/
                Log.e("softKeyboardListnenr", "keyBoardShow");
//                RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) rldlgview.getLayoutParams();
//                layoutParams.bottomMargin=height;
//                rldlgview.setLayoutParams(layoutParams);
            }

            @Override
            public void keyBoardHide(int height) {/*软键盘隐藏：隐藏聊天输入框并显示聊天按钮，执行显示title动画，并修改listview高度和装载礼物容器的高度*/
                Log.e("softKeyboardListnenr", "keyBoardHide");
                dismiss();
                imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);

            }
        });
    }

    @Override
    public void dismiss() {
        mLastDiff = 0;
        //dismiss之前重置mLastDiff值避免下次无法打开
        imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
        super.dismiss();
    }
}
