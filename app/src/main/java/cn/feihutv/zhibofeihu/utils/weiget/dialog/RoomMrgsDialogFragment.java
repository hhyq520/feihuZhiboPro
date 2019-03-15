package cn.feihutv.zhibofeihu.utils.weiget.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.feihutv.zhibofeihu.R;
import cn.feihutv.zhibofeihu.data.network.http.model.liveroom.GetRoomMrgsResponce;
import cn.feihutv.zhibofeihu.ui.live.OnItemClickListener;
import cn.feihutv.zhibofeihu.ui.live.RoomMrgsCancleClickListener;
import cn.feihutv.zhibofeihu.ui.live.SWCameraStreamingMvpPresenter;
import cn.feihutv.zhibofeihu.ui.live.SWCameraStreamingMvpView;
import cn.feihutv.zhibofeihu.ui.me.adapter.RoomMrgsAdapter;
import cn.feihutv.zhibofeihu.utils.ListViewDecoration;

/**
 * Created by huanghao on 2017/5/5.
 */

public class RoomMrgsDialogFragment extends DialogFragment  {
    private Dialog mDetailDialog;

    public interface OndissMissListener{
        void dissmiss();
    }
    private OndissMissListener mListener;
    public void setOndissMissListener(OndissMissListener listener){
        mListener=listener;
    }

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    private TextView textNoTip;
    private RoomMrgsAdapter adapter;
    private List<GetRoomMrgsResponce.RoomMrgData> roommrgsUserList=new ArrayList<>();
    private  SWCameraStreamingMvpPresenter<SWCameraStreamingMvpView> mvpPresenter;
    public static RoomMrgsDialogFragment newInstance(SWCameraStreamingMvpPresenter<SWCameraStreamingMvpView> mvpPresenter) {
        RoomMrgsDialogFragment fragment = new RoomMrgsDialogFragment();
        fragment.mvpPresenter=mvpPresenter;
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDetailDialog = new Dialog(getActivity(), R.style.cb_dialog);
        mDetailDialog.setContentView(R.layout.frament_dialog_roommrgs);
        recyclerView=(RecyclerView)mDetailDialog.findViewById(R.id.recyclerview) ;
        textNoTip=(TextView) mDetailDialog.findViewById(R.id.no_tip) ;
        swipeRefreshLayout=(SwipeRefreshLayout)mDetailDialog.findViewById(R.id.roommrgs_swipe_layout) ;
        initView(mDetailDialog);

        return mDetailDialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if(mListener!=null){
            mListener.dissmiss();
        }
        super.onDismiss(dialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels),(int)(dm.heightPixels*0.5));
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.setCanceledOnTouchOutside(true);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.cb_dialog);
    }

    private void initView(final Dialog mDetailDialog) {
        loadDatas();
        adapter = new RoomMrgsAdapter(roommrgsUserList,true);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.appColor));
        swipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));// 布局管理器。
        recyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        recyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        recyclerView.addItemDecoration(new ListViewDecoration());// 添加分割线。
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(view.getId()==R.id.tv_cancle){
                    showRoomMrgsDialog(position);
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    protected void loadDatas() {
        mvpPresenter.getRoomMgrs();
    }



    /**
     * 刷新监听。
     */
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mvpPresenter.getRoomMgrs();
                    // TODO: 2017/3/13 刷新数据
                    swipeRefreshLayout.setRefreshing(false);
                }
            }, 2000);
        }
    };

    /**
     * 取消场控
     * @param positon
     */
    private Dialog pickDialog2;
    private void showRoomMrgsDialog(final int positon) {
        pickDialog2 = new Dialog(getActivity(), R.style.floag_dialog);
        pickDialog2.setContentView(R.layout.dialog_roommrgs);

        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window dlgwin = pickDialog2.getWindow();
        WindowManager.LayoutParams lp = dlgwin.getAttributes();
        dlgwin.setGravity(Gravity.CENTER);
        lp.width = (int) (display.getWidth()); //设置宽度

        pickDialog2.getWindow().setAttributes(lp);
        Button button = (Button) pickDialog2.findViewById(R.id.btn_pop_renzheng);
        TextView tv_cancle = (TextView) pickDialog2.findViewById(R.id.tv_cancle);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 点击取消场控按钮
                mvpPresenter.cancelRoomMgr(roommrgsUserList.get(positon).getUserId(),positon);
            }
        });
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDialog2.dismiss();
            }
        });
        pickDialog2.setCanceledOnTouchOutside(false);
        pickDialog2.show();
    }


    public void cancelRoomMgr(boolean isSuccess,int positon){
        if(isSuccess){
            roommrgsUserList.remove(positon);
            adapter.notifyDataSetChanged();
            if(roommrgsUserList.size()==0){
                swipeRefreshLayout.setVisibility(View.GONE);
                textNoTip.setVisibility(View.VISIBLE);
            }
            pickDialog2.dismiss();
        }else{
            pickDialog2.dismiss();
        }
    }

    public void notifyRoomMrgList(List<GetRoomMrgsResponce.RoomMrgData> datas){
        if(datas.size()>0){
            if(roommrgsUserList.size()>0){
                roommrgsUserList.clear();
            }
            roommrgsUserList.addAll(datas);
            adapter.setNewData(roommrgsUserList);
        }else{
            swipeRefreshLayout.setVisibility(View.GONE);
            textNoTip.setVisibility(View.VISIBLE);
        }
    }
}
