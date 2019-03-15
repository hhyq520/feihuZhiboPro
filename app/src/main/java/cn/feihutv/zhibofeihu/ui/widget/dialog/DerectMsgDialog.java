package cn.feihutv.zhibofeihu.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.db.model.MessageEntity;
import cn.feihutv.zhibofeihu.data.db.model.RecentItem;
import cn.feihutv.zhibofeihu.data.network.http.model.common.MessageRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.MessageResponce;
import cn.feihutv.zhibofeihu.ui.live.OnStrClickListener;
import cn.feihutv.zhibofeihu.ui.widget.MultiImageView;
import cn.feihutv.zhibofeihu.ui.widget.dialog.adapter.MessageAdapter;
import cn.feihutv.zhibofeihu.ui.widget.xlistview.MsgListView;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.feihutv.zhibofeihu.utils.StringUtil;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by chenliwen on 2017/6/19 11:02.
 * 佛祖保佑，永无BUG
 */

public class DerectMsgDialog extends Dialog implements View.OnTouchListener, MsgListView.IXListViewListener {

    public static final int NEW_MESSAGE = 0x001;// 收到消息
    private Context context;
    private String userId;
    private String headUrl;
    private String nickName;
    private MessageAdapter adapter;
    private InputMethodManager imm;
    private MessageEntity item;

    @BindView(R.id.view_back_btn)
    ImageView btnReture;

    @BindView(R.id.view_contact_name)
    TextView tvName;

    @BindView(R.id.view_msg_listView)
    MsgListView msgListView;

    @BindView(R.id.view_message_edit)
    EditText editText;

    @BindView(R.id.view_send)
    TextView tvSend;

    private boolean isPc=false;
    private OnStrClickListener mListener;
    public void setOnStrClickListener(OnStrClickListener listener){
        mListener=listener;
    }

