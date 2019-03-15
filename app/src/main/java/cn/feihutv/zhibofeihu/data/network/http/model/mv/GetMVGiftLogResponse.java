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
public class GetMVGiftLogResponse extends BaseResponse {

    @Expose
    @SerializedName("Data")
    private  GetMVGiftLogData mGetMVGiftLogData;

    public GetMVGiftLogData getGetMVGiftLogData() {
        return mGetMVGiftLogData;
    }

    public void setGetMVGiftLogData(GetMVGiftLogData getMVGiftLogData) {
        mGetMVGiftLogData = getMVGiftLogData;
    }

    public static class GetMVGiftLogData{

        @Expose
        @SerializedName("NextOffset")
        private  String nextOffset;

        @Expose
        @SerializedName("List")
        private List<GetMVGiftLog> mGetMVGiftLogs;

        public String getNextOffset() {
            return nextOffset;
        }

        public void setNextOffset(String nextOffset) {
            this.nextOffset = nextOffset;
        }

        public List<GetMVGiftLog> getGetMVGiftLogs() {
            return mGetMVGiftLogs;
        }

        public void setGetMVGiftLogs(List<GetMVGiftLog> getMVGiftLogs) {
            mGetMVGiftLogs = getMVGiftLogs;
        }
    }





    public static class GetMVGiftLog{

        @SerializedName("Avatar")
        private  String avatar;// "https://img..png", //赠礼人头像

        @SerializedName("GiftCnt")
        private  String giftCnt;// 1,   //礼物数量

        @SerializedName("GiftId")
        private  String giftId;// 1,      //礼物id


        private transient String giftName;

        @SerializedName("HB")
        private  String hB;// 60,       //主播收入虎币数

        @SerializedName("Nickname")
        private  String nickname;// "admin", //赠礼人昵称

        @SerializedName("T")
        private  Long t;// 1510392269,       //赠礼时间

        @SerializedName("Uid")
        private  String uid;// "29"                  //赠礼人uid


        public String getGiftName() {
            return giftName;
        }

        public void setGiftName(String giftName) {
            this.giftName = giftName;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getGiftCnt() {
            return giftCnt;
        }

        public void setGiftCnt(String giftCnt) {
            this.giftCnt = giftCnt;
        }

        public String getGiftId() {
            return giftId;
        }

        public void setGiftId(String giftId) {
            this.giftId = giftId;
        }

        public String gethB() {
            return hB;
        }

        public void sethB(String hB) {
            this.hB = hB;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public Long getT() {
            return t;
        }

        public void setT(Long t) {
            this.t = t;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }
    }
}
