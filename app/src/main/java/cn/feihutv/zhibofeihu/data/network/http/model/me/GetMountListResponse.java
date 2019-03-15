package cn.feihutv.zhibofeihu.data.network.http.model.me;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/10 20:24
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class GetMountListResponse extends BaseResponse {


    @Expose
    @SerializedName("Data")
    private List<MountListResponseData> mMountListResponseDatas;

    public List<MountListResponseData> getMountListResponseDatas() {
        return mMountListResponseDatas;
    }

    public void setMountListResponseDatas(List<MountListResponseData> mountListResponseDatas) {
        mMountListResponseDatas = mountListResponseDatas;
    }

    public static class MountListResponseData {
        /**
         * ExpireAt : 1512722362
         * Mount : 10
         * type;//0:购买 1：使用 2：卸下
         */

        @Expose
        @SerializedName("ExpireAt")
        private int expireAt;
        @Expose
        @SerializedName("Mount")
        private int mount;
        @Expose
        @SerializedName("type")
        private int type;

        public int getExpireAt() {
            return expireAt;
        }

        public void setExpireAt(int expireAt) {
            this.expireAt = expireAt;
        }

        public int getMount() {
            return mount;
        }

        public void setMount(int mount) {
            this.mount = mount;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
