package cn.feihutv.zhibofeihu.ui.me.news;

import android.content.Intent;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.DataManager;
import cn.feihutv.zhibofeihu.data.db.model.MessageEntity;
import cn.feihutv.zhibofeihu.data.db.model.RecentItem;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadMsgListResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.MessageRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.MessageResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.LoadUserDataBaseResponse;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.rxbus.RxBusSubscribe;
import cn.feihutv.zhibofeihu.rxbus.ThreadMode;
import cn.feihutv.zhibofeihu.ui.base.BaseActivity;
import cn.feihutv.zhibofeihu.ui.me.dynamic.OthersCommunityActivity;
import cn.feihutv.zhibofeihu.ui.widget.dialog.adapter.MessageAdapter;
import cn.feihutv.zhibofeihu.ui.widget.xlistview.MsgListView;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.feihutv.zhibofeihu.utils.StringUtil;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/22 14:08
 *      desc   : 聊天界面
 *      version: 1.0
 * </pre>
 */

public class ChatActivity extends BaseActivity implements View.OnTouchListener, MsgListView.IXListViewListener {

    public static final int NEW_MESSAGE = 0x001;// 收到消息

    @BindView(R.id.contact_name)
    TextView contactName;

    @BindView(R.id.msg_listView)
    MsgListView mMsgListView;

    @BindView(R.id.message_edit)
    EditText msgEt;

    @BindView(R.id.send)
    TextView send;

    private String userId;
    private String userName;
    private String userHead;

    private InputMethodManager imm;

    private MessageEntity item;

    private MessageAdapter adapter;
    private LoadUserDataBaseResponse.UserData mUserData;
    private DataManager mDataManager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initWidget() {
        setUnBinder(ButterKnife.bind(this));

        msgEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                checkSendButtonEnable(msgEt);
            }
        });
        msgEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkSendButtonEnable(msgEt);
            }
        });
        initData();
        initChatView();
    }

    private void initData() {
        mDataManager = FeihuZhiboApplication.getApplication().mDataManager;
        mUserData = mDataManager.getUserData();
        userName = getIntent().getStringExtra("nickName");
        userId = getIntent().getStringExtra("userId");
        userHead = getIntent().getStringExtra("headUrl");
        if (userId == null) {// 如果为空，直接关闭
            finish();
        }

    }

    /**
     * 显示发送
     *
     * @param editText
     */
    private void checkSendButtonEnable(EditText editText) {
        String textMessage = editText.getText().toString();
        if (!TextUtils.isEmpty(StringUtil.removeBlanks(textMessage)) && editText.hasFocus()) {
            send.setVisibility(View.VISIBLE);
        } else {
            send.setVisibility(View.VISIBLE);
        }
    }

    private void initChatView() {
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        initMsgData();
        adapter = new MessageAdapter(this, initMsgData());
        mMsgListView.setOnTouchListener(this);
        mMsgListView.setPullLoadEnable(false);
        mMsgListView.setXListViewListener(this);
        mMsgListView.setAdapter(adapter);
        mMsgListView.setSelection(adapter.getCount() - 1);
        msgEt.setOnTouchListener(this);
        contactName.setText(userName);

        adapter.setChatClickHead(new MessageAdapter.ChatClickHeadListener() {
            @Override
            public void clickHead(String userId) {
                if (userId.equals("")) {
                    return;
                }
                Intent intent = new Intent(ChatActivity.this, OthersCommunityActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
        msgEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                checkSendButtonEnable(msgEt);
            }
        });
        msgEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkSendButtonEnable(msgEt);
            }
        });
    }

    /**
     * 加载消息历史，从数据库中读出
     */
    private List<MessageEntity> initMsgData() {
        List<MessageEntity> list = FeihuZhiboApplication.getApplication().mDataManager.queryListMessage(userId, mDataManager.getCurrentUserId());
        return list;
    }


    @OnClick({R.id.back_btn, R.id.send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.send:
                sendMessage();
                break;
            default:
                break;
        }
    }

    private void sendMessage() {
        final String msg = msgEt.getText().toString();
        if (TextUtils.isEmpty(msg)) {
            onToast("发送内容不能为空！", Gravity.CENTER, 0, 0);
            return;
        }

        if (TCUtils.isNetworkAvailable(this)) {
            new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                    .doMessageApiCall(new MessageRequest(userId, msg))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<MessageResponce>() {
                        @Override
                        public void accept(@NonNull MessageResponce responce) throws Exception {
                            String myUserId = mUserData.getUserId();
                            String myheadUrl = mUserData.getHeadUrl();
                            String mynickname = mUserData.getNickName();
                            int mylevel = mUserData.getLevel();
                            if (responce.getCode() == 0) {
                                long time=SharePreferenceUtil.getSessionLong(FeihuZhiboApplication.getApplication(),"chazhi")+System.currentTimeMillis() / 1000;
                                item = new MessageEntity(time, msg, userId, myheadUrl, mynickname, false, mylevel + "", myUserId, 1);
                                adapter.upDateMsg(item);
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
                                    RecentItem recentItem = new RecentItem(userId, userHead, userName, msg, System.currentTimeMillis(), myUserId, true);
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
                                } else {
                                    SharePreferenceUtil.saveSeesion(ChatActivity.this, "FH_MSG"+SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(),"PREF_KEY_USERID"), msg);
                                }
                            } else {
                                FHUtils.showToast("发送失败！");
                                long time=SharePreferenceUtil.getSessionLong(FeihuZhiboApplication.getApplication(),"chazhi")+System.currentTimeMillis() / 1000;
                                item = new MessageEntity(time, msg, userId, myheadUrl, mynickname, false, mylevel + "", myUserId, 2);
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
                                adapter.upDateMsg(item);
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {

                        }
                    }));
        } else {
            String myUserId = mUserData.getUserId();
            String myheadUrl = mUserData.getHeadUrl();
            String mynickname = mUserData.getNickName();
            int mylevel = mUserData.getLevel();
            long time=SharePreferenceUtil.getSessionLong(FeihuZhiboApplication.getApplication(),"chazhi")+System.currentTimeMillis() / 1000;
            item = new MessageEntity(time, msg, userId, myheadUrl, mynickname, false, mylevel + "", myUserId, 2);
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
            adapter.upDateMsg(item);
        }
        imm.hideSoftInputFromWindow(msgEt.getWindowToken(), 0);

        // if (adapter.getCount() - 10 > 10) {
        // L.i("begin to remove...");
        // adapter.removeHeadMsg();
        // MsgPagerNum--;
        // }
        mMsgListView.setSelection(adapter.getCount() - 1);
        //自己的id

        msgEt.setText("");

