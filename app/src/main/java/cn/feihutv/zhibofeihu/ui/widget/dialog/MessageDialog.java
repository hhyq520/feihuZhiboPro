package cn.feihutv.zhibofeihu.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.db.model.MessageEntity;
import cn.feihutv.zhibofeihu.data.db.model.RecentItem;
import cn.feihutv.zhibofeihu.data.network.http.model.common.GetFriendsRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.GetFriendsResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.MessageRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.MessageResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.JoinRoomResponce;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.rxbus.RxBusSubscribe;
import cn.feihutv.zhibofeihu.rxbus.ThreadMode;
import cn.feihutv.zhibofeihu.ui.me.dynamic.OthersCommunityActivity;
import cn.feihutv.zhibofeihu.ui.widget.dialog.adapter.FriendsListAdapter;
import cn.feihutv.zhibofeihu.ui.widget.dialog.adapter.MessageAdapter;
import cn.feihutv.zhibofeihu.ui.widget.dialog.adapter.RecentAdapter;
import cn.feihutv.zhibofeihu.ui.widget.swipelistview.BaseSwipeListViewListener;
import cn.feihutv.zhibofeihu.ui.widget.swipelistview.SwipeListView;
import cn.feihutv.zhibofeihu.ui.widget.xlistview.MsgListView;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.ListViewDecoration;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.feihutv.zhibofeihu.utils.StringUtil;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.feihutv.zhibofeihu.utils.UiUtil;
import cn.feihutv.zhibofeihu.utils.glideutil.GlideApp;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by chenliwen on 2017/6/12 16:43.
 * 佛祖保佑，永无BUG
 */

public class MessageDialog extends Dialog implements OnClickItem, View.OnTouchListener, MsgListView.IXListViewListener {

    @BindView(R.id.text_tip)
    TextView textTip;
    @BindView(R.id.view_dot)
    View viewDot;
    private Context context;
    public static final int NEW_MESSAGE_CHAT = 0x001;// 收到消息

    @BindView(R.id.contact_name)
    TextView contactName;

    @BindView(R.id.msg_listView)
    MsgListView mMsgListView;

    @BindView(R.id.message_edit)
    EditText msgEt;

    @BindView(R.id.send)
    TextView send;

    @BindView(R.id.net_status_bar_info_top)
    TextView netStatusBarInfoTop;

    @BindView(R.id.recent_listview)
    SwipeListView mRecentListView;

    @BindView(R.id.empty)
    TextView mEmpty;

    @BindView(R.id.playinpc_msg_linearlayout1)
    LinearLayout linearLayout1;

    @BindView(R.id.playinpc_msg_linearlayout2)
    LinearLayout linearLayout2;

    @BindView(R.id.friendslist_recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.playinpc_msg_linearlayout3)
    LinearLayout linearLayout3;

    @BindView(R.id.icon)
    ImageView icon;

    @BindView(R.id.fh_msg)
    TextView tvRecentMsg;

    @BindView(R.id.fh_name)
    TextView tvNick;

    @BindView(R.id.front)
    RelativeLayout relativeLayout;

