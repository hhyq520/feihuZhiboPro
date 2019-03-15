package cn.feihutv.zhibofeihu.ui.live.pclive;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import butterknife.Unbinder;
import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.db.model.MessageEntity;
import cn.feihutv.zhibofeihu.data.db.model.RecentItem;
import cn.feihutv.zhibofeihu.data.network.http.model.common.GetFriendsRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.GetFriendsResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.LoadMsgListResponce;
import cn.feihutv.zhibofeihu.data.network.http.model.common.MessageRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.MessageResponce;
import cn.feihutv.zhibofeihu.data.network.socket.model.live.JoinRoomResponce;
import cn.feihutv.zhibofeihu.rxbus.RxBus;
import cn.feihutv.zhibofeihu.rxbus.RxBusCode;
import cn.feihutv.zhibofeihu.rxbus.RxBusSubscribe;
import cn.feihutv.zhibofeihu.rxbus.ThreadMode;
import cn.feihutv.zhibofeihu.ui.base.BaseFragment;
import cn.feihutv.zhibofeihu.ui.live.OnStrClickListener;
import cn.feihutv.zhibofeihu.ui.me.dynamic.OthersCommunityActivity;
import cn.feihutv.zhibofeihu.ui.widget.dialog.OnClickItem;
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
import cn.feihutv.zhibofeihu.utils.glideutil.GlideApp;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by chenliwen on 2017/6/3 15:12.
 * 佛祖保佑，永无BUG
 */

public class MessageInPlayFragment extends BaseFragment implements OnClickItem, View.OnTouchListener, MsgListView.IXListViewListener {

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

    @BindView(R.id.net_status_bar_top)
    LinearLayout netStatusBarTop;

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
    @BindView(R.id.text_tip)
    TextView textTip;

    @BindView(R.id.front)
    RelativeLayout relativeLayout;
    @BindView(R.id.view_dot)
    View viewDot;
    Unbinder unbinder;

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
    private OnStrClickListener mListener;
    private int lastPos=0;
    public void setOnStrClickListener(OnStrClickListener listener) {
        mListener = listener;
    }
    private JoinRoomResponce.JoinRoomData roomData;
    public interface showDialogListener{
        void showDialog(String sendId, String nickName, String headUrl, boolean isPcLand);
    }
    private showDialogListener showDialogListener;
    public void setShowDialogListener(showDialogListener listener) {
        showDialogListener = listener;
    }

    public static MessageInPlayFragment getInstance(JoinRoomResponce.JoinRoomData roomData) {
        MessageInPlayFragment fragment = new MessageInPlayFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        fragment.roomData=roomData;
        return fragment;
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_pc_messageinplay;
    }

