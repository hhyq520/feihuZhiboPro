package cn.feihutv.zhibofeihu.data.network.http.model.mv;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/11/11
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class GetMyNeedListResponse extends BaseResponse{

    @Expose
    @SerializedName("Data")
    private GetMyNeedListData mGetMyNeedListData;

    public GetMyNeedListData getGetMyNeedListData() {
        return mGetMyNeedListData;
    }

    public void setGetMyNeedListData(GetMyNeedListData getMyNeedListData) {
        mGetMyNeedListData = getMyNeedListData;
    }


    public static class GetMyNeedListData{


        @Expose
        @SerializedName("List")
        private List<GetMyNeedList> mGetMyNeedLists;//偏移量（首次传0，之后从返回值中获取）
        @Expose
        @SerializedName("NextOffset")
        private  String  nextOffset;//加载条数（最多20）

        public List<GetMyNeedList> getGetMyNeedLists() {
            return mGetMyNeedLists;
        }

        public void setGetMyNeedLists(List<GetMyNeedList> getMyNeedLists) {
            mGetMyNeedLists = getMyNeedLists;
        }

        public String getNextOffset() {
            return nextOffset;
        }

        public void setNextOffset(String nextOffset) {
            this.nextOffset = nextOffset;
        }
    }


    public static class GetMyNeedList {



        @Expose
        @SerializedName("HB")
        private  Long  hB;//10000,//悬赏虎币数量

        @Expose
        @SerializedName("NeedId")
        private  String  needId;//2,//需求id

        @Expose
        @SerializedName("Require")
        private  String  require;//"主播说爱我222",//需求要求

        @Expose
        @SerializedName("SongName")
        private  String  songName;//"菊花残",

        @Expose
        @SerializedName("SingerName")
        private  String  singerName;//



        @Expose
        @SerializedName("T")
        private  Long   t;//-102495, //过期剩余秒数，负数表示已过期

        @Expose
        @SerializedName("Title")
        private  String  title;//"testMeed"//需求标题

        @Expose
        @SerializedName("Status")
        private  int  status;// //需求状态 1显示倒计时可修改 2无倒计时可修改  3显示过期可删除

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getSingerName() {
            return singerName;
        }

        public void setSingerName(String singerName) {
            this.singerName = singerName;
        }

        public Long gethB() {
            return hB;
        }

        public void sethB(Long hB) {
            this.hB = hB;
        }

        public String getNeedId() {
            return needId;
        }

        public void setNeedId(String needId) {
            this.needId = needId;
        }

        public String getRequire() {
            return require;
        }

        public void setRequire(String require) {
            this.require = require;
        }

        public String getSongName() {
            return songName;
        }

        public void setSongName(String songName) {
            this.songName = songName;
        }

        public Long getT() {
            return t;
        }

        public void setT(Long t) {
            this.t = t;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }


    }
