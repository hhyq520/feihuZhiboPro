package cn.feihutv.zhibofeihu.data.network.http.model.home;

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
 * Created by chenliwen on 2017/10/10 19:33.
 * 直播列表response
 */

public class LoadRoomListResponse extends BaseResponse {
    /**
     * Data : {"List":[{"BroadcastType":2,"GameOwner":"none","HeadUrl":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","LiveCover":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","Location":"","NickName":"哈哈1","OnlineUserCnt":0,"RoomId":"11335615","RoomName":"啦啦啦啦老K","RoomStatus":false},{"BroadcastType":0,"GameOwner":"none","HeadUrl":"https://img.feihutv.cn//headIcon/11332615/59bb38a98520874bb17fcf72.jpg","LiveCover":"https://img.feihutv.cn//cover/11332615/59bb38ba8520874bb17fcf73.jpg","Location":"","NickName":"皮皮情","OnlineUserCnt":0,"RoomId":"11332615","RoomName":"","RoomStatus":false},{"BroadcastType":2,"GameOwner":"none","HeadUrl":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","LiveCover":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","Location":"","NickName":"骑行者","OnlineUserCnt":0,"RoomId":"11335867","RoomName":"佳人苑","RoomStatus":false},{"BroadcastType":2,"GameOwner":"none","HeadUrl":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","LiveCover":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","Location":"","NickName":"whox","OnlineUserCnt":0,"RoomId":"11335609","RoomName":"哈哈哈","RoomStatus":false},{"BroadcastType":2,"GameOwner":"none","HeadUrl":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","LiveCover":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","Location":"","NickName":"idcs","OnlineUserCnt":0,"RoomId":"11332509","RoomName":"哦","RoomStatus":false},{"BroadcastType":2,"GameOwner":"none","HeadUrl":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","LiveCover":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","Location":"","NickName":"zlew","OnlineUserCnt":0,"RoomId":"11335620","RoomName":"6666","RoomStatus":false},{"BroadcastType":2,"GameOwner":"none","HeadUrl":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","LiveCover":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","Location":"","NickName":"周润发","OnlineUserCnt":0,"RoomId":"11335652","RoomName":"fjf","RoomStatus":false},{"BroadcastType":2,"GameOwner":"none","HeadUrl":"https://img.feihutv.cn//headIcon/11335635/59b3b0322a17e454ac71bcee.png","LiveCover":"https://img.feihutv.cn//headIcon/11335635/59b3b042852087500e18d00b.png","Location":"","NickName":"天神","OnlineUserCnt":0,"RoomId":"11335635","RoomName":"会吃醋","RoomStatus":false},{"BroadcastType":2,"GameOwner":"none","HeadUrl":"https://img.feihutv.cn//headIcon/11332498/59b2834f85208766441a0c0b.png","LiveCover":"https://img.feihutv.cn//headIcon/11332498/59b282952a17e4636c35dbc9.png","Location":"广东省 深圳市","NickName":"周周周周周","OnlineUserCnt":0,"RoomId":"11332498","RoomName":"测试","RoomStatus":false},{"BroadcastType":2,"GameOwner":"none","HeadUrl":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","LiveCover":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","Location":"","NickName":"59a7f21f","OnlineUserCnt":0,"RoomId":"11335614","RoomName":"gjgm","RoomStatus":false},{"BroadcastType":2,"GameOwner":"none","HeadUrl":"https://img.feihutv.cn//headIcon/11335603/59b78d7a2a17e47ff2678063.jpg","LiveCover":"https://img.feihutv.cn//cover/11335603/59bb39472a17e462a9e3e07b.jpg","Location":"","NickName":"TK","OnlineUserCnt":0,"RoomId":"11335603","RoomName":"哈哈哈","RoomStatus":false},{"BroadcastType":2,"GameOwner":"none","HeadUrl":"http://img.feihutv.cn//headIcon/11332492/596347c785208734804cc29c.png","LiveCover":"http://img.feihutv.cn//headIcon/11332492/596347a885208734804cc29b.png","Location":"","NickName":"❤你没道理","OnlineUserCnt":0,"RoomId":"11332492","RoomName":"关注","RoomStatus":false},{"BroadcastType":2,"GameOwner":"none","HeadUrl":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","LiveCover":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","Location":"","NickName":"东南西北中发白","OnlineUserCnt":0,"RoomId":"11335622","RoomName":"海兽祭祀","RoomStatus":false},{"BroadcastType":2,"GameOwner":"none","HeadUrl":"http://img.feihutv.cn//headIcon/11332511/59647cf98520875f1395468a.png","LiveCover":"http://img.feihutv.cn//headIcon/11332511/59642d692a17e474709d8f46.png","Location":"","NickName":"追 忆","OnlineUserCnt":0,"RoomId":"11332511","RoomName":"啦啦啦","RoomStatus":false},{"BroadcastType":1,"GameOwner":"none","HeadUrl":"http://img.feihutv.cn//headIcon/11332534/596470ee8520872d66294e47.png","LiveCover":"http://img.feihutv.cn//headIcon/11332534/596470dc8520872d66294e46.png","Location":"","NickName":"飞虎大队长","OnlineUserCnt":0,"RoomId":"11332534","RoomName":"哈哈哈","RoomStatus":false},{"BroadcastType":2,"GameOwner":"none","HeadUrl":"https://img.feihutv.cn//headIcon/11335605/59c0935185208751f7f9473d.png","LiveCover":"https://img.feihutv.cn//headIcon/11335605/59c0938285208751f7f9473e.png","Location":"","NickName":"蚊爷","OnlineUserCnt":0,"RoomId":"11335605","RoomName":"过分","RoomStatus":false},{"BroadcastType":2,"GameOwner":"none","HeadUrl":"https://q.qlogo.cn/qqapp/1106077402/8B9C2D593275D7BB14B10A0F5A12B200/100","LiveCover":"","Location":"","NickName":"🐦","OnlineUserCnt":0,"RoomId":"11336082","RoomName":"睡觉了","RoomStatus":false},{"BroadcastType":1,"GameOwner":"none","HeadUrl":"https://img.feihutv.cn/icon/default_headurl.png","LiveCover":"","Location":"","NickName":"wij","OnlineUserCnt":0,"RoomId":"11336080","RoomName":"1925","RoomStatus":false},{"BroadcastType":2,"GameOwner":"none","HeadUrl":"https://img.feihutv.cn/icon/default_headurl.png","LiveCover":"","Location":"广东省深圳市","NickName":"SilenceY","OnlineUserCnt":0,"RoomId":"11335863","RoomName":"刚刚好","RoomStatus":false},{"BroadcastType":2,"GameOwner":"none","HeadUrl":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","LiveCover":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","Location":"","NickName":"59b0fb81","OnlineUserCnt":0,"RoomId":"11335858","RoomName":"1111","RoomStatus":false}],"NextOffset":20}
     */

