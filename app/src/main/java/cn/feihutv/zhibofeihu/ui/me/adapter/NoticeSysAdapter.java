package cn.feihutv.zhibofeihu.ui.me.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.db.model.NoticeSysEntity;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/23 10:01
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class NoticeSysAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<NoticeSysEntity> datas;

    public NoticeSysAdapter(Context context) {
        this.context = context;
        this.datas = new ArrayList<>();
    }

    public void setDatas(List<NoticeSysEntity> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }
    public interface AcceptRequestListener{
        void accept(NoticeSysEntity sysEntity);
    }
    private AcceptRequestListener mListener;
    public void setAcceptRequestListener(AcceptRequestListener listener){
        mListener=listener;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(context).inflate(R.layout.item_recycler_notice_sys_two, parent, false);
                viewHolder = new MyTwoViewHolder(view);
                break;
            case 1:
                view = LayoutInflater.from(context).inflate(R.layout.item_recycler_notice_sys_one, parent, false);
                viewHolder = new MyOneViewHolder(view);
                break;
            default:
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        switch (getItemViewType(position)) {
            case 0:
                final MyTwoViewHolder holder2 = (MyTwoViewHolder) holder;
                holder2.tv_time.setText(TCUtils.getChatTime((long)(datas.get(position).getTime()) * 1000));
                String str="<font color='#ffbb00'> " + datas.get(position).getNickName() + "(" + datas.get(position).getAccountId() + ")" + "</font>" +
                        "<font color='#5d5d5d'>给你发送一个交易请求</font>";
                holder2.tv_notice_nickname.setText(Html.fromHtml(str));
                holder2.tv_notice_ward.setText("飞虎流星 "+ datas.get(position).getCnt() + " 个," + "价值 " + datas.get(position).getAmount() + " 虎币");
                holder2.tv_notice_cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        NoticeSysEntity sysEntity=datas.get(position);
                        sysEntity.setIsIgnore(true);
                        new CompositeDisposable().add(FeihuZhiboApplication.getApplication().mDataManager
                        .updateNoticeSys(sysEntity)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(@NonNull Boolean aBoolean) throws Exception {

                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {

                            }
                        }));
                        holder2.tv_notice_cancle.setText("已忽略");
                        holder2.tv_notice_recept.setEnabled(false);
                    }
                });
                holder2.tv_notice_recept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(datas.get(position).getIsAccept()){

                        }else{
                            if(mListener!=null) {
                                mListener.accept(datas.get(position));
                            }
                        }
                    }
                });
                if(datas.get(position).getIsAccept()){
                    holder2.tv_notice_recept.setText("已接受");
                    holder2.tv_notice_cancle.setEnabled(false);
                }else{
                    holder2.tv_notice_recept.setText("接受");
                }

                if(datas.get(position).getIsIgnore()){
                    holder2.tv_notice_cancle.setText("已忽略");
                    holder2.tv_notice_recept.setEnabled(false);
                }else{
                    holder2.tv_notice_cancle.setText("忽略");
                }
                break;
            case 1:
                MyOneViewHolder holder1 = (MyOneViewHolder) holder;
                holder1.tv_sys.setText(datas.get(position).getContent());
                holder1.tv_time.setText(TCUtils.getChatTime((long)(datas.get(position).getTime()) * 1000));
                break;
            default:
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (TextUtils.isEmpty(datas.get(position).getAction())) {
            // 普通文本
            return 1;
        } else {
            // 交易请求消息
            return 0;
        }
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    class MyOneViewHolder extends RecyclerView.ViewHolder {

        TextView tv_sys, tv_time;

        public MyOneViewHolder(View itemView) {
            super(itemView);
            tv_sys = (TextView) itemView.findViewById(R.id.tv_sys);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }

    class MyTwoViewHolder extends RecyclerView.ViewHolder {

        TextView tv_time, tv_notice_nickname, tv_notice_ward, tv_notice_cancle, tv_notice_recept;

        public MyTwoViewHolder(View itemView) {
            super(itemView);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_notice_nickname = (TextView) itemView.findViewById(R.id.tv_notice_nickname);
            tv_notice_ward = (TextView) itemView.findViewById(R.id.tv_notice_ward);
            tv_notice_cancle = (TextView) itemView.findViewById(R.id.tv_notice_cancle);
            tv_notice_recept = (TextView) itemView.findViewById(R.id.tv_notice_recept);

        }
    }
}

