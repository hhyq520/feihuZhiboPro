package cn.feihutv.zhibofeihu.ui.me;


import android.content.Context;

import com.tencent.cos.task.listener.IUploadTaskListener;

import cn.feihutv.zhibofeihu.di.PerActivity;
import cn.feihutv.zhibofeihu.ui.base.MvpPresenter;


/**
 * <pre>
 *     author : sichu.chen
 *     time   :
 *     desc   : 定义P操作
 *     version: 1.0
 * </pre>
 */
@PerActivity
public interface MyMvpPresenter<V extends MyMvpView> extends MvpPresenter<V> {

    //定义业务处理方法

    void loadUserDataBase();

}







