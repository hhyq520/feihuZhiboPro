package cn.feihutv.zhibofeihu.data.network.socket.model.live;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import cn.feihutv.zhibofeihu.data.network.socket.model.SocketBaseResponse;

/**
 * Created by huanghao on 2017/10/16.
 */

public class CloseLiveResponse extends SocketBaseResponse{
    @Expose
    @SerializedName("Data")
    private closeLiveData mCloseLiveData;

    public closeLiveData getmCloseLiveData() {
        return mCloseLiveData;
    }

    public void setmCloseLiveData(closeLiveData mCloseLiveData) {
        this.mCloseLiveData = mCloseLiveData;
    }

    public static class closeLiveData{
        @Expose
        @SerializedName("GHB")
        private int GHB;
        @Expose
        @SerializedName("Watches")
        private int Watches;
        @Expose
        @SerializedName("Followers")
        private int Followers;
        @Expose
        @SerializedName("LiveTime")
        private int LiveTime;
        @Expose
        @SerializedName("MonthTime")
        private int MonthTime;

        public int getGHB() {
            return GHB;
        }

        public void setGHB(int GHB) {
            this.GHB = GHB;
        }

        public int getWatches() {
            return Watches;
        }

        public void setWatches(int watches) {
            Watches = watches;
        }

        public int getFollowers() {
            return Followers;
        }

        public void setFollowers(int followers) {
            Followers = followers;
        }

        public int getLiveTime() {
            return LiveTime;
        }

        public void setLiveTime(int liveTime) {
            LiveTime = liveTime;
        }

        public int getMonthTime() {
            return MonthTime;
        }

        public void setMonthTime(int monthTime) {
            MonthTime = monthTime;
        }
    }
}
