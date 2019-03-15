package cn.feihutv.zhibofeihu.data.db.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;

import cn.feihutv.zhibofeihu.data.network.http.model.home.GetBannerResponse;

/**
 * <pre>
 *      author : liwen.chen
 *      time   : 2017/10/20 10:26
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */
@Entity
public class SysSignAwardBean {

    /**
     * day : 1
     * goods : [{"type":4,"id":0,"cnt":10},{"type":5,"id":0,"cnt":10}]
     */

    @Id
    private String day;
    private String goods;

    @Generated(hash = 1071534825)
    public SysSignAwardBean(String day, String goods) {
        this.day = day;
        this.goods = goods;
    }

    @Generated(hash = 2120628735)
    public SysSignAwardBean() {
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;

    }

}