    @OnClick({R.id.tv_friends, R.id.tv_noread, R.id.iv_back, R.id.back_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_friends:
                lastPos=1;
                MobclickAgent.onEvent(getContext(), "10024");
                linearLayout2.setVisibility(View.VISIBLE);
                linearLayout1.setVisibility(View.GONE);
                linearLayout3.setVisibility(View.GONE);
                loadFriends();

                break;
            case R.id.tv_noread:
                MobclickAgent.onEvent(getContext(), "10025");
                if (mListener != null) {
                    mListener.onItemClick("0");
                }
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

                    if(judegeIsExit(roomData.getMasterDataList().getUserId(),datas)){
                        for(RecentItem item:datas){
                            if(item.getUserId().equals(roomData.getMasterDataList().getUserId())){
                                datas.remove(item);
                                datas.add(0,item);
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
                        datas.add(0,recentItem);
                    }
                    mAdapter.setData(datas);
                }
                break;
            case R.id.iv_back:
                lastPos=0;
                linearLayout1.setVisibility(View.VISIBLE);
                linearLayout2.setVisibility(View.GONE);
                linearLayout3.setVisibility(View.GONE);
                break;
            case R.id.back_btn:
                // 从聊天页面返回
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
                if(lastPos==1){
                    linearLayout1.setVisibility(View.GONE);
                    linearLayout2.setVisibility(View.VISIBLE);
                    linearLayout3.setVisibility(View.GONE);
                }else{
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
        recyclerView.addItemDecoration(new ListViewDecoration());// 添加分割线。
        friendsListAdapter = new FriendsListAdapter(getContext());
        recyclerView.setAdapter(friendsListAdapter);
        friendsListAdapter.setOnClickItem(this);

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
//                        mMsgListView.setStackFromBottom(true);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void initWidget(View view) {
        setUnBinder(ButterKnife.bind(this, view));
        if(SharePreferenceUtil.getSessionBoolean(FeihuZhiboApplication.getApplication(), "fhkf_news", false)){
            viewDot.setVisibility(View.VISIBLE);
        }else{
            viewDot.setVisibility(View.GONE);
        }
        initRecentData();
        initView();
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
                            if(friendsListEntities.size()==0){
                                textTip.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }else{
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

    private void initView() {
        imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
        initFriendsView();
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastPos=2;
//                linearLayout3.setVisibility(View.VISIBLE);
//                linearLayout1.setVisibility(View.GONE);
//                linearLayout2.setVisibility(View.GONE);
                viewDot.setVisibility(View.GONE);
                SharePreferenceUtil.saveSeesionBoolean(FeihuZhiboApplication.getApplication(), "fhkf_news", false);
                userId = "";
                nickName = "飞虎客服";
//                initChatView();
                if(showDialogListener!=null){
                    showDialogListener.showDialog(userId,nickName,"",false);
                }
            }
        });
        mRecentListView.setEmptyView(mEmpty);
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
                        lastPos=2;
//                        linearLayout3.setVisibility(View.VISIBLE);
//                        linearLayout1.setVisibility(View.GONE);
//                        linearLayout2.setVisibility(View.GONE);

                        userId = item.getUserId();
                        headUrl = item.getHeadImg();
                        nickName = item.getName();
//                        initChatView();

                        if(showDialogListener!=null){
                            showDialogListener.showDialog(userId,nickName,headUrl,false);
                        }
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

    @Override
    public void onResume() {
        super.onResume();
        String fh_msg = SharePreferenceUtil.getSession(getContext(), "FH_MSG" + SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_USERID"));
        if (TextUtils.isEmpty(fh_msg)) {
            tvRecentMsg.setText("欢迎来到飞虎直播，很高兴为您服务...");
        } else {
            tvRecentMsg.setText(fh_msg);
        }
        RxBus.get().register(this);
        if (!SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_CURRENT_USER_ID").startsWith("g")) {
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
            mAdapter.setData(mRecentDatas);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        RxBus.get().unRegister(this);
    }

    private boolean judegeIsExit(String userId, List<RecentItem> da){
        for(RecentItem item:da){
            if(item.getUserId().equals(userId)){
                return true;
            }
        }
        return false;
    }

    private void initRecentData() {
        try {

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
            mAdapter = new RecentAdapter(getContext(), mRecentDatas, mRecentListView);
            mRecentListView.setAdapter(mAdapter);

        }catch (Exception e){

        }

    }

//    //收到消息
//    @RxBusSubscribe(code = RxBusCode.RX_BUS_CODE_NOTICE_SECRET_MESSAGE, threadMode = ThreadMode.MAIN)
//    public void onReceiveLoadMsgListDataPush(LoadMsgListResponce.LoadMsgListData pushData) {
//
//    }

    public void updataMessage(LoadMsgListResponce.LoadMsgListData pushData) {
        if (getActivity().getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            if (pushData.getUserId().equals("")) {
                GlideApp.loadImgtransform(getContext(), pushData.getHeadUrl(), R.drawable.face, icon);
                tvRecentMsg.setText(pushData.getContent());
                if(nickName.equals("飞虎客服")){
                    SharePreferenceUtil.saveSeesionInt(getContext(), "news_count", 0);
                    SharePreferenceUtil.saveSeesionBoolean(FeihuZhiboApplication.getApplication(), "fhkf_news", false);
                }else {
                    viewDot.setVisibility(View.VISIBLE);
                }
                SharePreferenceUtil.saveSeesion(getContext(), "FH_MSG" + SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_USERID"), pushData.getContent());
            } else {
                  if(pushData.getNickName().equals(nickName)) {
                      RecentItem recentItem = new RecentItem();
                      recentItem.setUserId(pushData.getUserId());
                      recentItem.setHeadImg(headUrl);
                      recentItem.setName(nickName);
                      recentItem.setMessage(pushData.getContent());
                      recentItem.setTime((long) pushData.getTime() * 1000);
                      recentItem.setUid(SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_USERID"));
                      recentItem.setIsRead(true);

                      new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                              .updateRecentItem(recentItem)
                              .subscribeOn(Schedulers.io())
                              .observeOn(AndroidSchedulers.mainThread())
                              .subscribe(new Consumer<Object>() {
                                  @Override
                                  public void accept(@io.reactivex.annotations.NonNull Object obj) throws Exception {
                                      initRecentData();
                                      SharePreferenceUtil.saveSeesionInt(getContext(), "news_count", 0);
                                  }
                              }, new Consumer<Throwable>() {
                                  @Override
                                  public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {

                                  }
                              }));
                  }else{
                      initRecentData();
                  }
            }

//                MessageEntity messageEntity1 = new MessageEntity();
//                messageEntity1.setTime(pushData.getTime());
//                messageEntity1.setContent(pushData.getContent());
//                messageEntity1.setSenderId(pushData.getUserId());
//                messageEntity1.setHeadUrl(headUrl);
//                messageEntity1.setIsComMeg(true);
//                messageEntity1.setMsgStatus(1);
//                Message message1 = handler.obtainMessage(NEW_MESSAGE_CHAT);
//                message1.obj = messageEntity1;
//                handler.sendMessage(message1);
//                SharePreferenceUtil.saveSeesionInt(getContext(), "news_count", 0);

        }
    }

    @Override
    public void onItemClick(String uid, String headUrl, String nickName) {
//        linearLayout3.setVisibility(View.VISIBLE);
        lastPos=1;
//        linearLayout1.setVisibility(View.GONE);
//        linearLayout2.setVisibility(View.GONE);

        this.userId = uid;
        this.headUrl = headUrl;
        this.nickName = nickName;

//        initChatView();
        if(showDialogListener!=null){
            showDialogListener.showDialog(userId,nickName,headUrl,false);
        }
    }

    // 初始化聊天界面
    private void initChatView() {

        initMsgData();
        adapter = new MessageAdapter(getContext(), initMsgData());
        adapter.setChatClickHead(new MessageAdapter.ChatClickHeadListener() {
            @Override
            public void clickHead(String userId) {
                Intent intent = new Intent(getContext(), OthersCommunityActivity.class);
                intent.putExtra("userId", userId);
                getContext().startActivity(intent);
            }
        });
        mMsgListView.setOnTouchListener(this);
        mMsgListView.setPullLoadEnable(false);
        mMsgListView.setPullRefreshEnable(false);
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
        if (TCUtils.isNetworkAvailable(getContext())) {
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
                                    mAdapter.notifyDataSetChanged();
                                } else {
                                    tvRecentMsg.setText(msg);
                                    SharePreferenceUtil.saveSeesion(getContext(), "FH_MSG" + SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "PREF_KEY_USERID"), msg);
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

    /**
     * 加载消息历史，从数据库中读出
     */
    private List<MessageEntity> initMsgData() {
        List<MessageEntity> list = FeihuZhiboApplication.getApplication().mDataManager.queryListMessage(userId,
                SharePreferenceUtil.getSession(getContext(), "PREF_KEY_USERID"));
        return list;
    }

    public void onFreshDatas() {
        if (mAdapter == null) {
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
            mAdapter = new RecentAdapter(getContext(), mRecentDatas, mRecentListView);
            mRecentListView.setAdapter(mAdapter);
        } else {
            if (mRecentDatas != null && mRecentDatas.size() > 0) {
                mRecentDatas.clear();
            }
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
            mAdapter.setData(mRecentDatas);
            mAdapter.notifyDataSetChanged();
        }
        nickName="";
//        mAdapter = new RecentAdapter(getContext(), mRecentDatas, mRecentListView);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
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

    public void resetAdapter() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }


    @Override
    protected void lazyLoad() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
