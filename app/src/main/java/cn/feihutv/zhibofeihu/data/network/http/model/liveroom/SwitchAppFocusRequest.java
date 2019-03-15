package cn.feihutv.zhibofeihu.data.network.http.model.liveroom;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by huanghao on 2017/10/13.
 */

public class SwitchAppFocusRequest {
    @Expose
    @SerializedName("focus")
    private boolean focus;

    public boolean isFocus() {
        return focus;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
    }
    public SwitchAppFocusRequest(boolean focus){
        this.focus=focus;
    }
}
