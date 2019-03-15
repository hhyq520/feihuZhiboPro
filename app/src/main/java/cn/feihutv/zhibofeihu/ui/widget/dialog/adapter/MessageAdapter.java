package cn.feihutv.zhibofeihu.ui.widget.dialog.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.db.model.MessageEntity;
import cn.feihutv.zhibofeihu.data.db.model.RecentItem;
import cn.feihutv.zhibofeihu.data.network.http.model.common.MessageRequest;
import cn.feihutv.zhibofeihu.data.network.http.model.common.MessageResponce;
import cn.feihutv.zhibofeihu.ui.widget.BottomBarView;
import cn.feihutv.zhibofeihu.ui.widget.MyTextView;
import cn.feihutv.zhibofeihu.utils.FHUtils;
import cn.feihutv.zhibofeihu.utils.PhotoUtils;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;
import cn.feihutv.zhibofeihu.utils.TCGlideCircleTransform;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MessageAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<MessageEntity> mMsgList;
    private static final int TYPE_LEFT = 0;
    private static final int TYPE_RIGHT = 1;
    ViewHolder mholder;
    private ChatClickHeadListener chatClickHead;

    public void setChatClickHead(ChatClickHeadListener chatClickHead) {
        this.chatClickHead = chatClickHead;
    }

    public MessageAdapter(Context context, List<MessageEntity> msgList) {
        this.mContext = context;
        mMsgList = msgList;
        mInflater = LayoutInflater.from(context);
    }

    public void removeHeadMsg() {
        if (mMsgList.size() - 10 > 10) {
            for (int i = 0; i < 10; i++) {
                mMsgList.remove(i);
            }
            notifyDataSetChanged();
        }
    }

    public void setMessageList(List<MessageEntity> msgList) {
        mMsgList = msgList;
        notifyDataSetChanged();
    }

    public void upDateMsg(MessageEntity msg) {
        mMsgList.add(msg);
        Log.e("print", "upDateMsg: ----->" + msg);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mMsgList == null ? 0 : mMsgList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mMsgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final MessageEntity item = mMsgList.get(position);
        int type = getItemViewType(position);
        ViewHolder holderLeft = null;
        ViewHolder holderRight = null;
        if (convertView == null) {
            switch (type) {
                case TYPE_LEFT:
                    convertView = mInflater.inflate(R.layout.chat_item_left, null);
                    holderLeft = new ViewHolder();
                    holderLeft.head = (ImageView) convertView.findViewById(R.id.icon);
                    holderLeft.imageFail = (ImageView) convertView.findViewById(R.id.send_img_fail);
                    holderLeft.time = (TextView) convertView.findViewById(R.id.datetime);
                    holderLeft.msg = (MyTextView) convertView.findViewById(R.id.message_text);
                    holderLeft.progressBar = (ProgressBar) convertView
                            .findViewById(R.id.progressBar);
                    holderLeft.imgmsg = (ImageView) convertView.findViewById(R.id.picture_news);
                    holderLeft.percentage = (TextView) convertView.findViewById(R.id.percentage);
                    holderLeft.relativeLayoutmes = (RelativeLayout) convertView.findViewById(R.id.relativeLayoutmes);
                    convertView.setTag(holderLeft);
                    break;
                case TYPE_RIGHT:
                    convertView = mInflater.inflate(R.layout.chat_item_right, null);
                    holderRight = new ViewHolder();
                    holderRight.head = (ImageView) convertView.findViewById(R.id.icon);
                    holderRight.imageFail = (ImageView) convertView.findViewById(R.id.send_img_fail);
                    holderRight.time = (TextView) convertView.findViewById(R.id.datetime);
                    holderRight.msg = (MyTextView) convertView.findViewById(R.id.message_text);
                    holderRight.progressBar = (ProgressBar) convertView
                            .findViewById(R.id.progressBar);
                    holderRight.imgmsg = (ImageView) convertView.findViewById(R.id.picture_news);
                    holderRight.percentage = (TextView) convertView.findViewById(R.id.percentage);
                    holderRight.relativeLayoutmes = (RelativeLayout) convertView.findViewById(R.id.relativeLayoutmes);
                    convertView.setTag(holderRight);
                    break;
                default:
                    break;
            }
        } else {
            switch (type) {
                case TYPE_LEFT:
                    holderLeft = (ViewHolder) convertView.getTag();
                    break;
                case TYPE_RIGHT:
                    holderRight = (ViewHolder) convertView.getTag();
                    break;
                default:
                    break;
            }
        }
        ViewHolder viewHolder = null;
        switch (type) {
            case TYPE_LEFT:
                viewHolder = holderLeft;
                viewHolder.head.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (chatClickHead != null) {
                            chatClickHead.clickHead(mMsgList.get(position).getSenderId());
                        }
                    }
                });
                break;
            case TYPE_RIGHT:
                viewHolder = holderRight;
                break;
            default:
                break;
        }
        viewHolder.time.setText(TCUtils.getChatTime((item.getTime()) * 1000));
        if (getCount() <= 1) {
            viewHolder.time.setVisibility(View.VISIBLE);
        } else {
            if (position >= 1) {
                if (mMsgList.get(position).getTime() - mMsgList.get(position - 1).getTime() <= 300) {
                    viewHolder.time.setVisibility(View.GONE);
                } else {
                    viewHolder.time.setVisibility(View.VISIBLE);
                }
            } else {
                viewHolder.time.setVisibility(View.VISIBLE);
            }
        }
        if (mMsgList.get(position).getMsgStatus() == 1) {
            viewHolder.imageFail.setVisibility(View.GONE);
        } else {
            viewHolder.imageFail.setVisibility(View.VISIBLE);
        }

        Glide.with(mContext)
                .load(item.getHeadUrl())
                .apply(new RequestOptions().placeholder(R.drawable.face).diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .transform(new TCGlideCircleTransform(mContext))).into(viewHolder.head);


        viewHolder.msg.setVisibility(View.VISIBLE);
        viewHolder.msg.setText(item.getContent());
        viewHolder.relativeLayoutmes.setVisibility(View.VISIBLE);
        viewHolder.imgmsg.setVisibility(View.GONE);
        viewHolder.imageFail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(item);
            }
        });
        mholder = viewHolder;
        return convertView;
    }

    private void sendMessage(final MessageEntity entity) {
        if (TCUtils.isNetworkAvailable(mContext)) {
            new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                    .doMessageApiCall(new MessageRequest(entity.getSenderId(), entity.getContent()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<MessageResponce>() {
                        @Override
                        public void accept(@NonNull MessageResponce responce) throws Exception {
                            if (responce.getCode() == 0) {
                                entity.setMsgStatus(1);
                                notifyDataSetChanged();

                                new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager.
                                        updataMessageEntity(entity)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Consumer<Object>() {
                                            @Override
                                            public void accept(@io.reactivex.annotations.NonNull Object responce) throws Exception {

                                            }
                                        }, new Consumer<Throwable>() {
                                            @Override
                                            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {

                                            }
                                        }));
                            } else {
                                FHUtils.showToast("发送失败！");
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {

                        }
                    }));
        } else {

        }
    }

    private void showImageView(ImageView imgmsg, String filepath) {
        //需要上传图片的原始的宽高
        if (filepath != null) {
            Bitmap bitmap = PhotoUtils.getBitmapFromFile(filepath);
            Bundle bundle = PhotoUtils.getBitmapWidthAndHeight(bitmap);
            if (bundle != null) {
                int width = bundle.getInt("width");
                int height = bundle.getInt("height");
                PhotoUtils.ImageSize imageSize = PhotoUtils.getThumbnailDisplaySize(width, height, getImageMaxEdge(), getImageMinEdge());
//                Glide.with(mContext).load(filepath).override(imageSize.width, imageSize.height).into(imgmsg);
            } else {
//                Glide.with(mContext).load(R.drawable.nim_image_download_failed).override(getImageMinEdge(), getImageMinEdge()).into(imgmsg);
            }

        }

    }

    public int getImageMaxEdge() {
        return (int) (165.0 / 320.0 * mContext.getResources().getDisplayMetrics().widthPixels);
    }

    public int getImageMinEdge() {
        return (int) (76.0 / 320.0 * mContext.getResources().getDisplayMetrics().widthPixels);
    }

    public void setMesSuccess() {
        mholder.progressBar.setVisibility(View.INVISIBLE);
    }

    public void setMesFail() {
        mholder.imageFail.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemViewType(int position) {
        if (mMsgList.get(position).getIsComMeg()) {
            return TYPE_LEFT;
        } else {
            return TYPE_RIGHT;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    static class ViewHolder {
        ImageView head;
        TextView time;
        MyTextView msg;
        ImageView imageFail;
        ProgressBar progressBar;
        ImageView imgmsg;
        TextView percentage;
        RelativeLayout relativeLayoutmes;
    }

    public interface ChatClickHeadListener {
        void clickHead(String userId);
    }
}
