package cn.feihutv.zhibofeihu.ui.mv.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.CncGetPublishCodeRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.CncGetPublishCodeResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetAllMvListResponse;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMVTokenRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.mv.GetMVTokenResponse;
import cn.feihutv.zhibofeihu.ui.widget.video.FhJZVideoPlayerStandard;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.CommonUtils;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.feihutv.zhibofeihu.utils.glideutil.GlideApp;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/10/24 17:08
 *      desc   : mv适配器
 *      version: 1.0
 * </pre>
 */

public class MvVideoListAdapter extends RecyclerView.Adapter<MvVideoListAdapter.ViewHolder> {


    private Map<String, FhJZVideoPlayerStandard> mPlayerStandardList = new HashMap<>();

    public Map<String, String> mMvVideoUrlMap = new HashMap<>();
    public Map<String, String> mMvVideoTitleMap = new HashMap<>();
    private Context mContext;

    public void setClickCallBack(ItemClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

    public interface ItemClickCallBack {
        void onItemClick(int pos);

        void onMv_downClick(int pos);

        void onMv_giftClick(int pos);

        void onMv_shareClick(int pos);
    }

    public List<GetAllMvListResponse.GetAllMvList> datas = null;
    private ItemClickCallBack clickCallBack;

    public MvVideoListAdapter(Context mContext, List<GetAllMvListResponse.GetAllMvList> datas) {
        this.datas = datas;
        this.mContext = mContext;
    }

    public void setMvData(List<GetAllMvListResponse.GetAllMvList> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }


    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_layout_mv_videolist, viewGroup, false);
        return new ViewHolder(view);
    }


    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        GetAllMvListResponse.GetAllMvList mvList = datas.get(position);

        TCUtils.showPicWithUrl(mContext, viewHolder.mv_user_head,
                mvList.getOwnerAvatar(), R.drawable.face);

        viewHolder.mv_user_name.setText(mvList.getOwnerNickname());
        if (!TextUtils.isEmpty(mvList.getNeedNickname())) {
            viewHolder.mv_user_desc.setText(mvList.getNeedNickname() + "的定制");
        }
        viewHolder.mv_comment.setText(mvList.getComments() + "");

        int play = mvList.getPlays();
        String playStr=""+play;
        if (play > 9999) {
            Double dd=play/10000d;
            playStr= CommonUtils.getDoubleMax2(dd)+"万";
        }