    private int offset = 0;
    private int count = 20;
    private List<GetFriendsResponce.GetFriendsData> friendsListEntities;
    private FriendsListAdapter friendsListAdapter;
    private String userId;
    private String headUrl;
    private String nickName;
    private MessageAdapter adapter;
    private InputMethodManager imm;
    private MessageEntity item;
    private int lastPos=0;
    private boolean isPc;
    private JoinRoomResponce.JoinRoomData roomData;
    public MessageDialog(@NonNull Context context, boolean isPc,JoinRoomResponce.JoinRoomData roomData) {
        super(context, R.style.user_dialog);
        this.context = context;
        this.isPc = isPc;
        this.roomData=roomData;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isPc) {
            setContentView(R.layout.fragment_pc_messageinplay);
        } else {
            setContentView(R.layout.fragment_messageinplay);
        }
        ButterKnife.bind(this);
        imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        initRecentData();
        initView();
        initFriendsView();
        String fh_msg = SharePreferenceUtil.getSession(context, "FH_MSG" + SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_USERID"));
        if (TextUtils.isEmpty(fh_msg)) {
            tvRecentMsg.setText("欢迎来到飞虎直播，很高兴为您服务...");
        } else {
            tvRecentMsg.setText(fh_msg);
        }
        if(SharePreferenceUtil.getSessionBoolean(FeihuZhiboApplication.getApplication(), "fhkf_news", false)){
            viewDot.setVisibility(View.VISIBLE);
        }else{
            viewDot.setVisibility(View.GONE);
        }
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastPos=3;
                linearLayout3.setVisibility(View.VISIBLE);
                linearLayout1.setVisibility(View.GONE);
                linearLayout2.setVisibility(View.GONE);
                viewDot.setVisibility(View.GONE);
                SharePreferenceUtil.saveSeesionBoolean(FeihuZhiboApplication.getApplication(), "fhkf_news", false);
                userId = "";
                nickName = "飞虎客服";
                initChatView();
            }
        });
    }

    private boolean judegeIsExit(String userId,List<RecentItem> dataf){
        for(RecentItem item:dataf){
            if(item.getUserId().equals(userId)){
                return true;
            }
        }
        return false;
    }

    private void initRecentData() {
        if(roomData==null){
            mRecentDatas = FeihuZhiboApplication.getApplication().mDataManager.queryRecentMessageItem(SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_USERID"));
//            Collections.reverse(mRecentDatas);
        }else{
            mRecentDatas = FeihuZhiboApplication.getApplication().mDataManager.queryRecentMessageItem(SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_USERID"));
//            Collections.reverse(mRecentDatas);
            if(judegeIsExit(roomData.getMasterDataList().getUserId(),mRecentDatas)){
                for(RecentItem item:mRecentDatas){
                    if(item.getUserId().equals(roomData.getMasterDataList().getUserId())){
                        mRecentDatas.remove(item);
                        mRecentDatas.add(0,item);
                        break;
                    }
                }
            }else{
                RecentItem recentItem = new RecentItem();
                recentItem.setUserId(roomData.getMasterDataList().getUserId());
                recentItem.setHeadImg(roomData.getMasterDataList().getHeadUrl());
                recentItem.setName(roomData.getMasterDataList().getNickName());
                recentItem.setMessage("在看什么了，快来和我聊聊吧！");
                recentItem.setTime(0);
                recentItem.setUid(SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(),"PREF_KEY_USERID"));
                recentItem.setIsRead(true);
                mRecentDatas.add(0,recentItem);
            }
        }

        mAdapter = new RecentAdapter(getContext(), mRecentDatas, mRecentListView);
        mRecentListView.setAdapter(mAdapter);
    }

    private void initView() {
        mRecentListView.setEmptyView(mEmpty);
        if (mRecentListView != null) {
            UiUtil.initialize(getContext());
            int screenWidth = UiUtil.getScreenWidth();
            mRecentListView.setOffsetLeft(screenWidth - UiUtil.dip2px(85));
        }
        mRecentListView
                .setSwipeListViewListener(new BaseSwipeListViewListener() {
                    @Override
                    public void onOpened(int position, boolean toRight) {
                    }

                    @Override
                    public void onClosed(int position, boolean fromRight) {
                    }

                    @Override
                    public void onListChanged() {

                    }

                    @Override
                    public void onMove(int position, float x) {
                    }

                    @Override
                    public void onStartOpen(int position, int action, boolean right) {
                        // L.d("swipe", String.format(
                        // "onStartOpen %d - action %d", position, action));
                    }

                    @Override
                    public void onStartClose(int position, boolean right) {
                        // L.d("swipe",
                        // String.format("onStartClose %d", position));
                    }

                    @Override
                    public void onClickFrontView(int position) {
                        // L.d("swipe",
                        // String.format("onClickFrontView %d", position));
                        // T.showShort(mApplication, "item onClickFrontView ="
                        // + mAdapter.getItem(position));
                        RecentItem item = (RecentItem) mAdapter
                                .getItem(position);
//                    item.setNewNum(0);
                        item.setIsRead(true);
                        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                                .updateRecentItem(item)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Object>() {
                                    @Override
                                    public void accept(@NonNull Object obj) throws Exception {

                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {

                                    }
                                })

                        );
                        lastPos=3;
                        linearLayout3.setVisibility(View.VISIBLE);
                        linearLayout1.setVisibility(View.GONE);
                        linearLayout2.setVisibility(View.GONE);

                        userId = item.getUserId();
                        headUrl = item.getHeadImg();
                        nickName = item.getName();
                        initChatView();
                    }

                    @Override
                    public void onClickBackView(int position) {
                        mRecentListView.closeOpenedItems();// 关闭打开的项
                    }

                    @Override
                    public void onDismiss(int[] reverseSortedPositions) {
                        for (int position : reverseSortedPositions) {
                            mAdapter.remove(position);
                        }
                        // mAdapter.notifyDataSetChanged();
                    }
                });
    }


    @OnClick({R.id.tv_friends, R.id.tv_noread, R.id.iv_back, R.id.back_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_friends:
                MobclickAgent.onEvent(getContext(), "10024");
                lastPos=2;
                linearLayout2.setVisibility(View.VISIBLE);
                linearLayout1.setVisibility(View.GONE);
                linearLayout3.setVisibility(View.GONE);
                loadFriends();
                break;
            case R.id.tv_noread:
                MobclickAgent.onEvent(getContext(), "10025");
                if (mAdapter != null) {
                    List<RecentItem> datas = FeihuZhiboApplication.getApplication().mDataManager.queryRecentMessageItem(SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_USERID"));
                    for (RecentItem item : datas) {
                        if (!item.getIsRead()) {
                            item.setIsRead(true);
                            new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                                    .saveRecentItem(item)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<Object>() {
                                        @Override
                                        public void accept(@NonNull Object obj) throws Exception {

                                        }
                                    }, new Consumer<Throwable>() {
                                        @Override
                                        public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {

                                        }
                                    })

                            );
                        }
                    }
//                    Collections.reverse(datas);
                    if(roomData!=null&&judegeIsExit(roomData.getMasterDataList().getUserId(),datas)){
                        for(RecentItem item:datas){
                            if(item.getUserId().equals(roomData.getMasterDataList().getUserId())){
                                datas.remove(item);
                                datas.add(0,item);
                                break;
                            }
                        }
                    }else{
                        if(roomData!=null) {
                            RecentItem recentItem = new RecentItem();
                            recentItem.setUserId(roomData.getMasterDataList().getUserId());
                            recentItem.setHeadImg(roomData.getMasterDataList().getHeadUrl());
                            recentItem.setName(roomData.getMasterDataList().getNickName());
                            recentItem.setMessage("在看什么了，快来和我聊聊吧！");
                            recentItem.setTime(0);
                            recentItem.setUid(SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_USERID"));
                            recentItem.setIsRead(true);
                            datas.add(0, recentItem);
                        }
                    }
                    mAdapter.setData(datas);
                }
                break;
            case R.id.iv_back:
                linearLayout1.setVisibility(View.VISIBLE);
                linearLayout2.setVisibility(View.GONE);
                linearLayout3.setVisibility(View.GONE);
                lastPos=0;
                break;
            case R.id.back_btn:
                // 从聊天页面返回
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘

                if(lastPos==1){
                    linearLayout1.setVisibility(View.GONE);
                    linearLayout2.setVisibility(View.VISIBLE);
                    linearLayout3.setVisibility(View.GONE);
                }else {
                    linearLayout1.setVisibility(View.VISIBLE);
                    linearLayout2.setVisibility(View.GONE);
                    linearLayout3.setVisibility(View.GONE);
                }
                initRecentData();
                break;
        }
    }

    private void initFriendsView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));// 布局管理器。
        recyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        recyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        recyclerView.addItemDecoration(new ListViewDecoration(1));// 添加分割线。
        friendsListAdapter = new FriendsListAdapter(getContext());
        recyclerView.setAdapter(friendsListAdapter);
        friendsListAdapter.setOnClickItem(this);
