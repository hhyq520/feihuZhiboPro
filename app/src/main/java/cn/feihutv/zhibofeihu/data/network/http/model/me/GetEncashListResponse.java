package cn.feihutv.zhibofeihu.data.network.http.model.me;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/3 18:43
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class GetEncashListResponse extends BaseResponse {

    @Expose
    @SerializedName("Data")
    private List<GetEncashListResponseData> mGetEncashListResponses;

    public List<GetEncashListResponseData> getGetEncashListResponses() {
        return mGetEncashListResponses;
    }

    public void setGetEncashListResponses(List<GetEncashListResponseData> getEncashListResponses) {
        mGetEncashListResponses = getEncashListResponses;
    }

    public static class GetEncashListResponseData {

        @Expose
        @SerializedName("Id")
        private int id;

        @Expose
        @SerializedName("Status")
        private int status;

        @Expose
        @SerializedName("GHB")
        private int gHB;

        @Expose
        @SerializedName("Amount")
        private int amount;

        @Expose
        @SerializedName("CreateTime")
        private int createTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getgHB() {
            return gHB;
        }

        public void setgHB(int gHB) {
            this.gHB = gHB;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getCreateTime() {
            return createTime;
        }

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }
    }

}