    public DerectMsgDialog(@NonNull Context context, String sendId, String nickName, String headUrl,boolean isPc) {
        super(context,  R.style.user_dialog);
        this.context = context;
        this.userId = sendId;
        this.headUrl = headUrl;
        this.nickName = nickName;
        this.isPc=isPc;
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == NEW_MESSAGE) {
                MessageEntity messageEntity = (MessageEntity) msg.obj;
                String senderId = messageEntity.getSenderId();
                if (!userId.equals(senderId)) {// 如果不是当前正在聊天对象的消息，不处理
                    return;
                }
                adapter.upDateMsg(messageEntity);
                msgListView.setSelection(adapter.getCount() - 1);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_chat_msg);
        ButterKnife.bind(this);
        initView();
        initData();
        initMsgView();
    }

    private void initData() {
        adapter = new MessageAdapter(context, initMsgData());
        adapter.setChatClickHead(new MessageAdapter.ChatClickHeadListener() {
            @Override
            public void clickHead(String userId) {
                if(mListener!=null){
                    mListener.onItemClick(userId);
                }
            }
        });
    }

    private void initMsgView() {
        imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        msgListView.setOnTouchListener(this);
        msgListView.setPullLoadEnable(false);
        msgListView.setXListViewListener(this);
        msgListView.setAdapter(adapter);
        msgListView.setSelection(adapter.getCount() - 1);
        editText.setOnTouchListener(this);
        tvName.setText(nickName);
        if(isPc){
            btnReture.setVisibility(View.VISIBLE);
        }
        btnReture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    /**
     * 加载消息历史，从数据库中读出
     */
    private List<MessageEntity> initMsgData() {
        List<MessageEntity> list = FeihuZhiboApplication.getApplication().mDataManager.queryListMessage(userId,
                SharePreferenceUtil.getSession(getContext(),"PREF_KEY_USERID"));
        return list;
    }

    private void initView() {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                checkSendButtonEnable(editText);
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkSendButtonEnable(editText);
            }
        });
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    /**
     * 显示发送
     *
     * @param editText
     */
    private void checkSendButtonEnable(EditText editText) {
        String textMessage = editText.getText().toString();
        if (!TextUtils.isEmpty(StringUtil.removeBlanks(textMessage)) && editText.hasFocus()) {
            tvSend.setVisibility(View.VISIBLE);
        } else {
            tvSend.setVisibility(View.VISIBLE);
        }
    }

    private void sendMessage() {
        final String msg = editText.getText().toString();
        if (TextUtils.isEmpty(msg)) {
            Toast.makeText(context, "发送内容不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TCUtils.isNetworkAvailable(context)) {
            new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                    .doMessageApiCall(new MessageRequest(userId, msg))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<MessageResponce>() {
                        @Override
                        public void accept(@NonNull MessageResponce responce) throws Exception {
                            String myUserId = SharePreferenceUtil.getSession(getContext(), "PREF_KEY_USERID");
                            String myheadUrl = SharePreferenceUtil.getSession(getContext(), "PREF_KEY_HEADURL");
                            String mynickname = SharePreferenceUtil.getSession(getContext(), "PREF_KEY_NICKNAME");
                            int mylevel = SharePreferenceUtil.getSessionInt(getContext(), "PREF_KEY_LEVEL");
                            if (responce.getCode() == 0) {
                                long time=SharePreferenceUtil.getSessionLong(getContext(),"chazhi")+System.currentTimeMillis() / 1000;
                                item = new MessageEntity(time, msg, userId, myheadUrl, mynickname, false, mylevel + "", myUserId, 1);
                                adapter.upDateMsg(item);
                                msgListView.setSelection(adapter.getCount() - 1);
                                new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                                        .saveMessageEntity(item)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Consumer<Object>() {
                                            @Override
                                            public void accept(@io.reactivex.annotations.NonNull Object obj) throws Exception {

                                            }
                                        }, new Consumer<Throwable>() {
                                            @Override
                                            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {

                                            }
                                        }));
                                if (!userId.equals("")) {
                                    RecentItem recentItem = new RecentItem(userId, headUrl, nickName, msg, System.currentTimeMillis(), myUserId, true);
                                    new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                                            .saveRecentItem(recentItem)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Consumer<Object>() {
                                                @Override
                                                public void accept(@io.reactivex.annotations.NonNull Object obj) throws Exception {

                                                }
                                            }, new Consumer<Throwable>() {
                                                @Override
                                                public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {

                                                }
                                            }));
                                }
                            } else {
                                FHUtils.showToast("发送失败！");
                                long time=SharePreferenceUtil.getSessionLong(getContext(),"chazhi")+System.currentTimeMillis() / 1000;
                                item = new MessageEntity(time, msg, userId, myheadUrl, mynickname, false, mylevel + "", myUserId, 2);
                                adapter.upDateMsg(item);
                                msgListView.setSelection(adapter.getCount() - 1);
                                new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                                        .saveMessageEntity(item)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Consumer<Object>() {
                                            @Override
                                            public void accept(@io.reactivex.annotations.NonNull Object obj) throws Exception {

                                            }
                                        }, new Consumer<Throwable>() {
                                            @Override
                                            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {

                                            }
                                        }));
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {

                        }
                    }));
        }else{
            String myUserId = SharePreferenceUtil.getSession(getContext(), "PREF_KEY_USERID");
            String myheadUrl = SharePreferenceUtil.getSession(getContext(), "PREF_KEY_HEADURL");
            String mynickname = SharePreferenceUtil.getSession(getContext(), "PREF_KEY_NICKNAME");
            int mylevel = SharePreferenceUtil.getSessionInt(getContext(), "PREF_KEY_LEVEL");
            long time=SharePreferenceUtil.getSessionLong(getContext(),"chazhi")+System.currentTimeMillis() / 1000;
            item = new MessageEntity(time, msg, userId, myheadUrl, mynickname, false, mylevel + "", myUserId, 2);
            adapter.upDateMsg(item);
            msgListView.setSelection(adapter.getCount() - 1);
            new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                    .saveMessageEntity(item)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull Object obj) throws Exception {

                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {

                        }
                    }));
        }

        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

        // if (adapter.getCount() - 10 > 10) {
        // L.i("begin to remove...");
        // adapter.removeHeadMsg();
        // MsgPagerNum--;
        // }
        msgListView.setSelection(adapter.getCount() - 1);
        //自己的id

        editText.setText("");

//        Message handlerMsg = handler.obtainMessage(NEW_MESSAGE);
//        handlerMsg.obj = msg;
//        handler.sendMessage(handlerMsg);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.msg_listView:
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                break;
            case R.id.message_edit:
                imm.showSoftInput(editText, 0);
                editText.setVisibility(View.VISIBLE);
                break;

            default:
                break;
        }
        return false;
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    public void update(int time, String content, String senderId, String headUrl) {
            MessageEntity messageEntity1 = new MessageEntity();
            messageEntity1.setTime(time);
            messageEntity1.setContent(content);
            messageEntity1.setSenderId(senderId);
            messageEntity1.setHeadUrl(headUrl);
            messageEntity1.setIsComMeg(true);
            messageEntity1.setMsgStatus(1);
            Message message1 = handler.obtainMessage(NEW_MESSAGE);
            message1.obj = messageEntity1;
            handler.sendMessage(message1);
    }
}