    @Expose
    @SerializedName("Data")
    private LoadRoomListResponseData mLoadRoomListResponseData;


    public LoadRoomListResponseData getmLoadRoomListResponseData() {
        return mLoadRoomListResponseData;
    }

    public void setmLoadRoomListResponseData(LoadRoomListResponseData mLoadRoomListResponseData) {
        this.mLoadRoomListResponseData = mLoadRoomListResponseData;
    }


    public static class LoadRoomListResponseData {
        /**
         * List : [{"BroadcastType":2,"GameOwner":"none","HeadUrl":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","LiveCover":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","Location":"","NickName":"哈哈1","OnlineUserCnt":0,"RoomId":"11335615","RoomName":"啦啦啦啦老K","RoomStatus":false},{"BroadcastType":0,"GameOwner":"none","HeadUrl":"https://img.feihutv.cn//headIcon/11332615/59bb38a98520874bb17fcf72.jpg","LiveCover":"https://img.feihutv.cn//cover/11332615/59bb38ba8520874bb17fcf73.jpg","Location":"","NickName":"皮皮情","OnlineUserCnt":0,"RoomId":"11332615","RoomName":"","RoomStatus":false},{"BroadcastType":2,"GameOwner":"none","HeadUrl":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","LiveCover":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","Location":"","NickName":"骑行者","OnlineUserCnt":0,"RoomId":"11335867","RoomName":"佳人苑","RoomStatus":false},{"BroadcastType":2,"GameOwner":"none","HeadUrl":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","LiveCover":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","Location":"","NickName":"whox","OnlineUserCnt":0,"RoomId":"11335609","RoomName":"哈哈哈","RoomStatus":false},{"BroadcastType":2,"GameOwner":"none","HeadUrl":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","LiveCover":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","Location":"","NickName":"idcs","OnlineUserCnt":0,"RoomId":"11332509","RoomName":"哦","RoomStatus":false},{"BroadcastType":2,"GameOwner":"none","HeadUrl":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","LiveCover":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","Location":"","NickName":"zlew","OnlineUserCnt":0,"RoomId":"11335620","RoomName":"6666","RoomStatus":false},{"BroadcastType":2,"GameOwner":"none","HeadUrl":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","LiveCover":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","Location":"","NickName":"周润发","OnlineUserCnt":0,"RoomId":"11335652","RoomName":"fjf","RoomStatus":false},{"BroadcastType":2,"GameOwner":"none","HeadUrl":"https://img.feihutv.cn//headIcon/11335635/59b3b0322a17e454ac71bcee.png","LiveCover":"https://img.feihutv.cn//headIcon/11335635/59b3b042852087500e18d00b.png","Location":"","NickName":"天神","OnlineUserCnt":0,"RoomId":"11335635","RoomName":"会吃醋","RoomStatus":false},{"BroadcastType":2,"GameOwner":"none","HeadUrl":"https://img.feihutv.cn//headIcon/11332498/59b2834f85208766441a0c0b.png","LiveCover":"https://img.feihutv.cn//headIcon/11332498/59b282952a17e4636c35dbc9.png","Location":"广东省 深圳市","NickName":"周周周周周","OnlineUserCnt":0,"RoomId":"11332498","RoomName":"测试","RoomStatus":false},{"BroadcastType":2,"GameOwner":"none","HeadUrl":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","LiveCover":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","Location":"","NickName":"59a7f21f","OnlineUserCnt":0,"RoomId":"11335614","RoomName":"gjgm","RoomStatus":false},{"BroadcastType":2,"GameOwner":"none","HeadUrl":"https://img.feihutv.cn//headIcon/11335603/59b78d7a2a17e47ff2678063.jpg","LiveCover":"https://img.feihutv.cn//cover/11335603/59bb39472a17e462a9e3e07b.jpg","Location":"","NickName":"TK","OnlineUserCnt":0,"RoomId":"11335603","RoomName":"哈哈哈","RoomStatus":false},{"BroadcastType":2,"GameOwner":"none","HeadUrl":"http://img.feihutv.cn//headIcon/11332492/596347c785208734804cc29c.png","LiveCover":"http://img.feihutv.cn//headIcon/11332492/596347a885208734804cc29b.png","Location":"","NickName":"❤你没道理","OnlineUserCnt":0,"RoomId":"11332492","RoomName":"关注","RoomStatus":false},{"BroadcastType":2,"GameOwner":"none","HeadUrl":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","LiveCover":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","Location":"","NickName":"东南西北中发白","OnlineUserCnt":0,"RoomId":"11335622","RoomName":"海兽祭祀","RoomStatus":false},{"BroadcastType":2,"GameOwner":"none","HeadUrl":"http://img.feihutv.cn//headIcon/11332511/59647cf98520875f1395468a.png","LiveCover":"http://img.feihutv.cn//headIcon/11332511/59642d692a17e474709d8f46.png","Location":"","NickName":"追 忆","OnlineUserCnt":0,"RoomId":"11332511","RoomName":"啦啦啦","RoomStatus":false},{"BroadcastType":1,"GameOwner":"none","HeadUrl":"http://img.feihutv.cn//headIcon/11332534/596470ee8520872d66294e47.png","LiveCover":"http://img.feihutv.cn//headIcon/11332534/596470dc8520872d66294e46.png","Location":"","NickName":"飞虎大队长","OnlineUserCnt":0,"RoomId":"11332534","RoomName":"哈哈哈","RoomStatus":false},{"BroadcastType":2,"GameOwner":"none","HeadUrl":"https://img.feihutv.cn//headIcon/11335605/59c0935185208751f7f9473d.png","LiveCover":"https://img.feihutv.cn//headIcon/11335605/59c0938285208751f7f9473e.png","Location":"","NickName":"蚊爷","OnlineUserCnt":0,"RoomId":"11335605","RoomName":"过分","RoomStatus":false},{"BroadcastType":2,"GameOwner":"none","HeadUrl":"https://q.qlogo.cn/qqapp/1106077402/8B9C2D593275D7BB14B10A0F5A12B200/100","LiveCover":"","Location":"","NickName":"🐦","OnlineUserCnt":0,"RoomId":"11336082","RoomName":"睡觉了","RoomStatus":false},{"BroadcastType":1,"GameOwner":"none","HeadUrl":"https://img.feihutv.cn/icon/default_headurl.png","LiveCover":"","Location":"","NickName":"wij","OnlineUserCnt":0,"RoomId":"11336080","RoomName":"1925","RoomStatus":false},{"BroadcastType":2,"GameOwner":"none","HeadUrl":"https://img.feihutv.cn/icon/default_headurl.png","LiveCover":"","Location":"广东省深圳市","NickName":"SilenceY","OnlineUserCnt":0,"RoomId":"11335863","RoomName":"刚刚好","RoomStatus":false},{"BroadcastType":2,"GameOwner":"none","HeadUrl":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","LiveCover":"https://img.feihutv.cn/uploadHtml/images/roomBg.png","Location":"","NickName":"59b0fb81","OnlineUserCnt":0,"RoomId":"11335858","RoomName":"1111","RoomStatus":false}]
         * NextOffset : 20
         */