//        Message handlerMsg = handler.obtainMessage(NEW_MESSAGE);
//        handlerMsg.obj = msg;
//        handler.sendMessage(handlerMsg);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.msg_listView:
                imm.hideSoftInputFromWindow(msgEt.getWindowToken(), 0);
                break;
            case R.id.message_edit:
                imm.showSoftInput(msgEt, 0);
                msgEt.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onRefresh() {
        List<MessageEntity> msgList = initMsgData();
        int position = adapter.getCount();
        adapter.setMessageList(msgList);
        mMsgListView.stopRefresh();
        mMsgListView.setSelection(adapter.getCount() - position - 1);
    }

    @Override
    public void onLoadMore() {

    }

    //收到消息
    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_NOTICE_SECRET_MESSAGE, threadMode = ThreadMode.MAIN)
    public void onReceiveLoadMsgListDataPush(LoadMsgListResponce.LoadMsgListData pushData) {
        SharePreferenceUtil.saveSeesionInt(this, "news_count", 0);
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setTime(pushData.getTime());
        messageEntity.setContent(pushData.getContent());
        messageEntity.setSenderId(pushData.getUserId());
        messageEntity.setHeadUrl(pushData.getHeadUrl());
        messageEntity.setMsgStatus(1);
        messageEntity.setIsComMeg(true);
        Message message = new Message();
        message.what = NEW_MESSAGE;
        message.obj = messageEntity;

        dealHander(message);

    }

    private void dealHander(final Message message) {
        Observable.create(new ObservableOnSubscribe<Message>() {
            @Override
            public void subscribe(ObservableEmitter<Message> e) throws Exception {
                e.onNext(message);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Message>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Message message) throws Exception {
                        int code = message.what;
                        switch (code) {
                            case NEW_MESSAGE:
                                MessageEntity messageEntity = (MessageEntity) message.obj;
                                String senderId = messageEntity.getSenderId();
                                if (!userId.equals(senderId)) {// 如果不是当前正在聊天对象的消息，不处理
                                    return;
                                }
                                adapter.upDateMsg(messageEntity);
                                break;
                            default:
                                break;
                        }
                    }
                });
    }
}
