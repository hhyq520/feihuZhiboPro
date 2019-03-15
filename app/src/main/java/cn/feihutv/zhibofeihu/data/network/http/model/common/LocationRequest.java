package cn.feihutv.zhibofeihu.data.network.http.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by huanghao on 2017/10/13.
 */

public class LocationRequest {
    @Expose
    @SerializedName("latlng")
    private String latlng;
    @Expose
    @SerializedName("sensor")
    private boolean sensor;
    @Expose
    @SerializedName("language")
    private String language;

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }

    public boolean isSensor() {
        return sensor;
    }

    public void setSensor(boolean sensor) {
        this.sensor = sensor;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public LocationRequest(String latlng,boolean sensor,String language){
        this.latlng=latlng;
        this.sensor=sensor;
        this.language=language;
    }
}
