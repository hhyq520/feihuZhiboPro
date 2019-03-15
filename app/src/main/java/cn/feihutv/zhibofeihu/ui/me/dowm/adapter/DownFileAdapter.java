package cn.feihutv.zhibofeihu.ui.me.dowm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.db.model.MvDownLog;
import cn.feihutv.zhibofeihu.utils.AppLogger;
import cn.feihutv.zhibofeihu.utils.TCUtils;
import cn.feihutv.zhibofeihu.utils.rxdownload.CustomMission;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import zlc.season.rxdownload3.RxDownload;
import zlc.season.rxdownload3.core.Downloading;
import zlc.season.rxdownload3.core.Failed;
import zlc.season.rxdownload3.core.Normal;
import zlc.season.rxdownload3.core.Status;
import zlc.season.rxdownload3.core.Succeed;
import zlc.season.rxdownload3.core.Suspend;
import zlc.season.rxdownload3.core.Waiting;
import zlc.season.rxdownload3.extension.ApkInstallExtension;
import zlc.season.rxdownload3.extension.ApkOpenExtension;
import zlc.season.rxdownload3.helper.UtilsKt;


/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/12/11
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class DownFileAdapter  extends RecyclerView.Adapter<DownFileAdapter.ViewHolder> {

    public List<MyDownInfo> getData() {
        return data;
    }

    private List<MyDownInfo> data = new ArrayList<>();



    private Context mContext;


    Map<String,CheckBox> mCheckBoxMap=new HashMap<>();

    public DownFileAdapter(Context context) {
        mContext = context;
    }

    public void addData(List<MyDownInfo> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_download_item,parent,false);
        return new ViewHolder(view,this);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        MyDownInfo myDownInfo=  data.get(position);
        holder.type=myDownInfo.getType();
        if(myDownInfo.getType()==1){
            if(position==0){
                holder. down_type.setText("正在下载");
                holder. down_type.setVisibility(View.VISIBLE);
            }else{
                holder. down_type.setVisibility(View.GONE);
            }
            holder.zb_layout.setVisibility(View.GONE);
            holder.progressLayout.setVisibility(View.VISIBLE);

        }else{
            if(position==0){
                holder. down_type.setText("已下载");
                holder. down_type.setVisibility(View.VISIBLE);
            }else{
                if(data.get(position-1).getType()==1){
                    holder. down_type.setText("已下载");
                    holder. down_type.setVisibility(View.VISIBLE);
                }else{
                    holder. down_type.setVisibility(View.GONE);
                }

            }
            holder.zb_layout.setVisibility(View.VISIBLE);
            holder.progressLayout.setVisibility(View.GONE);
        }

        final CustomMission mission= (CustomMission) myDownInfo.getMission();
        holder.setData(myDownInfo);
        TCUtils.showPicWithUrl(mContext,holder.mv_user_head,
                mission.getZbIcon(), R.drawable.face);
        holder.mv_user_name.setText(mission.getZbName());
        holder.  item_down_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDownMvCallBack!=null){
                    mDownMvCallBack.onItemClick(position,mission);
                }
                AppLogger.e(mission.getTag()+"   name="+mission.getSaveName()+"  path="+mission.getSavePath());

            }
        });


        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(mDownMvCallBack!=null){
                    mDownMvCallBack.onChecked(position,isChecked,mission);
                }
            }
        });
        mCheckBoxMap.put(mission.getTag(),holder.mCheckBox);
    }




    public void checkBoxCheckedAll(boolean isAll){
        for(Map.Entry<String,CheckBox> m:mCheckBoxMap.entrySet()){
            m.getValue().setChecked(isAll);
            m.getValue().setVisibility(View.VISIBLE);
        }
    }



    public void showCheckBoxView(boolean show){
        for(Map.Entry<String,CheckBox> m:mCheckBoxMap.entrySet()){
           if(show) {
               m.getValue().setVisibility(View.VISIBLE);
           }else{
               m.getValue().setVisibility(View.GONE);
           }
        }
    }



    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
            holder.onAttach();
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
            holder.onDetach();
    }



    static class ViewHolder extends RecyclerView.ViewHolder {
        DownFileAdapter mDownFileAdapter;
        private CustomMission customMission;
        MyDownInfo myDownInfo;
        MvDownLog mMvDownLog;
        private Disposable disposable;
        private Status currentStatus;
        public  ImageView icon,mv_user_head;
        public  Button action;
        public  ProgressBar progressBar;
        public  TextView percent;
        public TextView size;
        public TextView down_type,title,mv_user_name;
        public  View zb_layout,progressLayout,item_down_layout;

        public  CheckBox mCheckBox;
        public  int type;

        public ViewHolder(View itemView,DownFileAdapter mDownFileAdapter) {
            super(itemView);
            this.mDownFileAdapter=mDownFileAdapter;
            icon= itemView.findViewById(R.id.icon);
            action= itemView.findViewById(R.id.action);
            progressBar= itemView.findViewById(R.id.progressBar);
            percent= itemView.findViewById(R.id.percent);
            size= itemView.findViewById(R.id.size);
            down_type= itemView.findViewById(R.id.down_type);
            title= itemView.findViewById(R.id.title);
            zb_layout= itemView.findViewById(R.id.zb_layout);
            progressLayout= itemView.findViewById(R.id.progressLayout);
            mv_user_name= itemView.findViewById(R.id.mv_user_name);
            mv_user_head= itemView.findViewById(R.id.mv_user_head);
            item_down_layout= itemView.findViewById(R.id.item_down_layout);

            mCheckBox= itemView.findViewById(R.id.cb_check);
            action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchClick();
                }
            });

        }

        private void dispatchClick() {
            if (currentStatus instanceof Normal) {
                start();
            } else if (currentStatus instanceof Suspend) {
                start();
            } else if (currentStatus instanceof Failed) {
                start();
            } else if (currentStatus instanceof Downloading) {
                stop();
            } else if (currentStatus instanceof Succeed) {

            }
        }




        public void setData(MyDownInfo myDownInfo) {
            this.myDownInfo=myDownInfo;
            this.customMission = (CustomMission) myDownInfo.getMission();
            title.setText(customMission.getTitle());
            Glide.with(itemView.getContext()).load(customMission.getImg()).into(icon);
        }


        public void setData(MvDownLog myDownInfo) {
            this.mMvDownLog = myDownInfo;
            title.setText(mMvDownLog.getTitle());
            mv_user_name.setText(mMvDownLog.getZbName());

            Glide.with(itemView.getContext()).load(mMvDownLog.getIcon()).into(icon);
        }




        public void onAttach() {
            disposable = RxDownload.INSTANCE.create(customMission)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Status>() {
                        @Override
                        public void accept(Status status) throws Exception {
                            currentStatus = status;
                            setProgress(status);
                            setActionText(status);

                        }
                    });
        }

        private void setProgress(Status status) {
            progressBar.setMax((int) status.getTotalSize());
            progressBar.setProgress((int) status.getDownloadSize());
            percent.setText(status.percent());
            size.setText(status.formatString());
        }

        private void setActionText(Status status) {
            String text = "";
            if (status instanceof Normal) {
                text = "开始";
            } else if (status instanceof Suspend) {
                text = "已暂停";
            } else if (status instanceof Waiting) {
                text = "等待中";
            } else if (status instanceof Downloading) {
                text = "暂停";
            } else if (status instanceof Failed) {
                text = "失败";
            } else if (status instanceof Succeed) {
                text = "下载完成";
                 zb_layout.setVisibility(View.VISIBLE);
                 progressLayout.setVisibility(View.GONE);
                //设置下载状态为已下载
                myDownInfo.setType(0);
                mDownFileAdapter.orderList();
            }
            action.setText(text);
        }


        public void onDetach() {
            UtilsKt.dispose(disposable);
        }

        private void start() {
            RxDownload.INSTANCE.start(customMission).subscribe();
        }

        private void stop() {
            RxDownload.INSTANCE.stop(customMission).subscribe();
        }

        private void install() {
            RxDownload.INSTANCE.extension(customMission, ApkInstallExtension.class).subscribe();
        }

        private void open() {
            RxDownload.INSTANCE.extension(customMission, ApkOpenExtension.class).subscribe();
        }
    }


    private void orderList(){
        Observable.create(new ObservableOnSubscribe<List<MyDownInfo>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<MyDownInfo>> e) throws Exception {
                List<MyDownInfo> data1 = new ArrayList<>();
                List<MyDownInfo> data2 = new ArrayList<>();
                for (MyDownInfo mi:data){
                    if(mi.getType()==1){
                        data1.add(mi);
                    }else{
                        data2.add(mi);
                    }
                }
                data.clear();
                data.addAll(data1);
                data.addAll(data2);

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<MyDownInfo>>() {
                    @Override
                    public void accept(@NonNull List<MyDownInfo> myDownInfos) throws Exception {
                        notifyDataSetChanged();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                });

    }


    public void setDownMvCallBack(DownMvCallBack downMvCallBack) {
        mDownMvCallBack = downMvCallBack;
    }

    public DownMvCallBack mDownMvCallBack;

    public interface  DownMvCallBack{

        void  onChecked(int pos,boolean isCheck, CustomMission mission);

        void onItemClick(int p,CustomMission mission);
    }




}
