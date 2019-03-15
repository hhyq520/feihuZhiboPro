package cn.feihutv.zhibofeihu.data.network.http.model.me;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/10/26
 *     desc   : 社区-获取动态列表
 *     version: 1.0
 * </pre>
 */
public class LoadFeedListRequest {

    @Expose
    @SerializedName("userId")
    public String userId; //   空或无表示自己

    @Expose
    @SerializedName("last")
    public String last; //   上一页最后一条动态的ID（可选）//空或无表示第一页


    @Expose
    @SerializedName("cnt")
    public String cnt; // 加载数量（可选）空或无默认20条


    public LoadFeedListRequest(String userId, String last, String cnt) {
        this.userId = userId;
        this.last = last;
        this.cnt = cnt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getCnt() {
        return cnt;
    }

    public void setCnt(String cnt) {
        this.cnt = cnt;
    }
}
