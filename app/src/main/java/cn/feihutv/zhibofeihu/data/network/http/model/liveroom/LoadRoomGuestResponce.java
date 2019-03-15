package cn.feihutv.zhibofeihu.data.network.http.model.liveroom;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * Created by huanghao on 2017/10/18.
 */

public class LoadRoomGuestResponce extends BaseResponse{
    @Expose
    @SerializedName("Data")
    private RoomGuestData roomGuestData;

    public RoomGuestData getRoomGuestData() {
        return roomGuestData;
    }

    public void setRoomGuestData(RoomGuestData roomGuestData) {
        this.roomGuestData = roomGuestData;
    }


    public static class RoomGuestData{
        @Expose
        @SerializedName("GuestCnt")
        private int GuestCnt;

        public int getGuestCnt() {
            return GuestCnt;
        }

        public void setGuestCnt(int guestCnt) {
            GuestCnt = guestCnt;
        }
    }
}