//        viewHolder.mv_play_num.setText("播放次数:" + playStr + "");
//        viewHolder.mv_play_title.setText(mvList.getTitle() + "");

        viewHolder.mv_user_ing.setVisibility(View.GONE);
        if (mvList.getLiveStatus()) {
            viewHolder.mv_user_ing.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mv_user_ing.setVisibility(View.GONE);
        }

        FhJZVideoPlayerStandard jzVideoPlayerStandard = viewHolder.videoplayer;
        jzVideoPlayerStandard.setBaseVideoInfo(mvList.getMVId()+"",mvList.getTitle()
                ,"播放次数:"+playStr+"");
        jzVideoPlayerStandard.batteryTimeLayout.setVisibility(View.GONE);
        jzVideoPlayerStandard.backButton.setVisibility(View.GONE);
        jzVideoPlayerStandard.cancelDismissControlViewTimer();
        jzVideoPlayerStandard.tinyBackImageView.setVisibility(View.GONE);


        GlideApp.loadImg(mContext, mvList.getCover(), R.drawable.bg,
                jzVideoPlayerStandard.thumbImageView);
        mPlayerStandardList.put(mvList.getVideoId(), jzVideoPlayerStandard);
        mMvVideoTitleMap.put(mvList.getVideoId(),mvList.getTitle());
        doGetMVToken(mvList.getVideoId());

        viewHolder.rl_userInfo.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clickCallBack != null) {
                            clickCallBack.onItemClick(position);
                        }
                    }
                }
        );
        viewHolder.mv_down.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clickCallBack != null) {
                            clickCallBack.onMv_downClick(position);
                        }
                    }
                }
        );
        viewHolder.mv_gift.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clickCallBack != null) {
                            clickCallBack.onMv_giftClick(position);
                        }
                    }
                }
        );
        viewHolder.mv_share.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clickCallBack != null) {
                            clickCallBack.onMv_shareClick(position);
                        }
                    }
                }
        );
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas.size();
    }


    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mv_user_name;
        public TextView mv_user_desc;
        public TextView mv_comment;
        public TextView mv_play_num;
        public TextView mv_play_title;
        public ImageView mv_user_ing;
        public ImageView mv_user_head;
        public FhJZVideoPlayerStandard videoplayer;

        public View rl_userInfo, mv_down, mv_gift, mv_share;

        public ViewHolder(View view) {
            super(view);
            mv_user_name = (TextView) view.findViewById(R.id.mv_user_name);
            mv_user_desc = (TextView) view.findViewById(R.id.mv_user_desc);
            mv_comment = (TextView) view.findViewById(R.id.mv_comment);
            mv_play_num = (TextView) view.findViewById(R.id.mv_play_num);
            mv_play_title = (TextView) view.findViewById(R.id.mv_play_title);
            mv_user_ing = view.findViewById(R.id.mv_user_ing);
            mv_user_head = view.findViewById(R.id.mv_user_head);
            videoplayer = view.findViewById(R.id.videoplayer);
            rl_userInfo = view.findViewById(R.id.rl_userInfo);
            mv_down = view.findViewById(R.id.mv_down);
            mv_gift = view.findViewById(R.id.mv_gift);
            mv_share = view.findViewById(R.id.mv_share);
        }
    }


    private void doGetMVToken(final String videoId) {
        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                .doGetMVTokenCall(new GetMVTokenRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetMVTokenResponse>() {
                    @Override
                    public void accept(@NonNull GetMVTokenResponse response) throws Exception {

                        if (response.getCode() == 0) {

                            GetMVTokenResponse.GetMVTokenData mvTokenData = response.getGetMVTokenData();
                            CncGetPublishCodeRequest request =
                                    new CncGetPublishCodeRequest(mvTokenData.getUserId(),
                                            mvTokenData.getToken(), mvTokenData.getTimeStamp(), videoId);
                            request.setFormat("json");
                            request.setCodeType(4);
                            getCncPlayUrl(request, videoId);
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                })

        );

    }


    private void getCncPlayUrl(CncGetPublishCodeRequest request, final String videoId) {

        //请求网宿接口 进行鉴权 获取视频播放地址
        AndroidNetworking.post("http://api.cloudv.haplat.net/vod/videoManage/getPublishCode")
                .addBodyParameter(request)
                .setTag("getPublishCode")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(CncGetPublishCodeResponse.class,
                        new ParsedRequestListener<CncGetPublishCodeResponse>() {
                            @Override
                            public void onResponse(CncGetPublishCodeResponse response1) {
                                // do anything with response
                                if ("200".equals(response1.getCode())) {
                                    List<CncGetPublishCodeResponse.CncVideoUrlData> mCncVideoUrlDatas =
                                            response1.getCncGetPublishCodeData().getVideoUrlData();
                                    if (mCncVideoUrlDatas != null && mCncVideoUrlDatas.size() > 0) {
                                        for (CncGetPublishCodeResponse.CncVideoUrlData cncVideoUrlData :
                                                mCncVideoUrlDatas) {
                                            if ("移动端".equals(cncVideoUrlData.getUrlType())) {
                                                AppLogger.i("移动端视频播放url== " + cncVideoUrlData.getOriginUrl());
                                                String url = cncVideoUrlData.getOriginUrl();
//                                                mPlayerStandardList.get(videoId).play(url);
                                                mMvVideoUrlMap.put(videoId, url);
                                                mPlayerStandardList.get(videoId).setUp(url
                                              , FhJZVideoPlayerStandard.SCREEN_WINDOW_NORMAL,
                                                        mMvVideoTitleMap.get(videoId));

                                            }
                                        }
                                    }

                                }

                                AppLogger.i("获取视频播放数据：" + response1.toString());
                            }

                            @Override
                            public void onError(ANError anError) {
                                // handle error
                            }
                        });

    }


}
