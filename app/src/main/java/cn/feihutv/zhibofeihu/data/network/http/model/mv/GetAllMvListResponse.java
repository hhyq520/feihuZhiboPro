package cn.feihutv.zhibofeihu.data.network.http.model.mv;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
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
public class GetAllMvListResponse extends BaseResponse{

    @Expose
    @SerializedName("Data")
    private GetAllMvListData mGetAllMvListData;

    public GetAllMvListData getGetAllMvListData() {
        return mGetAllMvListData;
    }

    public void setGetAllMvListData(GetAllMvListData getAllMvListData) {
        mGetAllMvListData = getAllMvListData;
    }

    public static class GetAllMvListData {
        @Expose
        @SerializedName("List")
        private List<GetAllMvList> mGetAllMvLists;

        @Expose
        @SerializedName("NextOffset")
        private String nextOffset;

        public List<GetAllMvList> getGetAllMvLists() {
            return mGetAllMvLists;
        }

        public void setGetAllMvLists(List<GetAllMvList> getAllMvLists) {
            mGetAllMvLists = getAllMvLists;
        }

        public String getNextOffset() {
            return nextOffset;
        }

        public void setNextOffset(String nextOffset) {
            this.nextOffset = nextOffset;
        }
    }


    public static class GetAllMvList implements Serializable{


            @Expose
            @SerializedName("Comments")
            private int comments;
            @Expose
            @SerializedName("Cover")
            private String cover;
            @Expose
            @SerializedName("LiveStatus ")
            private boolean liveStatus ;
            @Expose
            @SerializedName("MVId")
            private int mVId;
            @Expose
            @SerializedName("NeedNickname")
            private String needNickname;
            @Expose
            @SerializedName("NeedUid")
            private String needUid;
            @Expose
            @SerializedName("OwnerAvatar")
            private String ownerAvatar;
            @Expose
            @SerializedName("OwnerNickname")
            private String ownerNickname;
            @Expose
            @SerializedName("OwnerUid")
            private String ownerUid;
            @Expose
            @SerializedName("Plays")
            private int plays;
            @Expose
            @SerializedName("Title")
            private String title;
            @Expose
            @SerializedName("VideoId")
            private String videoId;


            public void setComments(int comments) {
                this.comments = comments;
            }
            public int getComments() {
                return comments;
            }


            public void setCover(String cover) {
                this.cover = cover;
            }
            public String getCover() {
                return cover;
            }


            public void setLiveStatus (boolean liveStatus ) {
                this.liveStatus  = liveStatus ;
            }
            public boolean getLiveStatus () {
                return liveStatus ;
            }


            public void setMVId(int mVId) {
                this.mVId = mVId;
            }
            public int getMVId() {
                return mVId;
            }


            public void setNeedNickname(String needNickname) {
                this.needNickname = needNickname;
            }
            public String getNeedNickname() {
                return needNickname;
            }


            public void setNeedUid(String needUid) {
                this.needUid = needUid;
            }
            public String getNeedUid() {
                return needUid;
            }


            public void setOwnerAvatar(String ownerAvatar) {
                this.ownerAvatar = ownerAvatar;
            }
            public String getOwnerAvatar() {
                return ownerAvatar;
            }


            public void setOwnerNickname(String ownerNickname) {
                this.ownerNickname = ownerNickname;
            }
            public String getOwnerNickname() {
                return ownerNickname;
            }


            public void setOwnerUid(String ownerUid) {
                this.ownerUid = ownerUid;
            }
            public String getOwnerUid() {
                return ownerUid;
            }


            public void setPlays(int plays) {
                this.plays = plays;
            }
            public int getPlays() {
                return plays;
            }


            public void setTitle(String title) {
                this.title = title;
            }
            public String getTitle() {
                return title;
            }


            public void setVideoId(String videoId) {
                this.videoId = videoId;
            }
            public String getVideoId() {
                return videoId;
            }

        }


}
