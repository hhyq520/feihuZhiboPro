package cn.feihutv.zhibofeihu.ui.widget.bsrgift;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.feihutv.zhibofeihu.R;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;


/**
 * Created by yan on 2016/12/14.
 */

public class GiftAnmManager {

    private BSRGiftLayout giftLayout;
    private Context context;

    public GiftAnmManager(BSRGiftLayout giftLayout, Context context) {
        this.context = context;
        this.giftLayout = giftLayout;
    }

    public void showCarTwo(final Bitmap bitmap) {
        final BSRGiftView bsrGiftView = new BSRGiftView(context);
        bsrGiftView.setAlphaTrigger(-1);
        final int during = 300;
        final Subscription[] subscription = new Subscription[1];
        Flowable.interval(during, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Subscriber<Long>() {
                    int index = 0;

                    @Override
                    public void onSubscribe(Subscription s) {
                        subscription[0] = s;
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        BSRPathPoint carTwo = new BSRPathPoint();
                        carTwo.setDuring(during);
                        carTwo.setInterpolator(new LinearInterpolator());
                        carTwo.setRes(context,bitmap);
                        carTwo.setAdjustScaleInScreen(1f);
                        carTwo.setAntiAlias(true);
                        bsrGiftView.addBSRPathPointAndDraw(carTwo);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        BSRPathView bsrPathView = new BSRPathView();
        bsrPathView.setChild(bsrGiftView);
        bsrPathView.setPositionInScreen(true);
        bsrPathView.addPositionControlPoint(1f, 0.01f);
        bsrPathView.addPositionControlPoint(0.3f, 0.4f);
        bsrPathView.addPositionControlPoint(0.3f, 0.4f);
        bsrPathView.addPositionControlPoint(0.3f, 0.4f);
        bsrPathView.addPositionControlPoint(0.3f, 0.4f);
        bsrPathView.addPositionControlPoint(0.3f, 0.4f);
        bsrPathView.addPositionControlPoint(0.3f, 0.4f);
        bsrPathView.addPositionControlPoint(-0.5f, 0.6f);
        bsrPathView.addPositionControlPoint(-0.5f, 0.6f);
        bsrPathView.addPositionControlPoint(-0.5f, 0.6f);
//        bsrPathView.addPositionControlPoint(0.06f, 0.3f);
        bsrPathView.addScaleControl(0.1f);
        bsrPathView.addScaleControl(0.2f);
        bsrPathView.addScaleControl(0.3f);
        bsrPathView.addScaleControl(0.3f);
        bsrPathView.addScaleControl(0.3f);
        bsrPathView.addScaleControl(0.3f);
        bsrPathView.addScaleControl(0.3f);
        bsrPathView.addScaleControl(0.35f);
        bsrPathView.addScaleControl(0.35f);
        bsrPathView.addScaleControl(0.35f);
        bsrPathView.setXPercent(0f);
        bsrPathView.setYPercent(0f);
        bsrPathView.setDuring(4000);
        bsrPathView.setInterpolator(new AccelerateInterpolator());
        bsrPathView.addEndListeners(new OnAnmEndListener() {
            @Override
            public void onAnimationEnd(BSRPathBase bsrPathPoint) {
                subscription[0].cancel();
            }
        });
        giftLayout.addChild(bsrPathView);
    }

}
