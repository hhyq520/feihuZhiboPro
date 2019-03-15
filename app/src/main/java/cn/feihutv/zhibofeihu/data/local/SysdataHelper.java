package cn.feihutv.zhibofeihu.data.local;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.data.db.AppDbHelper;
import cn.feihutv.zhibofeihu.data.db.model.SysConfigBean;
import cn.feihutv.zhibofeihu.data.db.model.SysGameBetBean;
import cn.feihutv.zhibofeihu.data.db.model.SysGiftNewBean;
import cn.feihutv.zhibofeihu.data.db.model.SysGoodsNewBean;
import cn.feihutv.zhibofeihu.data.local.model.SysDataEntity;
import cn.feihutv.zhibofeihu.data.local.model.SysbagBean;

/**
 * Created by huanghao on 2017/4/7.
 */

public class SysdataHelper {

    public static final String SYSDATA_URL = "sysdata_url";
    public static int getStaticGiftLength(List<SysGiftNewBean> sysGiftBeen){
        int i=0;
        if (sysGiftBeen.size() > 0) {
            for (SysGiftNewBean item : sysGiftBeen) {
                if (item.getIsAnimation().equals("0")&&!item.getId().equals("20")) {
                   i++;
                }
            }
        }
        return i;
    }

    public static boolean isGiftVisual(String id,List<SysGoodsNewBean> sysGoods){
        boolean isShow=true;
        if(giftInShop(id,sysGoods)) {
            for (SysGoodsNewBean item : sysGoods) {
                if (item.getObjId().equals(id)) {
                    if (item.getShow().equals("0")) {
                        isShow = false;
                    }else{
                        long chazhi =FeihuZhiboApplication.getApplication().getComponent()
                                .getDataManager().getTimeChaZhi();
//                        long chazhi = SharePreferenceUtil.getSessionLong(FeihuZhiboApplication.getApplication(), "chazhi");
                        long second = (long) (System.currentTimeMillis() / 1000) + chazhi;
                        if (second < Long.valueOf(item.getShelfBegin()) || second > Long.valueOf(item.getShelfEnd())) {
                            isShow = false;
                        }
                    }
                    break;
                }
            }
        }else{
            isShow = false;
        }
        return isShow;
    }

    private static boolean giftInShop(String id,List<SysGoodsNewBean> sysGoods){
        boolean isShow=false;
        for (SysGoodsNewBean item : sysGoods) {
            if (item.getObjId().equals(id)) {
                isShow=true;
                break;
            }
        }
        return isShow;
    }


    public static SysGiftNewBean getGiftBeanByID(List<SysGiftNewBean> sysGiftNewBeanList,String giftID) {
        SysGiftNewBean targetGiftBean = null;
        if (sysGiftNewBeanList!=null&&sysGiftNewBeanList.size() > 0) {
            for (SysGiftNewBean item : sysGiftNewBeanList) {
                if (item.getId().equals(giftID)) {
                    targetGiftBean = new  SysGiftNewBean();
                    targetGiftBean.setId(item.getId());
                    targetGiftBean.setName(item.getName());
                    targetGiftBean.setIcon(item.getIcon());
                    targetGiftBean.setAnimName(item.getAnimName());
                    targetGiftBean.setIncomePercent(item.getIncomePercent());
                    targetGiftBean.setMount(item.isMount());
                    targetGiftBean.setTips(item.getTips());
                    targetGiftBean.setIsAnimation(item.getIsAnimation());
                    targetGiftBean.setShelfBegin(item.getShelfBegin());
                    targetGiftBean.setShelfEnd(item.getShelfEnd());
                    targetGiftBean.setGiftCount(item.getGiftCount());
                    targetGiftBean.setSelected(item.isSelected());
                    targetGiftBean.setSortOrder(item.getSortOrder());
                    targetGiftBean.setEnableVip(item.getEnableVip());
                    break;
                }
            }
        }
        return targetGiftBean;
    }

    public static List<SysGiftNewBean> getbagBeanList(List<SysGiftNewBean> sysGiftNewBeanList,List<SysbagBean> sysbagBeen) {
        List<SysGiftNewBean> sysGiftBeen = new ArrayList<>();
        for (SysbagBean item : sysbagBeen) {
            SysGiftNewBean giftBean = getGiftBeanByID(sysGiftNewBeanList,String.valueOf(item.getId()));
            if (giftBean != null) {
                giftBean.setGiftCount(item.getCnt());
                sysGiftBeen.add(giftBean);
            }
        }
        return sysGiftBeen;
    }
}