//        loadFriends();
    }

    private List<RecentItem> mRecentDatas;
    private RecentAdapter mAdapter;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NEW_MESSAGE_CHAT:
                    if (msg.what == NEW_MESSAGE_CHAT) {
                        MessageEntity messageEntity1 = (MessageEntity) msg.obj;
                        String senderId = messageEntity1.getSenderId();

                        if (!userId.equals(senderId)) {// 如果不是当前正在聊天对象的消息，不处理
                            return;
                        }
                        adapter.upDateMsg(messageEntity1);
                        mMsgListView.setSelection(adapter.getCount() - 1);
//                        mMsgListView.setStackFromBottom(true);
                    }
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    public void onItemClick(String uid, String headUrl, String nickName) {
        lastPos=1;
        linearLayout3.setVisibility(View.VISIBLE);
        linearLayout1.setVisibility(View.GONE);
        linearLayout2.setVisibility(View.GONE);
        this.userId = uid;
        this.headUrl = headUrl;
        this.nickName = nickName;
        initChatView();
    }

    // 初始化聊天界面
    private void initChatView() {
        adapter = new MessageAdapter(getContext(), initMsgData());
        adapter.setChatClickHead(new MessageAdapter.ChatClickHeadListener() {
            @Override
            public void clickHead(String userId) {
                Intent intent = new Intent(context, OthersCommunityActivity.class);
                intent.putExtra("userId", userId);
                context.startActivity(intent);
            }
        });
        mMsgListView.setOnTouchListener(this);
        mMsgListView.setPullRefreshEnable(false);
        mMsgListView.setPullLoadEnable(false);
        mMsgListView.setXListViewListener(this);
        mMsgListView.setAdapter(adapter);
        mMsgListView.setSelection(adapter.getCount() - 1);
        msgEt.setOnTouchListener(this);
        contactName.setText(nickName);
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
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    /**
     * 加载消息历史，从数据库中读出
     */
    private List<MessageEntity> initMsgData() {
        List<MessageEntity> list = FeihuZhiboApplication.getApplication().mDataManager.queryListMessage(userId,
                SharePreferenceUtil.getSession(getContext(), "PREF_KEY_USERID"));
        return list;
    }



    public void loadFriends() {
        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doGetFriendsApiCall(new GetFriendsRequest(offset, count))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetFriendsResponce>() {
                    @Override
                    public void accept(@NonNull GetFriendsResponce responce) throws Exception {
                        if (responce.getCode() == 0) {
                            friendsListEntities = responce.getGetFriendsDatas();
                            if (friendsListEntities.size() == 0) {
                                textTip.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            } else {
                                textTip.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                friendsListAdapter.setDatas(friendsListEntities);
                            }

                        } else {

                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {

                    }
                }));
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

    private void sendMessage() {
        final String msg = msgEt.getText().toString();
        if (TextUtils.isEmpty(msg)) {
            Toast.makeText(getContext(), "发送内容不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TCUtils.isNetworkAvailable(context)) {
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
                                new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                                        .saveMessageEntity(item)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Consumer<Object>() {
                                            @Override
                                            public void accept(@io.reactivex.annotations.NonNull Object obj) throws Exception {
                                                adapter.upDateMsg(item);
                                                mMsgListView.setSelection(adapter.getCount() - 1);
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
                                } else {
                                    tvRecentMsg.setText(msg);
                                    SharePreferenceUtil.saveSeesion(context, "FH_MSG" + SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_USERID"), msg);
                                }
                            } else {
                                FHUtils.showToast("发送失败！");
                                long time=SharePreferenceUtil.getSessionLong(getContext(),"chazhi")+System.currentTimeMillis() / 1000;
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
                                mMsgListView.setSelection(adapter.getCount() - 1);
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {

                        }
                    }));
        } else {
            String myUserId = SharePreferenceUtil.getSession(getContext(), "PREF_KEY_USERID");
            String myheadUrl = SharePreferenceUtil.getSession(getContext(), "PREF_KEY_HEADURL");
            String mynickname = SharePreferenceUtil.getSession(getContext(), "PREF_KEY_NICKNAME");
            int mylevel = SharePreferenceUtil.getSessionInt(getContext(), "PREF_KEY_LEVEL");
            long time=SharePreferenceUtil.getSessionLong(getContext(),"chazhi")+System.currentTimeMillis() / 1000;
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

    }

    @Override
    public void onLoadMore() {

    }

    public void update(int time, String content, String senderId, String headUrl,String nickName) {
        SharePreferenceUtil.saveSeesionInt(getContext(), "news_count", 0);
        if (senderId.equals("")) {
            GlideApp.loadImgtransform(context, headUrl, R.drawable.face, icon);
            tvRecentMsg.setText(content);
            SharePreferenceUtil.saveSeesion(context, "FH_MSG" + SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_USERID"), content);
            if(contactName.getText().equals("飞虎客服")&&linearLayout3.getVisibility() == View.VISIBLE){
                SharePreferenceUtil.saveSeesionInt(getContext(), "news_count", 0);
                SharePreferenceUtil.saveSeesionBoolean(FeihuZhiboApplication.getApplication(), "fhkf_news", false);
            }else{
                viewDot.setVisibility(View.VISIBLE);
            }
        } else {
            if (linearLayout3.getVisibility() == View.VISIBLE) {
                RecentItem recentItem = new RecentItem();
                recentItem.setUserId(senderId);
                recentItem.setHeadImg(headUrl);
                recentItem.setName(nickName);
                recentItem.setMessage(content);
                recentItem.setTime((long) time*1000);
                recentItem.setUid(SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(),"PREF_KEY_USERID"));
                recentItem.setIsRead(true);

                new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                        .updateRecentItem(recentItem)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(@io.reactivex.annotations.NonNull Object obj) throws Exception {
                                SharePreferenceUtil.saveSeesionInt(getContext(), "news_count", 0);
                                initRecentData();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {

                            }
                        }));
            }else {
                initRecentData();
            }

        }
        if (linearLayout3.getVisibility() == View.VISIBLE) {
            MessageEntity messageEntity1 = new MessageEntity();
            messageEntity1.setTime(time);
            messageEntity1.setContent(content);
            messageEntity1.setSenderId(senderId);
            messageEntity1.setHeadUrl(headUrl);
            messageEntity1.setIsComMeg(true);
            messageEntity1.setMsgStatus(1);
            Message message1 = handler.obtainMessage(NEW_MESSAGE_CHAT);
            message1.obj = messageEntity1;
            handler.sendMessage(message1);
            SharePreferenceUtil.saveSeesionInt(getContext(), "news_count", 0);
        }
    }

}