        @Expose
        @SerializedName("NextOffset")
        private int nextOffset;

        @Expose
        @SerializedName("List")
        private List<ListData> list;

        @Override
        public String toString() {
            return "LoadRoomListResponseData{" +
                    "nextOffset=" + nextOffset +
                    ", list=" + list +
                    '}';
        }

        public int getNextOffset() {
            return nextOffset;
        }

        public void setNextOffset(int nextOffset) {
            this.nextOffset = nextOffset;
        }

        public List<ListData> getList() {
            return list;
        }

        public void setList(List<ListData> list) {
            this.list = list;
        }

        public static class ListData {
            /**
             * BroadcastType : 2
             * GameOwner : none
             * HeadUrl : https://img.feihutv.cn/uploadHtml/images/roomBg.png
             * LiveCover : https://img.feihutv.cn/uploadHtml/images/roomBg.png
             * Location :
             * NickName : 哈哈1
             * OnlineUserCnt : 0
             * RoomId : 11335615
             * RoomName : 啦啦啦啦老K
             * RoomStatus : false
             */

            @Expose
            @SerializedName("BroadcastType")
            private int broadcastType;

            @Expose
            @SerializedName("GameOwner")
            private String gameOwner;

            @Expose
            @SerializedName("HeadUrl")
            private String headUrl;

