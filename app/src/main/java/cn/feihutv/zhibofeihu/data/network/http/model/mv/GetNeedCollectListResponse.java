package cn.feihutv.zhibofeihu.data.network.http.model.mv;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/11/14
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class GetNeedCollectListResponse extends BaseResponse {

    @Expose
    @SerializedName("Data")
    private GetNeedCollectListData mGetNeedCollectListData;

    public GetNeedCollectListData getGetNeedCollectListData() {
        return mGetNeedCollectListData;
    }

    public void setGetNeedCollectListData(GetNeedCollectListData getNeedCollectListData) {
        mGetNeedCollectListData = getNeedCollectListData;
    }

    public static class GetNeedCollectListData {


        @Expose
        @SerializedName("NextOffset")
        private String nextOffset;//加载条数（最多20）

        @Expose
        @SerializedName("List")
        private List<GetNeedCollectList> mGetNeedCollectLists;

        public String getNextOffset() {
            return nextOffset;
        }

        public void setNextOffset(String nextOffset) {
            this.nextOffset = nextOffset;
        }

        public List<GetNeedCollectList> getGetNeedCollectLists() {
            return mGetNeedCollectLists;
        }

        public void setGetNeedCollectLists(List<GetNeedCollectList> getNeedCollectLists) {
            mGetNeedCollectLists = getNeedCollectLists;
        }
    }


    public static class GetNeedCollectList {


        @Expose
        @SerializedName("Avatar")
        private String avatar;// "https://iadurl.png", //提需求人的头像

        @Expose
        @SerializedName("HB")
        private String hB;// 10000,//悬赏虎币数量

        @Expose
        @SerializedName("NeedId")
        private String needId;// 1,//需求id

        @Expose
        @SerializedName("Nickname")
        private String nickname;// "admin", //提需求人的昵称

        @Expose
        @SerializedName("Require")
        private String require;// "主播说爱我",//需求要求

        @Expose
        @SerializedName("SongName")
        private String songName;// "菊花台",

        @Expose
        @SerializedName("SingerName")
        private String singerName;// "菊花台",


        @Expose
        @SerializedName("T")
        private Long t;// -100769,  //过期剩余秒数，负数表示已过期

        @Expose
        @SerializedName("Title")
        private String title;// "testMeed",//需求标题

        @Expose
        @SerializedName("Uid")
        private String uid;// "29"//提需求人的uid

        @Expose
        @SerializedName("Collected")
        private boolean collected;// //true已收藏  false未收藏



        @Expose
        @SerializedName("ForUid")
        private String forUid;//"35"//指定的主播uid

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String gethB() {
            return hB;
        }

        public void sethB(String hB) {
            this.hB = hB;
        }

        public String getNeedId() {
            return needId;
        }

        public void setNeedId(String needId) {
            this.needId = needId;
        }

        public String getNickname() {
            return nickname;
        }

        public String getSingerName() {
            return singerName;
        }

        public void setSingerName(String singerName) {
            this.singerName = singerName;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
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

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public boolean isCollected() {
            return collected;
        }

        public void setCollected(boolean collected) {
            this.collected = collected;
        }

        public String getForUid() {
            return forUid;
        }

        public void setForUid(String forUid) {
            this.forUid = forUid;
        }
    }

}
