package cn.feihutv.zhibofeihu.data.network.http.model.bangdan;

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
 *      author : liwen.chen
 *      time   : 2017/10/25 21:03
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class LoadLuckRecordListResponse extends BaseResponse {

    /**
     * Data : {"List":[{"AwardTime":1508850000,"BroadcastType":2,"HeadUrl":"https://img.feihutv.cn/icon/default_headurl.png","LiveStatus":false,"MasterHeadUrl":"http://img.feihutv.cn//headIcon/11332511/59647cf98520875f1395468a.png","NickName":"rxpg","ObtainGiftCnt":1,"ObtainGiftId":1,"ObtainHB":30007,"RoomId":11032512,"RoomUserId":11332511,"UserId":"11336205"},{"AwardTime":1508504400,"BroadcastType":2,"HeadUrl":"https://img.feihutv.cn//headIcon/11335635/59b3b0322a17e454ac71bcee.png","LiveStatus":false,"MasterHeadUrl":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","NickName":"天神","ObtainGiftCnt":1,"ObtainGiftId":1,"ObtainHB":30007,"RoomId":97488866,"RoomUserId":11335615,"UserId":"11335635"},{"AwardTime":1508487000,"BroadcastType":2,"HeadUrl":"https://img.feihutv.cn//headIcon/11335635/59b3b0322a17e454ac71bcee.png","LiveStatus":false,"MasterHeadUrl":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","NickName":"天神","ObtainGiftCnt":1,"ObtainGiftId":2,"ObtainHB":30009,"RoomId":97488866,"RoomUserId":11335615,"UserId":"11335635"},{"AwardTime":1508378400,"BroadcastType":2,"HeadUrl":"https://img.feihutv.cn//headIcon/11334594/59c32a292a17e41f7681fd22.png","LiveStatus":false,"MasterHeadUrl":"https://img.feihutv.cn/icon/default_headurl.png","NickName":"音悦台MV","ObtainGiftCnt":400,"ObtainGiftId":1,"ObtainHB":32880,"RoomId":11032483,"RoomUserId":11332482,"UserId":"11334594"},{"AwardTime":1508327064,"BroadcastType":2,"HeadUrl":"https://img.feihutv.cn/r/976.jpg","LiveStatus":false,"MasterHeadUrl":"https://img.feihutv.cn/icon/default_headurl.png","NickName":"r0000004","ObtainGiftCnt":800,"ObtainGiftId":1,"ObtainHB":60240,"RoomId":11032483,"RoomUserId":11332482,"UserId":"11334597"},{"AwardTime":1508323928,"BroadcastType":2,"HeadUrl":"https://img.feihutv.cn/r/976.jpg","LiveStatus":false,"MasterHeadUrl":"https://img.feihutv.cn/icon/default_headurl.png","NickName":"r0000004","ObtainGiftCnt":1100,"ObtainGiftId":1,"ObtainHB":72480,"RoomId":11032483,"RoomUserId":11332482,"UserId":"11334597"},{"AwardTime":1507601400,"BroadcastType":2,"HeadUrl":"https://img.feihutv.cn//headIcon/11335619/59b5f1972a17e42a34bcc665.png","LiveStatus":false,"MasterHeadUrl":"https://q.qlogo.cn/qqapp/1106077402/8B9C2D593275D7BB14B10A0F5A12B200/100","NickName":"AOVHR","ObtainGiftCnt":7889,"ObtainGiftId":2,"ObtainHB":105734,"RoomId":11035123,"RoomUserId":11336082,"UserId":"11335619"},{"AwardTime":1507555200,"BroadcastType":2,"HeadUrl":"https://img.feihutv.cn/icon/default_headurl.png","LiveStatus":false,"MasterHeadUrl":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","NickName":"vp","ObtainGiftCnt":16,"ObtainGiftId":8,"ObtainHB":30396,"RoomId":30000000,"RoomUserId":11332509,"UserId":"11332488"},{"AwardTime":1507554600,"BroadcastType":2,"HeadUrl":"https://img.feihutv.cn/icon/default_headurl.png","LiveStatus":false,"MasterHeadUrl":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","NickName":"vp","ObtainGiftCnt":6,"ObtainGiftId":2,"ObtainHB":30153,"RoomId":97488866,"RoomUserId":11335615,"UserId":"11332488"},{"AwardTime":1507554000,"BroadcastType":2,"HeadUrl":"https://img.feihutv.cn/icon/default_headurl.png","LiveStatus":false,"MasterHeadUrl":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","NickName":"vp","ObtainGiftCnt":3,"ObtainGiftId":1,"ObtainHB":30129,"RoomId":30000000,"RoomUserId":11332509,"UserId":"11332488"},{"AwardTime":1506500400,"BroadcastType":1,"HeadUrl":"https://img.feihutv.cn//headIcon/11335619/59b5f1972a17e42a34bcc665.png","LiveStatus":false,"MasterHeadUrl":"https://img.feihutv.cn//headIcon/11332615/59bb38a98520874bb17fcf72.jpg","NickName":"AOVHR","ObtainGiftCnt":1834,"ObtainGiftId":8,"ObtainHB":58248,"RoomId":11032618,"RoomUserId":11332615,"UserId":"11335619"},{"AwardTime":1506499800,"BroadcastType":1,"HeadUrl":"https://img.feihutv.cn//headIcon/11335619/59b5f1972a17e42a34bcc665.png","LiveStatus":false,"MasterHeadUrl":"https://img.feihutv.cn//headIcon/11332615/59bb38a98520874bb17fcf72.jpg","NickName":"AOVHR","ObtainGiftCnt":8074,"ObtainGiftId":2,"ObtainHB":150345,"RoomId":11032618,"RoomUserId":11332615,"UserId":"11335619"},{"AwardTime":1506499200,"BroadcastType":1,"HeadUrl":"https://img.feihutv.cn//headIcon/11335619/59b5f1972a17e42a34bcc665.png","LiveStatus":false,"MasterHeadUrl":"https://img.feihutv.cn//headIcon/11332615/59bb38a98520874bb17fcf72.jpg","NickName":"AOVHR","ObtainGiftCnt":9389,"ObtainGiftId":1,"ObtainHB":177045,"RoomId":11032618,"RoomUserId":11332615,"UserId":"11335619"},{"AwardTime":1506478200,"BroadcastType":1,"HeadUrl":"https://img.feihutv.cn//headIcon/11335603/59b78d7a2a17e47ff2678063.jpg","LiveStatus":false,"MasterHeadUrl":"https://img.feihutv.cn//headIcon/11332615/59bb38a98520874bb17fcf72.jpg","NickName":"TK","ObtainGiftCnt":403,"ObtainGiftId":2,"ObtainHB":33868,"RoomId":11032618,"RoomUserId":11332615,"UserId":"11335603"},{"AwardTime":1506477600,"BroadcastType":1,"HeadUrl":"https://img.feihutv.cn//headIcon/11335603/59b78d7a2a17e47ff2678063.jpg","LiveStatus":false,"MasterHeadUrl":"https://img.feihutv.cn//headIcon/11332615/59bb38a98520874bb17fcf72.jpg","NickName":"TK","ObtainGiftCnt":9,"ObtainGiftId":1,"ObtainHB":30064,"RoomId":11032618,"RoomUserId":11332615,"UserId":"11335603"},{"AwardTime":1506414000,"BroadcastType":2,"HeadUrl":"http://img.feihutv.cn//headIcon/11332904/597e9b2b8520871fda62558d.jpg","LiveStatus":false,"MasterHeadUrl":"https://img.feihutv.cn//headIcon/11335603/59b78d7a2a17e47ff2678063.jpg","NickName":"我就测测看","ObtainGiftCnt":9,"ObtainGiftId":8,"ObtainHB":30192,"RoomId":89994666,"RoomUserId":11335603,"UserId":"11332904"},{"AwardTime":1506413400,"BroadcastType":1,"HeadUrl":"http://img.feihutv.cn//headIcon/11332904/597e9b2b8520871fda62558d.jpg","LiveStatus":false,"MasterHeadUrl":"https://img.feihutv.cn/icon/default_headurl.png","NickName":"我就测测看","ObtainGiftCnt":10,"ObtainGiftId":2,"ObtainHB":30096,"RoomId":99999999,"RoomUserId":11332621,"UserId":"11332904"},{"AwardTime":1506412800,"BroadcastType":1,"HeadUrl":"http://img.feihutv.cn//headIcon/11332904/597e9b2b8520871fda62558d.jpg","LiveStatus":false,"MasterHeadUrl":"https://img.feihutv.cn/icon/default_headurl.png","NickName":"我就测测看","ObtainGiftCnt":18,"ObtainGiftId":1,"ObtainHB":30129,"RoomId":99999999,"RoomUserId":11332621,"UserId":"11332904"},{"AwardTime":1506326400,"BroadcastType":1,"HeadUrl":"https://img.feihutv.cn//headIcon/11335595/59b8f9ce8520877a2032b26f.jpg","LiveStatus":false,"MasterHeadUrl":"https://img.feihutv.cn//headIcon/11332615/59bb38a98520874bb17fcf72.jpg","NickName":"Echo","ObtainGiftCnt":14,"ObtainGiftId":1,"ObtainHB":30100,"RoomId":11032618,"RoomUserId":11332615,"UserId":"11335595"},{"AwardTime":1506154800,"BroadcastType":2,"HeadUrl":"https://q.qlogo.cn/qqapp/1106077402/14C5B801179E1254D2F0E4C36ED26C54/100","LiveStatus":false,"MasterHeadUrl":"https://img.feihutv.cn//headIcon/11335608/59b6499b852087500e18d019.png","NickName":"SilenceS","ObtainGiftCnt":2,"ObtainGiftId":8,"ObtainHB":30024,"RoomId":66668666,"RoomUserId":11335608,"UserId":"11336192"}],"NextOffset":706}
     */

    @Expose
    @SerializedName("Data")
    private LoadLuckRecordListResponseData mResponseData;


    public LoadLuckRecordListResponseData getResponseData() {
        return mResponseData;
    }

    public void setResponseData(LoadLuckRecordListResponseData responseData) {
        this.mResponseData = responseData;
    }

    public static class LoadLuckRecordListResponseData {
        /**
         * List : [{"AwardTime":1508850000,"BroadcastType":2,"HeadUrl":"https://img.feihutv.cn/icon/default_headurl.png","LiveStatus":false,"MasterHeadUrl":"http://img.feihutv.cn//headIcon/11332511/59647cf98520875f1395468a.png","NickName":"rxpg","ObtainGiftCnt":1,"ObtainGiftId":1,"ObtainHB":30007,"RoomId":11032512,"RoomUserId":11332511,"UserId":"11336205"},{"AwardTime":1508504400,"BroadcastType":2,"HeadUrl":"https://img.feihutv.cn//headIcon/11335635/59b3b0322a17e454ac71bcee.png","LiveStatus":false,"MasterHeadUrl":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","NickName":"天神","ObtainGiftCnt":1,"ObtainGiftId":1,"ObtainHB":30007,"RoomId":97488866,"RoomUserId":11335615,"UserId":"11335635"},{"AwardTime":1508487000,"BroadcastType":2,"HeadUrl":"https://img.feihutv.cn//headIcon/11335635/59b3b0322a17e454ac71bcee.png","LiveStatus":false,"MasterHeadUrl":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","NickName":"天神","ObtainGiftCnt":1,"ObtainGiftId":2,"ObtainHB":30009,"RoomId":97488866,"RoomUserId":11335615,"UserId":"11335635"},{"AwardTime":1508378400,"BroadcastType":2,"HeadUrl":"https://img.feihutv.cn//headIcon/11334594/59c32a292a17e41f7681fd22.png","LiveStatus":false,"MasterHeadUrl":"https://img.feihutv.cn/icon/default_headurl.png","NickName":"音悦台MV","ObtainGiftCnt":400,"ObtainGiftId":1,"ObtainHB":32880,"RoomId":11032483,"RoomUserId":11332482,"UserId":"11334594"},{"AwardTime":1508327064,"BroadcastType":2,"HeadUrl":"https://img.feihutv.cn/r/976.jpg","LiveStatus":false,"MasterHeadUrl":"https://img.feihutv.cn/icon/default_headurl.png","NickName":"r0000004","ObtainGiftCnt":800,"ObtainGiftId":1,"ObtainHB":60240,"RoomId":11032483,"RoomUserId":11332482,"UserId":"11334597"},{"AwardTime":1508323928,"BroadcastType":2,"HeadUrl":"https://img.feihutv.cn/r/976.jpg","LiveStatus":false,"MasterHeadUrl":"https://img.feihutv.cn/icon/default_headurl.png","NickName":"r0000004","ObtainGiftCnt":1100,"ObtainGiftId":1,"ObtainHB":72480,"RoomId":11032483,"RoomUserId":11332482,"UserId":"11334597"},{"AwardTime":1507601400,"BroadcastType":2,"HeadUrl":"https://img.feihutv.cn//headIcon/11335619/59b5f1972a17e42a34bcc665.png","LiveStatus":false,"MasterHeadUrl":"https://q.qlogo.cn/qqapp/1106077402/8B9C2D593275D7BB14B10A0F5A12B200/100","NickName":"AOVHR","ObtainGiftCnt":7889,"ObtainGiftId":2,"ObtainHB":105734,"RoomId":11035123,"RoomUserId":11336082,"UserId":"11335619"},{"AwardTime":1507555200,"BroadcastType":2,"HeadUrl":"https://img.feihutv.cn/icon/default_headurl.png","LiveStatus":false,"MasterHeadUrl":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","NickName":"vp","ObtainGiftCnt":16,"ObtainGiftId":8,"ObtainHB":30396,"RoomId":30000000,"RoomUserId":11332509,"UserId":"11332488"},{"AwardTime":1507554600,"BroadcastType":2,"HeadUrl":"https://img.feihutv.cn/icon/default_headurl.png","LiveStatus":false,"MasterHeadUrl":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","NickName":"vp","ObtainGiftCnt":6,"ObtainGiftId":2,"ObtainHB":30153,"RoomId":97488866,"RoomUserId":11335615,"UserId":"11332488"},{"AwardTime":1507554000,"BroadcastType":2,"HeadUrl":"https://img.feihutv.cn/icon/default_headurl.png","LiveStatus":false,"MasterHeadUrl":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","NickName":"vp","ObtainGiftCnt":3,"ObtainGiftId":1,"ObtainHB":30129,"RoomId":30000000,"RoomUserId":11332509,"UserId":"11332488"},{"AwardTime":1506500400,"BroadcastType":1,"HeadUrl":"https://img.feihutv.cn//headIcon/11335619/59b5f1972a17e42a34bcc665.png","LiveStatus":false,"MasterHeadUrl":"https://img.feihutv.cn//headIcon/11332615/59bb38a98520874bb17fcf72.jpg","NickName":"AOVHR","ObtainGiftCnt":1834,"ObtainGiftId":8,"ObtainHB":58248,"RoomId":11032618,"RoomUserId":11332615,"UserId":"11335619"},{"AwardTime":1506499800,"BroadcastType":1,"HeadUrl":"https://img.feihutv.cn//headIcon/11335619/59b5f1972a17e42a34bcc665.png","LiveStatus":false,"MasterHeadUrl":"https://img.feihutv.cn//headIcon/11332615/59bb38a98520874bb17fcf72.jpg","NickName":"AOVHR","ObtainGiftCnt":8074,"ObtainGiftId":2,"ObtainHB":150345,"RoomId":11032618,"RoomUserId":11332615,"UserId":"11335619"},{"AwardTime":1506499200,"BroadcastType":1,"HeadUrl":"https://img.feihutv.cn//headIcon/11335619/59b5f1972a17e42a34bcc665.png","LiveStatus":false,"MasterHeadUrl":"https://img.feihutv.cn//headIcon/11332615/59bb38a98520874bb17fcf72.jpg","NickName":"AOVHR","ObtainGiftCnt":9389,"ObtainGiftId":1,"ObtainHB":177045,"RoomId":11032618,"RoomUserId":11332615,"UserId":"11335619"},{"AwardTime":1506478200,"BroadcastType":1,"HeadUrl":"https://img.feihutv.cn//headIcon/11335603/59b78d7a2a17e47ff2678063.jpg","LiveStatus":false,"MasterHeadUrl":"https://img.feihutv.cn//headIcon/11332615/59bb38a98520874bb17fcf72.jpg","NickName":"TK","ObtainGiftCnt":403,"ObtainGiftId":2,"ObtainHB":33868,"RoomId":11032618,"RoomUserId":11332615,"UserId":"11335603"},{"AwardTime":1506477600,"BroadcastType":1,"HeadUrl":"https://img.feihutv.cn//headIcon/11335603/59b78d7a2a17e47ff2678063.jpg","LiveStatus":false,"MasterHeadUrl":"https://img.feihutv.cn//headIcon/11332615/59bb38a98520874bb17fcf72.jpg","NickName":"TK","ObtainGiftCnt":9,"ObtainGiftId":1,"ObtainHB":30064,"RoomId":11032618,"RoomUserId":11332615,"UserId":"11335603"},{"AwardTime":1506414000,"BroadcastType":2,"HeadUrl":"http://img.feihutv.cn//headIcon/11332904/597e9b2b8520871fda62558d.jpg","LiveStatus":false,"MasterHeadUrl":"https://img.feihutv.cn//headIcon/11335603/59b78d7a2a17e47ff2678063.jpg","NickName":"我就测测看","ObtainGiftCnt":9,"ObtainGiftId":8,"ObtainHB":30192,"RoomId":89994666,"RoomUserId":11335603,"UserId":"11332904"},{"AwardTime":1506413400,"BroadcastType":1,"HeadUrl":"http://img.feihutv.cn//headIcon/11332904/597e9b2b8520871fda62558d.jpg","LiveStatus":false,"MasterHeadUrl":"https://img.feihutv.cn/icon/default_headurl.png","NickName":"我就测测看","ObtainGiftCnt":10,"ObtainGiftId":2,"ObtainHB":30096,"RoomId":99999999,"RoomUserId":11332621,"UserId":"11332904"},{"AwardTime":1506412800,"BroadcastType":1,"HeadUrl":"http://img.feihutv.cn//headIcon/11332904/597e9b2b8520871fda62558d.jpg","LiveStatus":false,"MasterHeadUrl":"https://img.feihutv.cn/icon/default_headurl.png","NickName":"我就测测看","ObtainGiftCnt":18,"ObtainGiftId":1,"ObtainHB":30129,"RoomId":99999999,"RoomUserId":11332621,"UserId":"11332904"},{"AwardTime":1506326400,"BroadcastType":1,"HeadUrl":"https://img.feihutv.cn//headIcon/11335595/59b8f9ce8520877a2032b26f.jpg","LiveStatus":false,"MasterHeadUrl":"https://img.feihutv.cn//headIcon/11332615/59bb38a98520874bb17fcf72.jpg","NickName":"Echo","ObtainGiftCnt":14,"ObtainGiftId":1,"ObtainHB":30100,"RoomId":11032618,"RoomUserId":11332615,"UserId":"11335595"},{"AwardTime":1506154800,"BroadcastType":2,"HeadUrl":"https://q.qlogo.cn/qqapp/1106077402/14C5B801179E1254D2F0E4C36ED26C54/100","LiveStatus":false,"MasterHeadUrl":"https://img.feihutv.cn//headIcon/11335608/59b6499b852087500e18d019.png","NickName":"SilenceS","ObtainGiftCnt":2,"ObtainGiftId":8,"ObtainHB":30024,"RoomId":66668666,"RoomUserId":11335608,"UserId":"11336192"}]
         * NextOffset : 706
         */

        @Expose
        @SerializedName("NextOffset")
        private int nextOffset;

        @Expose
        @SerializedName("List")
        private List<ListResponseData> mList;

        public int getNextOffset() {
            return nextOffset;
        }

        public void setNextOffset(int nextOffset) {
            this.nextOffset = nextOffset;
        }

        public List<ListResponseData> getList() {
            return mList;
        }

        public void setList(List<ListResponseData> list) {
            mList = list;
        }

        public static class ListResponseData {
            /**
             * AwardTime : 1508850000
             * BroadcastType : 2
             * HeadUrl : https://img.feihutv.cn/icon/default_headurl.png
             * LiveStatus : false
             * MasterHeadUrl : http://img.feihutv.cn//headIcon/11332511/59647cf98520875f1395468a.png
             * NickName : rxpg
             * ObtainGiftCnt : 1
             * ObtainGiftId : 1
             * ObtainHB : 30007
             * RoomId : 11032512
             * RoomUserId : 11332511
             * UserId : 11336205
             */

            @Expose
            @SerializedName("AwardTime")
            private long awardTime;
            @Expose
            @SerializedName("BroadcastType")
            private int broadcastType;
            @Expose
            @SerializedName("HeadUrl")
            private String headUrl;
            @Expose
            @SerializedName("LiveStatus")
            private boolean liveStatus;
            @Expose
            @SerializedName("MasterHeadUrl")
            private String masterHeadUrl;
            @Expose
            @SerializedName("NickName")
            private String nickName;
            @Expose
            @SerializedName("ObtainGiftCnt")
            private int obtainGiftCnt;
            @Expose
            @SerializedName("ObtainGiftId")
            private int obtainGiftId;
            @Expose
            @SerializedName("ObtainHB")
            private long obtainHB;
            @Expose
            @SerializedName("RoomId")
            private int roomId;
            @Expose
            @SerializedName("RoomUserId")
            private String roomUserId;
            @Expose
            @SerializedName("UserId")
            private String userId;

            @Expose
            @SerializedName("Vip")
            private int vip;

            @Expose
            @SerializedName("VipExpired")
            private boolean vipExpired;

            @Expose
            @SerializedName("MasterNickname")
            private String masterNickname;

            public int getVip() {
                return vip;
            }

            public void setVip(int vip) {
                this.vip = vip;
            }

            public boolean isVipExpired() {
                return vipExpired;
            }

            public void setVipExpired(boolean vipExpired) {
                this.vipExpired = vipExpired;
            }

            public long getAwardTime() {
                return awardTime;
            }

            public void setAwardTime(long awardTime) {
                this.awardTime = awardTime;
            }

            public int getBroadcastType() {
                return broadcastType;
            }

            public void setBroadcastType(int broadcastType) {
                this.broadcastType = broadcastType;
            }

            public String getHeadUrl() {
                return headUrl;
            }

            public void setHeadUrl(String headUrl) {
                this.headUrl = headUrl;
            }

            public boolean isLiveStatus() {
                return liveStatus;
            }

            public void setLiveStatus(boolean liveStatus) {
                this.liveStatus = liveStatus;
            }

            public String getMasterHeadUrl() {
                return masterHeadUrl;
            }

            public void setMasterHeadUrl(String masterHeadUrl) {
                this.masterHeadUrl = masterHeadUrl;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public int getObtainGiftCnt() {
                return obtainGiftCnt;
            }

            public void setObtainGiftCnt(int obtainGiftCnt) {
                this.obtainGiftCnt = obtainGiftCnt;
            }

            public int getObtainGiftId() {
                return obtainGiftId;
            }

            public void setObtainGiftId(int obtainGiftId) {
                this.obtainGiftId = obtainGiftId;
            }

            public long getObtainHB() {
                return obtainHB;
            }

            public void setObtainHB(long obtainHB) {
                this.obtainHB = obtainHB;
            }

            public int getRoomId() {
                return roomId;
            }

            public void setRoomId(int roomId) {
                this.roomId = roomId;
            }


            public String getRoomUserId() {
                return roomUserId;
            }

            public void setRoomUserId(String roomUserId) {
                this.roomUserId = roomUserId;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getMasterNickname() {
                return masterNickname;
            }

            public void setMasterNickname(String masterNickname) {
                this.masterNickname = masterNickname;
            }
        }
    }
}