            @Expose
            @SerializedName("LiveCover")
            private String liveCover;

            @Expose
            @SerializedName("Location")
            private String location;
            @Expose
            @SerializedName("NickName")
            private String nickName;
            @Expose
            @SerializedName("OnlineUserCnt")
            private String onlineUserCnt;
            @Expose
            @SerializedName("RoomId")
            private String roomId;
            @Expose
            @SerializedName("RoomName")
            private String roomName;
            @Expose
            @SerializedName("RoomStatus")
            private boolean roomStatus;

            public int getBroadcastType() {
                return broadcastType;
            }

            public void setBroadcastType(int broadcastType) {
                this.broadcastType = broadcastType;
            }

            public String getGameOwner() {
                return gameOwner;
            }

            public void setGameOwner(String gameOwner) {
                this.gameOwner = gameOwner;
            }

            public String getHeadUrl() {
                return headUrl;
            }

            public void setHeadUrl(String headUrl) {
                this.headUrl = headUrl;
            }

            public String getLiveCover() {
                return liveCover;
            }

            public void setLiveCover(String liveCover) {
                this.liveCover = liveCover;
            }

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getOnlineUserCnt() {
                return onlineUserCnt;
            }

            public void setOnlineUserCnt(String onlineUserCnt) {
                this.onlineUserCnt = onlineUserCnt;
            }

            public String getRoomId() {
                return roomId;
            }

            public void setRoomId(String roomId) {
                this.roomId = roomId;
            }

            public String getRoomName() {
                return roomName;
            }

            public void setRoomName(String roomName) {
                this.roomName = roomName;
            }

            public boolean isRoomStatus() {
                return roomStatus;
            }

            public void setRoomStatus(boolean roomStatus) {
                this.roomStatus = roomStatus;
            }

            @Override
            public String toString() {
                return "ListData{" +
                        "broadcastType=" + broadcastType +
                        ", gameOwner='" + gameOwner + '\'' +
                        ", headUrl='" + headUrl + '\'' +
                        ", liveCover='" + liveCover + '\'' +
                        ", location='" + location + '\'' +
                        ", nickName='" + nickName + '\'' +
                        ", onlineUserCnt='" + onlineUserCnt + '\'' +
                        ", roomId='" + roomId + '\'' +
                        ", roomName='" + roomName + '\'' +
                        ", roomStatus=" + roomStatus +
                        '}';
            }
        }
    }
}
